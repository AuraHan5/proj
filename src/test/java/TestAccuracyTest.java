import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class TestAccuracyTest {

    /**
     * Comprova que testAccuracy funciona amb llistes simples.
     */
    @Test
    public void testTestAccuracy() {
        int[] clusters = {0, 1, 0, 1, 0};
        List<Object> labels = List.of("A", "B", "A", "B", "A");

        double accuracy = TestAccuracy.testAccuracy(clusters, labels);
        assertEquals(1.0, accuracy, 0.001); // Tots correctes
    }

    /**
     * Comprova que testAccuracy funciona amb llistes buides.
     */
    @Test
    public void testTestAccuracyEmptyLists() {
        int[] clusters = {};
        List<Object> labels = new ArrayList<>();

        double accuracy = TestAccuracy.testAccuracy(clusters, labels);
        assertTrue(Double.isNaN(accuracy)); // Divisio per zero dona NaN
    }

    /**
     * Comprova que testAccuracy funciona amb llistes de longitud diferent.
     */
    @Test
    public void testTestAccuracyDifferentLengths() {
        int[] clusters = {0, 1, 0};
        List<Object> labels = List.of("A", "B");

        assertThrows(IllegalArgumentException.class, () -> {
            TestAccuracy.testAccuracy(clusters, labels);
        });
    }

    /**
     * Comprova que testAccuracy funciona amb clusters incorrectes.
     */
    @Test
    public void testTestAccuracyIncorrectClusters() {
        int[] clusters = {0, 1, 1, 1, 0};
        List<Object> labels = List.of("A", "B", "A", "B", "A");

        double accuracy = TestAccuracy.testAccuracy(clusters, labels);
        assertTrue(accuracy < 1.0); // No tots correctes
    }

}