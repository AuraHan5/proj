
import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class TestAccuracy {
    public static double testAccuracy(int[] clusters, List<Object> labels) {
        if (clusters.length != labels.size()) {
            throw new IllegalArgumentException("Clusters and labels must have same length");
        }

        int n = clusters.length;
        int k = 0; // default if array is empty
        for (int cluster : clusters) {
            if (cluster > k) {
                k = cluster;
            }
        }
        k += 1;

        // Map true labels to indices
        Map<String, Integer> labelToIndex = new HashMap<>();
        int index = 0;

        for (Object o : labels) {
            String label = o.toString();
            if (!labelToIndex.containsKey(label)) {
                labelToIndex.put(label, index++);
            }
        }

        int L = labelToIndex.size();

        int[][] counts = new int[L][k];
        for (int i = 0; i < n; i++) {
            int j = labelToIndex.get(labels.get(i).toString());
            int pc = clusters[i];
            counts[j][pc]++;
        }

        double accuracy;
        List<int[]> perms = generatePermutations(k);
        int best = 0;
        for (int[] perm : perms) {
            int correct = 0;
            for (int j = 0; j < L; j++) {
                correct += counts[j][perm[j]];
            }
            best = Math.max(best, correct);
        }
        accuracy = best * 1.0 / n;
        
        return accuracy;
    }

    //Generate all possible combinations of cluster assignment
    private static List<int[]> generatePermutations(int n) {
        List<int[]> perms = new ArrayList<>();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        permute(arr, 0, perms);
        return perms;
    }

    private static void permute(int[] arr, int l, List<int[]> list) {
        if (l == arr.length) {
            list.add(arr.clone());
            return;
        }
        for (int i = l; i < arr.length; i++) {
            int temp = arr[l]; arr[l] = arr[i]; arr[i] = temp;
            permute(arr, l + 1, list);
            temp = arr[l]; arr[l] = arr[i]; arr[i] = temp;
        }
    }

}