package shadattonmoy.navigationdrawer;

import java.util.ArrayList;

/**
 * Created by Shadat Tonmoy on 6/19/2017.
 */

public class SemesterCourse {
    private String titleOfSemester;
    private ArrayList<Course> coursesOfSemester;


    public SemesterCourse(String titleOfSemester, ArrayList<Course> coursesOfSemester)   {
        this.titleOfSemester = titleOfSemester;
        this.coursesOfSemester = coursesOfSemester;
    }


    public String getTitleOfSemester() {
        return titleOfSemester;
    }



    public ArrayList<Course> getCoursesOfSemester() {
        return coursesOfSemester;
    }


}
