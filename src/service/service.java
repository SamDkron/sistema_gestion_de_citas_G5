/**
 * @author Samuel David Dau Fernández, Santiago Duica Plata, Gustavo Daniel Olivos Rodríguez
 */

package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import data.DatosEjemplo;
import modelo.*;

/**
 * Archivo 'service' que contiene toda la logica de negocio del programa.
 * <p>
 *     aca esta la logica de la mayoria de los metodos que fueron ideados en los diagramas y
 *     gestiona las citas, pacientes, medicos, recepcionistas y consultorios
 * </p>
 */

public class service {
    private List<Cita> citas;
    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Consultorio> consultorios;
    private List<Recepcionista> recepcionista;
    private int contadorMedicos;
    private int contadorPacientes;
    private int contadorConsultorios;
    private int contadorCitas;

    /**
    * constructor del programa
    */
    public service() {
        this.citas = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.consultorios = new ArrayList<>();
        this.recepcionista = new ArrayList<>();
        this.contadorMedicos = 4;
        this.contadorPacientes = 4;
        this.contadorConsultorios = 5;
        this.contadorCitas = 1;

        this.recepcionista.addAll(DatosEjemplo.inicializarRecepcionista());
        this.consultorios.addAll(DatosEjemplo.inicializarConsultorioEJ());
        this.medicos.addAll(DatosEjemplo.inicializarMedicoEJ());
        this.pacientes.addAll(DatosEjemplo.inicializarPacienteEJ());

//        if(ejemplos) {
//            cargarEjemplos();
//        }

    }

//    public service() {
//        this(true);
//    }
//
//    public void cargarEjemplos(){
//        this.recepcionista.addAll(DatosEjemplo.inicializarRecepcionista());
//        this.consultorios.addAll(DatosEjemplo.inicializarConsultorioEJ());
//        this.medicos.addAll(DatosEjemplo.inicializarMedicoEJ());
//        this.pacientes.addAll(DatosEjemplo.inicializarPacienteEJ());
//    }




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
    public Paciente searchPacienteById(String id) {
        for (Paciente p1 : pacientes) {
            if (p1.getId().equals(id)) {
                return p1;
            }
        }
        return null;
    }

    public Medico searchMedicoById(String id) {
        for (Medico m1 : medicos) {
            if (m1.getId().equals(id)) {
                return m1;
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
     *     usuario que se encuentre guardado en alguna de las listas
     * </p>
     * @param id ID unica del usuario
     * @param password contraseña del usuario
     * @return
     */
    public Usuario iniciarSesion (String id, String password){
        for (Paciente p1 : pacientes) {
            if (p1.getId().equals(id) && p1.getPassword().equals(password)) {
                return p1;
            }
        }
        for (Medico m1 : medicos) {
            if (m1.getId().equals(id) && m1.getPassword().equals(password)) {
                return m1;
            }
        }
        for (Recepcionista r1 : recepcionista) {
            if (r1.getId().equals(id) && r1.getPassword().equals(password)) {
                return r1;
            }
        }
        return null;
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
        Paciente paciente = searchPacienteById(idPaciente);
        Medico medico = searchMedicoById(idMedico);
        Consultorio consultorio = searchConsultorioByNumero(numeroConsultorio);

        if(fecha.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("La fecha y hora de la cita no puede ser en el pasado.");
        }

        if(paciente == null || medico == null || consultorio == null){
            throw new IllegalArgumentException("ERROR!. Alguno de los datos ingresados no es válido.");
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
        Paciente paciente = searchPacienteById(idPaciente);
        if(paciente == null){
            throw new IllegalArgumentException("La paciente no se encuentra.");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Paciente:\n");
        sb.append("Historia clínica del paciente");
        sb.append(String.format("Nombre: %s\n", paciente.nombreCompleto()));
        sb.append(String.format("Historia Clínica: %s\n", paciente.getHistoriaClinica()));
        sb.append(String.format("Grupo Sanguíneo: %s\n", paciente.getTipoSangre()));
        sb.append(String.format("Fecha De Nacimiento: %s\n", paciente.getFechaNacimiento()));
        sb.append(String.format("Sexo: %s\n", paciente.getSexo()));
        sb.append(String.format("Número de Telefono: %s\n", paciente.getTelefono()));

        List<Cita> pacienteCitas = citas.stream()
                .filter(c -> c.getPaciente().getId().equals(paciente.getId()))
                .filter(c -> c.getEstadoCita() == citaState.COMPLETADA)
                .sorted(Comparator.comparing(Cita::getFecha).reversed())
                .collect(Collectors.toList());

        sb.append("Historial de citas: \n");

        if(pacienteCitas.isEmpty()){
            sb.append("No hay citas completadas.\n");
        } else {
            for (Cita c : pacienteCitas) {
                sb.append(c.toString()).append("\n");
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
        return searchPacienteById(idPaciente);
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

        if(cita != null && cita.getEstadoCita() == citaState.CONFIRMADA){
            cita.iniciarAtencion();
            cita.setDiagnostico(diagnostico);
            cita.setTratamiento(tratamiento);
            cita.setObservaciones(observaciones);
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
        Medico medico = searchMedicoById(idMedico);
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
                .toList();

        if(especialistas.isEmpty()){
            return "No hay especialistas disponibles en la especialidad " + especialidad;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("REMITIR PACIENTE A ESPECIALISTA");
        sb.append(String.format("Paciente: %s\n", cita.getPaciente().nombreCompleto()));
        sb.append(String.format("Motivo: %s\n\n", motivo));
        sb.append("Especialistas: " + especialistas + "\n");
        for(Medico m : especialistas){
            sb.append(String.format("Dr(a). %s / Consultorio: %s\n  / Especialidad: %s\n" + m.nombreCompleto() + m.getConsultorioAsignado() + m.getEspecialidad()));
        }
        return sb.toString();
    }

    //Metodos de recepcionista//

    public Medico registrarMedico(String id, String nombre, String apellido, String telefono, String email, String password, String especialidad) {
        Medico nuevoMedico = new Medico(id, nombre, apellido, telefono, email, password, especialidad);
        medicos.add(nuevoMedico);
        return nuevoMedico;
    }

    public Paciente registrarPaciente(String id, String nombre, String apellido, String telefono,
                                      String email, String password, String historiaClinica,
                                      String fechaNacimiento, String tipoSangre, String sexo) {
        Paciente nuevoPaciente = new Paciente(id, nombre, apellido, telefono, email, password,
                historiaClinica, fechaNacimiento, tipoSangre, sexo);
        pacientes.add(nuevoPaciente);
        return nuevoPaciente;
    }

    public boolean asignarConsultorioAMedico(String idMedico, String numeroConsultorio, LocalDateTime fecha) {

        Medico medico = searchMedicoById(idMedico);
        Consultorio consultorio = searchConsultorioByNumero(numeroConsultorio);

        if(medico != null && consultorio != null && consultarDisponibilidadConsultorio(numeroConsultorio, fecha)
                && consultarDisponibilidadMedico(idMedico, fecha)) {

            medico.setConsultorioAsignado(numeroConsultorio);
            return true;
        }
        return false;
    }

    public String consultarPaciente(String idPaciente) {
        Paciente paciente = searchPacienteById(idPaciente);
        if (paciente != null) {
            return paciente.toString();
        }
        return null;
    }

    public String consultarMedico(String idMedico) {
        Medico medico = searchMedicoById(idMedico);
        if(medico != null) {
            return medico.toString();
        }
        return null;
    }

    public boolean consultarDisponibilidadMedico(String idMedico, LocalDateTime fecha) {

        Medico medico = searchMedicoById(idMedico);
        if(medico == null) {
            return false;
        }

        return validarHorarioMedico(medico, fecha);
    }

    public boolean consultarDisponibilidadConsultorio(String numeroConsultorio, LocalDateTime fecha) {

        Consultorio consultorio = searchConsultorioByNumero(numeroConsultorio);
        if(consultorio == null) {
            return false;
        }

        return validarHorarioConsultorio(consultorio, fecha);
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

    public List<Paciente> enlistarPacientes(){
        return new ArrayList<>(pacientes);
    }

    public List<Medico> enlistarMedicos() {
        return new ArrayList<>(medicos);
    }

    public List<Consultorio> enlistarConsultorios() {
        return new ArrayList<>(consultorios);
    }

    public List<Cita> enlistarCitas() {
        return new ArrayList<>(citas);
    }
}
