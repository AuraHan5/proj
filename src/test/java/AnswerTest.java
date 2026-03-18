import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


public class AnswerTest {

    private Object[] answerArray;

    /**
     * Comprova que el constructor inicialitza un array buit.
     */
    public AnswerTest() {
        this.answerArray = new Object[0];
    }

    /**
     * Comprova que es pot assignar una resposta.
     */
    @Test
    public void setAnswer() {
        Object[] arr = {1, 2, 3};     // valor de prova
        this.answerArray = arr;
        assertArrayEquals(arr, this.answerArray);
    }


    /**
     * Comprova que es retorna correctament larray de respostes.
     */
    @Test
    public Object[] getArray() {
        return this.answerArray;
    }

    /**
     * Comprova que es llegeix una linia del sistema sense espais.
     */
    @Test
    private String read() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null) return null;
        return line.trim();
    }

    /**
     * Comprova que es llegeix un nombre valid i es converteix a double.
     * Retorna null si lentrada no es valida.
     */
    @Test
    public Object readFreeNumber() throws IOException {
        String line = read();
        if (line == null) return null;

        try {
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            System.out.println("Not a valid number");
            return null;
        }
    }

    /**
     * Comprova que es retorna el text escrit per l usuari.
     */
    @Test
    public Object readFreeText() throws IOException {
        String line = read();
        if (line == null) return null;
        return line;
    }

    /**
     * Comprova que es llegeix un index numeric i es retorna junt amb el nombre dopcions.
     */
    @Test
    public Object[] readSingleChoiceOrdered(Question.SingleChoiceQuestionOrdered q) throws IOException {
        String line = read();
        if (line == null) return null;

        int idx = Integer.parseInt(line);
        return new Object[]{ idx, q.opcions.size() };
    }

    /**
     * Comprova que es valida una opcio existent i es retorna el seu valor.
     * Retorna null si la opcio no existeix.
     */
    @Test
    public Object readSingleChoiceUnordered(Question.SingleChoiceQuestionUnordered q) throws IOException {
        String line = read();
        if (line == null) return null;

        if (!q.opcions.contains(line)) {
            System.out.println("Invalid option");
            return null;
        }

        return line;
    }


    /**
     * Comprova que es llegeixen diversos indexos separats per comes.
     * Valors invalids es descarten i es notifiquen.
     */
    @Test
    public Set<Object> readMultipleChoice(Question.MultipleChoiceQuestion q) throws IOException {
        Set<Object> res = new HashSet<>();

        String line = read();
        if (line == null || line.isEmpty()) return res;

        String[] parts = line.split(",");

        for (String p : parts) {
            int idx = Integer.parseInt(p.trim());

            if (idx < 0 || idx >= q.opcions.size()) {
                System.out.println("Invalid option: " + idx);
            } else {
                res.add(idx);
            }
        }

        return res;
    }

}