package shadattonmoy.sustnavigator.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.admin.controller.WebCrawler;
import shadattonmoy.sustnavigator.syllabus.model.SemesterWiseCourses;

public class SyllabusCrawler extends AsyncTask<String,Void,Void> {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String ... params) {
        WebCrawler webCrawler = new WebCrawler();
        Log.e("SyllabusCrawler","StartCrawling");
        String dept = params[0];
        List<SemesterWiseCourses> semesterWiseCoursesList= webCrawler.crawlSyllabusData(dept);
        Log.e("SyllabusCrawler","SemesterWiseCourses "+semesterWiseCoursesList.size());
        for(SemesterWiseCourses semesterWiseCourses : semesterWiseCoursesList)
        {
            addCourse(dept,semesterWiseCourses.getSemester(),semesterWiseCourses.getCourses());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public void addCourse(String dept,String semester,List<Course> courses) {
        Log.e("AddingCourse ",dept+" "+semester);
        for(Course course:courses)
            Log.e("AddingCourse ",course.toString());
        /*firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child("2014-15").child(dept.toLowerCase()).child(semester);
        for (Course course : courses) {
            databaseReference.push().setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                    Log.e("syllabusAdded", "For Dept CHE");
                }
            });
        }*/
    }
}
