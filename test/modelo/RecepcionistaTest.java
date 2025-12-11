package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecepcionistaTest {
    Recepcionista r = new Recepcionista("R123456", "Gustavo", "Olivos", "3046659511", "golivos@gmail.com", "12345678", "turnoActivo");

    @Test
    void getStateTest() {

        String resultado = r.getState();
        assertEquals("turnoActivo", resultado);
    }

    @Test
    void setStateTest() {
        r.setState("Suspendido");
        String resultado = r.getState();
        assertEquals("Suspendido", resultado);

    }

    @Test
    void fromCSVTest() {
        Recepcionista r2 = Recepcionista.fromCSV("R123456;Gustavo;Olivos;3046659511;golivos@gmail.com;12345678;turnoActivo");

        assertEquals(r.getId(), r2.getId());
        assertEquals(r.getNombre(), r2.getNombre());
        assertEquals(r.getApellido(), r2.getApellido());
        assertEquals(r.getEmail(), r2.getEmail());

    }
    @Test
    void toCSVTest() {

        String resultado = r.toCSV();
        String esperado ="R123456;Gustavo;Olivos;3046659511;golivos@gmail.com;12345678;turnoActivo";
        assertEquals(esperado, resultado);
    }

    @Test
    void getTipoTest() {
        String resultado = r.getTipo();
        assertEquals("Recepcionista", resultado);
    }

    @Test
    void toStringTest() {
        String resultado = r.toString();

        assertTrue(resultado.contains("Recepcionista:"));
        assertTrue(resultado.contains("turnoActivo"));
        assertTrue(resultado.contains("Gustavo"));
    }
}