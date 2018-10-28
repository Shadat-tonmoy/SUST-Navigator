package shadattonmoy.sustnavigator.teacher.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.about.AboutActivity;

public class TeacherDetailsActivity extends AppCompatActivity {


    private Context context;
    private ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);
        init();

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
}
