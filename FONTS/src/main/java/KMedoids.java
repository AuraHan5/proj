import java.util.List;

public class KMedoids extends Algorithm {
    public KMedoids(int k, int maxIterations) {
        super(k, maxIterations);
    }

    @Override
    protected Object[] calculateCentroid (List<Integer> clusterIndexes, int[] variableType, Object[][] data) {
        double minDistance = Double.MAX_VALUE;;
        Object[] reference = data[clusterIndexes.get(0)];

        for (Integer i : clusterIndexes) {
            double dist = 0;
            for (Integer ii : clusterIndexes)
            if (i != ii) {
                dist += CalculateDistance.compute(data[i],data[ii],variableType,this.max,this.min);
            }  

            if (dist < minDistance) {
                minDistance = dist;
                reference = data[i];
            }          
        }
        return reference;
    }
}