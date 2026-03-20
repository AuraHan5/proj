/* JUnit: GestorDeDadesTest — verifies JSON persistence load/save round-trips */
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorDeDadesTest {

    private final Path jsonPath = Path.of("surveys.json");

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(jsonPath);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(jsonPath);
    }

    @Test
    public void guardarICarregarFentRoundTrip() throws Exception {
        GestorDeDades gestor = new GestorDeDades();

        Map<String, Object> entrada = new HashMap<>();
        entrada.put("name", "demo");
        entrada.put("count", 3);
        entrada.put("date", LocalDate.of(2024, 1, 2));

        gestor.guardarRaw(List.of(entrada));

        assertTrue(Files.exists(jsonPath), "S hauria de crear surveys.json al directori de treball");

        List<Map<String, Object>> carregat = gestor.carregarRaw();
        assertEquals(1, carregat.size());
        Map<String, Object> item = carregat.get(0);
        assertEquals("demo", item.get("name"));
        assertEquals(3.0, item.get("count"));
        assertEquals("2024-01-02", item.get("date"));
    }

    @Test
    public void carregarRawQuanFitxerConteNullRetornaLlistaBuida() throws Exception {
        Files.writeString(jsonPath, "null");

        GestorDeDades gestor = new GestorDeDades();
        List<Map<String, Object>> carregat = gestor.carregarRaw();

        assertNotNull(carregat);
        assertTrue(carregat.isEmpty());
    }

    @Test
    public void carregarRawSenseFitxerLlençaIOException() {
        GestorDeDades gestor = new GestorDeDades();
        assertThrows(IOException.class, gestor::carregarRaw);
    }
}
