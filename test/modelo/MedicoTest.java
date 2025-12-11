package modelo;

import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicoTest {
    Medico m = new Medico("M001", "Carlos", "Ramírez", "555-0101", "cramirez@hospital.com", "doc121", "Medicina General");
    Paciente p = new Paciente("P001", "María", "López", "777-1001", "mlopez@hmail.com", "paciente120", "HC-001", "15/03/1985", "O+", "Femenino");
    Consultorio c = new Consultorio("1", true, "Mar caribe norte, piso 2");
    LocalDateTime fecha = LocalDateTime.of(2025, 11, 24, 10, 30);
    Cita cita = new Cita("001", p, m, c, "Consulta",fecha );

    @Test
    void getConsultorioAsignadoTest() {
        m.setConsultorioAsignado("Consultorio 1");
        assertEquals("Consultorio 1", m.getConsultorioAsignado());
    }

    @Test
    void getEspecialidadTest() {
        String resultado = m.getEspecialidad();
        assertTrue(resultado.equals("Medicina General"));
    }

    @Test
    void getAgendaTest() {
        List<Cita> agenda = m.getAgenda();


        assertNotNull(agenda);
        assertEquals(0,  agenda.size());
        m.agregarCita(cita);
        assertEquals(1,  agenda.size());
        
    }

    @Test
    void setConsultorioAsignadoTest() {
        m.setConsultorioAsignado("Consultorio 1");
        assertEquals("Consultorio 1", m.getConsultorioAsignado());
    }

    @Test
    void agregarCitaTest() {
        assertTrue(m.getAgenda().isEmpty());
        m.agregarCita(cita);
        assertEquals(1,  m.getAgenda().size());
    }

    @Test
    void removerCita() {
        m.agregarCita(cita);
        assertTrue(!m.getAgenda().isEmpty());
        m.removerCita(cita);
        assertTrue(m.getAgenda().isEmpty());
    }

    @Test
    void fromCSV() {
        Medico m2 = Medico.fromCSV("M001;Carlos;Ramírez;555-0101;cramirez@hospital.com;doc121;Medicina General");

        assertEquals(m.getNombre(), m2.getNombre());
        assertEquals(m.getApellido(), m2.getApellido());
        assertEquals(m.getId(), m2.getId());
    }

    @Test
    void toCSV() {
        String resultado = m.toCSV();
        String esperado = "M001;Carlos;Ramírez;555-0101;cramirez@hospital.com;doc121;Medicina General";
        assertEquals(esperado, resultado);
    }

    @Test
    void getTipo() {
        String resultado = m.getTipo();
        assertTrue(resultado.equals("Medico"));
    }

    @Test
    void testToString() {
        String resultado = m.toString();

        assertTrue(resultado.contains("Carlos Ramírez"));
        assertTrue(resultado.contains("M001"));
        assertTrue(resultado.contains("Medicina General"));

    }
}