/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;

/**
 * Clase enum que representa los diferentes tipos de estado que puede llegar a tomar
 * una cita medica dentro del sistema medico y asegura que la cita medica solo
 * tome estos valores establecidos.
 *
 * <p>
 *     Cada estado de la cita medica define el progreso o la situacion actual de la cita,
 *     desde que se crea, hasta que se finaliza o se cancela.
 * </p>
 */
public enum citaState {
    /**
     * Estado inicial de la cita medica.
     */
    PENDIENTE("Pendiente"),

    /**
     * La cita ha sido confirmada por el medico o por el medico.
     */
    CONFIRMADA("Confirmada"),

    /**
     * Indica que la cita ha sido cancelada por algun usuario, sea medico o paciente.
     */
    CANCELADA("Cancelada"),

    /**
     * Indica que la cita ha sido completada, este es el estado final de la cita.
     */
    COMPLETADA("Completada"),

    /**
     * La cita se encuentra actualmente en atencion.
     */
    EN_ATENCION("En Atención");

    /**
     * El texto que se encuentra en las comillas de cada estado es la descripcion.
     * Es una descripcion legible del estado actual de la cita
     */
    private String descripcion;

    /**
     * Constructor de la clase enum citaState que le agrega descripcion a cada estado
     * @param descripcion Descripcion del estado de la cita
     */
    citaState(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripcion del estado de la cita
     * @return String que describe el estado de la cita
     */
    public String getDescripcion() {
        return descripcion;
    }
}
