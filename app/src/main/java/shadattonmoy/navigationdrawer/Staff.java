package shadattonmoy.navigationdrawer;

/**
 * Created by Shadat Tonmoy on 10/1/2017.
 */

public class Staff {
    private String name,designation,roomNo,phoneNo,id;

    public Staff()
    {

    }

    public Staff(String name, String designation, String roomNo, String phoneNo) {
        this.name = name;
        this.designation = designation;
        this.roomNo = roomNo;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
