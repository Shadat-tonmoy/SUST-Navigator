package shadattonmoy.sustnavigator.cgpa.controller;

import android.content.Context;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.acl.LastOwnerException;

import shadattonmoy.sustnavigator.utils.Values;

public class GoogleDriveBackup {
    private Context context;
    private GoogleSignInAccount signInAccount;
    private DriveResourceClient driveResourceClient;
    private DriveClient driveClient;
    private TaskCompletionSource mOpenItemTaskSource;
    protected static final int REQUEST_CODE_OPEN_ITEM = 1;


    public GoogleDriveBackup(Context context,GoogleSignInAccount signInAccount) {
        this.context = context;
        this.signInAccount = signInAccount;
    }

    public void saveDBToDrive()
    {
        String packageName = context.getPackageName();
        driveResourceClient = Drive.getDriveResourceClient(context, signInAccount);
        driveClient = Drive.getDriveClient(context, signInAccount);
        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        final Task<DriveContents> createContentsTask = driveResourceClient.createContents();
        Tasks.whenAll(appFolderTask, createContentsTask)
                .continueWithTask(task -> {
                    DriveFolder parent = appFolderTask.getResult();
                    DriveContents contents = createContentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write("Hello World!");
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("SUSTNAVFILE")
                            .setMimeType("text/plain")
                            .setStarred(true)
                            .build();

                    return driveResourceClient.createFile(parent, changeSet, contents);
                })
                .addOnSuccessListener(new OnSuccessListener<DriveFile>() {
                    @Override
                    public void onSuccess(DriveFile driveFile) {
                        Values.showToast(context,"Data Backup Successfully");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Values.showToast(context,"Error Saving File");
                    }
                });
    }

    public void readDBFromDrive()
    {
        String packageName = context.getPackageName();
        driveResourceClient = Drive.getDriveResourceClient(context, signInAccount);
        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        Tasks.whenAll(appFolderTask)
                .continueWithTask(task -> {
                    DriveFolder parent = appFolderTask.getResult();
                    return driveResourceClient.openFile(parent.getDriveId().asDriveFile(),DriveFile.MODE_READ_ONLY);
                })
                .addOnSuccessListener(new OnSuccessListener<DriveContents>() {
                    @Override
                    public void onSuccess(DriveContents driveContents) {
                        Log.e("DriveContent",driveContents.getInputStream().toString());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Values.showToast(context,"Error Saving File");
                        Log.e("Error",e.getMessage());
                    }
                });
    }
}
