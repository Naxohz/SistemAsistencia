package controller;

import model.Empleado;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoController {

    // Crear empleado
    public void crearEmpleado(Empleado empleado) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "INSERT INTO empleados (nombre, apellido, email, password, administrador) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, empleado.getNombre());
        statement.setString(2, empleado.getApellido());
        statement.setString(3, empleado.getEmail());
        statement.setString(4, empleado.getPassword());
        statement.setBoolean(5, empleado.isAdministrador());
        statement.executeUpdate();
        connection.close();
    }

    // Editar empleado
    public void editarEmpleado(Empleado empleado) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "UPDATE empleados SET nombre = ?, apellido = ?, email = ?, password = ?, administrador = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, empleado.getNombre());
        statement.setString(2, empleado.getApellido());
        statement.setString(3, empleado.getEmail());
        statement.setString(4, empleado.getPassword());
        statement.setBoolean(5, empleado.isAdministrador());
        statement.setInt(6, empleado.getId());
        statement.executeUpdate();
        connection.close();
    }

    // Eliminar empleado
    public void eliminarEmpleado(int id) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "DELETE FROM empleados WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
        connection.close();
    }

    // Listar empleados
    public List<Empleado> listarEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM empleados";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nombre = resultSet.getString("nombre");
            String apellido = resultSet.getString("apellido");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            boolean administrador = resultSet.getBoolean("administrador");

            empleados.add(new Empleado(id, nombre, apellido, email, password, administrador));
        }

        connection.close();
        return empleados;
    }

    public Empleado login(String email, String password) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM empleados WHERE email = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        Empleado empleado = null;

        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nombre = resultSet.getString("nombre");
            String apellido = resultSet.getString("apellido");
            boolean administrador = resultSet.getBoolean("administrador");

            empleado = new Empleado(id, nombre, apellido, email, password, administrador);
        }

        connection.close();
        return empleado;
    }
}


