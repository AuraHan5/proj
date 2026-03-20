import java.io.*;
import java.util.*;

public class WordEmbeddings {
    private static Map<String, double[]> embeddings = new HashMap<>(); //holds words and their corresponding embeddings
    private static boolean loaded = false;

    public static void setEmbedding(String word, double[] vector) {
        embeddings.put(word, vector);
    }

    /*Load words appearing in the data into embeddings*/
    public static void loadEmbeddings(Set<String> words) {
        if (!loaded) embeddings.clear(); 

        // Resolve embeddings file path with fallbacks
        String[] candidates = new String[] {
            "glove.6B.50d.txt",
            "src/main/resources/glove.6B.50d.txt",
            "FONTS/src/main/resources/glove.6B.50d.txt",
            "build/resources/main/glove.6B.50d.txt",
            "FONTS/build/resources/main/glove.6B.50d.txt"
        };

        BufferedReader br = null;
        for (String p : candidates) {
            File f = new File(p);
            if (f.exists() && f.isFile()) {
                try {
                    br = new BufferedReader(new FileReader(f));
                    break;
                } catch (IOException ignored) {
                    // try next candidate
                }
            }
        }

        if (br == null) {
            // Try classpath resource as a last resort
            InputStream is = WordEmbeddings.class.getClassLoader().getResourceAsStream("glove.6B.50d.txt");
            if (is != null) {
                br = new BufferedReader(new InputStreamReader(is));
            }
        }

        if (br == null) {
            throw new RuntimeException(new FileNotFoundException("glove.6B.50d.txt not found in expected locations"));
        }

        try (BufferedReader reader = br) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String word = parts[0];

                //skip if it has already been added
                if (embeddings.containsKey(word)) continue;

                //load words that appear in the data
                if (!words.contains(word)) continue;

                double[] vec = new double[50];
                for (int i = 1; i <= 50; i++) {
                    vec[i-1] = Double.parseDouble(parts[i]);
                }

                embeddings.put(word, vec);
            }
            loaded = true;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*Convert string into embedding vector*/
    public static double[] getEmbedding(String text) {
        String[] tokens = text.toLowerCase().split(" ");
        double[] res = new double[50];
        int count = 0;

        for (String token : tokens) {
            double[] vec = embeddings.get(token); //get the embedding for a specific word
            if (vec != null) { //if an embedding exists
                for (int i = 0; i < 50; i++) res[i] += vec[i];
                count++;
            }
        }
        if (count == 0) return res; //none of the words have embedding available
        for (int i = 0; i < 50; i++) res[i] /= count;

        return res;
    }
}
