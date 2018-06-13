package shadattonmoy.sustnavigator.teacher.model;

/**
 * Created by Shadat Tonmoy on 6/17/2017.
 */

public class Teacher {
    private String name,designation,room,email,phone,fb,id;
    public Teacher()
    {
        super();
    }



    public Teacher(String name, String designation, String room, String phone, String email, String fb) {
        this.name = name;
        this.designation = designation;
        this.room = room;
        this.email = email;
        this.phone = phone;
        this.fb = fb;
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

    public String getFb() {
        return fb;
    }
}
