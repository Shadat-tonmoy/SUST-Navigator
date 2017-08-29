package shadattonmoy.navigationdrawer;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class CGPAShowFragment extends android.app.Fragment implements View.OnClickListener {

    private TextView cgpaFinalView,cgpaViewBackButton;
    private String finalCGPA;
    private FragmentManager manager;
    public CGPAShowFragment(String finalCGPA,FragmentManager manager) {
        this.finalCGPA = finalCGPA;
        this.manager = manager;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cgpashow, container, false);
        cgpaFinalView = (TextView)view.findViewById(R.id.final_cgpa_view);
        cgpaViewBackButton = (TextView) view.findViewById(R.id.cgpa_view_back_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cgpaFinalView.setText(finalCGPA);
        float finalCGPAVal = Float.parseFloat(finalCGPA);
        if(finalCGPAVal<(float)3.00)
        {
            cgpaFinalView.setBackgroundResource(R.drawable.round_red);
        }
        else if(finalCGPAVal<(float)3.50)
        {
            cgpaFinalView.setBackgroundResource(R.drawable.round_yellow);
        }

        //cgpaFinalView.setBackground(R.drawable.round_red);

        //manager = getActivity().getFragmentManager();
        cgpaViewBackButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==cgpaViewBackButton.getId())
        {
            manager.popBackStack();
            //Toast.makeText(getActivity().getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
        }


    }
}
