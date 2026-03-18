import java.util.Set;

public class CalculateDistance {
    /*Calculate distance between two points*/
    public static double compute(Object[] point, Object[] centroid, int[] variableType, double[] max, double[] min) { //object because different values have different data structures

        if (point == null || centroid == null || variableType == null || max == null || min == null)
            throw new IllegalArgumentException("Input arrays cannot be null");

        int n = variableType.length;
        double res = 0.0; 

        //Validate same length for all arrays
        if (point.length != n || centroid.length != n || max.length != n || min.length != n) 
            throw new IllegalArgumentException("All arrays must have equal length.");

        for (int i = 0; i < n; ++i) { //for each column in a point
            double dist = 0.0;

            //Validate non-null values
            if (point[i] == null || centroid[i] == null) {
                throw new IllegalArgumentException("Null values are not allowed.");
            }

            switch(variableType[i]) {
                case 0: //numerical
                    dist = numericDistance(point[i],centroid[i],max[i],min[i]);
                    break;

                case 1: //qualitative ordered, single-choice (point[0] = indexOptChosen, point[1] = numOptions)
                    dist = singleChoiceOrdered(point[i],centroid[i]);
                    break;

                case 2: //qualitative unordered, single-choice
                    dist = singleChoiceUnordered(point[i],centroid[i]);
                    break;

                case 3: //qualitative unordered, multiple-choice
                    dist = multipleChoiceUnordered(point[i],centroid[i]);
                    break;

                case 4: //free text
                    dist = freeText(point[i],centroid[i]);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown variable type: " + variableType[i]);
            }
            res += dist;
        }
        return res/n;
    }

    private static double numericDistance(Object point, Object centroid, double max, double min) {
        double x = (Double) point;
        double y = (Double) centroid;
        if (max == min) return 0;
        return Math.pow((x - y) / (max - min), 2); //calculate normalized distance and square it to enhance differences
    }

    private static double singleChoiceOrdered(Object point, Object centroid) {
        if (!(point instanceof Object[]) || ((Object[]) point).length != 2)
            throw new IllegalArgumentException("Invalid input format.");

        Object[] p = (Object[]) point;
        Object[] c = (Object[]) centroid;
        int pVal = (Integer) p[0];
        int numOptions = (Integer) p[1];
        int cVal = (Integer) c[0];
        if (pVal >= numOptions || cVal >= numOptions) throw new IllegalArgumentException("Incorrect indexes.");

        if (numOptions == 1) return 0;
        return Math.abs(pVal-cVal)/(double)(numOptions-1);
    }

    private static double singleChoiceUnordered(Object point, Object centroid) {
        Object p2 = point;
        Object c2 = centroid;
        if (p2.equals(c2)) return 0; //if they are the same
        else return 1;
    }

    private static double multipleChoiceUnordered(Object point, Object centroid) {
        if (!(point instanceof Set) || !(centroid instanceof Set))
            throw new IllegalArgumentException("Invalid input format");

        Set<Object> ps = (Set<Object>) point;
        Set<Object> cs = (Set<Object>) centroid;
        int intersection = 0;
        for (Object elem : ps) { //checks, for each element in ps, if it is contained in cs
            if (cs.contains(elem)) ++intersection;
        }
        double union = ps.size() + cs.size() - intersection; //checks for elements not in common
        if (union == 0) return 0;
        return (1 - ((double)intersection/union));
    }

    private static double freeText(Object point, Object centroid) {
        if (point == null && centroid == null) return 0; //minimum distance if both are empty null
        else if (point == null || centroid == null) return 1; //maximum distance if one of the two is null

        String s1 = point.toString().toLowerCase();
        String s2 = centroid.toString().toLowerCase();

        if (s1.length() == 0 && s2.length() == 0) return 0;

        return AuxiliarMethods.levenshtein(s1,s2) / (double) Math.max(s1.length(),s2.length()); //normalized levenshtein distance
    }
}