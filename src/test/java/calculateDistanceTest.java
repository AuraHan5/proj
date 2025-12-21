import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class calculateDistanceTest {

    /**
     * Comprova que el metode compute llenci una IllegalArgumentException
     * quan tots els parametres passats son null.
     */
    @Test
    public void testNullArrays() {
        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(null,null,null,null,null);
        });
    }

    /**
     * Comprova que el metode compute llenci una IllegalArgumentException
     * quan les arrays d entrada tenen longituds diferents.
     */
    @Test
    public void testArraysDifferentLengths() {
        Object[] point = {"hello",2,3.2};
        Object[] centroid = {"goodbye",1,6.2};
        double[] max = {0.0,2.0,6.2};
        double[] min = {0.0,1.0,3.2};
        int[] variableType = {4,0,0,1};

        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(point,centroid,variableType,max,min);
        });
    }

    /**
     * Comprova que compute llenci una IllegalArgumentException
     * quan algun element del punt o del centroide es null.
     */
    @Test
    public void testNullElements() {
        Object[] point = {null,2,3.2};
        Object[] centroid = {"goodbye",1,null};
        double[] max = {0.0,2.0,6.2};
        double[] min = {0.0,1.0,3.2};
        int[] variableType = {4,0,0};

        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(point,centroid,variableType,max,min);
        });
    }

    /**
     * Comprova que compute llenci una IllegalArgumentException
     * quan algun variableType es fora del rang permis (0–4).
     */
    @Test
    public void testInvalidVariableType() {
        Object[] point = {"hello",2,3.2};
        Object[] centroid = {"goodbye",1,2};
        double[] max = {0.0,2.0,6.2};
        double[] min = {0.0,1.0,3.2};
        int[] variableType = {5,0,0};

        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(point,centroid,variableType,max,min);
        });
    }

    /**
     * Comprova que compute retorni 0.0 quan punt i centroide son identics.
     */
    @Test
    public void testEqualMaxMin() {
        Object[] point = {1.0,1.0,1.0};
        Object[] centroid = {1.0,1.0,1.0};
        double[] max = {1,1,1};
        double[] min = {1,1,1};
        int[] variableType = {0,0,0};

        double dist =CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova el calcul normalitzat per a un unic valor numeric (variableType = 0).
     */
    @Test
    public void testNumericInput() {
        Object[] point = {2.3};
        Object[] centroid = {8.4};
        double[] max = {8.4};
        double[] min = {2.3};
        int[] variableType = {0};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova el calcul normalitzat per a diversos valors numerics.
     */
    @Test
    public void testNumericInputMultiple() {
        Object[] point = {2.3,3.1};
        Object[] centroid = {8.4,9.2};
        double[] max = {7.1,9.7};
        double[] min = {2.0,1.9};
        int[] variableType = {0,0};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.021103910876336,dist,1e-9);
    }

    /**
     * Comprova que compute retorni 0.0 quan punt i centroide tenen exactament el mateix valor.
     */
    @Test
    public void testSameValue() {
        Object[] point = {2.3};
        Object[] centroid = {2.3};
        double[] max = {8.4};
        double[] min = {2.3};
        int[] variableType = {0};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova el calcul amb valors negatius per a variableType numeric.
     */
    @Test
    public void testNegativeValue() {
        Object[] point = {-2.3};
        Object[] centroid = {-8.4};
        double[] max = {-8.4};
        double[] min = {-2.3};
        int[] variableType = {0};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova el calcul de distancia per a valors numerics molt grans.
     */
    @Test
    public void testLargeValues() {
        Object[] point = {45223426789123.2};
        Object[] centroid = {65223426789123.2};
        double[] max = {65223426789123.2};
        double[] min = {45223426789123.2};
        int[] variableType = {0};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova el calcul de distancia per a SingleChoiceOrdered.
     */
    @Test
    public void testSingleChoiceOrdered() {
        Object[] point = {new Object[] {2,4}};
        Object[] centroid = {new Object[]{0,4}};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {1};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(2.0/3.0,dist);
    }

    /**
     * Comprova que compute retorni 0.0 quan els dos SingleChoiceOrdered son iguals.
     */
    @Test
    public void testSingleChoiceOrderedEqual() {
        Object[] point = {new Object[] {2,4}};
        Object[] centroid = {new Object[]{2,4}};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {1};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova compute per a SingleChoiceOrdered quan la opcio es l ultima disponible.
     */
    @Test
    public void testSingleChoiceOrderedWithLastIndex() {
        Object[] point = {new Object[] {3,4}};
        Object[] centroid = {new Object[]{0,4}};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {1};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova que compute llenci una IllegalArgumentException
     * quan un index de SingleChoiceOrdered esta fora de rang.
     */
    @Test
    public void testSingleChoiceOrderedIndexOutOfBounds() {
        Object[] point = {new Object[] {4,4}};
        Object[] centroid = {new Object[]{0,4}};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {1};

        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(point,centroid,variableType,max,min);
        });
    }

    /**
     * Comprova que compute llenci una IllegalArgumentException
     * quan l entrada no segueix el format esperat per SingleChoiceOrdered.
     */
    @Test
    public void testSingleChoiceOrderedInvalidInput() {
        Object[] point = {4};
        Object[] centroid = {4};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {1};

        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(point,centroid,variableType,max,min);
        });
    }

    /**
     * Comprova compute per a multiples respostes de tipus SingleChoiceOrdered.
     */
    @Test
    public void testSingleChoiceOrderedMultipleInputs() {
        Object[] point = {new Object[] {2,4},new Object[] {2,4}};
        Object[] centroid = {new Object[]{0,4},new Object[] {2,4}};
        double[] max = {0.0,0.0};
        double[] min = {0.0,0.0};
        int[] variableType = {1,1};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals((2.0/3.0)/2.0,dist);
    }

    /**
     * Comprova compute per a un unic SingleChoiceOrdered amb una sola opcio.
     */
    @Test
    public void testSingleChoiceOrderedOnlyOneOption() {
        Object[] point = {new Object[]{0,1}};
        Object[] centroid = {new Object[]{0,1}};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {1};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova compute de SingleChoiceUnordered amb valors iguals.
     */
    @Test
    public void testSingleChoiceUnorderedEqualValues() {
        Object[] point = {"tomato"};
        Object[] centroid = {"tomato"};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {2};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova compute de SingleChoiceUnordered amb valors diferents.
     */
    @Test
    public void testSingleChoiceUnorderedDifferentValues() {
        Object[] point = {"tomato"};
        Object[] centroid = {"lettuce"};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {2};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova compute de SingleChoiceUnordered amb diverses entrades.
     */
    @Test
    public void testSingleChoiceUnorderedMultipleInputs() {
        Object[] point = {"tomato",1,5.2};
        Object[] centroid = {"tomato",2,5.2};
        double[] max = {0.0,0.0,0.0};
        double[] min = {0.0,0.0,0.0};
        int[] variableType = {2,2,2};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0/3.0,dist);
    }

    /**
     * Comprova compute de MultipleChoice quan punt i centroide tenen exactament el mateix conjunt.
     */
    @Test
    public void testMultipleChoiceEqualValues() {
        Object[] point = {Set.of("apple","banana","pear")};
        Object[] centroid = {Set.of("apple","banana","pear")};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {3};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova que compute llenci una IllegalArgumentException quan
     * l entrada per MultipleChoice no es un conjunt (Set).
     */
    @Test
    public void testMultipleChoiceInvalidInput() {
        Object[] point = {new Object[]{"apple","banana","pear"}};
        Object[] centroid = {new Object[]{"apple","banana","pear"}};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {3};

        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(point,centroid,variableType,max,min);
        });
    }

    /**
     * Comprova compute de MultipleChoice quan els conjunts son completament diferents.
     */
    @Test
    public void testMultipleChoiceDifferentValues() {
        Object[] point = {Set.of("melon","cherry","watermelon")};
        Object[] centroid = {Set.of("apple","banana","pear")};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {3};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova compute de MultipleChoice amb conjunts parcialment coincidents.
     */
    @Test
    public void testMultipleChoicePartiallyDifferentValues() {
        Object[] point = {Set.of("apple","cherry","melon")};
        Object[] centroid = {Set.of("apple","banana","pear")};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {3};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.8,dist);
    }

    /**
     * Comprova compute de MultipleChoice amb conjunts de mides diferents.
     */
    @Test
    public void testMultipleChoiceDifferentLenghtSets() {
        Object[] point = {Set.of("apple","banana")};
        Object[] centroid = {Set.of("apple","banana","pear")};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {3};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0/3.0,dist,1e-6);
    }

    /**
     * Comprova compute de MultipleChoice quan el punt es buit.
     */
    @Test
    public void testMultipleChoiceEmptySet() {
        Object[] point = {Set.of()};
        Object[] centroid = {Set.of("apple","banana","pear")};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {3};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova que compute llenci IllegalArgumentException
     * quan MultipleChoice conte valors duplicats.
     */
    @Test
    public void testMultipleChoiceDuplicateValues() {
        Object[] point = {List.of("apple","banana","pear","apple")};
        Object[] centroid = {List.of("apple","banana","pear")};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {3};

        assertThrows(IllegalArgumentException.class, () -> {
            CalculateDistance.compute(point,centroid,variableType,max,min);
        });
    }

    /**
     * Comprova compute de FreeText quan les dues cadenes son iguals.
     */
    @Test
    public void testFreeTextEqualValues() {
        Object[] point = {"I love school."};
        Object[] centroid = {"I love school."};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {4};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova compute de FreeText amb cadenes completament diferents.
     */
    @Test
    public void testFreeTextDifferentValues() {
        Object[] point = {"banana"};
        Object[] centroid = {"apples"};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {4};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova compute de FreeText amb dues cadenes buides.
     */
    @Test
    public void testFreeTextBothEmptyValues() {
        Object[] point = {""};
        Object[] centroid = {""};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {4};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }

    /**
     * Comprova compute de FreeText quan una cadena es buida i l altra no.
     */
    @Test
    public void testFreeTextOneEmptyValue() {
        Object[] point = {""};
        Object[] centroid = {"pear"};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {4};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(1.0,dist);
    }

    /**
     * Comprova compute de FreeText quan una cadena es substring parcial de l altra.
     */
    @Test
    public void testFreeTextPartialOverlap() {
        Object[] point = {"bad"};
        Object[] centroid = {"not bad"};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {4};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(4.0/7.0,dist);
    }

    /**
     * Comprova que compute de FreeText consideri insensibles les diferencies
     * entre majuscules i minuscules.
     */
    @Test
    public void testFreeTextCaseSensitive() {
        Object[] point = {"Good"};
        Object[] centroid = {"good"};
        double[] max = {0.0};
        double[] min = {0.0};
        int[] variableType = {4};

        double dist = CalculateDistance.compute(point,centroid,variableType,max,min);
        assertEquals(0.0,dist);
    }
}
