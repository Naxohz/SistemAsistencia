package view;

import controller.AsistenciaController;
import model.Empleado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegistroAsistenciaView extends JFrame {
    private AsistenciaController asistenciaController;

    public RegistroAsistenciaView(Empleado empleado) {
        setTitle("Registro de Asistencia");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        asistenciaController = new AsistenciaController();

        // Diseño de la interfaz
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        JLabel titleLabel = new JLabel("Bienvenido, " + empleado.getNombre(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JButton marcarEntradaButton = new JButton("Marcar Entrada");
        JButton marcarSalidaButton = new JButton("Marcar Salida");
        JButton cerrarSesionButton = new JButton("Cerrar Sesión");

        buttonPanel.add(marcarEntradaButton);
        buttonPanel.add(marcarSalidaButton);
        buttonPanel.add(cerrarSesionButton);

        marcarEntradaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    asistenciaController.registrarEntrada(empleado.getId());
                    JOptionPane.showMessageDialog(null, "Entrada registrada.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al registrar la entrada: " + ex.getMessage());
                }
            }
        });

        marcarSalidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    asistenciaController.registrarSalida(empleado.getId());
                    JOptionPane.showMessageDialog(null, "Salida registrada.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al registrar la salida: " + ex.getMessage());
                }
            }
        });

        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginView().setVisible(true);
                dispose();
            }
        });
    }
}


