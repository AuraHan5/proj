import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class VistaPrincipal extends JFrame {

    private CtrlPresentacio ctrl;

    // Layout principal que permet canviar entre pantalles
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panells fixos definits prèviament
    private Map<String, JPanel> vistes;
    
    public VistaPrincipal(CtrlPresentacio ctrl) {
        this.ctrl = ctrl;

        // Finestra principal
        setTitle("Gestor d'Enquestes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Diccionari de pantalles
        vistes = new HashMap<>();

        // Carreguem les pantalles fixes
        inicialitzarVistes();

        // Afegim al frame
        add(mainPanel);
    }

    private void inicialitzarVistes() {
        
        PanelMenu menu = new PanelMenu(ctrl);
        afegirVista("MENU", menu);

        // PANTALLA 1
        
        PanelGestionar gestionar = new PanelGestionar(ctrl);
        afegirVista("GESTIONAR", gestionar);

        PanelCrearEnquesta crear = new PanelCrearEnquesta(ctrl);
        afegirVista("CREAR", crear);

        // PANTALLA 2
        PanelCrearPregunta crearPregunta = new PanelCrearPregunta(ctrl);
        afegirVista("CREAR_PREGUNTA", crearPregunta);
    }


    private void afegirVista(String nom, JPanel panel) {
        vistes.put(nom, panel);
        mainPanel.add(panel, nom);
    }

    public void mostrarVista(String nom) {
        if (!vistes.containsKey(nom)) {
            System.err.println("Vista no trobada: " + nom);
            return;
        }
        cardLayout.show(mainPanel, nom);
    }

    public void mostrarVistaDinamica(JPanel novaVista, String nom) {
        // Si ja existia una vista amb aquest nom, l'eliminem
        if (vistes.containsKey(nom)) {
            mainPanel.remove(vistes.get(nom));
        }

        // Afegim la nova
        vistes.put(nom, novaVista);
        mainPanel.add(novaVista, nom);

        // Mostrar-la
        cardLayout.show(mainPanel, nom);

        // Forcem refresc
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    public void ferVisible() {
        setVisible(true);
        cardLayout.show(mainPanel, "MENU");
    }
}
