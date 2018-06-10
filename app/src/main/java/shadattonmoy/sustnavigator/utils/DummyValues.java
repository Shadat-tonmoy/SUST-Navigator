package shadattonmoy.sustnavigator.utils;

import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.school.model.School;

public class DummyValues {

    public static List<Dept> getDept(){
        List<Dept> depts = new ArrayList<>();
        depts.add(new Dept("1","Computer Science and Engineering","CSE"));
        depts.add(new Dept("2","Computer Science and Engineering","CSE"));
        depts.add(new Dept("3","Computer Science and Engineering","CSE"));
        depts.add(new Dept("4","Computer Science and Engineering","CSE"));
        depts.add(new Dept("5","Computer Science and Engineering","CSE"));
        depts.add(new Dept("6","Computer Science and Engineering","CSE"));
        depts.add(new Dept("7","Computer Science and Engineering","CSE"));
        depts.add(new Dept("8","Computer Science and Engineering","CSE"));
        depts.add(new Dept("9","Computer Science and Engineering","CSE"));
        return depts;
    }

    public static List<School> getSchools()
    {
        List<School> schools = new ArrayList<>();
        schools.add(new School("School of Applied Science",getDept()));
        schools.add(new School("School of Social Science",getDept()));
        schools.add(new School("School of Life Science",getDept()));
        schools.add(new School("School of Pure Science",getDept()));
        schools.add(new School("School of Business Studies",getDept()));
        return schools;
    }
}
