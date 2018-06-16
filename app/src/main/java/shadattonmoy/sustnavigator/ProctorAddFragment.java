package shadattonmoy.sustnavigator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import shadattonmoy.sustnavigator.proctor.model.Proctor;


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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.designation_for_proctor, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designationField.setAdapter(adapter);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
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
            if (!isUpdating) {
                if (awesomeValidation.validate()) {
                    final String name = nameField.getText().toString();
                    String contactNo = contactNoField.getText().toString();
                    String roomNo = roomNoField.getText().toString();
                    String designation = designationField.getSelectedItem().toString();
                    if (designation.equals("Choose a Designation"))
                        designation = "N/A";
                    if (roomNo.equals(""))
                        roomNo = "Room : N/A";
                    String warning_msg = "";
                    if (contactNo.trim().equals("") || roomNo.trim().equals("")) {
                        warning_msg = "Please notice you have not added ";
                        if (contactNo.trim().equals("")) {
                            warning_msg += "Phone ";
                            contactNo = "N/A";
                            if (roomNo.trim().equals(""))
                                warning_msg += ",";
                        }
                        if (roomNo.trim().equals("")) {
                            warning_msg += "Room No ";
                            roomNo = "N/A";
                        }
                        warning_msg += ". Do You want to continue?";
                    } else warning_msg = "OK";

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Please Notice");
                    builder.setMessage(warning_msg);
                    final String finalDesignation = designation;
                    final String finalRoomNo = roomNo;
                    final String finalContactNo = contactNo;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference().child("proctor");
                            databaseReference.push().setValue(new Proctor(name, finalDesignation, finalRoomNo, finalContactNo), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    Snackbar snackbar = Snackbar.make(view, "Proctorial Body Member added...", Snackbar.LENGTH_SHORT);
                                    snackbar.setAction("Back", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getFragmentManager().popBackStack();
                                        }
                                    });
                                    snackbar.show();

                                }
                            });
                            dialog.dismiss();
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


            } else {
                String name = nameField.getText().toString();
                String contactNo = contactNoField.getText().toString();
                String roomNo = roomNoField.getText().toString();
                String designation = designationField.getSelectedItem().toString();
                if (designation.equals("Choose a Designation"))
                    designation = "N/A";
                if (roomNo.equals(""))
                    roomNo = "Room : N/A";
                Toast.makeText(getActivity().getApplicationContext(), name + " " + contactNo + " " + roomNo + " " + designation, Toast.LENGTH_SHORT).show();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("proctor").child(proctor.getProctorId());
                databaseReference.setValue(new Proctor(name, designation, roomNo, contactNo), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Snackbar snackbar = Snackbar.make(view, "Updated successfully...", Snackbar.LENGTH_SHORT);
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
}
