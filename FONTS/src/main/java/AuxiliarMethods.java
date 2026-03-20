import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
 
public class AuxiliarMethods {
    /*Calculates levenshtein distance between two given strings*/
    public static int levenshtein(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                    Math.min(
                        dp[i - 1][j] + 1,     // deletion
                        dp[i][j - 1] + 1      // insertion
                    ),
                    dp[i - 1][j - 1] + cost  // substitution
                );
            }
        }

        return dp[n][m];
    }


    /*Calculates the most frequent value in a list of elements*/
    public static <T> T calculateMode(List<T> choices) { //MADE GENERIC
        Map<T,Integer> repetitions = new HashMap<>();
        for (T elem : choices) {
            if (elem == null) continue;
            if (!repetitions.containsKey(elem)) repetitions.put(elem,1); //if element was not present, we add it
            else repetitions.put(elem,repetitions.get(elem)+1); //otherwise, increase number of appearances by one
        }
        int max = -1;
        T mode = null;
        for (T elem : repetitions.keySet()) { //find the element with the most appearances
            int val = repetitions.get(elem);
            if (val > max) { 
                max = val;
                mode = elem;
            }
        }
        return mode;
    }

    /*Calculates the average of a list of elements*/
    public static Double calculateAverage(List<Double> data) {
        int count = 0;
        double sum = 0.0;
        for (Double val : data) {
            if (val != null) {
                sum += val;
                ++count;
            }
        }
        if (count == 0) return null;
        return sum/count;
    }

    /*Calculates the minimum values of all numeric columns in a data array*/
    public static double[] calculateMinVector(Object[][] data, int[] variableType) { 
        int n = data.length;
        int numColumns = variableType.length;
        double[] min = new double[numColumns];
    
        for (int j = 0; j < numColumns; ++j) {
            if (variableType[j] == 0) {
                double minVal = Double.MAX_VALUE; 
                for (int i = 0; i < n; ++i) { //for each point 
                    double val = (Double) data[i][j];
                    if (val < minVal) minVal = val;
                }
                min[j] = minVal;
            }
            else min[j] = -1; 
        }
        return min;
    }

    /*Calculates the maximum values of all numeric columns in a data array*/
    public static double[] calculateMaxVector(Object[][] data, int[] variableType) {
        int n = data.length;
        int numColumns = variableType.length;
        double[] max = new double[numColumns];
    
        for (int j = 0; j < numColumns; ++j) {
            if (variableType[j] == 0) {
                double maxVal = -Double.MAX_VALUE;
                for (int i = 0; i < n; ++i) { //for each point 
                    double val = (Double) data[i][j];
                    if (val > maxVal) maxVal = val;
                }
                max[j] = maxVal;
            }
            else max[j] = -1; 
        }
        return max;
    }

    /*Calculates the value located in the 95th percentile for a given dataset*/
    public static Double calculateMax95P(List<Double> list) {
        int count = 0;
        List<Double> validList = new ArrayList<>();

        for (Double d : list) {
            if (d != null) {
                ++count;
                validList.add(d);
            }
        }
        if (count == 0) return null; //no elements in the list
 
        double[] data = new double[count];
        for (int i = 0; i < validList.size(); ++i) {
            data[i] = validList.get(i);
        }
        java.util.Arrays.sort(data); //sort all values in ascending order
        
        int n = data.length;
        double pos = 0.95 * (n - 1);
        int index = (int) Math.floor(pos);
        return data[index];
    }

    public static Double[] calculateIQR (List<Double> list) {
        int count = 0;
        List<Double> validList = new ArrayList<>();

        for (Double d : list) {
            if (d != null) {
                ++count;
                validList.add(d);
            }
        }
        if (count == 0) return null; //no elements in the list

        double[] data = new double[count];
        for (int i = 0; i < validList.size(); ++i) {
            data[i] = validList.get(i);
        }
        java.util.Arrays.sort(data); //sort all values in ascending order

        double q1 = data[count/4];
        double q3 = data[3*count/4];
        double iqr = q3 - q1;

        Double[] limits = new Double[2]; // limits[0] = limitInferior, limits[1] = limitsSuperior

        limits[0] = q1 - 3*iqr;
        limits[1] = q3 + 3*iqr;
        return limits;
    }

    public static double[] calculateQuartiles(List<Double> list) {
        int count = 0;
        List<Double> validList = new ArrayList<>();

        for (Double d : list) {
            if (d != null) {
                ++count;
                validList.add(d);
            }
        }
        if (count == 0) return null; //no elements in the list

        double[] data = new double[count];
        for (int i = 0; i < validList.size(); ++i) {
            data[i] = validList.get(i);
        }
        java.util.Arrays.sort(data);
        double[] quartiles = new double[3];
        quartiles[0] = data[count/4];
        quartiles[1] = data[2*count/4];
        quartiles[2] = data[3*count/4];
        return quartiles;
    }

    public static Double calculateMedian (List<Double> list) {
        int count = 0;
        List<Double> validList = new ArrayList<>();

        for (Double d : list) {
            if (d != null) {
                ++count;
                validList.add(d);
            }
        }
        if (count == 0) return null; //no elements in the list

        double[] data = new double[count];
        for (int i = 0; i < validList.size(); ++i) {
            data[i] = validList.get(i);
        }
        java.util.Arrays.sort(data); //sort all values in ascending order
        
        int n = data.length;

        if (n%2 == 0) return (data[n/2 - 1]+ data[(n/2)])/2.0;
        else return data[n/2];
    }

    public static double cosineSimilarity(double[] vec1, double[] vec2) {
        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }
        if (norm1 == 0 || norm2 == 0) return 0.0; // handle zero vectors
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}