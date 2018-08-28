package shadattonmoy.sustnavigator.utils;

public class LastModified {
    String name,regNo,dept;
    long time;

    public LastModified() {
    }

    public LastModified(String name, String regNo, String dept, long time) {
        this.name = name;
        this.regNo = regNo;
        this.dept = dept;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
