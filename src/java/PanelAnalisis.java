import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;

public class PanelAnalisis extends JPanel {

    private CtrlPresentacio ctrl;
    private String currentAnswersFilePath = null;

    private JSpinner spinnerK;
    private JButton btnAnalitzar;
    private JButton btnAnalitzarRespostes;
    private JTextArea txtResult;

    public PanelAnalisis(CtrlPresentacio ctrl, String surveyTitle) {
        this(ctrl, surveyTitle, true);
    }

    public PanelAnalisis(CtrlPresentacio ctrl, String surveyTitle, boolean showClustering) {
        this.ctrl = ctrl;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20,20,20,20));

        String title = ctrl.getCurrentSurveyTitle();
        if (title == null) {
            title = showClustering ? "(sense enquesta)" : "Anàlisi de respostes";
        }
        String headerText = showClustering ? "Anàlisi: " + title : title;
        JLabel header = new JLabel(headerText);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        center.add(new JLabel("Valor de k:"));
        spinnerK = new JSpinner(new SpinnerNumberModel(2, 2, 100, 1));
        center.add(spinnerK);

        btnAnalitzar = new JButton("Fer Anàlisi");
        center.add(btnAnalitzar);

        if (!showClustering) {
            btnAnalitzarRespostes = new JButton("Analitzar Respostes");
            center.add(btnAnalitzarRespostes);
        }

        add(center, BorderLayout.CENTER);

        txtResult = new JTextArea(12, 60);
        txtResult.setEditable(false);
        add(new JScrollPane(txtResult), BorderLayout.SOUTH);

        btnAnalitzar.addActionListener(e -> {
            int k = (Integer) spinnerK.getValue();
            try {
                String result;
                if (showClustering) {
                    result = ctrl.tractarFerAnalisi(String.valueOf(k));
                } else {
                    if (currentAnswersFilePath == null) {
                        result = "Primer selecciona un fitxer de respostes.";
                    } else {
                        result = ctrl.tractarFerAnalisiRespostes(currentAnswersFilePath, String.valueOf(k));
                    }
                }
                txtResult.setText(result);
            } catch (IOException ex) {
                txtResult.setText("Error during analysis: " + ex.getMessage());
            }
        });

        if (!showClustering) {
            btnAnalitzarRespostes.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser("data/respostes");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
                
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    currentAnswersFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        String analysis = ctrl.analitzarRespostes(currentAnswersFilePath);
                        txtResult.setText(analysis);
                    } catch (IOException ex) {
                        txtResult.setText("Error during answers analysis: " + ex.getMessage());
                    }
                }
            });
        }

        // South panel with exit button
        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSud.setBorder(new EmptyBorder(10, 20, 10, 20));
        JButton btnSortir = new JButton("Sortir");
        btnSortir.addActionListener(e -> ctrl.anarAMenu());
        panelSud.add(btnSortir);
        add(panelSud, BorderLayout.PAGE_END);
    }
}
