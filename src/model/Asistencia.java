package model;

import java.util.Date;

public class Asistencia {
    private int id;
    private int empleadoId;
    private Date horaEntrada;
    private Date horaSalida;

    // Constructor
    public Asistencia(int id, int empleadoId, Date horaEntrada, Date horaSalida) {
        this.id = id;
        this.empleadoId = empleadoId;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }

    public Date getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(Date horaEntrada) { this.horaEntrada = horaEntrada; }

    public Date getHoraSalida() { return horaSalida; }
    public void setHoraSalida(Date horaSalida) { this.horaSalida = horaSalida; }
}


