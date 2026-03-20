import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

public class VistaAnalisis extends JPanel {

    private CtrlPresentacio ctrl;
    private String currentAnswersFilePath = null;

    private JSpinner spinnerK;
    private JButton btnAnalitzar;
    private JButton btnAnalitzarRespostes;
    private JTextArea txtResult;
    private JLabel headerLabel;

    public VistaAnalisis(CtrlPresentacio ctrl, String surveyTitle) {
        this(ctrl, surveyTitle, true);
    }

    public VistaAnalisis(CtrlPresentacio ctrl, String surveyTitle, boolean showClustering) {
        this.ctrl = ctrl;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20,20,20,20));

        headerLabel = new JLabel();
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Top container: header + controls stacked vertically
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (showClustering) {
            center.add(new JLabel("Valor de k:"));
            spinnerK = new JSpinner(new SpinnerNumberModel(2, 2, 100, 1));
            center.add(spinnerK);

            btnAnalitzar = new JButton("Fer Anàlisi");
            center.add(btnAnalitzar);
        }

        if (!showClustering) {
            btnAnalitzarRespostes = new JButton("Analitzar Respostes");
            center.add(btnAnalitzarRespostes);
        }

        top.add(headerLabel);
        top.add(center);
        add(top, BorderLayout.NORTH);

        refreshHeader(showClustering);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshHeader(showClustering);
            }
        });

        txtResult = new JTextArea(20, 80);
        txtResult.setEditable(false);
        add(new JScrollPane(txtResult), BorderLayout.CENTER);

        if (showClustering) {
            btnAnalitzar.addActionListener(e -> {
                refreshHeader(true);
                int k = (Integer) spinnerK.getValue();
                try {
                        String result = this.ctrl.tractarFerAnalisi(String.valueOf(k));
                    if (result.startsWith("ERROR:")) {
                        String errorMsg = result.substring(6).trim();
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(VistaAnalisis.this), errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        txtResult.setText(result);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(VistaAnalisis.this), "Error during analysis: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        if (!showClustering) {
            btnAnalitzarRespostes.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser("data/respostes");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
                
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    currentAnswersFilePath = selectedFile.getAbsolutePath();
                    refreshHeader(false);
                    try {
                        System.out.println("Analyzing your answers...");
                            String analysis = this.ctrl.tractarFerAnalisiRespostes(currentAnswersFilePath);
                        System.out.println("Analysis result: " + analysis);
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
            btnSortir.addActionListener(e -> this.ctrl.anarAMenu());
        panelSud.add(btnSortir);
        add(panelSud, BorderLayout.SOUTH);
    }

    private void refreshHeader(boolean showClustering) {
        if (showClustering) {
            String title = this.ctrl.getCurrentSurveyTitle();
            if (title == null || title.trim().isEmpty()) {
                title = "(sense enquesta)";
            }
            headerLabel.setText("Anàlisi: " + title);
        } else {
            if (currentAnswersFilePath != null) {
                headerLabel.setText("Anàlisi de respostes: " + new File(currentAnswersFilePath).getName());
            } else {
                headerLabel.setText("Anàlisi de respostes");
            }
        }
    }
}
