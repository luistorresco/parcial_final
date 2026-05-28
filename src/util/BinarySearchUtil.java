package util;

import java.util.ArrayList;
import java.util.List;
import model.Predio;

/**
 * Utilitario encargado de realizar búsquedas binarias eficientes sobre la lista de predios.
 * Permite búsquedas de coincidencia parcial por prefijo (case-insensitive) y expansiones bilaterales contiguas.
 * 
 * Búsqueda Binaria y Expansión: Optimización O(log N + K).
 */
public class BinarySearchUtil {

    /**
     * Clase que representa el rango de índices (de inicio a fin, inclusivos)
     * de los elementos que coinciden con una búsqueda.
     */
    public static class RangoBusqueda {
        public final int inicio;
        public final int fin;

        public RangoBusqueda(int inicio, int fin) {
            this.inicio = inicio;
            this.fin = fin;
        }

        @Override
        public String toString() {
            return "[" + inicio + ", " + fin + "]";
        }
    }

    /**
     * Realiza una búsqueda binaria para encontrar una coincidencia de prefijo, y luego
     * expande la búsqueda bidireccionalmente para recuperar todas las coincidencias.
     * 
     * @param predios   Lista de predios ordenada bajo el criterio especificado.
     * @param query     Término de búsqueda ingresado por el usuario.
     * @param criterio  Campo sobre el cual se realiza la búsqueda ("NPN", "Municipio", "Dirección", "Ficha").
     * @return Lista de predios que coinciden con el criterio (búsqueda parcial).
     */
    public static List<Predio> buscar(List<Predio> predios, String query, String criterio) {
        List<Predio> resultados = new ArrayList<>();
        RangoBusqueda rango = buscarRango(predios, query, criterio);
        if (rango == null) {
            return resultados;
        }
        
        for (int i = rango.inicio; i <= rango.fin; i++) {
            resultados.add(predios.get(i));
        }
        return resultados;
    }

    /**
     * Realiza una búsqueda binaria y retorna el rango de índices correspondientes a las coincidencias.
     * 
     * @param predios   Lista de predios ordenada bajo el criterio especificado.
     * @param query     Término de búsqueda.
     * @param criterio  Criterio de búsqueda ("NPN", "Municipio", "Dirección", "Ficha").
     * @return Objeto RangoBusqueda con los índices de inicio y fin, o null si no se encontraron coincidencias.
     */
    public static RangoBusqueda buscarRango(List<Predio> predios, String query, String criterio) {
        return buscarRango(predios, query, getExtractor(criterio));
    }

    /**
     * --- CUMPLIMIENTO DE OPEN/CLOSED PRINCIPLE (OCP) ---
     * 
     * Esta sobrecarga es genérica y acepta un extractor funcional 'Function<Predio, String>'.
     * Esto significa que si agregamos nuevos atributos al modelo Predio (o si usáramos otra clase),
     * este algoritmo de búsqueda binaria y expansión NO requiere ninguna modificación.
     * Es completamente abierto a la extensión pero cerrado a la modificación.
     */
    public static RangoBusqueda buscarRango(List<Predio> predios, String query, java.util.function.Function<Predio, String> extractor) {
        if (predios == null || predios.isEmpty() || query == null || query.trim().isEmpty() || extractor == null) {
            return null;
        }

        String queryLower = normalizarEspacios(query).toLowerCase();
        int low = 0;
        int high = predios.size() - 1;
        int indexInicial = -1;

        // --- DÓNDE INICIA BÚSQUEDA BINARIA ---
        while (low <= high) {
            int mid = low + (high - low) / 2;
            Predio predio = predios.get(mid);
            String val = normalizarEspacios(extractor.apply(predio)).toLowerCase();

            // --- DÓNDE OCURRE BÚSQUEDA PARCIAL ---
            if (val.startsWith(queryLower)) {
                indexInicial = mid;
                break;
            }

            if (val.compareTo(queryLower) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        // Si no se encontró ninguna coincidencia
        if (indexInicial == -1) {
            return null;
        }

        // --- DÓNDE OCURRE EXPANSIÓN IZQUIERDA ---
        int izq = indexInicial - 1;
        while (izq >= 0) {
            Predio predio = predios.get(izq);
            String val = normalizarEspacios(extractor.apply(predio)).toLowerCase();
            if (val.startsWith(queryLower)) {
                izq--;
            } else {
                break;
            }
        }

        // --- DÓNDE OCURRE EXPANSIÓN DERECHA ---
        int der = indexInicial + 1;
        while (der < predios.size()) {
            Predio predio = predios.get(der);
            String val = normalizarEspacios(extractor.apply(predio)).toLowerCase();
            if (val.startsWith(queryLower)) {
                der++;
            } else {
                break;
            }
        }

        return new RangoBusqueda(izq + 1, der - 1);
    }

    /**
     * Normaliza un texto reduciendo múltiples espacios en blanco consecutivos a un único espacio.
     * También recorta espacios al inicio y al final (trim).
     * 
     * @param texto Texto a normalizar
     * @return Texto normalizado con espacios simples
     */
    public static String normalizarEspacios(String texto) {
        if (texto == null) return "";
        return texto.trim().replaceAll("\\s+", " ");
    }

    /**
     * Mapea el criterio textual a su correspondiente función extractora en Predio.
     */
    private static java.util.function.Function<Predio, String> getExtractor(String criterio) {
        if (criterio == null) return p -> "";
        switch (criterio.toUpperCase()) {
            case "NPN":
                return Predio::getNpn;
            case "MUNICIPIO":
                return Predio::getMunicipio;
            case "DIRECCIÓN":
            case "DIRECCION":
                return Predio::getDireccion;
            case "FICHA":
            case "NÚMERO DE FICHA":
            case "NUMERO FICHA":
                return Predio::getNumeroFicha;
            default:
                return p -> "";
        }
    }
}
