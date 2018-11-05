package shadattonmoy.sustnavigator.admin.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.proctor.model.Proctor;
import shadattonmoy.sustnavigator.utils.Values;


public class ProctorAddFragment extends android.app.Fragment implements View.OnClickListener {

    private EditText nameField, contactNoField, roomNoField;
    private Spinner designationField;
    private View view;
    private Button submitButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Proctor proctor;
    private boolean isUpdating;
    private AwesomeValidation awesomeValidation;
    private Context context;
    private Activity activity;

    public ProctorAddFragment() {
        // Required empty public constructor
    }

    public ProctorAddFragment(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public Proctor getProctor() {
        return proctor;
    }

    public void setProctor(Proctor proctor) {
        this.proctor = proctor;
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
        view = inflater.inflate(R.layout.fragment_proctor_add, container, false);
        nameField = (EditText) view.findViewById(R.id.proctor_add_name_field);
        contactNoField = (EditText) view.findViewById(R.id.proctor_add_contact_no_field);
        roomNoField = (EditText) view.findViewById(R.id.proctor_add_room_no_field);
        designationField = (Spinner) view.findViewById(R.id.proctor_add_designation_field);
        submitButton = (Button) view.findViewById(R.id.proctor_add_submit_btn);
        context = getActivity();
        activity = (FragmentActivity) getActivity();
        return view;
    }

    private void resetFields()
    {
        nameField.setText("");
        contactNoField.setText("");
        roomNoField.setText("");
        designationField.setSelection(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.designation_for_proctor, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designationField.setAdapter(adapter);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.proctor_add_name_field, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_error);
        if (!isUpdating)
            designationField.setSelection(0);
        else {
            nameField.setText(proctor.getName());
            roomNoField.setText(proctor.getRoomNo());
            contactNoField.setText(proctor.getContactNo());

        }
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.proctor_add_submit_btn) {

            if (awesomeValidation.validate()) {
                final String name = nameField.getText().toString();
                String contactNo = contactNoField.getText().toString();
                String roomNo = roomNoField.getText().toString();
                String designation = designationField.getSelectedItem().toString();
                if (designation.equals("Choose a Designation"))
                    designation = "N/A";
                String warning_msg = "";
                if (contactNo.trim().equals("") || roomNo.trim().equals("")) {
                    warning_msg = "Please notice you have not added ";
                    if (contactNo.trim().equals("")) {
                        warning_msg += "Phone";
                        contactNo = "N/A";
                        if (roomNo.trim().equals(""))
                            warning_msg += ",";
                    }
                    if (roomNo.trim().equals("")) {
                        warning_msg += " Room No ";
                        roomNo = "N/A";
                    }
                    if (designation.trim().equals("N/A")) {
                        warning_msg += ", Designation ";
                        roomNo = "N/A";
                    }
                    warning_msg += ". Do You want to continue?";
                } else warning_msg = "Are you sure to add these information to Proctor Record?";

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please Notice");
                builder.setMessage(warning_msg);
                final String finalDesignation = designation;
                final String finalRoomNo = roomNo;
                final String finalContactNo = contactNo;
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isUpdating) {
                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setTitle("Updating Record");
                            progressDialog.setMessage("Please Wait....");
                            progressDialog.show();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference().child("proctor").child(proctor.getProctorId());
                            databaseReference.setValue(new Proctor(name, finalDesignation, finalRoomNo, finalContactNo), new DatabaseReference.CompletionListener() {
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
                            databaseReference = firebaseDatabase.getReference().child("proctor");
                            databaseReference.push().setValue(new Proctor(name, finalDesignation, finalRoomNo, finalContactNo), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    progressDialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(view, "Proctorial Body Member added...", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("Add New", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            resetFields();
                                        }
                                    }).setActionTextColor(context.getResources().getColor(R.color.blue));
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
