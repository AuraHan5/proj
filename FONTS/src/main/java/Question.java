
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// import java.util.Vector; // Removed unused import

/**
 * Classe abstracta que representa una pregunta genèrica d'una enquesta.
 * Pot ser de tipus:
 *  0 = Número lliure (FreeNumberQuestion)
 *  1 = Resposta única ordenada (SingleChoiceQuestionOrdered)
 *  2 = Resposta única desordenada (SingleChoiceQuestionUnordered)
 *  3 = Resposta múltiple (MultipleChoiceQuestion)
 *  4 = Text lliure (FreeTextQuestion)
 */
public abstract class Question {
    protected String enunciat;
    protected int tipus; 
    protected static final Scanner sc = new Scanner(System.in); 

    /**
     * Constructor comú a totes les preguntes.
     */
    public Question(String enunciat, int tipus) {
        this.enunciat = enunciat;
        this.tipus = tipus;
    }

    // Getters
    public String getEnunciat() {
        return enunciat;
    }

    public int getType() {
        return tipus; 
    }

    public void setEnunciat(String enunciat) {
        this.enunciat = enunciat;
    }

    /**
     * Cada tipus concret de pregunta ha de definir com validar una resposta.
     * @param resposta Objecte que representa la resposta de l’usuari.
     * @return true si la resposta és vàlida, false si no.
     */
    public abstract boolean validarResposta(Object resposta);

    /**
     * Retorna una representació de text per depuració o impressió.
     */
    @Override // ja que el fa del mètode de java
    public String toString() {
        return "Question: " + enunciat + " (type " + tipus + ")";
    }

    public abstract void ModifyQuestion();

    /**
     * Retorna les opcions de la pregunta. Per preguntes sense opcions, retorna llista buida.
     */
    public List<String> getOpcions() {
        return new ArrayList<>(); // Per defecte, llista buida
    }

    // ==============================
    // SUBCLASSES CONCRETES
    // ==============================

    /**
     * Pregunta de resposta única (un sol int identificador).
     */

    /**
     * Pregunta de número lliure (només valors enters o decimals).
     */
    public static class FreeNumberQuestion extends Question {

        public FreeNumberQuestion(String enunciat) {
            super(enunciat, 0);
        }

        @Override
        public boolean validarResposta(Object resposta) {
            if (resposta == null) return false;
            if (resposta instanceof Integer) return true;
            if (resposta instanceof Double) {
                Double d = (Double) resposta;
                return !d.isNaN() && !d.isInfinite();
            }
            return false;
        }

        @Override
        public void ModifyQuestion() {
            System.out.println("Current statement: " + getEnunciat());
            System.out.print("Do you want to change the statement? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("Next statement: ");
                setEnunciat(sc.nextLine());
            }
        }
    }

    public static class SingleChoiceQuestionOrdered extends Question {
        private List<String> opcions;

        public SingleChoiceQuestionOrdered(String enunciat, List<String> opcions) {
            super(enunciat, 1);
            this.opcions = opcions;
        }

        @Override 
        public boolean validarResposta(Object resposta) {
            if (!(resposta instanceof Integer)) return false;
            int idx = (Integer) resposta;
            return idx >= 0 && idx < getOpcions().size();
        }

        @Override /* Del toString de la classe Question */ 
        public String toString() {
            return super.toString() + " Options: " + getOpcions();
        }

        @Override
        public void ModifyQuestion() {
            System.out.println("Current options: " + getOpcions());
            System.out.print("Do you want to change the options? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Enter the new options separated by ; ");
                String[] arr = sc.nextLine().split(";");
                getOpcions().clear();
                for (String s : arr) getOpcions().add(s.trim());
            }
        }

        public List<String> getOpcions() {
            return opcions;
        }
    }

    public static class SingleChoiceQuestionUnordered extends Question {
        private List<String> opcions;

        public SingleChoiceQuestionUnordered(String enunciat, List<String> opcions) {
            super(enunciat, 2);
            this.opcions = opcions;
        }

        @Override 
        public boolean validarResposta(Object resposta) {
            Boolean found = false;
            for (int i = 0; i < getOpcions().size() && found == false; ++i){
                if (getOpcions().get(i).equals(resposta)) found = true;
            }
            return found;
        }

        @Override /* Del toString de la classe Question */ 
        public String toString() {
            return super.toString() + " Options: " + getOpcions();
        }

        @Override
        public void ModifyQuestion() {
            System.out.println("Current options: " + getOpcions());
            System.out.print("Do you want to change the options? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Enter the new options separated by ; ");
                String[] arr = sc.nextLine().split(";");
                getOpcions().clear();
                for (String s : arr) getOpcions().add(s.trim());
            }
        }

        public List<String> getOpcions() {
            return opcions;
        }
    }

    

    /**
     * Pregunta de resposta múltiple (varis int identificadors).
     */
    public static class MultipleChoiceQuestion extends Question {
        private List<String> opcions;
        private int maxRespostes; // pot ser -1 si no hi ha màxim

        public MultipleChoiceQuestion(String enunciat, List<String> opcions, int maxRespostes) {
            super(enunciat, 3);
            this.opcions = opcions;
            // Validar que maxRespostes sigui -1 o entre 2 i el nombre d'opcions
            if (maxRespostes != -1 && (maxRespostes < 2 || maxRespostes > opcions.size())) {
                throw new IllegalArgumentException("maxRespostes ha de ser -1 o entre 2 i " + opcions.size());
            }
            this.maxRespostes = maxRespostes;
        }

        @Override
        public boolean validarResposta(Object resposta) {
            if (!(resposta instanceof int[])) return false;
            int[] idxs = (int[]) resposta;

            if (getMaxRespostes() > 0 && idxs.length > getMaxRespostes()) return false;

            for (int idx : idxs) {
                if (idx < 0 || idx >= getOpcions().size()) return false;
            }
            return true;
        }

        @Override // Del toString de la classe Question
        public String toString() {
            String infoMax = (getMaxRespostes() > 0) ? " (maximum " + getMaxRespostes() + ")" : "";
            return super.toString() + " Options: " + getOpcions() + infoMax;
        }

        @Override
        public void ModifyQuestion() {
            System.out.println("Current options: " + opcions);
            System.out.print("Do you want to change the options? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Enter the new options separated by ; ");
                String[] arr = sc.nextLine().split(";");
                opcions.clear();
                for (String s : arr) opcions.add(s.trim());
            }

            System.out.println("Actual maximum: " + maxRespostes);
            System.out.print("Do you want to change the maximum number of answers? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("Enter the new maximum (-1 if there's no limit): ");
                maxRespostes = Integer.parseInt(sc.nextLine().trim());
            }
        }

        public List<String> getOpcions() {
            return opcions;
        }

        public int getMaxRespostes() {
            return maxRespostes;
        }

        public void setMaxRespostes(int maxRespostes) {
            this.maxRespostes = maxRespostes;
        }
    }

    /**
     * Pregunta de resposta lliure: text o número escrit per l'usuari.
     */
    public static class FreeTextQuestion extends Question {

        public FreeTextQuestion(String enunciat) {
            super(enunciat, 4);
        }

        @Override
        public boolean validarResposta(Object resposta) {
            if (resposta == null) return false;

            if (resposta instanceof String) {
                String text = (String) resposta;
                return text.length() <= 1000; // màxim 1000 caràcters
            } else if (resposta instanceof Integer) {
                return true; // també es permet número enter escrit
            }
            return false;
        }

        @Override
        public void ModifyQuestion() {
            System.out.println("Current statement: " + enunciat);
            System.out.print("Do you want to change the statement? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("New statement: ");
                enunciat = sc.nextLine();
            }
        }
    }

    

    // CREAR PREGUNTA

    public static Question createQuestion() {
        Scanner sc = Question.sc;

        System.out.println("Create a new question:");
        System.out.println("0  Free number (int or double)");
        System.out.println("1  Ordered single choice");
        System.out.println("2  Unordered single choice");
        System.out.println("3  Multiple choice");
        System.out.println("4  Free answer (text)");
        
        System.out.print("Select the type (0-4): ");
        int tipus = Integer.parseInt(sc.nextLine().trim());

        if (tipus < 0 || tipus > 4) {
            System.out.println("Error: Type must be between 0 and 4.");
            return null;
        }

        System.out.print("Enter the statement of the question: ");
        String enunciat = sc.nextLine();

        switch (tipus) {
            case 0: {
                return new FreeNumberQuestion(enunciat);
            }
            case 1: {
                System.out.println("Enter the options (separated by ;): ");
                String[] arr = sc.nextLine().split(";");
                List<String> opcions = new ArrayList<>();
                for (String s : arr) opcions.add(s.trim());
                return new SingleChoiceQuestionOrdered(enunciat, opcions);
            }
            case 2: {
                System.out.println("Enter the options (separated by ;): ");
                String[] arr = sc.nextLine().split(";");
                List<String> opcions = new ArrayList<>();
                for (String s : arr) opcions.add(s.trim());
                return new SingleChoiceQuestionUnordered(enunciat, opcions);
            }
            case 3: {
                System.out.println("Enter the options (separated by ;): ");
                String[] arr = sc.nextLine().split(";");
                List<String> opcions = new ArrayList<>();
                for (String s : arr) opcions.add(s.trim());
                System.out.print("Enter the new maximum (-1 if there's no limit): ");
                int max = Integer.parseInt(sc.nextLine().trim());
                return new MultipleChoiceQuestion(enunciat, opcions, max);
            }
            case 4: {
                return new FreeTextQuestion(enunciat);
            }
            default: {
                System.out.println("Non-valid type.");
                return null;
            }
        }
    }

    // ==============================
    // EXEMPLE D’ÚS
    // ==============================

    public static void main(String[] args) {
        Question q = createQuestion();
        if (q != null) {
            System.out.println("\n--- Question created correctly ---");
            System.out.println(q);
        } else {
            System.out.println("Error: A question has not been created.");
        }
    }
}
