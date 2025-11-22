/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package service;

import data.DatosEjemplo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import modelo.*;

/**
 * Archivo 'service' que contiene toda la logica de negocio del programa.
 * <p>
 *     aca esta la logica de la mayoria de los metodos que fueron ideados en los diagramas y
 *     gestiona las citas, pacientes, medicos, recepcionistas y consultorios
 * </p>
 */

public class Service {
    private final List<Cita> citas;
    private final List<Medico> medicos;
    private final List<Paciente> pacientes;
    private final List<Consultorio> consultorios;
    private final List<Recepcionista> recepcionistas;
    private final GestionarUsuario gestionarUsuario;
    private int contadorCitas;

    /**
     * constructor del programa
     */
    public Service() {
        this.citas = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.consultorios = new ArrayList<>();
        this.recepcionistas = new ArrayList<>();
        this.gestionarUsuario = new GestionarUsuario();
        gestionarUsuario.cargarDesdeArchivo();
        this.contadorCitas = 1;

        this.recepcionistas.addAll(DatosEjemplo.inicializarRecepcionista());
        this.consultorios.addAll(DatosEjemplo.inicializarConsultorioEJ());
        this.medicos.addAll(DatosEjemplo.inicializarMedicoEJ());
        this.pacientes.addAll(DatosEjemplo.inicializarPacienteEJ());

    }

    /**
     * metodo encargado de generar un id a cada cita nueva que se genere
     */

    private String generadorIdCita(){
        return String.format("CITA-A%07d", contadorCitas++);
    }

    /**
     * estos metodos se encargan de realizar busquedas
     * <p>
     *     buscan pacientes, medicos y citas mediante su id unico y buscan
     *     consultorios mediante su numero.
     * </p>
     * @param id ID unica del usuario
     * @return Paciente / Medico si se encuentra el id, null en caso contrario
     */

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

    /**
     *
     * @param numero numero del consultorio
     * @return Consultorio si se encuentra el id, null si no se encuentra
     */
    public Consultorio searchConsultorioByNumero(String numero) {
        for (Consultorio c1 : consultorios) {
            if (c1.getNumero().equals(numero)) {
                return c1;
            }
        }
        return null;
    }

    /**
     *
     * @param id ID unica de la cita
     * @return Cita si se encuentra la cita, null en caso contrario
     */
    public Cita searchCitaById(String id) {
        for (Cita c1 : citas) {
            if (c1.getId().equals(id)) {
                return c1;
            }
        }
        return null;
    }

    /**
     * metodo que muestra todas las citas del paciente que se le ingrese
     * @param idPaciente ID del paciente
     * @return Lista que contiene todas las citas del paciente
     */

    public List<Cita> verCitasPaciente(String idPaciente) {
        return citas.stream()
                .filter(cita -> cita.getPaciente().getId().equals(idPaciente))
                .filter(cita -> !cita.getEstadoCita().equals(citaState.CANCELADA))
                .sorted(Comparator.comparing(Cita::getFecha))
                .collect(Collectors.toList());
    }

    /**
     * metodo que muestra todas las citas que el medico tiene en su agenda
     * @param idMedico ID del medico
     * @return Lista que contiene todas las citas que tiene el medico en su agenda
     */
    public List<Cita> verAgendaMedico(String idMedico) {
        return citas.stream()
                .filter(cita -> cita.getMedico().getId().equals(idMedico))
                .filter(cita -> !cita.getEstadoCita().equals(citaState.CANCELADA))
                .sorted(Comparator.comparing(Cita::getFecha))
                .collect(Collectors.toList());
    }

    /*
     * Metodos que validan si la hora de una cita no se solapa o se cruza con la hora de otra cita.
     * Se utiliza para validar el horario o la disponibilidad del medico y del consultorio.
     */

    /**
     * @param medico objeto de clase Medico
     * @param fechaHora hora de la cita nueva
     * @return true o false dependiendo de la disponibilidad del medico
     */

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

    /**
     *
     * @param consultorio objeto de clase Consultorio
     * @param fechaHora hora de la nueva cita
     * @return true o false dependiendo de si esta disponible el consultorio en la hora de la cita
     */
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

    /**
     * metodo encargado de evaluar las credenciales de los usuarios en los inicios de sesion
     * <p>
     *     comprueba si el id y la contraseña ingresada coinciden con algun
     *     usuario que se encuentre en el archivo
     * </p>
     * @param id ID unica del usuario
     * @param password contraseña del usuario
     * @return
     */

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
        return usuario.getTipo(); //Puede entrar cualquier objeto que herede de usuario y va directamente al metodo override en la subclase
    }

    /**
     * metodo para reservar o apartar una cita medica
     * <p>
     *     reserva una cita medica siempre y cuando todos los parametros esten disponibles,
     *     dado caso que ocurra algun fallo, el programa lanza una excepcion
     * </p>
     * @param idPaciente id del paciente
     * @param idMedico id del medico
     * @param numeroConsultorio numero del consultorio en el que se va a realizar la cita
     * @param motivo motivo de la cita
     * @param fecha fecha en la que se quiere apartar la cita
     * @return Cita si todos los parametros pasan y no ocurre ningun fallo y si la hora no se cruza con otra cita
     */

    public Cita reservarCita(String idPaciente, String idMedico, String numeroConsultorio, String motivo, LocalDateTime fecha) {
        Paciente paciente = (Paciente) searchUserById(idPaciente);
        Medico medico = (Medico) searchUserById(idMedico);
        Consultorio consultorio = searchConsultorioByNumero(numeroConsultorio);

        if(fecha.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("La fecha y hora de la cita no puede ser en el pasado.");
        }

        if(paciente == null || medico == null || consultorio == null){
            throw new IllegalArgumentException("ERROR!. Alguno de los datos ingresados no es válido.");
        }

        // Si el médico no tiene consultorio asignado, asignarle el seleccionado
        if(medico.getConsultorioAsignado() == null || medico.getConsultorioAsignado().isEmpty()){
            medico.setConsultorioAsignado(numeroConsultorio);
        }

        if(!validarHorarioMedico(medico, fecha)){
            throw new IllegalArgumentException("El médico no está disponible en la fecha y hora seleccionadas.");
        }

        if(!validarHorarioConsultorio(consultorio, fecha)){
            throw new IllegalArgumentException("El consultorio no está disponible en la fecha y hora seleccionadas.");
        }

        String idCita = generadorIdCita();
        Cita nuevaCita = new Cita(idCita, paciente, medico, consultorio, motivo, fecha);
        citas.add(nuevaCita);
        medico.agregarCita(nuevaCita);
        return nuevaCita;
    }

    /**
     * metodo que se encarga de cancelar la cita que reciba como parametro
     * <p>
     *     recibe el id de la cita a cancelar y del paciente que apartó la cita.
     *     si el usuario existe y si la cita existe y no esta cancelada, terminada
     *     o en atencion, la cancela, caso contrario lanza una excepcion
     * </p>
     * @param idCita ID  de la cita a cancelar
     * @param idPaciente ID del paciente
     * @return true o false si la cita se canceló con exito
     */
    public boolean cancelarCita(String idCita, String idPaciente) {
        Cita cita = searchCitaById(idCita);
        if (cita != null && cita.getPaciente().getId().equals(idPaciente)) {
            if (cita.getEstadoCita() == citaState.PENDIENTE || cita.getEstadoCita() == citaState.CONFIRMADA) {
                cita.cancelarCita();
                return true;
            } else {
                throw new IllegalStateException("La cita no se puede cancelar porque ya ha sido atendida o cancelada.");
            }
        }
        return false;
    }

    /**
     * metodo que se encarga de reprogramar cita siempre y cuando la cita no haya terminado o no haya sido cancelada
     * y si el horario nuevo esta libre.
     * @param idCita ID de la cita a reprogramar
     * @param nuevaFecha nueva fecha de la cita
     * @return true or false si el horario de la cita se encuentra disponible
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
        return true;
    }

    /**
     * metodo por el cual el paciente puede revisar su historia clinica
     * @param idPaciente ID del paciente
     * @return String que contiene todos los datos del paciente que maneja el sistema
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
     * metodo en el cual el medico puede revisar que paciente va a atender
     * @param idPaciente ID del paciente
     * @return retorna el paciente siempre y cuando se encuentre guardado en la lista
     */
    public Paciente verPaciente(String idPaciente) {
        return (Paciente) searchUserById(idPaciente);
    }

    /**
     * metodo en el que el medico da por atendida la cita
     * @param idCita ID de la cita
     * @param diagnostico diagnostico que el paciente la da al paciente
     * @param tratamiento tratamiento establecido por el medico
     * @param observaciones observaciones del medico
     * @return true o false
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
            return true;
        }
        return false;
    }

    /**
     * metodo en el cual el medico puede ver el consultorio que se le ha sido asignado
     * @param idMedico ID del medico
     * @return el consultorio que le toca al medico
     */
    public Consultorio verConsultorioAsignado(String idMedico) {
        Medico medico = (Medico) searchUserById(idMedico);
        if(medico != null && medico.getConsultorioAsignado() != null){
            return searchConsultorioByNumero(medico.getConsultorioAsignado());
        }
        return null;
    }

    /**
     * metodo que se encarga de remitir un paciente a otra especialidad
     * <p>
     *     este metodo se encarga de remitir un paciente a otra especialidad
     *     dado caso que se llegue a necesitar. Este metodo verifica que exista
     *     la cita con el id ingresado, busca medicos en la especialidad solicitada
     *     y lo remite siempre y cuando se encuentren medicos en la especialidad
     * </p>
     * @param idCita ID de la cita
     * @param especialidad especialidad que se necesita
     * @param motivo motivo de la remision del paciente
     * @return un String que contiene todos los datos de la remision del paciente siempre y cuando sea exitosa
     */
    public String remitirPaciente(String idCita, String especialidad, String motivo){
        Cita cita = searchCitaById(idCita);
        if(cita == null){
            throw new IllegalArgumentException("La cita no se encuentra.");
        }

        List<Medico> especialistas = medicos.stream()
                .filter(doc -> doc.getEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());

        if(especialistas.isEmpty()){
            return "No hay especialistas disponibles en la especialidad " + especialidad;
        }

        StringBuilder sb = new StringBuilder();
        boolean creada = false;
        Medico medicoAsignado = especialistas.get(0);
        Paciente paciente = cita.getPaciente();
        Consultorio consultorioAsignado = null;
        LocalDateTime propuesta = LocalDateTime.now().plusHours(1);
        int minutos = propuesta.getMinute() < 30 ? 0 : 30; // si los minutos tienen un valor menor que 30, se redondea a 30, si no se rendondea a cero
        propuesta = propuesta.withMinute(minutos).withSecond(0).withNano(0);
        LocalDateTime limiteHorario = propuesta.plusDays(30);

        while(!creada && !propuesta.isAfter(limiteHorario)){
            if(validarHorarioMedico(medicoAsignado, propuesta)){
                if(consultorioAsignado == null || !validarHorarioConsultorio(consultorioAsignado, propuesta)){
                    for(Consultorio c: consultorios){
                        if(validarHorarioConsultorio(c, propuesta)){
                            consultorioAsignado = c;
                            break;
                        }
                    }
                }

                if(consultorioAsignado != null){
                    String idNuevaCita = generadorIdCita();
                    Cita nuevaCita = new Cita(idNuevaCita, paciente, medicoAsignado, consultorioAsignado, motivo, propuesta);
                    citas.add(nuevaCita);
                    medicoAsignado.agregarCita(nuevaCita);
                    sb.append("\n ======= Cita creada para remision de paciente =======  \n");
                    sb.append(nuevaCita).append("\n");// automaticamente se llama al metodo toString
                    creada = true;
                    cita.completar();
                    break;
                    //si logró agendar una cita, rompe el ciclo enseguida
                }
            }
            propuesta = propuesta.plusMinutes(30); // si no logra agendar cita en el horario, suma 30 minutos mas y vuelva a iterar hasta que encuentre un horario libre
        }

        if(!creada) {
            sb.append("\n No se encontro un horario disponible con algun especialista en los proximos 30 dias. \n");
        }

        return sb.toString();
    }

    //Metodos de recepcionista//

    /**
     * Registra un nuevo médico en el sistema.
     * <p>
     *     Crea una nueva instancia de la clase Medico con los datos proporcionados
     *     y la añade a la lista de médicos registrados en el sistema.
     * </p>
     *
     * @param id el identificador único del médico
     * @param nombre el nombre del médico
     * @param apellido el apellido del médico
     * @param telefono el número de teléfono de contacto del médico
     * @param email la dirección de correo electrónico del médico
     * @param password la contraseña asignada al médico para acceder al sistema
     * @param especialidad la especialidad médica del profesional
     * @return el objeto Medico registrado con todos sus datos
     */
    public Medico registrarMedico(String id, String nombre, String apellido, String telefono, String email, String password, String especialidad) {
        Medico nuevoMedico = new Medico(id, nombre, apellido, telefono, email, password, especialidad);
        for (Medico m : gestionarUsuario.getMedicos()) {
            if (m.getId().equals(id)) {
                System.out.println("Ya existe un recepcionista con ese ID");
                return null;
            }
        }
        medicos.add(nuevoMedico);
        gestionarUsuario.getMedicos().add(nuevoMedico);
        gestionarUsuario.guardarEnArchivo();
        return nuevoMedico;
    }

    /**
     * Registra un nuevo recepcionista en el sistema
     * @param id ID del recepcionista
     * @param nombre Nombre del recepcionista
     * @param apellido Apellido del recepcionista
     * @param telefono Telefono del recepcionista
     * @param email Email del recepcionista
     * @param password Contraseña del recepcionista
     * @param state Turno del recepcionista (Día/Noche)
     * @return El objeto Recepcionista creado o null si ya existe
     */
    public Recepcionista registrarRecepcionista(String id, String nombre, String apellido,
                                                String telefono, String email, String password,
                                                String state) {
        for (Recepcionista r : recepcionistas) {
            if (r.getId().equals(id)) {
                System.out.println("Ya existe un recepcionista con ese ID");
                return null;
            }
        }
        Recepcionista nuevoRecepcionista = new Recepcionista(id, nombre, apellido, telefono, email, password, state);
        recepcionistas.add(nuevoRecepcionista);
        gestionarUsuario.getRecepcionistas().add(nuevoRecepcionista);
        gestionarUsuario.guardarEnArchivo();
        System.out.println("Recepcionista registrado exitosamente: " + nuevoRecepcionista.nombreCompleto());
        return nuevoRecepcionista;
    }

    /**
     * Método que crea un nuevo paciente y lo añade a la lista de los demás.
     * @param id ID del paciente.
     * @param nombre Nombre del paciente.
     * @param apellido Apellido del paciente.
     * @param telefono Telefono del paciente.
     * @param email Email del paciente.
     * @param password Contraseña del paciente.
     * @param historiaClinica Historia clínica del paciente.
     * @param fechaNacimiento Fecha de nacimiento del paciente.
     * @param tipoSangre Tipo de sangre del paciente.
     * @param sexo Sexo del paciente.
     * @return El nuevo paciente.
     */
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
        pacientes.add(nuevoPaciente);
        gestionarUsuario.getPacientes().add(nuevoPaciente);
        gestionarUsuario.guardarEnArchivo();
        return nuevoPaciente;
    }

    /**
     * Método para asignarle un consultorio a un médico.
     * @param idMedico ID del médico.
     * @param numeroConsultorio Numero del consultorio.
     * @param fecha Fecha de cuando se le asignará el consultorio.
     * @return Si se le asignó el consultorio o si no se pudo asignar.
     */
    public boolean asignarConsultorioAMedico(String idMedico, String numeroConsultorio, LocalDateTime fecha) {
        Medico medico = (Medico) searchUserById(idMedico);
        Consultorio consultorio = searchConsultorioByNumero(numeroConsultorio);

        if(medico != null && consultorio != null) {
            medico.setConsultorioAsignado(numeroConsultorio);
            return true;
        }
        return false;
    }

    /**
     * Método para ver la información de un paciente a partir de su ID.
     * @param idPaciente ID del paciente.
     * @return Información del paciente.
     */
    public String consultarPaciente(String idPaciente) {
        Paciente paciente = (Paciente) searchUserById(idPaciente);
        if (paciente != null) {
            return paciente.toString();
        }
        return null;
    }

    /**
     * Método para ver la información de un médico a partir de su ID.
     * @param idMedico ID del médico.
     * @return Información del médico.
     */
    public String consultarMedico(String idMedico) {
        Medico medico = (Medico) searchUserById(idMedico);
        if(medico != null) {
            return medico.toString();
        }
        return null;
    }

    /**
     * Método para consultar si un médico esta disponible según la fecha.
     * @param idMedico ID del médico.
     * @param fecha Fecha a consultar.
     * @return Si el médico está o no está disponible en esa fecha.
     */
    public boolean consultarDisponibilidadMedico(String idMedico, LocalDateTime fecha) {

        Medico medico = (Medico) searchUserById(idMedico);
        if(medico == null) {
            return false;
        }

        return validarHorarioMedico(medico, fecha);
    }

    /**
     * Método para consultar si un consultorio está disponible según la fecha.
     * @param numeroConsultorio Número del consultorio
     * @param fecha Fecha en la que se consultará la disponibilidad.
     * @return Si el consultorio está o no está disponible en la fecha consultada.
     */
    public boolean consultarDisponibilidadConsultorio(String numeroConsultorio, LocalDateTime fecha) {

        Consultorio consultorio = searchConsultorioByNumero(numeroConsultorio);
        if(consultorio == null) {
            return false;
        }

        return validarHorarioConsultorio(consultorio, fecha);
    }

    public String obtenerDetalleConsultorios() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Consultorio consultorio : consultorios) {
            sb.append("Consultorio: ").append(consultorio.getNumero()).append("\n");
            sb.append("Ubicación: ").append(consultorio.getUbicacion()).append("\n");
            sb.append("Estado: ").append(consultorio.isDisponibilidad() ? "Disponible" : "Ocupado").append("\n");

            // Buscar citas programadas en este consultorio
            List<Cita> citasConsultorio = citas.stream()
                    .filter(c -> c.getConsultorio().getNumero().equals(consultorio.getNumero()))
                    .filter(c -> c.getEstadoCita() != citaState.CANCELADA && c.getEstadoCita() != citaState.COMPLETADA)
                    .filter(c -> c.getFecha().isAfter(LocalDateTime.now())) // Solo citas futuras
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

    /**
     * metodos encargados generar listas de objetos objetos registrados en el sistema
     * <p>
     *     estos metodos hacen una copia a las listas originales
     *     para que al modificar las listas, solo se modifiquen la copie
     *     y que la lista original no se modifique.
     * </p>
     * @return
     */

    /**
     * Obtiene una lista con todos los pacientes registrados en el sistema.
     * @return una lista de objetos Paciente registrados en el sistema
     */
    public List<Paciente> enlistarPacientes(){
        return new ArrayList<>(pacientes);
    }

    /**
     * Obtiene una lista con todos los médicos registrados en el sistema.
     * @return una lista de objetos Medico registrados en el sistema
     */
    public List<Medico> enlistarMedicos() {
        return new ArrayList<>(medicos);
    }

    /**
     * Obtiene una lista con todos los consultorios disponibles en el sistema.
     * @return una lista de objetos Consultorio registrados en el sistema
     */
    public List<Consultorio> enlistarConsultorios() {
        return new ArrayList<>(consultorios);
    }

    /**
     * Obtiene una lista con todas las citas médicas registradas en el sistema.
     * @return una lista de objetos Cita registrados en el sistema
     */
    public List<Cita> enlistarCitas() {
        return new ArrayList<>(citas);
    }
}