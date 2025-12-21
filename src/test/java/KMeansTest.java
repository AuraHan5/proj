import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

public class KMeansTest {

    /**
     * Comprova que el constructor de KMeans inicialitza correctament els valors.
     */
    @Test
    public void testConstructor() {
        KMeans kmeans = new KMeans(3, 100);
        assertEquals(3, kmeans.getK());
    }

    /**
     * Comprova que assignToCluster funciona amb dades simples numeriques.
     */
    @Test
    public void testAssignToClusterSimple() {
        KMeans kmeans = new KMeans(2, 100);

        // Dades simples: 2 clusters evidents
        Object[][] data = {
            {1.0, 2.0},
            {1.1, 2.1},
            {5.0, 6.0},
            {5.1, 6.1}
        };
        int[] variableType = {0, 0}; // Tots numerics

        int[] clusters = kmeans.assignToCluster(data, variableType);

        assertNotNull(clusters);
        assertEquals(4, clusters.length);
        // Comprovem que tots els clusters son valids (0 o 1)
        for (int cluster : clusters) {
            assertTrue(cluster >= 0 && cluster < 2);
        }
    }

    /**
     * Comprova que assignToCluster funciona amb dades buides.
     */
    @Test
    public void testAssignToClusterEmptyData() {
        KMeans kmeans = new KMeans(2, 100);

        Object[][] data = {};
        int[] variableType = {};

        // Amb dades buides, hauria de fallar
        assertThrows(Exception.class, () -> {
            kmeans.assignToCluster(data, variableType);
        });
    }

    /**
     * Comprova que setOpt canvia l'opcio d'optimitzacio.
     */
    @Test
    public void testSetOpt() {
        KMeans kmeans = new KMeans(2, 100);
        kmeans.setOpt(1);
        // No podem verificar directament, pero almenys no hauria de fallar
    }

    /**
     * Comprova que getK retorna el valor correcte.
     */
    @Test
    public void testGetK() {
        KMeans kmeans = new KMeans(5, 100);
        assertEquals(5, kmeans.getK());
    }

    /**
     * Comprova que assignToCluster funciona amb dades mixtes (numeric i categoric).
     */
    @Test
    public void testAssignToClusterMixedData() {
        KMeans kmeans = new KMeans(2, 100);

        Object[][] data = {
            {1.0, "red"},
            {1.1, "red"},
            {5.0, "blue"},
            {5.1, "blue"}
        };
        int[] variableType = {0, 2}; // Numeric i SingleChoiceUnordered

        int[] clusters = kmeans.assignToCluster(data, variableType);

        assertNotNull(clusters);
        assertEquals(4, clusters.length);
        for (int cluster : clusters) {
            assertTrue(cluster >= 0 && cluster < 2);
        }
    }

    /**
     * Comprova que assignToCluster funciona amb un sol cluster.
     */
    @Test
    public void testAssignToClusterSingleCluster() {
        KMeans kmeans = new KMeans(1, 100);

        Object[][] data = {
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        };
        int[] variableType = {0, 0};

        int[] clusters = kmeans.assignToCluster(data, variableType);

        assertNotNull(clusters);
        assertEquals(3, clusters.length);
        // Tots haurien d'estar al cluster 0
        for (int cluster : clusters) {
            assertEquals(0, cluster);
        }
    }

}