package view;

import controller.EmpleadoController;
import controller.ReporteController;
import model.Empleado;
import com.itextpdf.text.DocumentException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AdminDashboardView extends JFrame {

    private JButton reporteAtrasosButton;
    private JButton reporteSalidasAnticipadasButton;
    private JButton reporteInasistenciasButton;
    private JButton agregarEmpleadoButton;
    private JButton editarEmpleadoButton;
    private JButton eliminarEmpleadoButton;
    private JButton cerrarSesionButton;
    private JTable usuariosTable;
    private DefaultTableModel tableModel;
    private String nombreAdmin;

    public AdminDashboardView(String nombreAdmin) {
        this.nombreAdmin = nombreAdmin;

        setTitle("Dashboard del Administrador");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(0xF5F5F5)); 
        add(mainPanel);

        JLabel titleLabel = new JLabel("Bienvenido al Dashboard, " + nombreAdmin + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        titleLabel.setForeground(new Color(0x333333)); 
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(0xFFFFFF)); 
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setOpaque(true);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true)); 
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        String[] columnNames = {"ID", "Nombre", "Apellido", "Email", "Administrador"};
        tableModel = new DefaultTableModel(columnNames, 0);
        usuariosTable = new JTable(tableModel);
        usuariosTable.setRowHeight(35); 
        usuariosTable.setFont(new Font("Arial", Font.PLAIN, 16));
        usuariosTable.setGridColor(new Color(0xE0E0E0)); 
        usuariosTable.setSelectionBackground(new Color(0x6C7A89)); 
        usuariosTable.setSelectionForeground(Color.WHITE);

        JTableHeader header = usuariosTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(new Color(0x2C3E50)); 
        header.setForeground(Color.WHITE);

        usuariosTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(0xF9F9F9)); 
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCCC), 1)); 
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBackground(new Color(0xF5F5F5)); 
        centerPanel.add(buttonPanel, BorderLayout.EAST);

        reporteAtrasosButton = crearBoton("Generar Reporte de Atrasos", new Color(0xFF9800), new Color(0xFFC107));
        reporteSalidasAnticipadasButton = crearBoton("Generar Reporte de Salidas Anticipadas", new Color(0x00BCD4), new Color(0x0097A7));
        reporteInasistenciasButton = crearBoton("Generar Reporte de Inasistencias", new Color(0x4CAF50), new Color(0x388E3C));

        buttonPanel.add(reporteAtrasosButton);
        buttonPanel.add(reporteSalidasAnticipadasButton);
        buttonPanel.add(reporteInasistenciasButton);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomPanel.setBackground(new Color(0xF5F5F5));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel crudPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        crudPanel.setBackground(new Color(0xFFFFFF));
        agregarEmpleadoButton = crearBoton("Agregar Empleado", new Color(0x4CAF50), new Color(0x388E3C));
        editarEmpleadoButton = crearBoton("Editar Empleado", new Color(0x2196F3), new Color(0x1976D2));
        eliminarEmpleadoButton = crearBoton("Eliminar Empleado", new Color(0xF44336), new Color(0xD32F2F));

        crudPanel.add(agregarEmpleadoButton);
        crudPanel.add(editarEmpleadoButton);
        crudPanel.add(eliminarEmpleadoButton);

        bottomPanel.add(crudPanel);

        cerrarSesionButton = crearBoton("Cerrar Sesión", new Color(0x9E9E9E), new Color(0x616161));
        bottomPanel.add(cerrarSesionButton);

        cargarUsuarios();

        reporteAtrasosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporteAtrasos();
            }
        });

        reporteSalidasAnticipadasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporteSalidasAnticipadas();
            }
        });

        reporteInasistenciasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporteInasistencias();
            }
        });

        agregarEmpleadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarEmpleado();
            }
        });

        editarEmpleadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarEmpleado();
            }
        });

        eliminarEmpleadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEmpleado();
            }
        });

        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }

    // Método para crear botones con estilo moderno
    private JButton crearBoton(String texto, Color colorInicial, Color colorHover) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(colorInicial);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorHover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorInicial);
            }
        });
        return boton;
    }

   // Método para cargar los usuarios en la tabla
    private void cargarUsuarios() {
        try {
            EmpleadoController empleadoController = new EmpleadoController();
            List<Empleado> empleados = empleadoController.listarEmpleados();

            tableModel.setRowCount(0);  // Limpiar tabla

            for (Empleado empleado : empleados) {
                Object[] rowData = {
                        empleado.getId(),
                        empleado.getNombre(),
                        empleado.getApellido(),
                        empleado.getEmail(),
                        empleado.isAdministrador() ? "Sí" : "No"
                };
                tableModel.addRow(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los usuarios: " + e.getMessage());
        }
    }


// Métodos para CRUD y Reportes
    private void agregarEmpleado() {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String apellido = JOptionPane.showInputDialog("Apellido:");
        String email = JOptionPane.showInputDialog("Email:");
        String password = JOptionPane.showInputDialog("Contraseña:");
        boolean administrador = JOptionPane.showConfirmDialog(null, "¿Es administrador?") == JOptionPane.YES_OPTION;

        try {
            EmpleadoController controller = new EmpleadoController();
            Empleado empleado = new Empleado(0, nombre, apellido, email, password, administrador);
            controller.crearEmpleado(empleado);
            JOptionPane.showMessageDialog(this, "Empleado agregado con éxito.");
            cargarUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar el empleado: " + e.getMessage());
        }
    }

    private void editarEmpleado() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un empleado para editar.");
            return;
        }

        int empleadoId = (int) tableModel.getValueAt(selectedRow, 0);
        String nombre = JOptionPane.showInputDialog("Nombre:", tableModel.getValueAt(selectedRow, 1));
        String apellido = JOptionPane.showInputDialog("Apellido:", tableModel.getValueAt(selectedRow, 2));
        String email = JOptionPane.showInputDialog("Email:", tableModel.getValueAt(selectedRow, 3));
        String password = JOptionPane.showInputDialog("Contraseña:");
        boolean administrador = JOptionPane.showConfirmDialog(null, "¿Es administrador?") == JOptionPane.YES_OPTION;

        try {
            EmpleadoController controller = new EmpleadoController();
            Empleado empleado = new Empleado(empleadoId, nombre, apellido, email, password, administrador);
            controller.editarEmpleado(empleado);
            JOptionPane.showMessageDialog(this, "Empleado editado con éxito.");
            cargarUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al editar el empleado: " + e.getMessage());
        }
    }

    private void eliminarEmpleado() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un empleado para eliminar.");
            return;
        }

        int empleadoId = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            EmpleadoController controller = new EmpleadoController();
            controller.eliminarEmpleado(empleadoId);
            JOptionPane.showMessageDialog(this, "Empleado eliminado con éxito.");
            cargarUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el empleado: " + e.getMessage());
        }
    }

    // Métodos para generar reportes
    private void generarReporteAtrasos() {
        try {
            ReporteController controller = new ReporteController();
            controller.generarReporteAtrasosPDF();
            JOptionPane.showMessageDialog(this, "Reporte de Atrasos generado.");
        } catch (SQLException | DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage());
        }
    }

    private void generarReporteSalidasAnticipadas() {
        try {
            ReporteController controller = new ReporteController();
            controller.generarReporteSalidasAnticipadasPDF();
            JOptionPane.showMessageDialog(this, "Reporte de Salidas Anticipadas generado.");
        } catch (SQLException | DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage());
        }
    }

    private void generarReporteInasistencias() {
        try {
            ReporteController controller = new ReporteController();
            controller.generarReporteInasistenciasPDF(new Date());
            JOptionPane.showMessageDialog(this, "Reporte de Inasistencias generado.");
        } catch (SQLException | DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage());
        }
    }

    // Cerrar sesión
    private void cerrarSesion() {
        new LoginView().setVisible(true);
        dispose();
    }
}




