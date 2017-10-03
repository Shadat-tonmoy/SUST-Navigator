package shadattonmoy.navigationdrawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static shadattonmoy.navigationdrawer.R.id.imageView;

/**
 * Created by Shadat Tonmoy on 10/2/2017.
 */

public class AdminAdapter extends ArrayAdapter<Admin>{
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RelativeLayout relativeLayout;
    private Context context;
    private ProgressBar progressBar;
    private ImageView approveIcon,removeIcon;
    private FirebaseAuth firebaseAuth;
    private TextView notApprovedMsg;
    public AdminAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Admin> objects,RelativeLayout relativeLayout) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.relativeLayout = relativeLayout;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.admin_single_row,parent,false);
        }
        Admin admin= getItem(position);
        TextView adminIcon = (TextView) row.findViewById(R.id.admin_icon);
        TextView adminName = (TextView) row.findViewById(R.id.admin_name);
        TextView adminDept = (TextView) row.findViewById(R.id.admin_dept);
        TextView adminRegNo = (TextView) row.findViewById(R.id.admin_regNo);
        notApprovedMsg = (TextView) row.findViewById(R.id.not_varified_msg);

        approveIcon = (ImageView) row.findViewById(R.id.approve_admin);
        removeIcon = (ImageView) row.findViewById(R.id.remove_admin);


        String name = admin.getName();
        String dept = admin.getDept();
        String regNo = admin.getRegNo();
        final String id = admin.getId();
        final String email = admin.getEmail();
        final String password = admin.getPassword();
        String iconText = String.valueOf(name.charAt(0));


        if(!admin.isVarified())
        {
            approveIcon.setImageResource(R.drawable.admin_approve_icon);
            approveIcon.setVisibility(View.VISIBLE);
            notApprovedMsg.setText("Not Approved Yet");
            approveIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Approve Admin"+id,Toast.LENGTH_SHORT).show();
                    approveAdmin(id,email,password);
                }
            });
        }
        else
        {
            approveIcon.setVisibility(View.GONE);
            notApprovedMsg.setVisibility(View.GONE);
        }

        removeIcon.setImageResource(R.drawable.admin_remove);



        if(iconText.equals("A") || iconText.equals("E") || iconText.equals("I") || iconText.equals("O"))
            adminIcon.setBackgroundResource(R.drawable.round_yellow);
        else if(iconText.equals("B") || iconText.equals("D") || iconText.equals("F") || iconText.equals("H"))
            adminIcon.setBackgroundResource(R.drawable.round_carrot);
        else if(iconText.equals("B") || iconText.equals("D") || iconText.equals("F") || iconText.equals("H"))
            adminIcon.setBackgroundResource(R.drawable.round_black);
        else if(iconText.equals("C") || iconText.equals("G") || iconText.equals("J") || iconText.equals("K"))
            adminIcon.setBackgroundResource(R.drawable.round_blue);
        else if(iconText.equals("L") || iconText.equals("P") || iconText.equals("S") || iconText.equals("Y"))
            adminIcon.setBackgroundResource(R.drawable.round_green2);
        else if(iconText.equals("M") || iconText.equals("N") || iconText.equals("S") || iconText.equals("Z"))
            adminIcon.setBackgroundResource(R.drawable.round_green3);


        adminIcon.setText(iconText);
        adminName.setText(name);
        adminDept.setText(dept);
        adminRegNo.setText(regNo);
        return row;
    }
    void approveAdmin(String id, final String email, final String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("admin").child(id).child("varified");
        databaseReference.setValue(new Boolean(true)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                createAdmin(email,password);
            }
        });

    }

    void createAdmin(String email,String password)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(relativeLayout,"Admin is approved",Snackbar.LENGTH_SHORT);
                snackbar.show();
                approveIcon.setVisibility(View.GONE);
                notApprovedMsg.setVisibility(View.GONE);

            }
        });
    }
    void disableAdmin()
    {

    }
}
