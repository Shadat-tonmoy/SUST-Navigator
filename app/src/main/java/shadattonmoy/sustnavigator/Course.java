package shadattonmoy.sustnavigator;

import java.io.Serializable;

/**
 * Created by Shadat Tonmoy on 6/19/2017.
 */

public class Course implements Serializable{
    private String course_code,course_title,course_credit,course_detp,course_id,grade,local_id,courseDetail;
    private boolean isAdded;

    public Course(){
        super();
    }
    public Course(String course_code, String course_title, String course_credit, String course_detp, String courseDetail) {
        this.course_code = course_code;
        this.course_title = course_title;
        this.course_credit = course_credit;
        this.course_detp = course_detp;
        this.courseDetail = courseDetail;
    }
    public Course(String course_code, String course_title, String course_credit) {
        this.course_code = course_code;
        this.course_title = course_title;
        this.course_credit = course_credit;
    }

    public String getCourse_code() {
        return course_code;
    }

    public String getCourse_title() {
        return course_title;
    }

    public String getCourse_credit() {
        return course_credit;
    }

    public String getCourse_detp() {
        return course_detp;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public void setCourse_credit(String course_credit) {
        this.course_credit = course_credit;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }


    public String getCourseDetail() {
        if(courseDetail==null)
            return "";
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_code='" + course_code + '\'' +
                ", course_title='" + course_title + '\'' +
                ", course_credit='" + course_credit + '\'' +
                ", course_detp='" + course_detp + '\'' +
                ", course_id='" + course_id + '\'' +
                ", grade='" + grade + '\'' +
                ", local_id='" + local_id + '\'' +
                ", courseDetail='" + courseDetail + '\'' +
                ", isAdded=" + isAdded +
                '}';
    }
}
