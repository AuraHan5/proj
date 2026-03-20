import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class CtrlDominiFullInfoFormatTest {

    @Test
    public void testFullInfoFormatAfterSurveyCreation() {
        CtrlDomini ctrl = new CtrlDomini(false);
        ctrl.SurveyCreation("Titol prova", "Descripcio prova");
        List<String> infos = ctrl.getSurveysFullInfo();
        assertEquals(1, infos.size());
        String info = infos.get(0);
        assertTrue(info.startsWith("ID: "), "Info should start with ID: ");
        assertTrue(info.contains(" | "), "Info should contain separator ' | '");
        assertTrue(info.contains(" - "), "Info should contain ' - ' between title and description");
    }
}
