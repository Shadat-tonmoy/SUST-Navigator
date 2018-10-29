package shadattonmoy.sustnavigator.teacher.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;

/**
 * Created by Shadat Tonmoy on 6/18/2017.
 */

public class TeacherContactDialog extends DialogFragment{

    private LinearLayout numberContainer;
    private ArrayList<String> phoneNums;
    public TeacherContactDialog(){
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout,null);
        numberContainer = dialogView.findViewById(R.id.number_container);
        getValues();
        generateNumberLayout(inflater);
        builder.setView(dialogView);
        return builder.create();
    }

    private void getValues()
    {
        Bundle args = getArguments();
        if(args!=null)
        {
            phoneNums = args.getStringArrayList("phoneNos");
        }
    }

    private void generateNumberLayout(LayoutInflater inflater)
    {
        for(String phoneNo : phoneNums)
        {
            View numberRow = inflater.inflate(R.layout.number_single_row,null);
            TextView numberView = numberRow.findViewById(R.id.number_view);
            numberView.setText(phoneNo.trim().replaceAll("^[,A-Za-z.:-]+",""));
            numberContainer.addView(numberRow);
        }



    }


}

class clickListenet implements View.OnClickListener{

    private Context context;
    private String phoneNo;
    private String email;
    private String fb;
    private Activity activity;

    public clickListenet(Activity activity, String phoneNo,String email,String fb) {
        super();
        this.context = activity.getApplicationContext();
        this.phoneNo = phoneNo;
        this.email = email;
        this.fb = fb;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {



    }
    public void makeCall(String phoneNo)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNo));
        if(intent.resolveActivity(context.getPackageManager())!=null)
        {
            //context.startActivity(intent);
            activity.startActivity(intent);
        }
    }

    public void sendEmail(String email)
    {
        String[] address = {email};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
        else
        {
            Toast.makeText(context,"Email App Not Found!!!",Toast.LENGTH_SHORT).show();

        }
    }

    public void openFb(String fb)
    {
        Uri webpage = Uri.parse(fb);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }
}
