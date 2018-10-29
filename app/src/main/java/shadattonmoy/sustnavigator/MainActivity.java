package shadattonmoy.sustnavigator;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.about.AboutActivity;
import shadattonmoy.sustnavigator.admin.controller.WebCrawler;
import shadattonmoy.sustnavigator.admin.model.Admin;
import shadattonmoy.sustnavigator.admin.view.AdminFragment;
import shadattonmoy.sustnavigator.admin.view.AdminManage;
import shadattonmoy.sustnavigator.admin.view.AdminPanelFragment;
import shadattonmoy.sustnavigator.cgpa.controller.GoogleDriveBackup;
import shadattonmoy.sustnavigator.commons.view.SemesterListFragment;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.help.HelpActivity;
import shadattonmoy.sustnavigator.holiday.view.HolidaysFragment;
import shadattonmoy.sustnavigator.mlkit.CameraActivity;
import shadattonmoy.sustnavigator.proctor.view.ProctorialBodyFragment;
import shadattonmoy.sustnavigator.utils.DummyValues;
import shadattonmoy.sustnavigator.utils.LastModified;
import shadattonmoy.sustnavigator.utils.Values;

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
    private RelativeLayout root;
    private TextView lastModifiedText;
    private LastModified lastModified;
    private List<Admin> adminRequests;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastModifiedText = (TextView) findViewById(R.id.last_modified);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SUST Navigator");
        setSupportActionBar(toolbar);
        context = MainActivity.this;

        String dbname = Values.DATABASE_NAME;
        File database = context.getDatabasePath(dbname);
        if(database!=null)
        {
            Log.e("DatabasePath",database.getAbsolutePath()+" "+database.getName());
        }

        sqLiteAdapter = SQLiteAdapter.getInstance(MainActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();

        sqLiteAdapter.initDB();


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.nav_header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                for (int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                    manager.popBackStack();
                }

            }
        });


        root = (RelativeLayout) findViewById(R.id.main_content_root);
        if (root != null) {
            manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            MainFragment mainFragment = new MainFragment();
            transaction.add(R.id.main_content_root, mainFragment, "add_main_fragment");
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

        getLastModified();
        getAdminRequest();
//        addCourse();
    }
    /*end of onCreate Method*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        if (requestCode == Values.REQUEST_CODE_SIGN_IN_FOR_BACKUP)
        {
            startTask(task,true);
        }
        else if (requestCode == Values.REQUEST_CODE_SIGN_IN_FOR_RESTORE)
        {
            startTask(task,false);
        }


    }

    private void startTask(Task<GoogleSignInAccount> completedTask,boolean toBackup) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Values.showToast(context,account.getEmail()+" is signed in");
            SemesterListFragment.signOutMenu.setVisible(true);
            GoogleDriveBackup googleDriveBackup = new GoogleDriveBackup(context,account,MainActivity.this);

            String msg = "";
            if(toBackup)
                msg = "Backup Saved Date to Cloud";
            else msg = "Restored Saved Date from Cloud";
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(context);
            builder.setTitle("Proceed?")
                    .setMessage("You are successfully logged in. Continue with "+msg+"?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            GoogleDriveBackup googleDriveBackup = new GoogleDriveBackup(context, GoogleSignIn.getLastSignedInAccount(context), MainActivity.this);
                            if(toBackup)
                            {
                                googleDriveBackup.startBackupTask();
                            }
                            else {
                                Log.e("Restoring","WillStart");
                                googleDriveBackup.startRestoreTask();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            Log.e("SignIn", account.getEmail()+" is signed in");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Error", "signInResult:failed code=" + e.getMessage());
//            updateUI(null);
        }
    }




    public void getAdminRequest() {
        if (isNetworkAvailable()) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("admin");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    adminRequests = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Admin admin = child.getValue(Admin.class);
                        String pushId = child.getKey();
                        admin.setId(pushId);
                        if (!admin.isVarified()) {
                            adminRequests.add(admin);
                        }
                    }
                    if (adminRequests.size() > 0) {
                        Log.e("GetAdminReq", "Total Req " + adminRequests.size());
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            Query queryRef = databaseReference.child("admin").orderByChild("email").equalTo(email);
                            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Admin loggedInAdmin = child.getValue(Admin.class);
                                        Values.LOGGED_IN_ADMIN = loggedInAdmin;
                                        if (Values.LOGGED_IN_ADMIN != null) {
                                            Log.e("GetAdminReq", "LoggedInAsAAdmin");
                                            if (Values.LOGGED_IN_ADMIN.isSuperAdmin()) {
                                                Log.e("GetAdminReq", "LoggedInAsASuperAdmin");
                                                showAdminRequestDialog(adminRequests.size());
                                            } else {
                                                int count = 0;
                                                for (Admin admin : adminRequests) {
                                                    if (admin.getDept().toLowerCase().equals(Values.LOGGED_IN_ADMIN.getDept().toLowerCase())) {
                                                        count++;
                                                    }
                                                }
                                                if (count > 0) {
                                                    showAdminRequestDialog(count);
                                                }
                                            }

                                        } else {
                                            Log.e("GetAdminReq", "CurrentAdminNull");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {
                        Log.e("GetAdminReq", "NoNewRequest");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    private void showAdminRequestDialog(int numOfRequest) {
        Log.e("GetAdminReq", "ShowingDialog");
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("New Admin Request!")
                .setMessage(numOfRequest + " Admin Requests are Pending. Want to approve them?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openAdminManageFragment();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();


    }

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
            if (id == R.id.teacher) {
                openTeacherFragment(null);
            } else if (id == R.id.syllabus) {
                openSyllabusFragment(null);
            } else if (id == R.id.CGPA) {
                openCGPAFragment(null);
            }
        } else if (id == R.id.staff) {
            openStaffFragment(null);

        } else if (id == R.id.holidays) {
            openHolidayFragment(null);

        } else if (id == R.id.proctorial_body) {
            openProctorFragment(null);
        } else if (id == R.id.nav_help) {
            openHelpFragment(null);
        } else if (id == R.id.nav_about) {
            openAboutFragment(null);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openTeacherFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setPurpose("teacher");
        transaction.replace(R.id.main_content_root, deptFragment);
        transaction.addToBackStack("teacher_fragment");
        transaction.commit();
    }

    public void openSyllabusFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setPurpose("syllabus");
        transaction.replace(R.id.main_content_root, deptFragment);
        transaction.addToBackStack("syllabus_fragment");
        transaction.commit();
    }

    public void openHolidayFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HolidaysFragment holidaysFragment = new HolidaysFragment(false);
        transaction.replace(R.id.main_content_root, holidaysFragment);
        transaction.addToBackStack("holiday_fragment");
        transaction.commit();
    }

    public void openCGPAFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setPurpose("cgpa");
        transaction.replace(R.id.main_content_root, deptFragment);
        transaction.addToBackStack("cgpa_fragment");
        transaction.commit();
    }

    public void openStaffFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setPurpose("staff");
        transaction.replace(R.id.main_content_root, deptFragment);
        transaction.addToBackStack("staff_fragment");
        transaction.commit();
    }

    public void openAdminFragment(View view) {
        if (isNetworkAvailable()) {
            if (user != null) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                AdminPanelFragment adminPanelFragment = new AdminPanelFragment();
                transaction.replace(R.id.main_content_root, adminPanelFragment);
                transaction.addToBackStack("admin_panel_fragment");
                transaction.commit();
            } else {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                AdminFragment adminFragment = new AdminFragment();
                transaction.replace(R.id.main_content_root, adminFragment);
                transaction.addToBackStack("admin_fragment");
                transaction.commit();
            }
        } else {
            Snackbar.make(root, "No Internet Connection!! Try again", Snackbar.LENGTH_LONG)
                    .setAction("Got It", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark))
                    .show();
        }

    }

    public void openProctorFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ProctorialBodyFragment proctorialBodyFragment = new ProctorialBodyFragment(false);
        transaction.replace(R.id.main_content_root, proctorialBodyFragment);
        transaction.addToBackStack("proctor_fragment");
        transaction.commit();
    }

    public void openProctorManageFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ProctorialBodyFragment proctorialBodyFragment = new ProctorialBodyFragment(true);
        transaction.replace(R.id.main_content_root, proctorialBodyFragment);
        transaction.addToBackStack("proctor_fragment");
        transaction.commit();
    }

    public void openTeacherManageFragment(View view) {
        //Toast.makeText(getBaseContext(),"Teacher Manage",Toast.LENGTH_SHORT).show();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setPurpose(Values.PURPOSE_TEACHER_MANAGE);
        transaction.replace(R.id.main_content_root, deptFragment);
        transaction.addToBackStack("teacher_manage_fragment");
        transaction.commit();

    }

    public void openSyllabusManageFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setPurpose(Values.PURPOSE_SYLLABUS_MANAGE);
        transaction.replace(R.id.main_content_root, deptFragment);
        transaction.addToBackStack("syllabus_manage_fragment");
        transaction.commit();

    }

    public void openHolidayManageFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HolidaysFragment holidayManageFragment = new HolidaysFragment(true);
        transaction.replace(R.id.main_content_root, holidayManageFragment);
        transaction.addToBackStack("holiday_manage_fragment");
        transaction.commit();

    }

    public void openStaffManageFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setPurpose(Values.PURPOSE_STAFF_MANAGE);
        transaction.replace(R.id.main_content_root, deptFragment);
        transaction.addToBackStack("staff_manage_fragment");
        transaction.commit();


    }

    public void openAdminManageFragment(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AdminManage adminManage = new AdminManage();
        transaction.replace(R.id.main_content_root, adminManage);
        transaction.addToBackStack("admin_manage_fragment");
        transaction.commit();


    }

    public void openAdminManageFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AdminManage adminManage = new AdminManage();
        transaction.replace(R.id.main_content_root, adminManage);
        transaction.addToBackStack("admin_manage_fragment");
        transaction.commit();


    }

    public void openHelpFragment(View view) {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);


    }

    public void openAboutFragment(View view) {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);

    }

    public void openMLKit(View view) {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);


    }

    public void logout(View view) {
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public void getLastModified() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("lastModified");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastModified = new LastModified();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("name"))
                        lastModified.setName((String) child.getValue());
                    else if (child.getKey().equals("dept"))
                        lastModified.setDept((String) child.getValue());
                    else if (child.getKey().equals("regNo"))
                        lastModified.setRegNo((String) child.getValue());
                    else if (child.getKey().equals("time"))
                        lastModified.setTime((Long) child.getValue());
                }
                if (lastModified.getName() != "") {
                    lastModifiedText.setText("Last Updated by " + lastModified.getName() + "\nDepartment : " + lastModified.getDept() + "\nReg No : " + lastModified.getRegNo() + "\nAt " + Values.getTimeString(lastModified.getTime()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addCourse() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child("2014-15").child("cse").child("1_1");
        List<Course> courses = DummyValues.getCourses("1_1");
        for (Course course : courses) {
            databaseReference.push().setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("syllabusAdded", "For Dept CHE");
                }
            });

        }
        databaseReference = firebaseDatabase.getReference().child("syllabus").child("2014-15").child("cse").child("1_2");
        courses = DummyValues.getCourses("1_2");
        for (Course course : courses) {
            databaseReference.push().setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("syllabusAdded", "For Dept CHE");
                }
            });

        }
        databaseReference = firebaseDatabase.getReference().child("syllabus").child("2014-15").child("cse").child("2_1");
        courses = DummyValues.getCourses("2_1");
        for (Course course : courses) {
            databaseReference.push().setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("syllabusAdded", "For Dept CHE");
                }
            });

        }


    }
}
