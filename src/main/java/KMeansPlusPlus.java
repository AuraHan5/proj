import java.util.Random;

public class KMeansPlusPlus {
    public Object[][] initializeCentroids(Object[][] data, int k, int[] variableType, double[] max, double[] min) {
        int n = data.length;
        Object[][] centroids = new Object[k][];

        //Initialize first centroid as random int from i to n
        Random random = new Random(42);
        int first_index = random.nextInt(n);
        centroids[0] = data[first_index];
        
        //Initialize remaining centroids
        for (int i = 1; i < k; ++i) {
            double[] distances = calculateMinDistances(data,centroids,i,variableType,max,min);
            int index = chooseNextCentroid(distances,random);
            centroids[i] = data[index];
        }
        return centroids;
    }

    /*Calculate minimum distance from each point to the closest existing centroid*/
    private double[] calculateMinDistances(Object[][] data, Object[][] centroids, int centroidsCount, int[] variableType, double[] max, double[] min) {
        int n = data.length;
        //Empty vector minDistances  
        double[] minDistances = new double[n];

        for (int i = 0; i < n; i++) {
            //Assign the max double to minDistance to compare
            double minDistance = Double.MAX_VALUE;

            //Find minimum distance to any existing centroid
            for (int j = 0; j < centroidsCount; j++) {
                double distance = CalculateDistance.compute(data[i],centroids[j],variableType,max,min);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
            minDistances[i] = minDistance;
        }
        return minDistances;
    } 

    /*Select next centroid index based on squared distance probability distribution*/
    private int chooseNextCentroid(double[] distances, Random random) {
        int n = distances.length;
        double[] squaredDistances = new double[n];
        double totalSquaredDistance = 0.0;

        //Calculate squared distances and total
        for (int i = 0; i < n; i++) {
            squaredDistances[i] = distances[i] * distances[i];
            totalSquaredDistance += squaredDistances[i];
        }

        //Create cumulative probability distribution
        double[] cumulativeProb = new double[n];
        double cumulative = 0.0;
        for (int i = 0; i < n; i++) {
            cumulative += squaredDistances[i] / totalSquaredDistance;
            cumulativeProb[i] = cumulative;
        }

        //Select random point based on probability distribution
        double rand = random.nextDouble();
        for (int i = 0; i < n; i++) {
            if (rand <= cumulativeProb[i]) {
                return i;
            }
        }
        return n - 1; //if nothing else is returned, return last index
    }
}