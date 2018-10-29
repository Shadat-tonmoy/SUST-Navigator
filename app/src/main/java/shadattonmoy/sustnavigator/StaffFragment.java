package shadattonmoy.sustnavigator;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.admin.view.ProctorAddFragment;
import shadattonmoy.sustnavigator.admin.view.StaffAddFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;


public class StaffFragment extends android.app.Fragment {
    private Dept dept;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Staff> staffArray;
    public static StaffAdapter adapter;
    private ListView staffList;
    private TextView fragmentHeader, nothingFoundText;
    private ImageView nothingFoundImage;
    private ProgressBar progressBar;
    private Context context;
    private boolean isEditable = false;
    private FloatingActionButton addFab;
    private View view;
    public StaffFragment() {

    }
    public StaffFragment(Dept dept){
        this.dept = dept;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_staff, container, false);
        staffList =  view.findViewById(R.id.staff_list);
        progressBar =  view.findViewById(R.id.staff_loading);
        fragmentHeader =  view.findViewById(R.id.staff_fragment_title);
        nothingFoundText =  view.findViewById(R.id.nothing_found_txt);
        nothingFoundImage =  view.findViewById(R.id.nothing_found_image);
        addFab = view.findViewById(R.id.staff_add_fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentHeader.setText("Staff of "+dept.getDeptTitle());
        getStaffFromServer();



    }

    public void getStaffFromServer()
    {
        int i=0;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("staff").child(dept.getDeptCode().toLowerCase());
        Log.e("CheckingAt",dept.getDeptCode().toLowerCase());
        staffArray = new ArrayList<Staff>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    Staff staff = child.getValue(Staff.class);
                    staff.setId(child.getKey());
                    staffArray.add(staff);
                }
                if(staffArray.size()>0)
                {
                    adapter = new StaffAdapter(getActivity().getApplicationContext(),R.layout.teacher_single_row,R.id.teacher_icon,staffArray,isEditable);
                    adapter.setActivity(getActivity());
                    adapter.setDept(dept);
                    adapter.setView(view);
                    adapter.setManager(getFragmentManager());
                    staffList.setAdapter(adapter);
                } else {
                    setHasOptionsMenu(false);
                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    if(isEditable)
                        nothingFoundText.setText("Sorry!! No Records found for " + dept.getDeptTitle() + " Tap the '+' Button to add");
                    else
                        nothingFoundText.setText("Sorry!! No Records found for " + dept.getDeptTitle() + " Please Contact Admin");
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
                if(isEditable)
                {
                    addFab.setVisibility(View.VISIBLE);
                    addFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            StaffAddFragment staffAddFragment= new StaffAddFragment(false);
                            staffAddFragment.setDept(dept);
                            transaction.replace(R.id.main_content_root,staffAddFragment);
                            transaction.addToBackStack("staff_add_fragment");
                            transaction.commit();
                        }
                    });
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
