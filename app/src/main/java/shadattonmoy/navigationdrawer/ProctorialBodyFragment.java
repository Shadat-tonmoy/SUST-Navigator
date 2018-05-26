package shadattonmoy.navigationdrawer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProctorialBodyFragment extends android.app.Fragment {

    private ListView proctorList;
    private View view;
    private boolean isEditable;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Proctor> proctors;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    static ProctorAdapter adapter;

    public ProctorialBodyFragment(boolean isEditable) {
        super();
        this.isEditable = isEditable;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_proctorial_body, container, false);
        proctorList = (ListView) view.findViewById(R.id.proctor_list);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.proctor_add_fab);
        progressBar = (ProgressBar) view.findViewById(R.id.proctor_loading);
        if(isEditable)
        {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    ProctorAddFragment proctorAddFragment = new ProctorAddFragment(false);
                    transaction.replace(R.id.main_content_root,proctorAddFragment);
                    transaction.addToBackStack("proctor_add_fragment");
                    transaction.commit();
                }
            });
        }

        else floatingActionButton.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        proctors = new ArrayList<Proctor>();
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("proctor");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    String key = child.getKey();
                    Proctor proctor = child.getValue(Proctor.class);
                    proctor.setProctorId(key);
                    proctors.add(proctor);
                }
                adapter = new ProctorAdapter(getActivity().getApplicationContext(),R.layout.teacher_single_row,R.id.teacher_icon,proctors,isEditable,getFragmentManager());
                adapter.setActivity(getActivity());
                proctorList.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
