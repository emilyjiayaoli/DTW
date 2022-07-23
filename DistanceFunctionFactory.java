public class DistanceFunctionFactory {
   public static DistanceFunction EUCLIDEAN_DIST_FN = new EuclideanDistance();
   public static DistanceFunction getEuclideanDist() { return EUCLIDEAN_DIST_FN; }
}