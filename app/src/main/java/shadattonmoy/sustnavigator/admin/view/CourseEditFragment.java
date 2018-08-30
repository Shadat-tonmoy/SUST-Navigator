package shadattonmoy.sustnavigator.admin.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.utils.Values;


public class CourseEditFragment extends android.app.Fragment {

    private String courseCode,courseTitle,courseCredit,courseId,dept,semester,session;
    private EditText courseCodeEdit,courseTitleEdit,courseCreditEdit;
    private TextView editHeader;
    private Button courseEditSubmitBtn;
    private TextView courseEditResetBtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private View view;
    private AwesomeValidation awesomeValidation;
    private Activity activity;
    private Context context;
    public CourseEditFragment() {
        // Required empty public constructor
    }
    public CourseEditFragment(String dept, String semester,String courseCode,String courseTitle, String courseCredit,String courseId,String session)
    {
        this.courseCode=courseCode;
        this.courseCredit=courseCredit;
        this.courseTitle=courseTitle;
        this.courseId = courseId;
        this.dept = dept;
        this.semester = semester;
        this.session = session;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_syllabus_add, container, false);
        courseCodeEdit = (EditText) view.findViewById(R.id.course_code_field);
        courseTitleEdit = (EditText) view.findViewById(R.id.course_title_field);
        courseCreditEdit = (EditText) view.findViewById(R.id.course_credit_field);
        courseEditSubmitBtn = (Button) view.findViewById(R.id.course_add_submit_btn);
        courseEditResetBtn = (TextView) view.findViewById(R.id.course_add_reset_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        courseCodeEdit.setText(courseCode);
        courseTitleEdit.setText(courseTitle);
        courseCreditEdit.setText(courseCredit);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.course_code_field, "^[A-Za-z0-9\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.course_code_error);
        awesomeValidation.addValidation(getActivity(), R.id.course_title_field, "^[A-Za-z0-9\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.course_title_error);
        awesomeValidation.addValidation(getActivity(), R.id.course_credit_field, "^[0-9\\.]+$", R.string.course_credit_error);

        courseEditSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Adding Record");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    String courseCode = courseCodeEdit.getText().toString();
                    String courseTitle = courseTitleEdit.getText().toString();
                    String courseCredit = courseCreditEdit.getText().toString();
                    Course updatedCourse = new Course(courseCode,courseTitle,courseCredit);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept).child(semester).child(courseId);
                    databaseReference.setValue(updatedCourse, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null)
                            {
                                Snackbar snackbar = Snackbar.make(view,"Course Details has been updated",Snackbar.LENGTH_INDEFINITE);
                                snackbar.setActionTextColor(context.getResources().getColor(R.color.blue));
                                snackbar.setAction("Back", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        android.app.FragmentManager manager = getFragmentManager();
                                        manager.popBackStack();
                                    }
                                });
                                snackbar.show();
                                progressDialog.dismiss();
                                Values.updateLastModified();
                            }
                        }
                    });
                }
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
