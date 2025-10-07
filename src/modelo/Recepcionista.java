package modelo;

public class Recepcionista extends Usuario {
    private String state;

    public Recepcionista(String id, String nombre, String apellido, String telefono, String email, String password, String state){
        super(id, nombre, apellido, telefono, email, password);
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("Recepcionista: %s /// Turno: %s", super.toString(), state);
    }
}
