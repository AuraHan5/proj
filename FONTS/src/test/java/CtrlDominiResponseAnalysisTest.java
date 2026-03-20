import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class CtrlDominiResponseAnalysisTest {

    @Test
    public void testLoanTrainUsesK2AndProducesResult() throws IOException {
        CtrlDomini ctrl = new CtrlDomini(false);
        String path = "data/respostes/loan-train.csv";
        String result = ctrl.tractarFerAnalisiRespostes(path);
        assertNotNull(result);
        assertTrue(result.contains("Analysis completed with k=2"), "Should use k=2 for loan-train");
        assertTrue(result.contains("Accuracy:"), "Should include Accuracy in the result");
    }

    @Test
    public void testInvalidPathReturnsError() throws IOException {
        CtrlDomini ctrl = new CtrlDomini(false);
        String path = "data/respostes/this-file-does-not-exist.csv";
        String result = ctrl.tractarFerAnalisiRespostes(path);
        assertEquals("Error reading the file.", result);
    }
}
