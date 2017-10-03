package shadattonmoy.navigationdrawer;

/**
 * Created by Shadat Tonmoy on 10/2/2017.
 */

public class Admin {

    private String name,regNo,dept,email,password,id;
    private boolean isVarified;
    public Admin() {
    }

    public Admin(String name, String regNo, String dept, String email, String password,boolean isVarified) {
        this.name = name;
        this.regNo = regNo;
        this.dept = dept;
        this.email = email;
        this.password = password;
        this.isVarified = isVarified;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVarified() {
        return isVarified;
    }

    public void setVarified(boolean varified) {
        isVarified = varified;
    }
}
