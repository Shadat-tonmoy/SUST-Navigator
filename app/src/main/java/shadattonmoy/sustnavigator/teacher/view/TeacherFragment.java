package shadattonmoy.sustnavigator.teacher.view;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.teacher.controller.TeacherListAdapter;
import shadattonmoy.sustnavigator.TeacherContactDialog;
import shadattonmoy.sustnavigator.teacher.model.Teacher;


public class TeacherFragment extends android.app.Fragment {

    private ArrayList<Teacher> teachers = null;
    private View view = null;
    public FragmentManager manager = null;
    private String dept;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    public TeacherFragment()
    {

    }
    public TeacherFragment(String dept) {
        this.dept = dept;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.teacher_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Toast.makeText(getActivity().getApplicationContext(),dept,Toast.LENGTH_SHORT).show();
        super.onActivityCreated(savedInstanceState);

//        progressDialog.show();

        teachers = new ArrayList<Teacher>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.toLowerCase());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    Teacher currentTeachcer = child.getValue(Teacher.class);
                    teachers.add(currentTeachcer);
                    manager = getFragmentManager();
                    TeacherListAdapter adapter = new TeacherListAdapter(getActivity().getApplicationContext(),R.layout.teacher_single_row,R.id.teacher_icon,teachers,dept);
                    ListView teacherListView = (ListView) view.findViewById(R.id.teacherList);
                    teacherListView.setAdapter(adapter);
                    teacherListView.setOnItemClickListener(new detailListener(getActivity().getApplicationContext(),manager));
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

class detailListener implements AdapterView.OnItemClickListener{

    private Context context;
    FragmentManager manager;
    public detailListener(Context context,FragmentManager manager) {
        super();
        this.context = context;
        this.manager = manager;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Teacher touchedTeacher;
        SQLiteAdapter adapter = new SQLiteAdapter(context);
        touchedTeacher = (Teacher) parent.getItemAtPosition(position);
        String name = touchedTeacher.getName();
        String phone = touchedTeacher.getPhone();
        String fb = touchedTeacher.getFb();
        String email = touchedTeacher.getEmail();
        TeacherContactDialog dialog = new TeacherContactDialog(name,email,phone,fb);
        dialog.show(manager,"teacher_contact_dialog");
        //Toast.makeText(context,"Hello "+name,Toast.LENGTH_SHORT).show();
    }
}
