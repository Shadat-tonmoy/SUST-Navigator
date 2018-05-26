package shadattonmoy.navigationdrawer;

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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shadat Tonmoy on 6/18/2017.
 */

public class TeacherContactDialog extends DialogFragment{

    private String name,email,phone,fb;
    public TeacherContactDialog(){
        super();
    }
    public TeacherContactDialog(String name, String email, String phone,String fb) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.fb = fb;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout,null);

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dailog_title);
        ImageView emailButton = (ImageView) dialogView.findViewById(R.id.emailButton);
        ImageView phoneButton = (ImageView) dialogView.findViewById(R.id.phoneButton);
        ImageView fbButton = (ImageView) dialogView.findViewById(R.id.fbButton);




        dialogTitle.setText("Contact with "+ name);
        builder.setView(dialogView);
        //builder.setMessage("Email : "+email+"\nPhone : "+phone);
        builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Negative was clicked",Toast.LENGTH_SHORT).show();
                dismiss();

            }
        });



        emailButton.setOnClickListener(new clickListenet(getActivity(),phone,email,fb));

        phoneButton.setOnClickListener(new clickListenet(getActivity(),phone,email,fb));

        fbButton.setOnClickListener(new clickListenet(getActivity(),phone,email,fb));


        return builder.create();
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

        int id = v.getId();
        int emailButtonId = R.id.emailButton;
        int phoneButtonId = R.id.phoneButton;
        int fbButtonId = R.id.fbButton;
        if(id==emailButtonId)
        {
            if(email.equals("N/A"))
            {
                Toast.makeText(context,"Sorry!! Email Not Availabe",Toast.LENGTH_SHORT).show();
            }
            else
            {
                sendEmail(email);
            }
        }
        else if(id==phoneButtonId)
        {

            if(phoneNo.equals("N/A"))
            {
                Toast.makeText(context,"Sorry!! Phone No Not Availabe",Toast.LENGTH_SHORT).show();

            }
            else
            {
                makeCall(phoneNo);
            }

        }
        else if(id==fbButtonId)
        {

            if(fb.equals("N/A"))
            {
                Toast.makeText(context,"Sorry!! Facebook ID is not Available",Toast.LENGTH_SHORT).show();

            }
            else
            {
                openFb(fb);
            }

        }

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
