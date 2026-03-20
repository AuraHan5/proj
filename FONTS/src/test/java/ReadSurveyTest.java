/* JUnit: ReadSurveyTest — ensures survey questions are parsed correctly from CSV */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReadSurveyTest {

    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Crear un fitxer temporal per les proves
        tempFile = File.createTempFile("test_survey", ".csv");
        tempFile.deleteOnExit();
    }

    /**
     * Comprova que createSurveyFromCSV funciona amb un fitxer CSV simple.
     */
    @Test
    public void testCreateSurveyFromCSV() throws IOException {
        // Crear contingut del fitxer
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("0,What is your age?\n");
            writer.write("4,What is your name?\n");
        }

        ArrayList<Question> questions = ReadSurvey.createSurveyFromCSV(tempFile.getAbsolutePath());

        assertNotNull(questions);
        assertEquals(2, questions.size());
    }

    /**
     * Comprova que createSurveyFromCSV funciona amb fitxer buit.
     */
    @Test
    public void testCreateSurveyFromCSVEmpty() throws IOException {
        // Crear fitxer buit
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("");
        }

        ArrayList<Question> questions = ReadSurvey.createSurveyFromCSV(tempFile.getAbsolutePath());

        assertNotNull(questions);
        assertEquals(0, questions.size());
    }

    /**
     * Comprova que createSurveyFromCSV funciona amb preguntes de tipus diferents.
     */
    @Test
    public void testCreateSurveyFromCSVDifferentTypes() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("0,What is your age?\n");
            writer.write("1,What is your gender?,male|female\n");
            writer.write("2,What is your education?,high school|bachelor|master\n");
            writer.write("3,What are your hobbies?,reading|swimming|gaming,2\n");
            writer.write("4,What is your comment?\n");
        }

        ArrayList<Question> questions = ReadSurvey.createSurveyFromCSV(tempFile.getAbsolutePath());

        assertNotNull(questions);
        assertEquals(5, questions.size());
    }

    /**
     * Comprova que createSurveyFromCSV ignora línies mal formades.
     */
    @Test
    public void testCreateSurveyFromCSVMalformed() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("0,What is your age?\n");
            writer.write("invalid line\n");
            writer.write("4,What is your name?\n");
        }

        ArrayList<Question> questions = ReadSurvey.createSurveyFromCSV(tempFile.getAbsolutePath());

        assertNotNull(questions);
        // Hauria de tenir 2 preguntes vàlides
        assertEquals(2, questions.size());
    }

}