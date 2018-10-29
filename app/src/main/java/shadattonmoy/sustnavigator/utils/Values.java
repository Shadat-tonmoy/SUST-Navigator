package shadattonmoy.sustnavigator.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.MainActivity;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.model.Admin;
import shadattonmoy.sustnavigator.admin.view.AdminPanelFragment;

public class Values {
    public static String[] months = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
    public static String[] days = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    public static final int DETECTED_TYPE_COURSE_CODE = 1;
    public static final int DETECTED_TYPE_COURSE_TITLE = 2;
    public static final int DETECTED_TYPE_COURSE_CREDIT = 3;
    public static final int VIEW_TYPE_TEXTVIEW = 1;
    public static final int VIEW_TYPE_EDITTEXT = 2;
    public static final int REQUEST_CODE_SIGN_IN_FOR_BACKUP = 1;
    public static final int REQUEST_CODE_SIGN_IN_FOR_RESTORE = 2;
    public static final String GITHUB_LINK = "https://github.com/Shadat-tonmoy/SUST-Navigator";
    public static final String DATABASE_NAME = "sust_nav_database";
    public static final String DEV_GITHUB_LINK = "https://github.com/Shadat-tonmoy/";
    public static final String DEV_FB_LINK = "https://www.facebook.com/shadat.tonmoy";
    public static final String DEV_LINKEDIN_LINK = "https://www.linkedin.com/in/shadat-tonmoy-06266b27/";
    public static final String PURPOSE_SYLLABUS_MANAGE = "syllabus_manage";
    public static final String PURPOSE_STAFF_MANAGE = "staff_manage";
    public static final String PURPOSE_TEACHER_MANAGE = "teacher_manage";
    public static Admin LOGGED_IN_ADMIN = null;




    public static boolean IS_LOCAL_ADMIN = false;
    private static Map<String,String> semesterCodeMap = new HashMap<>();
    public static List<String> getSessions()
    {
        List<String> sessions = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        sessions.add((year-1)+"-"+(year%100));
        sessions.add((year-2)+"-"+((year-1)%100));
        sessions.add((year-3)+"-"+((year-2)%100));
        sessions.add((year-4)+"-"+((year-3)%100));
        sessions.add((year-5)+"-"+((year-4))%100);
        return sessions;
    }

    public static Map getSemesterCodeMap()
    {
        semesterCodeMap.put("1st Year 1st Semester","1_1");
        semesterCodeMap.put("1st Year 2nd Semester","1_2");
        semesterCodeMap.put("2nd Year 1st Semester","2_1");
        semesterCodeMap.put("2nd Year 2nd Semester","2_2");
        semesterCodeMap.put("3rd Year 1st Semester","3_1");
        semesterCodeMap.put("3rd Year 2nd Semester","3_2");
        semesterCodeMap.put("4th Year 1st Semester","4_1");
        semesterCodeMap.put("4th Year 2nd Semester","4_2");
        semesterCodeMap.put("5th Year 1st Semester","5_1");
        semesterCodeMap.put("5th Year 2nd Semester","5_2");
        semesterCodeMap.put("Optionals","o_p");
        semesterCodeMap.put("Second Major","s_m");

        return semesterCodeMap;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void updateLastModified()
    {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        Log.e("LoggedInAs",email);
        Query queryRef = databaseReference.child("admin").orderByChild("email").equalTo(email);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Admin admin = child.getValue(Admin.class);
                    String pushId = child.getKey();
                    admin.setId(pushId);
                    String name = admin.getName();
                    String regNo = admin.getRegNo();
                    String dept = admin.getDept();
                    long time = new Date().getTime();
                    LastModified lastModified = new LastModified(name,regNo,dept,time);
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("lastModified");
                    databaseReference.setValue(lastModified);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    public static void getAdminRequest(final Context context)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query queryRef = databaseReference.child("admin").orderByChild("varified").equalTo(false);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Admin> admins = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Admin admin = child.getValue(Admin.class);
                    String pushId = child.getKey();
                    admin.setId(pushId);
                    String name = admin.getName();
                    String regNo = admin.getRegNo();
                    String dept = admin.getDept();
                    if(!admin.isVarified())
                    {
                        admins.add(admin);
                    }
                }
                if(admins.size()>0)
                {
                    AdminPanelFragment.setAdminReqMessage(admins.size()+" Admin Requests are pending for approval");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static void getCurrentAdmin()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            String email = user.getEmail();
            Query queryRef = databaseReference.child("admin").orderByChild("email").equalTo(email);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Admin admin = child.getValue(Admin.class);
                        Values.LOGGED_IN_ADMIN = admin;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public static String getTimeString(long timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar.getTime().toString();
    }

    public static String getSemesterCode(String semester)
    {

        return semester.replace("/","_").toLowerCase();
    }

    public static void openLink(Context context,String url)
    {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void showToast(Context context,String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String trimStringTo(String src,int len)
    {
        if(src.length()>len)
        {
            src = src.substring(0,len-3) + "..";
        }
        return src;

    }


}
