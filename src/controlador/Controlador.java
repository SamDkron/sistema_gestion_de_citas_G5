package controlador;

import modelo.*;
import service.Service;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;


/**
 * La clase controlador funciona como intermediario entre la vista y el service
 * <p>
 *     Se encarga mayormente de controlar las interacciones entre la vista y
 *     la capa service. Esta clase recibe las solicitudes o interacciones del
 *     usuario, las dirige al service, procesa las solicitudes y envia la
 *     respuesta del service a la vista.
 * </p>
 */
public class Controlador {
    private Service service;
    private Recepcionista recepcionista;

    /**
     * Constructor de la clase
     * @param service recibe una instancia de la clase service
     */
    public Controlador(Service service) {
        this.service = service;
    }

    public Usuario iniciarSesion(String id, String password){
        return service.iniciarSesion(id, password);
    }

    public Cita reservarCita(String idPaciente, String idMedico, String numeroConsultorio, String motivo, LocalDateTime fecha){
        return service.reservarCita(idPaciente, idMedico, numeroConsultorio, motivo, fecha);
    }

    public boolean cancelarCita(String idCita, String idPaciente){
        return service.cancelarCita(idCita, idPaciente);
    }

    public boolean reprogramarCitas(String idCita, LocalDateTime nuevaFecha){
        try{
            return service.reprogramarCita(idCita, nuevaFecha);
        } catch(Exception e){
            System.err.println("ERROR!" + e.getMessage());
            return false;
        }
    }

    public String consultarHistoriaClinica(String idPaciente){
        return service.consultarHistoriaClinicaPaciente(idPaciente);
    }

    // metodos del medico

    public Paciente verPaciente(String idPaciente){
        return service.verPaciente(idPaciente);
    }

    public Consultorio verConsultorioAsignado(String idMedico){
        return service.verConsultorioAsignado(idMedico);
    }

    public boolean atenderCita(String idCita, String diagnostico, String tratamiento, String observaciones){
        return service.atenderCita(idCita, diagnostico, tratamiento, observaciones);
    }

//    public boolean registrarDiagnostico(String idCita, String diagnostico){
//        return service.registrarDiagnostico(idCita, diagnostico);
//    }
//
//    public boolean registrarTratamiento(String idCita, String tratamiento){
//        return service.registrarTratamiento(idCita, tratamiento);
//    }
//
//    public boolean realizarObservaciones(String idCita, String observaciones){
//        return service.realizarObservaciones(idCita, observaciones);
//    }

    public String remitirPaciente(String idCita, String especialidad, String motivo){
        return service.remitirPaciente(idCita, especialidad, motivo);
    }

    public Paciente searchPacienteById(String idPaciente){
        return service.searchPacienteById(idPaciente);
    }

    public Medico searchMedicoById(String idMedico){
        return service.searchMedicoById(idMedico);
    }

    public Consultorio searchConsultorioByNumero(String numeroConsultorio){
        return service.searchConsultorioByNumero(numeroConsultorio);
    }

    //Métodos de recepcionista

    public Medico registrarMedico(String id, String nombre, String apellido, String telefono, String email, String password, String especialidad) {
        return service.registrarMedico(id, nombre, apellido, telefono, email, password, especialidad);
    }

    public Paciente registrarPaciente(String id, String nombre, String apellido, String telefono, String email, String password, String historiaClinica, String fechaNacimiento, String tipoSangre, String sexo) {
        return service.registrarPaciente(id, nombre, apellido, telefono, email, password, historiaClinica, fechaNacimiento, tipoSangre, sexo);
    }

    public boolean asignarConsultorioAMedico(String idMedico, String numeroConsultorio, LocalDateTime fecha) {
        return service.asignarConsultorioAMedico(idMedico, numeroConsultorio, fecha);
    }

    public Recepcionista registrarRecepcionista(String id, String nombre, String apellido,
                                                String telefono, String email, String password,
                                                String state) {
        return service.registrarRecepcionista(id, nombre, apellido, telefono, email, password, state);
    }

    public String consultarPaciente(String idPaciente) {
        return service.consultarPaciente(idPaciente);
    }

    public String consultarMedico(String idMedico) {
        return service.consultarMedico(idMedico);
    }

    public boolean consultarDisponibilidadMedico(String idMedico, LocalDateTime fecha) {
        return service.consultarDisponibilidadMedico(idMedico, fecha);
    }

    public boolean consultarDisponibilidadConsultorio(String numeroConsultorio, LocalDateTime fecha) {
        return service.consultarDisponibilidadConsultorio(numeroConsultorio, fecha);
    }

    // listar objetos

    public List<Medico> enlistarMedicos(){
        return service.enlistarMedicos();
    }

    public List<Paciente> enlistarPacientes(){
        return service.enlistarPacientes();
    }

    public List<Consultorio> enlistarConsultorios(){
        return service.enlistarConsultorios();
    }

    public List<Cita> enlistarCitas(){
        return service.enlistarCitas();
    }
}

