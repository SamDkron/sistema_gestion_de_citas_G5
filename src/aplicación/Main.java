/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package aplicación;

import controlador.Controlador;
import javax.swing.SwingUtilities;
import modelo.GestionarUsuario;
import service.InicializarDatos;
import service.Service;
import vista.Vista;

/**
 * Método principal del sistema.
 * Inicia la aplicación creando las instancias del servicio, controlador y vista principal.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionarUsuario gestionarUsuario = new GestionarUsuario();
            InicializarDatos inicializador = new InicializarDatos(gestionarUsuario);
            inicializador.inicializarDatosEjemplo();
            Service service = new Service(gestionarUsuario);
            Controlador controlador = new Controlador(service);
            Vista vista = new Vista(controlador);
            vista.setVisible(true);
        });
    }
}
