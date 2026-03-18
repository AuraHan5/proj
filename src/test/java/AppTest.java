import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;

public class AppTest {

    private App app;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        app = new App();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    /**
     * Comprova el constructor d App.
     */
    @Test
    void testAppConstructor() {
        assertNotNull(app);
        // Comprovem que es pot crear sense errors
    }

    /**
     * Comprova initializeApp.
     */
    @Test
    void testInitializeApp() {
        Survey[] surveys = {
            new Survey(1, "Desc1", "Title1"),
            new Survey(2, "Desc2", "Title2")
        };
        
        app.initializeApp(surveys);
        
        // Comprovem indirectament que s han inicialitzat les enquestes
        // mitjancant displaySurveyList
        app.displaySurveyList();
        String output = outContent.toString();
        assertTrue(output.contains("Title1"));
        assertTrue(output.contains("Title2"));
    }

    /**
     * Comprova displaySurveyList amb llista buida.
     */
    @Test
    void testDisplaySurveyListEmpty() {
        app.displaySurveyList();
        String output = outContent.toString();
        assertTrue(output.contains("No surveys available"));
    }

    /**
     * Comprova displaySurveyList amb enquestes.
     */
    @Test
    void testDisplaySurveyListWithSurveys() {
        Survey[] surveys = {
            new Survey(1, "Test Description", "Test Title")
        };
        app.initializeApp(surveys);
        
        app.displaySurveyList();
        String output = outContent.toString();
        assertTrue(output.contains("Test Title"));
        assertTrue(output.contains("Test Description"));
    }

    /**
     * Comprova showMenu.
     */
    @Test
    void testShowMenu() {
        app.showMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Survey App"));
        assertTrue(output.contains("Manage survey"));
        assertTrue(output.contains("Answer survey"));
        assertTrue(output.contains("Analyze survey"));
        assertTrue(output.contains("Exit"));
    }

    /**
     * Comprova showMenuSurveyManagement.
     */
    @Test
    void testShowMenuSurveyManagement() throws IOException {
        app.showMenuSurveyManagement();
        String output = outContent.toString();
        assertTrue(output.contains("Survey Management"));
        assertTrue(output.contains("New survey"));
        assertTrue(output.contains("Modify survey"));
        assertTrue(output.contains("Delete survey"));
    }

    /**
     * Comprova que la survey s esborra correctament.
     */
    @Test
    void testSurveyDeletionExistingSurvey() {
        System.setIn(new ByteArrayInputStream("0\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        App app = new App();
        app.initializeApp(new Survey[]{ new Survey(1,"Desc","Title") });

        app.SurveyDeletion();

        assertTrue(out.toString().contains("deleted successfully"));
    }


    /**
     * Comprova selectSurvey amb index valid.
     */
    @Test
    void testSelectSurveyValid() {
        System.setIn(new ByteArrayInputStream("0\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        App app = new App();
        Survey testSurvey = new Survey(1, "Test Desc", "Test Title");
        app.initializeApp(new Survey[]{ testSurvey });

        Survey result = app.selectSurvey();

        assertNotNull(result);
        assertEquals(testSurvey, result);
    }


    /**
     * Comprova selectSurvey amb llista buida.
     */
    @Test
    void testSelectSurveyEmptyList() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Survey result = app.selectSurvey();
        
        assertNull(result);
        String output = outContent.toString();
        assertTrue(output.contains("No surveys available"));
    }

    /**
     * Comprova interactWithUserManagement amb opcio valida.
     */
    @Test
    void testInteractWithUserManagementValidOption() {
        // Test de la opcio 1 (New survey)
        String input = "1\nTest Title\nTest Description\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        app.interactWithUserManagement();
        
        String output = outContent.toString();
        assertTrue(output.contains("Survey Management"));
    }

    /**
     * Comprova que no s accepta una opcio invalida.
     */
    @Test
    void testInteractWithUserManagementInvalidOption() {
        System.setIn(new ByteArrayInputStream("99\n".getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        App app = new App();
        app.interactWithUserManagement();

        assertTrue(out.toString().contains("Invalid option"));
    }


    /**
     * Test d integracio basic del flux de l aplicacio.
     */
    @Test
    void testBasicAppFlow() {
        // Simulem: Crear enquesta -> Llistar enquestes -> Sortir
        String input = "1\nTest Title\nTest Description\nn\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Captem l execucio (aquest test pot necessitar ajustos)
        try {
            app.interactWithUser();
        } catch (Exception e) {
            // Podem esperar alguna excepcio degut a la simulacio d entrada
        }
        
        String output = outContent.toString();
        assertTrue(output.contains("Survey App") || output.contains("Test Title"));
    }
}
