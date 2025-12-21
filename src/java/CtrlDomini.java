import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;

public class CtrlDomini {

    // Llista de totes les enquestes del sistema
    private List<Survey> surveyList;

    // Referència a l'enquesta que s'està creant/modificant actualment
    private Survey currentSurvey;

    public CtrlDomini() {
        this.surveyList = new ArrayList<>();
    }

    // Inicialització amb dades 
    public void initializeCtrlDomini(Survey[] surveys) {
        surveyList = new ArrayList<>();
        if (surveys != null) {
            for (Survey survey : surveys) {
                surveyList.add(survey);
            }
        }
    }

    // --- LÒGICA DE DOMINI ---

    public void SurveyCreation(String title, String desc) {
        int id = surveyList.size() + 1; 
        Survey s = new Survey(id, desc, title);
        surveyList.add(s);
        this.currentSurvey = s;
        System.out.println("Domini: Enquesta creada i activa -> " + title);
    }

    // Afegeix pregunta de text o numèrica (sense llista opcions)
    public void afegirPreguntaSimple(String enunciat, String tipus) {
        if (currentSurvey == null) return;
        
        Question q = crearInstanciaPregunta(enunciat, tipus, null, -1);
        
        if (q != null) {
            currentSurvey.getQuestions().add(q);
            System.out.println("Domini: Pregunta simple afegida.");
        }
    }

    // Afegeix pregunta amb opcions
    public void afegirPreguntaAmbOpcions(String enunciat, String tipus, List<String> opcions, int maxRespostes) {
        if (currentSurvey == null) return;

        Question q = crearInstanciaPregunta(enunciat, tipus, opcions, maxRespostes);

        if (q != null) {
            currentSurvey.getQuestions().add(q);
            System.out.println("Domini: Pregunta amb opcions afegida.");
        }
    }

    /**
     * NOVA FUNCIÓ PRINCIPAL PER MODIFICAR.
     * Substitueix l'objecte Question antic per un de nou, permetent canviar de tipus.
     */
    public void modificarPregunta(int index, String enunciat, String tipus, List<String> opcions, int maxRespostes) {
        if (currentSurvey == null) return;
        if (index < 0 || index >= currentSurvey.getQuestions().size()) return;

        // Creem la nova versió de la pregunta (pot haver canviat de tipus)
        Question novaPregunta = crearInstanciaPregunta(enunciat, tipus, opcions, maxRespostes);

        if (novaPregunta != null) {
            // Substituïm l'antiga per la nova a la llista
            currentSurvey.getQuestions().set(index, novaPregunta);
            System.out.println("Domini: Pregunta " + index + " modificada a tipus " + tipus);
        } else {
            System.err.println("Error: No s'ha pogut modificar la pregunta (Tipus desconegut: " + tipus + ")");
        }
    }

    /**
     * Afegeix una resposta a l'enquesta actual.
     */
    public void afegirResposta(Answer a) {
        if (currentSurvey == null) return;
        if (a == null) return;
        currentSurvey.getAnswers().add(a);
        System.out.println("Domini: Resposta afegida a l'enquesta '" + currentSurvey.getTitle() + "'.");
    }

    /**
     * Mètode auxiliar per no repetir codi als switchs.
     * Crea l'objecte Question adequat segons l'String del tipus.
     */
    private Question crearInstanciaPregunta(String enunciat, String tipus, List<String> opcions, int maxRespostes) {
        Question q = null;

        // Assegurem que opcions no sigui null per evitar errors als constructors
        if (opcions == null) opcions = new ArrayList<>();

        switch (tipus) {
            case "Opció Única Ordenada":
                q = new Question.SingleChoiceQuestionOrdered(enunciat, opcions);
                break;

            case "Opció Única Desordenada":
                q = new Question.SingleChoiceQuestionUnordered(enunciat, opcions);
                break;

            case "Opció Múltiple":
                // Si no tenim límit definit, posem -1 o la mida de la llista
                q = new Question.MultipleChoiceQuestion(enunciat, opcions, maxRespostes);
                break;

            case "Numèrica":
                q = new Question.FreeNumberQuestion(enunciat);
                break;

            case "Text Lliure":
            default:
                // Per defecte o si és Text Lliure
                q = new Question.FreeTextQuestion(enunciat);
                break;
        }
        return q;
    }

    // --- GETTERS I UTILITATS ---

    public List<Survey> getSurveyList() {
        return surveyList;
    }

    public Survey getCurrentSurvey() {
        return currentSurvey;
    }

    public List<String> getSurveyIds() {
        List<String> ids = new ArrayList<>();
        for (Survey s : surveyList) {
            ids.add(String.valueOf(s.getId()));
        }
        return ids;
    }

    public List<String> getSurveyTitles() {
        List<String> titles = new ArrayList<>();
        for (Survey s : surveyList) {
            titles.add(s.getTitle());
        }
        return titles;
    }

    public Survey getSurvey(String id) {
        int idInt = Integer.parseInt(id);
        for (Survey s : surveyList) {
            if (s.getId() == idInt) return s;
        }
        return null;
    }

    public int getNumPreguntesActual() {
        if (currentSurvey == null || currentSurvey.getQuestions() == null) return 0;
        return currentSurvey.getQuestions().size();
    }

    public String getPreguntaData(int index) {
        if (currentSurvey == null) return null;
        if (index < 0 || index >= currentSurvey.getQuestions().size()) return null;
        Question q = currentSurvey.getQuestions().get(index);
        return serializeQuestion(q);
    }

    private String serializeQuestion(Question q) {
        StringBuilder sb = new StringBuilder();
        sb.append(q.getEnunciat()).append("||");
        String type = getQuestionType(q);
        sb.append(type).append("||");
        if (q.getOpcions() != null && !q.getOpcions().isEmpty()) {
            sb.append(String.join(";;", q.getOpcions()));
        }
        sb.append("||");
        if (q instanceof Question.MultipleChoiceQuestion) {
            sb.append(((Question.MultipleChoiceQuestion) q).getMaxRespostes());
        } else {
            sb.append("-1");
        }
        return sb.toString();
    }

    private String getQuestionType(Question q) {
        if (q instanceof Question.SingleChoiceQuestionOrdered) return "Opció Única Ordenada";
        if (q instanceof Question.SingleChoiceQuestionUnordered) return "Opció Única Desordenada";
        if (q instanceof Question.MultipleChoiceQuestion) return "Opció Múltiple";
        if (q instanceof Question.FreeTextQuestion) return "Text Lliure";
        if (q instanceof Question.FreeNumberQuestion) return "Numèrica";
        return "Text Lliure";
    }

    public void setCurrentSurveyByTitle(String title) {
        for (Survey s : surveyList) {
            if (s.getTitle().equals(title)) {
                this.currentSurvey = s;
                break;
            }
        }
    }

    public void setCurrentSurveyById(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            for (Survey s : surveyList) {
                if (s.getId() == id) {
                    this.currentSurvey = s;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            // ignore
        }
    }

    public String getCurrentSurveyTitle() {
        return currentSurvey != null ? currentSurvey.getTitle() : null;
    }

    public String getCurrentSurveyDescription() {
        return currentSurvey != null ? currentSurvey.getDescription() : null;
    }

    public String tractarAnalisi(String kStr) throws IOException {
        if (currentSurvey == null) return "No hi ha cap enquesta seleccionada.";
        int k;
        try { k = Integer.parseInt(kStr); } catch (Exception e) { return "Paràmetre k invàlid."; }
        return currentSurvey.SurveyAnalysis(k);
    }

    /**
     * Elimina una enquesta del sistema.
     */
    public void eliminarEnquesta(String title) {
        Survey toRemove = null;
        for (Survey s : surveyList) {
            if (s.getTitle().equals(title)) {
                toRemove = s;
                break;
            }
        }
        if (toRemove != null) {
            surveyList.remove(toRemove);
            if (currentSurvey == toRemove) currentSurvey = null;
            System.out.println("Domini: Enquesta '" + title + "' eliminada correctament.");
        } else {
            System.err.println("Domini: Error en eliminar l'enquesta (no trobada).");
        }
    }

    public void eliminarPregunta(int index) {
        if (currentSurvey == null) return;
        if (index < 0 || index >= currentSurvey.getQuestions().size()) return;
        currentSurvey.getQuestions().remove(index);
        System.out.println("Domini: Pregunta " + index + " eliminada.");
    }

    public void afegirPreguntaBuidaAlFinal(String tipus) {
        if (currentSurvey == null) return;
        
        String enunciat = "Nova Pregunta";
        List<String> opcions = new ArrayList<>();
        opcions.add("Opció A");
        opcions.add("Opció B");
        
        // Cridem al mètode centralitzat que ja coneix els tipus
        afegirPreguntaAmbOpcions(enunciat, tipus, opcions, -1);
    }

    public List<String> getCurrentSurveyQuestionsData() {
        List<String> result = new ArrayList<>();
        int num = getNumPreguntesActual();
        for (int i = 0; i < num; i++) {
            result.add(getPreguntaData(i));
        }
        return result;
    }

    public String tractarFerAnalisi(String kStr) throws IOException {
        int k;
        try { k = Integer.parseInt(kStr); } catch (Exception e) { return "Paràmetre k invàlid."; }
        Survey s = getCurrentSurvey();
        if (s == null) return "No hi ha cap enquesta seleccionada.";
        String result = s.SurveyAnalysis(k);
        return result;
    }

    public String tractarFerAnalisiRespostes(String filePath, String kStr) throws IOException {
        int k;
        try { k = Integer.parseInt(kStr); } catch (Exception e) { return "Paràmetre k invàlid."; }
        ReadAnswers ra = new ReadAnswers(new File(filePath));
        Object[][] data = ra.readFile();
        int numCols = data[0].length;
        int[] types = new int[numCols]; // assume all numeric
        ra.initializeVariableType(types);
        Object[][] processed = ra.processData(data);
        
        // Perform KMeans
        KMeans km = new KMeans(k, 100);
        int[] assignments = km.assignToCluster(processed, types);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Anàlisi K-Means de respostes importades (k=").append(k).append("):\n");
        sb.append("Centres dels clusters:\n");
        for (int i = 0; i < km.centroids.length; i++) {
            sb.append("Cluster ").append(i + 1).append(": ");
            Object[] centroid = km.centroids[i];
            for (int j = 0; j < centroid.length; j++) {
                sb.append(centroid[j].toString());
                if (j < centroid.length - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        sb.append("\nAssignacions:\n");
        for (int i = 0; i < assignments.length; i++) {
            sb.append("Resposta ").append(i + 1).append(" -> Cluster ").append(assignments[i] + 1).append("\n");
        }
        return sb.toString();
    }

    public void tractarEnviarResposta(Object[] arr) {
        Answer a = new Answer();
        a.setAnswer(arr);
        afegirResposta(a);
    }

    public void importSurvey(String filePath, String title, String description) throws IOException {
        ArrayList<Question> questions = ReadSurvey.createSurveyFromCSV(filePath);
        int id = surveyList.size() + 1;
        Survey s = new Survey(id, description, title);
        for (Question q : questions) {
            s.getQuestions().add(q);
        }
        surveyList.add(s);
        this.currentSurvey = s;
        System.out.println("Domini: Enquesta importada -> " + title);
    }

    public String analyzeAnswers(String filePath) throws IOException {
        ReadAnswers ra = new ReadAnswers(new File(filePath));
        
        List<String> questionsData = getCurrentSurveyQuestionsData();
        Object[][] data = ra.readFile();
        int numCols = data[0].length;
        
        int[] types;
        if (questionsData.isEmpty()) {
            // No survey selected, assume all columns are numeric
            types = new int[numCols];
            // all 0 (numeric)
        } else {
            types = new int[questionsData.size()];
            for (int i = 0; i < questionsData.size(); i++) {
                String[] parts = questionsData.get(i).split("\\|\\|");
                String tipus = parts[1];
                if ("Opció Única Ordenada".equals(tipus)) types[i] = 1;
                else if ("Opció Única Desordenada".equals(tipus)) types[i] = 2;
                else if ("Opció Múltiple".equals(tipus)) types[i] = 3;
                else if ("Text Lliure".equals(tipus)) types[i] = 4;
                else if ("Numèrica".equals(tipus)) types[i] = 0;
            }
        }
        
        ra.initializeVariableType(types);
        Object[][] processed = ra.processData(data);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Anàlisi de respostes:\n");
        sb.append("Nombre de respostes: ").append(processed.length).append("\n");
        
        for (int i = 0; i < types.length; i++) {
            sb.append("Pregunta ").append(i+1).append(": ");
            if (types[i] == 0) { // numeric
                double[] max = ra.getMaxValue();
                double[] min = ra.getMinValue();
                Double[][] iqr = ra.getIQR();
                sb.append("Max: ").append(max[i]).append(", Min: ").append(min[i]).append(", IQR: ").append(iqr[i][0]).append("-").append(iqr[i][1]).append("\n");
            } else {
                sb.append("Tipus no numèric\n");
            }
        }
        return sb.toString();
    }
}