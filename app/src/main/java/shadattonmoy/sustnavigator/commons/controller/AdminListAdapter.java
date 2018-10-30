package shadattonmoy.sustnavigator.commons.controller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.model.Admin;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.utils.Values;

public class AdminListAdapter extends ArrayAdapter<Admin> {

    private Context context;
    public AdminListAdapter(Context context, int resource, int textViewResourceId, ArrayList<Admin> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.admin_small_single_row,parent,false);
        }
        Admin currentAdmin= getItem(position);
        TextView iconView = row.findViewById(R.id.admin_icon);
        TextView nameView = row.findViewById(R.id.admin_name);
        TextView deptView = row.findViewById(R.id.admin_dept);
        nameView.setText(currentAdmin.getName());
        deptView.setText("Dept. of "+currentAdmin.getDept());
        iconView.setText(currentAdmin.getName().substring(0,1));
        return row;
    }
}
