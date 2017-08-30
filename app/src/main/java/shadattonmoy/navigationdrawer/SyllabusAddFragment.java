package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SyllabusAddFragment extends android.app.Fragment {
    private String dept,semester;


    public SyllabusAddFragment() {

    }
    public SyllabusAddFragment(String dept,String semester)
    {
        this.dept = dept;
        this.semester = semester;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_syllabus_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
