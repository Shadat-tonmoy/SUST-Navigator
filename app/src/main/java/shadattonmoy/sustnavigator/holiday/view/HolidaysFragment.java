package shadattonmoy.sustnavigator.holiday.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import shadattonmoy.sustnavigator.HolidayAddFragment;
import shadattonmoy.sustnavigator.commons.view.AdminListBottomSheet;
import shadattonmoy.sustnavigator.holiday.controller.HolidayAdapter;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.holiday.model.Holiday;
import shadattonmoy.sustnavigator.utils.Values;

/**
 * Created by Shadat Tonmoy on 8/31/2017.
 */

public class HolidaysFragment extends android.app.Fragment {


    private TextView holidayName,holidayDate, holidayDays,holidayTitle,noHolidayFoundView;
    private ImageView noHolidayFoundImage;
    private ArrayList<Holiday> holidays;
    ListView holidayList;
    private int year=-1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private FragmentManager manager;
    private Context context;
    private AppBarLayout appBarLayout;
    private FloatingActionButton addHolidayFab;
    private boolean isAdmin;
    private View view;
    private FragmentActivity activity;


    public HolidaysFragment() {

    }

    public HolidaysFragment(boolean isAdmin) {
        this.isAdmin = isAdmin;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_holidays, container, false);
        holidayTitle = (TextView) view.findViewById(R.id.holiday_title);
        noHolidayFoundView = (TextView) view.findViewById(R.id.nothing_found_txt);
        holidayName = (TextView)view.findViewById(R.id.holiday_name);
        holidayDate = (TextView)view.findViewById(R.id.holiday_date);
        holidayDays = (TextView)view.findViewById(R.id.holiday_days);
        holidayList = (ListView)view.findViewById(R.id.holiday_list);
        progressBar = (ProgressBar) view.findViewById(R.id.holiday_loading);
        noHolidayFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        addHolidayFab = (FloatingActionButton) view.findViewById(R.id.add_holiday_fab);

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
        holidays = new ArrayList<Holiday>();
        noHolidayFoundView.setVisibility(View.GONE);
        appBarLayout.setExpanded(false);




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
                    holiday.setHoliayId(key);
                    holidays.add(holiday);
                    numOfHolidays++;
                }
                if(numOfHolidays==0)
                {
                    if(isAdmin)
                        noHolidayFoundView.setText(Html.fromHtml("Sorry!! No Holiday found for "+year+". <b>Tap '+' to add a new Holiday</b>"));
                    else {
                        noHolidayFoundView.setText(Html.fromHtml("Sorry!! No Holiday found for "+year+". Please <b>Contact Admin</b>"));
                        noHolidayFoundView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AdminListBottomSheet adminListBottomSheet = new AdminListBottomSheet();
                                Bundle args = new Bundle();
                                args.putInt("purpose",Values.CONTACT_FOR_HOLIDAY);
                                adminListBottomSheet.setArguments(args);
                                adminListBottomSheet.show(activity.getSupportFragmentManager(),"adminList");
                            }
                        });
                    }
                    try{
                        Glide.with(context).load(context.getResources()
                                .getIdentifier("nothing_found", "drawable", context.getPackageName())).thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(noHolidayFoundImage);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    noHolidayFoundView.setVisibility(View.VISIBLE);
                    noHolidayFoundImage.setVisibility(View.VISIBLE);

                }
                else
                {
                    HolidayAdapter adapter = new HolidayAdapter(getActivity().getApplicationContext(),R.layout.holiday_single_row,R.id.holiday_desc,holidays,isAdmin,manager,view);
                    holidayList.setAdapter(adapter);
                    adapter.setActivity(getActivity());
                }
                progressBar.setVisibility(View.GONE);
                if(isAdmin)
                {
                    addHolidayFab.setVisibility(View.VISIBLE);
                    addHolidayFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            HolidayAddFragment holidayAddFragment = new HolidayAddFragment(String.valueOf(year));
                            transaction.replace(R.id.main_content_root,holidayAddFragment);
                            transaction.addToBackStack("holiday_add_fragment");
                            transaction.commit();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
