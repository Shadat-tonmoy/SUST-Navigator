package shadattonmoy.sustnavigator.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Values {
    public static String[] months = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
    public static String[] days = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private static Map<String,String> semesterCodeMap = new HashMap<>();
    public static List<String> getSessions()
    {
        List<String> sessions = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        sessions.add((year-1)+"-"+(year%100));
        sessions.add((year-2)+"-"+((year-1)%100));
        sessions.add((year-3)+"-"+((year-2)%100));
        sessions.add((year-4)+"-"+((year-3)%100));
        sessions.add((year-5)+"-"+((year-4))%100);
        return sessions;
    }

    public static Map getSemesterCodeMap()
    {
        semesterCodeMap.put("1st Year 1st Semester","1_1");
        semesterCodeMap.put("1st Year 2nd Semester","1_2");
        semesterCodeMap.put("2nd Year 1st Semester","2_1");
        semesterCodeMap.put("2nd Year 2nd Semester","2_2");
        semesterCodeMap.put("3rd Year 1st Semester","3_1");
        semesterCodeMap.put("3rd Year 2nd Semester","3_2");
        semesterCodeMap.put("4th Year 1st Semester","4_1");
        semesterCodeMap.put("4th Year 2nd Semester","4_2");
        semesterCodeMap.put("5th Year 1st Semester","5_1");
        semesterCodeMap.put("5th Year 2nd Semester","5_2");

        return semesterCodeMap;
    }
}
