package shadattonmoy.sustnavigator.admin.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.utils.Values;


public class AdminPanelFragment extends android.app.Fragment {
    private Context context;
    private static TextView adminReqTextView;
    private Toolbar toolbar;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AdminPanelFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_panel, container, false);
        adminReqTextView = (TextView) view.findViewById(R.id.admin_request_msg);
        adminReqTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminManageFragment();
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Values.getAdminRequest(context);
    }

    public static void setAdminReqMessage(String msg)
    {
        if(adminReqTextView!=null)
        {
            adminReqTextView.setVisibility(View.VISIBLE);
            adminReqTextView.setText(msg);

        }
    }

    public void openAdminManageFragment()
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AdminManage adminManage= new AdminManage();
        transaction.replace(R.id.main_content_root,adminManage);
        transaction.addToBackStack("admin_manage_fragment");
        transaction.commit();

    }
}
