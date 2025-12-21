
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
    private File filename;
    private Object[][] data;
    private int[] answer_column_type;
    private List<Object> actual_results; 
    private List<Object> ids; 
    private Double[][] limits; //limit[0] = limit inferior, limit[1] = limit superior
    private Set<String> words = new HashSet<>();
    private double[] maxValues;
    private double[] minValues;

    /*Constructor*/
    public ReadAnswers() {

    }

    public ReadAnswers(File filename) {
        this.filename = filename;
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

    public Double[][] getIQR() {
        return this.limits;
    }

    public double[] getMaxValue() {
        return this.maxValues;
    }

    public double[] getMinValue() {
        return this.minValues;
    }


    public Object[][] readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                System.out.println("Empty file!");
                return null;
            }
            String secondLine = br.readLine();
            if (secondLine == null) {
                System.out.println("Empty file!");
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
            System.out.println("Error reading the file: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Format error in some row: " + e.getMessage());
            return null;
        }
    }
    
    public Object[][] processData(Object[][] data) {
        System.out.print("\nAnalyzing your answers...\n");
        this.data = data;
        Object[] defaultValues = new Object[this.answer_column_type.length];
        this.maxValues = new double[this.answer_column_type.length];
        this.minValues = new double[this.answer_column_type.length];
        int numRows = data.length;
        int numCols = data[0].length;

        this.limits = new Double[this.answer_column_type.length][2];

        for (int j = 0; j < numCols; ++j) {
            if (this.answer_column_type[j] == 0) {
                List<Double> list = new ArrayList<>();
                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] != null) list.add((Double) this.data[i][j]);
                }
                defaultValues[j] = AuxiliarMethods.calculateAverage(list);
                this.limits[j] = AuxiliarMethods.calculateIQR(list);
                this.minValues[j] = this.limits[j][0];
                this.maxValues[j] = this.limits[j][1];

                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] == null) this.data[i][j] = defaultValues[j];
                    else if ((Double) this.data[i][j] > this.maxValues[j]) this.data[i][j] = this.maxValues[j];
                    else if ((Double) this.data[i][j] < this.minValues[j]) this.data[i][j] = this.minValues[j];
                }
            }
            else if (this.answer_column_type[j] == 1) {
                List<Object[]> list = new ArrayList<>();
                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] != null) list.add((Object[]) this.data[i][j]);
                } 
                defaultValues[j] = AuxiliarMethods.calculateMode(list);
                this.maxValues[j] = 0.0;

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
                this.maxValues[j] = 0.0;

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
                this.maxValues[j] = 0.0; // no s’utilitza per aquest tipus

                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] == null) this.data[i][j] = defaultValues[j];
                }
            }
            else {
                for (int i = 0; i < numRows; i++) {
                    if (data[i][j] != null) {
                        String[] tokens = ((String) data[i][j]).toLowerCase().split(" ");
                        for (String token : tokens) {
                            words.add(token);
                        }
                    }
                }
                WordEmbeddings.loadEmbeddings(words);
                words.clear();

                List<double[]> embeddings = new ArrayList<>();
                for (int i = 0; i < numRows; i++) {
                    embeddings.add(WordEmbeddings.getEmbedding((String)data[i][j]));
                }

                // Compute medoid
                int chosenIndex = -1;
                double minDistSum = Double.MAX_VALUE;
                for (int i = 0; i < embeddings.size(); i++) {
                    double distSum = 0.0;
                    for (int k = 0; k < embeddings.size(); k++) {
                        if (i == k) continue;
                        distSum += 1.0 - AuxiliarMethods.cosineSimilarity(embeddings.get(i), embeddings.get(k));
                    }
                    if (distSum < minDistSum) {
                        minDistSum = distSum;
                        chosenIndex = i;
                    }
                }
                defaultValues[j] = data[chosenIndex][j]; // medoid text

                for (int i = 0; i < numRows; ++i) {
                    if (this.data[i][j] == null) this.data[i][j] = defaultValues[j];
                }
                            
            }
        }
        //System.out.println("\nData read correctly. Rows: " + numRows + "  Columns: " + numCols);
        return data;
    }
}