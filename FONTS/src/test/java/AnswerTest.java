/* JUnit: AnswerTest — checks Answer getters/setters and initialization behaviors */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class AnswerTest {

    private Answer answer;

    @BeforeEach
    public void setUp() {
        answer = new Answer();
    }

    /**
     * Comprova que el constructor inicialitza un array buit.
     */
    @Test
    public void testConstructor() {
        Object[] result = answer.getArray();
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    /**
     * Comprova que es pot assignar una resposta.
     */
    @Test
    public void testSetAnswer() {
        Object[] arr = {1, 2, 3};
        answer.setAnswer(arr);
        assertArrayEquals(arr, answer.getArray());
    }

    /**
     * Comprova que es retorna correctament larray de respostes.
     */
    @Test
    public void testGetArray() {
        Object[] arr = {"test", 42, 3.14};
        answer.setAnswer(arr);
        Object[] result = answer.getArray();
        assertArrayEquals(arr, result);
    }

    /**
     * Comprova que setAnswer amb array null funciona correctament.
     */
    @Test
    public void testSetAnswerNull() {
        answer.setAnswer(null);
        assertNull(answer.getArray());
    }

    /**
     * Comprova que initialize funciona amb preguntes FreeNumber.
     */
    @Test
    public void testInitializeFreeNumber() throws IOException {
        // Simular entrada del usuario
        String input = "25\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question.FreeNumberQuestion("How old are you?"));

        int[] types = {0}; // FreeNumber

        Answer testAnswer = new Answer();
        testAnswer.initialize(types, questions);

        Object[] result = testAnswer.getArray();
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(25.0, (Double) result[0], 0.001);
    }

    /**
     * Comprova que initialize funciona amb preguntes FreeText.
     */
    @Test
    public void testInitializeFreeText() throws IOException {
        String input = "This is my answer\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question.FreeTextQuestion("What is your opinion?"));

        int[] types = {4}; // FreeText

        Answer testAnswer = new Answer();
        testAnswer.initialize(types, questions);

        Object[] result = testAnswer.getArray();
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("This is my answer", result[0]);
    }

    /**
     * Comprova que initialize funciona amb preguntes SingleChoiceOrdered.
     */
    @Test
    public void testInitializeSingleChoiceOrdered() throws IOException {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();
        options.add("Option A");
        options.add("Option B");
        questions.add(new Question.SingleChoiceQuestionOrdered("Choose:", options));

        int[] types = {1}; // SingleChoiceOrdered

        Answer testAnswer = new Answer();
        testAnswer.initialize(types, questions);

        Object[] result = testAnswer.getArray();
        assertNotNull(result);
        assertEquals(1, result.length);
        assertArrayEquals(new Object[]{1, 2}, (Object[]) result[0]);
    }

    /**
     * Comprova que initialize funciona amb preguntes SingleChoiceUnordered.
     */
    @Test
    public void testInitializeSingleChoiceUnordered() throws IOException {
        String input = "Option B\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<String> options = new ArrayList<>();
        options.add("Option A");
        options.add("Option B");
        questions.add(new Question.SingleChoiceQuestionUnordered("Choose:", options));

        int[] types = {2}; // SingleChoiceUnordered

        Answer testAnswer = new Answer();
        testAnswer.initialize(types, questions);

        Object[] result = testAnswer.getArray();
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("Option B", result[0]);
    }

    /**
     * Comprova que setAnswer funciona amb un array que conté nulls.
     */
    @Test
    public void testSetAnswerWithNulls() {
        Object[] arr = {"test", null, 42, null};
        answer.setAnswer(arr);
        Object[] result = answer.getArray();
        assertArrayEquals(arr, result);
    }

    /**
     * Comprova que setAnswer funciona amb un array buit.
     */
    @Test
    public void testSetAnswerEmpty() {
        Object[] arr = {};
        answer.setAnswer(arr);
        assertArrayEquals(arr, answer.getArray());
    }

    /**
     * Comprova que initialize funciona amb una llista de preguntes buida.
     */
    @Test
    public void testInitializeEmptyQuestions() throws IOException {
        ArrayList<Question> questions = new ArrayList<>();
        int[] types = {};

        Answer testAnswer = new Answer();
        testAnswer.initialize(types, questions);

        Object[] result = testAnswer.getArray();
        assertNotNull(result);
        assertEquals(0, result.length);
    }

}