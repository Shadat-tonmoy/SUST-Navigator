package shadattonmoy.sustnavigator.admin.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.List;

import shadattonmoy.sustnavigator.AllCourseListAdapter;
import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.teacher.controller.TeacherListAdapter;
import shadattonmoy.sustnavigator.teacher.model.Teacher;
import shadattonmoy.sustnavigator.utils.Values;

public class FacultyListFromWebDialog extends android.app.DialogFragment {

    private View view;
    private Context context;
    private ArrayList<Teacher> teachers;
    private TeacherListAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ListView facultyList;
    private  AlertDialog.Builder builder;
    private Dept dept;
    private FragmentActivity activity;
    private TextView selectAllBtn,saveSelectedBtn;
    private boolean isSelectAll = false;

    public FacultyListFromWebDialog() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity= (FragmentActivity) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.faculty_list_from_web_dialog,null);
        facultyList= view.findViewById(R.id.faculty_list);
        selectAllBtn=  view.findViewById(R.id.select_all_btn);
        saveSelectedBtn=  view.findViewById(R.id.save_selected_btn);
        context = getActivity();
        activity = (FragmentActivity) getActivity();

        initialize();
        builder.setView(view);

        return builder.create();
    }

    private void initialize()
    {
        adapter = new TeacherListAdapter(context, R.layout.teacher_single_row, R.id.teacher_icon, teachers, dept);
        adapter.setActivity(activity);
        adapter.setWebData(true);
        facultyList.setAdapter(adapter);
        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelectAll)
                {
                    isSelectAll = false;
                    adapter.toggleAllTeacher();
                    selectAllBtn.setText("Select All");
                }
                else
                {
                    isSelectAll = true;
                    adapter.toggleAllTeacher();
                    selectAllBtn.setText("Select None");
                }
            }
        });

        saveSelectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalTeacher = adapter.getSelectedTeachers();
                if(totalTeacher>0)
                {
                    showConfirmationDialog(totalTeacher);
                }
                else Values.showToast(context,"No Faculty is Selected");
            }
        });

    }

    private void showConfirmationDialog(int size)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Are You Sure?")
                .setMessage("You are going to add "+size+" Faculty Records to Database.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.addAllTeacherToServer();
                        dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .show();
    }

    public void getCoursesFromServer()
    {



    }

    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public void generateNewCourses()
    {
//        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept).child(semester);
     /*   for(Course course:courses)
        {
            databaseReference.push().setValue(course, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(view, "Syllabus Cloned", Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
                    snackbar.setAction("Back", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFragmentManager().popBackStack();
                        }
                    });
                    snackbar.show();

                }
            });
        }*/


    }
}
