package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shadat Tonmoy on 6/20/2017.
 */

public class CGPAAdapter extends ArrayAdapter<CGPA> {

    private String[] gpa = new String[]{"F","A+","A","A-","B+","B","B-","C+","C","C-"};
    ArrayList<String> gpaList = new ArrayList<String>();




    public static float totalCredit = (float)0.0;
    public static float totalGPA = (float)0.0;
    public static float cgpa = (float) 0.0;
    public static Map record = new HashMap();
    public static boolean isReset = false;



    public CGPAAdapter(Context context, int resource, int textViewResourceId, ArrayList<CGPA> objects) {
        super(context, resource, textViewResourceId, objects);
        for(int i=0;i<gpa.length;i++)
            gpaList.add(gpa[i]);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.cgpa_single_row,parent,false);
        }

        if(isReset)
        {
            Spinner cgpaListView = (Spinner) row.findViewById(R.id.cgpa_list);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.cgpa,R.layout.spinner_layout);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cgpaListView.setAdapter(adapter);
            cgpaListView.setSelection(0);
            record.clear();
            isReset=false;
        }
        else
        {

            CGPA currentCourseCgpa = getItem(position);
            String courseCode = currentCourseCgpa.getCourseCode();
            String courseTitle = currentCourseCgpa.getCourseTitle();
            String courseCredit = currentCourseCgpa.getCourseCredit();

            TextView courseCodeView = (TextView) row.findViewById(R.id.holiday_name);
            TextView courseTitleView = (TextView) row.findViewById(R.id.holiday_desc);
            TextView courseCreditView = (TextView) row.findViewById(R.id.holiday_days);
            Spinner cgpaListView = (Spinner) row.findViewById(R.id.cgpa_list);

            ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.cgpa,R.layout.spinner_layout);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cgpaListView.setAdapter(adapter);
            courseCodeView.setText(courseCode);
            courseTitleView.setText(courseTitle);
            courseCreditView.setText(courseCredit);

            if(record.get(courseCode)!=null)
            {
                String grade = record.get(courseCode).toString();
                int index = gpaList.indexOf(grade);
                cgpaListView.setSelection(index);
            }

            cgpaListView.setOnItemSelectedListener(new selectListener(getContext(),courseCredit,courseCode));
        }


        return row;
    }
}

class selectListener implements AdapterView.OnItemSelectedListener{

    Context context;
    String credit,code;
    public selectListener(Context context,String credit,String code)
    {
        this.context = context;
        this.credit = credit;
        this.code = code;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(CGPAAdapter.record.get(code)==null)
        {
            //Log.e("Msg : ","onItemSelected called for "+code+" null");
            String grade = ((TextView)view).getText().toString();
            CGPAAdapter.record.put(code,grade);
            ((TextView)view).setText(CGPAAdapter.record.get(code).toString());
        }
        else
        {
            //Log.e("Msg : ","onItemSelected called for "+code+" not null");
            String grade = ((TextView)view).getText().toString();
            CGPAAdapter.record.put(code,grade);
        }
        String viewGCP = ((TextView) view).getText().toString();
        //Toast.makeText(context,viewGCP,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(context,"Nothing ",Toast.LENGTH_SHORT).show();

    }
}
