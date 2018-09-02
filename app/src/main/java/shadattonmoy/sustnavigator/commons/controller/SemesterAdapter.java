package shadattonmoy.sustnavigator.commons.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.commons.model.Semester;
import shadattonmoy.sustnavigator.utils.Values;

/**
 * Created by Shadat Tonmoy on 8/29/2017.
 */

public class SemesterAdapter extends ArrayAdapter<Semester> {
    private TextView semesterCodeView,semesterNameView,totalCourseView,totalCreditView;
    private String semesterCode,semesterName,totalCourse,totalCredit;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView deleteSemesterIcon;
    private FragmentActivity activity;

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
            deleteSemesterIcon = (ImageView) row.findViewById(R.id.delete_semester_icon);
        }
        final Semester currentSemester = getItem(position);
        semesterCode = currentSemester.getSemesterCode();
        semesterName = currentSemester.getSemesterName();
        totalCourse = currentSemester.getTotalCourse();
        totalCredit = currentSemester.getTotalCredit();

        if(Values.IS_LOCAL_ADMIN)
        {
            deleteSemesterIcon.setVisibility(View.VISIBLE);
            deleteSemesterIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteConfirmationDialog(currentSemester);

                }
            });
        }
        else deleteSemesterIcon.setVisibility(View.GONE);



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

    public void showDeleteConfirmationDialog(final Semester semester)
    {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setTitle("Sure to delete")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
                        sqLiteAdapter.deleteSemester(semesterCode);
                        remove(semester);
                        Toast.makeText(context,"Removed",Toast.LENGTH_SHORT).show();


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
}
