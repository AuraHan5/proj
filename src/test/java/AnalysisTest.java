import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnalysisTest {

    /**
     * Comprova que el constructor d'Analysis inicialitza correctament.
     */
    @Test
    public void testConstructor() {
        Analysis analysis = new Analysis();
        // No hi ha getters publics, pero almenys no hauria de fallar
        assertNotNull(analysis);
    }

    /**
     * Comprova que initialize funciona amb un array de clusters valid.
     */
    @Test
    public void testInitialize() {
        Analysis analysis = new Analysis();
        int[] clusters = {0, 0, 1, 1, 2, 2};

        analysis.initialize(clusters);

        // No podem verificar l'estat intern, pero almenys no hauria de fallar
        assertNotNull(analysis);
    }

    /**
     * Comprova que initialize funciona amb array buit.
     */
    @Test
    public void testInitializeEmptyArray() {
        Analysis analysis = new Analysis();
        int[] clusters = {};

        analysis.initialize(clusters);
        assertNotNull(analysis);
    }

    /**
     * Comprova que silhouette funciona amb dades simples.
     */
    @Test
    public void testSilhouetteSimple() {
        Analysis analysis = new Analysis();
        int[] clusters = {0, 0, 1, 1};

        Object[][] data = {
            {1.0, 2.0},
            {1.1, 2.1},
            {5.0, 6.0},
            {5.1, 6.1}
        };
        int[] variableType = {0, 0};
        double[] min = {1.0, 2.0};
        double[] max = {5.1, 6.1};

        double silhouette = analysis.silhouette(clusters, data, variableType, min, max, 2);

        assertTrue(silhouette >= -1.0 && silhouette <= 1.0);
    }

    /**
     * Comprova que silhouette funciona amb un sol punt per cluster.
     */
    @Test
    public void testSilhouetteSinglePointClusters() {
        Analysis analysis = new Analysis();
        int[] clusters = {0, 1, 2};

        Object[][] data = {
            {1.0, 2.0},
            {5.0, 6.0},
            {9.0, 10.0}
        };
        int[] variableType = {0, 0};
        double[] min = {1.0, 2.0};
        double[] max = {9.0, 10.0};

        double silhouette = analysis.silhouette(clusters, data, variableType, min, max, 3);

        // Amb un sol punt per cluster, el coeficient de silhouette es 0
        assertEquals(0.0, silhouette, 0.001);
    }

    /**
     * Comprova que silhouette funciona amb clusters perfectes.
     */
    @Test
    public void testSilhouettePerfectClusters() {
        Analysis analysis = new Analysis();
        int[] clusters = {0, 0, 0, 1, 1, 1};

        Object[][] data = {
            {1.0, 1.0},
            {1.1, 1.1},
            {0.9, 0.9},
            {5.0, 5.0},
            {5.1, 5.1},
            {4.9, 4.9}
        };
        int[] variableType = {0, 0};
        double[] min = {0.9, 0.9};
        double[] max = {5.1, 5.1};

        double silhouette = analysis.silhouette(clusters, data, variableType, min, max, 2);

        // Clusters perfectes haurien de tenir un coeficient proper a 1
        assertTrue(silhouette > 0.5);
    }

}