import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearEnquesta extends JPanel {

    private CtrlPresentacio iCtrlPresentacio;
    private JTextField txtTitle;
    private JTextField txtDesc;
    private JButton btnOk;
    private JButton btnCancel;

    public VistaCrearEnquesta(CtrlPresentacio ctrl) {
        this.iCtrlPresentacio = ctrl;
        setLayout(new GridBagLayout()); // Centrat
        inicialitzarComponents();
    }

    private void inicialitzarComponents() {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        
        form.add(new JLabel("Títol de l'enquesta:"));
        txtTitle = new JTextField(20);
        form.add(txtTitle);
        
        form.add(new JLabel("Descripció:"));
        txtDesc = new JTextField(20);
        form.add(txtDesc);

        btnOk = new JButton("Guardar");
        btnCancel = new JButton("Cancel·lar");
        
        form.add(btnCancel);
        form.add(btnOk);

        add(form); // Posem el formulari al centre del panell.

        // Listeners, aquí tot junt per no crear una nova classe.
        btnOk.addActionListener(e -> actionPerformed_btnOk(e));
        btnCancel.addActionListener(e -> actionPerformed_btnCancel(e));
    }

    public void actionPerformed_btnOk(ActionEvent e) {
        String t = txtTitle.getText();
        String d = txtDesc.getText();
        
        // Posar títol de mida menor a 100 és obligatori. Descripció no és obligatòria.
        if (!t.isEmpty() && t.length() < 100) {
            iCtrlPresentacio.tractarCrearEnquesta(t, d);
            // Netejem camps
            txtTitle.setText("");
            txtDesc.setText("");
        } else {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "És obligatori posar títol.");
        }
    }

    public void actionPerformed_btnCancel(ActionEvent e) {
        iCtrlPresentacio.anarAGestionarEnquesta();
    }
}