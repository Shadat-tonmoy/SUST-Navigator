package shadattonmoy.sustnavigator.commons.model;

/**
 * Created by Shadat Tonmoy on 8/29/2017.
 */

public class Semester {
    private String totalCourse,totalCredit,semesterName,semesterCode;

    public Semester(String totalCourse, String totalCredit, String semesterName, String semesterCode) {
        this.totalCourse = totalCourse;
        this.totalCredit = totalCredit;
        this.semesterName = semesterName;
        this.semesterCode = semesterCode;
    }

    public String getTotalCourse() {
        return totalCourse;
    }

    public String getTotalCredit() {
        return totalCredit;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public String getSemesterCode() {
        return semesterCode;
    }
}
