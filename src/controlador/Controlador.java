package controlador;

import modelo.*;
import service.Service;
import vista.*;
import java.time.LocalDateTime;
import java.util.List;

public class Controlador {
    private Service service;

    public Controlador(Service service) {
        this.service = service;
    }

    public String procesarLogin(String id, String password, String tipoEsperado, Vista vistaActual) {
        if (id == null || id.trim().isEmpty() || password == null || password.isEmpty()) {
            return "Por favor completa todos los campos";
        }

        Usuario usuario = service.iniciarSesion(id, password);

        if (usuario == null) {
            return "Usuario o contraseña incorrectos";
        }

        if (!validarTipoUsuario(usuario, tipoEsperado)) {
            return "Este usuario no corresponde al tipo seleccionado";
        }

        abrirVentanaPrincipal(usuario);
        vistaActual.dispose();

        return null;
    }

    private boolean validarTipoUsuario(Usuario usuario, String tipoEsperado) {
        if (tipoEsperado.equals("MEDICO") && usuario instanceof Medico) return true;
        if (tipoEsperado.equals("PACIENTE") && usuario instanceof Paciente) return true;
        if (tipoEsperado.equals("RECEPCIONISTA") && usuario instanceof Recepcionista) return true;
        return false;
    }

    private void abrirVentanaPrincipal(Usuario usuario) {
        if (usuario instanceof Medico) {
            Medico m = (Medico) usuario;
            new MedicoVista(m.getId(), m.nombreCompleto(), m.getEspecialidad(),
                    m.getConsultorioAsignado(), this).setVisible(true);
        } else if (usuario instanceof Paciente) {
            Paciente p = (Paciente) usuario;
            new PacienteVista(p.getId(), p.nombreCompleto(), p.getHistoriaClinica(),
                    p.getTipoSangre(), this).setVisible(true);
        } else if (usuario instanceof Recepcionista) {
            Recepcionista r = (Recepcionista) usuario;
            new RecepcionistaVista(r.nombreCompleto(), r.getState(), this).setVisible(true);
        }
    }

    // ==================== MÉTODOS PARA RECEPCIONISTA ====================

    public String procesarRegistroMedico(String id, String nombre, String apellido, String telefono,
                                         String email, String password, String especialidad) {
        Medico medico = service.registrarMedico(id, nombre, apellido, telefono, email, password, especialidad);

        if (medico != null) {
            return null;
        } else {
            return "Error al registrar. El ID ya existe.";
        }
    }

    public String procesarRegistroPaciente(String id, String nombre, String apellido, String telefono,
                                           String email, String password, String historiaClinica,
                                           String fechaNacimiento, String tipoSangre, String sexo) {
        Paciente paciente = service.registrarPaciente(id, nombre, apellido, telefono, email, password,
                historiaClinica, fechaNacimiento, tipoSangre, sexo);

        if (paciente != null) {
            return null;
        } else {
            return "Error al registrar. El ID ya existe.";
        }
    }

    public String procesarRegistroRecepcionista(String id, String nombre, String apellido,
                                                String telefono, String email, String password,
                                                String turno) {
        Recepcionista recepcionista = service.registrarRecepcionista(id, nombre, apellido, telefono,
                email, password, turno);

        if (recepcionista != null) {
            return null;
        } else {
            return "Error al registrar. El ID ya existe.";
        }
    }

    public boolean asignarConsultorioAMedico(String idMedico, String numeroConsultorio,
                                             LocalDateTime fecha) {
        return service.asignarConsultorioAMedico(idMedico, numeroConsultorio, fecha);
    }

    public String consultarPaciente(String idPaciente) {
        return service.consultarPaciente(idPaciente);
    }

    public String consultarMedico(String idMedico) {
        return service.consultarMedico(idMedico);
    }

    public String obtenerTodasLasCitas() {
        StringBuilder sb = new StringBuilder();
        List<Cita> citas = service.enlistarCitas();

        for (Cita cita : citas) {
            sb.append(cita.toString()).append("\n").append("-".repeat(70)).append("\n\n");
        }

        return sb.toString();
    }

    public String obtenerListaMedicos() {
        StringBuilder sb = new StringBuilder("MÉDICOS REGISTRADOS EN EL SISTEMA\n\n");
        List<Medico> medicos = service.enlistarMedicos();

        if (medicos.isEmpty()) {
            sb.append("No hay médicos registrados.");
        } else {
            for (Medico medico : medicos) {
                sb.append(medico.toString()).append("\n").append("-".repeat(70)).append("\n");
            }
        }

        return sb.toString();
    }

    public String obtenerListaPacientes() {
        StringBuilder sb = new StringBuilder("PACIENTES REGISTRADOS EN EL SISTEMA\n\n");
        List<Paciente> pacientes = service.enlistarPacientes();

        if (pacientes.isEmpty()) {
            sb.append("No hay pacientes registrados.");
        } else {
            for (Paciente paciente : pacientes) {
                sb.append(paciente.toString()).append("\n").append("-".repeat(70)).append("\n");
            }
        }

        return sb.toString();
    }

    public String obtenerDetalleConsultorios() {
        return service.obtenerDetalleConsultorios();
    }

    // ==================== MÉTODOS PARA MÉDICO ====================

    /**
     * Obtiene la agenda del médico formateada como String.
     * @param idMedico ID del médico
     * @return String con la agenda formateada
     */
    public String obtenerAgendaMedico(String idMedico) {
        List<Medico> medicos = service.enlistarMedicos();
        Medico medico = null;

        for (Medico m : medicos) {
            if (m.getId().equals(idMedico)) {
                medico = m;
                break;
            }
        }

        if (medico == null) {
            return "Médico no encontrado";
        }

        StringBuilder agenda = new StringBuilder("AGENDA DEL DR(A). " + medico.nombreCompleto() + "\n\n");

        if (medico.getAgenda().isEmpty()) {
            agenda.append("No tienes citas agendadas.");
        } else {
            for (Cita cita : medico.getAgenda()) {
                agenda.append(cita.toString()).append("\n").append("-".repeat(50)).append("\n\n");
            }
        }

        return agenda.toString();
    }

    /**
     * Procesa la atención de una cita.
     * @return null si fue exitoso, mensaje de error en caso contrario
     */
    public String procesarAtencionCita(String idCita, String diagnostico, String tratamiento, String observaciones) {
        boolean exito = service.atenderCita(idCita, diagnostico, tratamiento, observaciones);

        if (exito) {
            return null;
        } else {
            return "Error al atender la cita. Verifica que el ID sea correcto.";
        }
    }

    /**
     * Obtiene el historial de citas atendidas por un médico.
     * @param idMedico ID del médico
     * @return String con el historial formateado
     */
    public String obtenerHistorialCitasMedico(String idMedico) {
        List<Medico> medicos = service.enlistarMedicos();
        Medico medico = null;

        for (Medico m : medicos) {
            if (m.getId().equals(idMedico)) {
                medico = m;
                break;
            }
        }

        if (medico == null) {
            return "Médico no encontrado";
        }

        StringBuilder historial = new StringBuilder("HISTORIAL DE CITAS ATENDIDAS\n\n");
        int citasAtendidas = 0;

        for (Cita cita : medico.getAgenda()) {
            if (cita.getEstadoCita() == citaState.COMPLETADA) {
                historial.append(cita.toString()).append("\n").append("-".repeat(50)).append("\n\n");
                citasAtendidas++;
            }
        }

        if (citasAtendidas == 0) {
            historial.append("No has atendido citas aún.");
        }

        return historial.toString();
    }

    /**
     * Remite un paciente a otra especialidad.
     * @return String con el resultado de la operación
     */
    public String remitirPaciente(String idCita, String especialidad, String motivo) {
        return service.remitirPaciente(idCita, especialidad, motivo);
    }

    /**
     * Consulta la historia clínica de un paciente.
     * @param idPaciente ID del paciente
     * @return String con la historia clínica
     */
    public String consultarHistoriaClinica(String idPaciente) {
        return service.consultarHistoriaClinicaPaciente(idPaciente);
    }

    // ==================== MÉTODOS PARA PACIENTE ====================

    /**
     * Procesa la reserva de una cita.
     * @return null si fue exitoso, mensaje de error en caso contrario
     */
    public String procesarReservaCita(String idPaciente, String idMedico, String numeroConsultorio,
                                      String motivo, LocalDateTime fecha) {
        Cita cita = service.reservarCita(idPaciente, idMedico, numeroConsultorio, motivo, fecha);

        if (cita != null) {
            return null;
        } else {
            return "Error al reservar la cita. Verifica los datos ingresados.";
        }
    }

    /**
     * Procesa la cancelación de una cita.
     * @return null si fue exitoso, mensaje de error en caso contrario
     */
    public String procesarCancelacionCita(String idCita, String idUsuario) {
        boolean exito = service.cancelarCita(idCita, idUsuario);

        if (exito) {
            return null;
        } else {
            return "Error al cancelar la cita. Verifica el ID.";
        }
    }

    /**
     * Procesa la reprogramación de una cita.
     * @return null si fue exitoso, mensaje de error en caso contrario
     */
    public String procesarReprogramacionCita(String idCita, LocalDateTime nuevaFecha) {
        try {
            boolean exito = service.reprogramarCita(idCita, nuevaFecha);

            if (exito) {
                return null;
            } else {
                return "Error al reprogramar la cita. Verifica el ID.";
            }
        } catch (Exception e) {
            return "Error al reprogramar: " + e.getMessage();
        }
    }

    /**
     * Obtiene todas las citas de un paciente.
     * @param idPaciente ID del paciente
     * @return String con las citas formateadas
     */
    public String obtenerCitasPaciente(String idPaciente) {
        StringBuilder citas = new StringBuilder("MIS CITAS AGENDADAS\n\n");
        List<Cita> todasLasCitas = service.enlistarCitas();
        int citasEncontradas = 0;

        for (Cita cita : todasLasCitas) {
            if (cita.getPaciente().getId().equals(idPaciente)) {
                citas.append(cita.toString()).append("\n").append("-".repeat(50)).append("\n\n");
                citasEncontradas++;
            }
        }

        if (citasEncontradas == 0) {
            citas.append("No tienes citas agendadas.");
        }

        return citas.toString();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    public Cita reservarCita(String idPaciente, String idMedico, String numeroConsultorio,
                             String motivo, LocalDateTime fecha) {
        return service.reservarCita(idPaciente, idMedico, numeroConsultorio, motivo, fecha);
    }

    public boolean cancelarCita(String idCita, String idPaciente) {
        return service.cancelarCita(idCita, idPaciente);
    }

    public boolean reprogramarCitas(String idCita, LocalDateTime nuevaFecha) {
        try {
            return service.reprogramarCita(idCita, nuevaFecha);
        } catch (Exception e) {
            System.err.println("ERROR!" + e.getMessage());
            return false;
        }
    }

    public boolean atenderCita(String idCita, String diagnostico, String tratamiento, String observaciones) {
        return service.atenderCita(idCita, diagnostico, tratamiento, observaciones);
    }

    public List<Medico> enlistarMedicos() {
        return service.enlistarMedicos();
    }

    public List<Paciente> enlistarPacientes() {
        return service.enlistarPacientes();
    }

    public List<Cita> enlistarCitas() {
        return service.enlistarCitas();
    }
}