package shadattonmoy.sustnavigator.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;

public class SyllabusSessionBottomSheet extends BottomSheetDialogFragment {

    private ListView sessionList;
    private Context context;
    private TextView sessionTextView;
    private Dialog dialog;
    private SchoolListAdapter schoolListAdapter;

    public SyllabusSessionBottomSheet()
    {

    }

    public SyllabusSessionBottomSheet(Context context,TextView sessionTextView)
    {
        this.context = context;
        this.sessionTextView = sessionTextView;



    }
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.session_selection_bottom_sheet, null);
        initNodes(contentView);
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
                String session = Values.getSessions().get(i);
                session = session.replaceAll("[a-zA-Z()\" \"]+","");
                sessionTextView.setText(session);
                DeptFragment.bottomSheetSelectedPosition= i;
                schoolListAdapter.setSession(session);
                dialog.dismiss();





            }
        });

    }

    public void setSchoolListAdapter(SchoolListAdapter schoolListAdapter) {
        this.schoolListAdapter = schoolListAdapter;
    }
}