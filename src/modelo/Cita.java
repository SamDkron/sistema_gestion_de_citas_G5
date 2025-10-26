/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una cita medica dentro del sistema.
 * Contiene la informacion basica de los participantes de la cita, es decir:
 * el paciente, el medico y otros datos basicos de la cita.
 */
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

    /**
     * Constructor de la clase cita, crea una nueva cita cada que se instancia
     *
     * @param id          ID de la cita medica
     * @param paciente    Paciente de la cita
     * @param medico      Medico encargado de la cita
     * @param consultorio Consultorio en el que se realizara la cita
     * @param motivo      Motivo de la cita
     * @param fecha       Fecha en la que se dara a cabo la cita medica
     */
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

    /**
     * @return String con el ID de la cita medica
     */
    public String getId() {
        return id;
    }

    /**
     * @return Paciente que va a participar en la cita medica
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * @return Estado actual de la cita medica
     */
    public citaState getEstadoCita() {
        return estadoCita;
    }

    /**
     * @return String con el motivo de la cita medica
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @return Fecha y hora en la que se va a realizar la cita
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * @return Stringo con el tratamiento propuesto por el medico para el paciente
     */
    public String getTratamiento() {
        return tratamiento;
    }

    /**
     * @return String con el diagnostico realizado por el medico
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * @return Observaciones realizadas por el medico
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @return Consultorio en el que se va a dar lugar a la cita medica
     */
    public Consultorio getConsultorio() {
        return consultorio;
    }

    /**
     * @return Medico que va a participar en la cita
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * @param estadoCita Estado nuevo que se le asignara a la cita.
     *                   Solo puede estar dentro de los estados establecidos en la clase citaState
     */
    public void setEstadoCita(citaState estadoCita) {
        this.estadoCita = estadoCita;
    }

    /**
     * @param observaciones Observaciones realizadas por el medico
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * @param fecha Fecha nueva en la que se va a hacer la cita
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * @param tratamiento Tratamiento establecido por el medico
     */
    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    /**
     * @param diagnostico Diagnostico establecido por el medico
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    //cambiar estado de las citas

    /**
     * Cambia el estado de la cita a CANCELADA si la cita se llega a cancelar
     */
    public void cancelarCita() {
        this.estadoCita = citaState.CANCELADA;
    }

    /**
     * Cambia el estado de la cita a CONFIRMADA
     */
    public void aceptarCita() {
        this.estadoCita = citaState.CONFIRMADA;
    }

    /**
     * Cambia el estado de la cita a COMPLETADA
     */
    public void completar() {
        this.estadoCita = citaState.COMPLETADA;
    }

    /**
     * Cambia el estado de la cita a EN_ATENCION si la cita se encuentra en atencion
     */
    public void iniciarAtencion() {
        this.estadoCita = citaState.EN_ATENCION;

    }

    /**
     * Formato de impresion que muestra la informacion basica de la cita medica
     * @return String con toda la informacion basica de la cita
     */
    @Override
    public String toString() {
        DateTimeFormatter fechaFormattter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("ID Cita: ").append(this.id).append("\n");
        sb.append("Fecha: ").append(fechaFormattter.format(this.fecha)).append("\n");
        sb.append("Estado: ").append(this.estadoCita.toString()).append("\n");
        sb.append("Paciente: ").append(this.paciente.nombreCompleto()).append("\n");
        sb.append("Historia clinica: ").append(this.paciente.getHistoriaClinica()).append("\n");
        sb.append("Medico: Dr(a). ").append(this.medico.nombreCompleto()).append("\n");
        sb.append("Especialidad del medico: ").append(this.medico.getEspecialidad()).append("\n");
        sb.append("Consultorio: ").append(this.consultorio.toString());
        sb.append("Motivos: ").append(motivo).append("\n");

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