
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
 
public class KMeans extends Algorithm {
    public KMeans(int k, int maxIterations) {
        super(k, maxIterations);
    }

    @Override
    protected Object[] calculateCentroid(List<Integer> clusterIndexes, int[] variableType, Object[][] data) {
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
                        Object cell = data[index][j];
                        if (!(cell instanceof Set<?>)) continue;
                        for (Object elem : (Set<?>) cell) {
                            if (!repetitions1.containsKey(elem)) repetitions1.put(elem,1);
                            else repetitions1.put(elem,repetitions1.get(elem)+1);
                        }
                    }
                    int sizeCluster = clusterIndexes.size();
                    Set<Object> frequents = new HashSet<>();
                    for (Object elem : repetitions1.keySet()) {
                        int val = repetitions1.get(elem);
                        if ((double)val/sizeCluster >= 0.25) {
                            frequents.add(elem);
                        }
                    }
                    reference[j] = frequents;
                    break;

                case 4: //free text
                    int chosenIndex = -1;
                    double minDistSum = Double.MAX_VALUE;

                    // Precompute embeddings for all texts in this cluster
                    Map<Integer, double[]> embeddingMap = new HashMap<>();
                    for (int index : clusterIndexes) {
                        String text = (String) data[index][j];
                        embeddingMap.put(index, WordEmbeddings.getEmbedding(text));
                    }

                    // Compute medoid using cosine distance
                    for (int index : clusterIndexes) {
                        double distSum = 0.0;
                        double[] emb1 = embeddingMap.get(index);

                        for (int index2 : clusterIndexes) {
                            if (index == index2) continue;
                            double[] emb2 = embeddingMap.get(index2);
                            distSum += 1.0 - AuxiliarMethods.cosineSimilarity(emb1, emb2); // cosine distance
                        }

                        if (distSum < minDistSum) {
                            minDistSum = distSum;
                            chosenIndex = index;
                        }
                    }
                    reference[j] = data[chosenIndex][j];
            }
        } 
        return reference;
    }
}