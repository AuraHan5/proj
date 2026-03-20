public class Analysis { //silhouette //k means torna vector amb a quin cluster pertany cada punt

    public static double silhouette(int[] result, Object[][] punts, int[] type, double[] min, double[] max, int k){ //punts hauria de tenir un punt per fila
        if (result == null || punts == null || type == null || min == null || max == null) {
            throw new IllegalArgumentException("Input arrays cannot be null");
        }
        if (punts.length != result.length) {
            throw new IllegalArgumentException("Points and cluster arrays must have same length");
        }
        if (type.length != min.length || type.length != max.length) {
            throw new IllegalArgumentException("Type, min, and max arrays must have same length");
        }

        
        double s_tot = 0;
        int valid_points = 0;
        
        for(int i = 0; i < result.length; ++i){
            double a_i = 0; // average distance to other points in same cluster
            double[] b_candidates = new double[k]; // average distances to other clusters
            int count_same_cluster = 0;
            int[] count_other_clusters = new int[k];
            
            // Initialize arrays
            for(int m = 0; m < k; ++m) {
                b_candidates[m] = 0;
                count_other_clusters[m] = 0;
            }
            
            // Calculate distances
            for(int j = 0; j < result.length; ++j){
                if(i == j) continue; // Skip self
                
                double dist = CalculateDistance.compute(punts[i], punts[j], type, max, min);
                
                if(result[i] == result[j]) {
                    // Same cluster
                    a_i += dist;
                    count_same_cluster++;
                } else {
                    // Different cluster
                    int c = result[j];
                    b_candidates[c] += dist;
                    count_other_clusters[c]++;
                }
            }
            
            // Calculate a(i)
            if(count_same_cluster == 0) {
                // Only one point in cluster, silhouette undefined, skip this point
                continue;
            }
            a_i /= count_same_cluster;
            
            // Calculate b(i) - minimum average distance to other clusters
            double b_i = Double.MAX_VALUE;
            for(int l = 0; l < k; ++l){
                if(l != result[i] && count_other_clusters[l] > 0) {
                    double avg_dist = b_candidates[l] / count_other_clusters[l];
                    if(avg_dist < b_i) {
                        b_i = avg_dist;
                    }
                }
            }
            
            if(b_i == Double.MAX_VALUE) {
                // No other clusters with points, skip this point
                continue;
            }
            
            // Calculate silhouette coefficient for this point
            double s_i = (b_i - a_i) / Math.max(a_i, b_i);
            s_tot += s_i;
            valid_points++;
        }
        
        if(valid_points == 0) {
            return 0.0; // No valid silhouette calculations
        }
        
        return s_tot / valid_points;
    }
}