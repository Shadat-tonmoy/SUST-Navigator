package shadattonmoy.sustnavigator.admin.view;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.model.Admin;


public class PasswordResetFragment extends android.app.Fragment {
    private View view;
    private Context context;
    private String session,dept,semester,sessionToClone;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView passwordResetMsg;
    private EditText resetEmail;
    private Button resetButton;
    ProgressBar progressBar;
    private  AlertDialog.Builder builder;
    private AwesomeValidation awesomeValidation;
    private FragmentActivity fragmentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        fragmentActivity = (FragmentActivity) context;
    }

    public PasswordResetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_password_reset,container,false);
        resetButton= (Button) view.findViewById(R.id.reset_button);
        progressBar = (ProgressBar) view.findViewById(R.id.reset_progress);
        resetEmail = (EditText) view.findViewById(R.id.reset_email);
        passwordResetMsg = (TextView) view.findViewById(R.id.reset_msg);
        initialize();
        return view;
    }

    private void initialize()
    {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidEmail();
                if(awesomeValidation.validate())
                {
                    String email = resetEmail.getText().toString();
                    checkValidEmailOnServer(email);

                }

            }
        });


    }

    public void checkValidEmail()
    {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(fragmentActivity, R.id.reset_email, "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", R.string.email_error);
    }

    public void checkValidEmailOnServer(final String emailToCheck)
    {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Query queryRef = databaseReference.child("admin").orderByChild("email").equalTo(emailToCheck);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Admin admin = child.getValue(Admin.class);
                    String pushId = child.getKey();
                    admin.setId(pushId);
                    String name = admin.getName();
                    String regNo = admin.getRegNo();
                    String dept = admin.getDept();
                    if(admin.getEmail().equals(emailToCheck))
                    {
                        found = true;
                        break;
                    }
                }
                if(found)
                {
                    sendPasswordResetEmail(emailToCheck);

                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    passwordResetMsg.setVisibility(View.VISIBLE);
                    passwordResetMsg.setText("No Admin found with this email");
                    passwordResetMsg.setTextColor(context.getResources().getColor(R.color.textRed));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendPasswordResetEmail(String email)
    {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            passwordResetMsg.setVisibility(View.VISIBLE);
                            passwordResetMsg.setText("Password Reset Email is sent. Please check your email to reset password");
                            passwordResetMsg.setTextColor(context.getResources().getColor(R.color.blue));

                        }
                    }
                });
    }



}
