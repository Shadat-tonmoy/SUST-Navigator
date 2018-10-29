package shadattonmoy.sustnavigator.teacher.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shadat Tonmoy on 6/17/2017.
 */

public class Teacher implements Parcelable {
    private String name,designation,room,email,phone,id;
    public Teacher()
    {
        super();
    }



    public Teacher(String name, String designation, String room, String phone, String email) {
        this.name = name;
        this.designation = designation;
        this.room = room;
        this.email = email;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getRoom() {
        return room;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", room='" + room + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
