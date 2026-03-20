/* JUnit: KMedoidsTest — validates KMedoids behavior for simple numeric clusters */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Proves bàsiques per validar KMedoids amb conjunts petits i controlables.
 */
public class KMedoidsTest {

    /**
     * Comprova que KMedoids agrupa correctament dos clústers numèrics separats.
     */
    @Test
    public void testAssignToClusterSimpleNumeric() {
        KMedoids kmedoids = new KMedoids(2, 100);

        Object[][] data = {
            {1.0, 2.0},
            {1.1, 2.1},
            {5.0, 6.0},
            {5.1, 6.1}
        };
        int[] variableType = {0, 0};

        int[] clusters = kmedoids.assignToCluster(data, variableType);

        assertNotNull(clusters);
        assertEquals(4, clusters.length);
        for (int c : clusters) {
            assertTrue(c >= 0 && c < 2, "Cluster ha d'estar dins [0,1]");
        }
    }

    /**
     * Comprova que amb k=1 totes les mostres s'assignen al mateix clúster.
     */
    @Test
    public void testAssignToClusterSingleCluster() {
        KMedoids kmedoids = new KMedoids(1, 50);

        Object[][] data = {
            {2.0},
            {3.0},
            {4.0}
        };
        int[] variableType = {0};

        int[] clusters = kmedoids.assignToCluster(data, variableType);

        assertNotNull(clusters);
        assertEquals(3, clusters.length);
        for (int c : clusters) {
            assertEquals(0, c, "Amb k=1 tots els punts han d'anar al clúster 0");
        }
    }
}
