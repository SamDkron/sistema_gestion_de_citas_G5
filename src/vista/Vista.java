package vista;

import controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista principal del sistema de gestión de citas
 * Maneja el login y registro de usuarios
 */
public class Vista extends JFrame {
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private Controlador controlador;

    public Vista(Controlador controlador) {
        super("SISTEMA DE GESTIÓN DE CITAS");
        this.controlador = controlador;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        panelPrincipal.add(crearPanelBienvenida(), "BIENVENIDA");
        panelPrincipal.add(crearPanelSeleccionTipo("REGISTRO"), "SELECCION_REGISTRO");
        panelPrincipal.add(crearPanelSeleccionTipo("LOGIN"), "SELECCION_LOGIN");

        panelPrincipal.add(crearPanelRegistroMedico(), "FORM_REGISTRO_MEDICO");
        panelPrincipal.add(crearPanelRegistroPaciente(), "FORM_REGISTRO_PACIENTE");
        panelPrincipal.add(crearPanelRegistroRecepcionista(), "FORM_REGISTRO_RECEPCIONISTA");

        panelPrincipal.add(crearPanelLogin("MEDICO"), "FORM_LOGIN_MEDICO");
        panelPrincipal.add(crearPanelLogin("PACIENTE"), "FORM_LOGIN_PACIENTE");
        panelPrincipal.add(crearPanelLogin("RECEPCIONISTA"), "FORM_LOGIN_RECEPCIONISTA");

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelBienvenida() {
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/recursos/fondo.jpg"));
        JPanel panel = new FondoPanel(fondoIcon.getImage());
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 50, 0);

        JLabel bienvenida = new JLabel("¡Te damos la bienvenida a Baus EPS!");
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 60));
        bienvenida.setForeground(Color.WHITE);
        panel.add(bienvenida, gbc);

        gbc.gridy++;
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        botones.setOpaque(false);

        JButton btnRegistro = crearBoton("Registrarse", new Color(255, 182, 193));
        JButton btnLogin = crearBoton("Iniciar Sesión", new Color(255, 182, 193));

        btnRegistro.addActionListener(e -> cardLayout.show(panelPrincipal, "SELECCION_REGISTRO"));
        btnLogin.addActionListener(e -> cardLayout.show(panelPrincipal, "SELECCION_LOGIN"));

        botones.add(btnRegistro);
        botones.add(btnLogin);

        panel.add(botones, gbc);

        return panel;
    }

    private JPanel crearPanelSeleccionTipo(String accion) {
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/recursos/fondo.jpg"));
        JPanel panel = new FondoPanel(fondoIcon.getImage());
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 30, 0);

        JLabel titulo = new JLabel(accion.equals("REGISTRO") ? "Selecciona tu tipo de usuario" : "¿Quién eres?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 40, 0);
        JPanel botones = new JPanel(new GridLayout(3, 1, 0, 20));
        botones.setOpaque(false);

        JButton btnMedico = crearBoton("Médico", new Color(135, 206, 250));
        JButton btnPaciente = crearBoton("Paciente", new Color(152, 251, 152));
        JButton btnRecepcionista = crearBoton("Recepcionista", new Color(255, 218, 185));

        String prefijoPanel = accion.equals("REGISTRO") ? "FORM_REGISTRO_" : "FORM_LOGIN_";
        btnMedico.addActionListener(e -> cardLayout.show(panelPrincipal, prefijoPanel + "MEDICO"));
        btnPaciente.addActionListener(e -> cardLayout.show(panelPrincipal, prefijoPanel + "PACIENTE"));
        btnRecepcionista.addActionListener(e -> cardLayout.show(panelPrincipal, prefijoPanel + "RECEPCIONISTA"));

        botones.add(btnMedico);
        botones.add(btnPaciente);
        botones.add(btnRecepcionista);

        panel.add(botones, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton btnVolver = crearBoton("← Volver", new Color(200, 200, 200));
        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "BIENVENIDA"));
        panel.add(btnVolver, gbc);

        return panel;
    }

    private JPanel crearPanelRegistroMedico() {
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/recursos/fondo.jpg"));
        JPanel panel = new FondoPanel(fondoIcon.getImage());
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel titulo = new JLabel("Registro de Médico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 35));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, gbc);

        gbc.gridy++;
        JPanel formulario = new JPanel(new GridLayout(7, 2, 15, 10));
        formulario.setOpaque(false);

        JTextField txtId = crearCampoTexto();
        JTextField txtNombre = crearCampoTexto();
        JTextField txtApellido = crearCampoTexto();
        JTextField txtTelefono = crearCampoTexto();
        JTextField txtEmail = crearCampoTexto();
        JPasswordField txtPassword = crearCampoPassword();
        JTextField txtEspecialidad = crearCampoTexto();

        agregarCampo(formulario, "ID:", txtId);
        agregarCampo(formulario, "Nombre:", txtNombre);
        agregarCampo(formulario, "Apellido:", txtApellido);
        agregarCampo(formulario, "Teléfono:", txtTelefono);
        agregarCampo(formulario, "Email:", txtEmail);
        agregarCampo(formulario, "Contraseña:", txtPassword);
        agregarCampo(formulario, "Especialidad:", txtEspecialidad);

        panel.add(formulario, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);

        JButton btnRegistrar = crearBoton("Registrarse", new Color(135, 206, 250));
        JButton btnVolver = crearBoton("← Volver", new Color(200, 200, 200));

        btnRegistrar.addActionListener(e -> {
            if (validarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword, txtEspecialidad)) {
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
                    // Éxito - La Vista construye su propio mensaje
                    JOptionPane.showMessageDialog(panel,
                            "¡Registro exitoso!\nBienvenido Dr(a).",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword, txtEspecialidad);
                    cardLayout.show(panelPrincipal, "BIENVENIDA");
                } else {
                    // Error - Mostrar mensaje del controlador
                    JOptionPane.showMessageDialog(panel, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVolver.addActionListener(e -> {
            limpiarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword, txtEspecialidad);
            cardLayout.show(panelPrincipal, "SELECCION_REGISTRO");
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelRegistroPaciente() {
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/recursos/fondo.jpg"));
        JPanel panel = new FondoPanel(fondoIcon.getImage());
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel titulo = new JLabel("Registro de Paciente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 35));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, gbc);

        gbc.gridy++;
        JPanel formulario = new JPanel(new GridLayout(9, 2, 15, 10));
        formulario.setOpaque(false);

        JTextField txtId = crearCampoTexto();
        JTextField txtNombre = crearCampoTexto();
        JTextField txtApellido = crearCampoTexto();
        JTextField txtTelefono = crearCampoTexto();
        JTextField txtEmail = crearCampoTexto();
        JPasswordField txtPassword = crearCampoPassword();
        JTextField txtFechaNacimiento = crearCampoTexto();
        JComboBox<String> cmbTipoSangre = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JComboBox<String> cmbSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});

        agregarCampo(formulario, "ID:", txtId);
        agregarCampo(formulario, "Nombre:", txtNombre);
        agregarCampo(formulario, "Apellido:", txtApellido);
        agregarCampo(formulario, "Teléfono:", txtTelefono);
        agregarCampo(formulario, "Email:", txtEmail);
        agregarCampo(formulario, "Contraseña:", txtPassword);
        agregarCampo(formulario, "Fecha Nac. (dd/mm/aaaa):", txtFechaNacimiento);
        agregarCampo(formulario, "Tipo de Sangre:", cmbTipoSangre);
        agregarCampo(formulario, "Sexo:", cmbSexo);

        panel.add(formulario, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);

        JButton btnRegistrar = crearBoton("Registrarse", new Color(152, 251, 152));
        JButton btnVolver = crearBoton("← Volver", new Color(200, 200, 200));

        btnRegistrar.addActionListener(e -> {
            if (validarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword, txtFechaNacimiento)) {
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
                    // Éxito - La Vista construye su propio mensaje
                    JOptionPane.showMessageDialog(panel,
                            "¡Registro exitoso!\nBienvenido\nHistoria Clínica: " + historiaClinica,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword, txtFechaNacimiento);
                    cardLayout.show(panelPrincipal, "BIENVENIDA");
                } else {
                    JOptionPane.showMessageDialog(panel, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVolver.addActionListener(e -> {
            limpiarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword, txtFechaNacimiento);
            cardLayout.show(panelPrincipal, "SELECCION_REGISTRO");
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelRegistroRecepcionista() {
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/recursos/fondo.jpg"));
        JPanel panel = new FondoPanel(fondoIcon.getImage());
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel titulo = new JLabel("Registro de Recepcionista");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 35));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, gbc);

        gbc.gridy++;
        JPanel formulario = new JPanel(new GridLayout(7, 2, 15, 10));
        formulario.setOpaque(false);

        JTextField txtId = crearCampoTexto();
        JTextField txtNombre = crearCampoTexto();
        JTextField txtApellido = crearCampoTexto();
        JTextField txtTelefono = crearCampoTexto();
        JTextField txtEmail = crearCampoTexto();
        JPasswordField txtPassword = crearCampoPassword();
        JComboBox<String> cmbTurno = new JComboBox<>(new String[]{"Día", "Noche"});

        agregarCampo(formulario, "ID:", txtId);
        agregarCampo(formulario, "Nombre:", txtNombre);
        agregarCampo(formulario, "Apellido:", txtApellido);
        agregarCampo(formulario, "Teléfono:", txtTelefono);
        agregarCampo(formulario, "Email:", txtEmail);
        agregarCampo(formulario, "Contraseña:", txtPassword);
        agregarCampo(formulario, "Turno:", cmbTurno);

        panel.add(formulario, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);

        JButton btnRegistrar = crearBoton("Registrarse", new Color(255, 218, 185));
        JButton btnVolver = crearBoton("← Volver", new Color(200, 200, 200));

        btnRegistrar.addActionListener(e -> {
            if (validarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword)) {
                String resultado = controlador.procesarRegistroRecepcionista(
                        txtId.getText().trim(),
                        txtNombre.getText().trim(),
                        txtApellido.getText().trim(),
                        txtTelefono.getText().trim(),
                        txtEmail.getText().trim(),
                        new String(txtPassword.getPassword()),
                        (String) cmbTurno.getSelectedItem()
                );

                if (resultado == null) {
                    // Éxito - La Vista construye su propio mensaje
                    JOptionPane.showMessageDialog(panel,
                            "¡Registro exitoso!\nBienvenido",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword);
                    cardLayout.show(panelPrincipal, "BIENVENIDA");
                } else {
                    JOptionPane.showMessageDialog(panel, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVolver.addActionListener(e -> {
            limpiarCampos(txtId, txtNombre, txtApellido, txtTelefono, txtEmail, txtPassword);
            cardLayout.show(panelPrincipal, "SELECCION_REGISTRO");
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelLogin(String tipoUsuario) {
        ImageIcon fondoIcon = new ImageIcon(getClass().getResource("/recursos/fondo.jpg"));
        JPanel panel = new FondoPanel(fondoIcon.getImage());
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 30, 0);

        JLabel titulo = new JLabel("Iniciar sesión - " + tipoUsuario.toLowerCase());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 35));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setOpaque(false);

        JLabel lblUsuario = new JLabel("ID de Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setMaximumSize(new Dimension(300, 40));
        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblContrasena.setForeground(Color.WHITE);
        lblContrasena.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtContrasena.setPreferredSize(new Dimension(300, 40));
        txtContrasena.setMaximumSize(new Dimension(300, 40));
        txtContrasena.setAlignmentX(Component.LEFT_ALIGNMENT);

        formulario.add(lblUsuario);
        formulario.add(Box.createRigidArea(new Dimension(0, 10)));
        formulario.add(txtUsuario);
        formulario.add(Box.createRigidArea(new Dimension(0, 20)));
        formulario.add(lblContrasena);
        formulario.add(Box.createRigidArea(new Dimension(0, 10)));
        formulario.add(txtContrasena);

        panel.add(formulario, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 0, 0);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);

        Color colorBoton = tipoUsuario.equals("MEDICO") ? new Color(135, 206, 250) :
                tipoUsuario.equals("PACIENTE") ? new Color(152, 251, 152) :
                        new Color(255, 218, 185);

        JButton btnLogin = crearBoton("Iniciar Sesión", colorBoton);
        JButton btnVolver = crearBoton("← Volver", new Color(200, 200, 200));

        btnLogin.addActionListener(e -> {
            String resultado = controlador.procesarLogin(
                    txtUsuario.getText().trim(),
                    new String(txtContrasena.getPassword()),
                    tipoUsuario,
                    this
            );

            if (resultado != null) {
                JOptionPane.showMessageDialog(panel, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            txtUsuario.setText("");
            txtContrasena.setText("");
            cardLayout.show(panelPrincipal, "SELECCION_LOGIN");
        });

        panelBotones.add(btnLogin);
        panelBotones.add(btnVolver);
        panel.add(panelBotones, gbc);

        return panel;
    }

    // === Métodos auxiliares ===

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField(15);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return campo;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField campo = new JPasswordField(15);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return campo;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        panel.add(lbl);
        panel.add(campo);
    }

    private boolean validarCampos(JComponent... campos) {
        for (JComponent campo : campos) {
            if (campo instanceof JTextField) {
                if (((JTextField) campo).getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor completa todos los campos",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else if (campo instanceof JPasswordField) {
                if (((JPasswordField) campo).getPassword().length == 0) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor completa todos los campos",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

    private void limpiarCampos(JComponent... campos) {
        for (JComponent campo : campos) {
            if (campo instanceof JTextField) {
                ((JTextField) campo).setText("");
            } else if (campo instanceof JPasswordField) {
                ((JPasswordField) campo).setText("");
            }
        }
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        boton.setPreferredSize(new Dimension(180, 50));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorFondo.darker());
                boton.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorFondo);
                boton.repaint();
            }
        });

        return boton;
    }

    static class FondoPanel extends JPanel {
        private final Image imagen;
        public FondoPanel(Image imagen) {
            this.imagen = imagen;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }
}