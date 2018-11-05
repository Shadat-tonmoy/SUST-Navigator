package shadattonmoy.sustnavigator.admin.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shadattonmoy.sustnavigator.R;

public class DetectedTextDialog extends DialogFragment {
    private View view,detectedTextLayout;
    private FlexboxLayout detectedTextContainer;
    private List<String> detectedTexts;
    private Context context;
    private TextView detectedTextView;
    private String session,semester,dept,imagePath;

    public DetectedTextDialog(Context context,List<String> detectedTexts,String session,String semester,String dept,String imagePath) {
        this.detectedTexts = detectedTexts;
        this.context = context;
        this.session = session;
        this.semester = semester;
        this.dept = dept;
        this.imagePath = imagePath;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.detected_text_dialog,null,false);
        detectedTextContainer = (FlexboxLayout) view.findViewById(R.id.detected_text_container);
        generateDetectedTexts(inflater,view);
        builder.setView(view);
        builder.setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setPositiveButton("Generate Course", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.app.FragmentManager manager = getFragmentManager();
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                GenerateCourseFragment generateCourseFragment = new GenerateCourseFragment();
                deleteSavedImage();
                Bundle args = new Bundle();
                args.putStringArrayList("detectedText", (ArrayList<String>) detectedTexts);
                args.putString("session",session);
                args.putString("semester",semester);
                args.putString("dept",dept);
                generateCourseFragment.setArguments(args);
                transaction.replace(R.id.main_content_root, generateCourseFragment);
                transaction.addToBackStack("syllabus_scan_fragment");
                transaction.commit();



            }
        });
        return builder.create();
    }

    private void generateDetectedTexts(LayoutInflater inflater,View view)
    {
        detectedTextContainer.removeAllViews();
        for(String text:detectedTexts)
        {
//            Log.e("TextDetected",text);
            detectedTextLayout = inflater.inflate(R.layout.detected_text_chip,null,false);
            detectedTextView = (TextView) detectedTextLayout.findViewById(R.id.detected_text_chip);
            detectedTextView.setText(text);
            if(detectedTextView.getParent()!=null)
                ((ViewGroup)detectedTextView.getParent()).removeView(detectedTextView);
            detectedTextContainer.addView(detectedTextView);
        }

    }

    private boolean deleteSavedImage()
    {
        File file = new File(imagePath);
        boolean deleted = file.delete();
        return deleted;
    }





}
