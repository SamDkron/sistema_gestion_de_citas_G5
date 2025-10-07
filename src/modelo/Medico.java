package modelo;

import java.util.ArrayList;
import java.util.List;

public class Medico extends Usuario {
    private String consultorioAsignado;
    private String especialidad;
    private List<Cita> agenda;

    public Medico(String id, String nombre, String apellido, String telefono, String email, String password, String especialidad) {
        super(id, nombre, apellido, telefono, email, password);
        this.especialidad = especialidad;
        this.consultorioAsignado = "";
        this.agenda = new ArrayList<>(agenda);
    }

    public String getConsultorioAsignado() {
        return consultorioAsignado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public List<Cita> getAgenda() {
        return agenda;
    }

    public void setConsultorioAsignado(String consultorio) {
        this.consultorioAsignado = consultorio;
    }

    public void agregarCita(Cita cita) {
        this.agenda.add(cita);
    }

    public void removerCita(Cita cita) {
        this.agenda.remove(cita);
    }

    @Override
    public String toString() {
        return String.format("Médico - %s /// Especialidad: %s /// Consultorio: %s", super.toString(), especialidad, consultorioAsignado != null ? consultorioAsignado : "Sin asignar");
    }
}
