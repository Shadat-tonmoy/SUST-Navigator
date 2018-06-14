package shadattonmoy.sustnavigator.utils;

import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.school.model.School;

public class DummyValues {

    public static List<Dept> getDept(String school){
        List<Dept> depts = new ArrayList<>();
        if(school.equals("School of Agriculture & Mineral Sciences"))
        {
            depts.add(new Dept("1","Forestry & Environmental Science","FES"));
        }
        else if(school.equals("School of Life Science"))
        {
            depts.add(new Dept("1","Biochemistry and Molecular Biology","BMB"));
            depts.add(new Dept("1","Genetic Engineering & Biotechnology","GEB"));
        }
        else if(school.equals("School of Applied Sciences & Technology"))
        {
            depts.add(new Dept("1","Architecture","ARC"));
            depts.add(new Dept("2","Chemical Engineering & Polymer Science","CEP"));
            depts.add(new Dept("3","Civil & Environmental Engineering","CEE"));
            depts.add(new Dept("4","Computer Science and Engineering","CSE"));
            depts.add(new Dept("5","Electrical & Electronic Engineering","EEE"));
            depts.add(new Dept("6","Food Engineering & Tea Technology","FET"));
            depts.add(new Dept("7","Industrial & Production Engineering","IPE"));
            depts.add(new Dept("8","Mechanical Engineering","MEE"));
            depts.add(new Dept("9","Petroleum and Mining Engineering","PME"));
        }
        else if(school.equals("School of Management & Business Administration"))
        {
            depts.add(new Dept("1","Business Administration","BAN"));
        }
        else if(school.equals("School of Physical Sciences"))
        {
            depts.add(new Dept("1","Chemistry","CHE"));
            depts.add(new Dept("2","Geography and Environment","GEE"));
            depts.add(new Dept("3","Mathematics","MAT"));
            depts.add(new Dept("4","Oceanography","OCG"));
            depts.add(new Dept("5","Physics","PHY"));
            depts.add(new Dept("6","Statistics","STA"));
        }
        else if(school.equals("School of Social Sciences"))
        {

            depts.add(new Dept("1","Anthropology","ANP"));
            depts.add(new Dept("2","Bangla","BNG"));
            depts.add(new Dept("3","Economics","ECO"));
            depts.add(new Dept("4","English","ENG"));
            depts.add(new Dept("5","Political Studies","PSS"));
            depts.add(new Dept("6","Public Administration","PAD"));
            depts.add(new Dept("7","Social Work","SCW"));
            depts.add(new Dept("7","Sociology","SCO"));
        }
        return depts;
    }

    public static List<School> getSchools()
    {
        List<School> schools = new ArrayList<>();
        schools.add(new School("School of Agriculture & Mineral Sciences",getDept("School of Agriculture & Mineral Sciences")));
        schools.add(new School("School of Applied Sciences & Technology",getDept("School of Applied Sciences & Technology")));
        schools.add(new School("School of Life Science",getDept("School of Life Science")));
        schools.add(new School("School of Management & Business Administration",getDept("School of Management & Business Administration")));
        schools.add(new School("School of Physical Sciences",getDept("School of Physical Sciences")));
        schools.add(new School("School of Social Sciences",getDept("School of Social Sciences")));
        return schools;
    }



    public static List<Course> getCourses(String semester)
    {
        List<Course> courses = new ArrayList<>();
        if(semester.equals("1_1"))
        {
            courses.add(new Course("CSE 133", "Structured Programming Language", "3"));
            courses.add(new Course("CSE 134", "Structured Programming Language Lab", "3"));
            courses.add(new Course("CSE 143", "Discrete Mathematics", "3"));
            courses.add(new Course("CSE 144", "Discrete Mathematics Lab", "1.5"));
            courses.add(new Course("EEE 109","Electrical Circuits" ,"3"));
            courses.add(new Course("EEE 110", "Electrical Circuits LAB", "1.5"));
            courses.add(new Course("MAT 102D", "Matrices, Vector Analysis and Geometry", "3"));
            courses.add(new Course("ENG 101D", "English Language I", "2"));
            courses.add(new Course("ENG 102D", "English Language I Lab", "1"));

        }
        else if(semester.equals("1_2"))
        {
            courses.add(new Course("CSE 137" ,"Data Structure", "3"));
            courses.add(new Course("CSE 138" ,"Data Structure Lab", "2"));
            courses.add(new Course("EEE 111" ,"Electronic Devices and Circuits", "3"));
            courses.add(new Course("EEE 112" ,"Electronic Devices and Circuits LAB", "1.5"));
            courses.add(new Course("IPE 106" ,"Engineering Graphics", "1.5"));
            courses.add(new Course("IPE 108" ,"Workshop Practice", "1"));
            courses.add(new Course("PHY 103D" ,"Mechanics, Wave, Heat & Thermodynamics", "3"));
            courses.add(new Course("MAT 103D","Calculus", "3"));
            courses.add(new Course("CSE 150" ,"Project Work I", "1"));

        }
        else if(semester.equals("2_1"))
        {
            courses.add(new Course("EEE 201","Digital Logic Design","3"));
            courses.add(new Course("EEE 202","Digital Logic Design Lab","2"));
            courses.add(new Course("CSE 237","Algorithm Design & Analysis","3"));
            courses.add(new Course("CSE 328","Algorithm Design & Analysis Lab ","1.5"));
            courses.add(new Course("BUS 203","Cost and Management Accounting","3" ));
            courses.add(new Course("PHY 207D","Electromagnetism, Optics & Modern Physics","3"));
            courses.add(new Course("PHY 202D","Basic Physics Lab","1.5"));
            courses.add(new Course("STA 202D","Basic Statistics & Probability","3" ));
        }
        return courses;
    }
}
