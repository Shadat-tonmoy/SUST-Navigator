package shadattonmoy.sustnavigator.holiday.controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import java.util.Calendar;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.HolidayAddFragment;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.holiday.model.Holiday;
import shadattonmoy.sustnavigator.syllabus.view.SyllabusFragment;

/**
 * Created by Shadat Tonmoy on 6/21/2017.
 */

public class HolidayAdapter extends ArrayAdapter<Holiday>{
    private boolean isEditable;
    private ImageView editIcon;
    private Context context;
    private View row,view;
    private android.app.FragmentManager manager;
    private Activity activity;
    private HolidayAdapter holidayAdapter;
    public HolidayAdapter(Context context, int resource, int textViewResourceId, ArrayList<Holiday> objects, boolean isEditable, android.app.FragmentManager manager,View view) {
        super(context, resource, textViewResourceId, objects);
        this.isEditable = isEditable;
        this.context = context;
        this.manager = manager;
        this.view = view;
        holidayAdapter = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.holiday_single_row,parent,false);;
        }
        final Holiday currentHoliday = getItem(position);
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

        String holidayDays = currentHoliday.getHolidayDays()+" Days";
        TextView holidayNameView = (TextView) row.findViewById(R.id.holiday_name);
        TextView holidayDateView = (TextView) row.findViewById(R.id.holiday_date);
        TextView holidayDaysView = (TextView) row.findViewById(R.id.holiday_days);
        TextView holidayDescView = (TextView) row.findViewById(R.id.holiday_desc);
        if(isEditable)
        {
            editIcon = (ImageView) row.findViewById(R.id.holiday_edit_icon);
            editIcon.setImageResource(R.drawable.more_vert_black);
            final PopupMenu popupMenu = new PopupMenu(context,editIcon, Gravity.LEFT);
            popupMenu.inflate(R.menu.holiday_manage_menu);
            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                    ClickHandler clickHandler = new ClickHandler(context,manager,currentHoliday,view,activity,holidayAdapter);
                    popupMenu.setOnMenuItemClickListener(clickHandler);
                }
            });

            //popupMenu.setOnMenuItemClickListener(new clickHandler(context,manager,holidayName,holidayStartTmp,holidayEndTmp,row));
        }

        holidayNameView.setText(holidayName);
        holidayDateView.setText(holidayDate);
        holidayDaysView.setText(holidayDays);
        holidayDescView.setText(holidayDesc);



        return row;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
class ClickHandler implements PopupMenu.OnMenuItemClickListener{

    private Context context;
    private Course course;
    private android.app.FragmentManager manager;
    private String holidayTitle,holidayStart,holidayEnd;
    private Holiday holiday;
    private View view;
    private Activity activity;
    private HolidayAdapter holidayAdapter;
    ClickHandler(Context context,android.app.FragmentManager manager,Holiday holiday,View view,Activity activity,HolidayAdapter holidayAdapter){
        this.context = context;
        this.manager = manager;
        this.holiday = holiday;
        this.view = view;
        this.activity = activity;
        this.holidayAdapter = holidayAdapter;
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.edit_holiday_menu)
        {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            FragmentTransaction transaction = manager.beginTransaction();
            HolidayAddFragment holidayAddFragment = new HolidayAddFragment(String.valueOf(year));
            Bundle args = new Bundle();
            args.putBoolean("isEditing",true);
            args.putSerializable("holiday",holiday);
            holidayAddFragment.setArguments(args);
            transaction.replace(R.id.main_content_root,holidayAddFragment);
            transaction.addToBackStack("holiday_edit_fragment");
            transaction.commit();
            return true;
        }
        else if (id == R.id.remove_holiday_menu)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Please Notice");
            builder.setMessage("Are you sure to permanently remove this Holiday Information From Record? Once you delete you will not be able to restore again.");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    dialogInterface.dismiss();
                    final ProgressDialog dialog;
                    dialog = new ProgressDialog(activity);
                    dialog.setTitle("Deleting Record");
                    dialog.setMessage("Please Wait....");
                    dialog.show();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("holiday").child(String.valueOf(year)).child(holiday.getHoliayId()).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar.make(view,"Holiday Record is Removed",Snackbar.LENGTH_LONG);
                            snackbar.show();
                            holidayAdapter.remove(holiday);
                        }
                    });

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();


            return true;

        }
        return false;
    }
}
