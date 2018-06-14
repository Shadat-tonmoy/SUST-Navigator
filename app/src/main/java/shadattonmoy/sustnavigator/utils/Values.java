package shadattonmoy.sustnavigator.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Values {
    public static String[] months = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
    public static String[] days = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
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
}
