package shadattonmoy.sustnavigator.school.controller;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import shadattonmoy.sustnavigator.R;

import java.util.List;

import shadattonmoy.sustnavigator.teacher.view.TeacherFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.school.model.School;

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.MyViewHolder>{

    private List<Dept> deptList;
    private ViewGroup parent;
    private Context context;
    private FragmentActivity activity;
    private List<School> schools;
    private FragmentManager fragmentManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView schoolTitle;
        LinearLayout deptList;

        public MyViewHolder(View view) {
            super(view);
            schoolTitle = (TextView) view.findViewById(R.id.school_title);
            deptList = (LinearLayout) view.findViewById(R.id.dept_list);
        }
    }


    public SchoolListAdapter(List<School> schools, Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.schools = schools;
        this.fragmentManager = fragmentManager;
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
        holder.deptList.removeAllViews();
        for(final Dept dept:depts)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout deptCell = (LinearLayout) inflater.inflate(R.layout.dept_single_cell, parent, false);
            TextView deptCode = (TextView) deptCell.findViewById(R.id.dept_code);
            TextView deptTitle = (TextView) deptCell.findViewById(R.id.dept_title);
            deptCode.setText(dept.getDeptCode());
            deptTitle.setText(dept.getDeptTitle());
            holder.deptList.addView(deptCell);
            deptCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager manager = fragmentManager;
                    TeacherFragment cseTeacherFragment = new TeacherFragment(dept.getDeptCode());
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.main_content_root,cseTeacherFragment,"eee_teacher_fragment");
                    transaction.addToBackStack(dept.getDeptCode()+" teacher fragment");
                    transaction.commit();
                }
            });
        }
        holder.schoolTitle.setText(schoolTitle);
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