package shadattonmoy.sustnavigator.admin.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.view.TeacherAddFragment;
import shadattonmoy.sustnavigator.teacher.model.Teacher;
import shadattonmoy.sustnavigator.utils.Values;

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
    private boolean isUpdate;
    private String facultyIdToUpdate;
    public FacultyAddConfirmationDialog(Context context,String warningMsg,String dept, Teacher teacher, View view,FragmentManager manager,boolean isUpdate){
        this.warningMsg = warningMsg;
        this.dept = dept;
        this.teacher = teacher;
        this.context = context;
        this.viewForSnackbar = view;
        this.fragmentManager = manager;
        this.isUpdate = isUpdate;
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
            if(isUpdate)
                textView.setText("Are You Sure to Update Record of Faculty Member?");
            else
                textView.setText("Are You Sure to Add This Faculty Member?");
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
                dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isUpdate)
                    updateFaculty(facultyIdToUpdate);
                else addFaculty();
            }
        });
        return builder.create();
    }

    public void addFaculty()
    {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Adding Record");
        dialog.setMessage("Please Wait....");
        dialog.show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.toLowerCase());
        databaseReference.push().setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try{
                    dialog.dismiss();
                    Snackbar snackbar = Snackbar.make(viewForSnackbar,"Faculty Added",Snackbar.LENGTH_INDEFINITE).setAction("Back", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragmentManager.popBackStack();

                        }
                    }).setAction("Add New Record", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TeacherAddFragment.reset();
                        }
                    }).setActionTextColor(context.getResources().getColor(R.color.blue));

                    snackbar.show();
                    Values.updateLastModified();

                } catch (Exception e){

                }
            }
        });
    }

    public void updateFaculty(String facultyId)
    {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating Record");
        dialog.setMessage("Please Wait....");
        dialog.show();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.toLowerCase()).child(facultyId);
        databaseReference.setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try{
                    dialog.dismiss();
                    Snackbar snackbar = Snackbar.make(viewForSnackbar,"Record Updated",Snackbar.LENGTH_INDEFINITE).setAction("Back", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragmentManager.popBackStack();

                        }
                    }).setActionTextColor(context.getResources().getColor(R.color.blue));

                    snackbar.show();
                    Values.updateLastModified();

                } catch (Exception e){

                }
            }
        });
    }

    public void setFacultyIdToUpdate(String facultyIdToUpdate) {
        this.facultyIdToUpdate = facultyIdToUpdate;
    }
}
