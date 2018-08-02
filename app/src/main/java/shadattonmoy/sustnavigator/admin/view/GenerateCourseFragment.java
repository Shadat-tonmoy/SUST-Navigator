package shadattonmoy.sustnavigator.admin.view;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;


public class GenerateCourseFragment extends android.app.Fragment {

    private View view, detectedTextLayout;
    private ArrayList<String> detectedTexts;
    private LinearLayout detectedTextContainer;
    private TextView detectedTextView;
    private EditText courseCodeField, courseCreditField, courseTitleField;
    private DragAndDropListener dragAndDropListener;
    private String textToDrop;

    public GenerateCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectedTexts = getArguments().getStringArrayList("detectedText");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_generate_course, container, false);
        detectedTextContainer = (LinearLayout) view.findViewById(R.id.detected_text_container);
        courseCodeField = (EditText) view.findViewById(R.id.course_code_field);
        courseCreditField = (EditText) view.findViewById(R.id.course_credit_field);
        courseTitleField = (EditText) view.findViewById(R.id.course_title_field);
        generateDetectedTextsLayout(inflater);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dragAndDropListener = new DragAndDropListener();
        courseCreditField.setOnDragListener(dragAndDropListener);
        courseTitleField.setOnDragListener(dragAndDropListener);
        courseCodeField.setOnDragListener(dragAndDropListener);


    }

    public void generateDetectedTextsLayout(LayoutInflater inflater) {
        detectedTextContainer.removeAllViews();
        for (String text : detectedTexts) {
            Log.e("TextDetected", text);
            detectedTextLayout = inflater.inflate(R.layout.detected_text_chip, null, false);
            detectedTextView = (TextView) detectedTextLayout.findViewById(R.id.detected_text_chip);
            detectedTextView.setText(text);
            detectedTextView.setOnDragListener(new DragAndDropListenerForTextView());
            detectedTextView.setOnTouchListener(new MyTouchListener());
            if (detectedTextView.getParent() != null)
                ((ViewGroup) detectedTextView.getParent()).removeView(detectedTextView);
            detectedTextContainer.addView(detectedTextView);
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        /*public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
//                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }*/

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("text", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
            }
            return true;
        }
    }

    private class DragAndDropListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            Log.e("Event","ONDrag");
            switch(event.getAction())
            {
                case DragEvent.ACTION_DRAG_STARTED:
//                    textToDrop = ((TextView)v).getText().toString();
                    Log.e("Event","OnDragStarted "+v.toString());
                    break;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    CharSequence paste = item.getText();
                    Log.e("OnDrop",paste.toString());
                    if(v.getId()==R.id.course_code_field)
                    {
                        Log.e("OnDrop"," Code Field "+paste.toString());
                        ((EditText) v).setText(textToDrop);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.e("Event","OnDragEnded");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.e("Event","OnDragExited");
                    break;
                default: break;
            }

            return false;
        }

    }

    private class DragAndDropListenerForTextView implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            Log.e("Event","ONDrag");
            textToDrop = "";
            switch(event.getAction())
            {
                case DragEvent.ACTION_DRAG_STARTED:
                    textToDrop = ((TextView)v).getText().toString();
                    Log.e("Event","OnDragStartedForTextView"+textToDrop+" ID "+v.toString());
                    break;
                case DragEvent.ACTION_DROP:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    textToDrop = ((TextView)v).getText().toString();
                    Log.e("Event","OnDragStartedEndedTextView"+textToDrop+" ID "+v.toString());
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.e("Event","OnDragExited");
                    break;
                default: break;
            }

            return false;
        }

    }
}
