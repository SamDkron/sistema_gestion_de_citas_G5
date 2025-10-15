package aplicación;

import controlador.Controlador;
import service.Service;
import vista.Vista;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Service service = new Service();
            Controlador controlador = new Controlador(service);
            Vista vista = new Vista(controlador); // <-- Aquí debes pasar el controlador
            vista.setVisible(true);
        });
    }
}