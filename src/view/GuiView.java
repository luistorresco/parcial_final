package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import model.Predio;

/**
 * Vista del Sistema Catastral implementando una Interfaz Gráfica de Usuario (GUI).
 * 
 * Arquitectura MVC: VISTA.
 * Desarrollado con Swing utilizando prácticas modernas y diseño limpio.
 */
public class GuiView {
    private JFrame frame;
    private JTable table;
    private PredioTableModel tableModel;
    
    private JComboBox<String> cmbCriterio;
    private JLabel lblTimer;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JLabel lblStatus;

    public GuiView() {
        inicializarComponentes();
    }

    /**
     * Construye y configura toda la ventana de la aplicación.
     */
    private void inicializarComponentes() {
        frame = new JFrame("Ordenamiento Predios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // --- PANEL SUPERIOR DE CONTROLES ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.setBackground(Color.WHITE);

        // Sub-panel izquierdo (Criterio y Cronómetro)
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftControls.setBackground(Color.WHITE);

        cmbCriterio = new JComboBox<>(new String[]{"NPN", "Municipio", "Dirección", "Ficha"});
        cmbCriterio.setPreferredSize(new Dimension(140, 35));
        cmbCriterio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        leftControls.add(cmbCriterio);

        // Separador estético (Play/Flecha y Cronómetro)
        JLabel lblPlay = new JLabel("▶");
        lblPlay.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPlay.setForeground(new Color(120, 120, 120));
        leftControls.add(lblPlay);

        lblTimer = new JLabel("00:00:00.000");
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 16));
        lblTimer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        leftControls.add(lblTimer);

        topPanel.add(leftControls, BorderLayout.WEST);

        // Sub-panel derecho (Buscador con ícono)
        JPanel rightControls = new JPanel(new BorderLayout(5, 0));
        rightControls.setBackground(Color.WHITE);

        btnSearch = new JButton("🔍");
        btnSearch.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnSearch.setPreferredSize(new Dimension(50, 35));
        btnSearch.setBackground(new Color(245, 245, 245));
        btnSearch.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        btnSearch.setFocusPainted(false);
        rightControls.add(btnSearch, BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 35));
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        rightControls.add(txtSearch, BorderLayout.CENTER);

        topPanel.add(rightControls, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);

        // --- TABLA DE DATOS CENTRAL ---
        tableModel = new PredioTableModel(new ArrayList<>());
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 230, 230));

        // Cabecera estilizada
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.DARK_GRAY);

        // Alinear columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // #
        table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);   // NPN
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);   // Municipio
        table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);   // Dirección
        table.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);   // Ficha

        // Ajustar anchos relativos
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(80);   // #
        colModel.getColumn(1).setPreferredWidth(320);  // NPN
        colModel.getColumn(2).setPreferredWidth(200);  // Municipio
        colModel.getColumn(3).setPreferredWidth(450);  // Dirección
        colModel.getColumn(4).setPreferredWidth(150);  // Ficha

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)));
        frame.add(scrollPane, BorderLayout.CENTER);

        // --- BARRA DE ESTADO INFERIOR ---
        lblStatus = new JLabel(" Cargando base de datos catastral...");
        lblStatus.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblStatus.setPreferredSize(new Dimension(frame.getWidth(), 25));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        frame.add(lblStatus, BorderLayout.SOUTH);
    }

    /**
     * Muestra la ventana de la aplicación.
     */
    public void mostrar() {
        frame.setVisible(true);
    }

    /**
     * Establece los datos iniciales o actualizados de la tabla.
     */
    public void setDatos(List<Predio> lista) {
        tableModel.setPredios(lista);
    }

    /**
     * Retorna el criterio de ordenamiento/búsqueda actualmente seleccionado.
     */
    public String getCriterioSeleccionado() {
        return (String) cmbCriterio.getSelectedItem();
    }

    /**
     * Retorna el texto ingresado en la barra de búsqueda.
     */
    public String getTextoBusqueda() {
        return txtSearch.getText().trim();
    }

    /**
     * Actualiza el cronómetro de tiempo en formato HH:mm:ss.SSS.
     * 
     * @param nanosegundos Tiempo transcurrido
     */
    public void setTiempoEjecucion(long nanosegundos) {
        long milisegundos = nanosegundos / 1_000_000;
        long ms = milisegundos % 1000;
        long totalSegundos = milisegundos / 1000;
        long s = totalSegundos % 60;
        long totalMinutos = totalSegundos / 60;
        long m = totalMinutos % 60;
        long h = totalMinutos / 60;

        String formatted = String.format("%02d:%02d:%02d.%03d", h, m, s, ms);
        lblTimer.setText(formatted);
    }

    /**
     * Registra un escuchador de eventos para el cambio de criterio en el combobox.
     */
    public void addCriterioChangeListener(ActionListener listener) {
        cmbCriterio.addActionListener(listener);
    }

    /**
     * Registra un escuchador de eventos para el botón de búsqueda y el Enter en la barra de texto.
     */
    public void addSearchListener(ActionListener listener) {
        btnSearch.addActionListener(listener);
        txtSearch.addActionListener(listener); // Permite ejecutar al presionar ENTER
    }

    /**
     * Selecciona y desplaza la tabla hacia los registros coincidentes.
     * 
     * @param inicio Índice del primer registro coincidente
     * @param fin    Índice del último registro coincidente
     */
    public void seleccionarRango(int inicio, int fin) {
        table.getSelectionModel().setSelectionInterval(inicio, fin);
        
        // Desplazar visualmente (scroll) hasta el primer elemento del rango
        table.scrollRectToVisible(table.getCellRect(inicio, 0, true));
        
        lblStatus.setText(String.format(" Coincidencias encontradas: %d registros seleccionados [Líneas %d - %d].", 
                (fin - inicio + 1), inicio, fin));
    }

    /**
     * Limpia cualquier selección previa de la tabla.
     */
    public void limpiarSeleccion() {
        table.clearSelection();
    }

    /**
     * Actualiza el mensaje en la barra de estado.
     */
    public void setStatus(String mensaje) {
        lblStatus.setText(" " + mensaje);
    }

    /**
     * Muestra un mensaje emergente de advertencia o información.
     */
    public void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de error.
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error Catastral", JOptionPane.ERROR_MESSAGE);
    }

    // --- SUB-CLASE INTERNA: TABLE MODEL ---
    private static class PredioTableModel extends AbstractTableModel {
        private final String[] columnNames = {"#", "NPN", "Municipio", "Dirección", "Ficha"};
        private List<Predio> predios;

        public PredioTableModel(List<Predio> predios) {
            this.predios = predios;
        }

        public void setPredios(List<Predio> predios) {
            this.predios = predios;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return predios.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (row < 0 || row >= predios.size()) return null;
            Predio p = predios.get(row);
            switch (col) {
                case 0:
                    return row; // Índice de fila coincidiendo con el comportamiento de la foto (42805, 42806, etc.)
                case 1:
                    return p.getNpn();
                case 2:
                    return p.getMunicipio();
                case 3:
                    return p.getDireccion();
                case 4:
                    return p.getNumeroFicha();
                default:
                    return null;
            }
        }
    }
}
