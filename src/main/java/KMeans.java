import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
 
public class KMeans {
    public int k;
    public int maxIterations;
    public Object[][] centroids;
    public int[] assignation;
    public double[] min;
    public double[] max;

    /*Constructor*/
    public KMeans(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    public int getK() {
        return this.k;
    }

    /*For each row in data, the different values are considered to assign a cluster*/
    public int[] assignToCluster(Object[][] data, int[] variableType) { 
        int n = data.length;
        int numColumns = variableType.length;

        //calculate max and min values for columns with numerical values
        this.min = AuxiliarMethods.calculateMinVector(data,variableType);
        this.max = AuxiliarMethods.calculateMaxVector(data,variableType);
        
        this.centroids = initializeRandomCentroids(data,n,variableType,this.max,this.min); //Initialize k centroids
        this.assignation = new int[n];
        boolean changed;

        for (int it = 0; it < maxIterations; ++it) { //Until maxIterations is reached
            changed = false;
            //Assign for each point its nearest centroid
            for (int i = 0; i < n; ++i) {
                int nearestCentroid = findNearestCentroid(i,variableType,max,min,data);
                if (this.assignation[i] != nearestCentroid) {
                    this.assignation[i] = nearestCentroid;
                    changed = true;
                }
            }

            //Re-calculate new centroids
            calculateCentroids(data,n,variableType);

            //If centroids converge, stop the loop
            if (!changed) break; 
        }
        return assignation;
    }

    /*Calls class that implements KMeans ++ algorithm to assign initial centroids*/
    private Object[][] initializeRandomCentroids(Object[][] data, int n, int[] variableType, double[] max, double[] min) {
        KMeansPlusPlus km = new KMeansPlusPlus();
        return km.initializeCentroids(data,this.k,variableType,max,min);

    }

    /*For each point, finds the nearest centroid*/
    private int findNearestCentroid(int i, int[] variableType, double[] max, double[] min, Object[][] data) {
        double dist = Double.MAX_VALUE;
        int clusterIndex = -1; 

        for (int it = 0; it < k; ++it) { //for each centroid
            double res = CalculateDistance.compute(data[i],this.centroids[it],variableType,max,min);
            if (res < dist) { //If the current distance calculated is shorter than the one calculated before, change cluster
                dist = res;
                clusterIndex = it;
            }
        }    
        return clusterIndex;
    }

    /*Re-calculates centroids after each iteration */
    private void calculateCentroids(Object[][] data, int n, int[] variableType) {
        for (int cluster = 0; cluster < this.k; ++cluster) {
            List<Integer> clusterIndexes = new ArrayList<>(); 
            for (int i = 0; i < n; ++i) { 
                if (assignation[i] == cluster) {
                    clusterIndexes.add(i); //stores all cluster indexes for a given cluster
                }
            }
            if (clusterIndexes.isEmpty()) continue; //no points in the cluster
            this.centroids[cluster] = calculateCentroid(clusterIndexes,variableType,data);
        }
    }

    /*Calculates the most representative centroid for a given cluster*/
    private Object[] calculateCentroid(List<Integer> clusterIndexes, int[] variableType, Object[][] data) {
        int numColumns = data[0].length;
        Object[] reference = new Object[numColumns];

        for (int j = 0; j < numColumns; ++j) { //for each column in a point
            switch(variableType[j]) {
                case 0: //numerical
                    double sum = 0.0;
                    for (int index : clusterIndexes) { //for each element in the cluster
                        double val = (Double) data[index][j];
                        sum += val;
                    }
                    reference[j] = sum/clusterIndexes.size();
                    break;

                case 1: //qualitative ordered, single-choice (point[0] = optChosen, point[1] = numOptions)
                    double sum1 = 0.0;
                    int numOptions1 = 0;
                    for (int index : clusterIndexes) {
                        Object[] val1 =  (Object[]) data[index][j];
                        sum1 += (Integer) val1[0];
                        numOptions1 = (Integer) val1[1];
                    }
                    int cent = (int) Math.round((double)sum1/clusterIndexes.size());
                    reference[j] = new Object[]{cent,numOptions1};
                    break;
                    
                case 2: //qualitative unordered, single-choice
                    List<Object> choices = new ArrayList<>(); //no need to initialize the size
                    for (int index : clusterIndexes) { //for each element in the cluster
                        Object val2 = data[index][j];
                        choices.add(val2);
                    }
                    Object mode = AuxiliarMethods.calculateMode(choices);
                    reference[j] = mode;
                    break;

                case 3: //qualitative unordered, multiple-choice
                    Map<Object,Integer> repetitions1 = new HashMap<>();
                    for (int index : clusterIndexes) {
                        for (Object elem : (Set<Object>) data[index][j]) {
                            if (!repetitions1.containsKey(elem)) repetitions1.put(elem,1);
                            else repetitions1.put(elem,repetitions1.get(elem)+1);
                        }
                    }
                    int sizeCluster = clusterIndexes.size();
                    Set<Object> frequents = new HashSet<>();
                    for (Object elem : repetitions1.keySet()) {
                        int val = repetitions1.get(elem);
                        if ((double)val/sizeCluster >= 0.5) {
                            frequents.add(elem);
                        }
                    }
                    reference[j] = frequents;
                    break;

                case 4: //free text
                    int chosenIndex = -1;
                    double min_dist = Double.MAX_VALUE;
                    for (int index : clusterIndexes) { //medoid
                        double dist = 0.0;
                        for (int index2 : clusterIndexes) {
                            String s1 = (String) data[index][j];
                            String s2 = (String) data[index2][j];
                            dist += AuxiliarMethods.levenshtein(s1,s2);
                        }
                        if (dist < min_dist) {
                            chosenIndex = index;
                            min_dist = dist;
                        }
                    }
                    reference[j] = data[chosenIndex][j];                    
                    break;
            }
        } 

        return reference;
    }
}