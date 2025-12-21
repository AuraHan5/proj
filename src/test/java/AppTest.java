import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        
        // Comprovem que s han inicialitzat les enquestes
        assertEquals(2, app.getSurveyList().size());
        assertEquals("Title1", app.getSurveyList().get(0).getTitle());
        assertEquals("Title2", app.getSurveyList().get(1).getTitle());
    }

    /**
     * Comprova SurveyCreation.
     */
    @Test
    void testSurveyCreation() {
        app.SurveyCreation("Test Title", "Test Description");
        
        assertEquals(1, app.getSurveyList().size());
        Survey created = app.getSurveyList().get(0);
        assertEquals("Test Title", created.getTitle());
        assertEquals("Test Description", created.getDescription());
        assertEquals(1, created.getId());
        assertEquals(created, app.getCurrentSurvey());
    }

    /**
     * Comprova afegirPreguntaSimple.
     */
    @Test
    void testAfegirPreguntaSimple() {
        app.SurveyCreation("Test Survey", "Desc");
        app.afegirPreguntaSimple("What is your name?", "Text Lliure");
        
        assertEquals(1, app.getNumPreguntesActual());
        Question q = app.getPreguntaActual(0);
        assertNotNull(q);
        assertTrue(q instanceof Question.FreeTextQuestion);
        assertEquals("What is your name?", q.getEnunciat());
    }

    /**
     * Comprova afegirPreguntaAmbOpcions.
     */
    @Test
    void testAfegirPreguntaAmbOpcions() {
        app.SurveyCreation("Test Survey", "Desc");
        List<String> opcions = new ArrayList<>();
        opcions.add("Option A");
        opcions.add("Option B");
        app.afegirPreguntaAmbOpcions("Choose one", "Opció Única Desordenada", opcions, -1);
        
        assertEquals(1, app.getNumPreguntesActual());
        Question q = app.getPreguntaActual(0);
        assertNotNull(q);
        assertTrue(q instanceof Question.SingleChoiceQuestionUnordered);
    }

    /**
     * Comprova modificarPregunta.
     */
    @Test
    void testModificarPregunta() {
        app.SurveyCreation("Test Survey", "Desc");
        app.afegirPreguntaSimple("Old question", "Text Lliure");
        
        List<String> newOpcions = new ArrayList<>();
        newOpcions.add("Yes");
        newOpcions.add("No");
        app.modificarPregunta(0, "New question", "Opció Única Desordenada", newOpcions, -1);
        
        Question q = app.getPreguntaActual(0);
        assertEquals("New question", q.getEnunciat());
        assertTrue(q instanceof Question.SingleChoiceQuestionUnordered);
    }

    /**
     * Comprova afegirResposta.
     */
    @Test
    void testAfegirResposta() {
        app.SurveyCreation("Test Survey", "Desc");
        app.afegirPreguntaSimple("Question", "Text Lliure");
        
        Answer answer = new Answer();
        // Assuming Answer has a way to set responses, but since it's not shown, just test the method exists
        app.afegirResposta(answer);
        
        assertEquals(1, app.getCurrentSurvey().getAnswers().size());
    }

    /**
     * Comprova getSurveyList.
     */
    @Test
    void testGetSurveyList() {
        assertNotNull(app.getSurveyList());
        assertTrue(app.getSurveyList().isEmpty());
        
        app.SurveyCreation("Test", "Desc");
        assertEquals(1, app.getSurveyList().size());
    }

    /**
     * Comprova getCurrentSurvey.
     */
    @Test
    void testGetCurrentSurvey() {
        assertNull(app.getCurrentSurvey());
        
        app.SurveyCreation("Test", "Desc");
        assertNotNull(app.getCurrentSurvey());
    }

    /**
     * Comprova setEnquestaActual.
     */
    @Test
    void testSetEnquestaActual() {
        Survey s = new Survey(1, "Desc", "Title");
        app.setEnquestaActual(s);
        assertEquals(s, app.getCurrentSurvey());
    }

    /**
     * Comprova eliminarEnquesta.
     */
    @Test
    void testEliminarEnquesta() {
        Survey s = new Survey(1, "Desc", "Title");
        app.getSurveyList().add(s);
        app.setEnquestaActual(s);
        
        app.eliminarEnquesta(s);
        assertFalse(app.getSurveyList().contains(s));
        assertNull(app.getCurrentSurvey());
    }

    /**
     * Comprova eliminarPregunta.
     */
    @Test
    void testEliminarPregunta() {
        app.SurveyCreation("Test", "Desc");
        app.afegirPreguntaSimple("Question", "Text Lliure");
        
        assertEquals(1, app.getNumPreguntesActual());
        app.eliminarPregunta(0);
        assertEquals(0, app.getNumPreguntesActual());
    }

    /**
     * Comprova afegirPreguntaBuidaAlFinal.
     */
    @Test
    void testAfegirPreguntaBuidaAlFinal() {
        app.SurveyCreation("Test", "Desc");
        
        app.afegirPreguntaBuidaAlFinal("Opció Única Desordenada");
        assertEquals(1, app.getNumPreguntesActual());
        Question q = app.getPreguntaActual(0);
        assertEquals("Nova Pregunta", q.getEnunciat());
    }
}
