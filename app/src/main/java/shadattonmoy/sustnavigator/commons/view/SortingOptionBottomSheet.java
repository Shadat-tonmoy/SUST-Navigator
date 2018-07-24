package shadattonmoy.sustnavigator.commons.view;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.school.controller.SchoolListAdapter;
import shadattonmoy.sustnavigator.teacher.view.TeacherFragment;
import shadattonmoy.sustnavigator.utils.SessionListAdapter;
import shadattonmoy.sustnavigator.utils.Values;

public class SortingOptionBottomSheet extends BottomSheetDialogFragment {

    private ListView sessionList;
    private Context context;
    private TextView sessionTextView;
    private Dialog dialog;
    private SchoolListAdapter schoolListAdapter;
    private TeacherFragment teacherFragment;
    private LinearLayout nameAsc, nameDesc, designationAsc, designationDesc;

    public SortingOptionBottomSheet()
    {

    }

    public SortingOptionBottomSheet(Context context,TextView sessionTextView)
    {
        this.context = context;
        this.sessionTextView = sessionTextView;



    }
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View sortingOptions = View.inflate(getContext(), R.layout.teacher_sorting_bottom_sheet, null);
        nameAsc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_name_asc);
        nameDesc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_name_desc);
        designationAsc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_designation_asc);
        designationDesc = (LinearLayout) sortingOptions.findViewById(R.id.sort_bottom_sheet_designation_desc);
        dialog.setContentView(sortingOptions);
        this.dialog = dialog;
    }


}