/* JUnit: CtrlDominiTest — basic construction and initialization checks */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CtrlDominiTest {

    private CtrlDomini cd;

    @BeforeEach
    public void setUp() {
        cd = new CtrlDomini();
    }

    /**
     * Comprova que el constructor funciona correctament.
     */
    @Test
    public void testConstructor() {
        assertNotNull(cd);
    }

    /**
     * Comprova que initializeCtrlDomini funciona correctament.
     */
    @Test
    public void testInitializeCtrlDomini() {
        Survey[] surveys = new Survey[0];
        cd.initializeCtrlDomini(surveys);

        // No podem verificar directament, pero almenys no hauria de fallar
        assertNotNull(cd);
    }

}