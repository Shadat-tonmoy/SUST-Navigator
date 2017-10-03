package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminSignUpForm extends android.app.Fragment {

    private EditText nameField,emailField,passwordField,regNoField,deptField;
    private TextInputLayout nameLayout,emailLayout,passwordLayout,regNoLayout,deptLayout;
    private View view;
    private Button signUpButton;
    private boolean isValid;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private CardView signUpConfirmation;
    public AdminSignUpForm() {

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
        deptField = (EditText) view.findViewById(R.id.admin_signup_dept_field);
        nameLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_name_layout);
        emailLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_email_layout);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_password_layout);
        deptLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_dept_layout);
        regNoLayout = (TextInputLayout) view.findViewById(R.id.admin_signup_regNo_layout);
        signUpButton = (Button) view.findViewById(R.id.admin_signup_submit_btn);
        signUpConfirmation = (CardView) view.findViewById(R.id.admin_signup_confirmation_msg);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValid = true;
                formValidate();
                if (isValid)
                    sendRequest();
            }
        });

    }

    void formValidate()
    {
        if(nameField.getText().toString().isEmpty())
        {
            isValid = false;
            nameLayout.setError("Name field is empty");
        }
        else nameLayout.setErrorEnabled(false);
        if(emailField.getText().toString().isEmpty())
        {
            isValid = false;
            emailLayout.setError("Email field is empty");
        }
        else emailLayout.setErrorEnabled(false);
        if(passwordField.getText().toString().isEmpty())
        {
            isValid = false;
            passwordLayout.setError("Password field is empty");
        }
        else if (passwordField.getText().toString().length()<6)
        {
            isValid = false;
            passwordLayout.setError("Password must be at least 6 digit");
        }
        else passwordLayout.setErrorEnabled(false);
        if(regNoField.getText().toString().isEmpty())
        {
            isValid = false;
            regNoLayout.setError("Reg No field is empty");
        }
        else regNoLayout.setErrorEnabled(false);
        if(deptField.getText().toString().isEmpty())
        {
            isValid = false;
            deptLayout.setError("Department field is empty");
        }
        else deptLayout.setErrorEnabled(false);
    }

    void sendRequest()
    {

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String regNo = regNoField.getText().toString();
        String dept = deptField.getText().toString();

        signUpButton.setClickable(false);
        signUpButton.setText("Please wait...");
        signUpButton.setBackgroundColor(Color.parseColor("#80CBC4"));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("admin");
        databaseReference.push().setValue(new Admin(name,regNo,dept,email,password,false)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                signUpConfirmation.setVisibility(View.VISIBLE);
                signUpButton.setClickable(false);
                signUpButton.setText("Sign Up");
                signUpButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

    }
}
