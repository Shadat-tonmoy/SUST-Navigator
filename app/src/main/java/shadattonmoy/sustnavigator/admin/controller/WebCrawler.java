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
            Log.e("Total",titles.size()+"");

            for(int i=0;i<titles.size();i++)
            {

                String facultyTitle = titles.get(i).attr("data-title");
                String facultyDesignation = designations.get(i).attr("data-designation");
                String facultyDescription = descriptions.get(i).attr("data-description");
                Log.e("Faculty","Title "+facultyTitle+" Designation "+facultyDesignation);
                Teacher teacher = new Teacher(facultyTitle,facultyDesignation,"","","","");
                teacher = addContactInfo(teacher,facultyDescription);
                teachers.add(teacher);
            }

        } catch (IOException e) {
            Log.e("Crawler Error",e.getMessage());
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
            return text.replace("Phone:","");
        }
        else return null;
    }

    public String getEmail(String text)
    {
        if(text.toLowerCase().contains("email"))
        {
            return text.replace("Email:","");
        }
        else return null;
    }

    public String getOfficeAddress(String text)
    {
        if(text.toLowerCase().contains("office address"))
        {
            return text.replace("Office Address:","");
        }
        else return null;
    }
}
