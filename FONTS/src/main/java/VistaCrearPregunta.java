import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearPregunta extends JPanel {

    private CtrlPresentacio iCtrlPresentacio;
    
    private JTextField txtEnunciat;
    private JComboBox<String> comboTipus; // Desplegable per triar tipus
    private JButton btnOk;
    private JButton btnCancel;

    public VistaCrearPregunta(CtrlPresentacio ctrl) {
        this.iCtrlPresentacio = ctrl;
        setLayout(new GridBagLayout()); // Per centrar-ho
        inicialitzarComponents();
    }

    private void inicialitzarComponents() {
        // Usem un GridLayout de 3 files x 2 columnes amb espai
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        
        // Enunciat 
        form.add(new JLabel("Enunciat de la pregunta:"));
        txtEnunciat = new JTextField(20);
        form.add(txtEnunciat);
        
        // Selector de tipus
        form.add(new JLabel("Tipus de resposta:"));
        
        // Creem les opcions del desplegable
        String[] tipusOptions = { 
            "Opció Única Ordenada", 
            "Opció Única Desordenada",
            "Opció Múltiple", 
            "Numèrica", 
            "Text Lliure" 
        };
        comboTipus = new JComboBox<>(tipusOptions);
        form.add(comboTipus);

        // Botons 
        btnCancel = new JButton("Cancel·lar");
        btnOk = new JButton("Guardar");
        
        form.add(btnCancel);
        form.add(btnOk);

        add(form); 

        btnOk.addActionListener(e -> actionPerformed_btnOk(e));
        btnCancel.addActionListener(e -> actionPerformed_btnCancel(e));
    }

    public void actionPerformed_btnOk(ActionEvent e) {
        String enunciat = txtEnunciat.getText();
        String tipusSeleccionat = (String) comboTipus.getSelectedItem();
        
        if (!enunciat.trim().isEmpty()) {
            // Cridem al controlador passant l'enunciat i el tipus
            iCtrlPresentacio.tractarAfegirPregunta(enunciat, tipusSeleccionat);
            
            // Netejem camps després de guardar
            txtEnunciat.setText("");
            comboTipus.setSelectedIndex(0); 
        } else {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "És obligatori posar la pregunta.");
        }
    }

    public void actionPerformed_btnCancel(ActionEvent e) {
        iCtrlPresentacio.anarAGestionarEnquesta();
    }
}