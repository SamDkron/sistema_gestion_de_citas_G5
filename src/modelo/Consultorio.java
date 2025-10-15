/**
 * @author Samuel David Dau Fernández, Santiago Duica Plata, Gustavo Daniel Olivos Rodríguez
 */

package modelo;

public class Consultorio {
    private String numero;
    private boolean disponibilidad;
    private String ubicacion;

    public Consultorio(String numero, boolean disponibilidad, String ubicacion) {
        this.numero = numero;
        this.disponibilidad = disponibilidad;
        this.ubicacion = ubicacion;
    }

    public String getNumero() {
        return numero;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    @Override
    public String toString() {
        return String.format("Consultorio %s /// Ubicación: %s /// Estado: %s", numero, ubicacion, disponibilidad ? "Disponible" : "Ocupado");
    }
}
