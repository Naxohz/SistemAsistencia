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

    // Configuración de estilos globales
    private Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
    private Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
    private Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

    // Generar Reporte de Atrasos con diseño mejorado
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

        // Añadir encabezado del reporte
        agregarEncabezado(document, "Reporte de Atrasos");

        // Crear tabla con diseño
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 2f, 2f});

        // Encabezados de tabla con fondo de color
        agregarEncabezadoTabla(table, "ID Empleado");
        agregarEncabezadoTabla(table, "Nombre");
        agregarEncabezadoTabla(table, "Hora Entrada");

        // Rellenar datos en la tabla
        while (resultSet.next()) {
            agregarCeldaTabla(table, String.valueOf(resultSet.getInt("id")));
            agregarCeldaTabla(table, resultSet.getString("nombre") + " " + resultSet.getString("apellido"));
            agregarCeldaTabla(table, resultSet.getTimestamp("horaEntrada").toString());
        }

        document.add(table);
        document.close();
        connection.close();

        // Guardar el reporte en la base de datos
        guardarReporteEnBaseDeDatos("Reporte de Atrasos", "reporte_atrasos.pdf");
    }

    // Método para agregar un encabezado a los documentos PDF
    private void agregarEncabezado(Document document, String titulo) throws DocumentException {
        Paragraph encabezado = new Paragraph(titulo, headerFont);
        encabezado.setAlignment(Element.ALIGN_CENTER);
        document.add(encabezado);
        document.add(new Paragraph("Fecha: " + new Date().toString()));
        document.add(new Paragraph(" "));
    }

    // Método para agregar una celda con estilo a la tabla
    private void agregarCeldaTabla(PdfPTable table, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, tableBodyFont));
        cell.setPadding(5);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }

    // Método para agregar encabezado de celda con fondo de color
    private void agregarEncabezadoTabla(PdfPTable table, String texto) {
        PdfPCell headerCell = new PdfPCell(new Phrase(texto, tableHeaderFont));
        headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
        headerCell.setPadding(5);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerCell.setBorderWidth(1);
        table.addCell(headerCell);
    }

    // Método para guardar el reporte en la base de datos
    private void guardarReporteEnBaseDeDatos(String tipoReporte, String archivoPDF) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "INSERT INTO reportes (tipoReporte, archivoPDF, fechaGeneracion) VALUES (?, ?, NOW())";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, tipoReporte);
        statement.setString(2, archivoPDF);
        statement.executeUpdate();
        connection.close();
    }

    // Métodos para generar otros reportes (similar a generarReporteAtrasosPDF)
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

        // Añadir encabezado y tabla mejorada
        agregarEncabezado(document, "Reporte de Salidas Anticipadas");

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2f, 2f});
        agregarEncabezadoTabla(table, "ID Empleado");
        agregarEncabezadoTabla(table, "Nombre");
        agregarEncabezadoTabla(table, "Hora Salida");

        while (resultSet.next()) {
            agregarCeldaTabla(table, String.valueOf(resultSet.getInt("id")));
            agregarCeldaTabla(table, resultSet.getString("nombre") + " " + resultSet.getString("apellido"));
            agregarCeldaTabla(table, resultSet.getTimestamp("horaSalida").toString());
        }

        document.add(table);
        document.close();
        connection.close();

        guardarReporteEnBaseDeDatos("Reporte de Salidas Anticipadas", "reporte_salidas_anticipadas.pdf");
    }

    // Generar Reporte de Inasistencias con diseño mejorado
    public void generarReporteInasistenciasPDF(Date fecha) throws SQLException, DocumentException, FileNotFoundException {
        Connection connection = DatabaseConnection.getConnection();

        // Consulta SQL para obtener las inasistencias excluyendo a los administradores
         String query = "SELECT e.id, e.nombre, e.apellido " +
                   "FROM empleados e " +
                   "LEFT JOIN asistencia a ON e.id = a.empleadoId AND DATE(a.horaEntrada) = ? " +
                   "WHERE a.horaEntrada IS NULL AND e.Administrador = 0";
    
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(fecha.getTime()));
        ResultSet resultSet = statement.executeQuery();

         // Crear PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(PDF_OUTPUT_PATH + "reporte_inasistencias.pdf"));
        document.open();

        // Añadir encabezado del reporte
        agregarEncabezado(document, "Reporte de Inasistencias");

        // Crear tabla con diseño
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 2f, 2f});

        // Encabezados de tabla con fondo de color
        agregarEncabezadoTabla(table, "ID Empleado");
        agregarEncabezadoTabla(table, "Nombre Completo");
        agregarEncabezadoTabla(table, "Estado");

        // Rellenar datos en la tabla
        while (resultSet.next()) {
        agregarCeldaTabla(table, String.valueOf(resultSet.getInt("id")));
        agregarCeldaTabla(table, resultSet.getString("nombre") + " " + resultSet.getString("apellido"));
        agregarCeldaTabla(table, "Inasistencia"); // Estado fijo para cada inasistencia
        }

        document.add(table);
        document.close();
        connection.close();

        // Guardar el reporte en la base de datos
        guardarReporteEnBaseDeDatos("Reporte de Inasistencias", "reporte_inasistencias.pdf");
    }
}