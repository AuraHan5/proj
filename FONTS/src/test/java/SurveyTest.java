/* JUnit: SurveyTest — verifies core Survey operations and helpers */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SurveyTest {

    /**
     * Comprova que el constructor de Survey inicialitza tots els camps correctament:
     * id, descripcio, titol, data de creacio i llistes buides.
     */
    @Test
    void testConstructor() {
        Survey s = new Survey(10, "desc", "title");

        assertEquals(10, s.getId());
        assertEquals("desc", s.getDescription());
        assertEquals("title", s.getTitle());
        assertEquals(LocalDate.now(), s.getCreationDate());

        assertTrue(s.getQuestions().isEmpty());
        assertTrue(s.getAnswers().isEmpty());
        assertTrue(s.getAnalysis().isEmpty());
    }

    /**
     * Comprova que intToObject converteix correctament un array d enters en un array d Object
     * mantenint els mateixos valors.
     */
    @Test
    void testIntToObjectNormal() {
        Survey s = new Survey();
        int[] array = {1,2,3};
        Object[] result = s.intToObject(array);

        assertArrayEquals(new Object[]{1,2,3}, result);
    }

    /**
     * Comprova que intToObject funciona correctament amb un array d enters buit.
     */
    @Test
    void testIntToObjectEmpty() {
        Survey s = new Survey();
        int[] array = {};
        Object[] result = s.intToObject(array);

        assertArrayEquals(new Object[]{}, result);
    }

    /**
     * Comprova que es poden afegir preguntes a l'enquesta.
     */
    @Test
    void testAddQuestions() {
        Survey s = new Survey(1, "desc", "title");
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question.FreeNumberQuestion("How old are you?"));

        s.addQuestions(questions);

        assertEquals(1, s.getQuestions().size());
    }

    /**
     * Comprova que getId retorna l'ID correcte.
     */
    @Test
    void testGetId() {
        Survey s = new Survey(42, "desc", "title");
        assertEquals(42, s.getId());
    }

    /**
     * Comprova que getTitle retorna el titol correcte.
     */
    @Test
    void testGetTitle() {
        Survey s = new Survey(1, "desc", "My Survey");
        assertEquals("My Survey", s.getTitle());
    }

    /**
     * Comprova que getDescription retorna la descripcio correcta.
     */
    @Test
    void testGetDescription() {
        Survey s = new Survey(1, "Test Description", "title");
        assertEquals("Test Description", s.getDescription());
    }

    /**
     * Comprova que getCreationDate retorna una data valida.
     */
    @Test
    void testGetCreationDate() {
        Survey s = new Survey(1, "desc", "title");
        assertNotNull(s.getCreationDate());
        assertEquals(LocalDate.now(), s.getCreationDate());
    }

    /**
     * Comprova que getQuestions retorna la llista correcta de preguntes.
     */
    @Test
    void testGetQuestions() {
        Survey s = new Survey(1, "desc", "title");
        assertNotNull(s.getQuestions());
        assertTrue(s.getQuestions().isEmpty());
    }

    /**
     * Comprova que getAnswers retorna la llista correcta de respostes.
     */
    @Test
    void testGetAnswers() {
        Survey s = new Survey(1, "desc", "title");
        assertNotNull(s.getAnswers());
        assertTrue(s.getAnswers().isEmpty());
    }

    /**
     * Comprova que getAnalysis retorna la llista correcta d'analisis.
     */
    @Test
    void testGetAnalysis() {
        Survey s = new Survey(1, "desc", "title");
        assertNotNull(s.getAnalysis());
        assertTrue(s.getAnalysis().isEmpty());
    }

    /**
     * Comprova que intToObject funciona amb valors negatius.
     */
    @Test
    void testIntToObjectNegative() {
        Survey s = new Survey();
        int[] array = {-1, -5, -100};
        Object[] result = s.intToObject(array);
        assertArrayEquals(new Object[]{-1, -5, -100}, result);
    }

    /**
     * Comprova que el constructor funciona amb strings buides.
     */
    @Test
    void testConstructorEmptyStrings() {
        Survey s = new Survey(1, "", "");
        assertEquals(1, s.getId());
        assertEquals("", s.getDescription());
        assertEquals("", s.getTitle());
    }

    /**
     * Comprova que addQuestions funciona amb una llista buida.
     */
    @Test
    void testAddQuestionsEmpty() {
        Survey s = new Survey(1, "desc", "title");
        ArrayList<Question> questions = new ArrayList<>();
        s.addQuestions(questions);
        assertEquals(0, s.getQuestions().size());
    }

    /**
     * Comprova que es poden afegir múltiples preguntes.
     */
    @Test
    void testAddMultipleQuestions() {
        Survey s = new Survey(1, "desc", "title");
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question.FreeNumberQuestion("Q1"));
        questions.add(new Question.FreeTextQuestion("Q2"));
        questions.add(new Question.FreeNumberQuestion("Q3"));

        s.addQuestions(questions);
        assertEquals(3, s.getQuestions().size());
    }

}
