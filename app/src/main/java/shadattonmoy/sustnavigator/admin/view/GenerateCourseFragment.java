package shadattonmoy.sustnavigator.admin.view;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shadattonmoy.sustnavigator.AllCourseListAdapter;
import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.proctor.model.Proctor;
import shadattonmoy.sustnavigator.utils.Values;


public class GenerateCourseFragment extends android.app.Fragment {

    private View view, detectedTextLayout;
    private ArrayList<String> detectedTexts;
    private FlexboxLayout detectedCodeContainer,detectedTitleContainer,detectedCreditContainer;
    private TextView detectedTextView,courseCodeButton,courseTitleButton,courseCreditButton,addMoreCourseButton,coursesButton;
    private EditText courseCodeField, courseCreditField, courseTitleField;
    private DragAndDropListener dragAndDropListener;
    private DragAndDropListenerForTextView dragAndDropListenerForTextView;
    private ListView courseList;
    private List<Course> courses;
    private AllCourseListAdapter courseListAdapter;
    private String textToDrop;
    private Button courseAddSubmitButton;
    private Boolean sendToServer = false;
    private Context context;
    private CardView generatedTextContainer,courseListContainer;
    private ScrollView courseFieldContainer;
    private String session,semester,dept;
    private TextView courseAddDoneButton;
    private FragmentActivity activity;
    private TextInputLayout courseCodeLayout,courseTitleLayout,courseCreditLayout;
    private int counter;

    public GenerateCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null)
        {
            detectedTexts = args.getStringArrayList("detectedText");
            session = args.getString("session");
            semester = args.getString("semester");
            dept = args.getString("dept");
        }

        courses = new ArrayList<>();
        courseListAdapter = new AllCourseListAdapter(context,R.layout.syllabus_single_row,R.id.course_icon, (ArrayList<Course>) courses);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_generate_course, container, false);
        detectedCodeContainer = (FlexboxLayout) view.findViewById(R.id.detected_code_container);
        detectedTitleContainer = (FlexboxLayout) view.findViewById(R.id.detected_title_container);
        detectedCreditContainer = (FlexboxLayout) view.findViewById(R.id.detected_credit_container);
        courseListContainer = (CardView) view.findViewById(R.id.course_list_container);
        generatedTextContainer = (CardView) view.findViewById(R.id.generated_text_container);
        courseFieldContainer = (ScrollView) view.findViewById(R.id.course_field_container);
        courseCodeField = (EditText) view.findViewById(R.id.course_code_field);
        courseCreditField = (EditText) view.findViewById(R.id.course_credit_field);
        courseTitleField = (EditText) view.findViewById(R.id.course_title_field);
        courseCodeLayout = (TextInputLayout) view.findViewById(R.id.code_layout);
        courseTitleLayout = (TextInputLayout) view.findViewById(R.id.title_layout);
        courseCreditLayout = (TextInputLayout) view.findViewById(R.id.credit_layout);
        courseCodeButton = (TextView) view.findViewById(R.id.course_code_button);
        courseTitleButton = (TextView) view.findViewById(R.id.course_title_button);
        courseCreditButton = (TextView) view.findViewById(R.id.course_credit_button);
        addMoreCourseButton = (TextView) view.findViewById(R.id.add_more_course_button);
        courseAddDoneButton = (TextView) view.findViewById(R.id.course_add_done_button);
        coursesButton = (TextView) view.findViewById(R.id.courses_button);
        courseList = (ListView) view.findViewById(R.id.course_list);
        courseAddSubmitButton = (Button) view.findViewById(R.id.course_add_submit_btn);
        detectedCreditContainer.setVisibility(View.GONE);
        detectedTitleContainer.setVisibility(View.GONE);
        initTabClickListener();
        generateDetectedTextsLayout(inflater);
        return view;
    }

    public void initTabClickListener()
    {
        courseCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectedCodeContainer.setVisibility(View.GONE);
                detectedTitleContainer.setVisibility(View.GONE);
                detectedCreditContainer.setVisibility(View.VISIBLE);
            }
        });
        courseTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectedCodeContainer.setVisibility(View.GONE);
                detectedTitleContainer.setVisibility(View.VISIBLE);
                detectedCreditContainer.setVisibility(View.GONE);
            }
        });
        courseCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectedCodeContainer.setVisibility(View.VISIBLE);
                detectedTitleContainer.setVisibility(View.GONE);
                detectedCreditContainer.setVisibility(View.GONE);
            }
        });

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseListContainer.setVisibility(View.VISIBLE);
                courseFieldContainer.setVisibility(View.GONE);
                generatedTextContainer.setVisibility(View.GONE);


            }
        });

        addMoreCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                courseListContainer.setVisibility(View.GONE);
                courseFieldContainer.setVisibility(View.VISIBLE);
                generatedTextContainer.setVisibility(View.VISIBLE);
                courseAddSubmitButton.setText(R.string.done);
                courseAddSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCourseToList();

                    }
                });
            }
        });

        courseAddDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourseToServer();
            }
        });

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dragAndDropListener = new DragAndDropListener();
        courseCreditField.setOnDragListener(dragAndDropListener);
        courseTitleField.setOnDragListener(dragAndDropListener);
        courseCodeField.setOnDragListener(dragAndDropListener);
        courseList.setAdapter(courseListAdapter);


    }

    public void addCourseToList()
    {
        boolean isValid = true;
        String courseCode = courseCodeField.getText().toString();
        String courseTitle = courseTitleField.getText().toString();
        String courseCredit = courseCreditField.getText().toString();
        if(courseCode.length()==0 || courseCode.equals(""))
        {
            courseCodeLayout.setErrorEnabled(true);
            Toast.makeText(context,"Please enter a valid course code",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else {

            courseCodeLayout.setErrorEnabled(false);
            isValid = true;
        }

        if(isValid && (courseTitle.length()==0 || courseTitle.equals("")))
        {
            courseTitleLayout.setErrorEnabled(true);
            Toast.makeText(context,"Please enter a valid course title",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else {

            courseCodeLayout.setErrorEnabled(false);
            isValid = true;
        }
        if(isValid && (courseCredit.length()==0 || courseCredit.equals("")))
        {
            courseCreditLayout.setErrorEnabled(true);
            Toast.makeText(context,"Please enter a valid course credit",Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else {

            courseCodeLayout.setErrorEnabled(false);
            isValid = true;
        }
        if(isValid)
        {
            courses.add(new Course(courseCode,courseTitle,courseCredit));
            courseListAdapter.notifyDataSetChanged();
            coursesButton.performClick();
        }

    }

    public void addCourseToServer()
    {
        if(courses.size()==0)
        {
            Toast.makeText(context,"No Course To Add. Tap 'Add New Course' to add course",Toast.LENGTH_SHORT).show();
        }
        else
        {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept).child(semester);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Adding Course Record");
            progressDialog.setMessage("Please Wait....");
            progressDialog.show();
            counter = 0;
            for(Course course:courses)
            {
                databaseReference.push().setValue(course, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        counter++;
                        if(counter>=courses.size())
                        {
                            progressDialog.dismiss();
                            Snackbar snackbar = Snackbar.make(view, "Course Added successfully", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getFragmentManager().popBackStack();
                                }
                            }).setActionTextColor(context.getResources().getColor(R.color.blue));
                            snackbar.show();

                        }

                    }
                });
            }


        }

    }

    public void generateDetectedTextsLayout(LayoutInflater inflater) {
        detectedTitleContainer.removeAllViews();
        detectedCodeContainer.removeAllViews();
        detectedCreditContainer.removeAllViews();
        int i=0;
        for (String text : detectedTexts) {
            TextView detectedTextView;
            View detectedTextLayout;
            Log.e("TextDetected", text);
            detectedTextLayout = inflater.inflate(R.layout.detected_text_chip, null, false);
            detectedTextView = (TextView) detectedTextLayout.findViewById(R.id.detected_text_chip);
            detectedTextView.setId(i);
            i++;
            detectedTextView.setText(text);
            detectedTextView.setOnTouchListener(new MyTouchListener());
            if (detectedTextView.getParent() != null)
                ((ViewGroup) detectedTextView.getParent()).removeView(detectedTextView);
            if(getDetectedTextType(text)==Values.DETECTED_TYPE_COURSE_CODE)
            {
                detectedCodeContainer.addView(detectedTextView);

            }
            else if(getDetectedTextType(text)==Values.DETECTED_TYPE_COURSE_TITLE)
            {
                detectedTitleContainer.addView(detectedTextView);

            }
            else if(getDetectedTextType(text)==Values.DETECTED_TYPE_COURSE_CREDIT)
            {
                detectedCreditContainer.addView(detectedTextView);

            }
            else {
                detectedCreditContainer.addView(detectedTextView);
            }

        }
        dragAndDropListenerForTextView = new DragAndDropListenerForTextView();
    }

    private int getDetectedTextType(String text)
    {
        String patternOfCode = "^[A-Za-z]{3}[ ]*[0-9A-Z*]+$";
        String patternOfTitle = "^[A-Za-z ]+$";
        String patternOfCredit = "^[0-9 .]+$";

        Pattern pattern = Pattern.compile(patternOfCode);
        Matcher matcher = pattern.matcher(text);
        if(matcher.matches())
        {
            return Values.DETECTED_TYPE_COURSE_CODE;
        }
        pattern = Pattern.compile(patternOfTitle);
        matcher = pattern.matcher(text);
        if(matcher.matches())
        {
            return Values.DETECTED_TYPE_COURSE_TITLE;
        }
        pattern = Pattern.compile(patternOfCredit);
        matcher = pattern.matcher(text);
        if(matcher.matches())
        {
            return Values.DETECTED_TYPE_COURSE_CREDIT;
        }
        return -1;
    }

    private final class MyTouchListener implements View.OnTouchListener {
        /*public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
//                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }*/

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e("OnTouch","TouchedOn "+view.getId());
                ClipData data = ClipData.newPlainText("text", "");
                dragAndDropListenerForTextView.setViewToDrag(view);
                view.setOnDragListener(dragAndDropListenerForTextView);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);

                return true;
            }
            return false;

        }
    }


    private class DragAndDropListenerForTextView implements View.OnDragListener {
        private View viewToDrag = null;



        @Override
        public boolean onDrag(View v, DragEvent event) {
            Log.e("Event","ONDrag");
            textToDrop = "";
            if(v.getId() == viewToDrag.getId())
            {
                switch(event.getAction())
                {
                    case DragEvent.ACTION_DRAG_STARTED:
                        textToDrop = ((TextView)v).getText().toString();
//                    Log.e("Event","OnDragStartedForTextView"+textToDrop+" ID "+v.getId());
                        break;
                    case DragEvent.ACTION_DROP:
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        textToDrop = ((TextView)v).getText().toString();
                        Log.e("Event","OnDragStartedEndedTextView"+textToDrop+" ID "+v.getId());
                        dragAndDropListener.setStringToDrop(textToDrop);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.e("Event","OnDragExited");
                        break;
                    default: break;
                }
            }

            return false;
        }

        public View getViewToDrag() {
            return viewToDrag;
        }

        public void setViewToDrag(View viewToDrag) {
            this.viewToDrag = viewToDrag;
        }
    }

    private class DragAndDropListener implements View.OnDragListener {
        private String stringToDrop;


        @Override
        public boolean onDrag(View v, DragEvent event) {
//            Log.e("Event","ONDrag");
            switch(event.getAction())
            {
                case DragEvent.ACTION_DRAG_STARTED:
//                    textToDrop = ((TextView)v).getText().toString();
                    Log.e("Event","OnDragStarted "+v.toString());
                    break;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    CharSequence paste = item.getText();
                    Log.e("OnDrop",paste.toString());
                    if(v.getId()==R.id.course_code_field || v.getId()==R.id.course_title_field || v.getId()==R.id.course_credit_field)
                    {
                        Log.e("OnDrop"," Code Field "+paste.toString()+ " TextToDrop  "+this.stringToDrop);
                        ((EditText) v).setText(stringToDrop);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.e("Event","OnDragEnded");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.e("Event","OnDragExited");
                    break;
                default: break;
            }

            return false;
        }

        public String getStringToDrop() {
            return stringToDrop;
        }

        public void setStringToDrop(String stringToDrop) {
            this.stringToDrop = stringToDrop;
        }
    }


}
