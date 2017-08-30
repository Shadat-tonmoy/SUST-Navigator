package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ThrowOnExtraProperties;


public class SyllabusFragment extends android.app.Fragment {

    private View view;
    private String dept,semester;
    private FloatingActionButton floatingActionButton;
    public SyllabusFragment() {
        super();
    }
    public SyllabusFragment(String dept,String semester)
    {
        this.dept = dept;
        this.semester = semester;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_syllabus2, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"Add for "+dept+" in "+semester,Toast.LENGTH_SHORT).show();
                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                SyllabusAddFragment syllabusAddFragment = new SyllabusAddFragment(dept,semester);
                transaction.replace(R.id.main_content_root,syllabusAddFragment);
                transaction.addToBackStack("syllabus_add_fragment");
                transaction.commit();
            }
        });
    }
}
