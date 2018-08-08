package shadattonmoy.sustnavigator.syllabus.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.view.ScanSyllabusDetailFragment;

public class SyllabusDetailFragment extends Fragment {
    private View view;
    private Course course;
    private boolean isAdmin;
    private TextView courseCodeView, courseCreditView, courseDetailView, nothingFoundText;
    private ImageView nothingFoundImage;
    private String courseCode, courseCredit, courseDetail;
    private Context context;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton addCustomSyllabusFab, scanFromSyllabusFab;
    private String session, dept, semester;


    public SyllabusDetailFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = (Course) getArguments().getSerializable("course");
            session = getArguments().getString("session");
            semester = getArguments().getString("semester");
            dept = getArguments().getString("dept");
            isAdmin = getArguments().getBoolean("isAdmin", false);
        }
        context = getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_syllabus_detail, container, false);
        courseCodeView = (TextView) view.findViewById(R.id.course_code);
        courseCreditView = (TextView) view.findViewById(R.id.course_credit);
        courseDetailView = (TextView) view.findViewById(R.id.course_detail);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);
        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.add_fab);
        scanFromSyllabusFab = (FloatingActionButton) view.findViewById(R.id.scan_detail_fab);
        addCustomSyllabusFab = (FloatingActionButton) view.findViewById(R.id.custom_detail_fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateView();

    }

    public void populateView() {
        courseCode = course.getCourse_code();
        courseCredit = course.getCourse_credit();
        courseDetail = course.getCourseDetail();
        courseCodeView.setText(courseCode);
        courseCreditView.setText(courseCredit + " Credits");
        if (course.getCourseDetail() == null || course.getCourseDetail().length() == 0) {
            nothingFoundImage.setVisibility(View.VISIBLE);
            nothingFoundText.setVisibility(View.VISIBLE);
        } else {
            courseDetailView.setText(course.getCourseDetail());
        }

        if (isAdmin) {
            nothingFoundText.setText("OOOPS!!! No Course Details Available. Tap '+' to Add");
            floatingActionMenu.setVisibility(View.VISIBLE);
            populateFloatingActionMenu();
        } else
            nothingFoundText.setText("OOOPS!!! No Course Details Available. Please Contact Admin");
        try {
            Glide.with(context).load(context.getResources()
                    .getIdentifier("nothing_found", "drawable", context.getPackageName())).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(nothingFoundImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateFloatingActionMenu() {
        addCustomSyllabusFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Custom", Toast.LENGTH_SHORT).show();

            }
        });

        scanFromSyllabusFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                ScanSyllabusDetailFragment scanSyllabusDetailFragment = new ScanSyllabusDetailFragment();
                Bundle args = new Bundle();
                args.putSerializable("course", course);
                args.putString("session", session);
                args.putString("semester", semester);
                args.putString("dept", dept);
                scanSyllabusDetailFragment.setArguments(args);
                transaction.replace(R.id.main_content_root, scanSyllabusDetailFragment);
                transaction.addToBackStack("scanSyllabusDetailFragment");
                transaction.commit();
            }
        });
    }
}
