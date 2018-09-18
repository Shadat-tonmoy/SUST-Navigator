package shadattonmoy.sustnavigator.about;

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
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;

import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.help.HelpActivity;

public class AboutActivity extends AppCompatActivity {

    private Context context;
    private ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    private void init() {
        context = AboutActivity.this;
        /*changing the text of toolbar title*/
        Toolbar toolbarStatic = (Toolbar) findViewById(R.id.toolbar);
        toolbarStatic.setTitleTextColor(Color.WHITE);
        toolbarStatic.setTitle("About");

        /*changing the text of toolbar title for support library*/
        setSupportActionBar(toolbarStatic);
        supportActionBar = getSupportActionBar();
        supportActionBar.setTitle("About");

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
