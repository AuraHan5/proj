import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe abstracta que representa una pregunta genèrica d'una enquesta.
 * Pot ser de tipus:
 *  1 = Resposta única (SingleChoiceQuestion)
 *  2 = Resposta múltiple (MultipleChoiceQuestion)
 *  3 = Text lliure (FreeTextQuestion)
 *  4 = Número lliure (FreeNumberQuestion)
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
            return (resposta instanceof Integer) || (resposta instanceof Double);
        }

        @Override
        public void ModifyQuestion() {
            System.out.println("Current statement: " + enunciat);
            System.out.print("Do you want to change the statement? (y/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.print("Next statement: ");
                enunciat = sc.nextLine();
            }
        }
    }

    public static class SingleChoiceQuestionOrdered extends Question {
        public List<String> opcions;

        public SingleChoiceQuestionOrdered(String enunciat, List<String> opcions) {
            super(enunciat, 1);
            this.opcions = opcions;
        }

        @Override 
        public boolean validarResposta(Object resposta) {
            if (!(resposta instanceof Integer)) return false;
            int idx = (Integer) resposta;
            return idx >= 0 && idx < opcions.size();
        }

        @Override /* Del toString de la classe Question */ 
        public String toString() {
            return super.toString() + " Options: " + opcions;
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
        }
    }

    public static class SingleChoiceQuestionUnordered extends Question {
        public List<String> opcions;

        public SingleChoiceQuestionUnordered(String enunciat, List<String> opcions) {
            super(enunciat, 2);
            this.opcions = opcions;
        }

        @Override 
        public boolean validarResposta(Object resposta) {
            Boolean found = false;
            for (int i = 0; i < opcions.size() && found == false; ++i){
                if (opcions.get(i).equals(resposta)) found = true;
            }
            return found;
        }

        @Override /* Del toString de la classe Question */ 
        public String toString() {
            return super.toString() + " Options: " + opcions;
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
        }
    }

    

    /**
     * Pregunta de resposta múltiple (varis int identificadors).
     */
    public static class MultipleChoiceQuestion extends Question {
        public List<String> opcions;
        public int maxRespostes; // pot ser -1 si no hi ha màxim

        public MultipleChoiceQuestion(String enunciat, List<String> opcions, int maxRespostes) {
            super(enunciat, 3);
            this.opcions = opcions;
            this.maxRespostes = maxRespostes;
        }

        @Override
        public boolean validarResposta(Object resposta) {
            if (!(resposta instanceof int[])) return false;
            int[] idxs = (int[]) resposta;

            if (maxRespostes > 0 && idxs.length > maxRespostes) return false;

            for (int idx : idxs) {
                if (idx < 0 || idx >= opcions.size()) return false;
            }
            return true;
        }

        @Override // Del toString de la classe Question
        public String toString() {
            String infoMax = (maxRespostes > 0) ? " (maximum " + maxRespostes + ")" : "";
            return super.toString() + " Options: " + opcions + infoMax;
        }

        @Override
        public void ModifyQuestion() {
            Scanner sc = new Scanner(System.in);

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
                return text.length() <= 100; // màxim 100 caràcters
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

    public static Question surveyQuestion(int tipus) {
        switch (tipus) {
            case 0: {
                return new FreeNumberQuestion(enunciat);
            }
            case 1: {
                String[] arr = sc.nextLine().split(";");
                List<String> opcions = new ArrayList<>();
                for (String s : arr) opcions.add(s.trim());
                return new SingleChoiceQuestionOrdered(enunciat, opcions);
            }
            case 2: {
                String[] arr = sc.nextLine().split(";");
                List<String> opcions = new ArrayList<>();
                for (String s : arr) opcions.add(s.trim());
                return new SingleChoiceQuestionUnordered(enunciat, opcions);
            }
            case 3: {
                String[] arr = sc.nextLine().split(";");
                List<String> opcions = new ArrayList<>();
                for (String s : arr) opcions.add(s.trim());
                int max = Integer.parseInt(sc.nextLine().trim());
                return new MultipleChoiceQuestion(enunciat, opcions, max);
            }
            case 4: {
                return new FreeTextQuestion(enunciat);
            }
            default: {
                return null;
            }
        }

    public static Question createQuestion() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Create a new question:");
        System.out.println("0  Free number (int or double)");
        System.out.println("1  Ordered single choice");
        System.out.println("2  Unordered single choice");
        System.out.println("3  Multiple choice");
        System.out.println("4  Free answer (text)");
        
        System.out.print("Select the type (0-4): ");
        int tipus = Integer.parseInt(sc.nextLine().trim());

        // POSAR CAS D'ENTRADA INCORRECTA: ES RETORNA ERROR I NO ES CREA. 

        System.out.print("Enter the statement of the question: ");
        String enunciat = sc.nextLine();

        switch (tipus) {
            case 0: {
                return surveyQuestion(tipus);
            }
            case 1: {
                System.out.println("Enter the options (separated by ;): ");
                return surveyQuestion(tipus);
            }
            case 2: {
                System.out.println("Enter the options (separated by ;): ");
                return surveyQuestion(tipus);
            }
            case 3: {
                System.out.println("Enter the options (separated by ;): ");
                System.out.print("Enter the new maximum (-1 if there's no limit): ");
                return surveyQuestion(tipus);
            }
            case 4: {
                return surveyQuestion(tipus);
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
