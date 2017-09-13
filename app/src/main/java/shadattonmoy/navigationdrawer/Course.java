package shadattonmoy.navigationdrawer;

/**
 * Created by Shadat Tonmoy on 6/19/2017.
 */

public class Course {
    private String course_code,course_title,course_credit,course_detp,course_id,grade,local_id;

    public Course(){
        super();
    }
    public Course(String course_code, String course_title, String course_credit, String course_detp) {
        this.course_code = course_code;
        this.course_title = course_title;
        this.course_credit = course_credit;
        this.course_detp = course_detp;
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
}
