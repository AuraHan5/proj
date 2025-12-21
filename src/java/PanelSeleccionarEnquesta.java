import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PanelSeleccionarEnquesta extends JPanel {

    private CtrlPresentacio ctrl;
    private JList<String> listEnquestes;
    private DefaultListModel<String> listModel;

    public PanelSeleccionarEnquesta(CtrlPresentacio ctrl) {
        this(ctrl, "MODIFICAR");
    }

    /**
     * Constructor amb mode: "MODIFICAR", "RESPONDRE", "ANALISIS" o "ANALISIS_RESPOS".
     */
    public PanelSeleccionarEnquesta(CtrlPresentacio ctrl, String mode) {
        this.ctrl = ctrl;

        List<String> enquestes = ctrl.getSurveyTitles();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        String titol = "Selecciona l'enquesta";
        String btnText = "Seleccionar aquesta enquesta";
        if ("RESPONDRE".equals(mode)) {
            titol = "Selecciona l'enquesta a respondre:";
            btnText = "Respondre aquesta enquesta";
        } else if ("ANALISIS".equals(mode)) {
            titol = "Selecciona l'enquesta a analitzar (clustering):";
            btnText = "Analitzar aquesta enquesta";
        } else if ("ANALISIS_RESPOS".equals(mode)) {
            titol = "Selecciona l'enquesta per analitzar respostes:";
            btnText = "Analitzar respostes d'aquesta enquesta";
        } else {
            titol = "Selecciona l'enquesta a modificar:";
            btnText = "Modificar aquesta enquesta";
        }

        // Títol
        JLabel lblTitol = new JLabel(titol);
        lblTitol.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitol.setBorder(new EmptyBorder(0,0,10,0));
        add(lblTitol, BorderLayout.NORTH);

        // Llista central
        listModel = new DefaultListModel<>();
        for (String s : enquestes) {
            listModel.addElement(s);
        }

        listEnquestes = new JList<>(listModel);
        listEnquestes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listEnquestes);
        add(scrollPane, BorderLayout.CENTER);

        // Botons sud
        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancel·lar");
        JButton btnSeleccionar = new JButton(btnText);

        panelSud.add(btnCancelar);
        panelSud.add(btnSeleccionar);
        add(panelSud, BorderLayout.SOUTH);

        btnSeleccionar.addActionListener(e -> {
            String seleccionada = listEnquestes.getSelectedValue();
            if (seleccionada != null) {
                if ("RESPONDRE".equals(mode)) ctrl.tractarSeleccioEnquestaPerRespond(seleccionada);
                else if ("ANALISIS".equals(mode)) ctrl.tractarSeleccioEnquestaPerAnalisis(seleccionada);
                else if ("ANALISIS_RESPOS".equals(mode)) ctrl.tractarSeleccioEnquestaPerAnalisisRespostes(seleccionada);
                else ctrl.tractarSeleccioEnquestaPerModificar(seleccionada);
            } else {
                JOptionPane.showMessageDialog(this, "Has de seleccionar una enquesta de la llista.");
            }
        });

        btnCancelar.addActionListener(e -> ctrl.anarAMenu());
    }
}