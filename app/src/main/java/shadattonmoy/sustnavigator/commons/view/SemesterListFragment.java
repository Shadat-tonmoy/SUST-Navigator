package shadattonmoy.sustnavigator.commons.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.admin.view.SemesterAddFragment;
import shadattonmoy.sustnavigator.cgpa.view.CGPAFragment;
import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.commons.controller.SemesterAdapter;
import shadattonmoy.sustnavigator.commons.model.Semester;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.syllabus.view.SyllabusFragment;


public class SemesterListFragment extends android.app.Fragment {
    private ListView semesterList;
    private View view;
    //TextView debugView;
    private Dept dept;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Semester> semesters = null;
    private ProgressBar progressBar;
    private String purpose,session;
    private boolean isSyllabusEditable;
    private TextView fragmentHeader,nothingFoundText;
    private ImageView nothingFoundImage;
    private Context context;
    private double subTotalCredit = 0.0;
    private FloatingActionButton semesterAddFab;




    public SemesterListFragment() {
        // Required empty public constructor
    }
    public SemesterListFragment(Dept dept,String purpose,String session)
    {
        this.dept = dept;
        this.purpose = purpose;
        this.session = session;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_semester_list, container, false);
        semesterList = (ListView) view.findViewById(R.id.semesterList);
        //debugView = (TextView) view.findViewById(R.id.debugView);
        progressBar = (ProgressBar) view.findViewById(R.id.semester_list_loading);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        semesterAddFab = (FloatingActionButton) view.findViewById(R.id.semester_add_fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        semesters = new ArrayList<Semester>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept.getDeptCode().trim().toLowerCase());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String txt = "";
                subTotalCredit=0;
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
                        subTotalCredit+=credit;

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
                    Log.e("TotalCredit",subTotalCredit+"");
                }
                Log.e("Semesters",txt);
                if(semesters.size()>0)
                {
                    SemesterAdapter adapter = new SemesterAdapter(getActivity().getApplicationContext(),R.layout.semester_single_row,R.id.semester_icon,semesters);
                    semesterList.setAdapter(adapter);
                }
                else
                {
                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    nothingFoundText.setText("OOOPS!!! No Records found for "+dept.getDeptTitle()+"  of "+session+" Session. Please Contact Admin");
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

                progressBar.setVisibility(View.GONE);
                if(isSyllabusEditable)
                {
                    semesterAddFab.setVisibility(View.VISIBLE);
                    semesterAddFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            SemesterAddFragment semesterAddFragment = new SemesterAddFragment(session,dept.getDeptCode().toLowerCase());
                            transaction.replace(R.id.main_content_root,semesterAddFragment);
                            transaction.addToBackStack("semester_add_fragment");
                            transaction.commit();
                        }
                    });
                }
                else semesterAddFab.setVisibility(View.GONE);
                //debugView.setText(txt);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(purpose.equals("cgpa"))
        {
            semesterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Semester currentSemester = (Semester) parent.getItemAtPosition(position);
                    String semesterCode = currentSemester.getSemesterCode();

                    android.app.FragmentManager manager = getFragmentManager();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    CGPAFragment cgpaFragment = new CGPAFragment(dept,semesterCode,session,subTotalCredit);
                    transaction.replace(R.id.main_content_root,cgpaFragment);
                    transaction.addToBackStack("cgpa_fragment");
                    transaction.commit();
                }
            });
        }
        else if(purpose.equals("syllabus"))
        {
            semesterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Semester currentSemester = (Semester) parent.getItemAtPosition(position);
                    String semesterCode = currentSemester.getSemesterCode();
                    android.app.FragmentManager manager = getFragmentManager();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    SyllabusFragment syllabusFragment = new SyllabusFragment(dept,semesterCode,isSyllabusEditable,session);
                    transaction.replace(R.id.main_content_root,syllabusFragment);
                    transaction.addToBackStack("syllabus_fragment");
                    transaction.commit();
                }
            });
        }
        else if(purpose.equals("syllabus_manage"))
        {
            Log.e("Purpose","syllabus_manage");
            semesterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Semester currentSemester = (Semester) parent.getItemAtPosition(position);
                    String semesterCode = currentSemester.getSemesterCode();
                    android.app.FragmentManager manager = getFragmentManager();
                    android.app.FragmentTransaction transaction = manager.beginTransaction();
                    SyllabusFragment syllabusFragment = new SyllabusFragment(dept,semesterCode,isSyllabusEditable,session);
                    transaction.replace(R.id.main_content_root,syllabusFragment);
                    transaction.addToBackStack("syllabus_manage_fragment");
                    transaction.commit();
                }
            });
        }

    }

    public void setSyllabusEditable(boolean syllabusEditable) {
        isSyllabusEditable = syllabusEditable;
    }
}