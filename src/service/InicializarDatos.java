package service;

import data.DatosEjemplo;
import modelo.*;

import java.util.List;

public class InicializarDatos {
    private GestionarUsuario gestionarUsuario;

    public InicializarDatos(GestionarUsuario gestionarUsuario) {
        this.gestionarUsuario = gestionarUsuario;
    }

    public void inicializarDatosEjemplo() {
        if (gestionarUsuario.getPacientes().isEmpty()
                || gestionarUsuario.getMedicos().isEmpty()
                || gestionarUsuario.getRecepcionistas().isEmpty()
                || gestionarUsuario.getConsultorios().isEmpty()) {

            gestionarUsuario.getPacientes().clear();
            gestionarUsuario.getMedicos().clear();
            gestionarUsuario.getRecepcionistas().clear();
            gestionarUsuario.getConsultorios().clear();

            agregarPacientesEjemplo();
            agregarMedicosEjemplo();
            agregarRecepcionistasEjemplo();
            agregarConsultoriosEjemplo();

            gestionarUsuario.guardarEnArchivo();
        }
    }

    private void agregarPacientesEjemplo() {
        try {
            List<Paciente> pacientes = DatosEjemplo.inicializarPacienteEJ();
            if (pacientes != null) {
                for (Paciente paciente : pacientes) {
                    gestionarUsuario.getPacientes().add(paciente);
                }
            }
        } catch (Exception ignored) { }
    }

    private void agregarMedicosEjemplo() {
        try {
            List<Medico> medicos = DatosEjemplo.inicializarMedicoEJ();
            if (medicos != null) {
                for (Medico medico : medicos) {
                    gestionarUsuario.getMedicos().add(medico);
                }
            }
        } catch (Exception ignored) { }
    }

    private void agregarRecepcionistasEjemplo() {
        try {
            List<Recepcionista> recepcs = DatosEjemplo.inicializarRecepcionista();
            if (recepcs != null) {
                for (Recepcionista r : recepcs) {
                    gestionarUsuario.getRecepcionistas().add(r);
                }
            }
        } catch (Exception ignored) { }
    }

    private void agregarConsultoriosEjemplo() {
        try {
            List<Consultorio> consultorios = DatosEjemplo.inicializarConsultorioEJ();
            if (consultorios != null && !consultorios.isEmpty()) {
                for (Consultorio c : consultorios) {
                    gestionarUsuario.getConsultorios().add(c);
                }
            } else {
                gestionarUsuario.getConsultorios().add(new Consultorio("1", true, "Consultorio Piso 1"));
                gestionarUsuario.getConsultorios().add(new Consultorio("2", true, "Consultorio Piso 1"));
            }
        } catch (Exception ignored) {
            gestionarUsuario.getConsultorios().add(new Consultorio("1", true, "Consultorio Piso 1"));
            gestionarUsuario.getConsultorios().add(new Consultorio("2", true, "Consultorio Piso 1"));
        }
    }
}