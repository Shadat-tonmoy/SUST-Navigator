package shadattonmoy.sustnavigator.dept.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.view.FacultyAddConfirmationDialog;
import shadattonmoy.sustnavigator.admin.view.TeacherAddFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.school.model.School;
import shadattonmoy.sustnavigator.teacher.model.Teacher;
import shadattonmoy.sustnavigator.utils.Values;


public class DeptAddFragment extends android.app.Fragment {

    private Context context;
    private FragmentActivity activity;
    private View view;
    private Spinner schoolSpinner;
    private String[] schools;
    private EditText nameField,codeField;
    private AwesomeValidation awesomeValidation;
    private Button deptAddSubmitButton;
    private Map<String,String > deptSchoolMap;

    public DeptAddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dept_add, container, false);
        schoolSpinner = (Spinner) view.findViewById(R.id.dept_add_school_field);
        nameField = (EditText) view.findViewById(R.id.dept_add_name_field);
        codeField = (EditText) view.findViewById(R.id.dept_add_code_field);
        deptAddSubmitButton = (Button) view.findViewById(R.id.dept_add_submit_btn);
        context = getActivity();
        activity = (FragmentActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deptSchoolMap = new HashMap<>();
        getSchoolsFromServer();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.dept_add_name_field, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_error);
        awesomeValidation.addValidation(getActivity(), R.id.dept_add_code_field, "^[A-Z]{3,3}$", R.string.dept_code_error);

        deptAddSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    String name = nameField.getText().toString();
                    String code = codeField.getText().toString();
                    String school = schoolSpinner.getSelectedItem().toString();
                    Dept dept= new Dept(name,code);
                    addDept(school,dept);

                }
            }
        });

    }
    public void getSchoolsFromServer()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("schools");
        schools = new String[500];
        final ArrayList<String> schoolNames = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    School currentSchool = child.getValue(School.class);
                    String pushId = child.getKey();
                    String schoolName = currentSchool.getSchoolTitle();
                    deptSchoolMap.put(schoolName,pushId);
                    schoolNames.add(schoolName);
                }
//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), schools, R.layout.spinner_layout);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, schoolNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                schoolSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void addDept(String school,Dept dept)
    {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Adding Record");
        dialog.setMessage("Please Wait....");
        dialog.show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(deptSchoolMap.get(school)).child("depts");
        databaseReference.push().setValue(dept).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try{
                    dialog.dismiss();
                    Toast.makeText(context,"Department Added",Toast.LENGTH_SHORT).show();
                    Values.updateLastModified();

                } catch (Exception e){

                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (FragmentActivity) context;


    }


}
