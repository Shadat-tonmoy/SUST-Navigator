package shadattonmoy.sustnavigator.cgpa.view;

import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.SQLiteAdapter;


public class CGPAShowFragment extends android.app.Fragment implements View.OnClickListener {

    private TextView gpaFinalView,cgpaFinalView,passedCreditView,totalCreditView,cgpaViewBackButton,cgpaViewSaveButton;
    private ImageView fragmentBg;
    private String finalCGPA,finalGPA,totalCredit,passedCredit,extraCredit,totalTakenCredit;
    private FragmentManager manager;
    private ArrayList<Course> courseList;
    private String semester,subTotalCredit;
    private Context context;
    public CGPAShowFragment(String finalGPA,String finalCGPA,FragmentManager manager,String semester,String passedCredit,String totalCredit,String extraCredit,String subTotalCredit) {
        this.finalGPA = finalGPA;
        this.manager = manager;
        this.semester = semester;
        this.finalCGPA = finalCGPA;
        this.passedCredit = passedCredit;
        this.totalCredit = totalCredit;
        this.extraCredit = extraCredit;
        this.subTotalCredit = subTotalCredit;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cgpashow, container, false);
        cgpaFinalView = (TextView)view.findViewById(R.id.final_cgpa_view);
        cgpaViewBackButton = (TextView) view.findViewById(R.id.cgpa_view_back_button);
        cgpaViewSaveButton = (TextView) view.findViewById(R.id.cgpa_view_save_button);
        fragmentBg = (ImageView) view.findViewById(R.id.cgpa_show_bg);
        gpaFinalView = (TextView) view.findViewById(R.id.final_cgpa_view2);
        totalCreditView = (TextView) view.findViewById(R.id.total_credit_view);
        passedCreditView = (TextView) view.findViewById(R.id.final_passed_credit_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cgpaFinalView.setText(finalCGPA);
        gpaFinalView.setText(finalGPA);
        passedCreditView.setText(passedCredit);
        totalCreditView.setText(subTotalCredit);
        try{
            Glide.with(context).load(context.getResources()
                    .getIdentifier("cgpa_show_bg", "drawable", context.getPackageName())).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fragmentBg);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        float finalCGPAVal = Float.parseFloat(finalCGPA);
        float finalGPAVal = Float.parseFloat(finalGPA);
        float finalTotalCredit = Float.parseFloat(totalCredit);
        float finalSubTotalCredit = Float.parseFloat(subTotalCredit);
        float creditPercent = ((finalTotalCredit/finalSubTotalCredit)*100);
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
        if(creditPercent<(float)50.00)
        {
            passedCreditView.setBackgroundResource(R.drawable.round_red);
        }
        else if(finalGPAVal<(float)75.00)
        {
            passedCreditView.setBackgroundResource(R.drawable.round_yellow);
        }
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
//            Toast.makeText(getActivity().getApplicationContext(),"Save ",Toast.LENGTH_SHORT).show();
            String txt = "Grades : \n";
            SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
            String[] semesters = {semester};
            Cursor record = sqLiteAdapter.getGPARecord(semesters);
            if(record.getCount()>0)
            {
//                Toast.makeText(getActivity().getApplicationContext(),"Record is available",Toast.LENGTH_SHORT).show();
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
                    long id = sqLiteAdapter.insertCourseCGPA(semester,code,title,credit,grade,isAdded);
                }
                Toast.makeText(getActivity().getApplicationContext(),"Record is saved",Toast.LENGTH_SHORT).show();
            }
            else
            {
//                Toast.makeText(getActivity().getApplicationContext(),"Will save as new data",Toast.LENGTH_SHORT).show();
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
                    long id = sqLiteAdapter.insertCourseCGPA(semester,code,title,credit,grade,isAdded);

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
