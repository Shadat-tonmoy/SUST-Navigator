package shadattonmoy.sustnavigator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shadattonmoy.sustnavigator.holiday.model.Holiday;


public class HolidayAddFragment extends android.app.Fragment implements View.OnClickListener{
    private String year;
    private View view;
    private TextView addHeading,resetAllFieldButton;
    public static EditText holidayTile;
    public static TextView holidayStart,holidayEnd;
    static String startDay,endDay;
    private Button holidayAddSubmit;
    private String holidayTitleValue,holidayStartValue,holidayEndValue;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private AwesomeValidation awesomeValidation;
    private Context context;
    private Activity activity;
    public static long holidayStartTime, holidayEndTime;

    public HolidayAddFragment(String year) {
        this.year = year;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_holiday_add, container, false);
        addHeading = (TextView) view.findViewById(R.id.add_holiday_header);
        holidayTile = (EditText) view.findViewById(R.id.holiday_add_title);
        holidayStart = (TextView) view.findViewById(R.id.holiday_add_start_from);
        holidayEnd = (TextView) view.findViewById(R.id.holiday_add_end_at);
        holidayAddSubmit = (Button) view.findViewById(R.id.holiday_add_submit);
        resetAllFieldButton = (TextView) view.findViewById(R.id.reset_all_field_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.holiday_add_title, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_error);
        startDay = null;
        endDay = null;
        addHeading.setText("Fill The form to add a new Holiday Record for "+year);
        holidayStart.setOnClickListener(this);
        holidayEnd.setOnClickListener(this);
        holidayAddSubmit.setOnClickListener(this);
        resetAllFieldButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.holiday_add_submit)
        {
            if(awesomeValidation.validate())
            {
                if(startDay==null)
                {
                    Toast.makeText(context,"Please Choose a start date",Toast.LENGTH_SHORT).show();
                }
                if(endDay==null)
                {
                    Toast.makeText(context,"Please Choose an end date",Toast.LENGTH_SHORT).show();
                }
                if(startDay!=null && endDay!=null)
                {
                    holidayTitleValue = holidayTile.getText().toString();
                    holidayStartValue = holidayStart.getText().toString();
                    holidayEndValue = holidayEnd.getText().toString();
                    holidayStartValue+="/"+startDay;
                    holidayEndValue+="/"+endDay;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("holiday").child(year);
                    String numOfDays = String.valueOf(((holidayEndTime-holidayStartTime)/(1000*60*60*24))+1);
                    Holiday holiday = new Holiday(holidayTitleValue, holidayStartValue, holidayEndValue);
                    holiday.setHolidayDays(numOfDays);

                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Adding Record");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    databaseReference.push().setValue(holiday, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                progressDialog.dismiss();
                                Snackbar snackbar = Snackbar.make(view,"Holiday is added",Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("Back", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getFragmentManager().popBackStack();
                                    }
                                }).setActionTextColor(context.getResources().getColor(R.color.blue));
                                snackbar.show();
                            }
                        }
                    });
                }
            }


        }
        else if(v.getId()==R.id.holiday_add_start_from)
        {
            DatepickerDialog datepickerDialog = new DatepickerDialog("holidayStart");
            datepickerDialog.show(getFragmentManager(), "datePicker");
        }
        else if(v.getId()==R.id.holiday_add_end_at)
        {
            DatepickerDialog datepickerDialog = new DatepickerDialog("holidayEnd");
            datepickerDialog.show(getFragmentManager(), "datePicker");
        }
        if(v.getId()==R.id.reset_all_field_button)
        {
            holidayTile.setText("");
            holidayStart.setText("");
            holidayEnd.setText("");
            startDay = null;
            endDay = null;
            Toast.makeText(getActivity().getApplicationContext(),"All Fields are Reset",Toast.LENGTH_SHORT).show();
        }

    }
}
