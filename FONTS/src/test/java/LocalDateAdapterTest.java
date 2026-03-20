/* JUnit: LocalDateAdapterTest — ensures JSON (de)serialization for LocalDate */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LocalDateAdapterTest {

    private final LocalDateAdapter adapter = new LocalDateAdapter();

    @Test
    public void serializeFaServirFormatISO() {
        JsonElement element = adapter.serialize(LocalDate.of(2023, 5, 1), LocalDate.class, null);
        assertTrue(element.isJsonPrimitive());
        assertEquals("2023-05-01", element.getAsString());
    }

    @Test
    public void serializeNullRetornaJsonNull() {
        JsonElement element = adapter.serialize(null, LocalDate.class, null);
        assertTrue(element.isJsonNull());
        assertSame(JsonNull.INSTANCE, element);
    }

    @Test
    public void deserializeISOConstrueixLocalDate() {
        LocalDate date = adapter.deserialize(new JsonPrimitive("2024-02-10"), LocalDate.class, null);
        assertEquals(LocalDate.of(2024, 2, 10), date);
    }

    @Test
    public void deserializeNullPermetNull() {
        LocalDate date = adapter.deserialize(JsonNull.INSTANCE, LocalDate.class, null);
        assertNull(date);
    }

    @Test
    public void deserializeInvalidLlençaExcepcio() {
        assertThrows(DateTimeParseException.class, () ->
                adapter.deserialize(new JsonPrimitive("2024-99-99"), LocalDate.class, null));
    }
}
