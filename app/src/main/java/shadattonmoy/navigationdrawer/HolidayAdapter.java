package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shadat Tonmoy on 6/21/2017.
 */

public class HolidayAdapter extends ArrayAdapter<Holiday> {
    public HolidayAdapter(Context context, int resource, int textViewResourceId, ArrayList<Holiday> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.holiday_single_row,parent,false);;
        }
        Holiday currentHoliday = getItem(position);
        String holidayName = currentHoliday.getHolidayName();
        String holidayDate = currentHoliday.getHolidayDate();
        String holidayDays = currentHoliday.getHolidayDays();
        String holidayDesc = currentHoliday.getHolidayDesc();

        TextView holidayNameView = (TextView) row.findViewById(R.id.holiday_name);
        TextView holidayDateView = (TextView) row.findViewById(R.id.holiday_date);
        TextView holidayDaysView = (TextView) row.findViewById(R.id.holiday_days);
        TextView holidayDescView = (TextView) row.findViewById(R.id.holiday_desc);

        holidayNameView.setText(holidayName);
        holidayDateView.setText(holidayDate);
        holidayDaysView.setText(holidayDays);
        holidayDescView.setText(holidayDesc);



        return row;
    }
}
