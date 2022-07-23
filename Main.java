import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.JSONException;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
  public static void main(String[] args) throws Exception {     
    ArrayList<String> vid2 = createVidNamesArrayListForVid2();
    ArrayList<String> vid4 = createVidNamesArrayListForVid4();
    ArrayList<String> vid5 = createVidNamesArrayListForVid5();
    ArrayList<String> vid6 = createVidNamesArrayListForVid6();
    bodyDTW(vid2, vid4, "body2_4");
  }
  
  public static AngleMap angleDTW(String tsIAngleFileName, String tsJAngleFileName, int i, HashMap<Integer, ArrayList<Integer>> tsIZeroHashMap, HashMap<Integer, ArrayList<Integer>> tsJZeroHashMap) throws Exception {
    double[] tsIframes = jsonParser(tsIAngleFileName);
    double[] tsJframes = jsonParser(tsJAngleFileName);
    tsIframes = buildZeroHashMap(tsIframes, i, tsIZeroHashMap);
    tsJframes = buildZeroHashMap(tsJframes, i, tsJZeroHashMap);
    Body.numOfStudentFrames = tsIframes.length;
    Body.numOfTeacherFrames = tsJframes.length;
    TimeWarpInfo s = testTimeSeriesAdHocCreationOneDimension(tsIframes, tsJframes);
    AngleMap aMap = s.getPath().createAngleMap("aMap.ser", tsIZeroHashMap, tsJZeroHashMap, i);
    return aMap;
  }
  
  public static double[] buildZeroHashMap(double[] frames, int i, HashMap<Integer, ArrayList<Integer>> zeroHashMap) {
    ArrayList<Integer> zeroIndexes = new ArrayList<>();   
    ArrayList<Double> newFrames = new ArrayList<>();
    for (int a = 0; a < frames.length; a++) {
        if (frames[a] == 0)
            zeroIndexes.add(a);
        else newFrames.add(frames[a]);
    }
    double[] frameArray = new double[newFrames.size()];
    for (int a = 0; a < newFrames.size(); a++)
        frameArray[a] = newFrames.get(a);
    frames = frameArray;
    zeroHashMap.put(i, zeroIndexes);
    return frames;
  }
  public static double[] jsonParser(String angleFileName) throws FileNotFoundException, IOException, ParseException {
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader(angleFileName));
    JSONObject jsonObject = (JSONObject)obj;
    JSONArray angles = (JSONArray)jsonObject.get("final_vid_angles");
    double[] frames = new double[angles.size()];
    for (int j = 0; j < frames.length; j++) frames[j] = Double.valueOf(angles.get(j).toString());    
    return frames;
  }
  
  public static void bodyDTW(ArrayList<String> video1, ArrayList<String> video2, String fileName) throws JSONException, Exception {
    int studentVidSize = video1.size();
    ArrayList<AngleMap> bodyList = new ArrayList<>();
    ArrayList<String> fileNameList = new ArrayList<>();  
    HashMap<Integer, ArrayList<Integer>> tsIZeroHashMap = new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> tsJZeroHashMap = new HashMap<>();
    for (int i = 0; i < studentVidSize; i++) {
        AngleMap aMap = angleDTW(video1.get(i), video2.get(i), i, tsIZeroHashMap, tsJZeroHashMap);
        bodyList.add(aMap);
        fileNameList.add("aMap.ser");
    }
    Body body = new Body(bodyList, fileNameList);
    ExternalFile.createJSON(fileName);
  }
  
  public static TimeWarpInfo testTimeSeriesAdHocCreationOneDimension(double[] tsIframes, double[] tsJframes) throws Exception {
    TimeSeries tsI = new TimeSeries(tsIframes);
    TimeSeries tsJ = new TimeSeries(tsJframes);
    final DistanceFunction distFn = DistanceFunctionFactory.getEuclideanDist();
    final TimeWarpInfo infoTSI = DTW.getWarpInfoBetween(tsI, tsJ, 500, distFn);
    return infoTSI;
  }
  
  public static ArrayList<String> createVidNamesArrayListForVid2() {
      ArrayList<String> vidNames = new ArrayList<>();
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__left_upper_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__neck_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__neck_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__right_upper_arm_Angle__2.json");     
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__torso_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_2_BodySeg__torso_Angle__2.json");
      return vidNames;
  }
  public static ArrayList<String> createVidNamesArrayListForVid4() {
      ArrayList<String> vidNames = new ArrayList<>();
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__left_upper_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__neck_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__neck_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__right_upper_arm_Angle__2.json");     
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__torso_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_4_BodySeg__torso_Angle__2.json");
      return vidNames;
  }
  public static ArrayList<String> createVidNamesArrayListForVid5() {
      ArrayList<String> vidNames = new ArrayList<>();
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__left_upper_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__neck_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__neck_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__right_upper_arm_Angle__2.json");     
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__torso_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_5_BodySeg__torso_Angle__2.json");
      return vidNames;
  }
  public static ArrayList<String> createVidNamesArrayListForVid6() {
      ArrayList<String> vidNames = new ArrayList<>();
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\Vid_6_BodySeg__left_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__left_upper_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__neck_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__neck_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_calf_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_calf_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_hip_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_hip_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_lower_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_lower_arm_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_shoulder_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_shoulder_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_thigh_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_thigh_Angle__2.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_upper_arm_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__right_upper_arm_Angle__2.json");     
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\Vid_6_BodySeg__torso_Angle__1.json");
      vidNames.add("C:\\Users\\Haridhar Pulivarthy\\Downloads\\balletposedata3\\balletposedata3-main\\2Vid_6_BodySeg__torso_Angle__2.json");
      return vidNames;
  }
}