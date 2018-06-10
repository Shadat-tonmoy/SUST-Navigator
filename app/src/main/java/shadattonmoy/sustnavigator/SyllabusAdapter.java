package shadattonmoy.sustnavigator;

import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Shadat Tonmoy on 8/30/2017.
 */

public class SyllabusAdapter extends ArrayAdapter<Course>{

    private View row;
    private TextView courseCodeView,courseTitleView,courseCreditView,courseIconView;
    private String courseCode,courseTitle,courseCredit,courseId,dept,semester;
    private ImageView courseEditIcon;
    private boolean isEditable;
    private android.app.FragmentManager manager;
    public SyllabusAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull ArrayList<Course> objects,boolean isEditable,android.app.FragmentManager manager,String dept, String semester) {
        super(context, resource, textViewResourceId, objects);
        this.isEditable = isEditable;
        this.manager = manager;
        this.dept = dept;
        this.semester = semester;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        if(isEditable)
        {
            courseEditIcon.setImageResource(R.drawable.edit_icon);
            final PopupMenu popupMenu = new PopupMenu(getContext(),courseEditIcon, Gravity.LEFT);
            popupMenu.inflate(R.menu.course_manage_menu);
            courseEditIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

            popupMenu.setOnMenuItemClickListener(new menuItemClickHandler(getContext(),manager,currentCourse,dept,semester,row));
        }

        return row;
    }
}
class menuItemClickHandler implements PopupMenu.OnMenuItemClickListener{

    private Context context;
    private Course course;
    private android.app.FragmentManager manager;
    private String dept,semester;
    private View view;
    menuItemClickHandler(Context context,android.app.FragmentManager manager,Course course,String dept, String semester,View view){
        this.context = context;
        this.manager = manager;
        this.course = course;
        this.dept = dept;
        this.semester = semester;
        this.view = view;
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.edit_course_menu)
        {
            String courseId = course.getCourse_id();
            String courseTitle = course.getCourse_title();
            String courseCode = course.getCourse_code();
            String courseCredit = course.getCourse_credit();
            CourseEditFragment courseEditFragment = new CourseEditFragment(dept,semester,courseCode,courseTitle,courseCredit,courseId);
            android.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_root,courseEditFragment);
            transaction.addToBackStack("course_edit_fragment");
            transaction.commit();
            Toast.makeText(context,"Edit course" + courseId,Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.remove_course_menu)
        {
            //Toast.makeText(context,"Remove course" + courseId,Toast.LENGTH_SHORT).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child("syllabus").child(dept).child(semester).child(course.getCourse_id());
            databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError==null)
                    {
                        Snackbar snackbar = Snackbar.make(view,"Course has been removed",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        SyllabusFragment.adapter.remove(course);

                    }
                }
            });

            return true;

        }
        return false;
    }
}
