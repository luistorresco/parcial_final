package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import model.Predio;
import view.GuiView;
import util.CsvReader;
import util.QuickSortHoare;
import util.BinarySearchUtil;
import util.TimerUtil;

/**
 * Controlador específico para la Interfaz Gráfica de Usuario (GUI).
 * 
 * Arquitectura MVC: CONTROLADOR.
 * Gestiona los eventos de la vista de Swing y realiza operaciones de ordenamiento
 * y búsqueda en segundo plano con optimización extrema.
 */
public class PredioGuiController {
    private final GuiView view;
    private final ArrayList<Predio> predios;
    private String criterioOrdenadoActual;

    public PredioGuiController(GuiView view) {
        this.view = view;
        this.criterioOrdenadoActual = "";
        
        // 1. Carga automática de registros
        this.predios = CsvReader.leerPredios();
        
        if (predios == null || predios.isEmpty()) {
            view.mostrarError("No se pudo cargar el archivo catastral desde /home/dainkor/Descargas/predios.csv");
            return;
        }

        // 2. Cargar datos iniciales en la vista
        view.setDatos(predios);
        view.setStatus("Base de datos cargada. Total registros: " + predios.size());

        // 3. Registrar escuchadores de eventos
        registrarEventos();

        // 4. Ordenamiento inicial por defecto (NPN)
        ordenarPorCriterio("NPN");
    }

    /**
     * Registra los manejadores de eventos (listeners) para la interacción del usuario.
     */
    private void registrarEventos() {
        // Escuchador para cambios en la selección de criterios (ComboBox)
        view.addCriterioChangeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String criterio = view.getCriterioSeleccionado();
                if (!criterio.equalsIgnoreCase(criterioOrdenadoActual)) {
                    ordenarPorCriterio(criterio);
                }
            }
        });

        // Escuchador para el botón de búsqueda y campo de texto (ENTER)
        view.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarBusqueda();
            }
        });
    }

    /**
     * Ordena físicamente la lista en memoria usando QuickSort Hoare y actualiza la tabla.
     */
    private void ordenarPorCriterio(String criterio) {
        view.setStatus("Ordenando base de datos por [" + criterio + "] usando QuickSort Hoare...");
        
        TimerUtil timer = new TimerUtil();
        timer.start();
        
        // Ejecución de QuickSort con Partición de Hoare
        QuickSortHoare.ordenar(predios, getComparator(criterio));
        
        timer.stop();
        criterioOrdenadoActual = criterio;

        // Actualizar UI
        view.setDatos(predios);
        view.limpiarSeleccion();
        view.setTiempoEjecucion(timer.getElapsedNanoseconds());
        view.setStatus(String.format("Base de datos ordenada por [%s] en %.2f milisegundos.", 
                criterio, timer.getElapsedMilliseconds()));
    }

    /**
     * Realiza la búsqueda binaria con expansión e ilumina/selecciona los registros encontrados.
     */
    private void ejecutarBusqueda() {
        String query = view.getTextoBusqueda();
        String criterio = view.getCriterioSeleccionado();

        if (query.isEmpty()) {
            view.mostrarMensaje("Búsqueda vacía", "Por favor ingrese un término de búsqueda.");
            return;
        }

        view.setStatus("Realizando búsqueda binaria por [" + criterio + "]...");
        view.limpiarSeleccion(); // Limpia selección previa en JTable

        // Garantizar que la lista esté ordenada por el criterio seleccionado
        if (!criterio.equalsIgnoreCase(criterioOrdenadoActual)) {
            ordenarPorCriterio(criterio);
        }

        TimerUtil timerSearch = new TimerUtil();
        timerSearch.start();
        
        // --- 1. EJECUTAR BÚSQUEDA BINARIA PARA RANGO ---
        BinarySearchUtil.RangoBusqueda rango = BinarySearchUtil.buscarRango(predios, query, criterio);
        
        timerSearch.stop();

        view.setTiempoEjecucion(timerSearch.getElapsedNanoseconds());

        if (rango != null) {
            // --- 2. SELECCIONAR Y DESPLAZAR EN LA TABLA ---
            view.seleccionarRango(rango.inicio, rango.fin);
        } else {
            view.limpiarSeleccion();
            view.setStatus(String.format("No se encontraron coincidencias para '%s' en [%s].", query, criterio));
            view.mostrarMensaje("Sin resultados", String.format("No se encontraron registros que comiencen con '%s' bajo el criterio [%s].", query, criterio));
        }
    }

    /**
     * Retorna el comparador case-insensitive adecuado para cada campo, normalizando los espacios.
     */
    private Comparator<Predio> getComparator(String criterio) {
        switch (criterio.toUpperCase()) {
            case "NPN":
                return (p1, p2) -> BinarySearchUtil.normalizarEspacios(p1.getNpn())
                        .compareToIgnoreCase(BinarySearchUtil.normalizarEspacios(p2.getNpn()));
            case "MUNICIPIO":
                return (p1, p2) -> BinarySearchUtil.normalizarEspacios(p1.getMunicipio())
                        .compareToIgnoreCase(BinarySearchUtil.normalizarEspacios(p2.getMunicipio()));
            case "DIRECCIÓN":
            case "DIRECCION":
                return (p1, p2) -> BinarySearchUtil.normalizarEspacios(p1.getDireccion())
                        .compareToIgnoreCase(BinarySearchUtil.normalizarEspacios(p2.getDireccion()));
            case "FICHA":
                return (p1, p2) -> BinarySearchUtil.normalizarEspacios(p1.getNumeroFicha())
                        .compareToIgnoreCase(BinarySearchUtil.normalizarEspacios(p2.getNumeroFicha()));
            default:
                throw new IllegalArgumentException("Criterio de ordenamiento no soportado: " + criterio);
        }
    }
}
