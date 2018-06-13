package shadattonmoy.sustnavigator.commons.controller;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.commons.model.Semester;

/**
 * Created by Shadat Tonmoy on 8/29/2017.
 */

public class SemesterAdapter extends ArrayAdapter<Semester> {
    private TextView semesterCodeView,semesterNameView,totalCourseView,totalCreditView;
    private String semesterCode,semesterName,totalCourse,totalCredit;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public SemesterAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Semester> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.semester_single_row,parent,false);
            semesterCodeView = (TextView) row.findViewById(R.id.semester_icon);
            semesterNameView = (TextView) row.findViewById(R.id.semester_title);
            totalCreditView = (TextView) row.findViewById(R.id.total_credit_in_semester);
            totalCourseView = (TextView) row.findViewById(R.id.number_of_course_in_semester);
        }
        Semester currentSemester = getItem(position);
        semesterCode = currentSemester.getSemesterCode();
        semesterName = currentSemester.getSemesterName();
        totalCourse = currentSemester.getTotalCourse();
        totalCredit = currentSemester.getTotalCredit();



        semesterCodeView.setText(semesterCode);
        semesterNameView.setText(semesterName);
        totalCourseView.setText("Total Courses : "+totalCourse);
        totalCreditView.setText("Total Credits : "+totalCredit);

        if(semesterCode.equals("1/1"))
            semesterCodeView.setBackgroundResource(R.drawable.round_yellow);
        else if(semesterCode.equals("1/2"))
            semesterCodeView.setBackgroundResource(R.drawable.round_black);
        else if(semesterCode.equals("2/1"))
            semesterCodeView.setBackgroundResource(R.drawable.round_blue);
        else if(semesterCode.equals("2/2"))
            semesterCodeView.setBackgroundResource(R.drawable.round_green2);
        else if(semesterCode.equals("3/1"))
            semesterCodeView.setBackgroundResource(R.drawable.round_carrot);
        else if(semesterCode.equals("3/2"))
            semesterCodeView.setBackgroundResource(R.drawable.round_pumpkin);
        else if(semesterCode.equals("4/1"))
            semesterCodeView.setBackgroundResource(R.drawable.round_green3);
        else if(semesterCode.equals("4/2"))
            semesterCodeView.setBackgroundResource(R.drawable.round_yellow);

        return row;
    }
}
