package shadattonmoy.sustnavigator.admin.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.model.Admin;
import shadattonmoy.sustnavigator.utils.Values;

/**
 * Created by Shadat Tonmoy on 10/2/2017.
 */

public class AdminAdapter extends ArrayAdapter<Admin>{
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RelativeLayout relativeLayout;
    private Context context;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FragmentActivity activity;
    private TextView notApprovedMsg;
    public AdminAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Admin> objects,RelativeLayout relativeLayout) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.relativeLayout = relativeLayout;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.admin_single_row,parent,false);
        }
        final Admin admin= getItem(position);
        TextView adminIcon = (TextView) row.findViewById(R.id.admin_icon);
        TextView adminName = (TextView) row.findViewById(R.id.admin_name);
        TextView adminDept = (TextView) row.findViewById(R.id.admin_dept);
        TextView adminRegNo = (TextView) row.findViewById(R.id.admin_regNo);
        TextView makeSuperAdmin = (TextView) row.findViewById(R.id.make_super_admin);
        TextView notApprovedMsg = (TextView) row.findViewById(R.id.not_varified_msg);

        ImageView approveIcon = (ImageView) row.findViewById(R.id.approve_admin);
        ImageView removeIcon = (ImageView) row.findViewById(R.id.remove_admin);


        String name = admin.getName();
        String dept = admin.getDept();
        String regNo = admin.getRegNo();
        final String id = admin.getId();
        final String email = admin.getEmail();
        final String password = admin.getPassword();
        String iconText = String.valueOf(name.charAt(0));
        if(Values.LOGGED_IN_ADMIN.isSuperAdmin())
        {
            if(!admin.isVarified())
            {
                Log.e("Admin",admin.toString()+" not Verified");
                if(approveIcon!=null)
                {
                    approveIcon.setImageResource(R.drawable.baseline_done_black_24);
                    approveIcon.setVisibility(View.VISIBLE);
                    approveIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            promptAdminApprove(admin,approveIcon);
                        }
                    });
                }
                if(notApprovedMsg!=null)
                {
                    notApprovedMsg.setVisibility(View.VISIBLE);
                    notApprovedMsg.setText("Not Approved Yet");
                }
                if(makeSuperAdmin!=null){
                    makeSuperAdmin.setVisibility(View.GONE);
                }

            }
            else
            {
                if(approveIcon!=null)
                    approveIcon.setVisibility(View.GONE);
                if(notApprovedMsg!=null)
                    notApprovedMsg.setVisibility(View.GONE);
                if(makeSuperAdmin!=null)
                    makeSuperAdmin.setVisibility(View.VISIBLE);
                if(!admin.getEmail().equals(Values.LOGGED_IN_ADMIN.getEmail()))
                {
                    makeSuperAdmin.setVisibility(View.VISIBLE);
                    makeSuperAdmin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            promptAdminApprove(admin,approveIcon);
                        }
                    });
                }

            }
            if(removeIcon!=null)
            {
                removeIcon.setImageResource(R.drawable.clear_black_24);
                removeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        promptAdminRemove(admin);
                    }
                });
            }
        }
        else
        {
            if(admin.getDept().toLowerCase().equals(Values.LOGGED_IN_ADMIN.getDept().toLowerCase()))
            {
                if(!admin.isVarified())
                {
                    if(approveIcon!=null)
                    {
                        approveIcon.setImageResource(R.drawable.baseline_done_black_24);
                        approveIcon.setVisibility(View.VISIBLE);
                    }
                    if(notApprovedMsg!=null)
                    {
                        notApprovedMsg.setVisibility(View.VISIBLE);
                        notApprovedMsg.setText("Not Approved Yet");
                    }
                    if(approveIcon!=null)
                    {
                        approveIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(context,"Approve Admin"+id,Toast.LENGTH_SHORT).show();
                                promptAdminApprove(admin,approveIcon);

                            }
                        });
                    }

                }
                else
                {
                    if(approveIcon!=null)
                        approveIcon.setVisibility(View.GONE);
                    if(notApprovedMsg!=null)
                        notApprovedMsg.setVisibility(View.GONE);
                }
                if(removeIcon!=null)
                {
                    removeIcon.setImageResource(R.drawable.clear_black_24);
                    removeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            promptAdminRemove(admin);
                        }
                    });
                }


            }
            else
            {
                if(removeIcon!=null)
                    removeIcon.setVisibility(View.GONE);
                if(approveIcon!=null)
                    approveIcon.setVisibility(View.GONE);
                if(notApprovedMsg!=null)
                    notApprovedMsg.setVisibility(View.GONE);
            }

        }



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

    private void promptAdminApprove(Admin admin,ImageView approveIcon)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setTitle("Sure to Approve?")
                .setMessage("Are you sure you want to approve "+admin.getName()+" as Admin ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        approveAdmin(admin.getId(),admin.getEmail(),admin.getPassword(),approveIcon,notApprovedMsg);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void promptAdminRemove(Admin admin)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setTitle("Sure to Delete?")
                .setMessage("Are you sure you want to remove "+admin.getName()+" From Admin ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        remove(admin);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    void approveAdmin(String id, final String email, final String password,ImageView approveIcon, TextView notAdminMsg)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("admin").child(id).child("varified");
        databaseReference.setValue(new Boolean(true)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                createAdmin(email,password,approveIcon,notAdminMsg);
            }
        });

    }

    void makeSuperAdmin(String id)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("admin").child(id).child("superAdmin");
        databaseReference.setValue(new Boolean(true)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context,"Super Admin Made",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    void createAdmin(String email,String password,ImageView approveIcon,TextView notApprovedMsg)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(relativeLayout,"Admin is approved",Snackbar.LENGTH_SHORT);
                snackbar.show();
                if(approveIcon!=null)
                    approveIcon.setVisibility(View.GONE);
                if(notApprovedMsg!=null)
                    notApprovedMsg.setVisibility(View.GONE);

            }
        });
    }
    void removeAdmin(final Admin admin)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("admin").child(admin.getId());
        databaseReference.setValue(null, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                remove(admin);
                Snackbar snackbar = Snackbar.make(relativeLayout,"Admin is removed",Snackbar.LENGTH_SHORT);
                snackbar.show();
                Values.updateLastModified();

            }
        });



    }
}
