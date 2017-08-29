package shadattonmoy.navigationdrawer;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class AdminFragment extends android.app.Fragment {
    private FragmentManager manager;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;

    public AdminFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        loginEmail = (EditText) view.findViewById(R.id.loginemail);
        loginPassword = (EditText) view.findViewById(R.id.loginpassword);
        loginButton = (Button) view.findViewById(R.id.loginbutton);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(),email,Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity().getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });


    }
}
