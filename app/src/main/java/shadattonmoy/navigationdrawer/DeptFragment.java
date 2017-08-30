package shadattonmoy.navigationdrawer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class DeptFragment extends android.app.Fragment implements View.OnClickListener{

    private String root;

    public DeptFragment() {

    }


    public void setRoot(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_dept, container, false);
        view.findViewById(R.id.dept_code_cse).setOnClickListener(this);
        view.findViewById(R.id.dept_code_eee).setOnClickListener(this);
        view.findViewById(R.id.dept_code_ipe).setOnClickListener(this);
        view.findViewById(R.id.dept_code_fet).setOnClickListener(this);
        view.findViewById(R.id.dept_code_mee).setOnClickListener(this);
        view.findViewById(R.id.dept_code_cee).setOnClickListener(this);
        view.findViewById(R.id.dept_code_cep).setOnClickListener(this);
        view.findViewById(R.id.dept_code_pme).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int id_cse = getActivity().findViewById(R.id.dept_code_cse).getId();
        int id_eee = getActivity().findViewById(R.id.dept_code_eee).getId();
        int id_ipe = getActivity().findViewById(R.id.dept_code_ipe).getId();
        int id_cee = getActivity().findViewById(R.id.dept_code_cee).getId();
        int id_cep = getActivity().findViewById(R.id.dept_code_cep).getId();
        int id_mee = getActivity().findViewById(R.id.dept_code_mee).getId();
        int id_fet = getActivity().findViewById(R.id.dept_code_fet).getId();
        int id_pme = getActivity().findViewById(R.id.dept_code_pme).getId();
        if(root.equals("TEACHER"))
        {
            if(id==id_cse)
            {
                FragmentManager manager = getFragmentManager();
                TeacherFragment cseTeacherFragment = new TeacherFragment("CSE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherFragment,"cse_teacher_fragment");
                transaction.addToBackStack("cse_teacher_fragment");
                transaction.commit();
            }

            else if(id==id_eee)
            {
                FragmentManager manager = getFragmentManager();
                TeacherFragment cseTeacherFragment = new TeacherFragment("EEE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherFragment,"eee_teacher_fragment");
                transaction.addToBackStack("eee_teacher_fragment");
                transaction.commit();

            }
            else if(id==id_ipe)
            {
                FragmentManager manager = getFragmentManager();
                TeacherFragment cseTeacherFragment = new TeacherFragment("IPE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherFragment,"ipe_teacher_fragment");
                transaction.addToBackStack("ipe_teacher_fragment");
                transaction.commit();
                Toast.makeText(getActivity().getApplicationContext(),"IPE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                FragmentManager manager = getFragmentManager();
                TeacherFragment cseTeacherFragment = new TeacherFragment("CEP");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherFragment,"cep_teacher_fragment");
                transaction.addToBackStack("cep_teacher_fragment");
                transaction.commit();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"PME",Toast.LENGTH_SHORT).show();
            }
        }
        else if(root.equals("TEACHER_MANAGE"))
        {
            if(id==id_cse)
            {
                FragmentManager manager = getFragmentManager();
                TeacherManageFragment cseTeacherManageFragment = new TeacherManageFragment("CSE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherManageFragment,"cse_teacher_fragment");
                transaction.addToBackStack("cse_teacher_manage_fragment");
                transaction.commit();
            }

            else if(id==id_eee)
            {
                FragmentManager manager = getFragmentManager();
                TeacherManageFragment eeeTeacherManageFragment = new TeacherManageFragment("EEE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,eeeTeacherManageFragment,"eee_teacher_fragment");
                transaction.addToBackStack("eee_teacher_manage_fragment");
                transaction.commit();

            }
            else if(id==id_ipe)
            {
                FragmentManager manager = getFragmentManager();
                TeacherManageFragment ipeTeacherManageFragment = new TeacherManageFragment("IPE");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,ipeTeacherManageFragment,"ipe_teacher_fragment");
                transaction.addToBackStack("ipe_teacher_manage_fragment");
                transaction.commit();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                FragmentManager manager = getFragmentManager();
                TeacherFragment cseTeacherFragment = new TeacherFragment("CEP");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cseTeacherFragment,"cep_teacher_fragment");
                transaction.addToBackStack("cep_teacher_fragment");
                transaction.commit();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"PME",Toast.LENGTH_SHORT).show();
            }
        }


        else if(root.equals("SYLLABUS"))
        {
            if(id==id_cse)
            {
                FragmentManager manager = getFragmentManager();
                SyllabusFragmentTmp syllabusFragmentTmp = new SyllabusFragmentTmp();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root, syllabusFragmentTmp,"syllabus_fragment");
                transaction.addToBackStack("syllabus_fragment");
                transaction.commit();
            }

            else if(id==id_eee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus EEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_ipe)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus IPE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus CEP",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus PME",Toast.LENGTH_SHORT).show();
            }
        }
        else if(root.equals("SYLLABUS_MANAGE"))
        {
            if(id==id_cse)
            {
                FragmentManager manager = getFragmentManager();
                SyllabusManageFragment syllabusManageFragment= new SyllabusManageFragment("cse",getActivity().getApplicationContext());
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,syllabusManageFragment,"syllabus_manage_fragment");
                transaction.addToBackStack("syllabus_manage_fragment");
                transaction.commit();
            }

            else if(id==id_eee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus EEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_ipe)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus IPE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus CEP",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Syllabus PME",Toast.LENGTH_SHORT).show();
            }
        }

        else if(root.equals("CGPA"))
        {
            if(id==id_cse)
            {
                FragmentManager manager = getFragmentManager();
                CGPAFragment cgpaFragment= new CGPAFragment("Computer Science and Engineering");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_content_root,cgpaFragment,"cgpa_fragment");
                transaction.addToBackStack("cgpa_fragment");
                transaction.commit();
            }

            else if(id==id_eee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA EEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_ipe)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA IPE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA CEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_cep)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA CEP",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_mee)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA MEE",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_fet)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA FET",Toast.LENGTH_SHORT).show();
            }
            else if(id==id_pme)
            {
                Toast.makeText(getActivity().getApplicationContext(),"CGPA PME",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
