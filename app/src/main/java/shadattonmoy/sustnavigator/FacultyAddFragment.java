package shadattonmoy.sustnavigator;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FacultyAddFragment extends android.app.Fragment {
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
    public FacultyAddFragment() {

    }
    public FacultyAddFragment(String dept) {
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
        addFacultyTitle.setText("Fill the form to add faculty for "+dept+" Dept");
        nameField = (EditText) view.findViewById(R.id.teacher_add_name_field);
        designationField = (Spinner) view.findViewById(R.id.teacher_add_designation_field);
        emailField = (EditText) view.findViewById(R.id.teacher_add_email_field);
        phoneField = (EditText) view.findViewById(R.id.teacher_add_contact_no_field);
        roomField = (EditText) view.findViewById(R.id.teacher_add_room_no_field);
        fbField = (EditText) view.findViewById(R.id.teacher_add_fb_field);
        teacherAddSubmitButton = (Button) view.findViewById(R.id.teacher_add_submit_btn);
        teacherAddLoading = (ProgressBar) view.findViewById(R.id.teacher_add_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getFragmentManager();
        teacherAddLoading.setVisibility(View.GONE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(),R.id.teacher_add_name_field,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",R.string.name_error);
        awesomeValidation.addValidation(getActivity(),R.id.teacher_add_email_field, Patterns.EMAIL_ADDRESS,R.string.email_error);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),R.array.designation,R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designationField.setAdapter(adapter);
        designationField.setSelection(0);


        designationField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = (CharSequence) parent.getItemAtPosition(position);
                if(position == 0)
                    designation = "N/A";


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        teacherAddSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {

                    String name = nameField.getText().toString();
                    String email = emailField.getText().toString();
                    String phone = phoneField.getText().toString();
                    String room = roomField.getText().toString();
                    String fb = fbField.getText().toString();
                    String warning_msg = "";
                   // Toast.makeText(getActivity().getApplicationContext(),"Details : \n" + name + "\n" + designation.toString() + "\n" + email,Toast.LENGTH_LONG).show();
                    if(phone.trim().equals("") || room.trim().equals("") || fb.trim().equals(""))
                    {
                        warning_msg = "Please notice you have not added ";
                        if(phone.trim().equals(""))
                        {
                            warning_msg += "Phone";
                            phone = "N/A";
                            if(room.trim().equals(""))
                                warning_msg += ",";
                        }
                        if(room.trim().equals(""))
                        {
                            warning_msg += "Room No";
                            room = "N/A";
                            if(fb.trim().equals(""))
                                warning_msg += ",";
                        }
                        if(fb.trim().equals(""))
                        {
                            warning_msg += "Facebook ID";
                            fb = "N/A";
                            if(designation.toString().trim().equals("N/A"))
                                warning_msg += ",";
                        }
                        if(designation.toString().trim().equals("N/A"))
                        {
                            warning_msg += "Designation";
                        }
                        warning_msg += ". Do You want to continue?";
                    }
                    else warning_msg = "OK";
                    Teacher teacherToAdd = new Teacher(name,designation.toString(),room,phone,email,fb);

                    Teacher teacher = new Teacher(name,designation.toString(),room,phone,email,fb);
                    FacultyAddConfirmationDialog dialog = new FacultyAddConfirmationDialog(getActivity().getApplicationContext(),warning_msg,dept,teacher,view,fragmentManager);
                    dialog.show(getFragmentManager(),"faculty_add_confirmation");

                }
//                databaseReference = firebaseDatabase.getReference().child("teacher").child(dept);
//                databaseReference.push().setValue(new Teacher(name,designation,room,phone,email,fb));
            }
        });

    }

    public static void reset(){
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        roomField.setText("");
        fbField.setText("");
        designationField.setSelection(0);
    }
}
