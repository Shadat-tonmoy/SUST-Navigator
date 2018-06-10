package shadattonmoy.sustnavigator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class CourseEditFragment extends android.app.Fragment {

    private String courseCode,courseTitle,courseCredit,courseId,dept,semester;
    private EditText courseCodeEdit,courseTitleEdit,courseCreditEdit;
    private TextView editHeader;
    private Button courseEditSubmitBtn,courseEditResetBtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private View view;
    public CourseEditFragment() {
        // Required empty public constructor
    }
    public CourseEditFragment(String dept, String semester,String courseCode,String courseTitle, String courseCredit,String courseId)
    {
        this.courseCode=courseCode;
        this.courseCredit=courseCredit;
        this.courseTitle=courseTitle;
        this.courseId = courseId;
        this.dept = dept;
        this.semester = semester;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_course_edit, container, false);
        courseCodeEdit = (EditText) view.findViewById(R.id.course_code_edit);
        courseTitleEdit = (EditText) view.findViewById(R.id.course_title_edit);
        courseCreditEdit = (EditText) view.findViewById(R.id.course_credit_edit);
        courseEditSubmitBtn = (Button) view.findViewById(R.id.course_edit_submit_btn);
        courseEditResetBtn = (Button) view.findViewById(R.id.course_edit_reset_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        courseCodeEdit.setText(courseCode);
        courseTitleEdit.setText(courseTitle);
        courseCreditEdit.setText(courseCredit);

        courseEditSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCode = courseCodeEdit.getText().toString();
                String courseTitle = courseTitleEdit.getText().toString();
                String courseCredit = courseCreditEdit.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(),"will edit "+courseId +" to "+courseCode+" " +courseTitle +" " +courseCredit + " in "+dept+" "+semester,Toast.LENGTH_SHORT).show();
                Course updatedCourse = new Course(courseCode,courseTitle,courseCredit);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("syllabus").child(dept).child(semester).child(courseId);
                databaseReference.setValue(updatedCourse, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null)
                        {
                            Snackbar snackbar = Snackbar.make(view,"Course Details has been updated",Snackbar.LENGTH_LONG);
                            snackbar.setAction("Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    android.app.FragmentManager manager = getFragmentManager();
                                    manager.popBackStack();

                                }
                            });
                            snackbar.show();

                        }

                    }
                });

            }
        });

        courseEditResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });


    }

    private void reset(){
        courseCodeEdit.setText("");
        courseTitleEdit.setText("");
        courseCreditEdit.setText("");
        Toast.makeText(getActivity().getApplicationContext(),"All Field has been reset",Toast.LENGTH_SHORT).show();

    }
}
