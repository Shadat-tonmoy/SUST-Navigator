package shadattonmoy.navigationdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.LinearLayout;
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
    private FloatingActionButton floatingActionButton,addFromCurrentFab,addFromCustomFab;
    private FragmentManager manager;
    private ProgressBar progressBar;
    static CGPAAdapter adapter;
    static float removedCredit;
    static float extraCredit;
    private LinearLayout moreFab;
    private Animation fabOpen,fabClose,rotateForward,rotateBackward;
    private boolean isFabOpen =false;

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
        addFromCurrentFab = (FloatingActionButton) view.findViewById(R.id.addFromCurrentFab);
        addFromCustomFab = (FloatingActionButton) view.findViewById(R.id.addFromCustomFab);
        moreFab = (LinearLayout) view.findViewById(R.id.more_fab);
        progressBar = (ProgressBar) view.findViewById(R.id.cgpa_fragment_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        deptTileView.setText(dept);
        fabOpen = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_backward);

        removedCredit = (float) 0.0;
        extraCredit = (float) 0.0;


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
        addFromCurrentFab.setOnClickListener(this);
        addFromCustomFab.setOnClickListener(this);

        courseList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    //Log.i("SCROLLING DOWN","TRUE");
                    floatingActionButton.setAnimation(fabClose);
                    floatingActionButton.setVisibility(View.GONE);
                    moreFab.setVisibility(View.GONE);
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    //Log.i("SCROLLING UP","TRUE");
                    floatingActionButton.setAnimation(fabOpen);
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
                mLastFirstVisibleItem=firstVisibleItem;

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==cgpaLoadButton.getId())
        {
            Toast.makeText(getActivity().getApplicationContext(),"Will Load the save data",Toast.LENGTH_SHORT).show();
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(getActivity().getApplicationContext());
            String[] semesters = {semester};
            Cursor cursor = sqLiteAdapter.getGPARecord(semesters);
            int count = 0;
            cgpaForCourse.clear();
            CGPAAdapter.record.clear();
            while (cursor.moveToNext())
            {
                String id = cursor.getString(0);
                String semester = cursor.getString(1);
                String code = cursor.getString(2);
                String title = cursor.getString(3);
                String credit = cursor.getString(4);
                String grade = cursor.getString(5);
                int isAdded = cursor.getInt(6);
                Course course = new Course(code,title,credit);
                if(isAdded>0)
                    course.setAdded(true);
                else course.setAdded(false);
                course.setGrade(grade);
                course.setLocal_id(id);
                cgpaForCourse.add(course);
                CGPAAdapter.record.put(code,grade);
                //Toast.makeText(getActivity().getApplicationContext(),"Id : "+id+" semester : "+semester+" code : "+code+" title : "+title+" credit : "+credit+" grade : "+grade,Toast.LENGTH_SHORT).show();
                count++;
            }
            if(count==0)
                Toast.makeText(getActivity().getApplicationContext(),"Nothing Found",Toast.LENGTH_SHORT).show();
            else
            {
                adapter = new CGPAAdapter(getActivity().getApplicationContext(),R.layout.fragment_cgpa,R.id.cgpa_calculate_button,cgpaForCourse);
                courseList.setAdapter(adapter);

            }

        }
        else if(v.getId()==cgpaCalculateButton.getId())
        {


            String result = "";
            float passedCredit=(float)0.0,totalGPA=(float)0.0,finalGPA=(float)0.0,finalCGPA=(float)0.0,totalCredit=(float)0.0;
            totalCredit+=removedCredit;

            for(int i=0;i<cgpaForCourse.size();i++)
            {
                Course cgpa = cgpaForCourse.get(i);
                String code = cgpa.getCourse_code();
                String credit = cgpa.getCourse_credit();
                String grade = (String) CGPAAdapter.record.get(code);
                cgpaForCourse.get(i).setGrade(grade);
                if(grade==null)
                    grade="F";
                Float creditVal = Float.parseFloat(credit);
                float creditValue = creditVal.floatValue();
                if(!cgpa.isAdded())
                    totalCredit+=creditValue;
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
                passedCredit+=creditValue;
                totalGPA+=(gpaValue*creditValue);
            }
            finalGPA = (float) totalGPA/passedCredit;
            finalCGPA = (float) totalGPA/passedCredit;
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(getActivity().getApplicationContext());
            String[] arr={};
            if(semester.equals("1_2"))
                arr = new String[]{"1_1"};
            else if (semester.equals("2_1"))
                arr = new String[]{"1_1","1_2"};
            else if (semester.equals("2_2"))
                arr = new String[]{"1_1","1_2","2_1"};
            else if (semester.equals("3_1"))
                arr = new String[]{"1_1","1_2","2_1","2_2"};
            else if (semester.equals("3_2"))
                arr = new String[]{"1_1","1_2","2_1","2_2","3_1"};
            else if (semester.equals("4_1"))
                arr = new String[]{"1_1","1_2","2_1","2_2","3_1","3_2"};
            else if (semester.equals("4_2"))
                arr = new String[]{"1_1","1_2","2_1","2_2","3_1","3_2","4_1"};
            Cursor cursor = sqLiteAdapter.getGPARecord(arr);
            while (cursor.moveToNext())
            {
                String credit = cursor.getString(4);
                String grade = cursor.getString(5);
                int isAdded = cursor.getInt(6);
                if(grade==null)
                    grade="F";
                Float creditVal = Float.parseFloat(credit);
                float creditValue = creditVal.floatValue();
                if(isAdded==0)
                    totalCredit+=creditValue;
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
                    passedCredit+=creditValue;
                    totalGPA+=(gpaValue*creditValue);
                }
                finalCGPA = (float) totalGPA/passedCredit;


            if(passedCredit==(float)0.0)
            {
                Toast.makeText(getActivity().getApplicationContext(),"You have not passed any course yet",Toast.LENGTH_SHORT).show();
            }
            else
            {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack("cgpa_final_show");
                CGPAShowFragment cgpaShowFragment = new CGPAShowFragment(String.format("%.2f", finalGPA),String.format("%.2f", finalCGPA),manager,semester,String.format("%.2f", passedCredit),String.format("%.2f", totalCredit),String.format("%.2f",extraCredit));
                cgpaShowFragment.setCourseList(cgpaForCourse);
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
            if(!isFabOpen)
            {
                floatingActionButton.startAnimation(rotateForward);
                moreFab.startAnimation(fabOpen);
                moreFab.setVisibility(View.VISIBLE);
                isFabOpen = true;
            }
            else
            {
                floatingActionButton.startAnimation(rotateBackward);
                moreFab.startAnimation(fabClose);
                moreFab.setVisibility(View.GONE);
                isFabOpen = false;

            }
        }
        else if(v.getId() == R.id.addFromCurrentFab)
        {
            CourseAddForCGPADialog dialog = new CourseAddForCGPADialog(dept,semester);
            dialog.show(manager,"course_add_for_cgpa_dialog");
            floatingActionButton.startAnimation(rotateBackward);
            moreFab.startAnimation(fabClose);
            moreFab.setVisibility(View.GONE);
            isFabOpen = false;
        }
        else if(v.getId() == R.id.addFromCustomFab)
        {
            CustomCourseAddForCGPA dialog = new CustomCourseAddForCGPA();
            dialog.show(manager,"custom_course_add_for_cgpa_dialog");
            floatingActionButton.startAnimation(rotateBackward);
            moreFab.startAnimation(fabClose);
            moreFab.setVisibility(View.GONE);
            isFabOpen = false;
        }
    }
}
