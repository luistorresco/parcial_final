package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import model.Predio;

/**
 * Utilitario encargado de la lectura y procesamiento del archivo CSV.
 * 
 * Lectura del CSV: Utiliza File, FileReader y BufferedReader obligatoriamente.
 */
public class CsvReader {

    private static final String DEFAULT_PATH = "predios.csv";

    /**
     * Lee el archivo catastral por defecto y retorna la lista de predios.
     * 
     * @return Lista de predios cargados
     */
    public static ArrayList<Predio> leerPredios() {
        return leerPredios(DEFAULT_PATH);
    }

    /**
     * Lee un archivo catastral en la ruta especificada y retorna la lista de predios.
     * Realiza validaciones rigurosas antes de iniciar la lectura.
     * 
     * @param rutaArchivo Ruta absoluta del archivo CSV
     * @return Lista de predios cargados (vacía si ocurre un error)
     */
    public static ArrayList<Predio> leerPredios(String rutaArchivo) {
        ArrayList<Predio> predios = new ArrayList<>();
        File archivo = new File(rutaArchivo);

        // --- VERIFICACIONES PREVIAS ---
        System.out.println("[SISTEMA] Iniciando verificación del archivo catastral...");
        
        if (!archivo.exists()) {
            System.err.println("[ERROR CATASTRAL] El archivo especificado no existe en la ruta: " + rutaArchivo);
            return predios;
        }

        if (!archivo.isFile()) {
            System.err.println("[ERROR CATASTRAL] La ruta no apunta a un archivo válido: " + rutaArchivo);
            return predios;
        }

        if (!archivo.canRead()) {
            System.err.println("[ERROR CATASTRAL] No se tienen permisos de lectura para el archivo: " + rutaArchivo);
            return predios;
        }

        System.out.println("[SISTEMA] Archivo verificado con éxito. Cargando registros...");

        // --- INICIO LECTURA DEL CSV ---
        // Se utilizan FileReader y BufferedReader en un bloque try-with-resources para garantizar el cierre seguro
        try (FileReader fileReader = new FileReader(archivo);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            
            String linea;
            boolean esEncabezado = true;
            long totalLineas = 0;
            long lineasIgnoradas = 0;

            while ((linea = bufferedReader.readLine()) != null) {
                totalLineas++;
                
                // Ignorar líneas vacías
                if (linea.trim().isEmpty()) {
                    lineasIgnoradas++;
                    continue;
                }

                // Omitir la primera línea que corresponde al encabezado (npn,municipio,direccion,ficha)
                if (esEncabezado) {
                    esEncabezado = false;
                    continue;
                }

                // Separar columnas usando coma como delimitador.
                // Como los datos no contienen comillas complejas según el análisis previo, split por coma es suficiente.
                String[] columnas = linea.split(",");

                // Validar estructura de columnas (debe contener npn, municipio, direccion, ficha -> 4 elementos)
                if (columnas.length < 4) {
                    lineasIgnoradas++;
                    continue;
                }

                String npn = columnas[0].trim();
                String municipio = columnas[1].trim();
                String direccion = columnas[2].trim();
                String numeroFicha = columnas[3].trim();

                // Validación simple de datos obligatorios mínimos
                if (npn.isEmpty() || municipio.isEmpty() || direccion.isEmpty() || numeroFicha.isEmpty()) {
                    lineasIgnoradas++;
                    continue;
                }

                // Crear instancia del modelo y agregar al ArrayList
                Predio predio = new Predio(npn, municipio, direccion, numeroFicha);
                predios.add(predio);
            }

            System.out.printf("[SISTEMA] Carga completada. Total líneas analizadas: %d | Registros cargados: %d | Líneas omitidas/inválidas: %d\n",
                    totalLineas, predios.size(), lineasIgnoradas);

        } catch (IOException e) {
            System.err.println("[ERROR CATASTRAL] Ocurrió un error de entrada/salida al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR CRÍTICO] Ocurrió un error inesperado al procesar el archivo CSV: " + e.getMessage());
        }

        return predios;
    }
}
