package modelo;

/**
 * Clase que representa al consultorio dentro del sistema medico
 *
 * Cada consultorio recibe un numero que funciona como identificador(ID),
 * una disponibilidad y una ubicacion que representa un espacio fisico.
 */
public class Consultorio {
    private String numero;
    private boolean disponibilidad;
    private String ubicacion;

    /**
     * Constructor de la clase Consultorio.
     * @param numero Numero del consultorio
     * @param disponibilidad Disponibilidad del consultorio (true = disponible)
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
     * Obtiene la ubicacion fisica en donde se encuentra el consultorio
     * @return String con la ubicacion del consultorio
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Convierte todos los datos del consultorio en formato CSV para guardarlo en el archivo.
     * @return String en formato csv
     */
    public String toCSV(){
        return numero + ";" + ubicacion + ";" + disponibilidad;
    }

    /**
     * Crea un consultorio con la informacion que recibe de un csv
     * Espera formato: numero;ubicacion;disponibilidad
     * @param csv linea en formato csv
     * @return Consultorio con los datos que se especificaron en la linea, o null si la línea es inválida
     */
    public static Consultorio fromCSV(String csv){
        if(csv == null || csv.trim().isEmpty()) return null;

        try {
            String[] linea = csv.split(";", -1);
            if (linea.length < 3) return null;

            String numero = linea[0].trim();
            String ubicacion = linea[1].trim();
            boolean disponibilidad = Boolean.parseBoolean(linea[2].trim());

            return new Consultorio(numero, disponibilidad, ubicacion);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Representación textual del consultorio mostrando datos básicos.
     * @return String que contiene los datos básicos del consultorio
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