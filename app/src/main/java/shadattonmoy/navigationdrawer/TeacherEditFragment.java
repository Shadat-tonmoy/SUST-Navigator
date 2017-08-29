package shadattonmoy.navigationdrawer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TeacherEditFragment extends android.app.Fragment {

    private String name,designation,room,phone,email,id,dept,fb;
    private View view;
    private EditText nameField,designationField,roomField,phoneField,emailField,fbField;
    private Button submitButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    public TeacherEditFragment() {
        super();
    }
    public TeacherEditFragment(String name, String designation, String room, String phone, String email,String id,String dept,String fb)
    {
        this.name = name;
        this.designation = designation;
        this.room = room;
        this.phone = phone;
        this.email = email;
        this.id = id;
        this.dept = dept;
        this.fb = fb;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_teacher_edit, container, false);
        nameField = (EditText) view.findViewById(R.id.teacher_edit_name_field);
        designationField = (EditText) view.findViewById(R.id.teacher_edit_designation_field);
        emailField = (EditText) view.findViewById(R.id.teacher_edit_email_field);
        phoneField = (EditText) view.findViewById(R.id.teacher_edit_contact_no_field);
        roomField = (EditText) view.findViewById(R.id.teacher_edit_room_no_field);
        fbField = (EditText) view.findViewById(R.id.teacher_edit_fb_field);
        submitButton = (Button) view.findViewById(R.id.teacher_edit_submit_btn);
        progressBar = (ProgressBar) view.findViewById(R.id.teacher_update_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nameField.setText(this.name);
        designationField.setText(this.designation);
        emailField.setText(this.email);
        phoneField.setText(this.phone);
        roomField.setText(this.room);
        fbField.setText(this.fb);
        progressBar.setVisibility(View.GONE);
        firebaseDatabase = FirebaseDatabase.getInstance();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);


                String name = nameField.getText().toString();
                String designation = designationField.getText().toString();
                String email = emailField.getText().toString();
                String phone = phoneField.getText().toString();
                String room = roomField.getText().toString();
                String fb = fbField.getText().toString();
                Teacher teacher = new Teacher(name,designation,room,phone,email,fb);
                databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.toLowerCase()).child(id);

                databaseReference.setValue(teacher, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        //Toast.makeText(getActivity().getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar.make(view,"Updated",Snackbar.LENGTH_LONG).setAction("Back", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                android.app.FragmentManager manager = getFragmentManager();
                                manager.popBackStack();

                            }
                        });
                        snackbar.setActionTextColor(Color.rgb(230, 126, 34));
                        snackbar.show();



                    }
                });
            }
        });
    }
}
