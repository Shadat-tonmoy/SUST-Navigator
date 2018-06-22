package shadattonmoy.sustnavigator.admin.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private final String TAG = "CameraActivity";
    public ScanSyllabusFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity().getApplicationContext();
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
                scan();

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
        // Get the dimensions of the View
        int targetW = outputImage.getWidth();
        int targetH = outputImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        outputImage.setImageBitmap(bitmap);
    }

    private void scan()
    {
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
                Log.e("OutputText",textBlock.getValue());
            }
        }
        Log.e("OutputText",detectedText);
    }




}
