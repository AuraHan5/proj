/* JUnit: CtrlDominiHeadlessTest — exercises domain logic without UI dependencies */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Proves bàsiques de CtrlDomini sense UI (headless).
 */
public class CtrlDominiHeadlessTest {

    private CtrlDomini ctrl;

    @BeforeEach
    public void setup() {
        ctrl = new CtrlDomini(false); // no carreguem de disc
    }

    /**
     * Comprova que es pot crear una enquesta i queda seleccionada com a currentSurvey.
     */
    @Test
    public void testSurveyCreationSetsCurrent() {
        ctrl.SurveyCreation("titol", "desc");
        List<String> titles = ctrl.getSurveyTitles();
        assertEquals(1, titles.size());
        assertTrue(titles.get(0).contains("titol"));
    }

    /**
     * Comprova que afegir preguntes simples i amb opcions incrementa el nombre de preguntes.
     */
    @Test
    public void testAddQuestionsToCurrentSurvey() {
        ctrl.SurveyCreation("titol", "desc");

        ctrl.afegirPreguntaSimple("Q1", "Numèrica");
        ArrayList<String> opcions = new ArrayList<>();
        opcions.add("A");
        opcions.add("B");
        ctrl.afegirPreguntaAmbOpcions("Q2", "Opció Única Desordenada", opcions, -1);

        Survey current = ctrl.getCurrentSurvey();
        assertNotNull(current);
        assertEquals(2, current.getQuestions().size());
    }

}
