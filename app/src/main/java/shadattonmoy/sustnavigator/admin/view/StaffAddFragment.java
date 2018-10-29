package shadattonmoy.sustnavigator.admin.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.Staff;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.proctor.model.Proctor;
import shadattonmoy.sustnavigator.utils.Values;


public class StaffAddFragment extends android.app.Fragment implements View.OnClickListener {

    private EditText nameField, contactNoField, roomNoField,designationField;
    private View view;
    private Button submitButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Staff staff;
    private boolean isUpdating;
    private AwesomeValidation awesomeValidation;
    private Context context;
    private Activity activity;
    private Dept dept;

    public StaffAddFragment() {
        // Required empty public constructor
    }

    public StaffAddFragment(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_add, container, false);
        nameField = view.findViewById(R.id.staff_add_name_field);
        contactNoField = view.findViewById(R.id.staff_add_contact_no_field);
        roomNoField = view.findViewById(R.id.staff_add_room_no_field);
        designationField = view.findViewById(R.id.staff_add_designation_field);
        submitButton = view.findViewById(R.id.staff_add_submit_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.staff_add_name_field, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_error);
        if (isUpdating)
        {
            nameField.setText(staff.getName());
            roomNoField.setText(staff.getRoomNo());
            contactNoField.setText(staff.getPhoneNo());
        }
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.staff_add_submit_btn) {

            if (awesomeValidation.validate()) {
                final String name = nameField.getText().toString();
                String contactNo = contactNoField.getText().toString();
                String designation = designationField.getText().toString();
                String roomNo = roomNoField.getText().toString();
                String warning_msg = "";
                if (contactNo.trim().equals("") || roomNo.trim().equals("")) {
                    warning_msg = "Please notice you have not added ";
                    if (contactNo.trim().equals("")) {
                        warning_msg += "Phone";
                        contactNo = "N/A";
                        if (roomNo.trim().equals(""))
                            warning_msg += ",";
                    }if (designation.trim().equals("")) {
                        warning_msg += "Designation,";
                        designation = "N/A";
                    }
                    if (roomNo.trim().equals("")) {
                        warning_msg += " Room No ";
                        roomNo = "N/A";
                    }
                    warning_msg += ". Do You want to continue?";
                } else warning_msg = "Are you sure to add these information to Staff Record?";

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please Notice");
                builder.setMessage(warning_msg);
                final String finalRoomNo = roomNo;
                final String finalContactNo = contactNo;
                final String finalDesignation= designation;
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isUpdating) {
                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setTitle("Updating Record");
                            progressDialog.setMessage("Please Wait....");
                            progressDialog.show();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference().child("staff").child(dept.getDeptCode().toLowerCase()).child(staff.getId());
                            databaseReference.setValue(new Staff(name, finalDesignation, finalRoomNo, finalContactNo), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    progressDialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(view, "Updated successfully...", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("Back", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getFragmentManager().popBackStack();
                                        }
                                    }).setActionTextColor(context.getResources().getColor(R.color.blue));
                                    snackbar.show();
                                    Values.updateLastModified();
                                }
                            });

                        } else {
                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setTitle("Adding Record");
                            progressDialog.setMessage("Please Wait....");
                            progressDialog.show();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference().child("staff").child(dept.getDeptCode().toLowerCase());
                            databaseReference.push().setValue(new Staff(name, finalDesignation, finalRoomNo, finalContactNo), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    progressDialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(view, "Staff Record added...", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("Back", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getFragmentManager().popBackStack();
                                        }
                                    });
                                    snackbar.show();
                                    Values.updateLastModified();

                                }
                            });
                            dialog.dismiss();
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
            }

        }
    }
}
