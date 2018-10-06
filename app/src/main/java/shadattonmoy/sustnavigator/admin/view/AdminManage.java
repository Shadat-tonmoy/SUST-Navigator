package shadattonmoy.sustnavigator.admin.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.controller.AdminAdapter;
import shadattonmoy.sustnavigator.admin.model.Admin;


public class AdminManage extends android.app.Fragment {
    private ListView adminList;
    private ArrayList<Admin> adminArray;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView debugView;
    private ProgressBar adminLoading;
    private RelativeLayout relativeLayout;
    private View view;
    private AppBarLayout appBarLayout;

    public AdminManage() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_manage, container, false);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        fetchData();
    }

    void fetchData()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("admin");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String txt = "";
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    Admin admin  = child.getValue(Admin.class);
                    admin.setId(child.getKey());
                    adminArray.add(admin);
                }
                AdminAdapter adminAdapter = new AdminAdapter(getActivity().getApplicationContext(),R.layout.admin_single_row,R.id.admin_icon,adminArray,relativeLayout);
                adminAdapter.setProgressBar(adminLoading);
                adminList.setAdapter(adminAdapter);
                adminLoading.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void init()
    {

        appBarLayout.setExpanded(false);
        adminArray =  new ArrayList<Admin>();
        adminList = (ListView) view.findViewById(R.id.admin_list);
        adminLoading = (ProgressBar) view.findViewById(R.id.admin_loading);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.admin_fragment);
    }
}
