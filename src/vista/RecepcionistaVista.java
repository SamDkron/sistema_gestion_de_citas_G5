package vista;

import controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecepcionistaVista extends JFrame {
    private String nombreCompleto;
    private String turno;
    private Controlador controlador;

    public RecepcionistaVista(String nombreCompleto, String turno, Controlador controlador) {
        super("Panel del Recepcionista - " + nombreCompleto);
        this.nombreCompleto = nombreCompleto;
        this.turno = turno;
        this.controlador = controlador;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(255, 218, 185));

        JPanel panelInfo = crearPanelInformacion();
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);

        JPanel panelOpciones = crearPanelOpciones();
        panelPrincipal.add(panelOpciones, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(210, 105, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblBienvenida = new JLabel("Bienvenido " + nombreCompleto);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblTurno = new JLabel("Turno: " + turno);
        lblTurno.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTurno.setForeground(Color.WHITE);

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setOpaque(false);
        panelTextos.add(lblBienvenida);
        panelTextos.add(Box.createVerticalStrut(10));
        panelTextos.add(lblTurno);

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

    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 218, 185));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(crearBotonOpcion("Registrar Médico", "👨‍⚕️", e -> registrarMedico()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Registrar Paciente", "👤", e -> registrarPaciente()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Asignar Consultorio", "🏥", e -> asignarConsultorio()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(crearBotonOpcion("Consultar Paciente", "📋", e -> consultarPaciente()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Consultar Médico", "👨‍⚕️", e -> consultarMedico()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Ver Todas las Citas", "📅", e -> verTodasCitas()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(crearBotonOpcion("Listar Médicos", "📝", e -> listarMedicos()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Listar Pacientes", "📝", e -> listarPacientes()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Listar Consultorios", "🏢", e -> listarConsultorios()), gbc);

        return panel;
    }

    private JButton crearBotonOpcion(String texto, String icono, ActionListener action) {
        JButton boton = new JButton("<html><center>" + icono + "<br>" + texto + "</center></html>");
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(210, 105, 30));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(250, 150));
        boton.addActionListener(action);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(255, 245, 238));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);
            }
        });

        return boton;
    }

    private void registrarMedico() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JTextField txtEspecialidad = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Especialidad:"));
        panel.add(txtEspecialidad);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Registrar Nuevo Médico", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String resultado = controlador.procesarRegistroMedico(
                    txtId.getText().trim(),
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtEmail.getText().trim(),
                    new String(txtPassword.getPassword()),
                    txtEspecialidad.getText().trim()
            );

            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                        "Médico registrado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarPaciente() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JTextField txtFechaNacimiento = new JTextField();
        JComboBox<String> cmbTipoSangre = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JComboBox<String> cmbSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});

        panel.add(new JLabel("ID:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Fecha Nacimiento (dd/MM/yyyy):"));
        panel.add(txtFechaNacimiento);
        panel.add(new JLabel("Tipo de Sangre:"));
        panel.add(cmbTipoSangre);
        panel.add(new JLabel("Sexo:"));
        panel.add(cmbSexo);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Registrar Nuevo Paciente", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String historiaClinica = "HC-" + txtId.getText().trim();

            String resultado = controlador.procesarRegistroPaciente(
                    txtId.getText().trim(),
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtEmail.getText().trim(),
                    new String(txtPassword.getPassword()),
                    historiaClinica,
                    txtFechaNacimiento.getText().trim(),
                    (String) cmbTipoSangre.getSelectedItem(),
                    (String) cmbSexo.getSelectedItem()
            );

            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                        "Paciente registrado exitosamente\nHistoria Clínica: " + historiaClinica,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void asignarConsultorio() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField txtIdMedico = new JTextField();
        JTextField txtNumConsultorio = new JTextField();
        JTextField txtFecha = new JTextField("dd/MM/yyyy HH:mm");

        panel.add(new JLabel("ID del Médico:"));
        panel.add(txtIdMedico);
        panel.add(new JLabel("Número de Consultorio:"));
        panel.add(txtNumConsultorio);
        panel.add(new JLabel("Fecha (dd/MM/yyyy HH:mm):"));
        panel.add(txtFecha);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Asignar Consultorio a Médico", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText(), formatter);

                boolean exito = controlador.asignarConsultorioAMedico(
                        txtIdMedico.getText().trim(),
                        txtNumConsultorio.getText().trim(),
                        fecha
                );

                if (exito) {
                    JOptionPane.showMessageDialog(this,
                            "Consultorio asignado exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al asignar. Verifica los datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en el formato de fecha. Use: dd/MM/yyyy HH:mm",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void consultarPaciente() {
        String idPaciente = JOptionPane.showInputDialog(this, "Ingrese el ID del paciente:");
        if (idPaciente != null && !idPaciente.trim().isEmpty()) {
            String info = controlador.consultarPaciente(idPaciente);

            JTextArea textArea = new JTextArea(info);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Información del Paciente", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void consultarMedico() {
        String idMedico = JOptionPane.showInputDialog(this, "Ingrese el ID del médico:");
        if (idMedico != null && !idMedico.trim().isEmpty()) {
            String info = controlador.consultarMedico(idMedico);

            JTextArea textArea = new JTextArea(info);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Información del Médico", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void verTodasCitas() {
        StringBuilder citas = new StringBuilder("TODAS LAS CITAS DEL SISTEMA\n\n");

        String listaCitas = controlador.obtenerTodasLasCitas();

        if (listaCitas.isEmpty()) {
            citas.append("No hay citas registradas.");
        } else {
            citas.append(listaCitas);
        }

        JTextArea textArea = new JTextArea(citas.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 500));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Todas las Citas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void listarMedicos() {
        String medicos = controlador.obtenerListaMedicos();

        JTextArea textArea = new JTextArea(medicos);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Lista de Médicos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void listarPacientes() {
        String pacientes = controlador.obtenerListaPacientes();

        JTextArea textArea = new JTextArea(pacientes);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Lista de Pacientes", JOptionPane.INFORMATION_MESSAGE);
    }

    private void listarConsultorios() {
        StringBuilder consultorios = new StringBuilder("CONSULTORIOS DEL SISTEMA\n\n");

        String detalleConsultorios = controlador.obtenerDetalleConsultorios();

        if (detalleConsultorios.isEmpty()) {
            consultorios.append("No hay consultorios registrados.");
        } else {
            consultorios.append(detalleConsultorios);
        }

        JTextArea textArea = new JTextArea(consultorios.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 500));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Lista de Consultorios", JOptionPane.INFORMATION_MESSAGE);
    }

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