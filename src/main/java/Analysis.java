import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class Analysis{ //silouette //k means torna vector amb a quin cluster pertany cada punt
    //atributs
    public int[] clusters;

    public void Analysis(){
        int [] a = null;
    }

    public void initialize(int[] clusters){
        this.clusters = clusters;
    }

    public double silouette(int[] result, Object[][] punts, int[] type, double[] min, double[] max, int k){ //punts hauria de tenir un punt per fila
        
        double s_tot = 0;
        
        for(int i = 0; i<result.length; ++i){
            double dist_propi = 0;
            double dist_altres[] = new double[k];
            double n_propi = 0;
            double n_altres[] = new double[k];
            for(int m = 0; m<k; ++m) n_altres[m] = 0;
            for(int j = 0; j<result.length; ++j){
                if(result[i] == result[j]) {
                    dist_propi += CalculateDistance.compute(punts[i], punts[j], type, max, min);
                    ++n_propi;
                }
                else{
                    int c = result[j];
                    dist_altres[c] += CalculateDistance.compute(punts[i], punts[j], type, max, min);
                    n_altres[c]++;
                }
            }
            dist_propi = dist_propi/n_propi;
            for(int l= 0; l<k; ++l) {
                if(n_altres[l] != 0)dist_altres[l] =  dist_altres[l]/n_altres[l];
                else dist_altres[l] = Double.MAX_VALUE;
            }
            double min_altres = Double.MAX_VALUE;
            for(int l = 0; l<k; ++l){
                if(l != result[i] && dist_altres[l] < min_altres){
                    min_altres = dist_altres[l];
                }
            }
            double s = (min_altres - dist_propi)/Math.max(dist_propi, min_altres);
            s_tot += s;
        }
        s_tot = s_tot*(1.0/result.length);

        return s_tot;
        
    }
}