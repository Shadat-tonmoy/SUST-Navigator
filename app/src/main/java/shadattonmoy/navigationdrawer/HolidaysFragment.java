package shadattonmoy.navigationdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shadat Tonmoy on 8/31/2017.
 */

public class HolidaysFragment extends android.app.Fragment {


    private TextView holidayName,holidayDate, holidayDays;
    private ArrayList<Holiday> holidays;
    ListView holidayList;
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
        holidayName = (TextView)view.findViewById(R.id.holiday_name);
        holidayDate = (TextView)view.findViewById(R.id.holiday_date);
        holidayDays = (TextView)view.findViewById(R.id.holiday_days);
        holidayList = (ListView)view.findViewById(R.id.holiday_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        holidays = new ArrayList<Holiday>();
        holidays.add(new Holiday("Fatema Yeazdaham","10\nJan","10 Jan, Tue", "01 Day"));
        holidays.add(new Holiday("Swaraswati Puza","01\nFeb","01 Feb, Wed","01 Day"));
        holidays.add(new Holiday("Internation Mother Language Day","21\nFeb","21 Feb, Tue","01 Day"));
        holidays.add(new Holiday("Birthday of Bangabandhu","17\nMar","17 Mar, Fri","01 Day"));
        holidays.add(new Holiday("Independence Day","26\nMar","26 Mar, Sun","01 Day"));
        holidays.add(new Holiday("Bangla New Yeary","14\nApr","14 Apr, Fri","01 Day"));
        holidays.add(new Holiday("Shab e Merraz","24\nApr","24 Apr, Mon","01 Day"));
        holidays.add(new Holiday("May Day","01\nMay","01 May, Mon","01 Day"));
        holidays.add(new Holiday("Buddha Purnima","10\nMay","10 May, Wed","01 Day"));
        holidays.add(new Holiday("Summer Vacation (Class)","04\nJun","04 Jun,Sun - 21 Jun,Wed","18 Days"));
        holidays.add(new Holiday("Summer Vacation (Office)","11\nJun","11 Jun,Sun - 21 Jun,Wed","11 Days"));
        holidays.add(new Holiday("Eid-ul-Fitrea & Sahab-e-Kadar (Class)","22\nJun","22 Jun,Mon - 04 July,Wed","13 Days"));
        holidays.add(new Holiday("Summer Vacation (Office)","11\nJun","11 Jun,Sun - 21 Jun,Wed","11 Days"));
        holidays.add(new Holiday("Eid-ul-Fitrea & Sahab-e-Kadar (Class)","22\nJun","22 Jun,Mon - 04 July,Wed","13 Days"));
        holidays.add(new Holiday("Summer Vacation (Office)","11\nJun","11 Jun,Sun - 21 Jun,Wed","11 Days"));
        holidays.add(new Holiday("Eid-ul-Fitrea & Sahab-e-Kadar (Class)","22\nJun","22 Jun,Mon - 04 July,Wed","13 Days"));
        holidays.add(new Holiday("Eid-ul-Fitrea & Sahab-e-Kadar (Class)","22\nJun","22 Jun,Mon - 04 July,Wed","13 Days"));
        holidays.add(new Holiday("Summer Vacation (Office)","11 Jun","11\nJun,Sun - 21 Jun,Wed","11 Days"));
        holidays.add(new Holiday("Eid-ul-Fitrea & Sahab-e-Kadar (Class)","22\nJun","22 Jun,Mon - 04 July,Wed","13 Days"));
        holidays.add(new Holiday("Summer Vacation (Office)","11\nJun","11 Jun,Sun - 21 Jun,Wed","11 Days"));
        holidays.add(new Holiday("Eid-ul-Fitrea & Sahab-e-Kadar (Class)","22\nJun","22 Jun,Mon - 04 July,Wed","13 Days"));

        holidayList.setAdapter(new HolidayAdapter(getActivity().getApplicationContext(),R.layout.holiday_single_row,R.id.holiday_date,holidays));


    }
}
