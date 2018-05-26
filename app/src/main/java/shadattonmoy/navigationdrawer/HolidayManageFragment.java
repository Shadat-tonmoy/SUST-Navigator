package shadattonmoy.navigationdrawer;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HolidayManageFragment extends android.app.Fragment implements View.OnClickListener{

    private View view;
    private TextView heading;
    private Button button;
    private FloatingActionButton floatingActionButton;
    private int year=-1;
    private String[] months,days;
    private ListView holidayList;
    private ArrayList<Holiday> holidays;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private FragmentManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_holiday_manage, container, false);
        heading = (TextView) view.findViewById(R.id.mange_holiday_header);
        holidayList = (ListView) view.findViewById(R.id.holiday_manage_list);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.holiday_manage_fab);
        progressBar = (ProgressBar) view.findViewById(R.id.holiday_manage_loading);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        progressBar.setVisibility(View.VISIBLE);
        manager = getFragmentManager();
        months = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
        days = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        holidays = new ArrayList<Holiday>();

        Date date = new Date();
        heading.setText("Manage Holiday For "+year);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("holiday").child(String.valueOf(year));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    String key = child.getKey();
                    Holiday holiday = child.getValue(Holiday.class);
                    holiday.setHoliayId(key);
                    holidays.add(holiday);
                }
                HolidayAdapter adapter = new HolidayAdapter(getActivity().getApplicationContext(),R.layout.holiday_single_row,R.id.holiday_desc,holidays,true,manager);
                holidayList.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //heading.setText("Full date is "+date+"\nDate is "+calendar.get(Calendar.DATE)+"\nMonth is "+months[calendar.get(Calendar.MONTH)]+"\nYear is : "+calendar.get(Calendar.YEAR)+"Day is "+days[calendar.get(Calendar.DAY_OF_WEEK)-1]);

        floatingActionButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.holiday_manage_fab)
        {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            HolidayAddFragment holidayAddFragment = new HolidayAddFragment(String.valueOf(year));
            transaction.replace(R.id.main_content_root,holidayAddFragment);
            transaction.addToBackStack("holiday_add_fragment");
            transaction.commit();
        }
//        DatepickerDialog datepickerDialog = new DatepickerDialog();
//        datepickerDialog.show(getFragmentManager(), "datePicker");

    }
}
