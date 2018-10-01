package shadattonmoy.sustnavigator.dept.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.StaffFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;
import shadattonmoy.sustnavigator.school.model.School;
import shadattonmoy.sustnavigator.syllabus.controller.SyllabusAdapter;
import shadattonmoy.sustnavigator.syllabus.view.SyllabusDetailFragment;
import shadattonmoy.sustnavigator.utils.SyllabusSessionBottomSheet;
import shadattonmoy.sustnavigator.utils.Values;


public class DeptFragment extends android.app.Fragment{

    private String purpose;
    private AppBarLayout appBarLayout;
    private RecyclerView deptList;
    private SchoolListAdapter schoolListAdapter;
    private Context context;
    private TextView headerText,sessionText,sessionMsg;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<School> schoolList;
    private ProgressBar progressBar;
    private FragmentActivity fragmentActivity;
    public static int bottomSheetSelectedPosition=0;
    private String selectedSession;
    private SearchView searchView;
    private FloatingActionButton deptAddFab;

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
        sessionMsg = (TextView) view.findViewById(R.id.session_msg);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.dept_progressbar);
        deptAddFab = (FloatingActionButton) view.findViewById(R.id.add_dept_fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StringBuilder stringBuilder = new StringBuilder(purpose);
        String purposeTrimmed = purpose.replace("_"," ");
        String purposeFormatted = WordUtils.capitalizeFully(purposeTrimmed);
        headerText.setText(Html.fromHtml("Dept. for <b>"+ purposeFormatted +"</b>"));
        appBarLayout.setExpanded(false);
        getSchoolsFromServer(purpose);


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
                    setHasOptionsMenu(true);
                    sessionText.setVisibility(View.VISIBLE);
                    sessionMsg.setVisibility(View.VISIBLE);
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
                if(purpose.equals(Values.PURPOSE_SYLLABUS_MANAGE) || purpose.equals(Values.PURPOSE_TEACHER_MANAGE))
                {
                    deptAddFab.setVisibility(View.VISIBLE);
                    deptAddFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            DeptAddFragment deptAddFragment = new DeptAddFragment();
                            transaction.replace(R.id.main_content_root,deptAddFragment);
                            transaction.addToBackStack("deptAddFragment");
                            transaction.commit();

                        }
                    });
                }
                else deptAddFab.setVisibility(View.GONE);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.e("OnCreateOption","Menu");
        inflater.inflate(R.menu.dept_fragment_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_dept);
        searchView = new SearchView(getActivity());
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(Html.fromHtml("<font color = #ecf0f1>" + getResources().getString(R.string.dept_search_hint) + "</font>"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return false;
            }
        });

    }

    public void filterData(String query)
    {
        if(searchView!=null && !query.equals("") && query.length()>0)
        {
            ArrayList<School> filteredSchool = new ArrayList<School>();
            for(School school:schoolList)
            {
                List<Dept> depts = school.getDepts();
                List<Dept> filteredDepts = new ArrayList<>();
                for(Dept dept:depts)
                {
                    if(dept.getDeptTitle().toLowerCase().startsWith(query.toLowerCase()) || dept.getDeptTitle().toLowerCase().endsWith(query.toLowerCase())|| dept.getDeptCode().toLowerCase().startsWith(query.toLowerCase()) || dept.getDeptCode().endsWith(query.toLowerCase()))
                    {
                        filteredSchool.add(school);
                        filteredDepts.add(dept);
                    }
                }
//                school.setDepts(filteredDepts);
                schoolListAdapter.setSchools(filteredSchool);
                schoolListAdapter.notifyDataSetChanged();

            }
        }
        else
        {
            schoolListAdapter.setSchools(schoolList);
            schoolListAdapter.notifyDataSetChanged();

        }

    }
}
