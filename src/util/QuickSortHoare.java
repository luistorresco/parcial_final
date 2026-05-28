package util;

import java.util.Comparator;
import java.util.List;
import model.Predio;

/**
 * Clase encargada de ordenar colecciones de tipo Predio utilizando el algoritmo QuickSort con partición de Hoare.
 * 
 * QuickSort Hoare: Optimizado para grandes volúmenes de datos con duplicados.
 */
public class QuickSortHoare {

    /**
     * Ordena la lista de predios dada utilizando el comparador especificado.
     * 
     * @param predios    Lista de predios a ordenar.
     * @param comparator Comparador que define el criterio de ordenamiento (por NPN, Municipio, Dirección o Ficha).
     */
    public static void ordenar(List<Predio> predios, Comparator<Predio> comparator) {
        if (predios == null || predios.size() <= 1) {
            return;
        }
        // --- INICIO QUICKSORT ---
        // Punto de entrada público al método recursivo
        quickSort(predios, 0, predios.size() - 1, comparator);
    }

    /**
     * Método recursivo principal del algoritmo QuickSort.
     * Divide la lista en sub-arreglos y los ordena de manera recursiva.
     */
    private static void quickSort(List<Predio> list, int low, int high, Comparator<Predio> comparator) {
        if (low < high) {
            // Se calcula el índice de la partición utilizando el esquema de Hoare
            int p = partition(list, low, high, comparator);
            
            // Recurrir en las dos particiones creadas.
            // Nota importante para la partición de Hoare: 
            // Las llamadas recursivas se realizan sobre [low, p] y [p + 1, high]
            quickSort(list, low, p, comparator);
            quickSort(list, p + 1, high, comparator);
        }
    }

    /**
     * --- IMPLEMENTACIÓN DE LA PARTICIÓN DE HOARE ---
     * 
     * Este esquema es mucho más eficiente que el de Lomuto para conjuntos de datos grandes
     * y con múltiples elementos duplicados, ya que realiza en promedio tres veces menos
     * intercambios (swaps) y divide el arreglo de manera más balanceada.
     * 
     * Utiliza dos índices que avanzan desde los extremos opuestos hacia el centro.
     */
    private static int partition(List<Predio> list, int low, int high, Comparator<Predio> comparator) {
        // Pivote de mediana: Elegimos el elemento central para mitigar el peor caso de arrays ya ordenados
        Predio pivot = list.get(low + (high - low) / 2);
        
        int i = low - 1;
        int j = high + 1;

        while (true) {
            // Mover el índice izquierdo hacia la derecha mientras el elemento actual sea menor al pivote
            do {
                i++;
            } while (comparator.compare(list.get(i), pivot) < 0);

            // Mover el índice derecho hacia la izquierda mientras el elemento actual sea mayor al pivote
            do {
                j--;
            } while (comparator.compare(list.get(j), pivot) > 0);

            // Si los índices se cruzan o coinciden, la partición ha terminado
            if (i >= j) {
                return j;
            }

            // --- CÓMO SE REALIZA EL INTERCAMBIO ---
            // Si i y j no se han cruzado, significa que encontramos dos elementos
            // fuera de orden con respecto al pivote, por lo que realizamos el intercambio (swap).
            swap(list, i, j);
        }
    }

    /**
     * Realiza el intercambio físico de elementos dentro del ArrayList.
     */
    private static void swap(List<Predio> list, int i, int j) {
        Predio temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
