/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un medico.
 * <p>
 *     Hereda de la clase Usuario y le incluye informacion adicional
 *     y unica del medico.
 * </p>
 */
public class Medico extends Usuario {
    private String consultorioAsignado;
    private final String especialidad;
    private List<Cita> agenda;

    /**
     * Constructor de la clase que crea un medico con toda la informacion proporcionada.
     * Hereda atributos de la clase Usuario
     * @param id ID del medico
     * @param nombre Nombre del medico
     * @param apellido Apellido del medico
     * @param telefono Numero de telefono del medico
     * @param email Correo electronico del medico
     * @param password Contraseña del medico
     * @param especialidad Especialidad del medico
     */
    public Medico(String id, String nombre, String apellido, String telefono, String email, String password, String especialidad) {
        super(id, nombre, apellido, telefono, email, password);
        this.especialidad = especialidad;
        this.consultorioAsignado = "";
        this.agenda = new ArrayList<>();
    }

    /**
     * Obtiene el consultorio que va a ocupar el medico
     * @return String con el numero del consultorio asignado
     */
    public String getConsultorioAsignado() {
        return consultorioAsignado;
    }

    /**
     * Obtiene la especialidad del medico
     * @return devuelve un String con la especialidad del medico
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Crea una Llista en la cual guarda todas las citas que tiene agendadas el medico
     * @return Una Lista con todas las citas que tiene el medico agendadas
     */
    public List<Cita> getAgenda() {
        return agenda;
    }

    /**
     * Asigna el consultorio en el que el medico se encontrara ubicado
     * @param consultorio Numero del consultorio asignado al medico
     */
    public void setConsultorioAsignado(String consultorio) {
        this.consultorioAsignado = consultorio;
    }

    /**
     * Agrega una nueva cita a la lista que contiene la agenda del medico
     * @param cita Cita medica que se va a añadir a la agenda
     */
    public void agregarCita(Cita cita) {
        this.agenda.add(cita);
    }

    /**
     * Elimina una cita existente de la agenda del medico
     * @param cita Cita medica que se va a eliminar de la agenda del medico
     */
    public void removerCita(Cita cita) {
        this.agenda.remove(cita);
    }

    public static Medico fromCSV(String csv) {
        String[] line = csv.split(";");
        return new Medico(line[0], line[1],  line[2], line[3], line[4], line[5], line[6]);
    }

    @Override
    public String toCSV(){
        return id + ";" + nombre + ";" + apellido + ";" + telefono + ";" + email + ";" + password + ";" + especialidad + ";" + consultorioAsignado;
    }

    @Override
    public String getTipo() {
        return "Medico";
    }

    /**
     * Formato para imprimir la informacion basica del medico
     * @return String que contiene la informacion basica del medico
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MÉDICO ===\n");
        sb.append(super.toString()).append("\n");
        sb.append("Especialidad: ").append(especialidad).append("\n");
        sb.append("Consultorio Asignado: ").append(consultorioAsignado != null && !consultorioAsignado.isEmpty() ? consultorioAsignado : "Sin asignar").append("\n");
        return sb.toString();
    }
}
