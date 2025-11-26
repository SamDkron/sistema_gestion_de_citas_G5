/**
 * @author Samuel David Dau Fernandez
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodriguez
 */

package modelo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GestionarUsuario {
    private List<Paciente> pacientes;
    private List<Medico> medicos;
    private List<Recepcionista> recepcionistas;
    private List<Cita> citas;
    private List<Consultorio> consultorios;
    private final String archivoPacientes = "./archivos/pacientes.txt";
    private final String archivoMedicos = "./archivos/medicos.txt";
    private final String archivoRecepcionistas = "./archivos/recepcionistas.txt";
    private final String archivoCitas = "./archivos/citas.txt";
    private final String archivoConsultorios = "./archivos/consultorios.txt";

    public GestionarUsuario() {
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
        recepcionistas = new ArrayList<>();
        citas = new ArrayList<>();
        consultorios = new ArrayList<>();
    }

    public void cargarDesdeArchivo() {
        cargarArchivoPacientes();
        cargarArchivoMedicos();
        cargarArchivoRecepcionistas();
        cargarArchivoConsultorios();
        cargarArchivoCitas();
    }

    private void cargarArchivoPacientes() {
        cargarArchivo(archivoPacientes, pacientes, Paciente::fromCSV);
    }

    private void cargarArchivoMedicos() {
        cargarArchivo(archivoMedicos, medicos, Medico::fromCSV);
    }

    private void cargarArchivoRecepcionistas() {
        cargarArchivo(archivoRecepcionistas, recepcionistas, Recepcionista::fromCSV);
    }

    private void cargarArchivoCitas() {
        File file = new File(archivoCitas);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                Cita c = Cita.fromCSV(linea, this);
                if (c != null) citas.add(c);
            }
        } catch (IOException ex) {
            System.err.println("Error leyendo " + archivoCitas + ": " + ex.getMessage());
        }
    }

    private void cargarArchivoConsultorios() {
        cargarArchivo(archivoConsultorios, consultorios, Consultorio::fromCSV);
    }

    private <T> void cargarArchivo(String archivo, List<T> lista, Function<String, T> creador) {
        File file = new File(archivo);
        if (!file.exists()) return;

        try(BufferedReader buffer = new BufferedReader(new FileReader(file))){
            String linea;
            while ((linea = buffer.readLine()) != null){
                if (linea.trim().isEmpty()) {
                    continue;
                }

                T objeto = creador.apply(linea);

                if (objeto != null) {
                    lista.add(objeto);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al cargar archivo " + archivo);
        }
    }

    public void guardarEnArchivo() {
        guardarArchivo(archivoPacientes, pacientes);
        guardarArchivo(archivoMedicos, medicos);
        guardarArchivo(archivoRecepcionistas, recepcionistas);
        guardarArchivoConsultorio();
        guardarArchivoCitas();
    }

    private void guardarArchivo(String rutaArchivo, List<? extends Usuario> lista) {
        try {
            File nuevoArchivo = new File("./archivos");
            if (!nuevoArchivo.exists()) nuevoArchivo.mkdirs();

            PrintWriter escritor = new PrintWriter(new FileWriter(rutaArchivo));
            for (Usuario usuario : lista) {
                escritor.println(usuario.toCSV());
            }
            escritor.close();
        } catch (IOException ex) {
            System.out.println("Error al escribir el archivo" + rutaArchivo);
        }
    }

    private void guardarArchivoCitas() {
        try {
            File dir = new File("./archivos");
            if (!dir.exists()) dir.mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivoCitas))) {
                for (Cita c : citas) {
                    pw.println(c.toCSV());
                }
            }
        } catch (IOException ex) {
            System.err.println("Error escribiendo " + archivoCitas + ": " + ex.getMessage());
        }
    }
    public void agregarCita(Cita c) { if (c != null) citas.add(c); }

    private void guardarArchivoConsultorio() {
        try {
            File nuevoArchivo = new File("./archivos");
            if (!nuevoArchivo.exists()) nuevoArchivo.mkdirs();

            PrintWriter escritor = new PrintWriter(new FileWriter(archivoConsultorios));
            for (Consultorio consultorio : consultorios) {
                escritor.println(consultorio.toCSV());
            }
            escritor.close();
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo" + archivoConsultorios);
        }
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Recepcionista> getRecepcionistas() {
        return recepcionistas;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public List<Consultorio> getConsultorios() {
        return consultorios;
    }
}