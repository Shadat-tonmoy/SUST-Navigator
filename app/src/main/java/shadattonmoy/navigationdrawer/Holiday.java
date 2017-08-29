package shadattonmoy.navigationdrawer;

/**
 * Created by Shadat Tonmoy on 6/21/2017.
 */

public class Holiday {
    private String holidayName,holidayDate,holidayDays,holidayDesc;
    public Holiday(String holidayName, String holidayDate, String holidayDesc,String holidayDays) {
        this.holidayName = holidayName;
        this.holidayDate = holidayDate;
        this.holidayDays = holidayDays;
        this.holidayDesc = holidayDesc;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public String getHolidayDays() {
        return holidayDays;
    }

    public String getHolidayDesc() {
        return holidayDesc;
    }
}
