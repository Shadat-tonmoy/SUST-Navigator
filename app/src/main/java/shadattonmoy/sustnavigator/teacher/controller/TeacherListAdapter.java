package shadattonmoy.sustnavigator.teacher.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.admin.view.TeacherAddFragment;
import shadattonmoy.sustnavigator.dept.model.Dept;
import shadattonmoy.sustnavigator.teacher.model.Teacher;
import shadattonmoy.sustnavigator.utils.Values;

/**
 * Created by Shadat Tonmoy on 6/17/2017.
 */

public class TeacherListAdapter extends ArrayAdapter<Teacher>{



    private Dept dept=null;
    private Context context;
    private ImageView imageView;
    private boolean isAdmin = false;
    private FragmentManager fragmentManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private View view;
    private ArrayList<Teacher> teachers;
    private Map<Integer,Boolean> selectedTeachers;
    private Map<Integer,View> views;
    private boolean isWebData = false;
    private Activity activity;
    public TeacherListAdapter(Context context, int resource, int textViewResourceId, ArrayList<Teacher> objects, Dept dept) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.dept = dept;
        this.selectedTeachers = new HashMap<>();
        this.teachers = objects;
        this.views= new HashMap<>();
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
        final Teacher currentTeacher = getItem(position);
        TextView teacherIcon = (TextView) row.findViewById(R.id.teacher_icon);
        ImageView contactmage = (ImageView) row.findViewById(R.id.contact_teacher);
        TextView teacherName = (TextView) row.findViewById(R.id.teacher_name);
        TextView teacherDesignation = (TextView) row.findViewById(R.id.teacher_designation);
        TextView teacherRoom = (TextView) row.findViewById(R.id.teacher_room);
        imageView = (ImageView) row.findViewById(R.id.contact_teacher);
        Log.e("ValueIn",position+" "+selectedTeachers.get(position));
        if(selectedTeachers.get(position)!=null && selectedTeachers.get(position))
            row.setBackgroundColor(context.getResources().getColor(R.color.textLightAsh));
        else row.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));

        if(isAdmin)
        {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.more_vert_black);
        }
        else imageView.setVisibility(View.GONE);
        final PopupMenu popupMenu = new PopupMenu(context,imageView,Gravity.LEFT);
        final String name = currentTeacher.getName();
        final String designation = currentTeacher.getDesignation();
        String room = currentTeacher.getRoom();
        if(room.equals("N/A"))
            room = "Room Not Available";
        final String phone = currentTeacher.getPhone();
        final String email = currentTeacher.getEmail();
        final String teacherId = currentTeacher.getId();
        String iconText = String.valueOf(name.charAt(0));
        popupMenu.inflate(R.menu.list_menu);
        final String finalRoom = room;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.edit_info_menu )
                        {
                            android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                            TeacherAddFragment teacherAddFragment = new TeacherAddFragment(dept.getDeptCode());
                            Bundle args = new Bundle();
                            args.putString("name",name);
                            args.putString("room", finalRoom);
                            args.putString("phone", phone);
                            args.putString("email", email);
                            args.putString("designation", designation);
                            args.putString("id", teacherId);
                            args.putBoolean("isEditing",true);
                            teacherAddFragment.setArguments(args);
                            transaction.replace(R.id.main_content_root, teacherAddFragment);
                            transaction.addToBackStack("teacher_edit_fragment");
                            transaction.commit();

                            return true;
                        }
                        else if ( id == R.id.remove_faculty_menu)
                        {
                            AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(context);
                            builder.setTitle("Delete Record?")
                                    .setMessage("Are you sure you want to delete this Faculty Record? Once it is deleted you will not be able to recover it")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            firebaseDatabase = FirebaseDatabase.getInstance();
                                            final ProgressDialog progressDialog;
                                            progressDialog = new ProgressDialog(activity);
                                            progressDialog.setTitle("Deleting Record");
                                            progressDialog.setMessage("Please Wait....");
                                            progressDialog.show();
                                            firebaseDatabase.getReference().child("teacher").child(dept.getDeptCode().toLowerCase()).child(teacherId).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    progressDialog.dismiss();
                                                    Snackbar snackbar = Snackbar.make(view,"Faculty is Removed",Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                    remove(currentTeacher);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();


                            return true;
                        }
                        return false;
                    }

                });
            }
        });

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
        if(room.length()<3)
            teacherRoom.setText("Office Details Not Available");
        else teacherRoom.setText(Values.trimStringTo(room,40));

        if(isWebData)
        {
            views.put(position,row);
            Log.e("PuttingAt",position+" "+views.get(position));
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleTeacherSelection(position,view);

                }
            });
        }



        return row;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isWebData() {
        return isWebData;
    }

    public void setWebData(boolean webData) {
        isWebData = webData;
    }

    private void toggleTeacherSelection(int position, View view)
    {
        Log.e("Toggling",selectedTeachers.get(position)+" "+view);
        if(selectedTeachers.get(position)==null)
        {
            selectedTeachers.put(position,true);
            if(view!=null)
                view.setBackgroundColor(context.getResources().getColor(R.color.textLightAsh));
        }
        else if(selectedTeachers.get(position)!=null && selectedTeachers.get(position))
        {
            selectedTeachers.put(position,false);
            if(view!=null)
                view.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
        }
        else if(selectedTeachers.get(position)!=null && !selectedTeachers.get(position))
        {
            selectedTeachers.put(position,true);
            if(view!=null)
                view.setBackgroundColor(context.getResources().getColor(R.color.textLightAsh));
        }
    }

    public void toggleAllTeacher()
    {
        for(int i=0;i<teachers.size();i++)
        {
            toggleTeacherSelection(i,views.get(i));
        }
        notifyDataSetChanged();

    }

    public int getSelectedTeachers()
    {
        int totalTeacher = 0;
        for(int i=0;i<teachers.size();i++)
        {
            if(selectedTeachers.get(i)!=null && selectedTeachers.get(i))
            {
                totalTeacher++;
            }
        }
        return totalTeacher;
    }

    public void addAllTeacherToServer()
    {
        TeacherAddTask teacherAddTask = new TeacherAddTask();
        teacherAddTask.execute();
    }



    private class TeacherAddTask extends AsyncTask<Void,Void,Void>{
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setTitle("Adding Record");
            dialog.setMessage("Please Wait....");
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            for(int i=0;i<teachers.size();i++)
            {
                if(selectedTeachers.get(i)!=null && selectedTeachers.get(i))
                {
                    addFaculty(teachers.get(i));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Values.showToast(context,"Faculty Record is Added.Please visit this screen again to see the updated result.");
            dialog.dismiss();
            Values.updateLastModified();
        }
    }

    public void addFaculty(Teacher teacher)
    {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(dept.getDeptCode().toLowerCase());
        databaseReference.push().setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try{


                } catch (Exception e){

                }
            }
        });
    }

}
