package shadattonmoy.sustnavigator.utils;

import android.content.Context;
import android.os.Build;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.dept.view.DeptFragment;
import shadattonmoy.sustnavigator.teacher.model.Teacher;

public class SessionListAdapter extends ArrayAdapter<String> {

    private Context context;
    private boolean isAdmin = false;
    public SessionListAdapter(Context context, int resource, int textViewResourceId, ArrayList<String > objects) {
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
            row = inflater.inflate(R.layout.session_single_row,parent,false);
        }
        String currentSession = getItem(position);
        ImageView checkIcon = (ImageView) row.findViewById(R.id.session_selected_icon);
        TextView sessionTitle = (TextView) row.findViewById(R.id.session_title);
        sessionTitle.setText(currentSession);
        if(position== DeptFragment.bottomSheetSelectedPosition)
        {
            try{
                Glide.with(context).load(context.getResources()
                        .getIdentifier("baseline_done_black_24", "drawable", context.getPackageName())).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(checkIcon);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
//            checkIcon.setVisibility(View.HIDE);
        }
        return row;
    }
}
