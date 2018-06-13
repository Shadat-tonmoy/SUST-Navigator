package shadattonmoy.sustnavigator.utils;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getSessions()
    {
        List<String> sessions = new ArrayList<>();
        sessions.add("2017-18 (Current Session)");
        sessions.add("2016-17");
        sessions.add("2015-16");
        sessions.add("2014-15");
        return sessions;
    }
}
