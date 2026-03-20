import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GestorDeDades ahora es responsable únicamente de leer/escribir JSON genérico.
 * No conoce las clases del dominio (Survey/Question/Answer). CtrlDomini
 * realiza la conversión entre estructuras genéricas y objetos de dominio.
 */
public class GestorDeDades {

    private static final String PATH = "surveys.json";
    private final Gson gson;

    public GestorDeDades() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    // Guarda estructuras JSON genéricas (listas/maps) en disco
    public void guardarRaw(List<Map<String, Object>> data) throws IOException {
        FileWriter writer = new FileWriter(PATH);
        gson.toJson(data, writer);
        writer.close();
    }

    // Carga estructuras JSON genéricas desde disco
    public List<Map<String, Object>> carregarRaw() throws IOException {
        FileReader reader = new FileReader(PATH);
        List<Map<String, Object>> raw = gson.fromJson(reader, new TypeToken<List<Map<String, Object>>>(){}.getType());
        reader.close();
        if (raw == null) return new ArrayList<>();
        return raw;
    }
}
