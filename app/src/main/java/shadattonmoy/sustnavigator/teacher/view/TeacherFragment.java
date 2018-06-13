package shadattonmoy.sustnavigator.teacher.view;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.teacher.controller.TeacherListAdapter;
import shadattonmoy.sustnavigator.TeacherContactDialog;
import shadattonmoy.sustnavigator.teacher.model.Teacher;


public class TeacherFragment extends android.app.Fragment {

    private ArrayList<Teacher> teachers = null;
    private View view = null;
    public FragmentManager manager = null;
    private Dept dept;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextView fragmentHeader,nothingFoundText;
    private ImageView nothingFoundImage;
    private Context context;
    public TeacherFragment()
    {

    }
    public TeacherFragment(Dept dept) {
        this.dept = dept;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.teacher_loading);
        fragmentHeader = (TextView) view.findViewById(R.id.teacher_fragment_title);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentHeader.setText(dept.getDeptTitle());
        getTeachersFromServer();


    }

    public void getTeachersFromServer()
    {
        teachers = new ArrayList<Teacher>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.getDeptCode().toLowerCase());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    Teacher currentTeachcer = child.getValue(Teacher.class);
                    teachers.add(currentTeachcer);
                }
                if(teachers.size()>0)
                {
                    manager = getFragmentManager();
                    TeacherListAdapter adapter = new TeacherListAdapter(getActivity().getApplicationContext(),R.layout.teacher_single_row,R.id.teacher_icon,teachers,dept);
                    ListView teacherListView = (ListView) view.findViewById(R.id.teacherList);
                    teacherListView.setAdapter(adapter);
                    teacherListView.setOnItemClickListener(new detailListener(getActivity().getApplicationContext(),manager));
                }
                else
                {
                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    nothingFoundText.setText("OOOPS!!! No Records found for "+dept.getDeptTitle()+"Please Contact Admin");
                    try{
                        Glide.with(context).load(context.getResources()
                                .getIdentifier("nothing_found", "drawable", context.getPackageName())).thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(nothingFoundImage);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.teacher_fragment_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_teacher:
                Toast.makeText(getActivity(),
                        "Search Teacher",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
