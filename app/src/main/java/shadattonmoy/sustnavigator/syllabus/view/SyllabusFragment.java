package shadattonmoy.sustnavigator.syllabus.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.syllabus.controller.SyllabusAdapter;
import shadattonmoy.sustnavigator.CourseAddFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;


public class SyllabusFragment extends android.app.Fragment {

    private View view;
    private String semester;
    private Dept dept;
    private FloatingActionButton floatingActionButton;
    private ListView syllabusList;
    private ProgressBar syllabusLoadingProgress;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private boolean isEditable;
    private ArrayList<Course> courses;
    public static SyllabusAdapter adapter;
    private String session;
    public SyllabusFragment() {
        super();
    }
    public SyllabusFragment(Dept dept,String semester,boolean isEditable,String session)
    {
        this.dept = dept;
        this.isEditable = isEditable;
        this.session = session;
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
        view = inflater.inflate(R.layout.fragment_syllabus2, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_fab);
        syllabusList = (ListView) view.findViewById(R.id.syllabus_list);
        syllabusLoadingProgress = (ProgressBar) view.findViewById(R.id.syllabus_loading);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSyllabusFromServer();

    }

    public void getSyllabusFromServer()
    {
        syllabusLoadingProgress.setVisibility(View.VISIBLE);
        courses = new ArrayList<Course>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept.getDeptCode().toLowerCase()).child(semester);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Course currentCourse = child.getValue(Course.class);
                    String pushId = child.getKey();
                    currentCourse.setCourse_id(pushId);
                    courses.add(currentCourse);
                }

                adapter = new SyllabusAdapter(getActivity().getApplicationContext(),R.layout.fragment_syllabus2,R.id.course_code,courses,isEditable,getFragmentManager(),dept.getDeptCode().toLowerCase(),semester);
                syllabusList.setAdapter(adapter);
                syllabusLoadingProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(isEditable)
        {
            floatingActionButton.setVisibility(View.VISIBLE);


            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(),"Add for "+dept+" in "+semester,Toast.LENGTH_SHORT).show();
                    android.app.FragmentManager manager = getFragmentManager();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    CourseAddFragment courseAddFragment = new CourseAddFragment(getActivity().getApplicationContext(),dept.getDeptCode().toLowerCase(),semester);
                    transaction.replace(R.id.main_content_root, courseAddFragment);
                    transaction.addToBackStack("syllabus_add_fragment");
                    transaction.commit();
                }
            });
        }
        else
        {
            floatingActionButton.setVisibility(View.GONE);
        }

    }
}
