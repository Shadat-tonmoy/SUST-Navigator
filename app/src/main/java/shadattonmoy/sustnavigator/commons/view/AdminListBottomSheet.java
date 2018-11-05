package shadattonmoy.sustnavigator.commons.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.model.Admin;
import shadattonmoy.sustnavigator.admin.view.CourseListDialog;
import shadattonmoy.sustnavigator.commons.controller.AdminListAdapter;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;
import shadattonmoy.sustnavigator.utils.Values;

public class AdminListBottomSheet extends BottomSheetDialogFragment {

    private ListView adminList;
    private Context context;
    private TextView sessionTextView;
    private Dialog dialog;
    private SchoolListAdapter schoolListAdapter;
    private boolean fetchFromServer;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Admin> adminFromServer;
    private ProgressBar adminLoadingProgressBar;
    private Dept dept;
    private Course course;
    private String session,semester;
    private FragmentActivity activity;
    private int purposeOfContact = -1;
    private int holidayYear = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity= (FragmentActivity) context;
    }

    public AdminListBottomSheet()
    {

    }
    @Override
    public void setupDialog(Dialog dialog, int style) {
        getValues();
        View contentView = View.inflate(getContext(), R.layout.admin_selection_bottom_sheet, null);
        initNodesFromServer(contentView);
        dialog.setContentView(contentView);
        context = getActivity();
        activity = getActivity();
        this.dialog = dialog;
    }

    private void getValues()
    {
        Bundle args = getArguments();
        if(args!=null)
        {
            dept = (Dept) args.getSerializable("dept");
            session = args.getString("session");
            semester = args.getString("semester");
            purposeOfContact = args.getInt("purpose");
            course = (Course) args.getSerializable("course");
        }
    }

    /*
     * method to initialize bottom sheet nodes
     * */


    public void initNodesFromServer(final View view)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("admin");
        adminFromServer = new ArrayList<>();
        adminLoadingProgressBar = (ProgressBar) view.findViewById(R.id.admin_loading_progress);
        adminLoadingProgressBar.setVisibility(View.VISIBLE);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Admin admin = child.getValue(Admin.class);
                    if(admin!=null)
                    {
                        if(purposeOfContact==Values.CONTACT_FOR_HOLIDAY || purposeOfContact==Values.CONTACT_FOR_PROCTOR)
                        {
                            if(admin.isVarified())
                                adminFromServer.add(admin);
                        }
                        else{
                            if((admin.isSuperAdmin() || (admin.getDept().toLowerCase().equals(dept.getDeptCode().toLowerCase()) && admin.isVarified())))
                                adminFromServer.add(admin);
                        }
                    }

                }
                if(adminFromServer.size()>0)
                {
                    adminLoadingProgressBar.setVisibility(View.GONE);
                    adminList = (ListView) view.findViewById(R.id.admin_list);
                    adminList.setVisibility(View.VISIBLE);
                    AdminListAdapter adminListAdapter = new AdminListAdapter(context,R.layout.admin_small_single_row,R.id.admin_name,adminFromServer);
                    adminList.setAdapter(adminListAdapter);
                    setNodesTouchListener();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setNodesTouchListener()
    {
        adminList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Admin admin = (Admin) adapterView.getItemAtPosition(i);
                String  name = admin.getName();
//                String  adminDept = admin.getDept();
                String  email = admin.getEmail();
                String message = "";
                if(purposeOfContact==Values.CONTACT_FOR_SYLLABUS)
                    message = Values.getEmailForSyllabus(name,dept.getDeptCode(),session);
                if(purposeOfContact==Values.CONTACT_FOR_SYLLABUS_DETAILS)
                    message = Values.getEmailForSyllabusDetail(name,dept.getDeptCode(),session,course);
                else if(purposeOfContact==Values.CONTACT_FOR_FACULTY)
                    message = Values.getEmailForFaculty(name,dept.getDeptCode());
                else if(purposeOfContact==Values.CONTACT_FOR_STAFF)
                    message = Values.getEmailForStaff(name,dept.getDeptCode());
                else if(purposeOfContact==Values.CONTACT_FOR_HOLIDAY)
                    message = Values.getEmailForHoliday(name,holidayYear);
                else if(purposeOfContact==Values.CONTACT_FOR_PROCTOR)
                    message = Values.getEmailForProctor(name);
                Values.sendEmail(email.trim(),message.trim(),context);
//                Log.e("Admin",name+" "+email+" "+dept);
                dialog.dismiss();
            }
        });

    }




}
