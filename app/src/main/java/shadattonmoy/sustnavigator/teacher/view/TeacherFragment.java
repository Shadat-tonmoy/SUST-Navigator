package shadattonmoy.sustnavigator.teacher.view;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionMenu;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import shadattonmoy.sustnavigator.admin.view.TeacherAddFragment;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.teacher.controller.TeacherListAdapter;
import shadattonmoy.sustnavigator.teacher.model.Teacher;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class TeacherFragment extends android.app.Fragment {

    private ArrayList<Teacher> teachers = null;
    private View view = null;
    public FragmentManager manager = null;
    private Dept dept;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextView fragmentHeader, nothingFoundText;
    private ImageView nothingFoundImage;
    private Context context;
    private boolean isAdmin;
    private FloatingActionMenu addMoreTeacherFab;
    private com.github.clans.fab.FloatingActionButton addCustomTeacher,grabFromWebsite;
    private TeacherListAdapter adapter;
    private BottomDialog bottomDialog;
    private SearchView searchView;
    private ListView teacherListView;
    public TeacherFragment() {

    }

    public TeacherFragment(Dept dept) {
        this.dept = dept;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        Log.e("isAdmin",isAdmin+"");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher, container, false);
        progressBar = view.findViewById(R.id.teacher_loading);
        fragmentHeader = view.findViewById(R.id.teacher_fragment_title);
        nothingFoundText = view.findViewById(R.id.nothing_found_txt);
        nothingFoundImage = view.findViewById(R.id.nothing_found_image);
        addMoreTeacherFab =  view.findViewById(R.id.add_more_teacher_fab);
        addCustomTeacher =  view.findViewById(R.id.custom_teacher_fab);
        grabFromWebsite =  view.findViewById(R.id.grab_teacher_fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentHeader.setText(dept.getDeptTitle());
        getTeachersFromServer();


    }

    public void getTeachersFromServer() {
        teachers = new ArrayList<Teacher>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.getDeptCode().toLowerCase());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Teacher currentTeachcer = child.getValue(Teacher.class);
                    currentTeachcer.setId(child.getKey());
                    teachers.add(currentTeachcer);
                }
                if (teachers.size() > 0) {
                    setHasOptionsMenu(true);
                    manager = getFragmentManager();
                    adapter = new TeacherListAdapter(getActivity().getApplicationContext(), R.layout.teacher_single_row, R.id.teacher_icon, teachers, dept);
                    adapter.setFragmentManager(getFragmentManager());
                    Log.e("isAdmin",isAdmin+"");
                    teacherListView = (ListView) view.findViewById(R.id.teacherList);
                    teacherListView.setAdapter(adapter);
                    teacherListView.setOnItemClickListener(new detailListener(getActivity().getApplicationContext(), manager));
                    if (isAdmin) {
                        adapter.setAdmin(isAdmin);
                        adapter.setView(view);
                        adapter.setActivity(getActivity());
                        addMoreTeacherFab.setVisibility(View.VISIBLE);
                        addMoreTeacherFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                android.app.FragmentManager manager = getFragmentManager();
                                android.app.FragmentTransaction transaction = manager.beginTransaction();
                                TeacherAddFragment teacherAddFragment = new TeacherAddFragment(dept.getDeptCode());
                                transaction.replace(R.id.main_content_root, teacherAddFragment);
                                transaction.addToBackStack("teacher_add_fragment");
                                transaction.commit();
                            }
                        });
                        /*teacherListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                                if(scrollState == SCROLL_STATE_IDLE){
                                    addMoreTeacherFab.animate().cancel();
                                    addMoreTeacherFab.setVisibility(View.GONE);
                                }else{
                                    addMoreTeacherFab.animate().cancel();
                                    addMoreTeacherFab.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                            }
                        });*/

                    }

                } else {
                    setHasOptionsMenu(false);
                    if (isAdmin) {
                        addMoreTeacherFab.setVisibility(View.VISIBLE);
                        addMoreTeacherFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                android.app.FragmentManager manager = getFragmentManager();
                                android.app.FragmentTransaction transaction = manager.beginTransaction();
                                TeacherAddFragment teacherAddFragment = new TeacherAddFragment(dept.getDeptCode());
                                transaction.replace(R.id.main_content_root, teacherAddFragment);
                                transaction.addToBackStack("teacher_add_fragment");
                                transaction.commit();
                            }
                        });
                        /*teacherListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                                if(scrollState == SCROLL_STATE_IDLE){
                                    addMoreTeacherFab.animate().cancel();
                                    addMoreTeacherFab.setVisibility(View.GONE);
                                }else{
                                    addMoreTeacherFab.animate().cancel();
                                    addMoreTeacherFab.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                            }
                        });*/

                    }
                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    if(isAdmin)
                        nothingFoundText.setText("Sorry!! No Records found for " + dept.getDeptTitle() + "Tap the '+' Button to add");
                    else nothingFoundText.setText("Sorry!! No Records found for " + dept.getDeptTitle() + "Please Contact Admin");
                    try {
                        Glide.with(context).load(context.getResources()
                                .getIdentifier("nothing_found", "drawable", context.getPackageName())).thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(nothingFoundImage);
                    } catch (Exception e) {
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

    public View generateSortingOptionBottomSheet() {
        View sortingOptions = null;
        LinearLayout nameAsc, nameDesc, designationAsc, designationDesc;
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            try {
                sortingOptions = inflater.inflate(R.layout.teacher_sorting_bottom_sheet, null, false);
                nameAsc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_name_asc);
                nameDesc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_name_desc);
                designationAsc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_designation_asc);
                designationDesc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_designation_desc);
                nameAsc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Name Asc ", Toast.LENGTH_SHORT).show();
                        sortTeacherList("name", false);
                        Log.e("SortingOption", "Sorted By Name Asc");

                    }
                });

                nameDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortTeacherList("name", true);
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Name Desc ", Toast.LENGTH_SHORT).show();

                    }
                });

                designationAsc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortTeacherList("designation", false);
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Designation Asc ", Toast.LENGTH_SHORT).show();
                    }
                });

                designationDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Designation Desc ", Toast.LENGTH_SHORT).show();
                        sortTeacherList("designation", true);

                    }
                });
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());

            }


        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return sortingOptions;
    }

    public void sortTeacherList(String parameter, final boolean desc) {
        hideSortingBottomSheet();
        if (parameter.equals("name")) {
            Collections.sort(teachers, new Comparator<Teacher>() {
                @Override
                public int compare(Teacher o1, Teacher o2) {
                    String name1 = o1.getName();
                    String name2 = o2.getName();
                    if (name1.compareTo(name2) >= 0) {
                        if (desc)
                            return -1;
                        else return 1;
                    } else {
                        if (desc)
                            return 1;
                        else return -1;
                    }
                }
            });
        }

        if (parameter.equals("designation")) {
            Collections.sort(teachers, new Comparator<Teacher>() {
                @Override
                public int compare(Teacher o1, Teacher o2) {
                    int designationPoint1 = -1;
                    int designationPoint2 = -1;
                    String designation1 = o1.getDesignation();
                    String designation2 = o2.getDesignation();
                    switch (designation1) {
                        case "Professor":
                            designationPoint1 = 4;
                            break;
                        case "Associate Professor":
                            designationPoint1 = 3;
                            break;
                        case "Assistant Professor":
                            designationPoint1 = 2;
                            break;
                        case "Lecturer":
                            designationPoint1 = 1;
                            break;
                    }

                    switch (designation2) {
                        case "Professor":
                            designationPoint2 = 4;
                            break;
                        case "Associate Professor":
                            designationPoint2 = 3;
                            break;
                        case "Assistant Professor":
                            designationPoint2 = 2;
                            break;
                        case "Lecturer":
                            designationPoint2 = 1;
                            break;
                    }

                    if (designationPoint1>=designationPoint2) {
                        if (desc)
                            return -1;
                        else return 1;
                    } else {
                        if (desc)
                            return 1;
                        else return -1;
                    }
                }
            });
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.teacher_fragment_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_teacher);
        searchView = new SearchView(getActivity());
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search Here");
        searchView.setQueryHint(Html.fromHtml("<font color = #ecf0f1>" + getResources().getString(R.string.teacher_search_hint) + "</font>"));

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
        ArrayList<Teacher> filteredTeacher = new ArrayList<Teacher>();
        if(searchView!=null)
        {
            for(Teacher teacher:teachers)
            {
                if(teacher.getName().toLowerCase().startsWith(query.toLowerCase()) || teacher.getName().toLowerCase().endsWith(query.toLowerCase())|| teacher.getDesignation().toLowerCase().startsWith(query.toLowerCase()) || teacher.getDesignation().endsWith(query.toLowerCase())
                        || teacher.getRoom().toLowerCase().startsWith(query.toLowerCase()) || teacher.getRoom().endsWith(query.toLowerCase())
                        || teacher.getPhone().toLowerCase().startsWith(query.toLowerCase()) || teacher.getPhone().endsWith(query.toLowerCase()))
                {
                    filteredTeacher.add(teacher);
                }
                //else filteredVendors=vendors;
                adapter = new TeacherListAdapter(context,R.layout.teacher_single_row,R.id.teacher_icon,filteredTeacher,dept);
                teacherListView.setAdapter(adapter);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_teacher:
                Toast.makeText(getActivity(),
                        "Search Teacher",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_teacher:
                showSortingBottomSheet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSortingBottomSheet() {
        bottomDialog = new BottomDialog.Builder(getActivity())
                .setTitle("Sort By")
                .setCustomView(generateSortingOptionBottomSheet())
                .setCancelable(true)
                .show();
    }

    public void hideSortingBottomSheet() {
        bottomDialog.dismiss();
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


}

class detailListener implements AdapterView.OnItemClickListener {

    private Context context;
    FragmentManager manager;

    public detailListener(Context context, FragmentManager manager) {
        super();
        this.context = context;
        this.manager = manager;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Teacher touchedTeacher;
        touchedTeacher = (Teacher) parent.getItemAtPosition(position);
        String name = touchedTeacher.getName();
        String phone = touchedTeacher.getPhone();
        String fb = touchedTeacher.getFb();
        String email = touchedTeacher.getEmail();
        TeacherContactDialog dialog = new TeacherContactDialog(name, email, phone, fb);
        dialog.show(manager, "teacher_contact_dialog");
        //Toast.makeText(context,"Hello "+name,Toast.LENGTH_SHORT).show();
    }
}
