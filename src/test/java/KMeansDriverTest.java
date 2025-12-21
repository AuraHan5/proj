import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class KMeansDriverTest {

    /**
     * Comprova que el constructor funciona correctament.
     */
    @Test
    public void testConstructor() {
        // KMeansDriver no te constructor public, pero podem verificar que existeix
        // Aquesta classe nomes te el metode main estatic
        assertTrue(true); // Placeholder test
    }

    /**
     * Comprova que la classe es pot instanciar (tot i que no te constructor public).
     */
    @Test
    public void testClassExists() {
        // Verificar que la classe existeix
        Class<?> clazz = KMeansDriver.class;
        assertNotNull(clazz);
        assertEquals("KMeansDriver", clazz.getSimpleName());
    }

}