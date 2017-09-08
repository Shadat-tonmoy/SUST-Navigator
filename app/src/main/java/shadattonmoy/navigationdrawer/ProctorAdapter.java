package shadattonmoy.navigationdrawer;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Created by Shadat Tonmoy on 9/8/2017.
 */

public class ProctorAdapter extends ArrayAdapter<Proctor> {

    private Context context;
    private ImageView imageView;
    private PopupMenu popupMenu;
    private boolean isEditable;

    public ProctorAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Proctor> objects,boolean isEditable) {
        super(context, resource, textViewResourceId, objects);
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.teacher_single_row,parent,false);
        }
        Proctor proctor = getItem(position);
        TextView teacherIcon = (TextView) row.findViewById(R.id.teacher_icon);
        ImageView contactImage = (ImageView) row.findViewById(R.id.contact_teacher);
        TextView teacherName = (TextView) row.findViewById(R.id.teacher_name);
        TextView teacherDesignation = (TextView) row.findViewById(R.id.teacher_designation);
        TextView teacherRoom = (TextView) row.findViewById(R.id.teacher_room);
        imageView = (ImageView) row.findViewById(R.id.contact_teacher);
        if(isEditable)
            imageView.setImageResource(R.drawable.edit_icon);
        else imageView.setImageResource(R.drawable.phone_final);
        //popupMenu = new PopupMenu(context,imageView, Gravity.NO_GRAVITY);
        String name = proctor.getName();
        String designation = proctor.getDesignation();
        String room = proctor.getRoomNo();
        String phone = proctor.getContactNo();
        String proctorId = proctor.getProctorId();
        String iconText = String.valueOf(name.charAt(0));
//        popupMenu.getMenuInflater().inflate(R.menu.list_menu,popupMenu.getMenu());
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupMenu.show();
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        int id = item.getItemId();
//                        if (id == R.id.edit_info_menu )
//                        {
//                            Toast.makeText(context,"Edit "+teacherId,Toast.LENGTH_SHORT).show();
//
//                            return true;
//                        }
//                        else if ( id == R.id.remove_faculty_menu)
//                        {
//                            Toast.makeText(context,"Remove "+teacherId,Toast.LENGTH_SHORT).show();
//                            return true;
//                        }
//                        return false;
//                    }
//
//                });
//            }
//        });

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
}
