package shadattonmoy.sustnavigator.admin.view;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import shadattonmoy.sustnavigator.Course;
import shadattonmoy.sustnavigator.R;
import shadattonmoy.sustnavigator.proctor.model.Proctor;


public class ScanSyllabusDetailFragment extends Fragment {
    private View view;
    private Activity activity;
    private Context context;
    private FragmentManager fragmentManager;
    private Button openCameraButton, startScanningButton, cropImageButton, cropDoneButton;
    private ImageView outputImage;
    private CropImageView cropImageView;
    String[] permissions;
    private int PERMISSION_ALL = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private String packageName;
    private Bitmap bitmap, bitmapCropped;
    private FirebaseVisionImage firebaseVisionImage;
    private FirebaseVisionTextDetector firebaseVisionTextDetector;
    private ArrayList<String> detectedTexts;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String session,dept,semester;
    private Course course;


    public ScanSyllabusDetailFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity().getApplicationContext();
        fragmentManager = getActivity().getFragmentManager();

        if(getArguments()!=null)
        {
            course = (Course) getArguments().getSerializable("course");
            session = getArguments().getString("session");
            semester = getArguments().getString("semester");
            dept = getArguments().getString("dept");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan_syllabus_detail, container, false);
        openCameraButton = (Button) view.findViewById(R.id.open_camera_button);
        startScanningButton = (Button) view.findViewById(R.id.start_scanning_button);
        cropImageButton = (Button) view.findViewById(R.id.crop_image_button);
        cropDoneButton = (Button) view.findViewById(R.id.crop_done_button);
        outputImage = (ImageView) view.findViewById(R.id.output_image);
        cropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        context = getActivity();
        activity = (FragmentActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == activity.RESULT_OK) {
//            Log.e("Image", "Taken");
            setPic();
        }
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
                new ScanSyllabusDetailFragment.BackgroundTask().execute(bitmapCropped);

            }
        });

        cropImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputImage.setVisibility(View.GONE);
                cropImageView.setVisibility(View.VISIBLE);
                cropImageView.setImageBitmap(bitmap);
                cropDoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bitmapCropped = cropImageView.getCroppedImage();
                        outputImage.setImageBitmap(bitmapCropped);
                        cropImageView.setVisibility(View.GONE);
                        outputImage.setVisibility(View.VISIBLE);
                        cropImageView.setImageBitmap(bitmapCropped);
//                        Log.e("CroppedImage", "Done");
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
//                Log.e("PhotoFile", photoFile.getName());
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
//        bitmap = adjustedContrast(bitmap,100);
//        cropImageView.setImageBitmap(bitmap);
        bitmap = changeBitmapContrastBrightness(bitmap, 1, -5);
        outputImage.setImageBitmap(bitmap);
//        outputImage.setVisibility(View.GONE);
    }

    private Bitmap changeBitmapContrastBrightness(Bitmap bitmap, float contrast, float brightness) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });
        Bitmap bitmapToReturn = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapToReturn);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapToReturn;
    }

    private class BackgroundTask extends AsyncTask<Bitmap, Void, Void> {

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
        protected Void doInBackground(Bitmap... bitmaps) {
            startScanning(bitmaps[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        private void startScanning(Bitmap bitmap) {
//            bitmap = changeBitmapContrastBrightness(bitmap,10,10);
            detectedTexts = new ArrayList<>();
            firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();

            Task<FirebaseVisionText> result =
                    firebaseVisionTextDetector.detectInImage(firebaseVisionImage)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    String detectedText = "";
                                    for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                                        Rect boundingBox = block.getBoundingBox();
                                        Point[] cornerPoints = block.getCornerPoints();
                                        String text = block.getText();
                                        detectedText += text;
                                    }
                                    detectedTexts.add(detectedText);
                                    showDialog(detectedText);
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
        }


        private void showDialog(final String detectedText) {

            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Detected Text")
                    .setMessage(detectedText)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            course.setCourseDetail(detectedText);
                            databaseReference = firebaseDatabase.getReference().child("syllabus").child(session).child(dept).child(semester).child(course.getCourse_id());
                            databaseReference.setValue(course,new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    progressDialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(view, "Course Detail added...", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("Back", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getFragmentManager().popBackStack();
                                        }
                                    });
                                    snackbar.show();

                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }


}
