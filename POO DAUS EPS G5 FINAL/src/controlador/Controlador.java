/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package controlador;

import vista.Vista;
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

    /**
     * Inicia sesión en el sistema verificando las credenciales del usuario.
     * @param id el identificador del usuario.
     * @param password la contraseña asociada al usuario.
     * @return un objeto Usuario si las credenciales son válidas; null en caso contrario.
     */
    public Usuario iniciarSesion(String id, String password){
        return service.iniciarSesion(id, password);
    }

    /**
     * Permite reservar una nueva cita médica.
     * @param idPaciente el identificador del paciente.
     * @param idMedico el identificador del médico asignado.
     * @param numeroConsultorio el número del consultorio donde se realizará la cita.
     * @param motivo el motivo de la cita.
     * @param fecha la fecha y hora programada para la cita.
     * @return un objeto Cita si la reserva fue exitosa; null en caso contrario.
     */
    public Cita reservarCita(String idPaciente, String idMedico, String numeroConsultorio, String motivo, LocalDateTime fecha){
        return service.reservarCita(idPaciente, idMedico, numeroConsultorio, motivo, fecha);
    }

    /**
     * Cancela una cita médica programada.
     * @param idCita el identificador de la cita a cancelar
     * @param idPaciente el identificador del paciente que solicita la cancelación
     * @return true si la cita fue cancelada exitosamente; false en caso contrario
     */
    public boolean cancelarCita(String idCita, String idPaciente){
        return service.cancelarCita(idCita, idPaciente);
    }

    /**
     * Reprograma una cita médica existente para una nueva fecha y hora.
     * @param idCita el identificador de la cita a reprogramar
     * @param nuevaFecha la nueva fecha y hora para la cita
     * @return true si la cita fue reprogramada exitosamente; false en caso contrario
     */
    public boolean reprogramarCitas(String idCita, LocalDateTime nuevaFecha){
        try{
            return service.reprogramarCita(idCita, nuevaFecha);
        } catch(Exception e){
            System.err.println("ERROR!" + e.getMessage());
            return false;
        }
    }

    /**
     * Consulta la historia clínica completa de un paciente.
     * @param idPaciente el identificador del paciente cuya historia clínica se desea consultar
     * @return una cadena con la información de la historia clínica del paciente
     */
    public String consultarHistoriaClinica(String idPaciente){
        return service.consultarHistoriaClinicaPaciente(idPaciente);
    }

    /**
     * Registra la atención de una cita médica con su diagnóstico, tratamiento y observaciones.
     * @param idCita el identificador de la cita a atender
     * @param diagnostico el diagnóstico médico realizado durante la consulta
     * @param tratamiento el tratamiento prescrito al paciente
     * @param observaciones las observaciones adicionales sobre la consulta
     * @return true si la cita fue atendida y registrada exitosamente, false en caso contrario
     */
    public boolean atenderCita(String idCita, String diagnostico, String tratamiento, String observaciones){
        return service.atenderCita(idCita, diagnostico, tratamiento, observaciones);
    }

    /**
     * Remite un paciente a un especialista médico.
     * @param idCita el identificador de la cita desde la cual se realiza la remisión
     * @param especialidad la especialidad médica a la que se remite el paciente
     * @param motivo el motivo o razón de la remisión
     * @return una cadena con la información de la remisión generada
     */
    public String remitirPaciente(String idCita, String especialidad, String motivo){
        return service.remitirPaciente(idCita, especialidad, motivo);
    }

    /**
     * Registra un nuevo médico en el sistema.
     * @param id el identificador único del médico
     * @param nombre el nombre del médico
     * @param apellido el apellido del médico
     * @param telefono el número de teléfono de contacto del médico
     * @param email la dirección de correo electrónico del médico
     * @param password la contraseña para acceso al sistema
     * @param especialidad la especialidad médica del profesional
     * @return el objeto Medico registrado con todos sus datos
     */
    public Medico registrarMedico(String id, String nombre, String apellido, String telefono,
                                  String email, String password, String especialidad) {
        return service.registrarMedico(id, nombre, apellido, telefono, email, password, especialidad);
    }

    /**
     * Registra un nuevo paciente en el sistema.
     * @param id el identificador único del paciente
     * @param nombre el nombre del paciente
     * @param apellido el apellido del paciente
     * @param telefono el número de teléfono de contacto del paciente
     * @param email la dirección de correo electrónico del paciente
     * @param password la contraseña para acceso al sistema
     * @param historiaClinica el número de historia clínica del paciente
     * @param fechaNacimiento la fecha de nacimiento del paciente
     * @param tipoSangre el tipo de sangre del paciente
     * @param sexo el sexo del paciente
     * @return el objeto Paciente registrado con todos sus datos
     */
    public Paciente registrarPaciente(String id, String nombre, String apellido, String telefono,
                                      String email, String password, String historiaClinica,
                                      String fechaNacimiento, String tipoSangre, String sexo) {
        return service.registrarPaciente(id, nombre, apellido, telefono, email, password,
                historiaClinica, fechaNacimiento, tipoSangre, sexo);
    }

    /**
     * Asigna un consultorio a un médico para una fecha específica.
     * @param idMedico el identificador del médico al que se asignará el consultorio
     * @param numeroConsultorio el número del consultorio a asignar
     * @param fecha la fecha y hora para la cual se asigna el consultorio
     * @return true si la asignación fue exitosa, false en caso contrario
     */
    public boolean asignarConsultorioAMedico(String idMedico, String numeroConsultorio,
                                             LocalDateTime fecha) {
        return service.asignarConsultorioAMedico(idMedico, numeroConsultorio, fecha);
    }

    /**
     * Registra un nuevo recepcionista en el sistema.
     * @param id el identificador único del recepcionista
     * @param nombre el nombre del recepcionista
     * @param apellido el apellido del recepcionista
     * @param telefono el número de teléfono de contacto del recepcionista
     * @param email la dirección de correo electrónico del recepcionista
     * @param password la contraseña para acceso al sistema
     * @param state el estado o área de trabajo del recepcionista
     * @return el objeto Recepcionista registrado con todos sus datos
     */
    public Recepcionista registrarRecepcionista(String id, String nombre, String apellido,
                                                String telefono, String email, String password,
                                                String state) {
        return service.registrarRecepcionista(id, nombre, apellido, telefono, email, password, state);
    }

    /**
     * Consulta la información completa de un paciente.
     * @param idPaciente el identificador del paciente a consultar
     * @return una cadena con la información detallada del paciente
     */
    public String consultarPaciente(String idPaciente) {
        return service.consultarPaciente(idPaciente);
    }

    /**
     * Consulta la información completa de un médico.
     * @param idMedico el identificador del médico a consultar
     * @return una cadena con la información detallada del médico
     */
    public String consultarMedico(String idMedico) {
        return service.consultarMedico(idMedico);
    }

    /**
     * Obtiene una lista con todos los médicos registrados.
     * @return una lista de objetos Medico.
     */
    public List<Medico> enlistarMedicos(){
        return service.enlistarMedicos();
    }

    /**
     * Obtiene una lista con todos los pacientes registrados.
     * @return una lista de objetos Paciente.
     */
    public List<Paciente> enlistarPacientes(){
        return service.enlistarPacientes();
    }

    /**
     * Obtiene el detalle completo de los consultorios registrados en el sistema.
     * @return una cadena con la información de todos los consultorios.
     */
    public String obtenerDetalleConsultorios(){
        return service.obtenerDetalleConsultorios();
    }

    /**
     * Obtiene una lista con todas las citas registradas.
     * @return una lista de objetos Cita.
     */
    public List<Cita> enlistarCitas(){
        return service.enlistarCitas();
    }
}

