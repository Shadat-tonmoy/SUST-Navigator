package shadattonmoy.sustnavigator.proctor.controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import shadattonmoy.sustnavigator.admin.view.ProctorAddFragment;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.proctor.model.Proctor;
import shadattonmoy.sustnavigator.proctor.view.ProctorialBodyFragment;

/**
 * Created by Shadat Tonmoy on 9/8/2017.
 */

public class ProctorAdapter extends ArrayAdapter<Proctor> {

    private Context context;
    private ImageView imageView;
    private boolean isEditable;
    private Activity activity;
    private FragmentManager manager;
    private View view;

    public ProctorAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Proctor> objects,boolean isEditable,FragmentManager manager) {
        super(context, resource, textViewResourceId, objects);
        this.isEditable = isEditable;
        this.context = context;
        this.manager = manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.teacher_single_row,parent,false);
        }
        final Proctor proctor = getItem(position);
        TextView teacherIcon = (TextView) row.findViewById(R.id.teacher_icon);
        ImageView contactImage = (ImageView) row.findViewById(R.id.contact_teacher);
        TextView teacherName = (TextView) row.findViewById(R.id.teacher_name);
        TextView teacherDesignation = (TextView) row.findViewById(R.id.teacher_designation);
        TextView teacherRoom = (TextView) row.findViewById(R.id.teacher_room);
        imageView = (ImageView) row.findViewById(R.id.contact_teacher);
        String name = proctor.getName();
        String designation = proctor.getDesignation();
        String room = proctor.getRoomNo();
        final String phone = proctor.getContactNo();
        final String proctorId = proctor.getProctorId();
        String iconText = String.valueOf(name.charAt(0));
        if(isEditable)
        {
            imageView.setBackgroundResource(R.drawable.more_vert_black);
            final PopupMenu popupMenu = new PopupMenu(getContext(),imageView,Gravity.LEFT);
            popupMenu.inflate(R.menu.proctor_manage_menu);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new clickHandlerProctor(getContext(),proctor,manager,activity,view));
                }
            });

        }
        else {
            imageView.setBackgroundResource(R.drawable.phone_black_24);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeCall(phone);
                    //Toast.makeText(context,"Call to"+phone,Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(iconText.equals("A") || iconText.equals("E") || iconText.equals("I") || iconText.equals("O"))
            teacherIcon.setBackgroundResource(R.drawable.round_yellow);
        else if(iconText.equals("B") || iconText.equals("D") || iconText.equals("F") || iconText.equals("H"))
            teacherIcon.setBackgroundResource(R.drawable.round_carrot);
        else if(iconText.equals("B") || iconText.equals("D") || iconText.equals("F") || iconText.equals("H"))
            teacherIcon.setBackgroundResource(R.drawable.round_black);
        else if(iconText.equals("C") || iconText.equals("G") || iconText.equals("J") || iconText.equals("K"))
            teacherIcon.setBackgroundResource(R.drawable.round_blue);
        else if(iconText.equals("L") || iconText.equals("P") || iconText.equals("S") || iconText.equals("Y"))
            teacherIcon.setBackgroundResource(R.drawable.round_green2);
        else if(iconText.equals("M") || iconText.equals("N") || iconText.equals("S") || iconText.equals("Z"))
            teacherIcon.setBackgroundResource(R.drawable.round_green3);


        teacherIcon.setText(iconText);
        teacherName.setText(name);
        teacherDesignation.setText(designation);
        teacherRoom.setText(room);



        return row;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void makeCall(String phoneNo)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNo));
        if(intent.resolveActivity(context.getPackageManager())!=null)
        {
            //context.startActivity(intent);
            activity.startActivity(intent);
        }
    }
}
class clickHandlerProctor implements PopupMenu.OnMenuItemClickListener{

    private Context context;
    private Proctor proctor;
    private FragmentManager manager;
    private Activity activity;
    private View view;

    public clickHandlerProctor(Context context,Proctor proctor,FragmentManager manager,Activity activity,View view)
    {
        this.context = context;
        this.proctor = proctor;
        this.manager = manager;
        this.activity = activity;
        this.view = view;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.edit_proctor_menu)
        {
            ProctorAddFragment proctorAddFragment = new ProctorAddFragment(true);
            proctorAddFragment.setProctor(proctor);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_root,proctorAddFragment);
            transaction.addToBackStack("proctor_edit_fragment");
            transaction.commit();
            return true;
        }
        else if ( id == R.id.remove_proctor_menu)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Please Notice");
            builder.setMessage("Are you sure to permanently remove this Proctor Information From Record? Once you delete you will not be able to restore again.");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("admin").child(proctor.getProctorId());
                    dialog.dismiss();
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Deleting Record");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            progressDialog.dismiss();;
                            ProctorialBodyFragment.adapter.remove(proctor);
                            Snackbar snackbar = Snackbar.make(view, "Proctorial Body Member added...", Snackbar.LENGTH_SHORT);
                            snackbar.setAction("Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    manager.popBackStack();
                                }
                            }).setActionTextColor(context.getResources().getColor(R.color.blue));
                            snackbar.show();


                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return true;
        }
        return false;
    }
}
