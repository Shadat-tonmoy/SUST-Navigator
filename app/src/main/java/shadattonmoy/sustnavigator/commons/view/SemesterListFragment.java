package shadattonmoy.sustnavigator.commons.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
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
import java.util.List;

import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.admin.view.SemesterAddFragment;
import shadattonmoy.sustnavigator.cgpa.view.CGPAFragment;
import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.commons.controller.SemesterAdapter;
import shadattonmoy.sustnavigator.commons.model.Semester;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.syllabus.view.SyllabusFragment;
import shadattonmoy.sustnavigator.utils.Values;


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
    private TextView fragmentHeader,nothingFoundText,actAsAdmin,loadFromLocal;
    private ImageView nothingFoundImage;
    private Context context;
    private double subTotalCredit = 0.0;
    private FloatingActionButton semesterAddFab;
    private boolean actAsAdminFlag = false;
    private FragmentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FragmentActivity) context;
    }

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
        Values.IS_LOCAL_ADMIN = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_semester_list, container, false);
        semesterList = (ListView) view.findViewById(R.id.semesterList);
        //debugView = (TextView) view.findViewById(R.id.debugView);
        progressBar = (ProgressBar) view.findViewById(R.id.semester_list_loading);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);
        actAsAdmin = (TextView) view.findViewById(R.id.act_as_admin);
        loadFromLocal = (TextView) view.findViewById(R.id.load_from_local);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        semesterAddFab = (FloatingActionButton) view.findViewById(R.id.semester_add_fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        semesters = new ArrayList<Semester>();
        loadFromServer();
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

    public void handleActAsAdmin()
    {
        Values.IS_LOCAL_ADMIN = true;
        actAsAdmin.setText(context.getResources().getString(R.string.tap_to_add_semester));
        semesterAddFab.setVisibility(View.VISIBLE);

    }

    public void loadFromLocalDB()
    {
        Values.IS_LOCAL_ADMIN = true;
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
        List<String> semesterCodes = sqLiteAdapter.getSemesters();
        semesters = new ArrayList<>();
        subTotalCredit=0;
        for(String semester: semesterCodes)
        {
            double totalCredit = 0.0;
            int totalCourse = 0;
            List<Course> courses = sqLiteAdapter.getCourses(semester);
            for(Course course : courses){
                try{
                    String courseCredit = course.getCourse_credit();
                    double credit = Double.parseDouble(courseCredit);
                    totalCredit+=credit;
                    totalCourse++;
                    subTotalCredit+=credit;

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            String totalCreditString = String.valueOf(totalCredit);
            String totalCourseString = String.valueOf(totalCourse);
            if(semester.equals("1_1"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"1st Year 1st Semester","1/1"));
            else if(semester.equals("1_2"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"1st Year 2nd Semester","1/2"));
            else if(semester.equals("2_1"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"2nd Year 1st Semester","2/1"));
            else if(semester.equals("2_2"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"2nd Year 2nd Semester","2/2"));
            else if(semester.equals("3_1"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"3rd Year 1st Semester","3/1"));
            else if(semester.equals("3_2"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"3rd Year 2nd Semester","3/2"));
            else if(semester.equals("4_1"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"4th Year 1st Semester","4/1"));
            else if(semester.equals("4_2"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"4th Year 2nd Semester","4/2"));
            else if(semester.equals("5_1"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"5th Year 1st Semester","5/1"));
            else if(semester.equals("5_2"))
                semesters.add(new Semester(totalCourseString,totalCreditString,"5th Year 2nd Semester","5/2"));
        }
        if(semesters.size()>0)
        {
            nothingFoundImage.setVisibility(View.GONE);
            nothingFoundText.setVisibility(View.GONE);
            actAsAdmin.setVisibility(View.GONE);
            SemesterAdapter adapter = new SemesterAdapter(getActivity().getApplicationContext(),R.layout.semester_single_row,R.id.semester_icon,semesters);
            adapter.setActivity(activity);
            adapter.setLoadLocal(loadFromLocal);
            semesterList.setAdapter(adapter);
        }
        else
        {
            nothingFoundImage.setVisibility(View.VISIBLE);
            nothingFoundText.setVisibility(View.VISIBLE);
            actAsAdmin.setVisibility(View.GONE);
            nothingFoundText.setText("No Records found on local database Tap the + button to add. It will be saved only in your phone");
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
        semesterAddFab.setVisibility(View.VISIBLE);
        semesterAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSemesterAddFragment();
            }
        });
    }

    public void loadFromServer()
    {
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
                        try{
                            Course course = child11.getValue(Course.class);
                            String courseCredit = course.getCourse_credit();
                            double credit = Double.parseDouble(courseCredit);
                            totalCredit+=credit;
                            totalCourse++;
                            subTotalCredit+=credit;

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

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
                    else if(key.equals("5_1"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"5th Year 1st Semester","5/1"));
                    else if(key.equals("5_2"))
                        semesters.add(new Semester(totalCourseString,totalCreditString,"5th Year 2nd Semester","5/2"));
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
                    actAsAdmin.setVisibility(View.VISIBLE);
                    actAsAdmin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            handleActAsAdmin();
                        }
                    });
                    nothingFoundText.setText("No Records found for "+dept.getDeptTitle()+"  of "+session+" Session. Please Contact Admin");
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
                            loadSemesterAddFragment();
                        }
                    });
                }
                else {
                    semesterAddFab.setVisibility(View.GONE);
                    loadFromLocal.setVisibility(View.VISIBLE);
                    loadFromLocal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadFromLocalDB();
                        }
                    });
                }
                //debugView.setText(txt);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadSemesterAddFragment()
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SemesterAddFragment semesterAddFragment = new SemesterAddFragment(session,dept.getDeptCode().toLowerCase());
        transaction.replace(R.id.main_content_root,semesterAddFragment);
        transaction.addToBackStack("semester_add_fragment");
        transaction.commit();

    }
}