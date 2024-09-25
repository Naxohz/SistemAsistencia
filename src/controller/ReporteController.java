package controller;

import model.DatabaseConnection;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.Date;

public class ReporteController {

    private static final String PDF_OUTPUT_PATH = "C:/reportes/";

    // Generar Reporte de Atrasos
    public void generarReporteAtrasosPDF() throws SQLException, DocumentException, FileNotFoundException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT e.id, e.nombre, e.apellido, a.horaEntrada " +
                       "FROM asistencia a " +
                       "JOIN empleados e ON a.empleadoId = e.id " +
                       "WHERE HOUR(a.horaEntrada) > 9 OR (HOUR(a.horaEntrada) = 9 AND MINUTE(a.horaEntrada) > 30)";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        // Crear PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(PDF_OUTPUT_PATH + "reporte_atrasos.pdf"));
        document.open();

        document.add(new Paragraph("Reporte de Atrasos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph("Fecha: " + new Date().toString()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.addCell("ID Empleado");
        table.addCell("Nombre");
        table.addCell("Hora Entrada");

        while (resultSet.next()) {
            table.addCell(String.valueOf(resultSet.getInt("id")));
            table.addCell(resultSet.getString("nombre") + " " + resultSet.getString("apellido"));
            table.addCell(resultSet.getTimestamp("horaEntrada").toString());
        }

        document.add(table);
        document.close();
        connection.close();

        // Guardar el reporte en la base de datos
        guardarReporteEnBaseDeDatos("Reporte de Atrasos", "reporte_atrasos.pdf");
    }

    // Generar Reporte de Salidas Anticipadas
    public void generarReporteSalidasAnticipadasPDF() throws SQLException, DocumentException, FileNotFoundException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT e.id, e.nombre, e.apellido, a.horaSalida " +
                       "FROM asistencia a " +
                       "JOIN empleados e ON a.empleadoId = e.id " +
                       "WHERE HOUR(a.horaSalida) < 17 OR (HOUR(a.horaSalida) = 17 AND MINUTE(a.horaSalida) < 30)";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        // Crear PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(PDF_OUTPUT_PATH + "reporte_salidas_anticipadas.pdf"));
        document.open();

        document.add(new Paragraph("Reporte de Salidas Anticipadas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph("Fecha: " + new Date().toString()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.addCell("ID Empleado");
        table.addCell("Nombre");
        table.addCell("Hora Salida");

        while (resultSet.next()) {
            table.addCell(String.valueOf(resultSet.getInt("id")));
            table.addCell(resultSet.getString("nombre") + " " + resultSet.getString("apellido"));
            table.addCell(resultSet.getTimestamp("horaSalida").toString());
        }

        document.add(table);
        document.close();
        connection.close();

        // Guardar el reporte en la base de datos
        guardarReporteEnBaseDeDatos("Reporte de Salidas Anticipadas", "reporte_salidas_anticipadas.pdf");
    }

    // Generar Reporte de Inasistencias
    public void generarReporteInasistenciasPDF(Date fecha) throws SQLException, DocumentException, FileNotFoundException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT e.id, e.nombre, e.apellido " +
                       "FROM empleados e " +
                       "LEFT JOIN asistencia a ON e.id = a.empleadoId AND DATE(a.horaEntrada) = ? " +
                       "WHERE a.horaEntrada IS NULL";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(fecha.getTime()));
        ResultSet resultSet = statement.executeQuery();

        // Crear PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(PDF_OUTPUT_PATH + "reporte_inasistencias.pdf"));
        document.open();

        document.add(new Paragraph("Reporte de Inasistencias", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph("Fecha: " + fecha.toString()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.addCell("ID Empleado");
        table.addCell("Nombre");
        table.addCell("Apellido");

        while (resultSet.next()) {
            table.addCell(String.valueOf(resultSet.getInt("id")));
            table.addCell(resultSet.getString("nombre"));
            table.addCell(resultSet.getString("apellido"));
        }

        document.add(table);
        document.close();
        connection.close();

        // Guardar el reporte en la base de datos
        guardarReporteEnBaseDeDatos("Reporte de Inasistencias", "reporte_inasistencias.pdf");
    }

    // Método para guardar el reporte generado en la base de datos
    private void guardarReporteEnBaseDeDatos(String tipoReporte, String archivoPDF) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "INSERT INTO reportes (tipoReporte, archivoPDF, fechaGeneracion) VALUES (?, ?, NOW())";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, tipoReporte);
        statement.setString(2, archivoPDF);
        statement.executeUpdate();
        connection.close();
    }

    // Métodos para obtener los datos de asistencia para los reportes
    public ResultSet obtenerAtrasos() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT e.id, e.nombre, e.apellido, a.horaEntrada " +
                       "FROM asistencia a " +
                       "JOIN empleados e ON a.empleadoId = e.id " +
                       "WHERE HOUR(a.horaEntrada) > 9 OR (HOUR(a.horaEntrada) = 9 AND MINUTE(a.horaEntrada) > 30)";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet obtenerSalidasAnticipadas() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT e.id, e.nombre, e.apellido, a.horaSalida " +
                       "FROM asistencia a " +
                       "JOIN empleados e ON a.empleadoId = e.id " +
                       "WHERE HOUR(a.horaSalida) < 17 OR (HOUR(a.horaSalida) = 17 AND MINUTE(a.horaSalida) < 30)";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public ResultSet obtenerInasistencias(Date fecha) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT e.id, e.nombre, e.apellido " +
                       "FROM empleados e " +
                       "LEFT JOIN asistencia a ON e.id = a.empleadoId AND DATE(a.horaEntrada) = ? " +
                       "WHERE a.horaEntrada IS NULL";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(fecha.getTime()));
        return statement.executeQuery();
    }
}



