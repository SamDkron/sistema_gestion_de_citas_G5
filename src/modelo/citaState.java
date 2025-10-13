/**
 * @author Samuel David Dau Fernández, Santiago Duica Plata, Gustavo Daniel Olivos Rodríguez
 */

package modelo;

public enum citaState {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada"),
    COMPLETADA("Completada"),
    EN_ATENCION("En Atención");

    private String descripcion;

    citaState(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
