/**
 * @author Samuel David Dau Fernandez
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodriguez
 */

package modelo;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class GestionarUsuario {
    private List<Usuario> usuarios;
    private final String archivo = "./archivos/usuario.txt";

    public GestionarUsuario() {
        usuarios = new ArrayList<>();
        cargarDesdeArchivo();
    }

    public void cargarDesdeArchivo() {
        File file = new File(archivo);
        if (!file.exists()) return;

        try(BufferedReader buffer = new BufferedReader(new FileReader(file))) {
            String linea;
            while((linea = buffer.readLine()) != null) {
                usuarios.add(Usuario.fromCSV(linea));
            }
        } catch (IOException ex) {
            System.out.println("Error al cargar archivo: " + ex.getMessage());
        }
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void guardarEnArchivo() {
        try(PrintWriter pw = new PrintWriter(new FileWriter(archivo))){
            for (Usuario usuario : usuarios) {
                pw.println(usuario.toCSV());
            }
        } catch (IOException ex) {
            System.out.println("Error al cargar archivo: " + ex.getMessage());
        }
    }

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
}
