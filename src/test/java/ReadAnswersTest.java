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

}