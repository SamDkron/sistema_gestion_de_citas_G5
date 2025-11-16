package vista;

import controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Vista principal para el paciente
 * Muestra las funcionalidades disponibles para el paciente
 */
public class PacienteVista extends JFrame {
    private String idPaciente;
    private String nombreCompleto;
    private String historiaClinica;
    private String tipoSangre;
    private Controlador controlador;

    /**
     * Constructor de la vista principal del paciente.
     * Inicializa los componentes gráficos y muestra las opciones disponibles.
     * @param idPaciente ID del paciente que inició sesión
     * @param nombreCompleto nombre completo del paciente
     * @param historiaClinica número de historia clínica
     * @param tipoSangre tipo de sangre del paciente
     * @param controlador instancia del controlador principal del sistema
     */
    public PacienteVista(String idPaciente, String nombreCompleto, String historiaClinica,
                         String tipoSangre, Controlador controlador) {
        super("Panel del Paciente - " + nombreCompleto);
        this.idPaciente = idPaciente;
        this.nombreCompleto = nombreCompleto;
        this.historiaClinica = historiaClinica;
        this.tipoSangre = tipoSangre;
        this.controlador = controlador;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(152, 251, 152));

        JPanel panelInfo = crearPanelInformacion();
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);

        JPanel panelOpciones = crearPanelOpciones();
        panelPrincipal.add(panelOpciones, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    /**
     * Crea el panel superior con la información del paciente.
     * Muestra el nombre, historia clínica y tipo de sangre.
     * @return el panel con la información del paciente
     */
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 179, 113));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblBienvenida = new JLabel("Bienvenido " + nombreCompleto);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblHistoria = new JLabel("Historia Clínica: " + historiaClinica);
        lblHistoria.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblHistoria.setForeground(Color.WHITE);

        JLabel lblSangre = new JLabel("Tipo de Sangre: " + tipoSangre);
        lblSangre.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSangre.setForeground(Color.WHITE);

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setOpaque(false);
        panelTextos.add(lblBienvenida);
        panelTextos.add(Box.createVerticalStrut(10));
        panelTextos.add(lblHistoria);
        panelTextos.add(lblSangre);

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
     * Crea el panel central con los botones de opciones disponibles para el paciente.
     * @return el panel con las opciones del paciente
     */
    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(152, 251, 152));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(crearBotonOpcion("Reservar Cita", "📅", e -> reservarCita()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Cancelar Cita", "X", e -> cancelarCita()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Reprogramar Cita", "🔄", e -> reprogramarCita()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(crearBotonOpcion("Ver Mis Citas", "📋", e -> verMisCitas()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Historia Clínica", "📄", e -> verHistoriaClinica()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Médicos Disponibles", "👨‍⚕️", e -> verMedicos()), gbc);

        return panel;
    }

    /**
     * Crea un botón personalizado con texto, ícono y acción asociada.
     * @param texto el texto que se mostrará en el botón
     * @param icono el ícono (en forma de texto) que acompaña al botón
     * @param action la acción que se ejecutará al presionar el botón
     * @return el botón configurado
     */
    private JButton crearBotonOpcion(String texto, String icono, java.awt.event.ActionListener action) {
        JButton boton = new JButton("<html><center>" + icono + "<br>" + texto + "</center></html>");
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(60, 179, 113));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 179, 113), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(250, 150));
        boton.addActionListener(action);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(240, 255, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);
            }
        });

        return boton;
    }

    /**
     * Permite al paciente reservar una nueva cita médica.
     * Captura los datos y delega al controlador el procesamiento.
     */
    private void reservarCita() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField txtIdMedico = new JTextField();
        JTextField txtConsultorio = new JTextField();
        JTextField txtMotivo = new JTextField();
        JTextField txtFecha = new JTextField("dd/MM/yyyy HH:mm");

        panel.add(new JLabel("ID del Médico:"));
        panel.add(txtIdMedico);
        panel.add(new JLabel("Número de Consultorio:"));
        panel.add(txtConsultorio);
        panel.add(new JLabel("Motivo:"));
        panel.add(txtMotivo);
        panel.add(new JLabel("Fecha (dd/MM/yyyy HH:mm):"));
        panel.add(txtFecha);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Reservar Nueva Cita", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText(), formatter);

                String resultado = controlador.procesarReservaCita(
                        idPaciente,
                        txtIdMedico.getText().trim(),
                        txtConsultorio.getText().trim(),
                        txtMotivo.getText().trim(),
                        fecha
                );

                if (resultado == null) {
                    JOptionPane.showMessageDialog(this,
                            "Cita reservada exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, resultado,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en el formato de fecha. Use: dd/MM/yyyy HH:mm",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Permite al paciente cancelar una cita previamente agendada.
     * Captura el ID y delega al controlador el procesamiento.
     */
    private void cancelarCita() {
        String idCita = JOptionPane.showInputDialog(this, "Ingrese el ID de la cita a cancelar:");
        if (idCita != null && !idCita.trim().isEmpty()) {
            String resultado = controlador.procesarCancelacionCita(idCita, idPaciente);

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
     * Permite al paciente reprogramar una cita existente.
     * Captura los datos y delega al controlador el procesamiento.
     */
    private void reprogramarCita() {
        String idCita = JOptionPane.showInputDialog(this, "Ingrese el ID de la cita:");
        if (idCita != null && !idCita.trim().isEmpty()) {
            String nuevaFecha = JOptionPane.showInputDialog(this,
                    "Nueva fecha (dd/MM/yyyy HH:mm):");

            if (nuevaFecha != null && !nuevaFecha.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime fecha = LocalDateTime.parse(nuevaFecha, formatter);

                    String resultado = controlador.procesarReprogramacionCita(idCita, fecha);

                    if (resultado == null) {
                        JOptionPane.showMessageDialog(this,
                                "Cita reprogramada exitosamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, resultado,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error en el formato de fecha. Use: dd/MM/yyyy HH:mm",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Muestra todas las citas agendadas por el paciente.
     * Delega al controlador la obtención de las citas.
     */
    private void verMisCitas() {
        String citas = controlador.obtenerCitasPaciente(idPaciente);

        JTextArea textArea = new JTextArea(citas);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Mis Citas", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra la historia clínica completa del paciente.
     * Delega al controlador la obtención de la información.
     */
    private void verHistoriaClinica() {
        String historia = controlador.consultarHistoriaClinica(idPaciente);

        JTextArea textArea = new JTextArea(historia);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Mi Historia Clínica", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra la lista de médicos disponibles en el sistema.
     * Delega al controlador la obtención de la lista.
     */
    private void verMedicos() {
        String medicos = controlador.obtenerListaMedicos();

        JTextArea textArea = new JTextArea(medicos);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Médicos Disponibles", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Cierra la sesión actual del paciente.
     * Pide confirmación al usuario y, si acepta, regresa a la vista principal del sistema.
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