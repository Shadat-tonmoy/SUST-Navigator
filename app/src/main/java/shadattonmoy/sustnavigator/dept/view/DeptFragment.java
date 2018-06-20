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
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;
import shadattonmoy.sustnavigator.school.model.School;
import shadattonmoy.sustnavigator.utils.SyllabusSessionBottomSheet;
import shadattonmoy.sustnavigator.utils.Values;


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
    public static int bottomSheetSelectedPosition=0;
    private String selectedSession;

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
        StringBuilder stringBuilder = new StringBuilder(purpose);
        headerText.setText(Html.fromHtml("Department for <b>"+ purpose.toUpperCase()+"</b>"));
        appBarLayout.setExpanded(false);
        getSchoolsFromServer(purpose);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int id_cse = getActivity().findViewById(R.id.dept_code_cse).getId();
        if(purpose.equals("STAFF"))
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
                if(purpose.equals("syllabus") || purpose.equals("cgpa") || purpose.equals("syllabus_manage"))
                {
                    sessionText.setVisibility(View.VISIBLE);
                    sessionText.setText(Values.getSessions().get(0));
                    sessionText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SyllabusSessionBottomSheet syllabusSessionBottomSheet = new SyllabusSessionBottomSheet(context,sessionText);
                            syllabusSessionBottomSheet.setSchoolListAdapter(schoolListAdapter);
                            syllabusSessionBottomSheet.show(fragmentActivity.getSupportFragmentManager(),"tag");

                        }
                    });
                    schoolListAdapter.setSession(sessionText.getText().toString());
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
