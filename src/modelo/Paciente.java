package modelo;

public class Paciente extends Usuario {
    private String historiaClinica;
    private String fechaNacimiento;
    private String tipoSangre;
    private String sexo;

    public Paciente(String id, String nombre, String apellido, String telefono, String email, String password,  String historiaClinica, String fechaNacimiento, String tipoSangre, String sexo) {
        super(id, nombre, apellido, telefono, email, password);
        this.historiaClinica = historiaClinica;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoSangre = tipoSangre;
        this.sexo = sexo;
    }

    public String getHistoriaClinica() {
        return historiaClinica;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public String getSexo() {
        return sexo;
    }

    @Override
    public String toString() {
        return String.format("Paciente - %s /// HC: %s /// Nacimiento: %s /// Grupo Sanguíneo: %s /// Sexo: %s", super.toString(), historiaClinica, fechaNacimiento, tipoSangre, sexo);
    }
}
