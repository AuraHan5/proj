import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuxiliarMethodsTest {

    /**
     * Comprova que es retorni correctament la moda.
     */
    @Test
    public void calculateModeNormal() {
        List<Double> elements = List.of(4.2,3.1,3.1,9.8,3.1,4.2);
        Double mode = AuxiliarMethods.calculateMode(elements);
        assertEquals(3.1,mode);
    }

    /**
     * Comprova que es retorni be la moda quan hi ha diverses opcions repetides.
     */
    @Test
    public void calculateModeMultipleOptions() {
        List<Double> elements = List.of(4.2,3.1,3.1,9.8,3.1,4.2,4.2);
        Double mode = AuxiliarMethods.calculateMode(elements);
        assertEquals(3.1,mode);
    }

    /**
     * Comprova que es retorni null quan tots els elements de la llista son nuls.
     */
    @Test
    public void calculateModeWithNullElements() {
        List<Double> elements = Arrays.asList(null,null);
        Double mode = AuxiliarMethods.calculateMode(elements);
        assertNull(mode);
    }

    /**
     * Comprova que es retorni null per a una llista buida.
     */
    @Test
    public void calculateModeEmptyList() {
        List<Double> elements = new ArrayList<>();
        Double mode = AuxiliarMethods.calculateMode(elements);
        assertNull(mode);
    }

    /**
     * Comprova que es retorni la moda correcta en una llista amb tots els valors iguals.
     */
    @Test
    public void calculateModeAllValuesEqual() {
        List<Double> elements = List.of(1.0,1.0,1.0,1.0,1.0,1.0);
        Double mode = AuxiliarMethods.calculateMode(elements);
        assertEquals(1.0,mode);
    }

    /**
     * Comprova que es retorni el primer valor quan tots els valors son diferents.
     */
    @Test
    public void calculateModeAllValuesDifferent() {
        List<Double> elements = List.of(1.0,1.1,1.2,1.3,1.4,1.5);
        Double mode = AuxiliarMethods.calculateMode(elements);
        assertEquals(1.0,mode);
    }

    /**
     * Comprova que es calculi correctament la mitjana en un cas general.
     */
    @Test
    public void calculateAverageNormal() {
        List<Double> elements = List.of(2.0,2.0,4.0,4.0);
        Double average = AuxiliarMethods.calculateAverage(elements);
        assertEquals(3.0,average);
    }

    /**
     * Comprova que es calculi correctament la mitjana d un unic valor.
     */
    @Test
    public void calculateAverageOneValue() {
        List<Double> elements = List.of(2.0);
        Double average = AuxiliarMethods.calculateAverage(elements);
        assertEquals(2.0,average);
    }

    /**
     * Comprova que la mitjana sigui correcta quan hi ha valors positius i negatius.
     */
    @Test
    public void calculateAverageNegativeValues() {
        List<Double> elements = List.of(2.0,-2.0,4.0,-4.0);
        Double average = AuxiliarMethods.calculateAverage(elements);
        assertEquals(0.0,average);
    }

    /**
     * Comprova que es retorni null per a una llista buida.
     */
    @Test
    public void calculateAverageEmptyList() {
        List<Double> elements = new ArrayList<>();
        Double average = AuxiliarMethods.calculateAverage(elements);
        assertNull(average);
    }

    /**
     * Comprova que es retorni null per a una llista amb elements nuls.
     */
    @Test
    public void calculateAverageWithNullElements() {
        List<Double> elements = Arrays.asList(null,null);
        Double average = AuxiliarMethods.calculateAverage(elements);
        assertNull(average);
    }

    /**
     * Comprova que es calculi correctament el minim quan hi ha valors numerics.
     */
    @Test
    public void calculateMinVectorNormal() {
        Object[][] data = {{1.1},{4.3},{5.6},{0.2}};
        int[] variableType = {0};
        double[] min = AuxiliarMethods.calculateMinVector(data,variableType);
        assertArrayEquals(new double[]{0.2},min);
    }

    /**
     * Comprova que es retorni -1.0 quan els valors no son numerics.
     */
    @Test
    public void calculateMinVectorNonNumericValue() {
        Object[][] data = {{"dog"},{"cat"},{"bird"},{"shark"}};
        int[] variableType = {4};
        double[] min = AuxiliarMethods.calculateMinVector(data,variableType);
        assertArrayEquals(new double[]{-1.0},min);
    }

    /**
     * Comprova que es calculi correctament el minim per a diverses columnes de valors.
     */
    @Test
    public void calculateMinVectorMultipleValues() {
        Object[][] data = {{1.1,0.2},{4.3,0.0},{5.6,1.3},{0.2,0.2}};
        int[] variableType = {0,0};
        double[] min = AuxiliarMethods.calculateMinVector(data,variableType);
        assertArrayEquals(new double[]{0.2,0.0},min);
    }

    /**
     * Comprova que es retorni el mateix valor quan tots els elements son iguals.
     */
    @Test
    public void calculateMinVectorAllEquals() {
        Object[][] data = {{0.2},{0.2},{0.2},{0.2}};
        int[] variableType = {0};
        double[] min = AuxiliarMethods.calculateMinVector(data,variableType);
        assertArrayEquals(new double[]{0.2},min);
    }

    /**
     * Comprova que es calculi correctament el maxim per a valors numerics.
     */
    @Test
    public void calculateMaxVectorNormal() {
        Object[][] data = {{1.1},{4.3},{5.6},{0.2}};
        int[] variableType = {0};
        double[] max = AuxiliarMethods.calculateMaxVector(data,variableType);
        assertArrayEquals(new double[]{5.6},max);
    }

    /**
     * Comprova que es retorni -1.0 quan els valors no son numerics.
     */
    @Test
    public void calculateMaxVectorNonNumericValue() {
        Object[][] data = {{"dog"},{"cat"},{"bird"},{"shark"}};
        int[] variableType = {4};
        double[] max = AuxiliarMethods.calculateMaxVector(data,variableType);
        assertArrayEquals(new double[]{-1.0},max);
    }

    /**
     * Comprova que es calculi correctament el maxim de diverses columnes.
     */
    @Test
    public void calculateMaxVectorMultipleValues() {
        Object[][] data = {{1.1,0.2},{4.3,0.0},{5.6,1.3},{0.2,0.2}};
        int[] variableType = {0,0};
        double[] max = AuxiliarMethods.calculateMaxVector(data,variableType);
        assertArrayEquals(new double[]{5.6,1.3},max);
    }

    /**
     * Comprova que es retorni el mateix maxim quan tots els valors son iguals.
     */
    @Test
    public void calculateMaxVectorAllEquals() {
        Object[][] data = {{0.2},{0.2},{0.2},{0.2}};
        int[] variableType = {0};
        double[] max = AuxiliarMethods.calculateMaxVector(data,variableType);
        assertArrayEquals(new double[]{0.2},max);
    }

    /**
     * Comprova que el percentil 95 es calculi correctament en una llista ordenada.
     */
    @Test
    public void test95thPercentileOrderedList() {
        List<Double> elements = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        Double value = AuxiliarMethods.calculateMax95P(elements);
        assertEquals(4.0,value);
    }

    /**
     * Comprova que el percentil 95 es calculi be encara que la llista estigui desordenada.
     */
    @Test
    public void test95thPercentileUnorderedList() {
        List<Double> elements = Arrays.asList(1.0,4.0,5.0,3.0,2.0);
        Double value = AuxiliarMethods.calculateMax95P(elements);
        assertEquals(4.0,value);
    }

    /**
     * Comprova el percentil 95 per a una llista d un unic element.
     */
    @Test
    public void test95thPercentileSingleValueList() {
        List<Double> elements = Arrays.asList(1.0);
        Double value = AuxiliarMethods.calculateMax95P(elements);
        assertEquals(1.0,value);
    }

    /**
     * Comprova que els valors nuls s ignorin al calcular el percentil 95.
     */
    @Test
    public void test95thPercentileNullValuesList() {
        List<Double> elements = Arrays.asList(1.0,null,3.0,4.0);
        Double value = AuxiliarMethods.calculateMax95P(elements);
        assertEquals(3.0,value);
    }

    /**
     * Comprova que es retorni null quan tots els elements son nuls.
     */
    @Test
    public void test95thPercentileAllNullsList() {
        List<Double> elements = Arrays.asList(null,null,null);
        Double value = AuxiliarMethods.calculateMax95P(elements);
        assertNull(value);
    }

    /**
     * Comprova el percentil 95 quan tots els valors son iguals.
     */
    @Test
    public void test95thPercentileRepeatedValuesList() {
        List<Double> elements = Arrays.asList(1.0,1.0,1.0,1.0);
        Double value = AuxiliarMethods.calculateMax95P(elements);
        assertEquals(1.0,value);
    }

    /**
     * Comprova que es calculi correctament l'IQR en un cas normal.
     */
    @Test
    public void testCalculateIQRNormal() {
        List<Double> elements = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        Double[] iqr = AuxiliarMethods.calculateIQR(elements);
        assertNotNull(iqr);
        assertEquals(2, iqr.length);
        // Q1 = 2.0, Q3 = 4.0, IQR = 2.0
        // Lower limit = 2.0 - 3*2.0 = -4.0
        // Upper limit = 4.0 + 3*2.0 = 10.0
        assertEquals(-4.0, iqr[0]);
        assertEquals(10.0, iqr[1]);
    }

    /**
     * Comprova que calculateIQR retorni null per a llista buida.
     */
    @Test
    public void testCalculateIQREmptyList() {
        List<Double> elements = new ArrayList<>();
        Double[] iqr = AuxiliarMethods.calculateIQR(elements);
        assertNull(iqr);
    }

    /**
     * Comprova que calculateIQR ignori valors nulls.
     */
    @Test
    public void testCalculateIQRWithNulls() {
        List<Double> elements = Arrays.asList(1.0, null, 3.0, 4.0);
        Double[] iqr = AuxiliarMethods.calculateIQR(elements);
        assertNotNull(iqr);
        // With values [1.0, 3.0, 4.0]: Q1=1.0, Q3=4.0, IQR=3.0
        // Lower = 1.0 - 3*3.0 = -8.0, Upper = 4.0 + 3*3.0 = 13.0
        assertEquals(-8.0, iqr[0]);
        assertEquals(13.0, iqr[1]);
    }

    /**
     * Comprova que es calculin correctament els quartils.
     */
    @Test
    public void testCalculateQuartilesNormal() {
        List<Double> elements = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        double[] quartiles = AuxiliarMethods.calculateQuartiles(elements);
        assertNotNull(quartiles);
        assertEquals(3, quartiles.length);
        assertEquals(2.0, quartiles[0]); // Q1
        assertEquals(3.0, quartiles[1]); // Q2 (median)
        assertEquals(4.0, quartiles[2]); // Q3
    }

    /**
     * Comprova que calculateQuartiles retorni null per a llista buida.
     */
    @Test
    public void testCalculateQuartilesEmptyList() {
        List<Double> elements = new ArrayList<>();
        double[] quartiles = AuxiliarMethods.calculateQuartiles(elements);
        assertNull(quartiles);
    }

    /**
     * Comprova que es calculi correctament la mediana per a llista amb nombre senar d'elements.
     */
    @Test
    public void testCalculateMedianOdd() {
        List<Double> elements = Arrays.asList(1.0, 3.0, 2.0);
        Double median = AuxiliarMethods.calculateMedian(elements);
        assertEquals(2.0, median);
    }

    /**
     * Comprova que es calculi correctament la mediana per a llista amb nombre parell d'elements.
     */
    @Test
    public void testCalculateMedianEven() {
        List<Double> elements = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        Double median = AuxiliarMethods.calculateMedian(elements);
        assertEquals(2.5, median);
    }

    /**
     * Comprova que calculateMedian retorni null per a llista buida.
     */
    @Test
    public void testCalculateMedianEmptyList() {
        List<Double> elements = new ArrayList<>();
        Double median = AuxiliarMethods.calculateMedian(elements);
        assertNull(median);
    }

    /**
     * Comprova que es calculi correctament la distancia de Levenshtein entre dues cadenes iguals.
     */
    @Test
    public void testLevenshteinEqualStrings() {
        int distance = AuxiliarMethods.levenshtein("test", "test");
        assertEquals(0, distance);
    }

    /**
     * Comprova que es calculi correctament la distancia de Levenshtein amb insercions.
     */
    @Test
    public void testLevenshteinInsertion() {
        int distance = AuxiliarMethods.levenshtein("abc", "abxc");
        assertEquals(1, distance);
    }

    /**
     * Comprova que es calculi correctament la distancia de Levenshtein amb eliminacions.
     */
    @Test
    public void testLevenshteinDeletion() {
        int distance = AuxiliarMethods.levenshtein("abcd", "acd");
        assertEquals(1, distance);
    }

    /**
     * Comprova que es calculi correctament la distancia de Levenshtein amb substitucions.
     */
    @Test
    public void testLevenshteinSubstitution() {
        int distance = AuxiliarMethods.levenshtein("abc", "adc");
        assertEquals(1, distance);
    }

    /**
     * Comprova que es calculi correctament la distancia de Levenshtein amb cadenes buides.
     */
    @Test
    public void testLevenshteinEmptyStrings() {
        int distance = AuxiliarMethods.levenshtein("", "");
        assertEquals(0, distance);
    }

    /**
     * Comprova que es calculi correctament la distancia de Levenshtein amb una cadena buida.
     */
    @Test
    public void testLevenshteinOneEmptyString() {
        int distance = AuxiliarMethods.levenshtein("", "abc");
        assertEquals(3, distance);
    }

    /**
     * Comprova que la similitud cosinus retorni 1.0 per vectors identics.
     */
    @Test
    public void testCosineSimilarityIdenticalVectors() {
        double[] vec1 = {1.0, 2.0, 3.0};
        double[] vec2 = {1.0, 2.0, 3.0};
        double similarity = AuxiliarMethods.cosineSimilarity(vec1, vec2);
        assertEquals(1.0, similarity, 1e-9);
    }

    /**
     * Comprova que la similitud cosinus retorni 0.0 per vectors ortogonals.
     */
    @Test
    public void testCosineSimilarityOrthogonalVectors() {
        double[] vec1 = {1.0, 0.0};
        double[] vec2 = {0.0, 1.0};
        double similarity = AuxiliarMethods.cosineSimilarity(vec1, vec2);
        assertEquals(0.0, similarity, 1e-9);
    }

    /**
     * Comprova que la similitud cosinus retorni 0.0 per vectors oposats.
     */
    @Test
    public void testCosineSimilarityOppositeVectors() {
        double[] vec1 = {1.0, 2.0};
        double[] vec2 = {-1.0, -2.0};
        double similarity = AuxiliarMethods.cosineSimilarity(vec1, vec2);
        assertEquals(-1.0, similarity, 1e-9);
    }

    /**
     * Comprova que la similitud cosinus retorni 0.0 per vectors zero.
     */
    @Test
    public void testCosineSimilarityZeroVectors() {
        double[] vec1 = {0.0, 0.0};
        double[] vec2 = {1.0, 2.0};
        double similarity = AuxiliarMethods.cosineSimilarity(vec1, vec2);
        assertEquals(0.0, similarity, 1e-9);
    }

}
