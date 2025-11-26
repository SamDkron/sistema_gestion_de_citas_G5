/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Clase que representa una cita medica dentro del sistema.
 * Contiene la informacion basica de los participantes de la cita, es decir:
 * el paciente, el medico y otros datos basicos de la cita como el lugar y la fecha.
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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


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
    /**
     * @return Paciente que va a participar en la cita médica
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
     * @return Fecha y hora en la que se va a realizar la cita
     */
    public LocalDateTime getFecha() {
        return fecha;
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

    /**
     * Cambia el estado de la cita a CANCELADA si la cita se llega a cancelar
     */
    public void cancelarCita() {
        this.estadoCita = citaState.CANCELADA;
    }

    /**
     * Cambia el estado de la cita a COMPLETADA
     */
    public void completar() {
        this.estadoCita = citaState.COMPLETADA;
    }

    public void setEstadoCita(citaState nuevoEstado) {
        this.estadoCita = nuevoEstado;
    }

    /**
     * Cambia el estado de la cita a EN_ATENCION si la cita se encuentra en atencion
     */
    public void iniciarAtencion() {
        this.estadoCita = citaState.EN_ATENCION;

    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace(";", ",").replace("\n", " ").replace("\r", " ").trim();
    }
    private static final DateTimeFormatter WRITE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter READ_FORMATTER = WRITE_FORMATTER;

    public String toCSV() {
        String pacienteId = paciente != null ? paciente.getId() : "";
        String medicoId = medico != null ? medico.getId() : "";
        String consultorioNum = consultorio != null ? consultorio.getNumero() : "";
        String fechaStr = fecha != null ? fecha.format(WRITE_FORMATTER) : "";
        String estado = estadoCita != null ? estadoCita.name() : "";

        return String.join(";",
                id,
                pacienteId,
                medicoId,
                consultorioNum,
                escape(motivo),
                fechaStr,
                estado
        );
    }

    public static Cita fromCSV(String csv, GestionarUsuario gu) {
        if (csv == null || csv.trim().isEmpty()) return null;
        String[] cols = csv.split(";", -1);
        if (cols.length < 6) return null;

        String id = cols[0].trim();
        String pacienteId = cols.length > 1 ? cols[1].trim() : "";
        String medicoId = cols.length > 2 ? cols[2].trim() : "";
        String consultorioNum = cols.length > 3 ? cols[3].trim() : "";
        String motivo = cols.length > 4 ? cols[4].trim() : "";
        String fechaStr = cols.length > 5 ? cols[5].trim() : "";
        String estadoStr = cols.length > 6 ? cols[6].trim() : "";

        Paciente paciente = null;
        Medico medico = null;
        Consultorio consultorio = null;
        LocalDateTime fecha = null;

        if (!pacienteId.isEmpty()) paciente = getPacienteById(pacienteId, gu);
        if (!medicoId.isEmpty()) medico = getMedicoById(medicoId, gu);
        if (!consultorioNum.isEmpty()) consultorio = getConsultorioByNumero(consultorioNum, gu);

        if (!fechaStr.isEmpty()) {
            try {
                fecha = LocalDateTime.parse(fechaStr, READ_FORMATTER);
            } catch (DateTimeParseException ignored) { fecha = null; }
        }

        Cita c = new Cita(id, paciente, medico, consultorio, motivo, fecha);
        if (!estadoStr.isEmpty()) {
            try { c.estadoCita = citaState.valueOf(estadoStr); } catch (Exception ignored) {}
        }
        return c;
    }
    private static Paciente getPacienteById(String id, GestionarUsuario gu) {
        if (id == null || gu == null) return null;
        for (Paciente p : gu.getPacientes()) if (id.equals(p.getId())) return p;
        return null;
    }
    private static Medico getMedicoById(String id, GestionarUsuario gu) {
        if (id == null || gu == null) return null;
        for (Medico m : gu.getMedicos()) if (id.equals(m.getId())) return m;
        return null;
    }
    private static Consultorio getConsultorioByNumero(String numero, GestionarUsuario gu) {
        if (numero == null || gu == null) return null;
        for (Consultorio c : gu.getConsultorios()) if (numero.equals(c.getNumero())) return c;
        return null;
    }
    public String getId() {
        return id;
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