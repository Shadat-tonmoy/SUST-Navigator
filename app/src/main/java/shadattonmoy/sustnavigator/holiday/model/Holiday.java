package shadattonmoy.sustnavigator.holiday.model;

/**
 * Created by Shadat Tonmoy on 6/21/2017.
 */

public class Holiday {
    private String holidayName,holidayDate,holidayDays,holidayDesc;
    private String holidayTitle,startingDate,endingDate,holiayId;
    public Holiday(){
        super();
    }
    public Holiday(String holidayName, String holidayDate, String holidayDesc,String holidayDays) {
        this.holidayName = holidayName;
        this.holidayDate = holidayDate;
        this.holidayDays = holidayDays;
        this.holidayDesc = holidayDesc;
    }

    public Holiday(String holidayTitle, String startingDate, String endingDate) {
        this.holidayTitle = holidayTitle;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
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

    public String getHolidayTitle() {
        return holidayTitle;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public String getHoliayId() {
        return holiayId;
    }

    public void setHoliayId(String holiayId) {
        this.holiayId = holiayId;
    }

    public void setHolidayDays(String holidayDays) {
        this.holidayDays = holidayDays;
    }
}
