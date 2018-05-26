package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shadat Tonmoy on 9/6/2017.
 */

public class AllCourseListAdapter extends ArrayAdapter<Course>{
    private View row;
    private TextView semesterHeader,debugView;
    private ListView semesterCourseList;
    private TextView courseCodeView,courseTitleView,courseCreditView,courseIconView;
    private String courseCode,courseTitle,courseCredit,courseId,dept,semester;
    private ImageView courseEditIcon;
    public AllCourseListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull ArrayList<Course> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.syllabus_single_row,parent,false);

        }
        courseCodeView = (TextView) row.findViewById(R.id.course_code);
        courseTitleView = (TextView) row.findViewById(R.id.course_title);
        courseCreditView = (TextView) row.findViewById(R.id.course_credit);
        courseIconView = (TextView) row.findViewById(R.id.course_icon);
        courseEditIcon = (ImageView) row.findViewById(R.id.edit_course_icon1);
        Course currentCourse = getItem(position);
        courseCode = currentCourse.getCourse_code();
        courseTitle = currentCourse.getCourse_title();
        courseCredit = currentCourse.getCourse_credit();
        courseId = currentCourse.getCourse_id();
        courseCodeView.setText(courseCode);
        courseTitleView.setText(courseTitle);
        String courseIconText = courseCode.substring(0,3);
        courseIconView.setText(courseIconText);
        courseCreditView.setText(courseCredit + " Credits");
        courseEditIcon.setImageResource(R.drawable.add_in_dialog);


        return row;
    }



}
