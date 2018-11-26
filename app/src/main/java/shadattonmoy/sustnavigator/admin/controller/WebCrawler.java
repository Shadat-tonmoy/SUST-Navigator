package shadattonmoy.sustnavigator.admin.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.syllabus.model.SemesterWiseCourses;
import shadattonmoy.sustnavigator.teacher.model.Teacher;

public class WebCrawler{
    private String deptCode;

    public WebCrawler() {
    }

    public WebCrawler(String deptCode) {
        this.deptCode = deptCode;
    }

    public List<Teacher> crawlFacultyData() {

        final StringBuilder builder = new StringBuilder();
        List<Teacher> teachers = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://www.sust.edu/d/"+deptCode+"/faculty").get();
            String title = doc.title();
            Elements links = doc.select("a[data-title][data-designation][data-description]");
            Elements titles = doc.select("a[data-title]");
            Elements designations = doc.select("a[data-designation]");
            Elements descriptions = doc.select("a[data-description]");
//            Log.e("Total",titles.size()+"");

            for(int i=0;i<titles.size();i++)
            {

                String facultyTitle = titles.get(i).attr("data-title");
                String facultyDesignation = designations.get(i).attr("data-designation");
                String facultyDescription = descriptions.get(i).attr("data-description");
//                Log.e("Faculty","Title "+facultyTitle+" Designation "+facultyDesignation);
                Teacher teacher = new Teacher(facultyTitle,facultyDesignation,"","","");
                teacher = addContactInfo(teacher,facultyDescription);
                teachers.add(teacher);
            }

        } catch (IOException e) {
//            Log.e("Crawler Error",e.getMessage());
        }
        return teachers;
    }

    public Teacher addContactInfo(Teacher teacher,String html)
    {
        Document document = Jsoup.parse(html);
        Elements contactInfoList = document.select("ul[class=contact-info-ul]");
        String phoneNo = "",email = "",office="";
        for(Element contactInfo : contactInfoList)
        {
            Elements contactInfosElements = contactInfo.getElementsByTag("li");
            for(Element contactInfoElement : contactInfosElements)
            {
                String elementText = contactInfoElement.text();
                if(elementText.toLowerCase().contains("phone"))
                    phoneNo = getPhoneNo(elementText);
                else if(elementText.toLowerCase().contains("email"))
                    email = getEmail(elementText);
                else if(elementText.toLowerCase().contains("office address"))
                    office = getOfficeAddress(elementText);
            }
        }
//        Log.e("Details ","Phone "+phoneNo+" Email "+email+" Office "+office);
        teacher.setPhone(phoneNo);
        teacher.setEmail(email);
        teacher.setRoom(office);
        return teacher;
    }

    public String getPhoneNo(String text)
    {
        if(text.toLowerCase().contains("phone"))
        {
            return text.replace("Phone:","").trim();
        }
        else return null;
    }

    public String getEmail(String text)
    {
        if(text.toLowerCase().contains("email"))
        {
            return text.replace("Email:","").trim();
        }
        else return null;
    }

    public String getOfficeAddress(String text)
    {
        if(text.toLowerCase().contains("office address"))
        {
            if(text.toLowerCase().contains("http://www"))
                return "N/A";
            return text.replace("Office Address:","").replace("Department","Dept.").trim();
        }
        else return null;
    }


    public List<SemesterWiseCourses> crawlSyllabusData(String deptCode) {

        final StringBuilder builder = new StringBuilder();
        List<Course> courses= new ArrayList<>();
        List<SemesterWiseCourses> semesterWiseCourses = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://www.sust.edu/d/"+deptCode+"/curriculam").get();
            String title = doc.title();
            Elements curriculumTable = doc.select("table[class=curriculum-table]");
            Log.e("Total",curriculumTable.size()+"");
            String semester = "";
            for(int i=0;i<curriculumTable.size();i++)
            {
                courses= new ArrayList<>();
                Elements curriculumRow = curriculumTable.get(i).getElementsByTag("tr");
                Log.e("Curriculum", "Size "+curriculumRow.size()+" "+curriculumRow.text());
                if(i>7)
                    break;
                for(int j=0;j<curriculumRow.size()-1;j++)
                {
                    Element curriculumColumn = curriculumRow.get(j);
                    Log.e("CurriculumColumn",curriculumColumn.toString());
                    Elements curriculumData = curriculumColumn.getElementsByTag("td");
                    if(curriculumData.size()>0)
                    {
                        Log.e("CurriculumData","Size "+curriculumData.size());
                        String courseCode ="",courseTitle = "",courseCredit = "";
                        for(int k=0;k<curriculumData.size()-1;k++)
                        {
                            String curriculumText = curriculumData.get(k).text();
                            if(k==0)
                                courseCode = curriculumText;
                            else if(k==1)
                                courseTitle = curriculumText;
                            else if(k==3)
                                courseCredit = curriculumText;
                        }
                        int sem =i+1;
                        if(sem%2==0)
                            semester=sem/2+"";
                        else semester=(sem/2+1)+"";

                        if(sem%2==0)
                            semester+="_2";
                        else semester+="_1";
                        Course course = new Course(courseCode,courseTitle,courseCredit);
                        courses.add(course);
                        //Log.e("CourseObject ",semester+" "+courseCode+" "+courseTitle+" "+courseCredit);

                    }
                }
                Log.e("CourseFor ",semester);
                semesterWiseCourses.add(new SemesterWiseCourses(semester,courses));
            }

            /*for(int i=0;i<titles.size();i++)
            {

                String facultyTitle = titles.get(i).attr("data-title");
                String facultyDesignation = designations.get(i).attr("data-designation");
                String facultyDescription = descriptions.get(i).attr("data-description");
//                Log.e("Faculty","Title "+facultyTitle+" Designation "+facultyDesignation);
                Teacher teacher = new Teacher(facultyTitle,facultyDesignation,"","","");
                teacher = addContactInfo(teacher,facultyDescription);
                teachers.add(teacher);
            }*/

        } catch (IOException e) {
            Log.e("Crawler Error",e.getMessage());
        }
        return semesterWiseCourses;
    }
}



