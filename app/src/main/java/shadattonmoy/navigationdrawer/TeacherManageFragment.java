package shadattonmoy.navigationdrawer;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TeacherManageFragment extends android.app.Fragment {

    private ProgressBar progressBar;
    private String deptToManage;
    private ArrayList<Teacher> teachers;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private View view = null;
    private TextView addFacultyButton;
    private FloatingActionButton floatingActionButton;
    private TeacherAdapterForManage adapter;
    private ListView teacherListView;

    public TeacherManageFragment() {
        super();

    }
    public TeacherManageFragment(String dept) {
        this.deptToManage = dept;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_teacher_manage, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.manage_teacher_loading);
        addFacultyButton = (TextView) view.findViewById(R.id.add_more_faculty_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        teachers = new ArrayList<Teacher>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("teacher").child(deptToManage.toLowerCase());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    String pushId = child.getKey();
                    Teacher currentTeachcer = child.getValue(Teacher.class);
                    currentTeachcer.setId(pushId);
                    teachers.add(currentTeachcer);
                    adapter = new TeacherAdapterForManage(getActivity().getApplicationContext(),R.layout.teacher_single_row,R.id.teacher_icon,teachers,deptToManage);
                    teacherListView = (ListView) view.findViewById(R.id.teacher_list_for_manage);
                    teacherListView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        addFacultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                FacultyAddFragment facultyAddFragment = new FacultyAddFragment(deptToManage);
                transaction.replace(R.id.main_content_root,facultyAddFragment);
                transaction.addToBackStack("teacher_add_fragment");
                transaction.commit();
            }
        });
    }
    public class TeacherAdapterForManage extends ArrayAdapter<Teacher> {



        private String dept=null;
        private Context context;
        private ImageView imageView;
        private PopupMenu popupMenu;
        public TeacherAdapterForManage(Context context, int resource, int textViewResourceId, ArrayList<Teacher> objects,String dept) {
            super(context, resource, textViewResourceId, objects);
            this.context = context;
            this.dept = dept;
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
            final TextView teacherDesignation = (TextView) row.findViewById(R.id.teacher_designation);
            TextView teacherRoom = (TextView) row.findViewById(R.id.teacher_room);
            imageView = (ImageView) row.findViewById(R.id.contact_teacher);
            imageView.setImageResource(R.drawable.edit_icon);
            popupMenu = new PopupMenu(getContext(),imageView);

            final String name = currentTeacher.getName();
            final String designation = currentTeacher.getDesignation();
            final String room = currentTeacher.getRoom();
            final String phone = currentTeacher.getPhone();
            final String email = currentTeacher.getEmail();
            final String pushId = currentTeacher.getId();
            final String fb = currentTeacher.getFb();
            final String teacherId = currentTeacher.getId();
            String iconText = String.valueOf(name.charAt(0));
            popupMenu.inflate(R.menu.list_menu);
            //popupMenu.getMenuInflater().inflate(R.menu.list_menu,popupMenu.getMenu());
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
                                //Toast.makeText(context,"Edit "+teacherId,Toast.LENGTH_SHORT).show();
                                android.app.FragmentManager manager = getFragmentManager();
                                TeacherEditFragment teacherEditFragment = new TeacherEditFragment(name,designation,room,phone,email,teacherId,deptToManage,fb);
                                android.app.FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.main_content_root,teacherEditFragment);
                                transaction.addToBackStack("teacher_edit_fragment");
                                transaction.commit();


                                return true;
                            }
                            else if ( id == R.id.remove_faculty_menu)
                            {
                                Toast.makeText(context,"Remove "+teacherId,Toast.LENGTH_SHORT).show();
                                firebaseDatabase.getReference().child("teacher").child(dept.toLowerCase()).child(teacherId).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Snackbar snackbar = Snackbar.make(view,"Faculty is Removed",Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        adapter.remove(currentTeacher);


                                    }
                                });
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
            teacherRoom.setText(room);



            return row;
        }
    }

}
