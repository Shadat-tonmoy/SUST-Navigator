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
import android.support.v4.app.FragmentActivity;
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
    private Context context;
    private FragmentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (FragmentActivity) context;
    }

    public TeacherContactDialog(){
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout,null);
        context = getActivity();
        activity = (FragmentActivity) getActivity();
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
            String phoneNumFinal = phoneNo.trim().replaceAll("^[,A-Za-z.:-]+","");
            numberView.setText(phoneNumFinal);
            numberContainer.addView(numberRow);
            numberRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeCall(phoneNumFinal);
                }
            });
        }
    }

    private void makeCall(String phoneNo)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNo));
        if(intent.resolveActivity(context.getPackageManager())!=null)
        {
            activity.startActivity(intent);
        }
    }


}


