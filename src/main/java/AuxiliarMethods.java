import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

 
public class AuxiliarMethods {
    /*Calculates levenshtein distance between two given strings*/
    public static int levenshtein(String s1, String s2) {
        //base cases
        if (s1.isEmpty()) return s2.length();
        if (s2.isEmpty()) return s1.length();

        //recursive cases
        if (s1.charAt(0) == s2.charAt(0)) { //if chars match
            return levenshtein(s1.substring(1), s2.substring(1));
        }
        else {
            int del = levenshtein(s1.substring(1), s2) + 1; //deletion
            int ins = levenshtein(s1,s2.substring(1)) + 1; //insertion
            int mod = levenshtein(s1.substring(1), s2.substring(1)) + 1; //modification
            int min = Math.min(del, Math.min(ins,mod));
            return min;
        }
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

}