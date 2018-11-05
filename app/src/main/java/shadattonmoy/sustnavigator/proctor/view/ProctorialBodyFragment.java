package shadattonmoy.sustnavigator.proctor.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.commons.view.AdminListBottomSheet;
import shadattonmoy.sustnavigator.proctor.controller.ProctorAdapter;
import shadattonmoy.sustnavigator.admin.view.ProctorAddFragment;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.proctor.model.Proctor;
import shadattonmoy.sustnavigator.utils.Values;


public class ProctorialBodyFragment extends android.app.Fragment {

    private ListView proctorList;
    private View view;
    private boolean isEditable;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Proctor> proctors;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    public static ProctorAdapter adapter;
    private ImageView nothingFoundImage;
    private TextView nothingFoundText;
    private Context context;
    private AppBarLayout appBarLayout;
    private FragmentActivity activity;

    public ProctorialBodyFragment(boolean isEditable) {
        super();
        this.isEditable = isEditable;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_proctorial_body, container, false);
        proctorList = (ListView) view.findViewById(R.id.proctor_list);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.proctor_add_fab);
        progressBar = (ProgressBar) view.findViewById(R.id.proctor_loading);
        nothingFoundImage = (ImageView) view.findViewById(R.id.nothing_found_image);
        nothingFoundText = (TextView) view.findViewById(R.id.nothing_found_txt);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar_layout);
        setHasOptionsMenu(true);
        context = getActivity();
        activity = (FragmentActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        appBarLayout.setExpanded(false);
        getProctorListFromServer();


    }

    public void getProctorListFromServer() {
        proctors = new ArrayList<Proctor>();
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("proctor");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    Proctor proctor = child.getValue(Proctor.class);
                    proctor.setProctorId(key);
                    proctors.add(proctor);
                }
                if (proctors.size() > 0) {
//                    Log.e("Editabel", isEditable + " ");
                    adapter = new ProctorAdapter(context, R.layout.teacher_single_row, R.id.teacher_icon, proctors, isEditable, getFragmentManager());
                    adapter.setActivity(getActivity());
                    adapter.setView(view);
                    proctorList.setAdapter(adapter);
                } else {

                    nothingFoundImage.setVisibility(View.VISIBLE);
                    nothingFoundText.setVisibility(View.VISIBLE);
                    if (isEditable)
                        nothingFoundText.setText(Html.fromHtml("Sorry! No Records found for Proctorial Body. <b>Tap '+' to Add</b>"));
                    else {
                        nothingFoundText.setText(Html.fromHtml("Sorry! No Records found for Proctorial Body. <b>Please Contact Admin</b>"));
                        nothingFoundText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestAdmin();
                            }
                        });
                    }
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
                if (isEditable) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            ProctorAddFragment proctorAddFragment = new ProctorAddFragment(false);
                            transaction.replace(R.id.main_content_root, proctorAddFragment);
                            transaction.addToBackStack("proctor_add_fragment");
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

    private void requestAdmin() {
        try {

            AdminListBottomSheet adminListBottomSheet = new AdminListBottomSheet();
            Bundle args = new Bundle();
            args.putInt("purpose", Values.CONTACT_FOR_PROCTOR);
            adminListBottomSheet.setArguments(args);
            if (activity == null)
                activity = (FragmentActivity) getActivity();
            adminListBottomSheet.show(activity.getSupportFragmentManager(), "adminList");

        } catch (Exception e) {
            Values.showToast(context, "Sorry!! An error occurred");
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_request_menu, menu);
        MenuItem item = menu.findItem(R.id.request_admin_menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.request_admin_menu:
                requestAdmin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
