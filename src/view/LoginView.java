package view;

import controller.EmpleadoController;
import model.Empleado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginView() {
        setTitle("Bienvenido al Sistema de Asistencia");
        setSize(400, 350);  // Ajuste para acomodar más espacio
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Título
        JLabel titleLabel = new JLabel("Bienvenido al Sistema de Asistencia", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0x0078D7));  // Color azul de la cabecera
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(400, 50));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));  // Se añade una fila más para las etiquetas
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        inputPanel.setBackground(Color.WHITE);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Etiquetas y campos de entrada
        JLabel emailLabel = new JLabel("Correo:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        emailField = new JTextField();  // Campo de texto para correo electrónico
        passwordField = new JPasswordField();  // Campo de texto para contraseña

        // Añadir los campos de entrada y las etiquetas al panel
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Botón de login
        loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(0x0078D7));  // Color azul del botón
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);

        // Añadir el botón centrado
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Acción del botón
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleLogin();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

private void handleLogin() throws SQLException {
    String email = emailField.getText();
    String password = new String(passwordField.getPassword());

    EmpleadoController empleadoController = new EmpleadoController();
    Empleado empleado = empleadoController.login(email, password);

    if (empleado != null) {
        if (empleado.isAdministrador()) {
            new AdminDashboardView(empleado.getNombre()).setVisible(true);
        } else {
            new RegistroAsistenciaView(empleado).setVisible(true);
        }
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}




