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
            System.out.println("No s'han trobat fitxers a ../data/respostes");
        }
 
        //List the files numbered
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println((i+1) + ". " + files[i].getName());
            }
        }

        System.out.print("\nIntrodueix el número del fitxer: ");
        //Read chosen file number
        int opt = sc.nextInt();
        sc.nextLine(); //clean line break
        File filename = files[opt - 1];
        System.out.println("Has triat: " + filename.getName());
        String fname = filename.getName().toLowerCase();

        int num = 0;
        if (fname.contains("iris")) num = 1;
        else if (fname.contains("loan")) num = 2;


        ReadAnswers readAnswers = new ReadAnswers(filename,num);
        Object[][] tempData = readAnswers.readFile();
        Object[][] data = readAnswers.processData(tempData);

        int[] variableType = readAnswers.getVariableType();

        System.out.print("\nChoose the value of k: ");
        int k = sc.nextInt();

        KMeans kmeans = new KMeans(k, 200);
        long startTime = System.nanoTime();
        int[] clusters = kmeans.assignToCluster(data,variableType);
        long endTime = System.nanoTime();
        System.out.println("Clustering took " + (endTime - startTime) / 1e9 + " seconds");

        List<Object> ids = readAnswers.getIds();
        List<Object> actual_results = readAnswers.getLabels();

        if (fname.contains("iris")) TestAccuracy.testAccuracyIris(clusters,actual_results);
        else if (fname.contains("loan")) TestAccuracy.testAccuracyLoanLoader(clusters,actual_results);            

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