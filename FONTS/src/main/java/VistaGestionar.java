import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;

public class VistaGestionar extends JPanel {

    private CtrlPresentacio iCtrlPresentacio;
    private JButton btnCreate;
    private JButton btnModify;
    private JButton btnDelete;
    private JButton btnImport;
    private JButton btnSortir;

    public VistaGestionar(CtrlPresentacio ctrl) {
        this.iCtrlPresentacio = ctrl;
        setLayout(new GridBagLayout()); // Centrat
        inicialitzarComponents();
    }

    private void inicialitzarComponents() {
        setLayout(new BorderLayout());

        // PANELL AMB LES 4 CAIXES: gestionar, respondre i analitzar.
        JPanel panelCaixes = new JPanel(new GridLayout(2, 2, 20, 20));
        panelCaixes.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnCreate = new JButton("Crea");
        btnImport = new JButton("Importa");
        btnModify = new JButton("Modifica");
        btnDelete = new JButton("Esborra");

        panelCaixes.add(createBox(btnCreate, "Crea una enquesta."));
        panelCaixes.add(createBox(btnImport, "Importa una enquesta des d'un fitxer CSV."));
        panelCaixes.add(createBox(btnModify, "Modifica una enquesta."));
        panelCaixes.add(createBox(btnDelete, "Esborra una enquesta."));

        add(panelCaixes, BorderLayout.CENTER);

        // SOUTH: button to return to main menu
        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSud.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnSortir = new JButton("Sortir");
        panelSud.add(btnSortir);
        add(panelSud, BorderLayout.SOUTH);

        // 3. ASSIGNACIÓ DE LISTENERS
        asignarListeners();
    }

    private void asignarListeners() {
        // Listener estil PROP (Delegació al controlador)
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnCreate(e);
            }
        });

        btnModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnModify(e);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnAnalysis(e);
            }
        });

        btnImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnImport(e);
            }
        });

        btnSortir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iCtrlPresentacio.anarAMenu();
            }
        });
    }

    public void actionPerformed_btnCreate(ActionEvent e) {
        iCtrlPresentacio.anarACrearEnquesta();
    }

    public void actionPerformed_btnModify(ActionEvent e) {
        iCtrlPresentacio.anarAModificarEnquesta();
    }

    public void actionPerformed_btnAnalysis(ActionEvent e) {
        iCtrlPresentacio.anarAEliminarEnquesta();
    }

    public void actionPerformed_btnImport(ActionEvent e) {
        // Open file chooser for data/enquestes
        JFileChooser fileChooser = new JFileChooser("data/enquestes");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Ask for title
            String title = JOptionPane.showInputDialog(SwingUtilities.getWindowAncestor(this), "Introdueix el títol de l'enquesta:");
            if (title == null || title.trim().isEmpty()) return;
            
            // Ask for description
            String description = JOptionPane.showInputDialog(SwingUtilities.getWindowAncestor(this), "Introdueix la descripció de l'enquesta:");
            if (description == null) description = "";
            
            try {
                iCtrlPresentacio.importarEnquesta(filePath, title.trim(), description.trim());
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Enquesta importada correctament!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Error en importar l'enquesta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createBox(JButton btn, String textDescripcio) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(15, 15, 15, 15)));
        panel.setBackground(new Color(245, 245, 245));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH; gbc.insets = new Insets(0, 0, 15, 0);
        
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 40));
        btn.setFocusPainted(false);
        panel.add(btn, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 0);
        JTextArea desc = new JTextArea(textDescripcio);
        desc.setLineWrap(true); desc.setWrapStyleWord(true); desc.setEditable(false);
        desc.setOpaque(false); desc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        desc.setForeground(Color.DARK_GRAY);
        panel.add(desc, gbc);

        gbc.gridy = 2; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(new JPanel(){{setOpaque(false);}}, gbc);
        return panel;
    }

}