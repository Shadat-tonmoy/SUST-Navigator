package shadattonmoy.navigationdrawer;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shadat Tonmoy on 8/31/2017.
 */

public class HolidaysFragment extends android.app.Fragment {


    private TextView holidayName,holidayDate, holidayDays,holidayTitle,noHolidayFoundView;
    private ArrayList<Holiday> holidays;
    ListView holidayList;
    private int year=-1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String[] months,days;
    private ProgressBar progressBar;
    private FragmentManager manager;;
    public HolidaysFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_holidays, container, false);
        holidayTitle = (TextView) view.findViewById(R.id.holiday_title);
        noHolidayFoundView = (TextView) view.findViewById(R.id.no_holiday_found_view);
        holidayName = (TextView)view.findViewById(R.id.holiday_name);
        holidayDate = (TextView)view.findViewById(R.id.holiday_date);
        holidayDays = (TextView)view.findViewById(R.id.holiday_days);
        holidayList = (ListView)view.findViewById(R.id.holiday_list);
        progressBar = (ProgressBar) view.findViewById(R.id.holiday_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Calendar calendar = Calendar.getInstance();
        manager = getFragmentManager();
        year = calendar.get(Calendar.YEAR);
        holidayTitle.setText("Holidays of "+year);
        progressBar.setVisibility(View.VISIBLE);
        months = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
        days = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        holidays = new ArrayList<Holiday>();
        noHolidayFoundView.setVisibility(View.GONE);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("holiday").child(String.valueOf(year));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numOfHolidays = 0;
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    String key = child.getKey();
                    Holiday holiday = child.getValue(Holiday.class);
                    holidays.add(holiday);
                    numOfHolidays++;
                }
                HolidayAdapter adapter = new HolidayAdapter(getActivity().getApplicationContext(),R.layout.holiday_single_row,R.id.holiday_desc,holidays,false,manager);
                if(numOfHolidays==0)
                {
                    noHolidayFoundView.setText("Sorry!! No Holiday found for "+year+". Please contact admin");
                    noHolidayFoundView.setVisibility(View.VISIBLE);
                    holidayTitle.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    holidayList.setVisibility(View.GONE);

                }
                else
                {
                    holidayList.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
