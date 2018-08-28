package shadattonmoy.sustnavigator.admin.view;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.utils.Values;


public class AdminFragment extends android.app.Fragment {
    private FragmentManager manager;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    private TextInputLayout emailLayout,passwordLayout;
    private boolean isValid;
    private CardView loginErrorMsg;
    private TextView notAnAdminView;
    private AppBarLayout appBarLayout;
    private FragmentActivity fragmentActivity;
    private AwesomeValidation awesomeValidation;

    public AdminFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_login, container, false);
        loginEmail = (EditText) view.findViewById(R.id.loginemail);
        loginPassword = (EditText) view.findViewById(R.id.loginpassword);
        loginButton = (Button) view.findViewById(R.id.loginbutton);
        emailLayout = (TextInputLayout) view.findViewById(R.id.login_email_layout);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.login_password_layout);
        loginErrorMsg = (CardView) view.findViewById(R.id.login_error_msg);
        notAnAdminView = (TextView) view.findViewById(R.id.not_an_admint_btn);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        appBarLayout.setExpanded(false);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValid = true;
                validateForm();
                if(awesomeValidation.validate())
                {
                    Values.hideKeyboard(fragmentActivity);
                    sendLoginRequest();
                }
            }
        });
        notAnAdminView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpForm();
            }
        });


    }

    void validateForm()
    {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(getActivity(), R.id.loginemail, "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", R.string.email_error);


        awesomeValidation.addValidation(getActivity(), R.id.loginpassword, "^[A-Za-z0-9]+$", R.string.password_error);

        /*if(loginEmail.getText().toString().isEmpty())
        {
            isValid = false;
            emailLayout.setError("Email field is empty");
        }
        else
        {
            emailLayout.setErrorEnabled(false);
        }
        if(loginPassword.getText().toString().isEmpty())
        {
            isValid = false;
            passwordLayout.setError("Password field is empty");
        }
        else
        {
            passwordLayout.setErrorEnabled(false);
        }*/
    }
    void sendLoginRequest()
    {
        loginErrorMsg.setVisibility(View.GONE);
        loginButton.setClickable(false);
        loginButton.setText("Please wait...");
        loginButton.setBackgroundColor(Color.parseColor("#80CBC4"));
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    android.app.FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    AdminPanelFragment adminPanelFragment = new AdminPanelFragment();
                    transaction.replace(R.id.main_content_root,adminPanelFragment);
                    transaction.addToBackStack("admin_panel_fragment");
                    transaction.commit();
                }
                else
                {
                    loginErrorMsg.setVisibility(View.VISIBLE);
                    loginButton.setClickable(true);
                    loginButton.setText("LOGIN");
                    loginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
            }
        });

    }

    void openSignUpForm()
    {
        android.app.FragmentManager manager = getFragmentManager();
        AdminSignUpForm signUpForm = new AdminSignUpForm();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_content_root,signUpForm);
        transaction.addToBackStack("admin_signup");
        transaction.commit();
    }
}
