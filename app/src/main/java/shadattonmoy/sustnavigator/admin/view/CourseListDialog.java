package shadattonmoy.sustnavigator.admin.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
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
import shadattonmoy.sustnavigator.proctor.model.Proctor;

public class CourseListDialog extends android.app.DialogFragment {

    private View view;
    private Context context;
    private List<Course> courses;
    private String session,dept,semester,sessionToClone;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView nothingFoundImage;
    private TextView nothingFoundText;
    ListView courseList;
    ProgressBar progressBar;
    private  AlertDialog.Builder builder;

    public CourseListDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.course_list_dialog,null);
        courseList= (ListView) view.findViewById(R.id.course_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);

        initialize();
        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setPositiveButton("Clone These Course", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                generateNewCourses();

            }
        });
        return builder.create();
    }

    private void initialize()
    {
        context = getActivity().getApplicationContext();
        Bundle args = getArguments();
        if(args!=null)
        {
            this.session = args.getString("session");
            this.dept= args.getString("dept");
            this.semester= args.getString("semester");
            this.sessionToClone= args.getString("sessionToClone");
        }
        getCoursesFromServer();


    }

    public void getCoursesFromServer()
    {
        courses = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(sessionToClone).child(dept).child(semester);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() )
                {
                    Course currentCourse = child.getValue(Course.class);
                    String pushId = child.getKey();
                    currentCourse.setCourse_id(pushId);
                    courses.add(currentCourse);
                }

                if(courses.size()>0)
                {
                    progressBar.setVisibility(View.GONE);
                    AllCourseListAdapter allCourseListAdapter = new AllCourseListAdapter(context,R.layout.course_list_dialog,R.id.course_code, (ArrayList<Course>) courses);
                    courseList.setAdapter(allCourseListAdapter);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    nothingFoundText.setText("OOOPS!!! No Records Found");
                    try{
                        Glide.with(context).load(context.getResources()
                                .getIdentifier("nothing_found", "drawable", context.getPackageName())).thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(nothingFoundImage);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    builder.setPositiveButton("", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void generateNewCourses()
    {
        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept).child(semester);
        for(Course course:courses)
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
        }


    }
}
