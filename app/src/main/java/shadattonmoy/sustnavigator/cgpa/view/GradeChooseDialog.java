package shadattonmoy.sustnavigator.cgpa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import shadattonmoy.sustnavigator.AllCourseListAdapter;
import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.cgpa.controller.CGPAAdapter;

public class GradeChooseDialog  extends DialogFragment {
    private View view;
    private Context context;
    private TextView aPlus,a,aMinus,bPlus,b,bMinus,cPlus,c,cMinus,f,fRange;
    private String courseCredit,courseCode;
    private TextView listGradeView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        courseCredit = args.getString("credit");
        courseCode = args.getString("code");
    }

    public GradeChooseDialog(){
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        context = getActivity().getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.grade_choose_dialog,null);
        aPlus = (TextView) view.findViewById(R.id.a_plus);
        a = (TextView) view.findViewById(R.id.a);
        aMinus = (TextView) view.findViewById(R.id.a_minus);
        bPlus = (TextView) view.findViewById(R.id.b_plus);
        b = (TextView) view.findViewById(R.id.b);
        bMinus = (TextView) view.findViewById(R.id.b_minus);
        cPlus = (TextView) view.findViewById(R.id.c_plus);
        c = (TextView) view.findViewById(R.id.c);
        cMinus = (TextView) view.findViewById(R.id.c_minus);
        f = (TextView) view.findViewById(R.id.f);
        setClickListener();

        dialog.setView(view);
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        return dialog.create();
    }

    private void closeDialog()
    {
        this.dismiss();
    }

    private void setClickListener(){
        ClickHandler clickHandler = new ClickHandler();
        aPlus.setOnClickListener(clickHandler);
        bPlus.setOnClickListener(clickHandler);
        cPlus.setOnClickListener(clickHandler);
        a.setOnClickListener(clickHandler);
        b.setOnClickListener(clickHandler);
        c.setOnClickListener(clickHandler);
        aMinus.setOnClickListener(clickHandler);
        bMinus.setOnClickListener(clickHandler);
        cMinus.setOnClickListener(clickHandler);
        f.setOnClickListener(clickHandler);
    }


    public void setListGradeView(TextView listGradeView) {
        this.listGradeView = listGradeView;
    }

    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            TextView gradeView = (TextView) view;
            String grade = gradeView.getText().toString();
            Log.e("SelectedGrade",grade);

            if(CGPAAdapter.record.get(courseCode)==null)
            {
                Log.e("Msg : ","onItemSelected called for "+courseCode+" null");

                grade = ((TextView)view).getText().toString();
                CGPAAdapter.record.put(courseCode,grade);
                gradeView.setText(CGPAAdapter.record.get(courseCode).toString());
            }
            else
            {
                Log.e("Msg : ","onItemSelected called for "+courseCode+" not null "+CGPAAdapter.record.get(courseCode));

                grade = gradeView.getText().toString();
                CGPAAdapter.record.put(courseCode,grade);
            }
            closeDialog();
            listGradeView.setText(grade);
        }
    }
}