package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class HolidayAddFragment extends android.app.Fragment implements View.OnFocusChangeListener,View.OnClickListener{
    private String year;
    private View view;
    private TextView addHeading;
    static EditText holidayTile,holidayStart,holidayEnd;
    static String startDay,endDay;
    private Button holidayAddSubmit,holidayAddReset;
    private String holidayTitleValue,holidayStartValue,holidayEndValue;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public HolidayAddFragment(String year) {
        this.year = year;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_holiday_add, container, false);
        addHeading = (TextView) view.findViewById(R.id.add_heading);
        holidayTile = (EditText) view.findViewById(R.id.holiday_add_title);
        holidayStart = (EditText) view.findViewById(R.id.holiday_add_start_from);
        holidayEnd = (EditText) view.findViewById(R.id.holiday_add_end_at);
        holidayAddSubmit = (Button) view.findViewById(R.id.holiday_add_submit);
        holidayAddReset = (Button) view.findViewById(R.id.holiday_add_reset);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startDay = null;
        endDay = null;
        addHeading.setText("Add holiday for "+year);
        holidayStart.setOnFocusChangeListener(this);
        holidayEnd.setOnFocusChangeListener(this);
        holidayAddSubmit.setOnClickListener(this);
        holidayAddReset.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId()==R.id.holiday_add_start_from && hasFocus)
        {
            Toast.makeText(getActivity().getApplicationContext(),"Start",Toast.LENGTH_SHORT);
            DatepickerDialog datepickerDialog = new DatepickerDialog("holidayStart");
            datepickerDialog.show(getFragmentManager(), "datePicker");
        }
        else if(v.getId()==R.id.holiday_add_end_at && hasFocus)
        {
            Toast.makeText(getActivity().getApplicationContext(),"End",Toast.LENGTH_SHORT);
            DatepickerDialog datepickerDialog = new DatepickerDialog("holidayEnd");
            datepickerDialog.show(getFragmentManager(), "datePicker");
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.holiday_add_submit)
        {
            holidayTitleValue = holidayTile.getText().toString();
            holidayStartValue = holidayStart.getText().toString();
            holidayEndValue = holidayEnd.getText().toString();
            holidayStartValue+="/"+startDay;
            holidayEndValue+="/"+endDay;
            Toast.makeText(getActivity().getApplicationContext(),"Title : "+holidayTitleValue+" "+holidayStartValue+" "+holidayEndValue,Toast.LENGTH_SHORT).show();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("holiday").child(year);
            databaseReference.push().setValue(new Holiday(holidayTitleValue, holidayStartValue, holidayEndValue), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError==null)
                    {
                        Snackbar snackbar = Snackbar.make(view,"Holiday is added",Snackbar.LENGTH_SHORT);
                        snackbar.setAction("Back", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().popBackStack();
                            }
                        });
                        snackbar.show();
                    }
                }
            });

        }
        if(v.getId()==R.id.holiday_add_reset)
        {
            holidayTile.setText("");
            holidayStart.setText("");
            holidayEnd.setText("");
            Toast.makeText(getActivity().getApplicationContext(),"All Fields are Reset",Toast.LENGTH_SHORT).show();
        }

    }
}
