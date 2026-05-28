package util;

/**
 * Utilitario para medir el tiempo de ejecución de procesos.
 * 
 * Medición de tiempo: Utiliza System.nanoTime() para máxima precisión.
 */
public class TimerUtil {
    private long startTime;
    private long endTime;

    /**
     * Inicia la medición del tiempo.
     */
    public void start() {
        this.startTime = System.nanoTime();
    }

    /**
     * Finaliza la medición del tiempo.
     */
    public void stop() {
        this.endTime = System.nanoTime();
    }

    /**
     * Obtiene el tiempo transcurrido en nanosegundos.
     * 
     * @return tiempo en nanosegundos
     */
    public long getElapsedNanoseconds() {
        return this.endTime - this.startTime;
    }

    /**
     * Obtiene el tiempo transcurrido en milisegundos.
     * 
     * @return tiempo en milisegundos con decimales
     */
    public double getElapsedMilliseconds() {
        return (double) getElapsedNanoseconds() / 1_000_000.0;
    }
}
