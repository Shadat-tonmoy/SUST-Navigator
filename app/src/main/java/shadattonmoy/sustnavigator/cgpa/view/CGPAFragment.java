package shadattonmoy.sustnavigator.cgpa.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import java.util.List;

import shadattonmoy.sustnavigator.cgpa.controller.CGPAAdapter;
import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.syllabus.controller.SyllabusAdapter;
import shadattonmoy.sustnavigator.utils.Values;


public class CGPAFragment extends android.app.Fragment implements View.OnClickListener {

    private TextView deptTileView, cgpaLoadButton, cgpaCalculateButton, cgpaResetButton;
    private ListView courseList;
    private String semester;
    private Dept dept;
    private ArrayList<Course> cgpaForCourse;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButton, addFromCurrentFab, addFromCustomFab;
    private FragmentManager manager;
    private ProgressBar progressBar;
    public static CGPAAdapter adapter;
    public static float removedCredit;
    public static float extraCredit;
    private LinearLayout moreFab;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isFabOpen = false;
    String session;
    private double subTotalCredit;
    private Context context;

    public CGPAFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public CGPAFragment(Dept dept, String semester, String session, double subTotalCredit) {

        this.dept = dept;
        this.session = session;
        this.subTotalCredit = subTotalCredit;
        if (semester.equals("1/1"))
            this.semester = "1_1";
        else if (semester.equals("1/2"))
            this.semester = "1_2";
        else if (semester.equals("2/1"))
            this.semester = "2_1";
        else if (semester.equals("2/2"))
            this.semester = "2_2";
        else if (semester.equals("3/1"))
            this.semester = "3_1";
        else if (semester.equals("3/2"))
            this.semester = "3_2";
        else if (semester.equals("4/1"))
            this.semester = "4_1";
        else if (semester.equals("4/2"))
            this.semester = "4_2";
        else if (semester.equals("5/1"))
            this.semester = "5_1";
        else if (semester.equals("5/2"))
            this.semester = "5_2";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getView();
        if(view==null)
        {
            view = inflater.inflate(R.layout.fragment_cgpa, container, false);
            courseList = (ListView) view.findViewById(R.id.courseList);
            cgpaLoadButton = (TextView) view.findViewById(R.id.cgpa_load_button);
            cgpaCalculateButton = (TextView) view.findViewById(R.id.cgpa_calculate_button);
            cgpaResetButton = (TextView) view.findViewById(R.id.cgpa_reset_button);
            floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_in_cgpa_fab);
            addFromCurrentFab = (FloatingActionButton) view.findViewById(R.id.addFromCurrentFab);
            addFromCustomFab = (FloatingActionButton) view.findViewById(R.id.addFromCustomFab);
            moreFab = (LinearLayout) view.findViewById(R.id.more_fab);
            progressBar = (ProgressBar) view.findViewById(R.id.cgpa_fragment_loading);
        }
        return view;
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("cgpaRecord",cgpaForCourse);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null)
        {
            cgpaForCourse = (ArrayList<Course>) savedInstanceState.getSerializable("cgpaRecord");
            Log.e("Restoring",cgpaForCourse.size()+" ");
        }
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabOpen = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_backward);

        removedCredit = (float) 0.0;
        extraCredit = (float) 0.0;
        progressBar.setVisibility(View.VISIBLE);
        cgpaForCourse = new ArrayList<Course>();
            if(Values.IS_LOCAL_ADMIN)
                getCoursesFromLocalDB();
            else getCoursesFromServer();
        manager = getFragmentManager();

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
                if (mLastFirstVisibleItem < firstVisibleItem) {
                    floatingActionButton.setAnimation(fabClose);
                    floatingActionButton.setVisibility(View.GONE);
                    moreFab.setVisibility(View.GONE);
                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
                    floatingActionButton.setAnimation(fabOpen);
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
                mLastFirstVisibleItem = firstVisibleItem;

            }
        });
    }

    public void getCoursesFromServer() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept.getDeptCode().toLowerCase()).child(semester);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Course course = child.getValue(Course.class);
                    cgpaForCourse.add(course);
                }
                adapter = new CGPAAdapter(getActivity().getApplicationContext(), R.layout.fragment_cgpa, R.id.cgpa_calculate_button, cgpaForCourse, getFragmentManager());
                courseList.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                CGPAAdapter.record.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCoursesFromLocalDB() {
        cgpaForCourse = new ArrayList<Course>();
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
        cgpaForCourse = (ArrayList<Course>) sqLiteAdapter.getCourses(semester);
        if (cgpaForCourse.size() > 0) {
            setHasOptionsMenu(true);
            adapter = new CGPAAdapter(getActivity().getApplicationContext(), R.layout.fragment_cgpa, R.id.cgpa_calculate_button, cgpaForCourse, getFragmentManager());
            courseList.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            CGPAAdapter.record.clear();
        }
        progressBar.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == cgpaLoadButton.getId()) {
            SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
            String[] semesters = {semester};
            Cursor cursor = sqLiteAdapter.getGPARecord(semesters);
            if (cursor.getCount() > 0) {
                int count = 0;
                cgpaForCourse.clear();
                CGPAAdapter.record.clear();
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    String semester = cursor.getString(1);
                    String code = cursor.getString(2);
                    String title = cursor.getString(3);
                    String credit = cursor.getString(4);
                    String grade = cursor.getString(5);
                    int isAdded = cursor.getInt(6);
                    Course course = new Course(code, title, credit);
                    if (isAdded > 0)
                        course.setAdded(true);
                    else course.setAdded(false);
                    course.setGrade(grade);
                    course.setLocal_id(id);
                    cgpaForCourse.add(course);
                    CGPAAdapter.record.put(code, grade);
                    count++;
                }
                if (count == 0)
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry!! No Records Found", Toast.LENGTH_SHORT).show();
                else {
                    adapter = new CGPAAdapter(getActivity().getApplicationContext(), R.layout.fragment_cgpa, R.id.cgpa_calculate_button, cgpaForCourse, getFragmentManager());
                    courseList.setAdapter(adapter);

                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Sorry!! No Records Found", Toast.LENGTH_SHORT).show();
            }


        } else if (v.getId() == cgpaCalculateButton.getId()) {


            String result = "";
            float passedCredit = (float) 0.0, totalGPA = (float) 0.0, finalGPA = (float) 0.0, finalCGPA = (float) 0.0, totalCredit = (float) 0.0;
            totalCredit += removedCredit;

            for (int i = 0; i < cgpaForCourse.size(); i++) {
                Course cgpa = cgpaForCourse.get(i);
                String code = cgpa.getCourse_code();
                String credit = cgpa.getCourse_credit();
                String grade = (String) CGPAAdapter.record.get(code);
                cgpaForCourse.get(i).setGrade(grade);
                if (grade == null)
                    grade = "F";
                Float creditVal = Float.parseFloat(credit);
                float creditValue = creditVal.floatValue();
                if (!cgpa.isAdded())
                    totalCredit += creditValue;
                float gpaValue = (float) 0.0;
                if (grade.equals("F"))
                    creditValue = (float) 0.0;
                switch (grade) {
                    case "F":
                        gpaValue = (float) 0.00;
                        break;
                    case "A+":
                        gpaValue = (float) 4.00;
                        break;
                    case "A":
                        gpaValue = (float) 3.75;
                        break;
                    case "A-":
                        gpaValue = (float) 3.50;
                        break;
                    case "B+":
                        gpaValue = (float) 3.25;
                        break;
                    case "B":
                        gpaValue = (float) 3.00;
                        break;
                    case "B-":
                        gpaValue = (float) 2.75;
                        break;
                    case "C+":
                        gpaValue = (float) 2.50;
                        break;
                    case "C":
                        gpaValue = (float) 2.25;
                        break;
                    case "C-":
                        gpaValue = (float) 2.00;
                        break;
                    default:
                        gpaValue = (float) 0.00;
                }
                passedCredit += creditValue;
                totalGPA += (gpaValue * creditValue);
            }
            if (passedCredit > 0)
                finalGPA = (float) totalGPA / passedCredit;
            else finalGPA = 0;
            finalCGPA = (float) totalGPA / passedCredit;
            SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
            String[] arr = {};
            if (semester.equals("1_2"))
                arr = new String[]{"1_1"};
            else if (semester.equals("2_1"))
                arr = new String[]{"1_1", "1_2"};
            else if (semester.equals("2_2"))
                arr = new String[]{"1_1", "1_2", "2_1"};
            else if (semester.equals("3_1"))
                arr = new String[]{"1_1", "1_2", "2_1", "2_2"};
            else if (semester.equals("3_2"))
                arr = new String[]{"1_1", "1_2", "2_1", "2_2", "3_1"};
            else if (semester.equals("4_1"))
                arr = new String[]{"1_1", "1_2", "2_1", "2_2", "3_1", "3_2"};
            else if (semester.equals("4_2"))
                arr = new String[]{"1_1", "1_2", "2_1", "2_2", "3_1", "3_2", "4_1"};
            else if (semester.equals("5_1"))
                arr = new String[]{"1_1", "1_2", "2_1", "2_2", "3_1", "3_2","4_1","4_2"};
            else if (semester.equals("5_2"))
                arr = new String[]{"1_1", "1_2", "2_1", "2_2", "3_1", "3_2","4_1","4_2","5_1"};
            Cursor cursor = sqLiteAdapter.getGPARecord(arr);
            while (cursor.moveToNext()) {
                String credit = cursor.getString(4);
                String grade = cursor.getString(5);
                int isAdded = cursor.getInt(6);
                if (grade == null)
                    grade = "F";
                Float creditVal = Float.parseFloat(credit);
                float creditValue = creditVal.floatValue();
                if (isAdded == 0)
                    totalCredit += creditValue;
                float gpaValue = (float) 0.0;
                if (grade.equals("F"))
                    creditValue = (float) 0.0;
                switch (grade) {
                    case "F":
                        gpaValue = (float) 0.00;
                        break;
                    case "A+":
                        gpaValue = (float) 4.00;
                        break;
                    case "A":
                        gpaValue = (float) 3.75;
                        break;
                    case "A-":
                        gpaValue = (float) 3.50;
                        break;
                    case "B+":
                        gpaValue = (float) 3.25;
                        break;
                    case "B":
                        gpaValue = (float) 3.00;
                        break;
                    case "B-":
                        gpaValue = (float) 2.75;
                        break;
                    case "C+":
                        gpaValue = (float) 2.50;
                        break;
                    case "C":
                        gpaValue = (float) 2.25;
                        break;
                    case "C-":
                        gpaValue = (float) 2.00;
                        break;
                    default:
                        gpaValue = (float) 0.00;
                }
                passedCredit += creditValue;
                totalGPA += (gpaValue * creditValue);
            }
            finalCGPA = (float) totalGPA / passedCredit;


            if (passedCredit == (float) 0.0) {
                Toast.makeText(getActivity().getApplicationContext(), "You have not passed any course yet", Toast.LENGTH_SHORT).show();
            } else {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack("cgpa_final_show");

                CGPAShowFragment cgpaShowFragment = new CGPAShowFragment(String.format("%.2f", finalGPA), String.format("%.2f", finalCGPA), manager, semester, String.format("%.2f", passedCredit), String.format("%.2f", totalCredit), String.format("%.2f", extraCredit), String.format("%.2f", subTotalCredit));
                cgpaShowFragment.setCourseList(cgpaForCourse);
                transaction.replace(R.id.main_content_root, cgpaShowFragment, "cgpa_final_show");
                transaction.commit();
            }
        } else if (v.getId() == R.id.cgpa_reset_button) {
            int count = courseList.getCount();
            CGPAAdapter.isReset = true;
            CGPAAdapter.record.clear();
            courseList.setAdapter(new CGPAAdapter(getActivity().getApplicationContext(), R.layout.fragment_cgpa, R.id.cgpa_calculate_button, cgpaForCourse, getFragmentManager()));
        } else if (v.getId() == R.id.add_in_cgpa_fab) {
            if (!isFabOpen) {
                floatingActionButton.startAnimation(rotateForward);
                moreFab.startAnimation(fabOpen);
                moreFab.setVisibility(View.VISIBLE);
                isFabOpen = true;
            } else {
                floatingActionButton.startAnimation(rotateBackward);
                moreFab.startAnimation(fabClose);
                moreFab.setVisibility(View.GONE);
                isFabOpen = false;

            }
        } else if (v.getId() == R.id.addFromCurrentFab) {
            CourseAddForCGPADialog dialog = new CourseAddForCGPADialog(dept.getDeptCode().toLowerCase(), semester, session);
            dialog.show(manager, "course_add_for_cgpa_dialog");
            floatingActionButton.startAnimation(rotateBackward);
            moreFab.startAnimation(fabClose);
            moreFab.setVisibility(View.GONE);
            isFabOpen = false;
        } else if (v.getId() == R.id.addFromCustomFab) {
            CustomCourseAddForCGPA dialog = new CustomCourseAddForCGPA();
            dialog.show(manager, "custom_course_add_for_cgpa_dialog");
            floatingActionButton.startAnimation(rotateBackward);
            moreFab.startAnimation(fabClose);
            moreFab.setVisibility(View.GONE);
            isFabOpen = false;
        }
    }
}
