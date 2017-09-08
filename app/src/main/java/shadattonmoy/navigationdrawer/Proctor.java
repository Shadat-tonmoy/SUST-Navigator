package shadattonmoy.navigationdrawer;

/**
 * Created by Shadat Tonmoy on 9/8/2017.
 */

public class Proctor {
    private String name,designation,roomNo,contactNo;

    public Proctor() {
        super();
    }

    public Proctor(String name, String designation, String roomNo, String contactNo) {
        this.name = name;
        this.designation = designation;
        this.roomNo = roomNo;
        this.contactNo = contactNo;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getContactNo() {
        return contactNo;
    }
}
