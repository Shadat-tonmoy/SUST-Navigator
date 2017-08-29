package shadattonmoy.navigationdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/**
 * Created by Shadat Tonmoy on 8/27/2017.
 */

public class FacultyAddConfirmationDialog extends DialogFragment {
    private String warningMsg;
    private String dept;
    private Teacher teacher;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private View view,viewForSnackbar;
    private Context context;
    private FragmentManager fragmentManager;
    public FacultyAddConfirmationDialog(Context context,String warningMsg,String dept, Teacher teacher, View view,FragmentManager manager){
        this.warningMsg = warningMsg;
        this.dept = dept;
        this.teacher = teacher;
        this.context = context;
        this.viewForSnackbar = view;
        this.fragmentManager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.confirmation_dialog,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.confirmation_dialog_image);
        TextView textView = (TextView) view.findViewById(R.id.confirmation_dialog_msg);
        if(warningMsg.equals("OK"))
        {
            textView.setText("Are you sure to add this Faculty Member?");
            textView.setBackgroundColor(getResources().getColor(R.color.warningGreen));
        }
        else {
            textView.setText(warningMsg);
            textView.setBackgroundColor(getResources().getColor(R.color.warningRed));
        }
        builder.setView(view);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Negative was clicked",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Positive was clicked",Toast.LENGTH_SHORT).show();
                addFaculty();
            }
        });
        return builder.create();
    }

    public void addFaculty()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.toLowerCase());
        databaseReference.push().setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try{

                    Snackbar snackbar = Snackbar.make(viewForSnackbar,"Faculty Added",Snackbar.LENGTH_INDEFINITE).setAction("Back", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragmentManager.popBackStack();

                        }
                    });

                    snackbar.show();

                } catch (Exception e){
                    //textView.setText(e.getMessage().toString());


                }
            }
        });
    }
}
