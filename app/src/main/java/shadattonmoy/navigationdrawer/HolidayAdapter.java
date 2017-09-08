package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Shadat Tonmoy on 6/21/2017.
 */

public class HolidayAdapter extends ArrayAdapter<Holiday>{
    private boolean isEditable;
    private ImageView editIcon;
    private Context context;
    private View row;
    private android.app.FragmentManager manager;
    public HolidayAdapter(Context context, int resource, int textViewResourceId, ArrayList<Holiday> objects, boolean isEditable, android.app.FragmentManager manager) {
        super(context, resource, textViewResourceId, objects);
        this.isEditable = isEditable;
        this.context = context;
        this.manager = manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.holiday_single_row,parent,false);;
        }
        Holiday currentHoliday = getItem(position);
        String holidayName = currentHoliday.getHolidayTitle();
        String holidayStart = currentHoliday.getStartingDate();
        String holidayEnd = currentHoliday.getEndingDate();
        String holidayStartTmp = holidayStart;
        String holidayEndTmp = holidayEnd;
        String[] holidaySplitted = holidayStart.split("/");
        String holidayDate = holidaySplitted[0]+"\n"+holidaySplitted[1];

        String[] holidayStartSplitted = holidayStart.split("/");
        holidayStart = holidayStartSplitted[0]+", "+holidayStartSplitted[1]+" | "+holidayStartSplitted[3].substring(0,3);

        String[] holidayEndSplitted = holidayEnd.split("/");
        holidayEnd = holidayEndSplitted[0]+", "+holidayEndSplitted[1]+" | "+holidayEndSplitted[3].substring(0,3);

        String holidayDesc = holidayStart+" - "+holidayEnd;

        String holidayDays = "Number of days";
        TextView holidayNameView = (TextView) row.findViewById(R.id.holiday_name);
        TextView holidayDateView = (TextView) row.findViewById(R.id.holiday_date);
        TextView holidayDaysView = (TextView) row.findViewById(R.id.holiday_days);
        TextView holidayDescView = (TextView) row.findViewById(R.id.holiday_desc);
        if(isEditable)
        {
            editIcon = (ImageView) row.findViewById(R.id.holiday_edit_icon);
            editIcon.setImageResource(R.drawable.edit_icon);
            final PopupMenu popupMenu = new PopupMenu(getContext(),editIcon, Gravity.LEFT);
            popupMenu.inflate(R.menu.holiday_manage_menu);
            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

            popupMenu.setOnMenuItemClickListener(new clickHandler(context,manager,holidayName,holidayStartTmp,holidayEndTmp,row));
        }

        holidayNameView.setText(holidayName);
        holidayDateView.setText(holidayDate);
        holidayDaysView.setText(holidayDays);
        holidayDescView.setText(holidayDesc);



        return row;
    }
}
class clickHandler implements PopupMenu.OnMenuItemClickListener{

    private Context context;
    private Course course;
    private android.app.FragmentManager manager;
    private String holidayTitle,holidayStart,holidayEnd;
    private View view;
    clickHandler(Context context,android.app.FragmentManager manager,String holidayTitle,String holidayStart,String holidayEnd,View view){
        this.context = context;
        this.manager = manager;
        this.holidayTitle = holidayTitle;
        this.holidayStart = holidayStart;
        this.holidayEnd = holidayEnd;
        this.view = view;
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.edit_holiday_menu)
        {
            Toast.makeText(context,"Edit holiday",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.remove_holiday_menu)
        {
            Toast.makeText(context,"Remove holiday",Toast.LENGTH_SHORT).show();
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference databaseReference = database.getReference().child("syllabus").child(dept).child(semester).child(course.getCourse_id());
//            databaseReference.removeValue(new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                    if(databaseError==null)
//                    {
//                        Snackbar snackbar = Snackbar.make(view,"Course has been removed",Snackbar.LENGTH_LONG);
//                        snackbar.show();
//                        SyllabusFragment.adapter.remove(course);
//
//                    }
//                }
//            });

            return true;

        }
        return false;
    }
}
