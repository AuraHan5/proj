
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

public class Survey{
    //atributs
    private int id;
    private LocalDate CreationDate;
    private String description;
    private String title;
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Answer> answers = new ArrayList<>();
    private ArrayList<Analysis> analysis = new ArrayList<>();
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public Survey(){
        this.CreationDate = LocalDate.now();
    }

    //constructora
    public Survey(int id, String descripcio, String titol){
        this.id = id;
        this.CreationDate = LocalDate.now();
        this.description = descripcio;
        this.title = titol;
    }

    //Constructor from Vector<String>
    public Survey(Vector<String> data) {
        this();
        if (data != null && data.size() >= 3) {
            this.id = Integer.parseInt(data.get(0));
            this.title = data.get(1);
            this.description = data.get(2);
        }
    }

    //Convert to Vector<String>
    public Vector<String> toStringVector() {
        Vector<String> result = new Vector<>();
        result.add(String.valueOf(id));
        result.add(title);
        result.add(description);
        result.add(CreationDate.toString());
        return result;
    }

    @Override
    public String toString() {
        return id + "|" + title + "|" + description + "|" + CreationDate;
    }

    // Getters i setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getCreationDate() {
        return CreationDate;
    }
    
    public ArrayList<Question> getQuestions() {
        return questions;
    }
    
    public ArrayList<Answer> getAnswers() {
        return answers;
    }
    
    public ArrayList<Analysis> getAnalysis() {
        return analysis;
    }

    // Utility method to convert int array to Object array
    public Object[] intToObject(int[] array) {
        if (array == null) return null;
        Object[] result = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    //metodes

    public void addQuestions(ArrayList<Question> newQuestions) {
        this.questions.addAll(newQuestions);
    }

    private void CanviTitol() throws IOException{
        System.out.println("Insert the new title");
        String newTitle = reader.readLine();
        this.title = newTitle;
    }

    private void CanviDescr() throws IOException{
        System.out.println("Insert the new Description");
        String newDescription = reader.readLine();
        this.description = newDescription;
    }

    private void gestionarPreg() throws IOException{
        System.out.println("Now you can modify or erase the questions?\n");

            Iterator<Question> it = questions.iterator();
            while (it.hasNext()) {
                Question p = it.next();
                System.out.println(p.toString());
                System.out.print("Vols (m) modify, (d) delete o (n) next? \n");

                String opcio = reader.readLine();

                if(opcio.equals("m")){
                    p.ModifyQuestion();
                }
                else if(opcio.equals("d")){
                    it.remove();
                    System.out.println("Question erased.\n");
                }
                else{
                    System.out.println("Going to the next question.");
                }
            }
    }

    public void addQuestions() throws IOException{
        String a = "y";
        while(a.equals("y")){
            System.out.println("Do you want to add a new question? (y/n)");
            a = reader.readLine();
            if(!a.equals("y")) break;
            Question q = Question.createQuestion(); // ja retorna el tipus correcte
            if (q != null) questions.add(q);
        }
    }

    public void addQuestion(Question q){
        questions.add(q);
    }

    public void ModifySurvey() throws IOException{ // IMPORTANT dividir en diverses funcions privades

        //canvi titol enquesta
        System.out.println("Do you want to change the title? (y/n)");
        String t = reader.readLine();
        if(t.equals("y")){
           CanviTitol();
        }
        //canvi descripcio enquesta
        System.out.println("Do you want to change the description? (y/n)");
        String d = reader.readLine();
        if(d.equals("y")){
            CanviDescr();
        }

        //mirar preguntes associades
        if (questions.isEmpty()) {
            System.out.println("This survey doesn't have questions");
        }
        else{
            gestionarPreg();
        }

        addQuestions();
        System.out.println("\nModification of the survey completed.");
    }

    public String SurveyAnalysis(int k) throws IOException{
        Algorithm km = new KMeans(k, 1000);
        int nrows = answers.size();
        int ncols = questions.size();
        Object[][] data = new Object[nrows][ncols];
        int[] varType = new int[ncols];
        //inicialitza varType
        for(int i = 0; i<ncols; ++i){
            varType[i] = questions.get(i).getType();
        }
        //inicialitza data
        for(int i = 0; i<nrows; ++i){
            data[i] = (answers.get(i).getArray()); //no sempre serà int crec
        }
        ReadAnswers readanswers = new ReadAnswers();
        readanswers.initializeVariableType(varType);
        Object[][] data2 = readanswers.processData(data);
        double maxSilhouette = 0;
        int best_result[] = null; 
        for(int i = 0; i<100; ++i){
            int result[] = km.assignToCluster(data2, varType);
            double[] min = AuxiliarMethods.calculateMinVector(data2, varType);
            double[] max = AuxiliarMethods.calculateMaxVector(data2, varType);
            double q = Analysis.silhouette(result, data2, varType, min, max, k);
            if(i == 0 || q > maxSilhouette){
                maxSilhouette = q;
                best_result = result;
            }
        }
        StringBuilder result = new StringBuilder();
        result.append("Analysis completed with k=").append(k).append("\n");
        result.append("Best silhouette score: ").append(maxSilhouette).append("\n");
        result.append("Cluster assignments:\n");
        for(int i = 0; i<best_result.length; ++i){
            result.append("The answer {").append(i).append("} belongs to the cluster {").append(best_result[i]).append("}\n");
            System.out.println("The answer {" + i + "} belongs to the cluster {" + best_result[i] + "}");
        }
        return result.toString();
    }

    public void SurveyAnswer() throws IOException {
        Answer a = new Answer();
        int t[] = new int[questions.size()];
        for(int i = 0; i<questions.size(); ++i){
            t[i] = questions.get(i).getType();
        }
        a.initialize(t, questions);
        answers.add(a);
        
    }

    public void setCreationDate(LocalDate creationDate) {
        CreationDate = creationDate;
    }

    public static class SurveyData {
        public String title;
        public String description;

        public SurveyData(String title, String description) {
            this.title = title;
            this.description = description;
        }
        public SurveyData getSurveyData() {
            return new SurveyData(this.title, this.description);
        }
    }


}



