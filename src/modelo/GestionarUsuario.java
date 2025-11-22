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
    private final String archivoPacientes = "./archivos/pacientes.txt";
    private final String archivoMedicos = "./archivos/medicos.txt";
    private final String archivoRecepcionistas = "./archivos/recepcionistas.txt";

    public GestionarUsuario() {
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
        recepcionistas = new ArrayList<>();
        cargarDesdeArchivo();
    }

    public void cargarDesdeArchivo() {
        cargarArchivoPacientes();
        cargarArchivoMedicos();
        cargarArchivoRecepcionistas();
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

    private <T> void cargarArchivo(String archivo, List<T> lista, Function<String, T> creador) {
        File file = new File(archivo);
        if (!file.exists()) return;

        try(BufferedReader buffer = new BufferedReader(new FileReader(file))){
            String linea;
            while ((linea = buffer.readLine()) != null){
                lista.add(creador.apply(linea));
            }
        } catch (IOException ex) {
            System.out.println("Error al cargar archivo " + archivo);
        }
    }

    public void guardarEnArchivo() {
        guardarArchivo(archivoPacientes, pacientes);
        guardarArchivo(archivoMedicos, medicos);
        guardarArchivo(archivoRecepcionistas, recepcionistas);
    }

    private void guardarArchivo(String rutaArchivo, List<? extends Usuario> lista) {
        try {
            File nuevoArchivo = new File("./archivos");
            if (!nuevoArchivo.exists()) nuevoArchivo.mkdir();

            PrintWriter escritor = new PrintWriter(new FileWriter(rutaArchivo));
            for (Usuario usuario : lista) {
                escritor.println(usuario);
            }
            escritor.close();
        } catch (IOException ex) {
            System.out.println("Error al escribir el archivo");
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
}

//    public void agregarUsuario(Usuario usuario) {
//        usuarios.add(usuario);
//    }
//    public Usuario iniciarSesion(String id, String password){
//        for (Usuario usuario : usuarios) {
//            if(usuario.getId().equals(id) && usuario.getPassword().equals(password)){
//                System.out.println("Bienvenido " + usuario.nombreCompleto() + " !!!");
//                return usuario;
//            }
//        }
//        System.out.println("Credenciales Invalidas");
//        return null;
//    }