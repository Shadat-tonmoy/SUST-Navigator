package shadattonmoy.sustnavigator.admin.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.proctor.model.Proctor;
import shadattonmoy.sustnavigator.utils.Values;


public class SemesterAddFragment extends android.app.Fragment {
    private View rootView;
    private TextView addMoreSemesterButton;
    private Spinner semesterSpinner;
    private LinearLayout semesterSpinnerContainer;
    private Context context;
    private Button semesterAddSubmitButton;
    private Map<Integer,String > selectedSemesterMap;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String session,dept;


    public SemesterAddFragment() {
        // Required empty public constructor
    }

    public SemesterAddFragment(String session, String dept) {
        this.session = session;
        this.dept = dept;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_semester_add, container, false);
        addMoreSemesterButton = (TextView) rootView.findViewById(R.id.add_more_semester_button);
        semesterSpinner = (Spinner) rootView.findViewById(R.id.semester_spinner);
        semesterSpinnerContainer = (LinearLayout) rootView.findViewById(R.id.semester_spinner_container);
        semesterAddSubmitButton = (Button) rootView.findViewById(R.id.semester_add_submit_btn);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context =  getActivity().getApplicationContext();
        selectedSemesterMap = new HashMap<>();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.semester_list, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(adapter);
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedSemester = adapterView.getItemAtPosition(i).toString();
                Log.e("Selected",i+" "+adapterView.getItemAtPosition(i).toString());
                selectedSemesterMap.put(i,selectedSemester);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addMoreSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View semesterSpinnerLayout = inflater.inflate(R.layout.semester_title_single_row,semesterSpinnerContainer,false);
                Spinner semesterSpinner = (Spinner) semesterSpinnerLayout.findViewById(R.id.semester_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.semester_list, R.layout.spinner_layout);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                semesterSpinner.setAdapter(adapter);
                semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedSemester = adapterView.getItemAtPosition(i).toString();
                        Log.e("Selected",i+" "+adapterView.getItemAtPosition(i).toString());
                        selectedSemesterMap.put(i,selectedSemester);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                semesterSpinnerContainer.addView(semesterSpinnerLayout);
            }
        });

        semesterAddSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Adding Record");
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();
                firebaseDatabase = FirebaseDatabase.getInstance();
                for (Map.Entry<Integer, String> entry : selectedSemesterMap.entrySet())
                {
                    int key = entry.getKey();
                    String value = entry.getValue();
                    String semesterCode = (String) Values.getSemesterCodeMap().get(value);
                    if (semesterCode!=null)
                    {
                        databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept).child(semesterCode);
                        Log.e("SemesterToAdd",semesterCode);
                        databaseReference.setValue("", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Snackbar snackbar = Snackbar.make(rootView, "Semester Added", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setActionTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
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
                    }
                }
                progressDialog.dismiss();
                /*for(int i=0;i<totalSemester;i++)
                {
                    View semesterRow = semesterSpinnerContainer.getChildAt(i);
                    Spinner semesterSpinner = (Spinner) semesterRow.findViewById(R.id.semester_spinner);
                    Log.e("SelectedSemester",i+" "+semesterRow.toString());*//*semesterSpinner.getSelectedItem().toString())*//*;

                }*/


            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
