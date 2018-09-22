package shadattonmoy.sustnavigator.dept.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.school.model.School;


public class DeptAddFragment extends android.app.Fragment {

    private Context context;
    private FragmentActivity activity;
    private View view;
    private Spinner schoolSpinner;
    private String[] schools;

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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSchoolsFromServer();

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (FragmentActivity) context;


    }


}
