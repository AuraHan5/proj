import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class TestAccuracy {
    public static void testAccuracyIris(int clusters[], List<Object> labels) {
        int[] setosa = new int[3];
        int[] versicolor = new int[3];
        int[] virginica = new int[3];

        int n = clusters.length;

        //count number of appearances of each species in each cluster
        for (int i = 0; i < n; ++i) {
            if ("Iris-setosa".equals(labels.get(i))) {
                ++setosa[clusters[i]];
            } 
            else if ("Iris-versicolor".equals(labels.get(i))) {
                ++versicolor[clusters[i]];
            }
            else if ("Iris-virginica".equals(labels.get(i))) {
                ++virginica[clusters[i]];
            }
        }

        double maxP = -1.0;
        double p;
        int[][] rot = {{0,1,2},{0,2,1},{1,0,2},{1,2,0},{2,0,1},{2,1,0}};
        for (int i = 0; i < 6; ++i) {
            int sum = setosa[rot[i][0]] + versicolor[rot[i][1]] +virginica[rot[i][2]];
            p = (double) sum/n;
            if (maxP < p) maxP = p;
        }
        System.out.printf("Accuracy: %.2f%%\n", maxP*100.0);
    }

    public static void testAccuracyLoanLoader(int clusters[], List<Object> labels) {
        int correctYes = 0;
        int correctNo = 0;
        int totalYes = 0;
        int totalNo = 0;

        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).equals("Y")) {
                if (clusters[i] == 1) ++correctYes;
                ++totalYes;
            }
            if (labels.get(i).equals("N")) {
                if (clusters[i] == 0) ++correctNo; 
                ++totalNo;  
            }
        }

        double accuracyYes = (double) correctYes / totalYes * 100;
        double accuracyNo = (double) correctNo / totalNo * 100;
        System.out.printf("Accuracy Yes: %.2f%%\n", accuracyYes);
        System.out.printf("Accuracy No: %.2f%%\n", accuracyNo);
        double totalAccuracy = (double) (correctNo+correctYes) / labels.size() * 100;
        System.out.printf("Accuracy Total: %.2f%%\n", totalAccuracy);
    }
}