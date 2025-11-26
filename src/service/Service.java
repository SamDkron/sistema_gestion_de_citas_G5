package service;

import data.DatosEjemplo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import modelo.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Archivo 'service' que contiene toda la logica de negocio del programa.
 */
public class Service {
    private final List<Cita> citas;
    private final GestionarUsuario gestionarUsuario;
    private int contadorCitas;

    // Archivo donde se persisten las citas (puedes cambiar la ruta si quieres)
    private static final Path CITAS_FILE = Paths.get("data", "citas.txt");
    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * constructor del programa
     */
    public Service(GestionarUsuario gestionarUsuario) {
        this.citas = new ArrayList<>();
        this.gestionarUsuario = gestionarUsuario;
        this.contadorCitas = 1;

        // Cargar citas desde archivo al iniciar
        cargarCitasDesdeArchivo();
    }

    /**
     * metodo encargado de generar un id a cada cita nueva que se genere
     */
    public String generadorIdCita(){
        return String.format("CITA-A%07d", contadorCitas++);
    }

    // -------------------- búsquedas -------------------- //

    public Usuario searchUserById(String id){
        for(Usuario paciente : gestionarUsuario.getPacientes()){
            if(paciente.getId().equals(id)){
                return paciente;
            }
        }

        for(Usuario medico : gestionarUsuario.getMedicos()){
            if(medico.getId().equals(id)){
                return medico;
            }
        }

        for(Usuario Recepcionista : gestionarUsuario.getRecepcionistas()){
            if(Recepcionista.getId().equals(id)){
                return Recepcionista;
            }
        }

        return null;
    }

    public Consultorio searchConsultorioByNumero(String numero) {
        for (Consultorio c1 : gestionarUsuario.getConsultorios()) {
            if (c1.getNumero().equals(numero)) {
                return c1;
            }
        }
        return null;
    }

    /**
     * Agrega una cita en memoria y persiste inmediatamente a archivo.
     */
    public void agregarCita(Cita cita){
        citas.add(cita);
        if (cita.getMedico() != null) {
            cita.getMedico().agregarCita(cita);
        }
        guardarCitasEnArchivo();
    }

    public Cita crearCita(String id, Paciente paciente, Medico medico, Consultorio consultorio, String motivo, LocalDateTime fecha){
        Cita nuevaCita = new Cita( id,  paciente,  medico,  consultorio, motivo, fecha);

        agregarCita(nuevaCita);

        return nuevaCita;
    }

    public Cita searchCitaById(String id) {
        if (id == null) return null;
        String buscado = id.trim();
        if (buscado.isEmpty()) return null;

        for (Cita c : gestionarUsuario.getCitas()) {
            if (c != null && c.getId() != null && c.getId().trim().equalsIgnoreCase(buscado)) {
                return c;
            }
        }
        return null;
    }

    public List<Cita> verCitasPaciente(String idPaciente) {
        return citas.stream()
                .filter(cita -> cita.getPaciente().getId().equals(idPaciente))
                .filter(cita -> !cita.getEstadoCita().equals(citaState.CANCELADA))
                .sorted(Comparator.comparing(Cita::getFecha))
                .collect(Collectors.toList());
    }

    public List<Cita> verAgendaMedico(String idMedico) {
        return citas.stream()
                .filter(cita -> cita.getMedico().getId().equals(idMedico))
                .filter(cita -> !cita.getEstadoCita().equals(citaState.CANCELADA))
                .sorted(Comparator.comparing(Cita::getFecha))
                .collect(Collectors.toList());
    }

    public boolean validarHorarioMedico(Medico medico, LocalDateTime fechaHora){
        LocalDateTime finalCita = fechaHora.plusMinutes(30);
        return citas.stream()
                .filter(cita -> cita.getMedico().getId().equals(medico.getId()))
                .filter(cita -> cita.getEstadoCita() != citaState.CANCELADA)
                .noneMatch(cita ->{
                    LocalDateTime inicioCitaCreada = cita.getFecha();
                    LocalDateTime finCitaCreada = inicioCitaCreada.plusMinutes(30);
                    return fechaHora.isBefore(finCitaCreada) && finalCita.isAfter(inicioCitaCreada);
                });
    }

    public boolean validarHorarioConsultorio(Consultorio consultorio, LocalDateTime fechaHora){
        LocalDateTime finalCita = fechaHora.plusMinutes(30);
        return citas.stream()
                .filter(cita -> cita.getConsultorio().getNumero().equals(consultorio.getNumero()))
                .filter(cita -> cita.getEstadoCita() != citaState.CANCELADA)
                .noneMatch(cita ->{
                    LocalDateTime inicioCitaCreada = cita.getFecha();
                    LocalDateTime finCitaCreada = inicioCitaCreada.plusMinutes(30);
                    return fechaHora.isBefore(finCitaCreada) && finalCita.isAfter(inicioCitaCreada);
                });
    }

    public Usuario iniciarSesion (String id, String password){
        if(id == null  || id.trim().isEmpty()){
            System.out.println("El ID del usuario no puede estar vacio");
            return null;
        }

        if(password == null || password.trim().isEmpty()){
            System.out.println("La contraseña no puede estar vacia");
            return null;
        }

        for(Paciente paciente : gestionarUsuario.getPacientes()){
            if(paciente.getId().equals(id) && paciente.getPassword().equals(password)){
                return paciente;
            }
        }

        for(Medico medico : gestionarUsuario.getMedicos()){
            if(medico.getId().equals(id) && medico.getPassword().equals(password)){
                return medico;
            }
        }

        for(Recepcionista recepcionista : gestionarUsuario.getRecepcionistas()){
            if(recepcionista.getId().equals(id) && recepcionista.getPassword().equals(password)){
                return recepcionista;
            }
        }
        return null;
    }

    public String identificarTipoUsuario(Usuario usuario){
        if(usuario == null) return "Desconocido";
        return usuario.getTipo();
    }

    /**
     * Reserva una cita (persistida automáticamente mediante agregarCita)
     */
    public Cita reservarCita(String idPaciente, String idMedico, String numeroConsultorio, String motivo, LocalDateTime fecha) {

        Paciente p = null; for (Paciente px: gestionarUsuario.getPacientes()) if (idPaciente.equals(px.getId())) { p = px; break; }
        Medico m = null; for (Medico mx: gestionarUsuario.getMedicos()) if (idMedico.equals(mx.getId())) { m = mx; break; }
        Consultorio c = null; for (Consultorio cx: gestionarUsuario.getConsultorios()) if (numeroConsultorio.equals(cx.getNumero())) { c = cx; break; }

        if (p == null) throw new IllegalArgumentException("Paciente no encontrado: " + idPaciente);
        if (m == null) throw new IllegalArgumentException("Médico no encontrado: " + idMedico);
        if (c == null) throw new IllegalArgumentException("Consultorio no encontrado: " + numeroConsultorio);
        if (fecha == null) throw new IllegalArgumentException("Fecha inválida");
        if (fecha.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("La fecha debe ser futura");

        String newId = "C" + (gestionarUsuario.getCitas().size() + 1);

        Cita cita = new Cita(newId, p, m, c, motivo, fecha);

        gestionarUsuario.agregarCita(cita);
        gestionarUsuario.guardarEnArchivo();

        return cita;
    }

    /**
     * Cancela una cita y persiste el cambio
     */
    public boolean cancelarCita(String idCita) {
        if (idCita == null) return false;
        String buscado = idCita.trim();
        if (buscado.isEmpty()) return false;

        Cita cita = searchCitaById(buscado);
        if (cita == null) return false;


        if (cita.getEstadoCita() == citaState.PENDIENTE || cita.getEstadoCita() == citaState.CONFIRMADA) {
            cita.setEstadoCita(citaState.CANCELADA);

            try {
                gestionarUsuario.guardarEnArchivo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * Reprograma y persiste
     */
    public boolean reprogramarCita(String idCita, LocalDateTime nuevaFecha) {
        Cita cita = searchCitaById(idCita);

        if(cita == null || cita.getEstadoCita() == citaState.COMPLETADA || cita.getEstadoCita() == citaState.CANCELADA){
            return false;
        }

        if(!validarHorarioMedico(cita.getMedico(), nuevaFecha)){
            throw new IllegalArgumentException("El médico no está disponible en la fecha y hora seleccionadas.");
        }

        if(!validarHorarioConsultorio(cita.getConsultorio(), nuevaFecha)){
            throw new IllegalArgumentException("El consultorio no está disponible en la fecha y hora seleccionadas.");
        }

        cita.setFecha(nuevaFecha);
        guardarCitasEnArchivo();
        return true;
    }

    /**
     * consultarHistoriaClinicaPaciente (sin cambios funcionales)
     */
    public String consultarHistoriaClinicaPaciente(String idPaciente) {
        Paciente paciente = (Paciente) searchUserById(idPaciente);
        if(paciente == null){
            return "Paciente no encontrado";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("=== HISTORIA CLÍNICA DEL PACIENTE ===\n\n");
        sb.append("ID del paciente: ").append(paciente.getId()).append("\n");
        sb.append("Nombre: ").append(paciente.nombreCompleto()).append("\n");
        sb.append("Historia Clínica: ").append(paciente.getHistoriaClinica()).append("\n");
        sb.append("Grupo Sanguíneo: ").append(paciente.getTipoSangre()).append("\n");
        sb.append("Fecha de Nacimiento: ").append(paciente.getFechaNacimiento()).append("\n");
        sb.append("Sexo: ").append(paciente.getSexo()).append("\n");
        sb.append("Número de Teléfono: ").append(paciente.getTelefono()).append("\n");
        sb.append("\n");

        List<Cita> pacienteCitas = citas.stream()
                .filter(c -> c.getPaciente().getId().equals(paciente.getId()))
                .filter(c -> c.getEstadoCita() == citaState.COMPLETADA)
                .sorted(Comparator.comparing(Cita::getFecha).reversed())
                .collect(Collectors.toList());

        sb.append("=== HISTORIAL DE CITAS ===\n\n");

        if(pacienteCitas.isEmpty()){
            sb.append("No hay citas completadas.\n");
        } else {
            for (Cita c : pacienteCitas) {
                sb.append(c.toString()).append("\n");
                sb.append("-".repeat(70)).append("\n\n");
            }
        }
        return sb.toString();
    }

    //---------------metodos de medico---------------//

    /**
     * Atender cita
     */
    public boolean atenderCita(String idCita, String diagnostico, String tratamiento, String observaciones) {
        Cita cita = searchCitaById(idCita);

        if(cita != null && (cita.getEstadoCita() == citaState.CONFIRMADA ||
                cita.getEstadoCita() == citaState.PENDIENTE)) {
            cita.iniciarAtencion();
            cita.setDiagnostico(diagnostico);
            cita.setTratamiento(tratamiento);
            cita.setObservaciones(observaciones);
            cita.completar();
            Medico m = cita.getMedico();
            if (m != null) {
                boolean existe = false;
                for (Cita cc : m.getAgenda()) {
                    if (cc != null && cc.getId() != null && cc.getId().trim().equalsIgnoreCase(cita.getId().trim())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    m.getAgenda().add(cita);
                }
            }
            guardarCitasEnArchivo();
            return true;
        }
        return false;
    }

    /**
     * Remitir paciente: intenta buscar turno disponible y crea nueva cita (persistida por agregarCita) y marca completada la original
     */
    public String remitirPaciente(String idCita, String especialidad, String motivo) {
        Cita cita = searchCitaById(idCita);
        if (cita == null) {
            throw new IllegalArgumentException("La cita no se encuentra.");
        }

        List<Medico> especialistas = gestionarUsuario.getMedicos().stream()
                .filter(doc -> doc.getEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());

        if (especialistas.isEmpty()) {
            return "No hay especialistas disponibles en la especialidad " + especialidad;
        }

        StringBuilder sb = new StringBuilder();
        boolean creada = false;
        Medico medicoAsignado = especialistas.get(0);
        Paciente paciente = cita.getPaciente();
        Consultorio consultorioAsignado = null;
        LocalDateTime propuesta = LocalDateTime.now().plusHours(1);

        int minutos = propuesta.getMinute() < 30 ? 0 : 30;
        propuesta = propuesta.withMinute(minutos).withSecond(0).withNano(0);

        LocalDateTime limiteHorario = propuesta.plusDays(30);

        while (!creada && !propuesta.isAfter(limiteHorario)) {
            if (validarHorarioMedico(medicoAsignado, propuesta)) {
                if (consultorioAsignado == null || !validarHorarioConsultorio(consultorioAsignado, propuesta)) {
                    for (Consultorio c : gestionarUsuario.getConsultorios()) {
                        if (validarHorarioConsultorio(c, propuesta)) {
                            consultorioAsignado = c;
                            break;
                        }
                    }
                }

                if (consultorioAsignado != null) {
                    String idNuevaCita = generadorIdCita();
                    Cita nuevaCita = new Cita(idNuevaCita, paciente, medicoAsignado, consultorioAsignado, motivo, propuesta);

                    agregarCita(nuevaCita); // esto ya guarda el archivo

                    sb.append("\n ======= Cita creada para remision de paciente =======  \n");
                    sb.append(nuevaCita).append("\n");
                    creada = true;

                    cita.completar();
                    guardarCitasEnArchivo(); // guardar que la cita original quedó completada
                    break;
                }
            }
            propuesta = propuesta.plusMinutes(30);
        }

        if (!creada) {
            sb.append("\n No se encontro un horario disponible con algun especialista en los proximos 30 dias. \n");
        }

        return sb.toString();
    }

    //--------------- metodos de recepcionista ---------------//

    public Medico registrarMedico(String id, String nombre, String apellido, String telefono, String email, String password, String especialidad) {
        Medico nuevoMedico = new Medico(id, nombre, apellido, telefono, email, password, especialidad);
        for (Medico m : gestionarUsuario.getMedicos()) {
            if (m.getId().equals(id)) {
                System.out.println("Ya existe un recepcionista con ese ID");
                return null;
            }
        }
        gestionarUsuario.getMedicos().add(nuevoMedico);
        gestionarUsuario.guardarEnArchivo();
        return nuevoMedico;
    }

    public Recepcionista registrarRecepcionista(String id, String nombre, String apellido,
                                                String telefono, String email, String password,
                                                String state) {
        for (Recepcionista r : gestionarUsuario.getRecepcionistas()) {
            if (r.getId().equals(id)) {
                System.out.println("Ya existe un recepcionista con ese ID");
                return null;
            }
        }
        Recepcionista nuevoRecepcionista = new Recepcionista(id, nombre, apellido, telefono, email, password, state);
        gestionarUsuario.getRecepcionistas().add(nuevoRecepcionista);
        gestionarUsuario.guardarEnArchivo();
        System.out.println("Recepcionista registrado exitosamente: " + nuevoRecepcionista.nombreCompleto());
        return nuevoRecepcionista;
    }

    public Paciente registrarPaciente(String id, String nombre, String apellido, String telefono,
                                      String email, String password, String historiaClinica,
                                      String fechaNacimiento, String tipoSangre, String sexo) {
        Paciente nuevoPaciente = new Paciente(id, nombre, apellido, telefono, email, password,
                historiaClinica, fechaNacimiento, tipoSangre, sexo);
        for (Paciente p : gestionarUsuario.getPacientes()){
            if (p.getId().equals(id)) {
                System.out.println("Ya existe un recepcionista con ese ID");
                return null;
            }
        }
        gestionarUsuario.getPacientes().add(nuevoPaciente);
        gestionarUsuario.guardarEnArchivo();
        return nuevoPaciente;
    }

    public boolean asignarConsultorioAMedico(String idMedico, String numeroConsultorio, LocalDateTime fecha) {
        Medico medico = (Medico) searchUserById(idMedico);
        Consultorio consultorio = searchConsultorioByNumero(numeroConsultorio);

        if(medico != null && consultorio != null) {
            medico.setConsultorioAsignado(numeroConsultorio);
            return true;
        }
        return false;
    }

    public String consultarPaciente(String idPaciente) {
        Paciente paciente = (Paciente) searchUserById(idPaciente);
        if (paciente != null) {
            return paciente.toString();
        }
        return null;
    }

    public String consultarMedico(String idMedico) {
        Medico medico = (Medico) searchUserById(idMedico);
        if(medico != null) {
            return medico.toString();
        }
        return null;
    }

    public String obtenerDetalleConsultorios() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Consultorio consultorio : gestionarUsuario.getConsultorios()) {
            sb.append("Consultorio: ").append(consultorio.getNumero()).append("\n");
            sb.append("Ubicación: ").append(consultorio.getUbicacion()).append("\n");
            sb.append("Estado: ").append(consultorio.isDisponibilidad() ? "Disponible" : "Ocupado").append("\n");

            List<Cita> citasConsultorio = citas.stream()
                    .filter(c -> c.getConsultorio().getNumero().equals(consultorio.getNumero()))
                    .filter(c -> c.getEstadoCita() != citaState.CANCELADA && c.getEstadoCita() != citaState.COMPLETADA)
                    .filter(c -> c.getFecha().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Cita::getFecha))
                    .collect(Collectors.toList());

            if (!citasConsultorio.isEmpty()) {
                sb.append("\nHorarios Ocupados:\n");
                for (Cita cita : citasConsultorio) {
                    LocalDateTime inicio = cita.getFecha();
                    LocalDateTime fin = inicio.plusMinutes(30);
                    sb.append("  • ").append(formatter.format(inicio))
                            .append(" - ").append(fin.format(DateTimeFormatter.ofPattern("HH:mm")))
                            .append(" (Dr(a). ").append(cita.getMedico().nombreCompleto()).append(")\n");
                }
            } else {
                sb.append("\nNo hay citas programadas.\n");
            }

            sb.append("\n").append("-".repeat(70)).append("\n");
        }

        return sb.toString();
    }

    public List<Paciente> enlistarPacientes(){
        return new ArrayList<>(gestionarUsuario.getPacientes());
    }

    public List<Medico> enlistarMedicos() {
        return new ArrayList<>(gestionarUsuario.getMedicos());
    }

    public List<Consultorio> enlistarConsultorios() {
        return new ArrayList<>(gestionarUsuario.getConsultorios());
    }

    public List<Cita> enlistarCitas() {return gestionarUsuario.getCitas();}

    /**
     * Guarda todas las citas en el archivo CITAS_FILE (una cita por línea en CSV)
     */
    private void guardarCitasEnArchivo() {
        try {
            // Asegurar directorio
            if (CITAS_FILE.getParent() != null) {
                Files.createDirectories(CITAS_FILE.getParent());
            }

            List<String> lines = new ArrayList<>();
            for (Cita c : citas) {
                lines.add(c.toCSV());
            }

            Files.write(CITAS_FILE, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error guardando citas en archivo: " + e.getMessage());
        }
    }

    /**
     * Carga las citas desde el archivo. Si una línea no puede parsearse o faltan usuarios referenciados,
     * se ignora esa línea y se continúa. También actualiza contadorCitas para no repetir IDs.
     */
    private void cargarCitasDesdeArchivo() {
        if (!Files.exists(CITAS_FILE)) {
            return;
        }

        int maxIdNum = 0;

        try {
            List<String> lines = Files.readAllLines(CITAS_FILE, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) continue;
                try {
                    Cita c = Cita.fromCSV(line, gestionarUsuario);
                    if (c == null) continue;

                    if (c.getPaciente() == null || c.getMedico() == null || c.getConsultorio() == null) {
                        System.err.println("Se omitió cargar cita por referencias faltantes: " + line);
                        continue;
                    }

                    citas.add(c);

                    c.getMedico().agregarCita(c);

                    String id = c.getId();
                    String prefix = "CITA-A";
                    if (id != null && id.startsWith(prefix)) {
                        String numberPart = id.substring(prefix.length());
                        try {
                            int num = Integer.parseInt(numberPart);
                            if (num > maxIdNum) maxIdNum = num;
                        } catch (NumberFormatException ignored) { }
                    }
                } catch (Exception ex) {
                    System.err.println("Error parseando línea de cita: " + line + " -> " + ex.getMessage());
                }
            }

            this.contadorCitas = Math.max(this.contadorCitas, maxIdNum + 1);
        } catch (IOException e) {
            System.err.println("Error cargando citas desde archivo: " + e.getMessage());
        }
    }
}