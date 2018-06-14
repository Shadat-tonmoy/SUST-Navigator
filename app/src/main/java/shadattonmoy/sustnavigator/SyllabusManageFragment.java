package shadattonmoy.sustnavigator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.commons.controller.SemesterAdapter;
import shadattonmoy.sustnavigator.commons.model.Semester;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.syllabus.view.SyllabusFragment;


public class SyllabusManageFragment extends android.app.Fragment {
    private View view;
    private Dept dept;
    private ListView semesterList;
    private Context context;
    private boolean isEditable;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public SyllabusManageFragment() {
        super();
    }
    public SyllabusManageFragment(Dept dept, Context context, boolean isEditable){
        this.dept = dept;
        this.context = context;
        this.isEditable = isEditable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_syllabus_manage, container, false);
        semesterList = (ListView) view.findViewById(R.id.semester_list);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Semester> semesters = new ArrayList<Semester>();
        semesters.add(new Semester("5","30","1st Year 1st Semester","1/1"));
        semesters.add(new Semester("5","30","1st Year 2nd Semester","1/2"));
        semesters.add(new Semester("5","30","2nd Year 1st Semester","2/1"));
        semesters.add(new Semester("5","30","2nd Year 2nd Semester","2/2"));
        semesters.add(new Semester("5","30","3rd Year 1st Semester","3/1"));
        semesters.add(new Semester("5","30","3rd Year 2nd Semester","3/2"));
        semesters.add(new Semester("5","30","4th Year 1st Semester","4/1"));
        semesters.add(new Semester("5","30","4th Year 1st Semester","4/2"));
        semesterList.setAdapter(new SemesterAdapter(context,R.layout.semester_single_row,R.id.semester_icon,semesters));
        semesterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Semester currentSemester = (Semester) parent.getItemAtPosition(position);
                String semesterCode = currentSemester.getSemesterCode();
                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                SyllabusFragment syllabusFragment = new SyllabusFragment(dept,semesterCode,isEditable,"2014-15");
                transaction.replace(R.id.main_content_root,syllabusFragment);
                transaction.addToBackStack("syllabus_fragment");
                transaction.commit();
            }
        });


    }
}
class ClickHandler implements View.OnClickListener{

    private String id;
    private Context context;
    public ClickHandler(String id, Context context)
    {
        this.id = id;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context,"ID --> "+id,Toast.LENGTH_SHORT).show();

    }
}
