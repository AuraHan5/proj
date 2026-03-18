import java.io.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Llegeix un fitxer CSV i converteix cada resposta al tipus d’objecte correcte segons el codi variableType[i] que s’utilitza al càlcul de distàncies.
 *
 * Tipus de dades:
 *  0 → numèrica (Double)
 *  1 → qualitative ordered, single-choice (point[0] = indexOptChosen, point[1] = numOptions)
 *  2 → qualitative unordered, single-choice
 *  3 → qualitative unordered, multiple-choice
 *  4 → free text
 */

public class ReadAnswers {
    File filename;
    Object[][] data;
    int[] answer_column_type;
    List<Object> actual_results; 
    List<Object> ids; 
    int opt;

    /*Constructor*/
    public ReadAnswers() {
        this.opt = -1;
    }

    public ReadAnswers(File filename, int opt) {
        this.filename = filename;
        this.opt = opt;
    }

    public void initializeVariableType(int[] answer_column_type) {
        this.answer_column_type = answer_column_type;
    }

    public int[] getVariableType() {
        return this.answer_column_type;
    }

    public List<Object> getIds() {
        return this.ids;
    }

    public List<Object> getLabels() {
        return this.actual_results;
    } 


    public Object[][] readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                System.out.println("Fitxer buit!");
                return null;
            }
            String secondLine = br.readLine();
            if (secondLine == null) {
                System.out.println("Fitxer buit!");
                return null;
            }

            String[] firstRow = firstLine.split("[,;]"); // accepta , o ;
            int numCols = firstRow.length;

            String[] secondRow = secondLine.split("[,;]");

            this.answer_column_type = new int[numCols-2];
            for (int i = 0; i < numCols-2; i++) {
                this.answer_column_type[i] = Integer.parseInt(secondRow[i+1].trim());
            }

            this.actual_results = new ArrayList<>();
            this.ids = new ArrayList<>();
            List<String[]> rows = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) rows.add(line.split("[,;]"));
                
            }

            int numRows = rows.size();
            this.data = new Object[numRows][this.answer_column_type.length];

            List<Object> actual_results = new ArrayList<>();
            List<Object> ids = new ArrayList<>();
            // Convertim cada valor al tipus corresponent
            for (int i = 0; i < numRows; i++) {
                String[] tokens = rows.get(i);
                for (int j = 1; j < numCols-1; j++) {
                    String token = tokens[j].trim();
                    if (token.isEmpty()) {
                        this.data[i][j-1] = null;
                        continue;
                    }

                    switch (this.answer_column_type[j-1]) {
                        case 0: // numèrica
                            this.data[i][j-1] = Double.parseDouble(token);
                            break;
                        case 1: // ordered single-choice (per exemple “2/4” o “2”)
                            if (token.equals("3+")) token = "3";
                            if (token.contains("/")) {
                                String[] parts = token.split("/");
                                this.data[i][j-1] = new Object[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
                            } else {
                                this.data[i][j-1] = new Object[]{Integer.parseInt(token), 4}; // per defecte 4 opcions
                            }
                            break;
                        case 2: // unordered single-choice
                            this.data[i][j-1] = (String) token;
                            break;
                        case 3: // unordered multiple-choice (elements separats per ;)
                            Set<String> set = new HashSet<>(Arrays.asList(token.split("\\|")));
                            this.data[i][j-1] = set;
                            break;
                        case 4: // free text
                            this.data[i][j-1] = token;
                            break;
                        default:
                            this.data[i][j-1] = token;
                    }
                }
                this.ids.add(tokens[0]);
                this.actual_results.add(tokens[numCols-1]);
            }
            return data;
        } catch (IOException e) {
            System.out.println("Error llegint el fitxer: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Error de format en alguna columna: " + e.getMessage());
            return null;
        }
    }
    
    public Object[][] processData(Object[][] data) {
        this.data = data;
        Object[] defaultValues = new Object[this.answer_column_type.length];
        double[] maxValues = new double[this.answer_column_type.length];
        int numRows = data.length;
        int numCols = data[0].length;

        for (int j = 0; j < numCols; ++j) {
            if (this.answer_column_type[j] == 0) {
                List<Double> list = new ArrayList<>();
                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] != null) list.add((Double) this.data[i][j]);
                }
                defaultValues[j] = AuxiliarMethods.calculateAverage(list);
                maxValues[j] = AuxiliarMethods.calculateMax95P(list);

                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] == null) this.data[i][j] = defaultValues[j];
                    else if ((opt == 2) && (Double) this.data[i][j] > maxValues[j]) this.data[i][j] = maxValues[j];
                }
            }
            else if (this.answer_column_type[j] == 1) {
                List<Object[]> list = new ArrayList<>();
                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] != null) list.add((Object[]) this.data[i][j]);
                }
                defaultValues[j] = AuxiliarMethods.calculateMode(list);
                maxValues[j] = 0.0;

                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] == null) this.data[i][j] = defaultValues[j];
                }
            }
            else if (this.answer_column_type[j] == 2) {
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] != null) list.add(this.data[i][j]);
                }
                defaultValues[j] = AuxiliarMethods.calculateMode(list);
                maxValues[j] = 0.0;

                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] == null) this.data[i][j] = defaultValues[j];
                }
            }
            else if (this.answer_column_type[j] == 3) {
                Map<Object,Integer> repetitions1 = new HashMap<>();
                for (int i = 0; i < numRows; ++i) {
                    for (Object elem : (Set<Object>) data[i][j]) {
                        if (!repetitions1.containsKey(elem)) repetitions1.put(elem,1);
                        else repetitions1.put(elem,repetitions1.get(elem)+1);
                    }
                }
                Set<Object> frequents = new HashSet<>();
                for (Object elem : repetitions1.keySet()) {
                    int val = repetitions1.get(elem);
                    if ((double)val/numRows >= 0.5) {
                        frequents.add(elem);
                    }
                }
                defaultValues[j] = frequents;
                maxValues[j] = 0.0; // no s’utilitza per aquest tipus
            }
            else {
                int chosenIndex = -1;
                double min_dist = Double.MAX_VALUE;
                for (int i = 0; i < numRows; ++i) { //medoid
                    double dist = 0.0;
                    for (int ii = 0; ii < numRows; ++ii) {
                        String s1 = (String) data[i][j];
                        String s2 = (String) data[ii][j];
                        dist += AuxiliarMethods.levenshtein(s1,s2);
                    }
                    if (dist < min_dist) {
                        chosenIndex = i;
                        min_dist = dist;
                    }
                }
                defaultValues[j] = data[chosenIndex][j];
            }
        }
        System.out.println("\nDades llegides correctament. Files: " + numRows + "  Columnes: " + numCols);
        return data;
    }
}