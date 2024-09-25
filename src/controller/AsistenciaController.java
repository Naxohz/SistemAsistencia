package controller;

import model.Asistencia;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsistenciaController {

    // Registrar entrada
    public void registrarEntrada(int empleadoId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "INSERT INTO asistencia (empleadoId, horaEntrada) VALUES (?, NOW())";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, empleadoId);
        statement.executeUpdate();
        connection.close();
    }

    // Registrar salida
    public void registrarSalida(int empleadoId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "UPDATE asistencia SET horaSalida = NOW() WHERE empleadoId = ? AND horaSalida IS NULL";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, empleadoId);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated == 0) {
            throw new SQLException("Error: No se ha encontrado una entrada registrada para hoy.");
        }
        connection.close();
    }

    // Verificar asistencia registrada
    public boolean verificarAsistenciaRegistrada(int empleadoId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM asistencia WHERE empleadoId = ? AND DATE(horaEntrada) = CURDATE()";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, empleadoId);
        ResultSet resultSet = statement.executeQuery();
        boolean existeEntrada = resultSet.next();
        connection.close();
        return existeEntrada;
    }

    // Obtener todas las asistencias
    public List<Asistencia> obtenerTodasLasAsistencias() throws SQLException {
        List<Asistencia> asistencias = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM asistencia";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int empleadoId = resultSet.getInt("empleadoId");
            Date horaEntrada = resultSet.getTimestamp("horaEntrada");
            Date horaSalida = resultSet.getTimestamp("horaSalida");
            asistencias.add(new Asistencia(id, empleadoId, horaEntrada, horaSalida));
        }

        connection.close();
        return asistencias;
    }
}



