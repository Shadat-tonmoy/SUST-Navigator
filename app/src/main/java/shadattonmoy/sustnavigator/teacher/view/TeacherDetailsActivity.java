package shadattonmoy.sustnavigator.teacher.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.about.AboutActivity;
import shadattonmoy.sustnavigator.utils.Values;

public class TeacherDetailsActivity extends AppCompatActivity {


    private Context context;
    private ActionBar supportActionBar;
    private TextView titleView,officeView,designationView,phoneView,emailView,teacherIcon;
    private String title,office,designation,phone,email,iconText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);
        init();
        findViews();
        findValues();
        setValuesToViews();
        setClickListener();
    }

    private void init() {
        context = TeacherDetailsActivity.this;
        /*changing the text of toolbar title*/
        Toolbar toolbarStatic = (Toolbar) findViewById(R.id.toolbar);
        toolbarStatic.setTitleTextColor(Color.WHITE);
        toolbarStatic.setTitle("Faculty Detail");

        /*changing the text of toolbar title for support library*/
        setSupportActionBar(toolbarStatic);
        supportActionBar = getSupportActionBar();
        supportActionBar.setTitle("Faculty Detail");

        /*set toolbar navigation icon and click listener*/
        toolbarStatic.setNavigationIcon(R.drawable.back_white);
        toolbarStatic.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void findViews()
    {
        titleView = (TextView) findViewById(R.id.faculty_title);
        phoneView = (TextView) findViewById(R.id.phone);
        emailView = (TextView) findViewById(R.id.email);
        officeView = (TextView) findViewById(R.id.office);
        designationView = (TextView) findViewById(R.id.faculty_designation);
        teacherIcon = (TextView) findViewById(R.id.faculty_logo);
    }

    private void findValues()
    {
        title = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        office = getIntent().getStringExtra("office");
        designation = getIntent().getStringExtra("designation");
        iconText = title.substring(0,1);
    }
    private void setValuesToViews()
    {
        titleView.setText(title);
        phoneView.setText(phone);
        emailView.setText(email);
        if(office.length()<3)
            officeView.setText("Office Details Not Available");
        else officeView.setText(office);
        designationView.setText(designation);
        teacherIcon.setText(iconText);

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
    }

    private void setClickListener()
    {
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] phoneNos = phone.split(",");
                if(phoneNos.length>1)
                {
                    showPhoneNotChooseDialog(phoneNos);
                }
                else
                {
                    makeCall(phone);
                }
            }
        });

        emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(email);
            }
        });
    }

    private void makeCall(String phoneNo)
    {
        Log.e("Phone",phoneNo);
        if(phoneNo==null || phoneNo.length()<4 )
        {
            Values.showToast(context,"Contact Number not Available");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNo));
        if(intent.resolveActivity(context.getPackageManager())!=null)
        {
            //context.startActivity(intent);
            startActivity(intent);
        }
    }

    private void showPhoneNotChooseDialog(String[] phoneNos)
    {
        ArrayList<String> phoneNums = new ArrayList<String>(Arrays.asList(phoneNos));
        TeacherContactDialog teacherContactDialog = new TeacherContactDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("phoneNos",phoneNums);
        teacherContactDialog.setArguments(args);
        teacherContactDialog.show(getFragmentManager(),"phoneNosDialog");

    }

    private void sendEmail(String email)
    {
        if(email==null || email.length()<4 )
        {
            Values.showToast(context,"Email Address not Available");
            return;
        }
        String[] address = {email};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else
        {
            Values.showToast(context,"Email App Not Found!!!");
        }
    }


}
