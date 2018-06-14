package shadattonmoy.sustnavigator.cgpa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.cgpa.view.CGPAFragment;

/**
 * Created by Shadat Tonmoy on 9/28/2017.
 */

public class CustomCourseAddForCGPA extends DialogFragment {
    private View view;
    private EditText courseCode,courseTitle,courseCredit;
    private String courseCodeValue,courseTitleValue,courseCreditValue;
    private Button submitButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_course_dialog_layout,null);
        courseCode = (EditText) view.findViewById(R.id.custom_course_code);
        courseTitle = (EditText) view.findViewById(R.id.custom_course_title);
        courseCredit = (EditText) view.findViewById(R.id.custom_course_credit);
        submitButton = (Button) view.findViewById(R.id.custom_course_submit_btn);
        dialog.setView(view);
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabValue();
                dismiss();
                Toast.makeText(getActivity().getApplicationContext(),"Custom Course Added",Toast.LENGTH_SHORT).show();
            }
        });
        return dialog.create();
    }

    void grabValue(){
        courseCodeValue = courseCode.getText().toString();
        courseTitleValue = courseTitle.getText().toString();
        courseCreditValue = courseCredit.getText().toString();
        Course course = new Course(courseCodeValue,courseTitleValue,courseCreditValue);
        CGPAFragment.adapter.add(course);
        CGPAFragment.extraCredit += Float.parseFloat(courseCreditValue);



    }
}
