/**
 * @author Samuel David Dau Fernández
 * @author Santiago Duica Plata
 * @author Gustavo Daniel Olivos Rodríguez
 */

package vista;

import controlador.Controlador;
import javax.swing.*;
import java.awt.*;

/**
 * Vista principal para el médico
 * Muestra las funcionalidades disponibles para el médico
 */
public class MedicoVista extends JFrame {
    private String idMedico;
    private String nombreCompleto;
    private String especialidad;
    private String consultorioAsignado;
    private Controlador controlador;

    /**
     * Constructor de la vista del médico.
     * Inicializa la ventana principal con la información del médico y las opciones disponibles.
     * @param idMedico ID del médico autenticado
     * @param nombreCompleto nombre completo del médico
     * @param especialidad especialidad del médico
     * @param consultorioAsignado consultorio asignado al médico
     * @param controlador controlador que gestiona la lógica del sistema
     */
    public MedicoVista(String idMedico, String nombreCompleto, String especialidad,
                       String consultorioAsignado, Controlador controlador) {
        super("Panel del Médico - " + nombreCompleto);
        this.idMedico = idMedico;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.consultorioAsignado = consultorioAsignado;
        this.controlador = controlador;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(135, 206, 250));

        JPanel panelInfo = crearPanelInformacion();
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);

        JPanel panelOpciones = crearPanelOpciones();
        panelPrincipal.add(panelOpciones, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    /**
     * Crea el panel superior que muestra la información del médico,
     * incluyendo su nombre, especialidad y consultorio asignado.
     * También contiene el botón para cerrar sesión.
     * @return panel con la información del médico
     */
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(100, 149, 237));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblBienvenida = new JLabel("Bienvenido Dr(a). " + nombreCompleto);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblEspecialidad = new JLabel("Especialidad: " + especialidad);
        lblEspecialidad.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblEspecialidad.setForeground(Color.WHITE);

        String consultorio = (consultorioAsignado == null || consultorioAsignado.isEmpty()) ?
                "Sin asignar" : consultorioAsignado;
        JLabel lblConsultorio = new JLabel("Consultorio: " + consultorio);
        lblConsultorio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblConsultorio.setForeground(Color.WHITE);

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setOpaque(false);
        panelTextos.add(lblBienvenida);
        panelTextos.add(Box.createVerticalStrut(10));
        panelTextos.add(lblEspecialidad);
        panelTextos.add(lblConsultorio);

        panel.add(panelTextos, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCerrarSesion.setBackground(new Color(220, 20, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panel.add(btnCerrarSesion, BorderLayout.EAST);

        return panel;
    }

    /**
     * Crea el panel principal de opciones que contiene los botones
     * para acceder a las distintas funcionalidades del médico.
     * @return panel con los botones de opciones
     */
    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(135, 206, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(crearBotonOpcion("Ver Mi Agenda", "📅", e -> verAgenda()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Atender Cita", "😷", e -> atenderCita()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Historia Clínica", "📋", e -> verHistoriaClinicaPaciente()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(crearBotonOpcion("Remitir Paciente", "📋", e -> remitirPaciente()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Cancelar Cita", "X", e -> cancelarCita()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Historial de Citas", "📊", e -> verHistorial()), gbc);

        return panel;
    }

    /**
     * Crea un botón personalizado para el menú de opciones del médico,
     * con texto, ícono y acción asociada.
     * @param texto texto del botón
     * @param icono símbolo o ícono representativo del botón
     * @param action acción que se ejecutará al presionar el botón
     * @return botón configurado con el diseño y acción especificados
     */
    private JButton crearBotonOpcion(String texto, String icono, java.awt.event.ActionListener action) {
        JButton boton = new JButton("<html><center>" + icono + "<br>" + texto + "</center></html>");
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(100, 149, 237));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(250, 150));
        boton.addActionListener(action);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(230, 240, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);
            }
        });

        return boton;
    }

    /**
     * Muestra la agenda actual del médico, incluyendo las citas programadas.
     * Delega al controlador la obtención de la agenda.
     */
    private void verAgenda() {
        String agenda = controlador.obtenerAgendaMedico(idMedico);

        JTextArea textArea = new JTextArea(agenda);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Mi Agenda", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Permite al médico registrar la atención de una cita.
     * Captura los datos y delega al controlador el procesamiento.
     */
    private void atenderCita() {
        String idCita = JOptionPane.showInputDialog(this, "Ingrese el ID de la cita:");
        if (idCita != null && !idCita.trim().isEmpty()) {
            String diagnostico = JOptionPane.showInputDialog(this, "Diagnóstico:");
            if (diagnostico != null && !diagnostico.trim().isEmpty()) {
                String tratamiento = JOptionPane.showInputDialog(this, "Tratamiento:");
                if (tratamiento != null && !tratamiento.trim().isEmpty()) {
                    String observaciones = JOptionPane.showInputDialog(this, "Observaciones:");

                    String resultado = controlador.procesarAtencionCita(
                            idCita,
                            diagnostico,
                            tratamiento,
                            observaciones != null ? observaciones : ""
                    );

                    if (resultado == null) {
                        JOptionPane.showMessageDialog(this,
                                "Cita atendida exitosamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, resultado,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Consulta y muestra la historia clínica de un paciente.
     * Delega al controlador la obtención de la información.
     */
    private void verHistoriaClinicaPaciente() {
        String idPaciente = JOptionPane.showInputDialog(this, "Ingrese el ID del paciente:");
        if (idPaciente != null && !idPaciente.trim().isEmpty()) {
            String historia = controlador.consultarHistoriaClinica(idPaciente);

            JTextArea textArea = new JTextArea(historia);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Historia Clínica del Paciente", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Permite al médico remitir a un paciente a otra especialidad.
     * Captura los datos y delega al controlador el procesamiento.
     */
    private void remitirPaciente() {
        String idCita = JOptionPane.showInputDialog(this, "Ingrese el ID de la cita:");
        if (idCita != null && !idCita.trim().isEmpty()) {
            String especialidad = JOptionPane.showInputDialog(this, "Especialidad a remitir:");
            if (especialidad != null && !especialidad.trim().isEmpty()) {
                String motivo = JOptionPane.showInputDialog(this, "Motivo de la remisión:");
                if (motivo != null && !motivo.trim().isEmpty()) {
                    String resultado = controlador.remitirPaciente(idCita, especialidad, motivo);
                    JOptionPane.showMessageDialog(this, resultado, "Remisión",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    /**
     * Cancela una cita médica.
     * Captura el ID y delega al controlador la cancelación.
     */
    private void cancelarCita() {
        String idCita = JOptionPane.showInputDialog(this, "Ingrese el ID de la cita a cancelar:");
        if (idCita != null && !idCita.trim().isEmpty()) {
            String resultado = controlador.procesarCancelacionCita(idCita, idMedico);

            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                        "Cita cancelada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, resultado,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Muestra el historial de citas atendidas por el médico.
     * Delega al controlador la obtención del historial.
     */
    private void verHistorial() {
        String historial = controlador.obtenerHistorialCitasMedico(idMedico);

        JTextArea textArea = new JTextArea(historial);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Historial", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Cierra la sesión actual del médico previa confirmación.
     * Al confirmar, cierra la ventana y regresa a la vista principal de login.
     */
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            this.dispose();
            new Vista(controlador).setVisible(true);
        }
    }
}