import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class SurveyTest {

    /**
     * Comprova que el constructor de Survey inicialitza tots els camps correctament:
     * id, descripcio, titol, data de creacio i llistes buides.
     */
    @Test
    void testConstructor() {
        Survey s = new Survey(10, "desc", "title");

        assertEquals(10, s.id);
        assertEquals("desc", s.description);
        assertEquals("title", s.title);
        assertEquals(LocalDate.now(), s.CreationDate);

        assertTrue(s.questions.isEmpty());
        assertTrue(s.answers.isEmpty());
        assertTrue(s.analysis.isEmpty());
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
}
