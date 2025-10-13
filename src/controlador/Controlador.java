package controlador;

import modelo.*;
import service.service;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class Controlador {
    private service service;

    public Controlador(service service) {
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
        return verPaciente(idPaciente);
    }

    public Consultorio verConsultorioAsignado(String idMedico){
        return verConsultorioAsignado(idMedico);
    }

    public boolean atenderCita(String idCita, String diagnostico, String tratamiento, String observaciones){
        return atenderCita(idCita, diagnostico, tratamiento, observaciones);
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
        return searchPacienteById(idPaciente);
    }

    public Medico searchMedicoById(String idMedico){
        return searchMedicoById(idMedico);
    }

    public Consultorio searchConsultorioByNumero(String numeroConsultorio){
        return searchConsultorioByNumero(numeroConsultorio);
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

