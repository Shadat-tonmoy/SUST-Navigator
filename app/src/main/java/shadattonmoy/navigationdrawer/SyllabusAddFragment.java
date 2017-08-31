package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SyllabusAddFragment extends android.app.Fragment {
    private String dept,semester,courseCode,courseTitle,courseCredit;
    private View view;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText courseCodeField, courseTitleField, courseCreditField;
    private Button courseAddSubmitBtn,courseAddResetBtn;
    private Context context;


    public SyllabusAddFragment() {
        super();

    }
    public SyllabusAddFragment(Context context,String dept,String semester)
    {
        this.dept = dept;
        this.context = context;
        this.semester = semester;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_syllabus_add, container, false);
        courseCodeField = (EditText) view.findViewById(R.id.course_code_field);
        courseTitleField = (EditText) view.findViewById(R.id.course_title_field);
        courseCreditField = (EditText) view.findViewById(R.id.course_credit_field);
        courseAddSubmitBtn = (Button) view.findViewById(R.id.course_add_submit_btn);
        courseAddResetBtn = (Button) view.findViewById(R.id.course_add_reset_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        courseAddSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseCode = courseCodeField.getText().toString().trim();
                courseTitle = courseTitleField.getText().toString().trim();
                courseCredit = courseCreditField.getText().toString().trim();

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("syllabus").child(dept.toLowerCase()).child(semester);


                databaseReference.push().setValue(new Course(courseCode, courseTitle, courseCredit), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        //Toast.makeText(getActivity().getApplicationContext(),"Added...",Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(view,"Course Has Been Added",Snackbar.LENGTH_SHORT).setAction("Back", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                android.app.FragmentManager manager = getFragmentManager();
                                manager.popBackStack();
                            }
                        });
                        snackbar.show();
                    }
                });
            }
        });


        courseAddResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    public void reset()
    {
        courseCodeField.setText("");
        courseTitleField.setText("");
        courseTitleField.setText("");
        Toast.makeText(getActivity().getApplicationContext(),"All Field Has Been Reset",Toast.LENGTH_SHORT).show();
    }
}
