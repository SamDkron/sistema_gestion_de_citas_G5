/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package data;

import modelo.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de inicializar todos los datos de ejemplo del sistema.
 * <p>
 * Se encarga de la creación de pacientes, médicos, consultorios y recepcionistas
 * para usarlos como datos de ejemplo para probar el funcionamiento del sistema.
 * </p>
 */
public class DatosEjemplo {

    /**
     * Inicializa una lista de consultorios con datos de ejemplo.
     * @return una lista de objetos Consultorio con información predefinida.
     */
    public static List<Consultorio> inicializarConsultorioEJ() {
        List<Consultorio> consultorios = new ArrayList<>();

        consultorios.add(new Consultorio("1", true, "Mar caribe norte, piso 2"));
        consultorios.add(new Consultorio("2", true, "Mar caribe sur, piso 1"));
        consultorios.add(new Consultorio("3", true, "Mar caribe sur, piso 2"));
        consultorios.add(new Consultorio("4", true, "Mar caribe norte, piso 1"));
        consultorios.add(new Consultorio("5", true, "Mar caribe sur, piso 2"));
        return consultorios;
    }

    /**
     * Inicializa una lista de recepcionistas con datos de ejemplo.
     * @return una lista de objetos Recepcionista con información predefinida.
     */
    public static List<Recepcionista> inicializarRecepcionista() {
        List<Recepcionista> recepcionistas = new ArrayList<>();

        recepcionistas.add(new Recepcionista("R123456", "Gustavo", "Olivos", "3046659511", "golivos@gmail.com", "12345678", "turnoActivo"));
        return recepcionistas;
    }

    /**
     * Inicializa una lista de médicos con datos de ejemplo.
     * @return una lista de objetos Medico con información predefinida.
     */
    public static List<Medico> inicializarMedicoEJ() {
        List<Medico> medicos = new ArrayList<>();

        medicos.add(new Medico("M001", "Carlos", "Ramírez", "555-0101", "cramirez@hospital.com", "doc121", "Medicina General"));
        medicos.add(new Medico("M002", "Ana", "Martínez", "555-0102", "amartinez@hospital.com", "doc122", "Cardiología"));
        medicos.add(new Medico("M003", "Luis", "González", "555-0103", "lgonzalez@hospital.com", "doc123", "Pediatría"));
        medicos.add(new Medico("M004", "José", "Mosquera", "555-0155", "@jmosquera@hospital.com", "doc124", "Neurología"));
        return medicos;
    }

    /**
     * Inicializa una lista de pacientes con datos de ejemplo.
     * @return una lista de objetos Paciente con información predefinida.
     */
    public static List<Paciente> inicializarPacienteEJ() {
        List<Paciente> pacientes = new ArrayList<>();

        pacientes.add(new Paciente("P001", "María", "López", "777-1001", "mlopez@hmail.com", "paciente120", "HC-001", "15/03/1985", "O+", "Femenino"));
        pacientes.add(new Paciente("P002", "Juan", "Plata", "777-1002", "jplata@hmail.com", "paciente121", "HC-002", "22/07/1990", "A+", "Masculino"));
        pacientes.add(new Paciente("P003", "Camilo", "Medina", "777-1003", "cmedina@hmail.com", "paciente122", "HC-003", "5/01/2005", "B-", "Masculino"));
        pacientes.add(new Paciente("P004", "Laura", "Fernández", "777-1004", "lfernandez@hmail.com", "paciente123", "HC-004", "10/11/1978", "B+", "Femenino"));
        return pacientes;
    }
}
