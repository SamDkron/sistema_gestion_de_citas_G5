/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;

/**
 * Clase que representa al consultorio dentro del sistema medico
 * <p>
 *     Cada consultorio recibe un numero que funciona como identificador(ID)
 *     una disponibilidad y una ubicacion que representa un espacio fisico
 * </p>
 */
public class Consultorio {
    private String numero;
    private boolean disponibilidad;
    private String ubicacion;

    /**
     * Constructor de la clase Consultorio.
     * Crea un consultorio con los datos que le entran
     * @param numero Numero del consultorio
     * @param disponibilidad Disponibilidad del consultorio
     * @param ubicacion Ubicacion fisica en la que se encuentra el consultorio
     */
    public Consultorio(String numero, boolean disponibilidad, String ubicacion) {
        this.numero = numero;
        this.disponibilidad = disponibilidad;
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene el numero del consultorio
     * @return String con el numero del consultorio
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Obtiene la disponibilidad del consultorio
     * @return True o false dependiendo de la disponibilidad del consultorio
     */
    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    /**
     * Actualiza la disponibilidad del consultorio
     * @param disponibilidad True o false dependiendo de la nueva disponibilidad del consultorio
     */
    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    /**
     * Obtiene la ubicacion fisica en donde se encuentra el consultorio
     * @return String con la ubicacion del consultorio
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Formato con una representacion textual del consultorio mostrando datos basicos del consultorio
     * @return String que contiene los datos basicos del consultorio
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(numero).append("\n");
        sb.append("Ubicación: ").append(ubicacion).append(" ");
        sb.append("~~~ Estado: ").append(disponibilidad ? "Disponible" : "Ocupado").append("\n");
        return sb.toString();
    }
}
