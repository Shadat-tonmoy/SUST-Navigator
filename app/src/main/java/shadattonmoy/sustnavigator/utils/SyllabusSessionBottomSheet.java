package shadattonmoy.sustnavigator.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.view.CourseListDialog;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;
import shadattonmoy.sustnavigator.syllabus.controller.SyllabusAdapter;
import shadattonmoy.sustnavigator.syllabus.view.SyllabusFragment;

public class SyllabusSessionBottomSheet extends BottomSheetDialogFragment {

    private ListView sessionList;
    private Context context;
    private TextView sessionTextView;
    private Dialog dialog;
    private SchoolListAdapter schoolListAdapter;
    private boolean fetchFromServer;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<String> sessionFromServer;
    private ProgressBar sessionLoadingProgressBar;
    private String dept,semester,session;
    private SyllabusFragment syllabusFragment;
    public SyllabusSessionBottomSheet()
    {

    }

    public SyllabusSessionBottomSheet(Context context,boolean fetchFromServer,String dept, String semester,String session)
    {
        this.context = context;
        this.fetchFromServer = fetchFromServer;
        this.dept = dept;
        this.semester = semester;
        this.session = session;

    }

    public SyllabusSessionBottomSheet(Context context,TextView sessionTextView)
    {
        this.context = context;
        this.sessionTextView = sessionTextView;
    }
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.session_selection_bottom_sheet, null);
        context = getActivity();
        if(fetchFromServer)
            initNodesFromServer(contentView);
        else initNodes(contentView);
        setNodesTouchListener();
        dialog.setContentView(contentView);
        this.dialog = dialog;
    }

    /*
     * method to initialize bottom sheet nodes
     * */

    public void initNodes(View view)
    {
        sessionList = (ListView) view.findViewById(R.id.session_list);
        sessionList.setVisibility(View.VISIBLE);
        SessionListAdapter sessionListAdapter = new SessionListAdapter(context,R.layout.session_single_row,R.id.session_title,(ArrayList<String>) Values.getSessions());
        sessionList.setAdapter(sessionListAdapter);
    }

    public void initNodesFromServer(final View view)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus");
        sessionFromServer = new ArrayList<>();
        sessionFromServer.clear();
        sessionLoadingProgressBar = (ProgressBar) view.findViewById(R.id.session_loading_progress);
        sessionLoadingProgressBar.setVisibility(View.VISIBLE);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    String session = child.getKey();
                    sessionFromServer.add(session);
                }

                if(sessionFromServer.size()>0)
                {
                    sessionLoadingProgressBar.setVisibility(View.GONE);
                    sessionList = (ListView) view.findViewById(R.id.session_list);
                    sessionList.setVisibility(View.VISIBLE);
                    SessionListAdapter sessionListAdapter = new SessionListAdapter(context,R.layout.session_single_row,R.id.session_title,sessionFromServer);
                    sessionList.setAdapter(sessionListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        sessionList = (ListView) view.findViewById(R.id.session_list);
        SessionListAdapter sessionListAdapter = new SessionListAdapter(context,R.layout.session_single_row,R.id.session_title,(ArrayList<String>) Values.getSessions());
        sessionList.setAdapter(sessionListAdapter);
    }

    /*
     * method to handle bottom sheet menu click
     * */
    public void setNodesTouchListener()
    {
        sessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = adapterView.getSelectedItemPosition();
                String sessionToClone = Values.getSessions().get(i);
//                Log.e("SelectedSession",session);
                if(!fetchFromServer)
                {
                    sessionToClone = sessionToClone.replaceAll("[a-zA-Z()\" \"]+","");
                    sessionTextView.setText(sessionToClone);
                    DeptFragment.bottomSheetSelectedPosition= i;
                    schoolListAdapter.setSession(sessionToClone);
                }
                else
                {
                    sessionToClone = sessionFromServer.get(i);
                    CourseListDialog courseListDialog = new CourseListDialog();
                    courseListDialog.setSyllabusFragment(syllabusFragment);
                    Bundle args = new Bundle();
                    args.putString("sessionToClone",sessionToClone);
                    args.putString("session",session);
                    args.putString("dept",dept);
                    args.putString("semester",semester);
                    courseListDialog.setArguments(args);
                    courseListDialog.show(getActivity().getFragmentManager(),"courseListDialog");
                }
                dialog.dismiss();





            }
        });

    }

    public void setSchoolListAdapter(SchoolListAdapter schoolListAdapter) {
        this.schoolListAdapter = schoolListAdapter;
    }

    public void setSyllabusFragment(SyllabusFragment syllabusFragment) {
        this.syllabusFragment = syllabusFragment;
    }
}