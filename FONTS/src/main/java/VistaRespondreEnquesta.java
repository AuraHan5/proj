import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VistaRespondreEnquesta extends JPanel {

    private CtrlPresentacio ctrl;
    private List<String> questionsData;
    private int index = 0;
    private Object[] responses;

    // UI
    private JLabel lblHeader;
    private JLabel lblQuestion;
    private JPanel panelCenter;
    private JButton btnAnterior;
    private JButton btnSeguent;
    private JButton btnRespondre;

    public VistaRespondreEnquesta(CtrlPresentacio ctrl, String surveyTitle) {
        this.ctrl = ctrl;
        this.questionsData = this.ctrl.getCurrentSurveyQuestionsData();
        this.responses = new Object[questionsData.size()];

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20,20,20,20));

        String title = this.ctrl.getCurrentSurveyTitle();
        if (title == null) title = "(sense enquesta)";
        lblHeader = new JLabel("Respondent: " + title);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(lblHeader, BorderLayout.NORTH);

        panelCenter = new JPanel(new BorderLayout());
        lblQuestion = new JLabel();
        lblQuestion.setBorder(new EmptyBorder(10,0,10,0));
        panelCenter.add(lblQuestion, BorderLayout.NORTH);

        add(panelCenter, BorderLayout.CENTER);

        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAnterior = new JButton("<< Anterior");
        btnSeguent = new JButton("Següent >>");
        btnRespondre = new JButton("Respondre");

        panelSud.add(btnAnterior);
        panelSud.add(btnSeguent);
        panelSud.add(btnRespondre);
        add(panelSud, BorderLayout.SOUTH);

        btnAnterior.addActionListener(e -> {
            saveCurrent();
            if (index > 0) index--;
            renderCurrent();
        });

        btnSeguent.addActionListener(e -> {
            if (!saveCurrent()) return;
            if (index < questionsData.size()-1) index++;
            renderCurrent();
        });

        btnRespondre.addActionListener(e -> {
            if (!saveCurrent()) return;
            // build serialized representation of responses and send as String
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<responses.length;++i) {
                if (i>0) sb.append("||");
                Object r = responses[i];
                if (r == null) { sb.append("NULL"); continue; }
                if (r instanceof Number) { sb.append("NUM:").append(r.toString()); continue; }
                if (r instanceof Object[]) {
                    Object[] pair = (Object[]) r;
                    sb.append("IDX:").append(pair[0]).append(",").append(pair[1]);
                    continue;
                }
                if (r instanceof java.util.Set) {
                    // join set of labels with a safe separator
                    java.util.Set<?> set = (java.util.Set<?>) r;
                    sb.append("SET:");
                    boolean first = true;
                    for (Object it : set) {
                        if (!first) sb.append(";;;;");
                        sb.append(String.valueOf(it));
                        first = false;
                    }
                    continue;
                }
                // default: string
                sb.append("STR:").append(String.valueOf(r));
            }
            this.ctrl.tractarEnviarResposta(sb.toString());
        });

        renderCurrent();
    }

    private void renderCurrent() {
        panelCenter.removeAll();
        String qData = questionsData.get(index);
        String[] parts = qData.split("\\|\\|");
        String enunciat = parts[0];
        String tipus = parts[1];
        List<String> opcions = parts[2].isEmpty() ? null : Arrays.asList(parts[2].split(";;;;"));
        int maxTemp = -1;
        if (parts.length > 3) {
            try { maxTemp = Integer.parseInt(parts[3]); } catch (Exception e) {}
        }
        final int max = maxTemp;
        lblQuestion.setText("Pregunta " + (index+1) + " / " + questionsData.size() + ": " + enunciat);
        panelCenter.add(lblQuestion, BorderLayout.NORTH);

        JPanel input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));

        // Types: based on tipus
        if ("Opció Única Ordenada".equals(tipus) || "Opció Única Desordenada".equals(tipus)) {
            // single selection but allow deselect by using JToggleButtons
            JPanel optsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            List<JToggleButton> toggles = new ArrayList<>();
            for (int i=0;i<opcions.size();++i) {
                final int idx = i;
                JToggleButton tb = new JToggleButton(opcions.get(i));
                tb.addActionListener(e -> {
                    // if toggled off, clear response
                    if (!tb.isSelected()) {
                        responses[index] = null;
                    } else {
                        // deselect others manually
                        for (JToggleButton other : toggles) {
                            if (other != tb) other.setSelected(false);
                        }
                        // store in the normalized format expected by ReadAnswers
                        if ("Opció Única Ordenada".equals(tipus)) {
                            responses[index] = new Object[]{idx, opcions.size()};
                        } else {
                            responses[index] = opcions.get(idx);
                        }
                    }
                });
                toggles.add(tb);
                optsPanel.add(tb);
            }
            input.add(optsPanel);
            // restore previous (normalized formats)
            if ("Opció Única Ordenada".equals(tipus)) {
                if (responses[index] instanceof Object[]) {
                    Object[] arr = (Object[]) responses[index];
                    if (arr.length>0 && arr[0] instanceof Integer) {
                        int sel = (Integer) arr[0];
                        if (sel >=0 && sel < toggles.size()) toggles.get(sel).setSelected(true);
                    }
                }
            } else {
                if (responses[index] instanceof String) {
                    String selLabel = (String) responses[index];
                    for (int i=0;i<opcions.size();++i) if (opcions.get(i).equals(selLabel)) toggles.get(i).setSelected(true);
                }
            }
        } else if ("Opció Múltiple".equals(tipus)) {
            JPanel optsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            List<JCheckBox> boxes = new ArrayList<>();
            for (int i=0;i<opcions.size();++i) {
                JCheckBox cb = new JCheckBox(opcions.get(i));
                cb.addActionListener(e -> {
                    // Validar que no se exceda el máximo cuando se selecciona
                    if (max > 0) {
                        int selectedCount = 0;
                        for (JCheckBox box : boxes) {
                            if (box.isSelected()) selectedCount++;
                        }
                        if (selectedCount > max && cb.isSelected()) {
                            cb.setSelected(false);
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(VistaRespondreEnquesta.this), 
                                "Nombre màxim de seleccions: " + max, 
                                "Límit de respostes", 
                                JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
                boxes.add(cb);
                optsPanel.add(cb);
            }
            // restore previous (expecting Set<String> of option labels)
            if (responses[index] instanceof Set) {
                Set<?> set = (Set<?>) responses[index];
                for (Object o : set) {
                    if (o instanceof String) {
                        String label = (String) o;
                        for (int i=0;i<boxes.size();++i) if (boxes.get(i).getText().equals(label)) boxes.get(i).setSelected(true);
                    }
                }
            }
            input.add(optsPanel);
        } else if ("Text Lliure".equals(tipus)) {
            JTextArea ta = new JTextArea(6,40);
            if (responses[index] instanceof String) ta.setText((String)responses[index]);
            input.add(new JScrollPane(ta));
        } else if ("Numèrica".equals(tipus)) {
            JTextField tf = new JTextField(20);
            if (responses[index] instanceof Number) tf.setText(responses[index].toString());
            input.add(tf);
        } else {
            input.add(new JLabel("Tipus de pregunta no suportat."));
        }

        panelCenter.add(input, BorderLayout.CENTER);

        btnAnterior.setEnabled(index>0);
        btnSeguent.setEnabled(index<questionsData.size()-1);
        btnRespondre.setEnabled(index==questionsData.size()-1);

        revalidate();
        repaint();
    }

    private boolean saveCurrent() {
        String qData = questionsData.get(index);
        String[] parts = qData.split("\\|\\|");
        String tipus = parts[1];
        List<String> opcions = parts[2].isEmpty() ? null : Arrays.asList(parts[2].split(";;;;"));
        int max = -1;
        if (parts.length > 3) {
            try { max = Integer.parseInt(parts[3]); } catch (Exception e) {}
        }
        Component comp = panelCenter.getComponent(1); // input panel
        if ("Opció Única Ordenada".equals(tipus) || "Opció Única Desordenada".equals(tipus)) {
            // look for selected JToggleButton and store normalized value
            JPanel optsPanel = (JPanel) ((JPanel)comp).getComponent(0);
            int sel = -1;
            for (int i=0;i<optsPanel.getComponentCount();++i) {
                Component c = optsPanel.getComponent(i);
                if (c instanceof JToggleButton) {
                    JToggleButton tb = (JToggleButton)c;
                    if (tb.isSelected()) { sel = i; break; }
                }
            }
            if (sel==-1) responses[index] = null;
            else {
                if ("Opció Única Ordenada".equals(tipus)) {
                    responses[index] = new Object[]{sel, opcions.size()};
                } else {
                    responses[index] = opcions.get(sel);
                }
            }
        } else if ("Opció Múltiple".equals(tipus)) {
            JPanel optsPanel = (JPanel) ((JPanel)comp).getComponent(0);
            Set<String> set = new HashSet<>();
            for (int i=0;i<optsPanel.getComponentCount();++i) {
                Component c = optsPanel.getComponent(i);
                if (c instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox)c;
                    if (cb.isSelected()) set.add(cb.getText());
                }
            }
            // Validar que no se exceda el máximo
            if (max > 0 && set.size() > max) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), 
                    "Nombre màxim de seleccions: " + max, 
                    "Límit de respostes", 
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            responses[index] = set;
        } else if ("Text Lliure".equals(tipus)) {
            JScrollPane sp = (JScrollPane) ((JPanel)comp).getComponent(0);
            JTextArea ta = (JTextArea) sp.getViewport().getView();
            responses[index] = ta.getText();
        } else if ("Numèrica".equals(tipus)) {
            Component c = ((JPanel)comp).getComponent(0);
            if (c instanceof JTextField) {
                String s = ((JTextField)c).getText().trim();
                if (s.isEmpty()) { responses[index] = null; return true; }
                try {
                    Double v = Double.parseDouble(s);
                    responses[index] = v;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Introduïu un número vàlid.");
                    return false;
                }
            }
        }
        return true;
    }
}
