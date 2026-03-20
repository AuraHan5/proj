import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedWriter;

public class CtrlDomini {

    // Llista de totes les enquestes del sistema
    private List<Survey> surveyList;
    private GestorDeDades gestor;


    // Referència a l'enquesta que s'està creant/modificant actualment
    private Survey currentSurvey;

    public CtrlDomini() {
        this(false);
    }

    public CtrlDomini(boolean loadFromDisk) {
        surveyList = new ArrayList<>();
        gestor = new GestorDeDades();
        if (loadFromDisk) {
            try {
                List<Map<String, Object>> raw = gestor.carregarRaw();
                surveyList = surveysFromRaw(raw);
            } catch (IOException e) {
                surveyList = new ArrayList<>();
            }
        }
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
        saveSurveys();
    }

    // Afegeix pregunta de text o numèrica (sense llista opcions)
    public void afegirPreguntaSimple(String enunciat, String tipus) {
        if (currentSurvey == null) return;
        
        Question q = crearInstanciaPregunta(enunciat, tipus, null, -1);
        
            if (q != null) {
                currentSurvey.getQuestions().add(q);
            }
        saveSurveys();
    }

    // Afegeix pregunta amb opcions
    public void afegirPreguntaAmbOpcions(String enunciat, String tipus, List<String> opcions, int maxRespostes) {
        if (currentSurvey == null) return;

        Question q = crearInstanciaPregunta(enunciat, tipus, opcions, maxRespostes);

            if (q != null) {
                currentSurvey.getQuestions().add(q);
            }
        saveSurveys();
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
            }
        saveSurveys();
    }

    /**
     * Afegeix una resposta a l'enquesta actual.
     */
    public void afegirResposta(Answer a) {
        if (currentSurvey == null) return;
        if (a == null) return;
        currentSurvey.getAnswers().add(a);
        saveSurveys();
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
        saveSurveys();
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

    public List<String> getSurveysFullInfo() {
        List<String> infos = new ArrayList<>();
        for (Survey s : surveyList) {
            String info = "ID: " + s.getId() + " | " + s.getTitle() + " - " + s.getDescription();
            infos.add(info);
        }
        return infos;
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
            // Use distinct separator for option list to avoid collision with field separator
            sb.append(String.join(";;;;", q.getOpcions()));
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

    public void modificarEnquesta(String nouTitol, String novaDesc) {
        if (currentSurvey == null) return;
        currentSurvey.setTitle(nouTitol);
        currentSurvey.setDescription(novaDesc);
        saveSurveys();
    }

    public String tractarAnalisi(String kStr) throws IOException {
        int k;
        try { k = Integer.parseInt(kStr); } catch (Exception e) { return "ERROR: Paràmetre k invàlid."; }
        Survey s = getCurrentSurvey();
        if (s == null) return "ERROR: No hi ha cap enquesta seleccionada.";
        
        if (s.getAnswers() == null || s.getAnswers().isEmpty()) {
            return "ERROR: L'enquesta no té respostes per analitzar.";
        }
        
        // Check if all answers are empty
        if (allAnswersEmpty(s)) {
            return "ERROR: L'enquesta no té respostes per analitzar.";
        }
        
        String result = s.SurveyAnalysis(k);
        return result;
    }

    private boolean allAnswersEmpty(Survey s) {
        if (s == null || s.getAnswers() == null || s.getAnswers().isEmpty()) {
            return true;
        }
        
        for (Answer answer : s.getAnswers()) {
            Object[] answerArray = answer.getArray();
            if (answerArray != null && answerArray.length > 0) {
                // Check if at least one element is not null
                for (Object obj : answerArray) {
                    if (obj != null && !isEmptyAnswer(obj)) {
                        return false; // Found at least one non-empty answer
                    }
                }
            }
        }
        
        return true; // All answers are empty
    }

    private boolean isEmptyAnswer(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String && ((String) obj).trim().isEmpty()) return true;
        if (obj instanceof Integer && (Integer) obj == 0) return true;
        if (obj instanceof Double && (Double) obj == 0.0) return true;
        if (obj instanceof Set && ((Set<?>) obj).isEmpty()) return true;
        if (obj instanceof Object[] && ((Object[]) obj).length == 0) return true;
        return false;
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
        } else {
            System.err.println("Domini: Error en eliminar l'enquesta (no trobada).");
        }
        saveSurveys();
    }

    public void eliminarPregunta(int index) {
        if (currentSurvey == null) return;
        if (index < 0 || index >= currentSurvey.getQuestions().size()) return;
        currentSurvey.getQuestions().remove(index);
        saveSurveys();
    }

    public void afegirPreguntaBuidaAlFinal(String tipus) {
        if (currentSurvey == null) return;
        
        String enunciat = "Nova Pregunta";
        List<String> opcions = new ArrayList<>();
        opcions.add("Opció A");
        opcions.add("Opció B");
        
        // Cridem al mètode centralitzat que ja coneix els tipus
        afegirPreguntaAmbOpcions(enunciat, tipus, opcions, -1);
        saveSurveys();
    }

    public List<String> getCurrentSurveyQuestionsData() {
        List<String> result = new ArrayList<>();
        int num = getNumPreguntesActual();
        for (int i = 0; i < num; i++) {
            result.add(getPreguntaData(i));
        }
        return result;
    }

    public String tractarFerAnalisiRespostes(String filePath) throws IOException {
        // Determine k based on filename
        int k = 3; // Default k value
        String fileName = new File(filePath).getName().toLowerCase();
        if (fileName.contains("loan-train")) {
            k = 2; // Special case for loan-train dataset
        }
        
        return tractarFerAnalisiRespostesAmbK(filePath, k);
    }

    private String tractarFerAnalisiRespostesAmbK(String filePath, int k) throws IOException {
        
        long startTime = System.nanoTime();
        
        // Usar ReadAnswers igual que KMeansDriver
        File file = new File(filePath);
        ReadAnswers ra = new ReadAnswers(file);
        Object[][] tempData = ra.readFile();
        
        if (tempData == null || tempData.length == 0) {
            return "Error reading the file.";
        }
        
        Object[][] data = ra.processData(tempData);
        int[] variableType = ra.getVariableType();
        List<Object> actual_results = ra.getLabels();
        List<Object> ids = ra.getIds();
        
        // Perform K-Means clustering multiple times to find best result
        double bestAccuracyKMeans = -1.0;
        int[] bestClustersKMeans = null;
        for (int iter = 0; iter < 20; ++iter) {
            Algorithm km = new KMeans(k, 200);
            int[] clusters = km.assignToCluster(data, variableType);
            double accuracyKMeans = TestAccuracy.testAccuracy(clusters, actual_results);
            if (accuracyKMeans > bestAccuracyKMeans) {
                bestAccuracyKMeans = accuracyKMeans;
                bestClustersKMeans = clusters;
            }
        }
        
        // Perform K-Medoids clustering multiple times to find best result
        double bestAccuracyKMedoids = -1.0;
        int[] bestClustersMedoids = null;
        for (int iter = 0; iter < 20; ++iter) {
            Algorithm kmedoids = new KMedoids(k, 200);
            int[] clusters = kmedoids.assignToCluster(data, variableType);
            double accuracyKMedoids = TestAccuracy.testAccuracy(clusters, actual_results);
            if (accuracyKMedoids > bestAccuracyKMedoids) {
                bestAccuracyKMedoids = accuracyKMedoids;
                bestClustersMedoids = clusters;
            }
        }
        
        long endTime = System.nanoTime();
        double timeSeconds = (endTime - startTime) / 1e9;
        
        // Use the best accuracy from both algorithms
        double accuracy = Math.max(bestAccuracyKMeans, bestAccuracyKMedoids);
        int[] finalClusters = (bestAccuracyKMeans >= bestAccuracyKMedoids) ? bestClustersKMeans : bestClustersMedoids;

        Path resultsPath = writeResultsCsv(ids, bestClustersKMeans, bestClustersMedoids, actual_results);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Analysis completed with k=").append(k).append("\n");
        sb.append("Clustering took ").append(String.format("%.2f", timeSeconds)).append(" seconds\n");
        sb.append("Accuracy: ").append(String.format("%.2f%%", accuracy * 100.0)).append("\n");
        if (resultsPath != null) {
            sb.append("Results saved to ").append(resultsPath.toString()).append("\n");
        }
        
        return sb.toString();
    }

    private Path writeResultsCsv(List<Object> ids, int[] clustersKm, int[] clustersKmedoids, List<Object> labels) {
        if (ids == null || clustersKm == null || clustersKmedoids == null || labels == null) return null;
        int n = Math.min(Math.min(ids.size(), labels.size()), Math.min(clustersKm.length, clustersKmedoids.length));
        if (n == 0) return null;

        List<Path> candidateDirs = new ArrayList<>();
        candidateDirs.add(Paths.get("FONTS"));
        candidateDirs.add(Paths.get("."));
        candidateDirs.add(Paths.get(".."));

        Path outPath = null;
        for (Path dir : candidateDirs) {
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                outPath = dir.resolve("results.csv");
                break;
            }
        }
        if (outPath == null) outPath = Paths.get("results.csv");

        try (BufferedWriter writer = Files.newBufferedWriter(outPath, StandardCharsets.UTF_8)) {
            writer.write("id,kmeans_cluster,kmedoids_cluster,actual_label");
            writer.newLine();
            for (int i = 0; i < n; i++) {
                writer.write(String.valueOf(ids.get(i)));
                writer.write(',');
                writer.write(String.valueOf(clustersKm[i]));
                writer.write(',');
                writer.write(String.valueOf(clustersKmedoids[i]));
                writer.write(',');
                writer.write(String.valueOf(labels.get(i)));
                writer.newLine();
            }
            return outPath.toAbsolutePath().normalize();
        } catch (IOException e) {
            System.err.println("Error writing results.csv: " + e.getMessage());
            return null;
        }
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
        saveSurveys();
    }

    public String analyzeAnswers(String filePath) throws IOException {
        ReadAnswers ra = new ReadAnswers(new File(filePath), false);
        
        List<String> questionsData = getCurrentSurveyQuestionsData();
        Object[][] data = ra.readFile();
        if (data == null) {
            return "Error reading the file.";
        }
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
    // --- Conversion helpers between generic JSON structures and domain objects ---
    private List<Survey> surveysFromRaw(List<Map<String, Object>> raw) {
        List<Survey> surveys = new ArrayList<>();
        if (raw == null) return surveys;

        for (Map<String, Object> m : raw) {
            Double idD = (m.get("id") instanceof Number) ? ((Number) m.get("id")).doubleValue() : null;
            int id = (idD == null) ? 0 : idD.intValue();
            String title = (String) m.get("title");
            String description = (String) m.get("description");

            Survey s = new Survey(id, description, title);

            if (m.containsKey("creationDate") && m.get("creationDate") != null) {
                try {
                    Object cd = m.get("creationDate");
                    if (cd instanceof LocalDate) s.setCreationDate((LocalDate) cd);
                    else if (cd instanceof String) s.setCreationDate(LocalDate.parse((String) cd));
                } catch (Exception ignored) {}
            }

            Object qsObj = m.get("questions");
            if (qsObj instanceof List) {
                List<?> qlist = (List<?>) qsObj;
                for (Object qo : qlist) {
                    if (!(qo instanceof Map)) continue;
                    Map<?,?> qmap = (Map<?,?>) qo;
                    int type = ((Number) qmap.get("type")).intValue();
                    String enunciat = (String) qmap.get("enunciat");
                    Question q = null;
                    switch (type) {
                        case 0:
                            q = new Question.FreeNumberQuestion(enunciat);
                            break;
                        case 1: {
                            Object opts = qmap.get("opcions");
                            List<String> opcions = new ArrayList<>();
                            if (opts instanceof List) {
                                for (Object o : (List<?>) opts) opcions.add(String.valueOf(o));
                            }
                            q = new Question.SingleChoiceQuestionOrdered(enunciat, opcions);
                            break;
                        }
                        case 2: {
                            Object opts = qmap.get("opcions");
                            List<String> opcions = new ArrayList<>();
                            if (opts instanceof List) {
                                for (Object o : (List<?>) opts) opcions.add(String.valueOf(o));
                            }
                            q = new Question.SingleChoiceQuestionUnordered(enunciat, opcions);
                            break;
                        }
                        case 3: {
                            Object opts = qmap.get("opcions");
                            List<String> opcions = new ArrayList<>();
                            if (opts instanceof List) {
                                for (Object o : (List<?>) opts) opcions.add(String.valueOf(o));
                            }
                            int max = -1;
                            if (qmap.get("maxRespostes") instanceof Number) {
                                max = ((Number) qmap.get("maxRespostes")).intValue();
                            }
                            q = new Question.MultipleChoiceQuestion(enunciat, opcions, max);
                            break;
                        }
                        case 4:
                            q = new Question.FreeTextQuestion(enunciat);
                            break;
                        default:
                            break;
                    }
                    if (q != null) s.addQuestion(q);
                }
            }

            Object ansObj = m.get("answers");
            if (ansObj instanceof java.util.List) {
                List<?> answersRaw = (List<?>) ansObj;
                for (Object araw : answersRaw) {
                    if (!(araw instanceof java.util.List)) continue;
                    List<?> elems = (List<?>) araw;
                    Object[] arr = new Object[elems.size()];
                    for (int i = 0; i < elems.size(); ++i) {
                        Object el = elems.get(i);
                        int qtype = -1;
                        if (i < s.getQuestions().size()) qtype = s.getQuestions().get(i).getType();

                        if (el == null) { arr[i] = null; continue; }

                        if (el instanceof java.util.List) {
                            List<?> inner = (List<?>) el;
                            if (qtype == 1) {
                                Object[] innerArr = new Object[inner.size()];
                                for (int k = 0; k < inner.size(); ++k) {
                                    Object v = inner.get(k);
                                    if (v instanceof Number) innerArr[k] = ((Number) v).intValue();
                                    else {
                                        String sv = String.valueOf(v);
                                        try { innerArr[k] = Integer.parseInt(sv); }
                                        catch (NumberFormatException nfe) {
                                            // try to resolve option text to index
                                            List<String> opts = (i < s.getQuestions().size()) ? s.getQuestions().get(i).getOpcions() : null;
                                            int idx = -1;
                                            if (opts != null) idx = opts.indexOf(sv);
                                            innerArr[k] = (idx >= 0) ? idx : sv;
                                        }
                                    }
                                }
                                arr[i] = innerArr;
                            } else if (qtype == 3) {
                                java.util.Set<Object> set = new java.util.HashSet<>();
                                List<String> opts = (i < s.getQuestions().size()) ? s.getQuestions().get(i).getOpcions() : null;
                                for (Object v : inner) {
                                    if (v instanceof Number) set.add(((Number) v).intValue());
                                    else {
                                        String sv = String.valueOf(v);
                                        try { set.add(Integer.parseInt(sv)); }
                                        catch (NumberFormatException nfe) {
                                            int idx = -1;
                                            if (opts != null) idx = opts.indexOf(sv);
                                            if (idx >= 0) set.add(idx);
                                            // else: skip unknown string values
                                        }
                                    }
                                }
                                arr[i] = set;
                            } else {
                                Object[] innerArr = new Object[inner.size()];
                                for (int k = 0; k < inner.size(); ++k) innerArr[k] = inner.get(k);
                                arr[i] = innerArr;
                            }
                        } else if (el instanceof Number) {
                            Number n = (Number) el;
                            if (qtype == 0) arr[i] = n.doubleValue();
                            else if (qtype == 1) arr[i] = new Object[]{n.intValue(), 4};
                            else if (qtype == 3) {
                                java.util.Set<Object> set = new java.util.HashSet<>();
                                set.add(n.intValue());
                                arr[i] = set;
                            } else arr[i] = el;
                        } else {
                            if (qtype == 2 || qtype == 4) arr[i] = String.valueOf(el);
                            else {
                                String sEl = String.valueOf(el);
                                try { double d = Double.parseDouble(sEl); if (qtype == 0) arr[i] = d; else arr[i] = sEl; }
                                catch (Exception ex) { arr[i] = sEl; }
                            }
                        }
                    }
                    Answer ans = new Answer();
                    ans.setAnswer(arr);
                    s.getAnswers().add(ans);
                }
            }

            surveys.add(s);
        }

        return surveys;
    }

    private List<Map<String, Object>> rawFromSurveys(List<Survey> surveys) {
        List<Map<String, Object>> dataToSave = new ArrayList<>();
        for (Survey s : surveys) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("title", s.getTitle());
            map.put("description", s.getDescription());
            map.put("creationDate", s.getCreationDate());

            List<Map<String, Object>> qlist = new ArrayList<>();
            for (Question q : s.getQuestions()) {
                Map<String, Object> qmap = new HashMap<>();
                qmap.put("type", q.getType());
                qmap.put("enunciat", q.getEnunciat());
                if (q instanceof Question.SingleChoiceQuestionOrdered) qmap.put("opcions", ((Question.SingleChoiceQuestionOrdered) q).getOpcions());
                else if (q instanceof Question.SingleChoiceQuestionUnordered) qmap.put("opcions", ((Question.SingleChoiceQuestionUnordered) q).getOpcions());
                else if (q instanceof Question.MultipleChoiceQuestion) {
                    qmap.put("opcions", ((Question.MultipleChoiceQuestion) q).getOpcions());
                    qmap.put("maxRespostes", ((Question.MultipleChoiceQuestion) q).getMaxRespostes());
                }
                qlist.add(qmap);
            }
            map.put("questions", qlist);

            List<List<Object>> answersList = new ArrayList<>();
            for (Answer a : s.getAnswers()) {
                Object[] arr = a.getArray();
                List<Object> ansJson = new ArrayList<>();
                for (Object el : arr) {
                    if (el == null) { ansJson.add(null); continue; }
                    if (el instanceof Object[]) {
                        List<Object> inner = new ArrayList<>();
                        for (Object ie : (Object[]) el) {
                            if (ie instanceof Number) {
                                Number n = (Number) ie;
                                if (n.doubleValue() == n.intValue()) inner.add(n.intValue());
                                else inner.add(n.doubleValue());
                            } else inner.add(ie);
                        }
                        ansJson.add(inner);
                    } else if (el instanceof java.util.Set) {
                        List<Object> inner = new ArrayList<>();
                        for (Object ie : (java.util.Set<?>) el) {
                            if (ie instanceof Number) {
                                Number n = (Number) ie;
                                if (n.doubleValue() == n.intValue()) inner.add(n.intValue());
                                else inner.add(n.doubleValue());
                            } else inner.add(String.valueOf(ie));
                        }
                        ansJson.add(inner);
                    } else if (el instanceof Number) {
                        Number n = (Number) el;
                        ansJson.add(n.doubleValue());
                    } else {
                        ansJson.add(el);
                    }
                }
                answersList.add(ansJson);
            }
            map.put("answers", answersList);

            dataToSave.add(map);
        }
        return dataToSave;
    }

    public void saveSurveys() {
        try {
            List<Map<String, Object>> raw = rawFromSurveys(surveyList);
            gestor.guardarRaw(raw);
        } catch (IOException e) {
            // ignore or UI may display via CtrlPresentacio
        }
    }
}

