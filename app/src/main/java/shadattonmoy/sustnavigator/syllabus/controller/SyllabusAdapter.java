package shadattonmoy.sustnavigator.syllabus.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.admin.view.CourseEditFragment;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.syllabus.view.SyllabusFragment;
import shadattonmoy.sustnavigator.utils.Values;

/**
 * Created by Shadat Tonmoy on 8/30/2017.
 */

public class SyllabusAdapter extends ArrayAdapter<Course>{

    private View row;
    private TextView courseCodeView,courseTitleView,courseCreditView,courseIconView;
    private String courseCode,courseTitle,courseCredit,courseId,dept,semester,session;
    private ImageView courseEditIcon;
    private boolean isEditable;
    private Activity activity;
    private android.app.FragmentManager manager;
    public SyllabusAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull ArrayList<Course> objects,boolean isEditable,android.app.FragmentManager manager,String dept, String semester,String session,Activity activity) {
        super(context, resource, textViewResourceId, objects);
        this.isEditable = isEditable;
        this.manager = manager;
        this.dept = dept;
        this.semester = semester;
        this.session = session;
        this.activity = activity;
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
        if(isEditable || Values.IS_LOCAL_ADMIN)
        {
            courseEditIcon.setVisibility(View.VISIBLE);
            courseEditIcon.setImageResource(R.drawable.more_vert_black);
            final PopupMenu popupMenu = new PopupMenu(getContext(),courseEditIcon, Gravity.LEFT);
            popupMenu.inflate(R.menu.course_manage_menu);
            courseEditIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

            popupMenu.setOnMenuItemClickListener(new menuItemClickHandler(getContext(),manager,currentCourse,dept,semester,row,session,activity));
        }

        return row;
    }
}
class menuItemClickHandler implements PopupMenu.OnMenuItemClickListener{

    private Context context;
    private Course course;
    private android.app.FragmentManager manager;
    private String dept,semester,session;
    private View view;
    private Activity activity;
    menuItemClickHandler(Context context,android.app.FragmentManager manager,Course course,String dept, String semester,View view,String session,Activity activity){
        this.context = context;
        this.manager = manager;
        this.course = course;
        this.dept = dept;
        this.semester = semester;
        this.view = view;
        this.session = session;
        this.activity =activity;
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
            CourseEditFragment courseEditFragment = new CourseEditFragment(dept,semester,courseCode,courseTitle,courseCredit,course.getCourse_id(),session);
            android.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_root,courseEditFragment);
            transaction.addToBackStack("course_edit_fragment");
            transaction.commit();
            return true;
        }
        else if (id == R.id.remove_course_menu)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Please Notice");
            builder.setMessage("Are you sure to permanently remove this Course Information From Record? Once you delete you will not be able to restore again.");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(Values.IS_LOCAL_ADMIN)
                    {
                        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
                        sqLiteAdapter.deleteSingleCourse(course.getCourse_id());
                        Snackbar snackbar = Snackbar.make(view,"Course has been removed",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        SyllabusFragment.adapter.remove(course);
                    }
                    else
                    {
                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(activity);
                        progressDialog.setTitle("Deleting Record");
                        progressDialog.setMessage("Please Wait....");
                        progressDialog.show();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference().child("syllabus").child(session).child(dept).child(semester).child(course.getCourse_id());
                        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError==null)
                                {
                                    Snackbar snackbar = Snackbar.make(view,"Course has been removed",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    progressDialog.dismiss();
                                    SyllabusFragment.adapter.remove(course);

                                }
                            }
                        });
                    }

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return true;

        }
        return false;
    }
}
