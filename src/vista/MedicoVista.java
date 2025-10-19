package vista;

import controlador.Controlador;
import modelo.*;
import javax.swing.*;
import java.awt.*;

/**
 * Vista principal para el médico
 * Muestra las funcionalidades disponibles para el médico
 */
public class MedicoVista extends JFrame {
    private Medico medico;
    private Controlador controlador;

    public MedicoVista(Medico medico, Controlador controlador) {
        super("Panel del Médico - " + medico.nombreCompleto());
        this.medico = medico;
        this.controlador = controlador;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(135, 206, 250));

        // Panel superior con información del médico
        JPanel panelInfo = crearPanelInformacion();
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);

        // Panel central con opciones
        JPanel panelOpciones = crearPanelOpciones();
        panelPrincipal.add(panelOpciones, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(100, 149, 237));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblBienvenida = new JLabel("Bienvenido Dr(a). " + medico.nombreCompleto());
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblEspecialidad = new JLabel("Especialidad: " + medico.getEspecialidad());
        lblEspecialidad.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblEspecialidad.setForeground(Color.WHITE);

        String consultorio = medico.getConsultorioAsignado().isEmpty() ?
                "Sin asignar" : medico.getConsultorioAsignado();
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

    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(135, 206, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Primera fila
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(crearBotonOpcion("Ver Mi Agenda", "📅", e -> verAgenda()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Atender Cita", "💪", e -> atenderCita()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Ver Paciente", "👤", e -> verPaciente()), gbc);

        // Segunda fila
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(crearBotonOpcion("Remitir Paciente", "📋", e -> remitirPaciente()), gbc);

        gbc.gridx = 1;
        panel.add(crearBotonOpcion("Cancelar Cita", "X", e -> cancelarCita()), gbc);

        gbc.gridx = 2;
        panel.add(crearBotonOpcion("Historial de Citas", "📊", e -> verHistorial()), gbc);

        return panel;
    }

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

    private void verAgenda() {
        StringBuilder agenda = new StringBuilder("AGENDA DEL DR(A). " + medico.nombreCompleto() + "\n\n");

        if (medico.getAgenda().isEmpty()) {
            agenda.append("No tienes citas agendadas.");
        } else {
            for (Cita cita : medico.getAgenda()) {
                agenda.append(cita.toString()).append("\n").append("-".repeat(50)).append("\n\n");
            }
        }

        JTextArea textArea = new JTextArea(agenda.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Mi Agenda", JOptionPane.INFORMATION_MESSAGE);
    }

    private void atenderCita() {
        String idCita = JOptionPane.showInputDialog(this, "Ingrese el ID de la cita:");
        if (idCita != null && !idCita.trim().isEmpty()) {
            String diagnostico = JOptionPane.showInputDialog(this, "Diagnóstico:");
            if (diagnostico != null && !diagnostico.trim().isEmpty()) {
                String tratamiento = JOptionPane.showInputDialog(this, "Tratamiento:");
                if (tratamiento != null && !tratamiento.trim().isEmpty()) {
                    String observaciones = JOptionPane.showInputDialog(this, "Observaciones:");

                    boolean exito = controlador.atenderCita(idCita, diagnostico, tratamiento,
                            observaciones != null ? observaciones : "");

                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Cita atendida exitosamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al atender la cita",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void verPaciente() {
        String idPaciente = JOptionPane.showInputDialog(this, "Ingrese el ID del paciente:");
        if (idPaciente != null && !idPaciente.trim().isEmpty()) {
            Paciente paciente = controlador.verPaciente(idPaciente);

            if (paciente != null) {
                String info = "INFORMACIÓN DEL PACIENTE\n\n" + paciente.toString();
                JOptionPane.showMessageDialog(this, info, "Datos del Paciente",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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

    private void cancelarCita() {
        String idCita = JOptionPane.showInputDialog(this, "Ingrese el ID de la cita a cancelar:");
        if (idCita != null && !idCita.trim().isEmpty()) {
            boolean exito = controlador.cancelarCita(idCita, medico.getId());

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Cita cancelada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al cancelar la cita. Verifica el ID.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verConsultorio() {
        Consultorio consultorio = controlador.verConsultorioAsignado(medico.getId());

        if (consultorio != null) {
            String info = "CONSULTORIO ASIGNADO\n\n" + consultorio.toString();
            JOptionPane.showMessageDialog(this, info, "Mi Consultorio",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No tienes consultorio asignado actualmente",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void verHistorial() {
        StringBuilder historial = new StringBuilder("HISTORIAL DE CITAS ATENDIDAS\n\n");

        int citasAtendidas = 0;
        for (Cita cita : medico.getAgenda()) {
            if (cita.getEstadoCita() == citaState.COMPLETADA) {
                historial.append(cita.toString()).append("\n").append("-".repeat(50)).append("\n\n");
                citasAtendidas++;
            }
        }

        if (citasAtendidas == 0) {
            historial.append("No has atendido citas aún.");
        }

        JTextArea textArea = new JTextArea(historial.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Historial", JOptionPane.INFORMATION_MESSAGE);
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