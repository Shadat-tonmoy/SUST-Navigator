package shadattonmoy.sustnavigator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.school.model.School;
import shadattonmoy.sustnavigator.utils.DummyValues;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager manager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private SQLiteAdapter sqLiteAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SUST Navigator");
        setSupportActionBar(toolbar);



        sqLiteAdapter = new SQLiteAdapter(this);
        firebaseAuth = FirebaseAuth.getInstance();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        RelativeLayout root = (RelativeLayout)findViewById(R.id.main_content_root);
        if(root!=null)
        {
            manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            MainFragment mainFragment = new MainFragment();
            transaction.add(R.id.main_content_root,mainFragment,"add_main_fragment");
            transaction.commit();

        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here


            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
//        addFaculty();
//        addSchools();
    }
    /*end of onCreate Method*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*getMenuInflater().inflate(R.menu.main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.teacher || id == R.id.syllabus || id == R.id.CGPA) {
            DeptFragment deptFragment = new DeptFragment();
            if(id == R.id.teacher)
            {
                deptFragment.setRoot("TEACHER");
                fragment = deptFragment;
            }
            else if(id == R.id.syllabus)
            {
                deptFragment.setRoot("SYLLABUS");
                fragment = deptFragment;
            }
            else if (id == R.id.CGPA) {
                deptFragment.setRoot("CGPA");
                fragment = deptFragment;
            }
        }
        else if (id == R.id.staff) {
            fragment = new StaffFragment();

        }
        else if (id == R.id.bus_service) {
            fragment = new BusServiceFragment();

        }
        else if (id == R.id.student_organization) {
            fragment = new StudentOrganizationFragment();

        }
        else if (id == R.id.holidays) {
            fragment = new HolidaysFragment();

       }
        else if (id == R.id.proctorial_body) {
            fragment = new ProctorialBodyFragment(false);

        }
        else if (id == R.id.admission_info) {
            fragment = new AdmissionInfoFragment();

        }

        if(fragment!=null)
        {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_root,fragment);
            transaction.addToBackStack("replace_fragment");
            transaction.commit();
        }
        else
        {
            Toast.makeText(this,"Sorry No Fragment Found!!!",Toast.LENGTH_SHORT).show();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openTeacherFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setRoot("TEACHER");
        transaction.replace(R.id.main_content_root,deptFragment);
        transaction.addToBackStack("teacher_fragment");
        transaction.commit();
    }

    public void openSyllabusFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setRoot("SYLLABUS");
        transaction.replace(R.id.main_content_root,deptFragment);
        transaction.addToBackStack("syllabus_fragment");
        transaction.commit();
    }

    public void openHolidayFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HolidaysFragment holidaysFragment = new HolidaysFragment();
        transaction.replace(R.id.main_content_root,holidaysFragment);
        transaction.addToBackStack("holiday_fragment");
        transaction.commit();
    }

    public void openCGPAFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setRoot("CGPA");
        transaction.replace(R.id.main_content_root,deptFragment);
        transaction.addToBackStack("cgpa_fragment");
        transaction.commit();
    }

    public void openStaffFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setRoot("STAFF");
        transaction.replace(R.id.main_content_root,deptFragment);
        transaction.addToBackStack("staff_fragment");
        transaction.commit();
    }

    public void openAdminFragment(View view)
    {
        //Toast.makeText(this,"LL",Toast.LENGTH_SHORT).show();
        if(user!=null)
        {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            AdminPanelFragment adminPanelFragment = new AdminPanelFragment();
            transaction.replace(R.id.main_content_root,adminPanelFragment);
            transaction.addToBackStack("admin_panel_fragment");
            transaction.commit();
        }
        else
        {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            AdminFragment adminFragment = new AdminFragment();
            transaction.replace(R.id.main_content_root,adminFragment);
            transaction.addToBackStack("admin_fragment");
            transaction.commit();

        }
    }

    public void openProctorFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ProctorialBodyFragment proctorialBodyFragment = new ProctorialBodyFragment(false);
        transaction.replace(R.id.main_content_root,proctorialBodyFragment);
        transaction.addToBackStack("proctor_fragment");
        transaction.commit();
    }

    public void openProctorManageFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ProctorialBodyFragment proctorialBodyFragment = new ProctorialBodyFragment(true);
        transaction.replace(R.id.main_content_root,proctorialBodyFragment);
        transaction.addToBackStack("proctor_fragment");
        transaction.commit();
    }


    public void openBusFragment(View view)
    {
        Toast.makeText(getBaseContext(),"Not ready yet",Toast.LENGTH_SHORT).show();
    }

    public void openTeacherManageFragment(View view)
    {
        //Toast.makeText(getBaseContext(),"Teacher Manage",Toast.LENGTH_SHORT).show();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setRoot("TEACHER_MANAGE");
        transaction.replace(R.id.main_content_root,deptFragment);
        transaction.addToBackStack("teacher_manage_fragment");
        transaction.commit();

    }

    public void openSyllabusManageFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setRoot("SYLLABUS_MANAGE");
        transaction.replace(R.id.main_content_root,deptFragment);
        transaction.addToBackStack("syllabus_manage_fragment");
        transaction.commit();

    }

    public void openHolidayManageFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HolidayManageFragment holidayManageFragment = new HolidayManageFragment();
        transaction.replace(R.id.main_content_root,holidayManageFragment);
        transaction.addToBackStack("holiday_manage_fragment");
        transaction.commit();

    }

    public void openAdminManageFragment(View view)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AdminManage adminManage= new AdminManage();
        transaction.replace(R.id.main_content_root,adminManage);
        transaction.addToBackStack("admin_manage_fragment");
        transaction.commit();

    }

    public void openBusManageFragment(View view)
    {
        Toast.makeText(getBaseContext(),"Bus Manage",Toast.LENGTH_SHORT).show();

    }

    public void logout(View view)
    {
        firebaseAuth.signOut();
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    /*public void addFaculty(*//*Teacher teacher, final String dept*//*)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        AssetManager assetManager = getAssets();
        InputStream input;
        try {
            input = assetManager.open("chemistry.txt");
            String fileName = "chemistry.txt";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                Log.e("ReadLine",line);
                String[] properties = line.split(":");
                String name,telephone,mobile,email;
                int i=0;
                *//*for (String prop:properties){
                    System.out.println(prop+" at index "+i);;
                    i++;
                }*//*

                name = properties[0];
                telephone = properties[1];
                mobile = properties[2];
                email = properties[3];

                databaseReference = firebaseDatabase.getReference().child("teacher").child("CHE".toLowerCase());
                databaseReference.push().setValue(new Teacher(name, "N/A", "N/A", mobile, email, "N/A")).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("TeacherAddeds","For Dept CHE");
                    }
                });

                Log.e("Detail", "Name "+name+"\nTelephone "+telephone+"\nmobile "+mobile+"\nemail "+email+"\n");

//                fileContent+=line;
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/

    public void addSchools()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("schools");
        List<School> schools = DummyValues.getSchools();
        for(School school:schools)
        {
            databaseReference.push().setValue(school).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("schoolAdded","For Dept CHE");
                }
            });

        }


    }
}
