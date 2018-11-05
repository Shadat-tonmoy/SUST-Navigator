package shadattonmoy.sustnavigator.admin.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.model.Admin;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.dept.view.DeptAddFragment;
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;
import shadattonmoy.sustnavigator.school.model.School;
import shadattonmoy.sustnavigator.utils.SyllabusSessionBottomSheet;
import shadattonmoy.sustnavigator.utils.Values;


public class AdminSignUpForm extends android.app.Fragment {

    private EditText nameField,emailField,passwordField,regNoField;
    private TextInputLayout nameLayout,emailLayout,passwordLayout,regNoLayout;
    private View view;
    private Spinner deptSpinner;
    private Button signUpButton;
    private boolean isValid;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private CardView signUpConfirmation;
    private AwesomeValidation awesomeValidation;
    private TextView signUpMessage;
    private Context context;
    private ProgressDialog progressDialog;
    private List<String> depts;
    public AdminSignUpForm() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_sign_up_form, container, false);
        nameField = (EditText) view.findViewById(R.id.admin_signup_name_field);
        emailField = (EditText) view.findViewById(R.id.admin_signup_email_field);
        passwordField = (EditText) view.findViewById(R.id.admin_signup_password_field);
        regNoField = (EditText) view.findViewById(R.id.admin_signup_regNo_field);
        deptSpinner = (Spinner) view.findViewById(R.id.dept_spinner);
        nameLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_name_layout);
        emailLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_email_layout);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_password_layout);
        regNoLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_regNo_layout);
        signUpButton = (Button) view.findViewById(R.id.admin_signup_submit_btn);
        signUpConfirmation = (CardView) view.findViewById(R.id.admin_signup_confirmation_msg);
        signUpMessage = (TextView) view.findViewById(R.id.signup_msg);
        context = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDeptSpinner();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValid = true;
                formValidate();
                if (awesomeValidation.validate() && isValid)
                    sendRequest();
            }
        });

    }

    public void initDeptSpinner()
    {
        depts = new ArrayList<>();
        depts.add("Choose Your Department");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("schools");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    School school= child.getValue(School.class);
                    List<Dept> deptList = school.getDepts();
                    for(Dept dept:deptList)
                    {
                        depts.add(dept.getDeptCode());
//                        Log.e("DeptFromServer",dept.getDeptCode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, depts);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                deptSpinner.setAdapter(dataAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void formValidate()
    {

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(getActivity(), R.id.admin_signup_name_field, "^[A-Za-z0-9\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_error);


        awesomeValidation.addValidation(getActivity(), R.id.admin_signup_email_field, "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", R.string.email_error);


        awesomeValidation.addValidation(getActivity(), R.id.admin_signup_password_field, "^[A-Za-z0-9]{6,}$", R.string.password_error);

        awesomeValidation.addValidation(getActivity(), R.id.admin_signup_regNo_field, "^\\d{10}$", R.string.reg_no_error);

        if(deptSpinner.getSelectedItem().toString().equals("Choose Your Department"))
        {
            isValid = false;
            Toast.makeText(context,"Please Choose a department",Toast.LENGTH_SHORT).show();
        }

    }

    void sendRequest()
    {
        final String name = nameField.getText().toString();
        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();
        final String regNo = regNoField.getText().toString();
        final String dept = deptSpinner.getSelectedItem().toString();

        signUpButton.setClickable(false);
        signUpButton.setText("Please wait...");

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("admin");
        Query queryRef = databaseReference.child("admin").orderByChild("email").equalTo(email);
//        Log.e("EmailToMatch",email);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean found = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Admin admin = child.getValue(Admin.class);
//                    Log.e("AdminToMatch",admin.getEmail());
                    if(admin.getEmail().equals(email))
                    {
                        found = true;
                        signUpButton.setClickable(true);
                        signUpButton.setText("Submit");
                        signUpMessage.setText(context.getResources().getString(R.string.email_exists));
                        signUpConfirmation.setVisibility(View.VISIBLE);
                        signUpConfirmation.setCardBackgroundColor(context.getResources().getColor(R.color.warningRed));
                        break;
                    }
                }
                if(!found)
                {
                    FirebaseDatabase firebaseDatabase;
                    DatabaseReference databaseReference;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("admin");
                    databaseReference.push().setValue(new Admin(name,regNo,dept,email,password,false,false)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            signUpConfirmation.setVisibility(View.VISIBLE);
                            signUpButton.setClickable(false);
                            signUpButton.setText("Sign Up");
                            signUpMessage.setText(context.getResources().getString(R.string.signup_done));
                            signUpConfirmation.setCardBackgroundColor(context.getResources().getColor(R.color.cardBlue));
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
