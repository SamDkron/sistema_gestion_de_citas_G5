/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;

/**
 * Clase base la cual representa las caracteristicas basicas de todos los usuarios del sistema
 */

public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;

    /**
     * Constructor de la clase Usuario
     * @param id ID unico del usuario
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param telefono Numero de telefono del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     */
    public Usuario(String id, String nombre, String apellido, String telefono, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
    }

    /**
     * obtiene el ID unico del usuario
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * obtiene el nombre del usuario
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el apellido del usuario
     * @return apellido del usuario
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Obtiene el numero de telefono del usuario
     * @return numero de telefono del usuario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Obtiene el correo email del usuario
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene la contraseña del usuario
     * @return contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Junta los nombres y los apellidos del usuario para obtener el nombre completo
     * @return el nombre completo del usuario
     */
    public String nombreCompleto() {
        return nombre + " " + apellido;
    }

    public String toCSV(){
        return id + ";" + nombre + ";" + apellido + ";" + telefono + ";" + email + ";" + password;
    }

    public static Usuario fromCSV(String csv){
        String[] line = csv.split(";");
        return new Usuario(line[0], line[1], line[2], line[3], line[4], line[5]);
    }

    /**
     * Formato para imprimir los datos del usuario
     * @return cadena de texto con la informacion del usuario
     */
    @Override
    public String toString() {
        return String.format("ID: %s /// %s %s /// Tel: %s /// Email: %s", id, nombre, apellido, telefono, email);
    }
}





