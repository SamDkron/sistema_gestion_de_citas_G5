package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita {
    private String id;
    private Paciente paciente;
    private Medico medico;
    private Consultorio consultorio;
    private citaState estadoCita;
    private String observaciones;
    private String diagnostico;
    private String tratamiento;
    private String motivo;
    private LocalDateTime fecha;

    public Cita(String id, Paciente paciente, Medico medico, Consultorio consultorio, String motivo, LocalDateTime fecha) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.consultorio = consultorio;
        this.estadoCita = citaState.PENDIENTE;
        this.observaciones = "";
        this.tratamiento = "";
        this.diagnostico = "";
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public citaState getEstadoCita() {
        return estadoCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setEstadoCita(citaState estadoCita) {
        this.estadoCita = estadoCita;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    //cambiar estado de las citas

    public void cancelarCita() {
        this.estadoCita = citaState.CANCELADA;
    }

    public void aceptarCita() {
        this.estadoCita = citaState.CONFIRMADA;
    }

    public void completar() {
        this.estadoCita = citaState.COMPLETADA;
    }

    public void iniciarAtencion() {
        this.estadoCita = citaState.EN_ATENCION;

    }

    @Override
    public String toString() {
        DateTimeFormatter fechaFormattter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Fecha: ").append(fechaFormattter.format(this.fecha)).append("\n");
        sb.append("Estado: ").append(this.estadoCita.toString()).append("\n");
        sb.append("Paciente: ").append(this.paciente.nombreCompleto()).append("\n");
        sb.append("Historia clinica").append(this.paciente.getHistoriaClinica()).append("\n");
        sb.append("Medico: Dr(a). ").append(this.medico.nombreCompleto()).append("\n");
        sb.append("Especialidad del medico: ").append(this.medico.getEspecialidad()).append("\n");
        sb.append("Consultorio: ").append(this.consultorio.toString());
        sb.append("Motivos: ").append(this.motivo).append("\n");

        if(!diagnostico.isEmpty()){
            sb.append("Diagnostico: ").append(this.diagnostico).append("\n");
        }

        if(!observaciones.isEmpty()) {
            sb.append("Observaciones: ").append(this.observaciones).append("\n");
        }

        if(!tratamiento.isEmpty()) {
            sb.append("Tratamiento: ").append(this.tratamiento).append("\n");
        }

        return sb.toString();
    }
}