import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Integer;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.TreeMap;

public class WarpPath {
   // DATA
   private ArrayList tsIindexes;   // ArrayList of Integer
   private ArrayList tsJindexes;   // ArrayList of Integer

   // CONSTRUCTORS
   public WarpPath() {
      tsIindexes = new ArrayList();
      tsJindexes = new ArrayList();
   }

   public WarpPath(int initialCapacity) {
      this();
      tsIindexes.ensureCapacity(initialCapacity);
      tsJindexes.ensureCapacity(initialCapacity);
   }

   public WarpPath(String inputFile) {
      this();
      try {
         // Record the Label names (fropm the top row.of the input file).
         final BufferedReader br = new BufferedReader (new FileReader (inputFile));  // open the input file

         // Read Cluster assignments.
         String line;
         while ((line = br.readLine()) != null) { // read lines until end of file
            final StringTokenizer st = new StringTokenizer(line, ",", false);
            if (st.countTokens() == 2) {
               tsIindexes.add(Integer.valueOf(st.nextToken()));
               tsJindexes.add(Integer.valueOf(st.nextToken()));
            }
            else throw new InternalError("The Warp Path File '" + inputFile + "' has an incorrect format.  There must be\n" + "two numbers per line separated by commas");
         }
      } catch (FileNotFoundException e) { throw new InternalError("ERROR:  The file '" + inputFile + "' was not found.");
      } catch (IOException e) { throw new InternalError("ERROR:  Problem reading the file '" + inputFile + "'."); }
   }

   // FUNCTIONS
   public int size() { return tsIindexes.size(); }
   public int minI() { return ((Integer)tsIindexes.get(0)).intValue(); }
   public int minJ() { return ((Integer)tsJindexes.get(0)).intValue(); }
   public int maxI() { return ((Integer)tsIindexes.get( tsIindexes.size()-1 )).intValue();}
   public int maxJ() { return ((Integer)tsJindexes.get( tsJindexes.size()-1 )).intValue(); }
   public void addFirst(int i, int j) {
      tsIindexes.add(0, new Integer(i));
      tsJindexes.add(0, new Integer(j));
   }
   
   public void addLast(int i, int j) {
      tsIindexes.add(new Integer(i));
      tsJindexes.add(new Integer(j));
   }
   
   public ArrayList getMatchingIndexesForI(int i) {
      int index = tsIindexes.indexOf(new Integer(i));
      if (index < 0) throw new InternalError("ERROR:  index '" + i + " is not in the " + "warp path.");
      final ArrayList matchingJs = new ArrayList();
      while (index<tsIindexes.size() && tsIindexes.get(index).equals(new Integer(i))) matchingJs.add(tsJindexes.get(index++));
      return matchingJs;
   }
   
   public ArrayList getMatchingIndexesForJ(int j) {
      int index = tsJindexes.indexOf(new Integer(j));
      if (index < 0) throw new InternalError("ERROR:  index '" + j + " is not in the " + "warp path.");
      final ArrayList matchingIs = new ArrayList();
      while (index<tsJindexes.size() && tsJindexes.get(index).equals(new Integer(j))) matchingIs.add(tsIindexes.get(index++));
      return matchingIs;
    }

   // Create a new WarpPath that is the same as THIS WarpPath, but J is the reference template, rather than I.
   public WarpPath invertedCopy() {
      final WarpPath newWarpPath = new WarpPath();
      for (int x=0; x<tsIindexes.size(); x++) newWarpPath.addLast(((Integer)tsJindexes.get(x)).intValue(), ((Integer)tsIindexes.get(x)).intValue());
      return newWarpPath;
   }

   // Swap I and J so that the warp path now indicates that J is the template rather than I.
   public void invert() {
      for (int x=0; x<tsIindexes.size(); x++) {
         final Object temp = tsIindexes.get(x);
         tsIindexes.set(x, tsJindexes.get(x));
         tsJindexes.set(x, temp);
      }
   }

   public ColMajorCell get(int index) {
      if ( (index>this.size()) || (index<0) ) throw new NoSuchElementException();
      else return new ColMajorCell(((Integer)tsIindexes.get(index)), ((Integer)tsJindexes.get(index))); }
  

  // This method loops through the tsI frames and counts
  // how many times that tsI frame occurs.
  // If it occurs more than once, i.e. if it has duplicate
  // tsJ frames associated with it, it is added to the
  // duplicate array.
  public ArrayList<Integer> findDuplicateTSIframes() {
    int size = tsIindexes.size();
    int[] count = new int[size];
    ArrayList<Integer> duplicates = new ArrayList<>();
    for (int i = 0; i < size; i++) count[(int) tsIindexes.get(i)]++;
    for (int i = 0; i < size; i++) {
      if (count[(int) tsIindexes.get(i)] > 1) duplicates.add((int) tsIindexes.get(i));
    }
    return duplicates;
  }

  // This method loops through the tsI frames and checks if it has duplicate values (using the
  // dArray from findDuplicateTSIFrames()).
  // If it doesn't, then that tsI frame and its corresponding tsJ frame is added to the
  // AngleMap.
  // @param : ArrayList of duplicate tsI frames
  public AngleMap addUniqueTSIframesToAngleMap(ArrayList<Integer> dFrames, String fileName) {
    AngleMap aMap = new AngleMap(new HashMap<Integer, Integer>(), fileName);
    for (int i = 0; i < tsIindexes.size(); i++) {
      int tsIframe = (int) tsIindexes.get(i);
      int tsJframe = (int) tsJindexes.get(i);
      if (!dFrames.contains(tsIframe))
          aMap.put(tsIframe, tsJframe);
    }
    return aMap;
  }


  // This method loops through the tsI frames and checks if it is one of the duplicate frames.
  // If it is, then we add the associated tsJ frame to a list.
  // Once we have all of the associated tsJ frames, we add the tsIframe and the list of tsJframes to the map of
  // duplicates.
  // @param : ArrayList of duplicate tsI frames 
  public HashMap<Integer, ArrayList<Integer>> createMapOfValuesForDuplicateTSIframes(ArrayList<Integer> dFrames) {
    HashMap<Integer, ArrayList<Integer>> dMap = new HashMap<>();
    for (int d:dFrames) {
      ArrayList<Integer> tsJframeList = new ArrayList<>();
      for (int i = 0; i < tsIindexes.size(); i++) {
        int tsIframe = (int) tsIindexes.get(i);
        int tsJframe = (int) tsJindexes.get(i);
        if (d == tsIframe) tsJframeList.add(tsJframe);
      }
      dMap.put(d, tsJframeList);
    }
    return dMap;
  }


  // This methhod deserializes the costMatrixMap from an earlier
  // file and sends it to the bigger method.
  public HashMap<ArrayList<Integer>, Double> createCostMatrixMap(){
    HashMap<ArrayList<Integer>, Double> cMap = new HashMap<>();
    try {
      FileInputStream fis = new FileInputStream("costMatrixHashmap.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      cMap = (HashMap) ois.readObject();
      ois.close();
      fis.close();
    } catch (IOException ioe) {
        ioe.printStackTrace();
        System.out.println("Incomplete cost map: IOEexception");
    } catch (ClassNotFoundException c) {
        System.out.println("Class not found");
        System.out.println("Incomplete cost map: ClassNotFoundException");
        c.printStackTrace();
    }
    return cMap;
  }


  // This method is a util method that just gets the cost from the costMap given tsI and tsJ frames.
  // @param : tsI frame
  // @param : tsJ frame
  // @param : CostMatrixMap
  public double getCost(Integer tsIframe, Integer tsJframe, HashMap<ArrayList<Integer>, Double> costMap) {
    ArrayList<Integer> indexes = new ArrayList<Integer>();
    indexes.add(tsIframe);
    indexes.add(tsJframe);
    double cost = costMap.get(indexes);
    return cost;
  }


  // This method adds all of the tsJ frames for one tsI frame to a TreeMap, which sorts them by their cost.
  // Then, we get the tsJ frame with the lowest cost and return it.
  // @param : tsI frame
  // @param : tsJ frame
  // @param : CostMatrixMap
  public int findCorrectTSJframe(Integer tsIframe, ArrayList<Integer> possibleTSJframes, HashMap<ArrayList<Integer>, Double> costMap){
    TreeMap<Double, Integer> costRanking = new TreeMap<>();
    for (int tsJframe:possibleTSJframes) {
      double cost = getCost(tsIframe, tsJframe, costMap);
      costRanking.put(cost, tsJframe);
    }
    double lowestCost = costRanking.firstKey();
    int correctTSJframe = costRanking.get(lowestCost);
    return correctTSJframe;
  }

  
  // This method finds the duplicate tsI frames
  // It adds the non-duplicate tsI frames to the angle map
  // It makes a map of the duplicate tsI frames to an ArrayList of the associated tsJ frame.
  // It uses the costMatrixMap to find the lowest cost tsJ frame for each duplicate tsI frame
  // Then it adds those duplicate tsI frames to the angle map
  // At the end, both duplicate and non-duplicate tsI frames have been added.
  // We serialize the final AngleMap, sort it, and return it
  // @param : fileName for the AngleMap to serialize and deserialize
  public AngleMap createAngleMap(String fileName, HashMap<Integer, ArrayList<Integer>> tsIZeroHashMap, HashMap<Integer, ArrayList<Integer>> tsJZeroHashMap, int i) {

    ArrayList<Integer> duplicateTSIframes = findDuplicateTSIframes();

    AngleMap aMap = addUniqueTSIframesToAngleMap(duplicateTSIframes, fileName);

    HashMap<Integer, ArrayList<Integer>> dHashMap = createMapOfValuesForDuplicateTSIframes(duplicateTSIframes);

    HashMap<ArrayList<Integer>, Double> costHashMap = createCostMatrixMap();

    for (int tsIframe:dHashMap.keySet()) {

      ArrayList<Integer> potentialTSJframes = dHashMap.get(tsIframe);

      int tsJframe = findCorrectTSJframe(tsIframe, potentialTSJframes, costHashMap);

      aMap.put(tsIframe, tsJframe);
    }

    aMap.serialize();

    aMap.sort();
    
    aMap.addZeroes(tsIZeroHashMap, tsJZeroHashMap, i);

    return aMap;
  }
  

   public String toString() {
      StringBuffer outStr = new StringBuffer("[");
      for (int x=0; x<tsIindexes.size(); x++) {
         outStr.append("(" + tsIindexes.get(x) + "," + tsJindexes.get(x) + ")");
         if (x < tsIindexes.size()-1) outStr.append(",");
      }
      return new String(outStr.append("]"));
   }

   public boolean equals(Object obj) {
      if ( (obj instanceof WarpPath) ) { // trivial false test
         final WarpPath p = (WarpPath)obj;
         if ( (p.size()==this.size()) && (p.maxI()==this.maxI()) && (p.maxJ()==this.maxJ())) { // less trivial reject
            // Compare each value in the warp path for equality
            for (int x=0; x<this.size(); x++)
               if ( !(this.get(x).equals(p.get(x))) ) return false;
            return true;
         } else return false;
      } else return false;
   }

   public int hashCode() { return tsIindexes.hashCode() * tsJindexes.hashCode(); }
}