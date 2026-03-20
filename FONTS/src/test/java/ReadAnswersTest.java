/* JUnit: ReadAnswersTest — verifies CSV reading and preprocessing into data arrays */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReadAnswersTest {

    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Crear un fitxer temporal per les proves
        tempFile = File.createTempFile("test_answers", ".csv");
        tempFile.deleteOnExit();
    }

    /**
     * Comprova que el constructor funciona correctament.
     */
    @Test
    public void testConstructor() {
        ReadAnswers ra = new ReadAnswers();
        assertNotNull(ra);
    }

    /**
     * Comprova que el constructor amb fitxer funciona correctament.
     */
    @Test
    public void testConstructorWithFile() {
        ReadAnswers ra = new ReadAnswers(tempFile);
        assertNotNull(ra);
    }

    /**
     * Comprova que readFile funciona amb un fitxer CSV simple.
     */
    @Test
    public void testReadFileSimple() throws IOException {
        // Crear contingut del fitxer
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,name,age\n");
            writer.write("0,4,0\n"); // tipus de columnes: numeric, text, numeric
            writer.write("1,John,25\n");
            writer.write("2,Jane,30\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        Object[][] data = ra.readFile();

        assertNotNull(data);
        assertEquals(2, data.length); // 2 files
    }

    /**
     * Comprova que processData funciona correctament.
     */
    @Test
    public void testProcessData() throws IOException {
        // Crear fitxer amb dades
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,name,age\n");
            writer.write("0,4,0\n"); // tipus: id=numeric, name=text, age=numeric
            writer.write("1,John,25\n");
            writer.write("2,Jane,30\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        Object[][] rawData = ra.readFile();
        Object[][] processedData = ra.processData(rawData);

        assertNotNull(processedData);
        assertEquals(2, processedData.length);
    }

    /**
     * Comprova que getVariableType funciona correctament.
     */
    @Test
    public void testGetVariableType() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,name,age\n");
            writer.write("0,4,0\n"); // tipus
            writer.write("1,John,25\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        ra.readFile();
        int[] types = ra.getVariableType();

        assertNotNull(types);
        assertEquals(1, types.length); // numCols-2 = 3-2 = 1
        assertEquals(4, types[0]); // name -> text
    }

    /**
     * Comprova que getIds funciona correctament.
     */
    @Test
    public void testGetIds() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,name\n");
            writer.write("0,4\n");
            writer.write("1,John\n");
            writer.write("2,Jane\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        ra.readFile();
        List<Object> ids = ra.getIds();

        assertNotNull(ids);
        assertEquals(2, ids.size());
        assertEquals("1", ids.get(0));
        assertEquals("2", ids.get(1));
    }

    /**
     * Comprova que getLabels funciona correctament.
     */
    @Test
    public void testGetLabels() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,name,category\n");
            writer.write("0,4,1\n");
            writer.write("1,John,A\n");
            writer.write("2,Jane,B\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        ra.readFile();
        List<Object> labels = ra.getLabels();

        assertNotNull(labels);
        assertEquals(2, labels.size());
        assertEquals("A", labels.get(0));
        assertEquals("B", labels.get(1));
    }

    /**
     * Comprova que initializeVariableType funciona correctament.
     */
    @Test
    public void testInitializeVariableType() {
        ReadAnswers ra = new ReadAnswers();
        int[] types = {0, 4, 2};

        ra.initializeVariableType(types);

        // No podem verificar directament, pero almenys no hauria de fallar
        assertNotNull(ra);
    }

    /**
     * Comprova que processData omple tots els camps null en una combinació de tipus.
     */
    @Test
    public void testProcessDataFillsNullsAcrossTypes() {
        ReadAnswers ra = new ReadAnswers();
        int[] types = {0, 1, 2, 3, 4}; // numeric, ordered, unordered, multiple, text
        ra.initializeVariableType(types);

        Object[][] data = new Object[][] {
            {5.0, null, null, null, null},
            {null, null, null, null, null}
        };

        Object[][] processed = ra.processData(data);
        assertNotNull(processed);
        assertEquals(2, processed.length);
        assertEquals(5, processed[0].length);

        for (Object[] row : processed) {
            for (Object value : row) {
                assertNotNull(value, "Cap valor ha de quedar null després de processar");
            }
        }
    }

    /**
     * Comprova que una columna numèrica completament buida rep un valor per defecte segur (0.0).
     */
    @Test
    public void testProcessDataNumericAllNullsGetsDefault() {
        ReadAnswers ra = new ReadAnswers();
        int[] types = {0};
        ra.initializeVariableType(types);

        Object[][] data = new Object[][] {
            {null},
            {null}
        };

        Object[][] processed = ra.processData(data);
        assertNotNull(processed);
        assertEquals(2, processed.length);
        assertEquals(1, processed[0].length);

        for (Object[] row : processed) {
            assertTrue(row[0] instanceof Double);
            assertEquals(0.0, (Double) row[0], 0.0);
        }
    }

    /**
     * Comprova que readFile retorna null si hi ha un error de format (p.ex. tipus no numèric).
     */
    @Test
    public void testReadFileFormatErrorYieldsNullCell() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,age,label\n");
            writer.write("0,0,1\n"); // age numeric
            writer.write("1,not-a-number,X\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        Object[][] data = ra.readFile();
        assertNull(data, "Amb un valor numèric mal formatejat es retorna null (fallback actual)");
    }

    /**
     * Comprova que readFile retorna null si falta la línia de tipus (second line).
     */
    @Test
    public void testReadFileMissingTypesLineDefaultsNumeric() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,name,age\n");
            writer.write("1,John,25\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile, false); // sense línia de tipus
        Object[][] data = ra.readFile();

        assertNull(data, "Amb hasTypesLine=false i valor no numèric, readFile retorna null (Format error)");
    }

    /**
     * Comprova que les files amb columnes insuficients s'ignoren (només es processen les completes).
     */
    @Test
    public void testReadFileSkipsShortRows() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,score,label\n");
            writer.write("0,0,0\n");
            writer.write("1,5,A\n");
            writer.write("2,,B\n"); // score buit -> null
            writer.write("3,onlyid\n"); // fila curta -> s'ha d'ignorar
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        Object[][] data = ra.readFile();

        assertNotNull(data, "Les files vàlides s'han de processar");
        assertEquals(2, data.length, "La fila curta s'ha d'ignorar");
        assertEquals(1, data[0].length);
        assertEquals(5.0, (Double) data[0][0], 0.0);
        assertNull(data[1][0], "Valors buits es queden a null per imputació posterior");
    }

    /**
     * Comprova que es poden llegir CSV separats amb punt i coma.
     */
    @Test
    public void testReadFileWithSemicolonSeparator() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id;score;label\n");
            writer.write("0;0;1\n"); // línia de tipus: score numèrica
            writer.write("1;0;A\n");
            writer.write("2;10;B\n");
        }

        ReadAnswers ra = new ReadAnswers(tempFile);
        Object[][] data = ra.readFile();

        assertNotNull(data);
        assertEquals(2, data.length);
        assertEquals(1, data[0].length);
        assertEquals(0.0, (Double) data[0][0], 0.0);
        assertEquals(10.0, (Double) data[1][0], 0.0);
    }

}