
import java.io.*;
import java.util.*;

public class KMeansDriver {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose survey answers or dataset between the following:\n");

        //Folder with all the files
        File folder = new File("data/respostes");
        //List with file titles
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("The file was not found in ../data/respostes");
        }
 
        //List the files numbered
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println((i+1) + ". " + files[i].getName());
            }
        }

        System.out.print("\nPlease select a valid file number: ");
        //Read chosen file number
        int opt = sc.nextInt();
        sc.nextLine(); //clean line break
        File filename = files[opt - 1];
        System.out.println("You've chosen: " + filename.getName());
        String fname = filename.getName().toLowerCase();

        System.out.print("\nChoose the value of k: ");
        int k = sc.nextInt();

        long startTime = System.nanoTime();
        ReadAnswers ra = new ReadAnswers(filename);
        Object[][] tempData = ra.readFile();
        Object[][] data = ra.processData(tempData);
        int[] variableType = ra.getVariableType();
        List<Object> ids = ra.getIds();
        List<Object> actual_results = ra.getLabels();

        Algorithm alg;

        //analysis with KMeans
        alg = new KMeans(k, 200);
        int[] clusters = alg.assignToCluster(data,variableType);
        
        //analysis with KMedoids
        alg = new KMedoids(k,200);
        int[] clusters1 = alg.assignToCluster(data,variableType);

        long endTime = System.nanoTime();
        System.out.println("Clustering took " + (endTime - startTime) / 1e9 + " seconds");
        
        double accuracyKMedoids = TestAccuracy.testAccuracy(clusters1,actual_results);
        double accuracyKMeans = TestAccuracy.testAccuracy(clusters,actual_results);    
        
        double accuracy;
        if (accuracyKMeans > accuracyKMedoids) accuracy = accuracyKMeans;
        else accuracy = accuracyKMedoids;
        System.out.printf("Accuracy: %.2f%%\n", accuracy * 100.0);

        String outputFile = "results.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < clusters.length; i++) {
                String line1 = ids.get(i).toString()+ "\t" + clusters[i] + "\t" + actual_results.get(i).toString();
                writer.write(line1);
                writer.newLine();
            }
            System.out.println("Results saved to " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}