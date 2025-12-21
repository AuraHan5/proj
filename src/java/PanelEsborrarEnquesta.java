import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PanelEsborrarEnquesta extends JPanel {

    private CtrlPresentacio ctrl;
    private JList<String> listEnquestes;
    private DefaultListModel<String> listModel;

    public PanelEsborrarEnquesta(CtrlPresentacio ctrl) {
        this.ctrl = ctrl;

        List<String> enquestes = ctrl.getSurveyTitles();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitol = new JLabel("Selecciona l'enquesta a esborrar:");
        lblTitol.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitol.setBorder(new EmptyBorder(0,0,10,0));
        add(lblTitol, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        for (String s : enquestes) listModel.addElement(s);

        listEnquestes = new JList<>(listModel);
        listEnquestes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(listEnquestes), BorderLayout.CENTER);

        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancel·lar");
        JButton btnEliminar = new JButton("Esborrar aquesta enquesta");

        panelSud.add(btnCancelar);
        panelSud.add(btnEliminar);
        add(panelSud, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> ctrl.anarAGestionarEnquesta());

        btnEliminar.addActionListener(e -> {
            String seleccionada = listEnquestes.getSelectedValue();
            if (seleccionada == null) {
                JOptionPane.showMessageDialog(this, "Has de seleccionar una enquesta de la llista.");
                return;
            }

            int resp = JOptionPane.showConfirmDialog(
                this,
                "Estàs segur que vols esborrar l'enquesta '" + seleccionada + "'?",
                "Confirmació d'esborrat",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (resp == JOptionPane.YES_OPTION) {
                ctrl.tractarEliminarEnquesta(seleccionada);
            }
        });
    }
}
