package shadattonmoy.sustnavigator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import shadattonmoy.sustnavigator.utils.Values;

/**
 * Created by Shadat Tonmoy on 9/7/2017.
 */

public class DatepickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener  {
    String editField;
    public DatepickerDialog(String editField)
    {
        super();
        this.editField = editField;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        int day = c.get(Calendar.DAY_OF_WEEK);






        return new DatePickerDialog(getActivity(), this, year, month, date);
    }

    public void onDateSet(DatePicker view, int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,date);
        long timeStamps = calendar.getTimeInMillis();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        String chosen = date+"/"+ Values.months[month].substring(0,3)+"/"+year;

        if(editField.equals("holidayStart"))
        {
            HolidayAddFragment.holidayStart.setText(chosen);
            HolidayAddFragment.startDay = Values.days[day];
            HolidayAddFragment.holidayStartTime = timeStamps;
        }
        else if(editField.equals("holidayEnd"))
        {
            HolidayAddFragment.holidayEnd.setText(chosen);
            HolidayAddFragment.endDay= Values.days[day];
            HolidayAddFragment.holidayEndTime = timeStamps;
        }

    }
}
