import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaModificarEnquesta extends JPanel {

    private CtrlPresentacio iCtrlPresentacio;
    private JTextField txtTitle;
    private JTextField txtDesc;
    private JButton btnOk;
    private JButton btnCancel;

    public VistaModificarEnquesta(CtrlPresentacio ctrl, String currentTitle, String currentDesc) {
        this.iCtrlPresentacio = ctrl;
        setLayout(new GridBagLayout()); // Centrat
        inicialitzarComponents(currentTitle, currentDesc);
    }

    private void inicialitzarComponents(String currentTitle, String currentDesc) {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        
        form.add(new JLabel("Títol de l'enquesta:"));
        txtTitle = new JTextField(currentTitle != null ? currentTitle : "", 20);
        form.add(txtTitle);
        
        form.add(new JLabel("Descripció:"));
        txtDesc = new JTextField(currentDesc != null ? currentDesc : "", 20);
        form.add(txtDesc);

        btnOk = new JButton("Guardar");
        btnCancel = new JButton("Cancel·lar");
        
        form.add(btnCancel);
        form.add(btnOk);

        add(form); // Posem el formulari al centre del panell.

        // Listeners
        btnOk.addActionListener(e -> actionPerformed_btnOk(e));
        btnCancel.addActionListener(e -> actionPerformed_btnCancel(e));
    }

    public void actionPerformed_btnOk(ActionEvent e) {
        String t = txtTitle.getText();
        String d = txtDesc.getText();
        
        // Posar títol de mida menor a 100 és obligatori. Descripció no és obligatòria.
        if (!t.isEmpty() && t.length() < 100) {
            iCtrlPresentacio.tractarModificarEnquesta(t, d);
        } else {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "És obligatori posar títol.");
        }
    }

    public void actionPerformed_btnCancel(ActionEvent e) {
        iCtrlPresentacio.anarAGestionarEnquesta();
    }
}
