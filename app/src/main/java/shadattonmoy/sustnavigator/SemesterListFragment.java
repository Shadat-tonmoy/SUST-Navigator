package shadattonmoy.sustnavigator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.math.DoubleMath;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SemesterListFragment extends android.app.Fragment {
    private ListView semesterList;
    private View view;
    //TextView debugView;
    private String dept;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Semester> semesters = null;
    private ProgressBar progressBar;




    public SemesterListFragment() {
        // Required empty public constructor
    }
    public SemesterListFragment(String dept)
    {
        this.dept = dept;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_semester_list, container, false);
        semesterList = (ListView) view.findViewById(R.id.semesterList);
        //debugView = (TextView) view.findViewById(R.id.debugView);
        progressBar = (ProgressBar) view.findViewById(R.id.semester_list_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);

        semesters = new ArrayList<Semester>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(dept);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String txt = "";
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    txt+="\n Key : "+child.getKey()+"\n";
                    double totalCredit = 0.0;
                    int totalCourse = 0;
                    String key = child.getKey();
                    for(DataSnapshot child11 : child.getChildren()){
                        Course course = child11.getValue(Course.class);
                        String courseCredit = course.getCourse_credit();
                        double credit = Double.parseDouble(courseCredit);
                        totalCredit+=credit;
                        totalCourse++;

                    }
                    String totalCreditString = String.valueOf(totalCredit);
                    String totalCourseString = String.valueOf(totalCourse);
                    txt+=" Total Course : "+totalCourseString + "\nTotal Credit : "+totalCreditString;
                    if(key.equals("1_1"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"1st Year 1st Semester","1/1"));
                    else if(key.equals("1_2"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"1st Year 2nd Semester","1/2"));
                    else if(key.equals("2_1"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"2nd Year 1st Semester","2/1"));
                    else if(key.equals("2_2"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"2nd Year 2nd Semester","2/2"));
                    else if(key.equals("3_1"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"3rd Year 1st Semester","3/1"));
                    else if(key.equals("3_2"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"3rd Year 2nd Semester","3/2"));
                    else if(key.equals("4_1"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"4th Year 1st Semester","4/1"));
                    else if(key.equals("4_2"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"4th Year 2nd Semester","4/2"));
                    txt+="\n";
                }
                SemesterAdapter adapter = new SemesterAdapter(getActivity().getApplicationContext(),R.layout.semester_single_row,R.id.semester_icon,semesters);
                semesterList.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                //debugView.setText(txt);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        semesters.add(new Semester("5","30","2nd Year 1st Semester","2/1"));
//        semesters.add(new Semester("5","30","2nd Year 2nd Semester","2/2"));
//        semesters.add(new Semester("5","30","3rd Year 1st Semester","3/1"));
//        semesters.add(new Semester("5","30","3rd Year 2nd Semester","3/2"));
//        semesters.add(new Semester("5","30","4th Year 1st Semester","4/1"));
//        semesters.add(new Semester("5","30","4th Year 1st Semester","4/2"));


        semesterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Semester currentSemester = (Semester) parent.getItemAtPosition(position);
                String semesterCode = currentSemester.getSemesterCode();

                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                CGPAFragment cgpaFragment = new CGPAFragment(dept,semesterCode);
                transaction.replace(R.id.main_content_root,cgpaFragment);
                transaction.addToBackStack("cgpa_fragment");
                transaction.commit();
            }
        });

    }
}