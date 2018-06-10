package shadattonmoy.sustnavigator.school.model;

import java.util.List;

import shadattonmoy.sustnavigator.dept.model.Dept;

public class School {
    private String schoolTitle;
    private List<Dept> depts;

    public School() {
    }

    public School(String schoolTitle, List<Dept> depts) {
        this.schoolTitle = schoolTitle;
        this.depts = depts;
    }

    public String getSchoolTitle() {
        return schoolTitle;
    }

    public void setSchoolTitle(String schoolTitle) {
        this.schoolTitle = schoolTitle;
    }

    public List<Dept> getDepts() {
        return depts;
    }

    public void setDepts(List<Dept> depts) {
        this.depts = depts;
    }
}
