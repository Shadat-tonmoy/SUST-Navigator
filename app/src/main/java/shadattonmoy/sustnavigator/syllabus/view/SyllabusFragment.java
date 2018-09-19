package shadattonmoy.sustnavigator.syllabus.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import com.github.clans.fab.FloatingActionButton;
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

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.admin.view.ScanSyllabusFragment;
import shadattonmoy.sustnavigator.commons.controller.SemesterAdapter;
import shadattonmoy.sustnavigator.commons.model.Semester;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.syllabus.controller.SyllabusAdapter;
import shadattonmoy.sustnavigator.admin.view.CourseAddFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.teacher.controller.TeacherListAdapter;
import shadattonmoy.sustnavigator.teacher.model.Teacher;
import shadattonmoy.sustnavigator.utils.SyllabusSessionBottomSheet;
import shadattonmoy.sustnavigator.utils.Values;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class SyllabusFragment extends android.app.Fragment {

    private View view;
    private String semester;
    private Dept dept;
    private FloatingActionButton customCourseButton,scanSyllabusButton,cloneSyllabusButton;
    private FloatingActionMenu floatingActionMenu;
    private ListView syllabusList;
    private ProgressBar syllabusLoadingProgress;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private boolean isEditable;
    private ArrayList<Course> courses;
    public static SyllabusAdapter adapter;
    private String session;
    private TextView nothingFoundText;
    private ImageView nothingFoundImage;
    private Context context;
    private Activity activity;
    private BottomDialog bottomDialog;
    private SearchView searchView;
    private FragmentActivity fragmentActivity;
    public SyllabusFragment() {
        super();
    }

    public SyllabusFragment(Dept dept,String semester,boolean isEditable,String session)
    {
        this.dept = dept;
        this.isEditable = isEditable;
        this.session = session;
        this.semester = Values.getSemesterCode(semester);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        activity= getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_syllabus2, container, false);
        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.add_fab);
        customCourseButton = (FloatingActionButton) view.findViewById(R.id.custom_course_fab);
        scanSyllabusButton = (FloatingActionButton) view.findViewById(R.id.scan_syllabus_fab);
        cloneSyllabusButton = (FloatingActionButton) view.findViewById(R.id.clone_syllabus_fab);
        syllabusList = (ListView) view.findViewById(R.id.syllabus_list);
        syllabusLoadingProgress = (ProgressBar) view.findViewById(R.id.syllabus_loading);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(Values.IS_LOCAL_ADMIN)
            getSyllabusFromLocalDB();
        else getSyllabusFromServer();

    }

    public void getSyllabusFromServer()
    {
        syllabusLoadingProgress.setVisibility(View.VISIBLE);
        courses = new ArrayList<Course>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept.getDeptCode().toLowerCase()).child(semester);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Course currentCourse = child.getValue(Course.class);
                    String pushId = child.getKey();
                    currentCourse.setCourse_id(pushId);
                    courses.add(currentCourse);
                }

                if(courses.size()>0)
                {
                    setHasOptionsMenu(true);
                    adapter = new SyllabusAdapter(getActivity().getApplicationContext(),R.layout.fragment_syllabus2,R.id.course_code,courses,isEditable,getFragmentManager(),dept.getDeptCode().toLowerCase(),semester,session,activity);
                    syllabusList.setAdapter(adapter);
                    syllabusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Course clickedCourse = (Course) adapterView.getItemAtPosition(i);
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            SyllabusDetailFragment syllabusDetailFragment = new SyllabusDetailFragment();
                            Bundle args = new Bundle();
                            args.putSerializable("course",clickedCourse);
                            args.putString("session",session);
                            args.putString("semester",semester);
                            args.putString("dept",dept.getDeptCode().toLowerCase());
                            if(isEditable)
                            {
                                args.putBoolean("isAdmin",true);
                            }
                            syllabusDetailFragment.setArguments(args);
                            transaction.replace(R.id.main_content_root,syllabusDetailFragment);
                            transaction.addToBackStack("syllabusDetailFragment");
                            transaction.commit();
                        }
                    });
                }
                else
                {
                    setHasOptionsMenu(false);
                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    nothingFoundText.setText("No Records found for "+dept.getDeptTitle()+"  of "+session+" Session. Tap + to add");
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
                syllabusLoadingProgress.setVisibility(View.GONE);
                if(isEditable)
                {
                    floatingActionMenu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(isEditable)
        {
            setFloatActionMenuHandler();
        }
        else
        {
            floatingActionMenu.setVisibility(View.GONE);
        }

    }

    public void getSyllabusFromLocalDB()
    {
        courses = new ArrayList<Course>();
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
        List<Course> courses = sqLiteAdapter.getCourses(semester);
        if(courses.size()>0)
        {
            setHasOptionsMenu(true);
            adapter = new SyllabusAdapter(getActivity().getApplicationContext(),R.layout.fragment_syllabus2,R.id.course_code, (ArrayList<Course>) courses,isEditable,getFragmentManager(),dept.getDeptCode().toLowerCase(),semester,session,activity);
            syllabusList.setAdapter(adapter);
            floatingActionMenu.setVisibility(View.VISIBLE);
            setFloatActionMenuHandler();
            syllabusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    /*Course clickedCourse = (Course) adapterView.getItemAtPosition(i);
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    SyllabusDetailFragment syllabusDetailFragment = new SyllabusDetailFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("course",clickedCourse);
                    args.putString("session",session);
                    args.putString("semester",semester);
                    args.putString("dept",dept.getDeptCode().toLowerCase());
                    if(isEditable)
                    {
                        args.putBoolean("isAdmin",true);
                    }
                    syllabusDetailFragment.setArguments(args);
                    transaction.replace(R.id.main_content_root,syllabusDetailFragment);
                    transaction.addToBackStack("syllabusDetailFragment");
                    transaction.commit();*/
                }
            });
        }
        else
        {
            setHasOptionsMenu(false);
            nothingFoundImage.setVisibility(View.VISIBLE);
            nothingFoundText.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.VISIBLE);
            setFloatActionMenuHandler();
            nothingFoundText.setText("No Records found on local Database. Tap + to add");
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
        syllabusLoadingProgress.setVisibility(View.GONE);

    }

    public void setFloatActionMenuHandler()
    {
        customCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getActivity().getApplicationContext(),"Add for "+dept+" in "+semester,Toast.LENGTH_SHORT).show();*/
                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                CourseAddFragment courseAddFragment = new CourseAddFragment(getActivity().getApplicationContext(),dept.getDeptCode().toLowerCase(),semester,session);
                transaction.replace(R.id.main_content_root, courseAddFragment);
                transaction.addToBackStack("syllabus_add_fragment");
                transaction.commit();
            }
        });

        scanSyllabusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getActivity().getApplicationContext(),"Add for "+dept+" in "+semester,Toast.LENGTH_SHORT).show();*/
                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                ScanSyllabusFragment scanSyllabusFragment= new ScanSyllabusFragment();
                Bundle args = new Bundle();
                args.putString("session",session);
                args.putString("semester",semester);
                args.putString("dept",dept.getDeptCode().toLowerCase());
                if(isEditable)
                {
                    args.putBoolean("isAdmin",true);
                }
                scanSyllabusFragment.setArguments(args);
//                    ScanSyllabusFragment scanSyllabusFragment= new ScanSyllabusFragment(getActivity().getApplicationContext(),dept.getDeptCode().toLowerCase(),semester,session);
                transaction.replace(R.id.main_content_root, scanSyllabusFragment);
                transaction.addToBackStack("syllabus_scan_fragment");
                transaction.commit();
            }
        });

        if(Values.IS_LOCAL_ADMIN)
        {
            cloneSyllabusButton.setVisibility(View.GONE);
        }
        else
        {
            cloneSyllabusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SyllabusSessionBottomSheet syllabusSessionBottomSheet = new SyllabusSessionBottomSheet(context,true,dept.getDeptCode().toLowerCase(),semester,session);
                    syllabusSessionBottomSheet.show(fragmentActivity.getSupportFragmentManager(),"syllabusSessionBottomSheet");
                }
            });
        }


    }

    public View generateSortingOptionBottomSheet() {
        View sortingOptions = null;
        LinearLayout courseCreditAsc,courseCreditDesc, courseCodeAsc, courseCodeDesc,courseTitleAsc, courseTitleDesc;
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            try {
                sortingOptions = inflater.inflate(R.layout.syllabus_sorting_bottom_sheet, null, false);
                courseCodeAsc = (LinearLayout) sortingOptions.findViewById(R.id.course_code_asc);
                courseCodeDesc = (LinearLayout) sortingOptions.findViewById(R.id.course_code_desc);
                courseTitleAsc  = (LinearLayout) sortingOptions.findViewById(R.id.course_title_asc);
                courseTitleDesc  = (LinearLayout) sortingOptions.findViewById(R.id.course_title_desc);
                courseCreditAsc = (LinearLayout) sortingOptions.findViewById(R.id.course_credit_asc);
                courseCreditDesc  = (LinearLayout) sortingOptions.findViewById(R.id.course_credit_desc);

                courseCodeAsc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Course Code Ascending ", Toast.LENGTH_SHORT).show();
                        sortSyllabus("code", false);

                    }
                });

                courseCodeDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Course Code Descending ", Toast.LENGTH_SHORT).show();
                        sortSyllabus("code", true);

                    }
                });

                courseTitleAsc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Course Title Ascending ", Toast.LENGTH_SHORT).show();
                        sortSyllabus("title", false);

                    }
                });

                courseTitleDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Course Title Descending ", Toast.LENGTH_SHORT).show();
                        sortSyllabus("title", true);

                    }
                });

                courseCreditAsc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Course Credit Ascending ", Toast.LENGTH_SHORT).show();
                        sortSyllabus("credit", false);

                    }
                });

                courseCreditDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorted By Course Credit Descending ", Toast.LENGTH_SHORT).show();
                        sortSyllabus("credit", true);

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

    public void sortSyllabus(String parameter, final boolean desc) {
        hideSortingBottomSheet();
        if (parameter.equals("code")) {
            Collections.sort(courses, new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    String code1 = o1.getCourse_code();
                    String code2 = o2.getCourse_code();
                    if (code1.compareTo(code2) >= 0) {
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

        else if (parameter.equals("title")) {
            Collections.sort(courses, new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    String title1 = o1.getCourse_title();
                    String title2 = o2.getCourse_title();
                    if (title1.compareTo(title2) >= 0) {
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
        else if (parameter.equals("credit")) {
            Collections.sort(courses, new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    double credit1 = 0.0;
                    double credit2 = 0.0;
                    try {
                        credit1 = Double.parseDouble(o1.getCourse_credit());
                        credit2 = Double.parseDouble(o2.getCourse_credit());
                    }catch (Exception e)
                    {
                        Log.e("Exception",e.getMessage());
                    }

                    if (credit1>=credit2) {
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
        searchView.setQueryHint(Html.fromHtml("<font color = #ecf0f1>" + getResources().getString(R.string.course_search_hint) + "</font>"));

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
        ArrayList<Course> filteredCourse = new ArrayList<Course>();
        if(searchView!=null)
        {
            for(Course course:courses)
            {
                if(course.getCourse_code().toLowerCase().startsWith(query.toLowerCase()) || course.getCourse_code().toLowerCase().endsWith(query.toLowerCase())|| course.getCourse_credit().toLowerCase().startsWith(query.toLowerCase()) || course.getCourse_credit().endsWith(query.toLowerCase())
                        || course.getCourse_title().toLowerCase().startsWith(query.toLowerCase()) || course.getCourse_title().endsWith(query.toLowerCase()) || course.getCourseDetail().toLowerCase().contains(query.toLowerCase()))
                {
                    filteredCourse.add(course);
                }
                adapter = new SyllabusAdapter(getActivity().getApplicationContext(),R.layout.fragment_syllabus2,R.id.course_code,filteredCourse,isEditable,getFragmentManager(),dept.getDeptCode().toLowerCase(),semester,session,activity);
                syllabusList.setAdapter(adapter);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_teacher:
                Toast.makeText(getActivity(),
                        "Search Course",
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

    @Override
    public void onAttach(Activity activity) {
        fragmentActivity=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Values.IS_LOCAL_ADMIN = false;
    }
}
