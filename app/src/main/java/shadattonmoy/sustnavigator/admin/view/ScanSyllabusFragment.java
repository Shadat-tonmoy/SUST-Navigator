package shadattonmoy.sustnavigator.admin.view;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import shadattonmoy.sustnavigator.R;


public class ScanSyllabusFragment extends android.app.Fragment {

    private View view;
    String[] permissions;
    private int PERMISSION_ALL = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Button openCameraButton, startScanningButton;
    private ImageView outputImage;
    private String mCurrentPhotoPath;
    private String packageName;
    private Bitmap bitmap;
    private Activity activity;
    private Context context;
    private FirebaseVisionImage firebaseVisionImage;
    private FirebaseVisionTextDetector firebaseVisionTextDetector;
    private FragmentManager fragmentManager;
    private ArrayList<String> detectedTexts;
    private final String TAG = "CameraActivity";
    public ScanSyllabusFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity().getApplicationContext();
        fragmentManager = getActivity().getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan_syllabus, container, false);
        openCameraButton = (Button) view.findViewById(R.id.open_camera_button);
        startScanningButton = (Button) view.findViewById(R.id.start_scanning_button);
        outputImage = (ImageView) view.findViewById(R.id.output_image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestPermissions();
        packageName = activity.getPackageName();
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        startScanningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == activity.RESULT_OK) {
            Log.e("Image", "Taken");
            setPic();
        }
    }

    public void requestPermissions() {
        permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}; /*array of string containing all the required permission defined in manifest file*/
        PERMISSION_ALL = 1;
        if (!hasPermissions(context, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_ALL);
        } else {

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.e("PhotoFile", photoFile.getName());
                Uri photoURI = FileProvider.getUriForFile(context,
                        packageName + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setPic() {

        int targetW = outputImage.getWidth();
        int targetH = outputImage.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        outputImage.setImageBitmap(bitmap);
    }



    private class BackgroundTask extends AsyncTask<Void,Void,Void>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Scanning Image");
            progressDialog.setMessage("Please Wait....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            startScanning();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }


        private void scan()
        {
            detectedTexts = new ArrayList<>();
            TextRecognizer textRecognizer = new TextRecognizer.Builder(context)
                    .build();
            if (!textRecognizer.isOperational()) {
                new AlertDialog.Builder(context)
                        .setMessage("Text recognizer could not be set up on your device :(").show();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> text = textRecognizer.detect(frame);

            String detectedText = "";

            for (int i = 0; i < text.size(); i++) {
                TextBlock textBlock = text.valueAt(i);
                if (textBlock != null && textBlock.getValue() != null) {
                    detectedText += textBlock.getValue();
                    String textBlockValue = textBlock.getValue();
                    detectedTexts.add(textBlockValue);
                    Log.e("OutputText",textBlock.getValue());
                }
            }
            Log.e("OutputText",detectedText);
        }

        private void startScanning() {
            detectedTexts = new ArrayList<>();
            firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();

            Task<FirebaseVisionText> result =
                    firebaseVisionTextDetector.detectInImage(firebaseVisionImage)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks()) {
                                        Rect boundingBox = block.getBoundingBox();
                                        Point[] cornerPoints = block.getCornerPoints();
                                        String text = block.getText();
                                        for (FirebaseVisionText.Line line: block.getLines()) {

                                            Log.e("LineText",line.getText());
                                            for (FirebaseVisionText.Element element: line.getElements()) {
                                            Log.e("LineElement",element.getText());
                                            detectedTexts.add(element.getText());
                                            }
                                        }
                                    }
                                    showDialog();


                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
        }

        private void showDialog()
        {

            DetectedTextDialog detectedTextDialog = new DetectedTextDialog(context,detectedTexts);
            detectedTextDialog.show(fragmentManager,"detectedTextDialog");

        }
    }




}
