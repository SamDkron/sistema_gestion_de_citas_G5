/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package modelo;

/**
 *  clase que representa a un paciente
 *  <p>
 *      hereda de la clase Usuario y le agrega caracteristicas propias
 *      del paciente como lo son la historia clinica, la fecha de nacimiento
 *      el tipo de sangre y el sexo
 *  </p>
 */
public class Paciente extends Usuario {
    private String historiaClinica;
    private String fechaNacimiento;
    private String tipoSangre;
    private String sexo;

    /**
     * Constructor de la clase Paciente y hereda atributos de la clase Usuario
     *
     * @param id              ID del paciente
     * @param nombre          Nombre del paciente
     * @param apellido        Apellido paciente
     * @param telefono        Telefono del paciente
     * @param email           Correo electronico del paciente
     * @param password        Contraseña del paciente
     * @param historiaClinica Codigo de historia clinica del paciente
     * @param fechaNacimiento Fecha de nacimiento del paciente
     * @param tipoSangre      Grupo Sanguineo del paciente
     * @param sexo            Sexo del paciente
     */
    public Paciente(String id, String nombre, String apellido, String telefono, String email, String password, String historiaClinica, String fechaNacimiento, String tipoSangre, String sexo) {
        super(id, nombre, apellido, telefono, email, password);
        this.historiaClinica = historiaClinica;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoSangre = tipoSangre;
        this.sexo = sexo;
    }

    /**
     * Obtiene el codigo de historia clinica del paciente
     *
     * @return String que contiene la historia clinica del paciente
     */
    public String getHistoriaClinica() {
        return historiaClinica;
    }

    /**
     * Obtiene la fecha de nacimiento del paciente
     *
     * @return String con la fecha de nacimiento del paciente
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Obtiene el grupo sanguineo del paciente
     *
     * @return String con el tipo o grupo sanguineo del paciente
     */
    public String getTipoSangre() {
        return tipoSangre;
    }

    /**
     * Obtiene el sexo del paciente
     *
     * @return String con el sexo del paciente
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Formato para imprimir la informacion del paciente
     *
     * @return String con toda la informacion del paciente
     */

    @Override
    public String getTipo() {
        return "Paciente";
    }

    @Override
    public String toCSV() {
        return id + ";" + nombre + ";" + apellido + ";" + telefono + ";" + email + ";" + password + ";" + fechaNacimiento + ";" + historiaClinica + ";" + tipoSangre+ ";" + sexo;
    }


    public static Paciente fromCSV(String csv) {
        if (csv == null || csv.trim().isEmpty()) {
            return null;
        }
        String[] line = csv.split(";");
        return new Paciente(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7], line[8], line[9]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PACIENTE ===\n");
        sb.append(super.toString()).append("\n");
        sb.append("Historia Clínica: ").append(historiaClinica).append("\n");
        sb.append("Fecha de Nacimiento: ").append(fechaNacimiento).append("\n");
        sb.append("Grupo Sanguíneo: ").append(tipoSangre).append("\n");
        sb.append("Sexo: ").append(sexo).append("\n");
        return sb.toString();
    }
}
