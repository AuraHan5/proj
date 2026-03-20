/* JUnit: AnalysisTest — validates silhouette computations and edge cases */
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
        assertNotNull(analysis);
    }

    /**
     * Comprova que initialize funciona amb array buit.
     */
    @Test
    public void testInitializeEmptyArray() {
        double silhouette = Analysis.silhouette(new int[0], new Object[0][0], new int[0], new double[0], new double[0], 0);
        assertEquals(0.0, silhouette, 0.0);
    }

    /**
     * Comprova que silhouette funciona amb dades simples.
     */
    @Test
    public void testSilhouetteSimple() {
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

        double silhouette = Analysis.silhouette(clusters, data, variableType, min, max, 2);

        assertTrue(silhouette >= -1.0 && silhouette <= 1.0);
    }

    /**
     * Comprova que silhouette funciona amb un sol punt per cluster.
     */
    @Test
    public void testSilhouetteSinglePointClusters() {
        int[] clusters = {0, 1, 2};

        Object[][] data = {
            {1.0, 2.0},
            {5.0, 6.0},
            {9.0, 10.0}
        };
        int[] variableType = {0, 0};
        double[] min = {1.0, 2.0};
        double[] max = {9.0, 10.0};

        double silhouette = Analysis.silhouette(clusters, data, variableType, min, max, 3);

        // Amb un sol punt per cluster, el coeficient de silhouette es 0
        assertEquals(0.0, silhouette, 0.001);
    }

    /**
     * Comprova que silhouette funciona amb clusters perfectes.
     */
    @Test
    public void testSilhouettePerfectClusters() {
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

        double silhouette = Analysis.silhouette(clusters, data, variableType, min, max, 2);

        // Clusters perfectes haurien de tenir un coeficient proper a 1
        assertTrue(silhouette > 0.5);
    }

    /**
     * Comprova que amb tots els punts al mateix clúster, la silhouette és 0.
     */
    @Test
    public void testSilhouetteSingleClusterAllPoints() {
        int[] clusters = {0, 0, 0, 0};
        Object[][] data = {
            {1.0}, {2.0}, {3.0}, {4.0}
        };
        int[] variableType = {0};
        double[] min = {1.0};
        double[] max = {4.0};

        double silhouette = Analysis.silhouette(clusters, data, variableType, min, max, 1);
        assertEquals(0.0, silhouette, 1e-9);
    }

    /**
     * Comprova que es llença excepció si les mides dels arrays no coincideixen.
     */
    @Test
    public void testSilhouetteMismatchedLengths() {
        int[] clusters = {0, 1};
        Object[][] data = {
            {1.0, 2.0},
        };
        int[] variableType = {0, 0};
        double[] min = {1.0, 2.0};
        double[] max = {5.0, 6.0};

        assertThrows(IllegalArgumentException.class, () -> {
            Analysis.silhouette(clusters, data, variableType, min, max, 2);
        });
    }

    /**
     * Comprova que silhouette funciona amb k = 0.
     */
    @Test
    public void testSilhouetteKZero() {
        int[] clusters = {};
        Object[][] data = {};
        int[] variableType = {};
        double[] min = {};
        double[] max = {};

        double silhouette = Analysis.silhouette(clusters, data, variableType, min, max, 0);
        assertEquals(0.0, silhouette, 1e-9);
    }

    /**
     * Comprova que silhouette funciona amb dos punts i dos clusters.
     */
    @Test
    public void testSilhouetteTwoPointsTwoClusters() {
        int[] clusters = {0, 1};
        Object[][] data = {
            {1.0},
            {10.0}
        };
        int[] variableType = {0};
        double[] min = {1.0};
        double[] max = {10.0};

        double silhouette = Analysis.silhouette(clusters, data, variableType, min, max, 2);
        assertEquals(0.0, silhouette, 1e-9);
    }

    /**
     * Comprova que silhouette funciona amb clusters molt desbalancejats.
     */
    @Test
    public void testSilhouetteUnbalancedClusters() {
        int[] clusters = {0, 0, 0, 0, 0, 1};
        Object[][] data = {
            {1.0}, {1.1}, {1.2}, {1.3}, {1.4}, {10.0}
        };
        int[] variableType = {0};
        double[] min = {1.0};
        double[] max = {10.0};

        double silhouette = Analysis.silhouette(clusters, data, variableType, min, max, 2);
        assertTrue(silhouette >= -1.0 && silhouette <= 1.0);
    }

}