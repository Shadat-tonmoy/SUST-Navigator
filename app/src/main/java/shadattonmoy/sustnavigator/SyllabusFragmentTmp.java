package shadattonmoy.sustnavigator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SyllabusFragmentTmp extends android.app.Fragment {


//    ArrayList<Course> courses11, courses12,courses21,courses22,courses31,courses32;
//    TextView semesterTitle;
//    ListView syllabus;
//    ArrayList<SemesterCourse> semesterCourse;
//    public SyllabusFragmentTmp() {
//        // Required empty public constructor
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_syllabus, container, false);
//        syllabus = (ListView) view.findViewById(R.id.syllabus);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        semesterCourse = new ArrayList<SemesterCourse>();
//
//        courses11 = new ArrayList<Course>();
//        courses11.add(new Course("CSE133","Structured Programming Language","3.00","CSE"));
//        courses11.add(new Course("CSE134","Structured Programming Language Lab","3.00","CSE"));
//        courses11.add(new Course("CSE143","Discrete Mathematics","3.00","CSE"));
//        courses11.add(new Course("CSE144","Discrete Mathematics Lab","1.50","CSE"));
//        courses11.add(new Course("EEE109Q","Electrical Circuits","3.00","EEE"));
//        courses11.add(new Course("EEE110Q","Electrical Circuits Lab","1.50","EEE"));
//        courses11.add(new Course("MAT102D","Matrices,Vector Analysis and Geometry","4.00","MAT"));
//        courses11.add(new Course("ENG101","English Language I","2.00","ENG"));
//        courses11.add(new Course("ENG101","English Language I Lab","1.00","ENG"));
//
//
//        courses12 = new ArrayList<Course>();
//        courses12.add(new Course("CSE100","Project Work","1.00","CSE"));
//        courses12.add(new Course("CSE137","Data Structure","3.00","CSE"));
//        courses12.add(new Course("CSE138","Data Structure Lab","2.00","CSE"));
//        courses12.add(new Course("EEE203Q","Electric Device and Circuit","3.00","EEE"));
//        courses12.add(new Course("EEE204Q","Electric Device and Circuit Lab","1.50","EEE"));
//        courses12.add(new Course("IPE106","Engineering Graphics","1.00","IPE"));
//        courses12.add(new Course("IPE108","Workshop Practice","1.00","IPE"));
//        courses12.add(new Course("PHY103E","Mechanics,Wave,Heat and Thermodynamics","3.00","PHY"));
//        courses12.add(new Course("MAT103D","Calculus","4.00","MAT"));
//
//
//        courses21 = new ArrayList<Course>();
//        courses21.add(new Course("EEE201Q","Digital Logic Design","3.00","EEE"));
//        courses21.add(new Course("EEE202Q","Digital Logic Design Lab","2.00","EEE"));
//        courses21.add(new Course("CSE237","Algorithm Design and Analysis","3.00","CSE"));
//        courses21.add(new Course("CSE238","Algorithm Design and Analysis Lab","1.50","CSE"));
//        courses21.add(new Course("BUS243","Cost and Management Accounting","3.00","BUS"));
//        courses21.add(new Course("PHY207E","Electromagnetics,Optics and Modern Physics","3.00","PHY"));
//        courses21.add(new Course("PHY202B","Basic Physics Lab","1.50","PHY"));
//        courses21.add(new Course("STA202","Basic Statistics and Probability","4.00","STA"));
//
//
//        semesterCourse.add(new SemesterCourse("First Year First Semester",courses11));
//        semesterCourse.add(new SemesterCourse("First Year Second Semester",courses12));
//        semesterCourse.add(new SemesterCourse("Second Year First Semester",courses21));
//
//        syllabus.setAdapter(new MySyllabusAdapter(getActivity().getApplicationContext(),R.layout.syllabus_single_row,R.id.holiday_name, semesterCourse));
//
//    }
//
//    public class MySyllabusAdapter extends ArrayAdapter {
//
//        public MySyllabusAdapter(Context context, int resource, int textViewResourceId, ArrayList<SemesterCourse> objects) {
//            super(context, resource, textViewResourceId, objects);
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View row = convertView;
//            if(row==null)
//            {
//                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = inflater.inflate(R.layout.syllabus_single_row,parent,false);
//            }
//
//            SemesterCourse currentSemesterCourse = (SemesterCourse) getItem(position);
//            String titleOfSemester = currentSemesterCourse.getTitleOfSemester();
//            ArrayList<Course> coursesOfSemester = currentSemesterCourse.getCoursesOfSemester();
//
//            TextView semesterTitleView = (TextView) row.findViewById(R.id.semesterTitle);
//            semesterTitleView.setText(titleOfSemester);
//
//            int[] course_code_ids = {R.id.holiday_name,R.id.course_code2,R.id.course_code3,R.id.course_code4,R.id.course_code5,R.id.course_code6,R.id.course_code7,R.id.course_code8,R.id.course_code9,R.id.course_code10};
//
//            int[] course_title_ids = {R.id.holiday_desc,R.id.course_title2,R.id.course_title3,R.id.course_title4,R.id.course_title5,R.id.course_title6,R.id.course_title7,R.id.course_title8,R.id.course_title9,R.id.course_title10};
//
//            int[] course_credit_ids = {R.id.holiday_days,R.id.course_credit2,R.id.course_credit3,R.id.course_credit4,R.id.course_credit5,R.id.course_credit6,R.id.course_credit7,R.id.course_credit8,R.id.course_credit9,R.id.course_credit10};
//
//            int[] course_dept_ids = {R.id.holiday_date,R.id.course_dept2,R.id.course_dept3,R.id.course_dept4,R.id.course_dept5,R.id.course_dept6,R.id.course_dept7,R.id.course_dept8,R.id.course_dept9,R.id.course_dept10};
//
//            int[] edit_icon_ids = {R.id.edit_course_icon1,R.id.edit_course_icon2,R.id.edit_course_icon3,R.id.edit_course_icon4,R.id.edit_course_icon5,R.id.edit_course_icon6,R.id.edit_course_icon7,R.id.edit_course_icon8,R.id.edit_course_icon9,R.id.edit_course_icon10};
//
//
//            for(int i=0;i<10;i++)
//            {
//
//                if(i<coursesOfSemester.size())
//                {
//                    Course currentCourse = coursesOfSemester.get(i);
//                    final String courseCode = currentCourse.getCourse_code();
//                    final String courseTitle = currentCourse.getCourse_title();
//                    String courseCredit = currentCourse.getCourse_credit();
//                    final String courseCreditForEdit = courseCredit;
//                    courseCredit+=" Credit";
//                    String courseDept = currentCourse.getCourse_detp();
//
//                    TextView courseCodeView = (TextView) row.findViewById(course_code_ids[i]);
//                    TextView courseTitleView = (TextView) row.findViewById(course_title_ids[i]);
//                    TextView courseCreditView = (TextView) row.findViewById(course_credit_ids[i]);
//                    TextView courseDeptView = (TextView) row.findViewById(course_dept_ids[i]);
//                    ImageView courseEditIcon = (ImageView) row.findViewById(edit_icon_ids[i]);
//
//                    courseEditIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //Toast.makeText(getContext(),"Edit : "+courseTitle, Toast.LENGTH_SHORT).show();
//                            android.app.FragmentManager manager = getFragmentManager();
//                            CourseEditFragment courseEditfragment = new CourseEditFragment(courseCode,courseTitle,courseCreditForEdit);
//                            android.app.FragmentTransaction transaction = manager.beginTransaction();
//                            transaction.replace(R.id.main_content_root,courseEditfragment);
//                            transaction.addToBackStack("course_edit_fragment");
//                            transaction.commit();
//
//
//
//
//                        }
//                    });
//
//                    courseCodeView.setText(courseCode);
//                    courseTitleView.setText(courseTitle);
//                    courseCreditView.setText(courseCredit);
//                    courseDeptView.setText(courseDept);
//                    courseEditIcon.setImageResource(R.drawable.edit_icon);
//
//                }
//                else
//                {
//                    TextView courseCodeView = (TextView) row.findViewById(course_code_ids[i]);
//                    TextView courseTitleView = (TextView) row.findViewById(course_title_ids[i]);
//                    TextView courseCreditView = (TextView) row.findViewById(course_credit_ids[i]);
//                    TextView courseDeptView = (TextView) row.findViewById(course_dept_ids[i]);
//                    ImageView courseEditIcon = (ImageView) row.findViewById(edit_icon_ids[i]);
//
//                    courseCodeView.setVisibility(View.GONE);
//                    courseTitleView.setVisibility(View.GONE);
//                    courseCreditView.setVisibility(View.GONE);
//                    courseDeptView.setVisibility(View.GONE);
//                    courseEditIcon.setVisibility(View.GONE);
//
//                }
//            }
//            return row;
//        }
//    }












}
