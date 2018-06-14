package shadattonmoy.sustnavigator.cgpa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;

import shadattonmoy.sustnavigator.AllCourseListAdapter;
import shadattonmoy.sustnavigator.cgpa.controller.CGPAAdapter;
import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;

/**
 * Created by Shadat Tonmoy on 9/5/2017.
 */

public class CourseAddForCGPADialog extends DialogFragment{
    private String dept,semester;
    private View view;
    private ListView allCourseList;
    private TextView debugView;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Course> courses;
    static HashMap<Course,Boolean> checkTaken;


    public CourseAddForCGPADialog(){
        super();
    }
    public CourseAddForCGPADialog(String dept,String semester) {
        this.dept = dept;
        this.semester = semester;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        courses = new ArrayList<Course>();
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkTaken = new HashMap<Course,Boolean>();
        view = inflater.inflate(R.layout.course_add_for_cgpa_dialog,null);
        allCourseList = (ListView) view.findViewById(R.id.all_course_list);
        progressBar = (ProgressBar) view.findViewById(R.id.all_course_list_loading);
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(dept);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String txt = "Courses : \n";

                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    String key = child.getKey();
                    if(!key.equals(semester))
                    {
                        txt+="For semester : "+key+"\n";
                        for(DataSnapshot child11 : child.getChildren())
                        {
                            Course course = child11.getValue(Course.class);
                            String code = course.getCourse_code();
                            String title = course.getCourse_title();
                            String credit = course.getCourse_credit();
                            txt+="Code : "+code+"\nTitle : "+title+"\nCredit : "+credit+"\n";
                            course.setAdded(true);
                            courses.add(course);
                        }

                    }

                }
                AllCourseListAdapter allCourseListAdapter =  new AllCourseListAdapter(getActivity().getApplicationContext(),R.layout.syllabus_single_row,R.id.course_icon,courses);
                allCourseList.setAdapter(allCourseListAdapter);
                progressBar.setVisibility(View.GONE);
                allCourseList.setOnItemClickListener(new clickListener(getActivity().getApplicationContext(),view));
                //debugView.setText(txt);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialog.setView(view);

        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        return dialog.create();
    }



}
class clickListener implements AdapterView.OnItemClickListener{
    private Context context;
    private View view;

    public clickListener(Context context,View view) {
        this.context = context;
        this.view  = view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Course course = (Course) parent.getItemAtPosition(position);


        if(CourseAddForCGPADialog.checkTaken.get(course)!=null && CourseAddForCGPADialog.checkTaken.get(course)==true )
        {
            Toast.makeText(context,course.getCourse_code()+" is already taken",Toast.LENGTH_SHORT).show();
        }
        else if (CGPAAdapter.record.get(course.getCourse_code())!=null)
        {
            Toast.makeText(context,course.getCourse_code()+" is already taken",Toast.LENGTH_SHORT).show();
        }
        else
        {
            CourseAddForCGPADialog.checkTaken.put(course,true);
            course.setAdded(true);
            CGPAFragment.adapter.add(course);
            CGPAFragment.extraCredit += Float.parseFloat(course.getCourse_credit());

            String code = course.getCourse_code();
            Snackbar snackbar = Snackbar.make(view,code+" is Added..",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CGPAFragment.adapter.remove(course);
                    CourseAddForCGPADialog.checkTaken.remove(course);
                    Toast.makeText(context,course.getCourse_code()+" is removed",Toast.LENGTH_SHORT).show();


                }
            });
            snackbar.show();

        }
    }

}
