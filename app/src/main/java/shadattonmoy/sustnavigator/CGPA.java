package shadattonmoy.sustnavigator;

import android.widget.Spinner;

/**
 * Created by Shadat Tonmoy on 6/20/2017.
 */

public class CGPA {
    private String courseCode,courseTitle,courseCredit;


    public CGPA(String courseCode, String courseTitle, String courseCredit) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.courseCredit = courseCredit;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

}
