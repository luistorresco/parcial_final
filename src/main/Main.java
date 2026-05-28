package main;

import controller.PredioGuiController;
import view.GuiView;

/**
 * Clase principal que actúa como punto de entrada (bootstrap) de la aplicación.
 * 
 * Arquitectura MVC: BOOTSTRAP.
 * Inicializa e inicia directamente la Interfaz Gráfica de Usuario (GUI).
 */
public class Main {
    public static void main(String[] args) {
        // Ejecutar en el hilo de Swing Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look & Feel del Sistema para un diseño moderno nativo
                javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {}

            GuiView vistaGui = new GuiView();
            try {
                // Inicializar el controlador de la GUI
                PredioGuiController controladorGui = new PredioGuiController(vistaGui);
                vistaGui.mostrar();
            } catch (Exception e) {
                vistaGui.mostrarError("Error crítico en modo GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
