import javax.swing.*; 
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;

public class CtrlPresentacio {
    
    private CtrlDomini ctrlDomini;                 
    private VistaPrincipal vistaPrincipal;  

    public CtrlPresentacio() {
        ctrlDomini = new CtrlDomini();
        vistaPrincipal = new VistaPrincipal(this);
    }

    // Overload used by Main to decide whether to load persisted data
    public CtrlPresentacio(boolean loadFromDisk) {
        ctrlDomini = new CtrlDomini(loadFromDisk);
        vistaPrincipal = new VistaPrincipal(this);
    }

    public void inicialitzar() {
        vistaPrincipal.ferVisible();
    }

    // --- PANTALLA 1 (Menú Principal) ---

    public void anarAMenu() {
        vistaPrincipal.mostrarVista("MENU");
    }

    public void anarAGestionarEnquesta() {
        vistaPrincipal.mostrarVista("GESTIONAR");
    }

    public void anarARespondreEnquesta(){
        List<String> llista = getSurveyTitles();
        if (llista.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "No hi ha enquestes disponibles per respondre.");
            return;
        }
        VistaSeleccionarEnquesta panelSel = new VistaSeleccionarEnquesta(this, "RESPONDRE");
        vistaPrincipal.mostrarVistaDinamica(panelSel, "SELECCIONAR_ENQUESTA_RESPONDRE");
    }

    public void anarAnalisis(){
        List<String> llista = getSurveyTitles();
        if (llista.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "No hi ha enquestes disponibles per analitzar.");
            return;
        }
        VistaSeleccionarEnquesta panelSel = new VistaSeleccionarEnquesta(this, "ANALISIS");
        vistaPrincipal.mostrarVistaDinamica(panelSel, "SELECCIONAR_ENQUESTA_ANALISIS");
    }

    public void anarAnalisisRespostes(){
        VistaAnalisis panel = new VistaAnalisis(this, null, false);
        vistaPrincipal.mostrarVistaDinamica(panel, "ANALISIS_RESPOS_ENQUESTA");
    }

    // --- PANTALLA 2 (Gestió Enquestes) ---

    public void anarACrearEnquesta(){
        vistaPrincipal.mostrarVista("CREAR");
    }

    public void anarAModificarEnquesta() {
        List<String> llista = getSurveyTitles();

        if (llista.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "No hi ha enquestes disponibles per modificar.");
            return;
        }

        VistaSeleccionarEnquesta panelSel = new VistaSeleccionarEnquesta(this);
        vistaPrincipal.mostrarVistaDinamica(panelSel, "SELECCIONAR_ENQUESTA");
    }

    public void anarAEliminarEnquesta(){
        List<String> llista = getSurveyTitles();

        if (llista.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "No hi ha enquestes disponibles per eliminar.");
            return;
        }

        VistaEsborrarEnquesta panelDel = new VistaEsborrarEnquesta(this);
        vistaPrincipal.mostrarVistaDinamica(panelDel, "ESBORRAR_ENQUESTA");
    }

    // --- PANTALLA 3 (Gestió Preguntes UNIFICADA) ---

    public void anarACrearPregunta(){
        VistaEditorPregunta panelUnificat = new VistaEditorPregunta(
            this, 
            "", 
            "Opció Única Ordenada", 
            "", 
            -1, 
            ctrlDomini.getNumPreguntesActual(),
            -1
        );
        
        vistaPrincipal.mostrarVistaDinamica(panelUnificat, "EDITOR_PREGUNTA");
    }
    
    public void navegarPregunta(String indexDesti) {
        int index;
        try { index = Integer.parseInt(String.valueOf(indexDesti)); }
        catch (Exception e) { return; }

        int total = ctrlDomini.getNumPreguntesActual();
        if (index < 0 || index >= total) return;

        String qData = ctrlDomini.getPreguntaData(index);
        if (qData == null) return;

        String[] parts = qData.split("\\|\\|");
        String enunciat = parts[0];
        String tipus = parts[1];
        int max = -1;
        if (parts.length > 3) {
            try { max = Integer.parseInt(parts[3]); } catch (Exception e) {}
        }

        VistaEditorPregunta panel = new VistaEditorPregunta(
            this,
            enunciat,
            tipus,
            parts[2],
            index,
            total,
            max
        );

        vistaPrincipal.mostrarVistaDinamica(panel, "EDITOR_PREGUNTA");
    }

    // --- MÈTODES DE PERSISTÈNCIA (GUARDAR) ---

    public void guardarModificacioPreguntaOpcions(String indexStr, String enunciat, String tipus, String opcionsSerialized, int maxRespostes) {
        // A. CAS CREACIÓ (Nova Pregunta)
        int index = -1;
        try { index = Integer.parseInt(indexStr); } catch (Exception e) { index = -1; }

        List<String> opcions = null;
        if (opcionsSerialized != null && !opcionsSerialized.isEmpty()) {
            // Split options using dedicated separator to avoid collision with field separator
            opcions = Arrays.asList(opcionsSerialized.split(";;;;"));
        }

        if (index == -1) {
            tractarAfegirPregunta(enunciat, tipus, opcionsSerialized, maxRespostes);
        } 
        // B. CAS MODIFICACIÓ (Editar existent)
        else {
            boolean esTextONumeric = tipus.contains("Lliure") || tipus.contains("Numèrica");
            
            if (esTextONumeric) {
                ctrlDomini.modificarPregunta(index, enunciat, tipus, null, maxRespostes); 
            } else {
                ctrlDomini.modificarPregunta(index, enunciat, tipus, opcions, maxRespostes);
            }
            
            JOptionPane.showMessageDialog(vistaPrincipal, "Canvis guardats correctament a la pregunta " + (index + 1));
        }
    }

    public void tractarAfegirPregunta(String enunciat, String tipus, String opcionsSerialized, int maxRespostes) {
        boolean esTextONumeric = tipus.contains("Lliure") || tipus.contains("Numèrica");

        if (esTextONumeric) {
            ctrlDomini.afegirPreguntaSimple(enunciat, tipus);
        } else {
            List<String> opcions = null;
            if (opcionsSerialized != null && !opcionsSerialized.isEmpty()) opcions = Arrays.asList(opcionsSerialized.split(";;;;"));
            ctrlDomini.afegirPreguntaAmbOpcions(enunciat, tipus, opcions, maxRespostes);
        }
        
        JOptionPane.showMessageDialog(vistaPrincipal, "Nova pregunta creada correctament!");
    }

    public void tractarAfegirPregunta(String enunciat, String tipus) {
        tractarAfegirPregunta(enunciat, tipus, null, -1);
    }

    // --- UTILITATS ---

    public void tractarCrearEnquesta(String titol, String desc) {
        ctrlDomini.SurveyCreation(titol, desc);
        anarACrearPregunta();
    }

    public void tractarSeleccioEnquestaPerRespond(String surveyTitle) {
        ctrlDomini.setCurrentSurveyByTitle(surveyTitle);
        VistaRespondreEnquesta panel = new VistaRespondreEnquesta(this, surveyTitle);
        vistaPrincipal.mostrarVistaDinamica(panel, "RESPONDRE_ENQUESTA");
    }

    public void tractarSeleccioEnquestaPerAnalisis(String surveyTitle) {
        ctrlDomini.setCurrentSurveyByTitle(surveyTitle);
        VistaAnalisis panel = new VistaAnalisis(this, surveyTitle);
        vistaPrincipal.mostrarVistaDinamica(panel, "ANALISIS_ENQUESTA");
    }

    public void tractarSeleccioEnquestaPerAnalisisRespostes(String surveyTitle) {
        ctrlDomini.setCurrentSurveyByTitle(surveyTitle);
        VistaAnalisis panel = new VistaAnalisis(this, surveyTitle, false);
        vistaPrincipal.mostrarVistaDinamica(panel, "ANALISIS_RESPOS_ENQUESTA");
    }

    public void tractarSeleccioEnquestaPerModificar(String surveyTitle) {
        ctrlDomini.setCurrentSurveyByTitle(surveyTitle);
        String currentTitle = ctrlDomini.getCurrentSurveyTitle();
        String currentDesc = ctrlDomini.getCurrentSurveyDescription();
        VistaModificarEnquesta panelModificar = new VistaModificarEnquesta(this, currentTitle, currentDesc);
        vistaPrincipal.mostrarVistaDinamica(panelModificar, "MODIFICAR_ENQUESTA");
    }

    public void tractarModificarEnquesta(String nouTitol, String novaDesc) {
        ctrlDomini.modificarEnquesta(nouTitol, novaDesc);
        if (ctrlDomini.getNumPreguntesActual() > 0) {
            navegarPregunta("0");
        } else {
            anarACrearPregunta();
        }
    }

    /**
     * Executa l'anàlisi sobre l'enquesta actual amb k clusters i retorna
     * una cadena amb el resultat per mostrar a la UI.
     */
    public String tractarFerAnalisi(String kStr) throws IOException {
        return ctrlDomini.tractarAnalisi(kStr);
    }

    public String tractarFerAnalisiRespostes(String filePath) throws IOException {
        return ctrlDomini.tractarFerAnalisiRespostes(filePath);
    }

    public void tractarEnviarResposta(String serialized) {
        if (serialized == null) return;
        String[] parts = serialized.split("\\|\\|");
        Object[] arr = new Object[parts.length];
        for (int i=0;i<parts.length;++i) {
            String token = parts[i];
            if (token.equals("NULL")) { arr[i] = null; continue; }
            if (token.startsWith("NUM:")) {
                String sub = token.substring(4);
                try { arr[i] = Double.parseDouble(sub); } catch (Exception e) { arr[i] = null; }
                continue;
            }
            if (token.startsWith("IDX:")) {
                String sub = token.substring(4);
                String[] ss = sub.split(",");
                try {
                    Object[] pair = new Object[2];
                    pair[0] = Integer.parseInt(ss[0]);
                    pair[1] = Integer.parseInt(ss[1]);
                    arr[i] = pair;
                } catch (Exception e) { arr[i] = null; }
                continue;
            }
            if (token.startsWith("SET:")) {
                String sub = token.substring(4);
                Set<String> set = new HashSet<>();
                if (!sub.isEmpty()) {
                    String[] items = sub.split(";;;;");
                    for (String it : items) set.add(it);
                }
                arr[i] = set;
                continue;
            }
            if (token.startsWith("STR:")) {
                arr[i] = token.substring(4);
                continue;
            }

            arr[i] = token;
        }

        Answer a = new Answer();
        a.setAnswer(arr);
        ctrlDomini.afegirResposta(a);
        JOptionPane.showMessageDialog(vistaPrincipal, "Gràcies per respondre l'enquesta!");
        anarAMenu();
    }

    public void tractarEliminarEnquesta(String title) {
        ctrlDomini.eliminarEnquesta(title);
        JOptionPane.showMessageDialog(vistaPrincipal, "Enquesta esborrada correctament.");
        anarAGestionarEnquesta();
    }

    public void eliminarPregunta(String indexStr) {
        int index;
        try { index = Integer.parseInt(indexStr); } catch (Exception e) { return; }

        if (ctrlDomini.getCurrentSurveyTitle() == null) return;
        int totalBefore = ctrlDomini.getNumPreguntesActual();
        if (index < 0 || index >= totalBefore) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Índex de pregunta invàlid.");
            return;
        }

        // Eliminar la pregunta
        ctrlDomini.eliminarPregunta(index);

        int totalAfter = ctrlDomini.getNumPreguntesActual();

        if (totalAfter == 0) {
            // If no questions left, add a default one and stay editing it (index 0)
            ctrlDomini.afegirPreguntaBuidaAlFinal("Opció Única Ordenada");
            JOptionPane.showMessageDialog(vistaPrincipal, "Pregunta eliminada. S'ha creat una pregunta per defecte.");
            navegarPregunta("0");
            return;
        }

        // Otherwise, decide which question to show next:
        // - If we deleted the last question, show the new last (index-1)
        // - Else show the question that now occupies the same index
        int nextIndex = index;
        if (index >= totalAfter) nextIndex = totalAfter - 1;

        JOptionPane.showMessageDialog(vistaPrincipal, "Pregunta eliminada correctament.");
        navegarPregunta(String.valueOf(nextIndex));
    }

    public void tractarAfegirPreguntaDesDeModificar() {
        String[] opcions = {"Opció Única Ordenada", "Opció Única Desordenada", "Opció Múltiple", "Text Lliure", "Numèrica"};

        String tipusSeleccionat = (String) JOptionPane.showInputDialog(
                vistaPrincipal,
                "Quin tipus de pregunta vols afegir?",
                "Nova Pregunta",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcions,
                opcions[0]
        );

        if (tipusSeleccionat == null) return; 

        ctrlDomini.afegirPreguntaBuidaAlFinal(tipusSeleccionat);

        int total = ctrlDomini.getNumPreguntesActual();
        if (total > 0) navegarPregunta(String.valueOf(total - 1));
    }
    
    public List<String> getSurveyTitles() {
        return ctrlDomini.getSurveyTitles();
    }

    public List<String> getSurveysFullInfo() {
        return ctrlDomini.getSurveysFullInfo();
    }

    public String getCurrentSurveyTitle() {
        return ctrlDomini.getCurrentSurveyTitle();
    }

    public List<String> getCurrentSurveyQuestionsData() {
        List<String> data = new ArrayList<>();
        int num = ctrlDomini.getNumPreguntesActual();
        for (int i = 0; i < num; i++) {
            String qData = ctrlDomini.getPreguntaData(i);
            if (qData != null) data.add(qData);
        }
        return data;
    }

    public void importarEnquesta(String filePath, String title, String description) throws IOException {
        ctrlDomini.importSurvey(filePath, title, description);
    }

    public String analitzarRespostes(String filePath) throws IOException {
        return ctrlDomini.analyzeAnswers(filePath);
    }
}
