package shadattonmoy.navigationdrawer;

import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CGPAShowFragment extends android.app.Fragment implements View.OnClickListener {

    private TextView gpaFinalView,cgpaFinalView,passedCreditView,totalCreditView,cgpaViewBackButton,debugView,cgpaViewSaveButton,extraCreditView,totalTakenCreditView;
    private String finalCGPA,finalGPA,totalCredit,passedCredit,extraCredit,totalTakenCredit;
    private FragmentManager manager;
    private ArrayList<Course> courseList;
    private String semester;
    public CGPAShowFragment(String finalGPA,String finalCGPA,FragmentManager manager,String semester,String passedCredit,String totalCredit,String extraCredit) {
        this.finalGPA = finalGPA;
        this.manager = manager;
        this.semester = semester;
        this.finalCGPA = finalCGPA;
        this.passedCredit = passedCredit;
        this.totalCredit = totalCredit;
        this.extraCredit = extraCredit;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cgpashow, container, false);
        cgpaFinalView = (TextView)view.findViewById(R.id.final_cgpa_view);
        cgpaViewBackButton = (TextView) view.findViewById(R.id.cgpa_view_back_button);
        cgpaViewSaveButton = (TextView) view.findViewById(R.id.cgpa_view_save_button);
        //debugView = (TextView) view.findViewById(R.id.debugView);
        //extraCreditView = (TextView) view.findViewById(R.id.extra_credit_view);
        //totalTakenCreditView = (TextView) view.findViewById(R.id.total_taken_credit_view);
        gpaFinalView = (TextView) view.findViewById(R.id.final_cgpa_view2);
        //totalCreditView = (TextView) view.findViewById(R.id.final_total_credit_view);
        passedCreditView = (TextView) view.findViewById(R.id.final_passed_credit_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //totalTakenCredit = Float.toString((Float.parseFloat(totalCredit) + Float.parseFloat(extraCredit)));
        cgpaFinalView.setText(finalCGPA);
        gpaFinalView.setText(finalGPA);
        passedCreditView.setText(passedCredit);
        //totalCreditView.setText(totalCredit);
        //extraCreditView.setText(extraCredit);
        //totalTakenCreditView.setText(totalCredit);
        float finalCGPAVal = Float.parseFloat(finalCGPA);
        float finalGPAVal = Float.parseFloat(finalGPA);
        float finalTotalCredit = Float.parseFloat(totalCredit);
        if(finalCGPAVal<(float)3.00)
        {
            cgpaFinalView.setBackgroundResource(R.drawable.round_red);
        }
        else if(finalCGPAVal<(float)3.50)
        {
            cgpaFinalView.setBackgroundResource(R.drawable.round_yellow);
        }
        if(finalGPAVal<(float)3.00)
        {
            gpaFinalView.setBackgroundResource(R.drawable.round_red);
        }
        else if(finalGPAVal<(float)3.50)
        {
            gpaFinalView.setBackgroundResource(R.drawable.round_yellow);
        }

        //cgpaFinalView.setBackground(R.drawable.round_red);

        //manager = getActivity().getFragmentManager();
        cgpaViewBackButton.setOnClickListener(this);
        cgpaViewSaveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==cgpaViewBackButton.getId())
        {
            manager.popBackStack();
            //Toast.makeText(getActivity().getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
        }
        else if(v.getId()==R.id.cgpa_view_save_button)
        {
            Toast.makeText(getActivity().getApplicationContext(),"Save ",Toast.LENGTH_SHORT).show();
            String txt = "Grades : \n";
            SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(getActivity().getApplicationContext());
            String[] semesters = {semester};
            Cursor record = sqLiteAdapter.getGPARecord(semesters);
            if(record.getCount()>0)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Record is available",Toast.LENGTH_SHORT).show();
                sqLiteAdapter.delete(semester);
                for(int i=0;i<courseList.size();i++)
                {
                    Course course = courseList.get(i);
                    String code = course.getCourse_code();
                    String title = course.getCourse_title();
                    String credit = course.getCourse_credit();
                    String grade = course.getGrade();
                    int isAdded = 0;
                    if(course.isAdded())
                        isAdded=1;
                    long id = sqLiteAdapter.insertCourse(semester,code,title,credit,grade,isAdded);
                }
                Toast.makeText(getActivity().getApplicationContext(),"Record is saved",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Will save as new data",Toast.LENGTH_SHORT).show();
                for(int i=0;i<courseList.size();i++)
                {
                    Course course = courseList.get(i);
                    String code = course.getCourse_code();
                    String title = course.getCourse_title();
                    String credit = course.getCourse_credit();
                    String grade = course.getGrade();
                    int isAdded = 0;
                    if(course.isAdded())
                        isAdded=1;
                    long id = sqLiteAdapter.insertCourse(semester,code,title,credit,grade,isAdded);

                }
                Toast.makeText(getActivity().getApplicationContext(),"Record is saved",Toast.LENGTH_SHORT).show();

            }
            //debugView.setText(txt);
        }


    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }
}
