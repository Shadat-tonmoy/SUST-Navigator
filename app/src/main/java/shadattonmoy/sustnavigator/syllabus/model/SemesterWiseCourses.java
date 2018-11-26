package shadattonmoy.sustnavigator.syllabus.model;

import java.util.List;

import shadattonmoy.sustnavigator.Course;

public class SemesterWiseCourses {
    private String semester;
    private List<Course> courses;

    public SemesterWiseCourses() {
        super();
    }

    public SemesterWiseCourses(String semester, List<Course> courses) {
        this.semester = semester;
        this.courses = courses;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
