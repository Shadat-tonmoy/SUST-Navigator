package shadattonmoy.sustnavigator.admin.view;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.teacher.model.Teacher;


public class TeacherAddFragment extends android.app.Fragment {
    private String dept;
    private View view;
    private TextView addFacultyTitle;
    private static EditText nameField;
    private static EditText roomField;
    private static EditText phoneField;
    private static EditText emailField;
    private static EditText fbField;
    private Button teacherAddSubmitButton;
    private ProgressBar teacherAddLoading;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private AwesomeValidation awesomeValidation;
    private static Spinner designationField;
    private CharSequence designation;
    private FragmentManager fragmentManager;
    private boolean isEditing = false;
    private String facultyIdToUpdate;

    public TeacherAddFragment() {

    }

    public TeacherAddFragment(String dept) {
        this.dept = dept;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faculty_add, container, false);
        addFacultyTitle = (TextView) view.findViewById(R.id.add_faculty_header);
        addFacultyTitle.setText("Fill the form to add faculty for " + dept + " Dept");
        nameField = (EditText) view.findViewById(R.id.teacher_add_name_field);
        designationField = (Spinner) view.findViewById(R.id.teacher_add_designation_field);
        emailField = (EditText) view.findViewById(R.id.teacher_add_email_field);
        phoneField = (EditText) view.findViewById(R.id.teacher_add_contact_no_field);
        roomField = (EditText) view.findViewById(R.id.teacher_add_room_no_field);
        fbField = (EditText) view.findViewById(R.id.teacher_add_fb_field);
        teacherAddSubmitButton = (Button) view.findViewById(R.id.teacher_add_submit_btn);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.designation, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designationField.setAdapter(adapter);

        Bundle args = getArguments();
        isEditing = false;
        if (args != null) {
            isEditing = getArguments().getBoolean("isEditing", false);
            if (isEditing) {
                nameField.setText(args.getString("name"));
                emailField.setText(args.getString("email"));
                phoneField.setText(args.getString("phone"));
                roomField.setText(args.getString("room"));
                fbField.setText(args.getString("fb"));
                String designation = args.getString("designation");
                facultyIdToUpdate = args.getString("id");
                String[] designationArray = getActivity().getResources().getStringArray(R.array.designation);
                for (int i = 0; i < designationArray.length; i++) {
                    if (designationArray[i].equals(designation)) {
                        designationField.setSelection(i);
                        break;
                    }
                }
                teacherAddSubmitButton.setText("Update");
            }
        } else designationField.setSelection(0);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getFragmentManager();
        firebaseDatabase = FirebaseDatabase.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.teacher_add_name_field, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_error);
        awesomeValidation.addValidation(getActivity(), R.id.teacher_add_email_field, Patterns.EMAIL_ADDRESS, R.string.email_error);
        designationField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = (CharSequence) parent.getItemAtPosition(position);
                if (position == 0)
                    designation = "N/A";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        teacherAddSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    String name = nameField.getText().toString();
                    String email = emailField.getText().toString();
                    String phone = phoneField.getText().toString();
                    String room = roomField.getText().toString();
                    String fb = fbField.getText().toString();
                    String warning_msg = "";
                    if (phone.trim().equals("") || room.trim().equals("") || fb.trim().equals("")) {
                        warning_msg = "Please notice you have not added ";
                        if (phone.trim().equals("")) {
                            warning_msg += "Phone ";
                            phone = "N/A";
                            if (room.trim().equals(""))
                                warning_msg += ",";
                        }
                        if (room.trim().equals("")) {
                            warning_msg += "Room No ";
                            room = "N/A";
                            if (fb.trim().equals(""))
                                warning_msg += ",";
                        }
                        if (fb.trim().equals("")) {
                            warning_msg += "Facebook ID ";
                            fb = "N/A";
                            if (designation.toString().trim().equals("N/A"))
                                warning_msg += ",";
                        }
                        if (designation.toString().trim().equals("N/A")) {
                            warning_msg += "Designation ";
                        }
                        warning_msg += ". Do You want to continue?";
                    } else warning_msg = "OK";

                    Teacher teacher = new Teacher(name, designation.toString(), room, phone, email, fb);
                    FacultyAddConfirmationDialog dialog = new FacultyAddConfirmationDialog(getActivity().getApplicationContext(), warning_msg, dept, teacher, view, fragmentManager,isEditing);
                    if(isEditing)
                        dialog.setFacultyIdToUpdate(facultyIdToUpdate);
                    dialog.show(getFragmentManager(), "faculty_add_confirmation");

                }
            }
        });

    }

    public static void reset() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        roomField.setText("");
        fbField.setText("");
        designationField.setSelection(0);
    }
}
