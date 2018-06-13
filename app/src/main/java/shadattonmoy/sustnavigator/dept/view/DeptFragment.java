package shadattonmoy.sustnavigator.dept.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.StaffFragment;
import shadattonmoy.sustnavigator.TeacherManageFragment;
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;
import shadattonmoy.sustnavigator.school.model.School;
import shadattonmoy.sustnavigator.utils.SyllabusSessionBottomSheet;


public class DeptFragment extends android.app.Fragment implements View.OnClickListener{

    private String purpose;
    private AppBarLayout appBarLayout;
    private RecyclerView deptList;
    private SchoolListAdapter schoolListAdapter;
    private Context context;
    private TextView headerText,sessionText;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<School> schoolList;
    private ProgressBar progressBar;
    private FragmentActivity fragmentActivity;

    public DeptFragment() {

    }


    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurpose() {
        return purpose;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dept_fragment, container, false);
        deptList = (RecyclerView) view.findViewById(R.id.school_list);
        headerText = (TextView) view.findViewById(R.id.dept_header_msg);
        sessionText = (TextView) view.findViewById(R.id.dept_session_msg);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.dept_progressbar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headerText.setText(Html.fromHtml("Choose Department for <b>"+ purpose.toLowerCase()+"</b>"));
        appBarLayout.setExpanded(false);
        getSchoolsFromServer(purpose);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int id_cse = getActivity().findViewById(R.id.dept_code_cse).getId();
        int id_eee = getActivity().findViewById(R.id.dept_code_eee).getId();
        int id_ipe = getActivity().findViewById(R.id.dept_code_ipe).getId();
        int id_cee = getActivity().findViewById(R.id.dept_code_cee).getId();
        int id_cep = getActivity().findViewById(R.id.dept_code_cep).getId();
        int id_mee = getActivity().findViewById(R.id.dept_code_mee).getId();
        int id_fet = getActivity().findViewById(R.id.dept_code_fet).getId();
        int id_pme = getActivity().findViewById(R.id.dept_code_pme).getId();
        int id_che = getActivity().findViewById(R.id.dept_code_che).getId();
        if(purpose.equals("TEACHER_MANAGE"))
        {
            if(id==id_cse)
            {
                FragmentManager manager = getFragmentManager();
                TeacherManageFragment cseTeacherManageFragment = new TeacherManageFragment("CSE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherManageFragment,"cse_teacher_fragment");
                transaction.addToBackStack("cse_teacher_manage_fragment");
                transaction.commit();
            }

            else if(id==id_eee)
            {
                FragmentManager manager = getFragmentManager();
                TeacherManageFragment eeeTeacherManageFragment = new TeacherManageFragment("EEE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,eeeTeacherManageFragment,"eee_teacher_fragment");
                transaction.addToBackStack("eee_teacher_manage_fragment");
                transaction.commit();

            }
            else if(id==id_ipe)
            {
                FragmentManager manager = getFragmentManager();
                TeacherManageFragment ipeTeacherManageFragment = new TeacherManageFragment("IPE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,ipeTeacherManageFragment,"ipe_teacher_fragment");
                transaction.addToBackStack("ipe_teacher_manage_fragment");
                transaction.commit();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                /*FragmentManager manager = getFragmentManager();
                TeacherFragment cseTeacherFragment = new TeacherFragment("CEP");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherFragment,"cep_teacher_fragment");
                transaction.addToBackStack("cep_teacher_fragment");
                transaction.commit();*/
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"PME",Toast.LENGTH_SHORT).show();
            }
        }
        else if(purpose.equals("SYLLABUS_MANAGE"))
        {
            if(id==id_cse)
            {
                /*FragmentManager manager = getFragmentManager();
                SyllabusManageFragment syllabusManageFragment= new SyllabusManageFragment("cse",getActivity().getApplicationContext(),true);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,syllabusManageFragment,"syllabus_manage_fragment");
                transaction.addToBackStack("syllabus_manage_fragment");
                transaction.commit();*/
            }

            else if(id==id_eee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus EEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_ipe)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus IPE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus CEP",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus PME",Toast.LENGTH_SHORT).show();
            }
        }

        else if(purpose.equals("CGPA"))
        {
            if(id==id_cse)
            {
                /*FragmentManager manager = getFragmentManager();
                SemesterListFragment semesterListFragment = new SemesterListFragment ("cse");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,semesterListFragment,"semester_list_fragment");
                transaction.addToBackStack("semester_list_fragment");
                transaction.commit();*/
            }

            else if(id==id_eee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA EEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_ipe)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA IPE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA CEP",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA PME",Toast.LENGTH_SHORT).show();
            }
        }
        else if(purpose.equals("STAFF"))
        {

            if(id==id_cse)
            {
                FragmentManager manager = getFragmentManager();
                StaffFragment staffFragment= new StaffFragment ("cse");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,staffFragment,"staff_fragment");
                transaction.addToBackStack("staff_fragment");
                transaction.commit();
            }

            else if(id==id_eee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Staff EEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_ipe)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Staff IPE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Staff CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Staff CEP",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Staff MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Staff FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Staff PME",Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void getSchoolsFromServer(final String purpose)
    {
        schoolList = new ArrayList<School>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("schools");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    School school= child.getValue(School.class);
                    schoolList.add(school);
                    Log.e("GettingData",school.getSchoolTitle());
                }
                progressBar.setVisibility(View.GONE);
                schoolListAdapter = new SchoolListAdapter(schoolList,context,getFragmentManager(),purpose);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                deptList.setLayoutManager(mLayoutManager);
                deptList.setItemAnimator(new DefaultItemAnimator());
                deptList.setAdapter(schoolListAdapter);
                if(purpose.equals("syllabus"))
                {
                    sessionText.setVisibility(View.VISIBLE);
                    sessionText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SyllabusSessionBottomSheet syllabusSessionBottomSheet = new SyllabusSessionBottomSheet(context);
                            syllabusSessionBottomSheet.show(fragmentActivity.getSupportFragmentManager(),"tag");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onAttach(Activity activity) {
        fragmentActivity=(FragmentActivity) activity;
        super.onAttach(activity);
    }
}
