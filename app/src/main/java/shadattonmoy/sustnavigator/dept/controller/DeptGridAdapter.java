package shadattonmoy.sustnavigator.dept.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.R;

public class DeptGridAdapter extends BaseAdapter {

    private List<Dept> depts;
    private Context context;

    public class ViewHolder {
        TextView deptTitle,deptCode;
    }

    public DeptGridAdapter(Context context, List<Dept> depts) {
        this.context = context;
        this.depts= depts;

    }

    @Override
    public int getCount() {
        return depts.size();
    }

    @Override
    public Object getItem(int position) {
        return depts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        DeptGridAdapter.ViewHolder viewHolder;
        if (cell == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cell = inflater.inflate(R.layout.dept_single_cell, parent, false);
            viewHolder = new DeptGridAdapter.ViewHolder();
            viewHolder.deptTitle= (TextView) cell.findViewById(R.id.dept_title);
            viewHolder.deptCode = (TextView) cell.findViewById(R.id.dept_code);
            cell.setTag(viewHolder);
        } else {
            viewHolder = (DeptGridAdapter.ViewHolder) convertView.getTag();
        }
        String deptTitle = depts.get(position).getDeptTitle();
        String deptCode = depts.get(position).getDeptCode();

        viewHolder.deptTitle.setText(deptTitle);
        viewHolder.deptCode.setText(deptCode);
        return cell;
    }

    private class ClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }
}
