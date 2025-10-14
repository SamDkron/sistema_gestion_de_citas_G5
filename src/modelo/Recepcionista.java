/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;
import service.service;
import java.time.LocalDateTime;

/**
 * Clase que representa al recepcionista en el sistema medico
 */
public class Recepcionista extends Usuario {
    private String state;

    /**
     * Constructor de la clase Recepcionista
     * @param id ID del recepcionista
     * @param nombre Nombre del recepcionista
     * @param apellido Apellido del recepcionista
     * @param telefono Numero de telefono del recepcionista
     * @param email Correo electronico del recepcionista
     * @param password Contraseña del recepcionista
     * @param state Turno del recepcionista(dia/noche)
     */
    public Recepcionista(String id, String nombre, String apellido, String telefono, String email, String password, String state){
        super(id, nombre, apellido, telefono, email, password);
        this.state = state;
    }

    /**
     * Obtiene el estado del recepcionista
     * @return String con el estado del recepcionista
     */
    public String getState() {
        return state;
    }

    /**
     * Asigna el estado del recepcionista
     * @param state Turno en el que se encuentra eel recepcionista
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Formato para imprimir que contiene los datos basicos del recepcionista
     * @return String con la informacion del recepcionista
     */
    @Override
    public String toString() {
        return String.format("Recepcionista: %s /// Turno: %s", super.toString(), state);
    }
}
