package service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import modelo.*;

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

    public service() {
        this.citas = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.consultorios = new ArrayList<>();
        this.recepcionista = new ArrayList<>();
        this.contadorMedicos = 1;
        this.contadorPacientes = 1;
        this.contadorConsultorios = 1;
        this.contadorCitas = 1;
    }

    //metodo para generar id//
    private String generadorIdCita(){
        return String.format("CITA-A%07d", contadorCitas++);
    }
    

    //metodos para buscar por id//
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

    public Consultorio searchConsultorioByNumero(String numero) {
        for (Consultorio c1 : consultorios) {
            if (c1.getNumero().equals(numero)) {
                return c1;
            }
        }
        return null;
    }

    public Cita searchCitaById(String id) {
        for (Cita c1 : citas) {
            if (c1.getId().equals(id)) {
                return c1;
            }
        }
        return null;
    }

    // metodos para ver consultar citas (paciente) y agenda (medico)//

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

    //metodos para validar horarios | disponibilidad //

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

    //metodo para iniciar sesion//

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

    // metodos de paciente//

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

    //metodos de medico//

    public Paciente verPaciente(String idPaciente) {
        return searchPacienteById(idPaciente);
    }

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

    public boolean registrarDiagnostico(String idCita, String diagnostico){
        Cita cita = searchCitaById(idCita);
        if(cita != null){
            cita.setDiagnostico(diagnostico);
            return true;
        }
        return false;
    }

    public boolean registrarTratamiento(String idCita, String tratamiento){
        Cita cita = searchCitaById(idCita);
        if(cita != null){
            cita.setTratamiento(tratamiento);
            return true;
        }
        return false;
    }

    public boolean registrarObservaciones(String idCita, String observaciones){
        Cita cita = searchCitaById(idCita);
        if(cita != null){
            cita.setObservaciones(observaciones);
            cita.completar();
            return true;
        }
        return false;
    }

    public Consultorio verConsultorioAsignado(String idMedico) {
        Medico medico = searchMedicoById(idMedico);
        if(medico != null && medico.getConsultorioAsignado() != null){
            return searchConsultorioByNumero(medico.getConsultorioAsignado());
        }
        return null;
    }
}
