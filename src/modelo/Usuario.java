package modelo;

public abstract class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;

    public Usuario(String id, String nombre, String apellido, String telefono, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String nombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return String.format("ID: %s /// %s %s /// Tel: %s /// Email: %s", id, nombre, apellido, telefono, email);
    }
}





