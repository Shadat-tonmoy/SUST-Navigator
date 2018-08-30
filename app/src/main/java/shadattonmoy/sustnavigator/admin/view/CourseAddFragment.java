package shadattonmoy.sustnavigator.admin.view;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.utils.Values;


public class CourseAddFragment extends android.app.Fragment {
    private String dept,semester,courseCode,courseTitle,courseCredit,session;
    private View view;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText courseCodeField, courseTitleField, courseCreditField;
    private Button courseAddSubmitBtn;
    private TextView courseAddResetBtn;
    private Context context;
    private AwesomeValidation awesomeValidation;
    private Activity activity;


    public CourseAddFragment() {
        super();

    }
    public CourseAddFragment(Context context, String dept, String semester,String session)
    {
        this.dept = dept;
        this.context = context;
        this.semester = semester;
        this.session = session;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_syllabus_add, container, false);
        courseCodeField = (EditText) view.findViewById(R.id.course_code_field);
        courseTitleField = (EditText) view.findViewById(R.id.course_title_field);
        courseCreditField = (EditText) view.findViewById(R.id.course_credit_field);
        courseAddSubmitBtn = (Button) view.findViewById(R.id.course_add_submit_btn);
        courseAddResetBtn = (TextView) view.findViewById(R.id.course_add_reset_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.course_title_field, "^[A-Za-z0-9\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.course_title_error);
        awesomeValidation.addValidation(getActivity(), R.id.course_code_field, "^[A-Za-z0-9\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.course_code_error);
        awesomeValidation.addValidation(getActivity(), R.id.course_credit_field, "^[0-9\\.]+$", R.string.course_credit_error);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept.toLowerCase()).child(semester);


        courseAddSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Adding Record");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    courseCode = courseCodeField.getText().toString().trim();
                    courseTitle = courseTitleField.getText().toString().trim();
                    courseCredit = courseCreditField.getText().toString().trim();

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept.toLowerCase()).child(semester);


                    databaseReference.push().setValue(new Course(courseCode, courseTitle, courseCredit), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //Toast.makeText(getActivity().getApplicationContext(),"Added...",Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(view,"Course Has Been Added",Snackbar.LENGTH_INDEFINITE).setAction("Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    android.app.FragmentManager manager = getFragmentManager();
                                    manager.popBackStack();
                                }
                            }).setActionTextColor(context.getResources().getColor(R.color.blue));
                            progressDialog.dismiss();
                            snackbar.show();
                            Values.updateLastModified();

                        }
                    });
                }
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
