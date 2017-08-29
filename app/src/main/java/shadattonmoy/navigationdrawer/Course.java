package shadattonmoy.navigationdrawer;

/**
 * Created by Shadat Tonmoy on 6/19/2017.
 */

public class Course {
    private String course_code,course_title,course_credit,course_detp;

    public Course(String course_code, String course_title, String course_credit, String course_detp) {
        this.course_code = course_code;
        this.course_title = course_title;
        this.course_credit = course_credit;
        this.course_detp = course_detp;
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
}
