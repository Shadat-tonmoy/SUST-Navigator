package shadattonmoy.sustnavigator.school.controller;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import shadattonmoy.sustnavigator.R;

import java.util.List;

import shadattonmoy.sustnavigator.dept.controller.DeptGridAdapter;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.school.model.School;

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.MyViewHolder>{

    private List<Dept> deptList;
    private ViewGroup parent;
    private Context context;
    private FragmentActivity activity;
    private List<School> schools;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView schoolTitle;
        GridView deptGrid;

        public MyViewHolder(View view) {
            super(view);
            schoolTitle = (TextView) view.findViewById(R.id.school_title);
            deptGrid = (GridView) view.findViewById(R.id.dept_grid);
        }
    }


    public SchoolListAdapter(List<School> schools, Context context) {
        this.context = context;
        this.schools = schools;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.school_single_row, parent, false);
        this.parent=parent;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        School school = schools.get(position);
        String schoolTitle = school.getSchoolTitle();
        List<Dept> depts = school.getDepts();
        DeptGridAdapter deptGridAdapter = new DeptGridAdapter(context,depts);
        holder.schoolTitle.setText(schoolTitle);
        holder.deptGrid.setAdapter(deptGridAdapter);
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }



    private class DeptClickListener implements View.OnClickListener{


        public DeptClickListener() {

        }

        @Override
        public void onClick(View view) {

        }
    }
}