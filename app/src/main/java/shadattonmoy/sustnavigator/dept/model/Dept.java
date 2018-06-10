package shadattonmoy.sustnavigator.dept.model;

public class Dept {
    private String deptId,deptTitle,deptCode;

    public Dept(String deptId, String deptTitle, String deptCode) {
        this.deptId = deptId;
        this.deptTitle = deptTitle;
        this.deptCode = deptCode;
    }

    public Dept() {
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptTitle() {
        return deptTitle;
    }

    public void setDeptTitle(String deptTitle) {
        this.deptTitle = deptTitle;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
}
