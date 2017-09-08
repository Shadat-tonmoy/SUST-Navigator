package shadattonmoy.navigationdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CGPAFragment extends android.app.Fragment implements View.OnClickListener{

    private TextView deptTileView,cgpaLoadButton,cgpaCalculateButton,cgpaResetButton;
    private ListView courseList;
    private String dept,semester;
    private ArrayList<Course> cgpaForCourse;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButton;
    private FragmentManager manager;
    private ProgressBar progressBar;
    static CGPAAdapter adapter;

    public CGPAFragment() {

    }

    public CGPAFragment(String dept,String semester) {

        this.dept = dept;
        if(semester.equals("1/1"))
            this.semester = "1_1";
        else if(semester.equals("1/2"))
            this.semester = "1_2";
        else if(semester.equals("2/1"))
            this.semester = "2_1";
        else if(semester.equals("2/2"))
            this.semester = "2_2";
        else if(semester.equals("3/1"))
            this.semester = "3_1";
        else if(semester.equals("3/2"))
            this.semester = "3_2";
        else if(semester.equals("4/1"))
            this.semester = "4_1";
        else if(semester.equals("4/2"))
            this.semester = "4_2";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cgpa, container, false);
        //deptTileView = (TextView) view.findViewById(R.id.deptTitle);
        courseList = (ListView) view.findViewById(R.id.courseList);
        cgpaLoadButton = (TextView) view.findViewById(R.id.cgpa_load_button);
        cgpaCalculateButton = (TextView) view.findViewById(R.id.cgpa_calculate_button);
        cgpaResetButton = (TextView) view.findViewById(R.id.cgpa_reset_button);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_in_cgpa_fab);
        progressBar = (ProgressBar) view.findViewById(R.id.cgpa_fragment_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        deptTileView.setText(dept);
        cgpaForCourse=new ArrayList<Course>();
        manager = getFragmentManager();
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(dept).child(semester);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    Course course = child.getValue(Course.class);
                    cgpaForCourse.add(course);
                }
                adapter = new CGPAAdapter(getActivity().getApplicationContext(),R.layout.fragment_cgpa,R.id.cgpa_calculate_button,cgpaForCourse);
                courseList.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                CGPAAdapter.record.clear();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        CGPAAdapter.isReset = false;
        cgpaLoadButton.setOnClickListener(this);
        cgpaCalculateButton.setOnClickListener(this);
        cgpaResetButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==cgpaLoadButton.getId())
        {
            Toast.makeText(getActivity().getApplicationContext(),"Will Load the save data",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId()==cgpaCalculateButton.getId())
        {


            String result = "";
            float totalCredit=(float)0.0,totalGPA=(float)0.0,finalCGPA=(float)0.0;
//            Toast.makeText(getActivity().getApplicationContext(),"isReset = "+CGPAAdapter.isReset,Toast.LENGTH_LONG).show();
            for(int i=0;i<cgpaForCourse.size();i++)
            {
                Course cgpa = cgpaForCourse.get(i);
                String code = cgpa.getCourse_code();
                String credit = cgpa.getCourse_credit();
                String grade = (String) CGPAAdapter.record.get(code);
                if(grade==null)
                    grade="F";
                Float creditVal = Float.parseFloat(credit);
                float creditValue = creditVal.floatValue();
                float gpaValue = (float) 0.0;
                if(grade.equals("F"))
                    creditValue = (float)0.0;
                switch (grade){
                    case "F" :
                        gpaValue = (float) 0.00;
                        break;
                    case "A+" :
                        gpaValue = (float) 4.00;
                        break;
                    case "A" :
                        gpaValue = (float) 3.75;
                        break;
                    case "A-" :
                        gpaValue = (float) 3.50;
                        break;
                    case "B+" :
                        gpaValue = (float) 3.25;
                        break;
                    case "B" :
                        gpaValue = (float) 3.00;
                        break;
                    case "B-" :
                        gpaValue = (float) 2.75;
                        break;
                    case "C+" :
                        gpaValue = (float) 2.50;
                        break;
                    case "C" :
                        gpaValue = (float) 2.25;
                        break;
                    case "C-" :
                        gpaValue = (float) 2.00;
                        break;
                    default:
                        gpaValue = (float) 0.00;
                }
                totalCredit+=creditValue;
                totalGPA+=(gpaValue*creditValue);
//                result = result + "Code : "+code+" , Credit : "+creditVal+" , Grade : "+grade+" , GPA : "+gpaValue+"\n";
            }
            finalCGPA = (float) totalGPA/totalCredit;
            if(totalCredit==(float)0.0)
            {
                Toast.makeText(getActivity().getApplicationContext(),"You have not passed any course yet",Toast.LENGTH_SHORT).show();
            }
            else
            {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack("cgpa_final_show");
                CGPAShowFragment cgpaShowFragment = new CGPAShowFragment(String.format("%.2f", finalCGPA),manager);
                transaction.replace(R.id.main_content_root,cgpaShowFragment,"cgpa_final_show");
                transaction.commit();
               //Toast.makeText(getActivity().getApplicationContext(),"Your CGPA is : "+String.format("%.2f", finalCGPA),Toast.LENGTH_LONG).show();
            }
        }
        else if(v.getId()==R.id.cgpa_reset_button)
        {
            int count = courseList.getCount();
            CGPAAdapter.isReset = true;
            CGPAAdapter.record.clear();
            courseList.setAdapter(new CGPAAdapter(getActivity().getApplicationContext(),R.layout.fragment_cgpa,R.id.cgpa_calculate_button,cgpaForCourse));
            //Toast.makeText(getActivity().getApplicationContext(),"Reset "+count+" isReset = "+CGPAAdapter.isReset,Toast.LENGTH_SHORT).show();
        }
        else if(v.getId()==R.id.add_in_cgpa_fab)
        {
            Toast.makeText(getActivity().getApplicationContext(),"Add more course",Toast.LENGTH_SHORT).show();
            CourseAddForCGPADialog dialog = new CourseAddForCGPADialog(dept,semester);
            dialog.show(manager,"course_add_for_cgpa_dialog");


        }
    }
}
