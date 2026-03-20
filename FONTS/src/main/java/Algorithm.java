import java.util.ArrayList;
import java.util.List;

public abstract class Algorithm {
    public int k;
    public int maxIterations;
    public Object[][] centroids;
    public int[] assignation;
    public double[] min;
    public double[] max;
    public double[][] quartiles;

    protected Algorithm(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    public int getK() {
        return this.k;
    }

    /*For each row in data, the different values are considered to assign a cluster*/
    public int[] assignToCluster(Object[][] data, int[] variableType) { 
        //calculate max and min values for columns with numerical values
        this.min = AuxiliarMethods.calculateMinVector(data,variableType);
        this.max = AuxiliarMethods.calculateMaxVector(data,variableType);

        int n = data.length;
        
        this.centroids = initializeRandomCentroids(data,n,variableType); //Initialize k centroids
        this.assignation = new int[n];
        boolean changed;

        for (int it = 0; it < maxIterations; ++it) { //Until maxIterations is reached
            changed = false;
            //Assign for each point its nearest centroid
            for (int i = 0; i < n; ++i) {
                int nearestCentroid = findNearestCentroid(i,variableType,data);
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
        return this.assignation;
    }

    /*Calls class that implements KMeans ++ algorithm to assign initial centroids*/
    private Object[][] initializeRandomCentroids(Object[][] data, int n, int[] variableType) {
        KMeansPlusPlus km = new KMeansPlusPlus();
        return km.initializeCentroids(data,this.k,variableType,this.max,this.min);

    }

    /*For each point, finds the nearest centroid*/
    private int findNearestCentroid(int i, int[] variableType, Object[][] data) {
        double dist = Double.MAX_VALUE;
        int clusterIndex = -1; 

        for (int it = 0; it < k; ++it) { //for each centroid
            double res = CalculateDistance.compute(data[i],this.centroids[it],variableType,this.max,this.min);
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
    protected abstract Object[] calculateCentroid(List<Integer> clusterIndexes, int[] variableType, Object[][] data);
}