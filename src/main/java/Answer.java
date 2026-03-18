import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Answer {
    private Object[] answer;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public Answer() {
        this.answer = new Object[0];
    }

    public void initialize(int[] type, ArrayList<Question> questions) throws IOException{
        answer = new Object[type.length];

        for (int i = 0; i < type.length; i++) {
            System.out.println("\n" + (i + 1) + ": " + questions.get(i).getEnunciat());

                switch (type[i]) {
                    case 0: // Free Number
                        answer[i] = readFreeNumber();
                        break;
                    case 1: // Single Choice Ordered
                        answer[i] = readSingleChoiceOrdered(questions.get(i));
                        break;
                    case 2: // Single Choice Unordered
                        answer[i] = readSingleChoiceUnordered(questions.get(i));
                        break;
                    case 3: // Multiple Choice
                        answer[i] = readMultipleChoice(questions.get(i));
                        break;
                    case 4: // Free Text
                        answer[i] = readFreeText();
                        break;
                }

        }
    }

    public Object[] readSingleChoiceOrdered(Question question) throws IOException {
        if (question instanceof Question.SingleChoiceQuestionOrdered) {
            Question.SingleChoiceQuestionOrdered scq = (Question.SingleChoiceQuestionOrdered) question;
            System.out.println("Options:");
            for (int j = 0; j < scq.opcions.size(); j++) {
                System.out.println("  " + j + ": " + scq.opcions.get(j));
            }
            System.out.print("Select an option (write index): ");
            Object[] result = new Object[2];
            result[0] = Integer.parseInt(reader.readLine().trim());
            result[1] = scq.opcions.size();
            return result;
        }
        return new Object[0];
    }

    public String readSingleChoiceUnordered(Question question) throws IOException {
        if (question instanceof Question.SingleChoiceQuestionUnordered) {
            Question.SingleChoiceQuestionUnordered scq = (Question.SingleChoiceQuestionUnordered) question;
            System.out.println("Options:");
            for (int j = 0; j < scq.opcions.size(); j++) {
                System.out.println("  " + scq.opcions.get(j));
            }
            System.out.print("Select an option (write the exact text): ");
            return reader.readLine().trim();
        }
        return "";
    }

    public Set<Object> readMultipleChoice(Question question) throws IOException {
        if (question instanceof Question.MultipleChoiceQuestion) {
            Question.MultipleChoiceQuestion mcq = (Question.MultipleChoiceQuestion) question;

            System.out.println("Options:");
            for (int j = 0; j < mcq.opcions.size(); j++) {
                System.out.println("  " + j + ": " + mcq.opcions.get(j));
            }

            System.out.print("Select the options (by comma, ex: 0,2,3): ");
            String[] choices = reader.readLine().split(",");

            Set<Object> result = new HashSet<>();  

            for (String choice : choices) {
                choice = choice.trim();
                if (!choice.isEmpty()) {
                    int index = Integer.parseInt(choice);

                    // Validació
                    if (index < 0 || index >= mcq.opcions.size()) {
                        System.out.println("Invalid option: " + index);
                    } else {
                        result.add(index);         
                    }
                }
            }

            return result;
        }

        return new HashSet<>();
    }


    public String readFreeText() throws IOException {
        System.out.print("Write your answer (text): ");
        return reader.readLine();
    }

    public Object readFreeNumber() throws IOException {
        System.out.print("Write your answer (number): ");
        String input = reader.readLine().trim();

        //parse as double
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e2) {
                System.out.println("Not a valid number");
                return null;
            }
    }

    public void setAnswer(Object[] answer) {
        this.answer = answer;
    }

    public Object[] getArray(){
        return this.answer;
    }

}