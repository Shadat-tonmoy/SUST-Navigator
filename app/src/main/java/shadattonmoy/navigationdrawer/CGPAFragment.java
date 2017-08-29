package shadattonmoy.navigationdrawer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CGPAFragment extends android.app.Fragment implements View.OnClickListener{

    private TextView deptTileView,cgpaLoadButton,cgpaCalculateButton,cgpaResetButton;
    private ListView courseList;
    private String deptTitle;
    private ArrayList<CGPA> cgpaForCourse;

    public CGPAFragment() {

    }

    public CGPAFragment(String deptTitle) {
        this.deptTitle = deptTitle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cgpa, container, false);
        deptTileView = (TextView) view.findViewById(R.id.deptTitle);
        courseList = (ListView) view.findViewById(R.id.courseList);
        cgpaLoadButton = (TextView) view.findViewById(R.id.cgpa_load_button);
        cgpaCalculateButton = (TextView) view.findViewById(R.id.cgpa_calculate_button);
        cgpaResetButton = (TextView) view.findViewById(R.id.cgpa_reset_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deptTileView.setText(deptTitle);
        cgpaForCourse=new ArrayList<CGPA>();
        if(deptTitle.equals("Computer Science and Engineering"))
        {

            cgpaForCourse.add(new CGPA("CSE133","Structured Programming Language","3.00"));

            cgpaForCourse.add(new CGPA("CSE134","Structured Programming Language Lab","3.00"));

            cgpaForCourse.add(new CGPA("CSE143","Discrete Mathematics","3.00"));
            cgpaForCourse.add(new CGPA("CSE144","Discrete Mathematics Lab","1.50"));
            cgpaForCourse.add(new CGPA("EEE109Q","Electrical Circuits","3.00"));
            cgpaForCourse.add(new CGPA("EEE110Q","Electrical Circuits Lab","1.50"));
            cgpaForCourse.add(new CGPA("MAT102D","Matrices,Vector Analysis and Geometry","4.00"));
            cgpaForCourse.add(new CGPA("ENG101","English Language I","2.00"));
            cgpaForCourse.add(new CGPA("ENG101","English Language I Lab","1.00"));

            cgpaForCourse.add(new CGPA("CSE100","Project Work","1.00"));
            cgpaForCourse.add(new CGPA("CSE137","Data Structure","3.00"));
            cgpaForCourse.add(new CGPA("CSE138","Data Structure Lab","2.00"));
            cgpaForCourse.add(new CGPA("EEE203Q","Electric Device and Circuit","3.00"));
            cgpaForCourse.add(new CGPA("EEE204Q","Electric Device and Circuit Lab","1.50"));
            cgpaForCourse.add(new CGPA("IPE106","Engineering Graphics","1.00"));
            cgpaForCourse.add(new CGPA("IPE108","Workshop Practice","1.00"));
            cgpaForCourse.add(new CGPA("PHY103E","Mechanics,Wave,Heat and Thermodynamics","3.00"));
            cgpaForCourse.add(new CGPA("MAT103D","Calculus","4.00"));



            courseList.setAdapter(new CGPAAdapter(getActivity().getApplicationContext(),R.layout.fragment_cgpa,R.id.deptTitle,cgpaForCourse));
            CGPAAdapter.isReset = false;
            cgpaLoadButton.setOnClickListener(this);
            cgpaCalculateButton.setOnClickListener(this);
            cgpaResetButton.setOnClickListener(this);

        }
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
                CGPA cgpa = cgpaForCourse.get(i);
                String code = cgpa.getCourseCode();
                String credit = cgpa.getCourseCredit();
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
            courseList.setAdapter(new CGPAAdapter(getActivity().getApplicationContext(),R.layout.fragment_cgpa,R.id.deptTitle,cgpaForCourse));
            //Toast.makeText(getActivity().getApplicationContext(),"Reset "+count+" isReset = "+CGPAAdapter.isReset,Toast.LENGTH_SHORT).show();
        }
    }
}
