import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class PanelEditorPregunta extends JPanel {

    private CtrlPresentacio iCtrlPresentacio;

    // Dades de context
    private int indexPregunta;     
    private int totalPreguntes;    
    private boolean esPreguntaText; 

    // Components gràfics
    private JTextField txtEnunciat;
    private JComboBox<String> comboTipus; 
    private JLabel lblInfoHeader; 

    private JSpinner spinnerMaxRespostes; // visible only for multiple choice

    private JPanel panelLlistaOpcions;
    private JScrollPane scrollPaneOpcions;
    private List<JTextField> llistaTextFields;

    private JPanel panelControlOpcions;

    // Botons
    private JButton btnAnterior;
    private JButton btnSeguent;
    private JButton btnAfegir; 
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnEliminar;
    // btnFinalitzar ELIMINAT

    public PanelEditorPregunta(CtrlPresentacio ctrl, 
                               String enunciat, 
                               String tipus, 
                               String opcionsSerialized, 
                               int index, 
                               int total,
                               Integer maxRespostesInicial) {
        
        List<String> opcions = null;
        if (opcionsSerialized != null && !opcionsSerialized.isEmpty()) {
            opcions = Arrays.asList(opcionsSerialized.split("\\|\\|"));
        }
        
        this.iCtrlPresentacio = ctrl;
        this.indexPregunta = index;
        this.totalPreguntes = total;
        this.llistaTextFields = new ArrayList<>();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        inicialitzarComponents(enunciat, tipus, maxRespostesInicial);
        carregarDadesInicials(tipus, opcions, maxRespostesInicial);
    }

    private void inicialitzarComponents(String enunciatInicial, String tipusInicial, Integer maxRespostesInicial) {

        // --- NORD ---
        JPanel panelNord = new JPanel(new BorderLayout(10, 10));
        panelNord.setBorder(new EmptyBorder(0, 0, 15, 0));

        String titolInfo = (indexPregunta == -1) ? "Creant Nova Pregunta" : "Editant Pregunta " + (indexPregunta + 1) + " de " + totalPreguntes;
        lblInfoHeader = new JLabel(titolInfo);
        lblInfoHeader.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInfoHeader.setForeground(Color.GRAY);

        JPanel panelEnunciat = new JPanel(new BorderLayout());
        JLabel lblTitolEnunciat = new JLabel("Enunciat:");
        txtEnunciat = new JTextField(enunciatInicial);
        txtEnunciat.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        panelEnunciat.add(lblInfoHeader, BorderLayout.NORTH);
        panelEnunciat.add(lblTitolEnunciat, BorderLayout.WEST); 
        panelEnunciat.add(txtEnunciat, BorderLayout.CENTER);

        JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelSelector.add(new JLabel("Tipus: "));
        
        String[] tipusOptions = { 
            "Opció Única Ordenada", 
            "Opció Única Desordenada",
            "Opció Múltiple", 
            "Numèrica", 
            "Text Lliure" 
        };
        comboTipus = new JComboBox<>(tipusOptions);
        
        if (tipusInicial != null && !tipusInicial.isEmpty()) {
            comboTipus.setSelectedItem(tipusInicial);
        }
        panelSelector.add(comboTipus);

        panelNord.add(panelEnunciat, BorderLayout.CENTER);
        panelNord.add(panelSelector, BorderLayout.EAST);
        
        add(panelNord, BorderLayout.NORTH);

        // --- CENTRE ---
        panelLlistaOpcions = new JPanel();
        panelLlistaOpcions.setLayout(new BoxLayout(panelLlistaOpcions, BoxLayout.Y_AXIS));

        scrollPaneOpcions = new JScrollPane(panelLlistaOpcions);
        scrollPaneOpcions.setBorder(BorderFactory.createTitledBorder("Configuració"));
        // Spinner for maximum responses (only used for multiple choice)
        spinnerMaxRespostes = new JSpinner(new SpinnerNumberModel(-1, -1, Integer.MAX_VALUE, 1));
        spinnerMaxRespostes.setPreferredSize(new Dimension(80, 25));
        if (maxRespostesInicial != null) spinnerMaxRespostes.setValue(maxRespostesInicial);
        
        add(scrollPaneOpcions, BorderLayout.CENTER);

        // --- SUD ---
        JPanel panelSud = new JPanel(new BorderLayout());
        panelSud.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Controls Opcions
        panelControlOpcions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnMes = new JButton("+");
        JButton btnMenys = new JButton("-");
        
        Dimension dimBtn = new Dimension(45, 30);
        btnMes.setPreferredSize(dimBtn);
        btnMenys.setPreferredSize(dimBtn);
        
        panelControlOpcions.add(btnMes);
        panelControlOpcions.add(Box.createHorizontalStrut(5));
        panelControlOpcions.add(btnMenys);

        // Navegació (keeps place for potential extra nav controls)
        JPanel panelNavegacio = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAnterior = new JButton("<< Anterior");
        btnSeguent = new JButton("Següent >>");

        if (indexPregunta <= 0) btnAnterior.setEnabled(false);
        if (indexPregunta >= totalPreguntes - 1 || indexPregunta == -1) btnSeguent.setEnabled(false);
        // Accions
        JPanel panelAccions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnAfegir = new JButton("Nova Pregunta");
        btnAfegir.setBackground(new Color(230, 255, 230)); 
        
        btnGuardar = new JButton("Guardar i Sortir");
        btnCancelar = new JButton("Cancel·lar");
        btnEliminar = new JButton("Eliminar Pregunta");
        btnEliminar.setBackground(new Color(255, 230, 230));

        // Place navigation buttons first in actions so they are visible and adjacent
        panelAccions.add(btnAnterior);
        panelAccions.add(btnSeguent);
        panelAccions.add(Box.createHorizontalStrut(10));
        panelAccions.add(btnAfegir);
        panelAccions.add(btnCancelar);
        panelAccions.add(btnEliminar);
        panelAccions.add(btnGuardar);

        panelSud.add(panelControlOpcions, BorderLayout.WEST);
        panelSud.add(panelNavegacio, BorderLayout.CENTER);
        panelSud.add(panelAccions, BorderLayout.EAST);
        
        add(panelSud, BorderLayout.SOUTH);

        // Disable delete when creating a new question
        btnEliminar.setEnabled(indexPregunta >= 0);

        // --- LISTENERS ---

        comboTipus.addActionListener(e -> actualitzarConfiguracioTipus());

        btnMes.addActionListener(e -> {
            afegirCampOpcio("");
            actualitzarScroll();
        });

        btnMenys.addActionListener(e -> {
            eliminarUltimaOpcio();
            actualitzarScroll();
        });

        btnAnterior.addActionListener(e -> {
            if (guardarDadesCurrents()) {
                iCtrlPresentacio.navegarPregunta(String.valueOf(indexPregunta - 1));
            }
        });

        btnSeguent.addActionListener(e -> {
            if (guardarDadesCurrents()) {
                iCtrlPresentacio.navegarPregunta(String.valueOf(indexPregunta + 1));
            }
        });

        btnAfegir.addActionListener(e -> {
            if (guardarDadesCurrents()) {
                iCtrlPresentacio.tractarAfegirPreguntaDesDeModificar();
            }
        });

        btnGuardar.addActionListener(e -> {
            if (guardarDadesCurrents()) {
                if (indexPregunta == -1) {
                    iCtrlPresentacio.anarACrearPregunta();
                } else {
                    iCtrlPresentacio.anarAMenu();
                }
            }
        });

        btnCancelar.addActionListener(e -> iCtrlPresentacio.anarAGestionarEnquesta());

        // Delete listener: confirm and call controller
        btnEliminar.addActionListener(e -> {
            if (indexPregunta < 0) {
                JOptionPane.showMessageDialog(this, "No es pot eliminar una pregunta que encara no existeix.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int resp = JOptionPane.showConfirmDialog(this, "Estàs segur que vols eliminar aquesta pregunta?", "Confirmar eliminació", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                iCtrlPresentacio.eliminarPregunta(String.valueOf(indexPregunta));
            }
        });
    }

    private void carregarDadesInicials(String tipus, List<String> opcions, Integer maxRespostesInicial) {
        actualitzarConfiguracioTipus();

        if (!esPreguntaText) {
            panelLlistaOpcions.removeAll();
            llistaTextFields.clear();

            if (opcions != null && !opcions.isEmpty()) {
                for (String op : opcions) {
                    afegirCampOpcio(op);
                }
            } else {
                afegirCampOpcio("");
            }
            // If it's multiple choice, add spinner at top with current value
            String tipusSeleccionat = (String) comboTipus.getSelectedItem();
            if (tipusSeleccionat != null && tipusSeleccionat.contains("Múltiple")) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.setMaximumSize(new Dimension(800, 45));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);
                row.add(new JLabel("Màxim respostes ( -1 = sense límit ): "));
                if (maxRespostesInicial != null) spinnerMaxRespostes.setValue(maxRespostesInicial);
                row.add(spinnerMaxRespostes);
                panelLlistaOpcions.add(row, 0);
            }
            panelLlistaOpcions.revalidate();
            panelLlistaOpcions.repaint();
        }
    }

    private void actualitzarConfiguracioTipus() {
        String tipusSeleccionat = (String) comboTipus.getSelectedItem();
        
        esPreguntaText = tipusSeleccionat.contains("Lliure") || tipusSeleccionat.contains("Numèrica");
        
        panelLlistaOpcions.removeAll();
        llistaTextFields.clear();

        if (esPreguntaText) {
            panelControlOpcions.setVisible(false);
            scrollPaneOpcions.setBorder(BorderFactory.createTitledBorder("Àrea de Resposta"));

            JLabel lblInfo = new JLabel("<html><div style='text-align: center; color: gray;'><i>" +
                    "Aquest tipus de pregunta no té opcions predefinides.<br>" +
                    "L'usuari respondrà amb text o números." +
                    "</i></div></html>");
            lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblInfo.setBorder(new EmptyBorder(40, 20, 20, 20));
            panelLlistaOpcions.add(lblInfo);

        } else {
            panelControlOpcions.setVisible(true);
            scrollPaneOpcions.setBorder(BorderFactory.createTitledBorder("Opcions de Resposta"));

            // Add spinner for multiple choice type only
            if (tipusSeleccionat != null && tipusSeleccionat.contains("Múltiple")) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.setMaximumSize(new Dimension(800, 45));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);
                row.add(new JLabel("Màxim respostes ( -1 = sense límit ): "));
                row.add(spinnerMaxRespostes);
                panelLlistaOpcions.add(row);
            }

            afegirCampOpcio("");
            afegirCampOpcio("");
        }

        panelLlistaOpcions.revalidate();
        panelLlistaOpcions.repaint();
    }

    private void afegirCampOpcio(String textInicial) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelNum = new JLabel("Opció " + (llistaTextFields.size() + 1) + ": ");
        
        JTextField txt = new JTextField(textInicial, 30);
        
        row.add(labelNum);
        row.add(txt);
        row.setMaximumSize(new Dimension(800, 45)); 
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelLlistaOpcions.add(row);
        llistaTextFields.add(txt); 
        updateSpinnerLimit();
    }

    private void eliminarUltimaOpcio() {
        if (llistaTextFields.size() > 1) {
            int index = llistaTextFields.size() - 1;
            llistaTextFields.remove(index);
            panelLlistaOpcions.remove(index);
            updateSpinnerLimit();
        } else {
            JOptionPane.showMessageDialog(this, "Hi ha d'haver com a mínim una opció disponible.");
        }
    }

    /**
     * Ajusta el límit superior del spinner perquè no sigui més gran que
     * el nombre d'opcions actual. Permet el valor -1 (sense límit).
     */
    private void updateSpinnerLimit() {
        if (spinnerMaxRespostes == null) return;
        SpinnerNumberModel model = (SpinnerNumberModel) spinnerMaxRespostes.getModel();
        int numOpcions = Math.max(1, llistaTextFields.size());
        model.setMaximum(Integer.valueOf(numOpcions));

        Object val = model.getValue();
        try {
            int cur = (val instanceof Integer) ? (Integer) val : Integer.parseInt(String.valueOf(val));
            if (cur == -1) {
                // allow -1 intentionally
                model.setMinimum(Integer.valueOf(-1));
            } else if (cur > numOpcions) {
                model.setValue(Integer.valueOf(numOpcions));
            }
        } catch (Exception e) {
            model.setValue(Integer.valueOf(-1));
        }
    }

    private void actualitzarScroll() {
        panelLlistaOpcions.revalidate();
        panelLlistaOpcions.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPaneOpcions.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private boolean guardarDadesCurrents() {
        String nouEnunciat = txtEnunciat.getText().trim();
        String tipusSeleccionat = (String) comboTipus.getSelectedItem();
        List<String> novesOpcions = new ArrayList<>();
        
        if (nouEnunciat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'enunciat no pot estar buit.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!esPreguntaText) {
            for (JTextField txt : llistaTextFields) {
                String valor = txt.getText().trim();
                if (valor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No pots deixar opcions buides.", "Error", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                novesOpcions.add(valor);
            }
            if (novesOpcions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Has d'afegir almenys una opció.", "Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } 

        // get maxRespostes if multiple
        int max = -1;
        if (tipusSeleccionat != null && tipusSeleccionat.contains("Múltiple")) {
            try { max = (Integer) spinnerMaxRespostes.getValue(); } catch (Exception ex) { max = -1; }
        }

        // convert index and options to Strings to pass to controller
        String indexStr = String.valueOf(indexPregunta);
        String opcionsSerialized = null;
        if (novesOpcions != null) opcionsSerialized = String.join("||", novesOpcions);
        iCtrlPresentacio.guardarModificacioPreguntaOpcions(indexStr, nouEnunciat, tipusSeleccionat, opcionsSerialized, max);
        return true;
    }
}