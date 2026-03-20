import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class VistaMenu extends JPanel {

    private CtrlPresentacio iCtrlPresentacio;
    private JButton btnGestion;
    private JButton btnAnswer;
    private JButton btnAnalysis;
    private JButton btnAnalysisAnswers;
    private JTextArea output;

    public VistaMenu(CtrlPresentacio ctrl) {
        this.iCtrlPresentacio = ctrl;
        inicialitzarComponents();
    }

    private void inicialitzarComponents() {
        setLayout(new BorderLayout());

        // PANELL AMB LES 4 CAIXES: gestionar, respondre i analitzar.
        JPanel panelCaixes = new JPanel(new GridLayout(2, 2, 20, 20));
        panelCaixes.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnGestion = new JButton("Gestionar enquesta");
        btnAnswer = new JButton("Respondre enquesta");
        btnAnalysis = new JButton("Analitzar enquesta de la App");
        btnAnalysisAnswers = new JButton("Analitzar respostes importades");

        panelCaixes.add(createBox(btnGestion, "Gestiona Enquestes: Crea, modifica o elimina."));
        panelCaixes.add(createBox(btnAnswer, "Respon una enquesta."));
        panelCaixes.add(createBox(btnAnalysis, "Executa l’anàlisi amb k-means."));
        panelCaixes.add(createBox(btnAnalysisAnswers, "Analitza respostes existents amb ReadAnswers."));

        add(panelCaixes, BorderLayout.CENTER);

        // CAIXA DE TEXT INFERIOR PER MOSTRAR TOT EL QUE PASSA
        output = new JTextArea(8, 40);
        output.setEditable(false);
        output.setBorder(BorderFactory.createTitledBorder("Resultats"));
        add(new JScrollPane(output), BorderLayout.SOUTH);

        // 3. ASSIGNACIÓ DE LISTENERS
        asignarListeners();
    }

    private void asignarListeners() {
        // Listener estil PROP (Delegació al controlador)
        btnGestion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnGestion(e);
            }
        });

        btnAnswer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnAnswer(e);
            }
        });

        btnAnalysis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnAnalysis(e);
            }
        });

        btnAnalysisAnswers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_btnAnalysisAnswers(e);
            }
        });
    }

    // (Vista -> Controlador) 

    public void actionPerformed_btnGestion(ActionEvent e) {
        // Demanem al controlador que canviï la vista
        iCtrlPresentacio.anarAGestionarEnquesta();
    }

    public void actionPerformed_btnAnswer(ActionEvent e) {
        // Inicia el procés per respondre una enquesta
        iCtrlPresentacio.anarARespondreEnquesta();
    }

    public void actionPerformed_btnAnalysis(ActionEvent e){
        iCtrlPresentacio.anarAnalisis();
    }

    public void actionPerformed_btnAnalysisAnswers(ActionEvent e){
        iCtrlPresentacio.anarAnalisisRespostes();
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