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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import shadattonmoy.sustnavigator.R;


public class GenerateCourseFragment extends android.app.Fragment {

    private View view,detectedTextLayout;
    private ArrayList<String> detectedTexts;
    private LinearLayout detectedTextContainer;
    private TextView detectedTextView;
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
        generateDetectedTextsLayout(inflater);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void generateDetectedTextsLayout(LayoutInflater inflater)
    {
        detectedTextContainer.removeAllViews();
        for(String text:detectedTexts)
        {
            Log.e("TextDetected",text);
            detectedTextLayout = inflater.inflate(R.layout.detected_text_chip,null,false);
            detectedTextView = (TextView) detectedTextLayout.findViewById(R.id.detected_text_chip);
            detectedTextView.setText(text);
            detectedTextView.setOnDragListener(new DragAndDropListener());
            detectedTextView.setOnTouchListener(new MyTouchListener());
            if(detectedTextView.getParent()!=null)
                ((ViewGroup)detectedTextView.getParent()).removeView(detectedTextView);
            detectedTextContainer.addView(detectedTextView);
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
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
        }
    }

    private class DragAndDropListener implements View.OnDragListener{

        @Override
        public boolean onDrag(View view, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.e("Action","ACTION_DRAG_STARTED");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.e("Action","ACTION_DRAG_ENTERED");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.e("Action","ACTION_DRAG_EXITED");
                    break;
                case DragEvent.ACTION_DROP:
                    Log.e("Action","ACTION_DROP");
                    TextView textView = (TextView) event.getLocalState();
                    Log.e("DroppedText",textView.getText().toString()+" To Dropped "+view.toString());

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.e("Action","ACTION_DRAG_ENDED");
                    textView = (TextView) event.getLocalState();
                    Log.e("DroppedText",textView.getText().toString()+" To Dropped "+view.toString());
                default:
                    break;
            }
            return true;
        }
    }
}
