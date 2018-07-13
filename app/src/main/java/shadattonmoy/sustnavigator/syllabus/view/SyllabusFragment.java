package shadattonmoy.sustnavigator.syllabus.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.view.ScanSyllabusFragment;
import shadattonmoy.sustnavigator.commons.controller.SemesterAdapter;
import shadattonmoy.sustnavigator.syllabus.controller.SyllabusAdapter;
import shadattonmoy.sustnavigator.admin.view.CourseAddFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;


public class SyllabusFragment extends android.app.Fragment {

    private View view;
    private String semester;
    private Dept dept;
    private FloatingActionButton customCourseButton,scanSyllabusButton,cloneSyllabusButton;
    private FloatingActionMenu floatingActionMenu;
    private ListView syllabusList;
    private ProgressBar syllabusLoadingProgress;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private boolean isEditable;
    private ArrayList<Course> courses;
    public static SyllabusAdapter adapter;
    private String session;
    private TextView nothingFoundText;
    private ImageView nothingFoundImage;
    private Context context;
    private Activity activity;
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
        context = getActivity().getApplicationContext();
        activity= getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_syllabus2, container, false);
        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.add_fab);
        customCourseButton = (FloatingActionButton) view.findViewById(R.id.custom_course_fab);
        scanSyllabusButton = (FloatingActionButton) view.findViewById(R.id.scan_syllabus_fab);
        cloneSyllabusButton = (FloatingActionButton) view.findViewById(R.id.clone_syllabus_fab);
        syllabusList = (ListView) view.findViewById(R.id.syllabus_list);
        syllabusLoadingProgress = (ProgressBar) view.findViewById(R.id.syllabus_loading);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
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

                if(courses.size()>0)
                {
                    adapter = new SyllabusAdapter(getActivity().getApplicationContext(),R.layout.fragment_syllabus2,R.id.course_code,courses,isEditable,getFragmentManager(),dept.getDeptCode().toLowerCase(),semester,session,activity);
                    syllabusList.setAdapter(adapter);
                }
                else
                {
                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    nothingFoundText.setText("OOOPS!!! No Records found for "+dept.getDeptTitle()+"  of "+session+" Session. Tap + to add");
                    try{
                        Glide.with(context).load(context.getResources()
                                .getIdentifier("nothing_found", "drawable", context.getPackageName())).thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(nothingFoundImage);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                syllabusLoadingProgress.setVisibility(View.GONE);
                if(isEditable)
                {
                    floatingActionMenu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(isEditable)
        {
            customCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Toast.makeText(getActivity().getApplicationContext(),"Add for "+dept+" in "+semester,Toast.LENGTH_SHORT).show();*/
                    android.app.FragmentManager manager = getFragmentManager();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    CourseAddFragment courseAddFragment = new CourseAddFragment(getActivity().getApplicationContext(),dept.getDeptCode().toLowerCase(),semester,session);
                    transaction.replace(R.id.main_content_root, courseAddFragment);
                    transaction.addToBackStack("syllabus_add_fragment");
                    transaction.commit();
                }
            });

            scanSyllabusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Toast.makeText(getActivity().getApplicationContext(),"Add for "+dept+" in "+semester,Toast.LENGTH_SHORT).show();*/
                    android.app.FragmentManager manager = getFragmentManager();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    ScanSyllabusFragment scanSyllabusFragment= new ScanSyllabusFragment();
//                    ScanSyllabusFragment scanSyllabusFragment= new ScanSyllabusFragment(getActivity().getApplicationContext(),dept.getDeptCode().toLowerCase(),semester,session);
                    transaction.replace(R.id.main_content_root, scanSyllabusFragment);
                    transaction.addToBackStack("syllabus_scan_fragment");
                    transaction.commit();
                }
            });
        }
        else
        {
            floatingActionMenu.setVisibility(View.GONE);
        }

    }
}
