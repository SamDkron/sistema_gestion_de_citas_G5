/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package aplicación;

import controlador.Controlador;
import service.Service;
import vista.Vista;

import javax.swing.SwingUtilities;

/**
 * Método principal del sistema.
 * Inicia la aplicación creando las instancias del servicio, controlador y vista principal.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Service service = new Service();
            Controlador controlador = new Controlador(service);
            Vista vista = new Vista(controlador);
            vista.setVisible(true);
        });
    }
}
