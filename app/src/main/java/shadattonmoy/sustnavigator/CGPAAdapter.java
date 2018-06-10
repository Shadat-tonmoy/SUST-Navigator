package shadattonmoy.sustnavigator;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shadat Tonmoy on 6/20/2017.
 */

public class CGPAAdapter extends ArrayAdapter<Course> {

    private String[] gpa = new String[]{"F","A+","A","A-","B+","B","B-","C+","C","C-"};
    ArrayList<String> gpaList = new ArrayList<String>();
    private Context context;




    public static float totalCredit = (float)0.0;
    public static float totalGPA = (float)0.0;
    public static float cgpa = (float) 0.0;
    public static Map record = new HashMap();
    public static boolean isReset = false;



    public CGPAAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Course> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        for(int i=0;i<gpa.length;i++)
            gpaList.add(gpa[i]);

    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
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

            final Course currentCourseCgpa = getItem(position);
            final String courseCode = currentCourseCgpa.getCourse_code();
            String courseTitle = currentCourseCgpa.getCourse_title();
            final String courseCredit = currentCourseCgpa.getCourse_credit();

            TextView courseCodeView = (TextView) row.findViewById(R.id.holiday_name);
            TextView courseTitleView = (TextView) row.findViewById(R.id.holiday_desc);
            TextView courseCreditView = (TextView) row.findViewById(R.id.holiday_days);
            Spinner cgpaListView = (Spinner) row.findViewById(R.id.cgpa_list);
            final ImageView moreOption = (ImageView) row.findViewById(R.id.more_option_cgpa);
            moreOption.setImageResource(R.drawable.more);

            final ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),R.array.cgpa,R.layout.spinner_layout);
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

            moreOption.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,moreOption, Gravity.LEFT);
                    popupMenu.inflate(R.menu.cgpa_menu);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId() == R.id.remove_course_for_cgpa_menu)
                            {
                                CGPAFragment.adapter.remove(currentCourseCgpa);
                                float removedCredit = Float.parseFloat(courseCredit);
                                CGPAFragment.removedCredit+=removedCredit;
                                Toast.makeText(context,courseCode+" is removed",Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            else if(item.getItemId() == R.id.edit_course_for_cgpa_menu)
                            {
                                Toast.makeText(context,"Edit Course "+courseCode,Toast.LENGTH_SHORT).show();
                                return true;
                            }


                            return false;
                        }
                    });

                }
            });
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
