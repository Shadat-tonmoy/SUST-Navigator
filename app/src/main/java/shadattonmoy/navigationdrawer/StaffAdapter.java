package shadattonmoy.navigationdrawer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Shadat Tonmoy on 10/1/2017.
 */

public class StaffAdapter extends ArrayAdapter<Staff> {
    private Context context;
    private ImageView imageView;
    private boolean isEditable;
    private Activity activity;
    private FragmentManager manager;
    public StaffAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Staff> objects,boolean isEditable) {
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
        final Staff staff = getItem(position);
        TextView teacherIcon = (TextView) row.findViewById(R.id.teacher_icon);
        ImageView contactImage = (ImageView) row.findViewById(R.id.contact_teacher);
        TextView teacherName = (TextView) row.findViewById(R.id.teacher_name);
        TextView teacherDesignation = (TextView) row.findViewById(R.id.teacher_designation);
        TextView teacherRoom = (TextView) row.findViewById(R.id.teacher_room);
        imageView = (ImageView) row.findViewById(R.id.contact_teacher);
        String name = staff.getName();
        String designation = staff.getDesignation();
        String room = staff.getRoomNo();
        final String phone = staff.getPhoneNo();
        final String proctorId = staff.getId();
        String iconText = String.valueOf(name.charAt(0));
        if(isEditable)
        {
//            imageView.setImageResource(R.drawable.edit_icon);
//            final PopupMenu popupMenu = new PopupMenu(getContext(),imageView,Gravity.LEFT);
//            popupMenu.inflate(R.menu.proctor_manage_menu);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupMenu.show();
//                    popupMenu.setOnMenuItemClickListener(new clickHandlerProctor(getContext(),proctor,manager));
//                }
//            });

        }
        else {
            imageView.setImageResource(R.drawable.phone_final);
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
