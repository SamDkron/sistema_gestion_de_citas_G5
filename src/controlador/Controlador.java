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

    /**
     * Procesa el registro de un médico en el sistema.
     * @param id ID único del médico.
     * @param nombre Nombre del médico.
     * @param apellido Apellido del médico.
     * @param telefono Teléfono de contacto.
     * @param email Correo electrónico.
     * @param password Contraseña del usuario.
     * @param especialidad Área de especialidad del médico.
     * @return null si el registro fue exitoso; mensaje de error si el ID ya existe.
     */
    public String procesarRegistroMedico(String id, String nombre, String apellido, String telefono,
                                         String email, String password, String especialidad) {
        Medico medico = service.registrarMedico(id, nombre, apellido, telefono, email, password, especialidad);

        if (medico != null) {
            return null;
        } else {
            return "Error al registrar. El ID ya existe.";
        }
    }

    /**
     * Procesa el registro de un paciente en el sistema.
     * @param id ID único del paciente.
     * @param nombre Nombre del paciente.
     * @param apellido Apellido del paciente.
     * @param telefono Teléfono de contacto.
     * @param email Correo electrónico.
     * @param password Contraseña del usuario.
     * @param historiaClinica Número de historia clínica.
     * @param fechaNacimiento Fecha de nacimiento del paciente.
     * @param tipoSangre Tipo de sangre del paciente.
     * @param sexo Sexo del paciente.
     * @return null si el registro fue exitoso; mensaje de error si el ID ya existe.
     */
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

    /**
     * Procesa el registro de un recepcionista en el sistema.
     * @param id ID único del recepcionista.
     * @param nombre Nombre del recepcionista.
     * @param apellido Apellido del recepcionista.
     * @param telefono Teléfono de contacto.
     * @param email Correo electrónico.
     * @param password Contraseña del usuario.
     * @param turno Turno asignado.
     * @return null si el registro fue exitoso; mensaje de error si el ID ya existe.
     */
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

    /**
     * Asigna un consultorio a un médico en una fecha específica.
     * @param idMedico ID del médico.
     * @param numeroConsultorio Consultorio a asignar.
     * @param fecha Fecha de la asignación.
     * @return true si la asignación fue exitosa; false si falló.
     */
    public boolean asignarConsultorioAMedico(String idMedico, String numeroConsultorio,
                                             LocalDateTime fecha) {
        return service.asignarConsultorioAMedico(idMedico, numeroConsultorio, fecha);
    }

    /**
     * Consulta la información de un paciente por su ID.
     * @param idPaciente ID del paciente.
     * @return Información del paciente en formato texto.
     */
    public String consultarPaciente(String idPaciente) {
        return service.consultarPaciente(idPaciente);
    }

    /**
     * Consulta la información de un médico por su ID.
     * @param idMedico ID del médico.
     * @return Información del médico en formato texto.
     */
    public String consultarMedico(String idMedico) {
        return service.consultarMedico(idMedico);
    }

    /**
     * Obtiene una lista en texto con todas las citas registradas.
     * @return Cadena con cada cita separada por líneas.
     */
    public String obtenerTodasLasCitas() {
        StringBuilder sb = new StringBuilder();
        List<Cita> citas = service.enlistarCitas();

        for (Cita cita : citas) {
            sb.append(cita.toString()).append("\n").append("-".repeat(70)).append("\n\n");
        }

        return sb.toString();
    }

    /**
     * Obtiene una lista en texto con todos los médicos registrados.
     * @return Cadena con la información de los médicos o un mensaje si no hay registros.
     */
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

    /**
     * Obtiene una lista en texto con todos los pacientes registrados.
     * @return Cadena con la información de los pacientes o un mensaje si no hay registros.
     */
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
        StringBuilder sb = new StringBuilder();
        sb.append("AGENDA DEL MEDICO: ").append(idMedico).append("\n\n");

        List<Cita> todas = service.enlistarCitas(); // debe devolver la lista en memoria
        int encontradas = 0;

        for (Cita c : todas) {
            String mid = (c.getMedico() != null ? c.getMedico().getId() : null);
            if (mid != null && mid.equals(idMedico)) {
                sb.append(c.toString()).append("\n").append("-".repeat(40)).append("\n");
                encontradas++;
            }
        }

        if (encontradas == 0) {
            sb.append("No hay citas asignadas a este médico.\n");
        }
        return sb.toString();
    }

    /**
     * Procesa la atención de una cita.
     * @return null si fue exitoso, mensaje de error en caso contrario
     */
    public String procesarAtencionCita(String idCita, String diagnostico, String tratamiento, String observaciones) {
        if (idCita == null || idCita.trim().isEmpty()) {
            return "ID de cita inválido.";
        }
        String idTrim = idCita.trim();

        boolean exito = service.atenderCita(idTrim, diagnostico, tratamiento, observaciones);

        if (exito) {
            return null;
        } else {
            Cita c = service.searchCitaById(idTrim);
            if (c == null) return "Cita no encontrada: " + idTrim;
            return "No se pudo atender la cita. Estado actual: " +
                    (c.getEstadoCita() != null ? c.getEstadoCita().name() : "Desconocido");
        }
    }

    /**
     * Obtiene el historial de citas atendidas por un médico.
     * @param idMedico ID del médico
     * @return String con el historial formateado
     */
    public String obtenerHistorialCitasMedico(String idMedico) {
        if (idMedico == null || idMedico.trim().isEmpty()) {
            return "ID de médico inválido.";
        }
        String buscado = idMedico.trim();

        StringBuilder historial = new StringBuilder("HISTORIAL DE CITAS ATENDIDAS\n\n");
        int citasAtendidas = 0;


        List<Cita> todas = service.enlistarCitas();
        for (Cita cita : todas) {
            if (cita == null) continue;
            if (cita.getMedico() == null || cita.getMedico().getId() == null) continue;

            if (cita.getMedico().getId().trim().equalsIgnoreCase(buscado)
                    && cita.getEstadoCita() == citaState.COMPLETADA) {
                historial.append(cita.toString()).append("\n").append("-".repeat(50)).append("\n\n");
                citasAtendidas++;
            }
        }

        if (citasAtendidas == 0) {
            historial.append("No has atendido citas aún.");
        } else {
            historial.append("\nTotal atendidas: ").append(citasAtendidas).append("\n");
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
    public String procesarReservaCita(String idPaciente, String idMedico, String numeroConsultorio, String motivo, java.time.LocalDateTime fecha) {
        try {
            service.reservarCita(idPaciente, idMedico, numeroConsultorio, motivo, fecha);
            return null;
        } catch (IllegalArgumentException iae) {
            return iae.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR inesperado: " + (ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage());
        }
    }

    /**
     * Procesa la cancelación de una cita.
     * @return null si fue exitoso, mensaje de error en caso contrario
     */
    // Controlador: procesarCancelacionCita
    public String procesarCancelacionCita(String idCita) {
        if (idCita == null || idCita.trim().isEmpty()) return "ID de cita inválido.";
        String idTrim = idCita.trim();

        boolean exito = service.cancelarCita(idTrim);
        if (exito) return null;

        // intentar dar feedback
        Cita c = service.searchCitaById(idTrim);
        if (c == null) return "Cita no encontrada: " + idTrim;
        return "No se pudo cancelar la cita. Estado actual: " + (c.getEstadoCita() != null ? c.getEstadoCita().name() : "Desconocido");
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
}