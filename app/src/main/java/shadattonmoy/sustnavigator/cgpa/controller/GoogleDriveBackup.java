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
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
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

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        String dbname = Values.DATABASE_NAME;
        File database = context.getDatabasePath(dbname);
        if(database!=null)
        {
            Log.e("DatabasePath",database.getAbsolutePath()+" "+database.getName());
        }
        else Log.e("DatabasePath","Null");
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
                    FileInputStream fileInputStream = new FileInputStream(database);
                    IOUtils.copy(fileInputStream, outputStream);
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("sust_nav_backup")
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
                        Log.e("Error",e.getMessage());
                    }
                });
    }

    public void readDBFromDrive()
    {
        String packageName = context.getPackageName();
        driveResourceClient = Drive.getDriveResourceClient(context, signInAccount);
        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        Tasks.whenAll(appFolderTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DriveFolder parent = appFolderTask.getResult();
                Log.e("DriveFolder",parent.getDriveId().asDriveFolder().toString());
                Task<MetadataBuffer> files = driveResourceClient.listChildren(parent);
                files.addOnSuccessListener(new OnSuccessListener<MetadataBuffer>() {
                    @Override
                    public void onSuccess(MetadataBuffer metadatas) {
                        for(Metadata metadata:metadatas)
                        {
                            DriveFile driveFile = metadata.getDriveId().asDriveFile();
                            driveResourceClient.openFile(driveFile,DriveFile.MODE_READ_ONLY).addOnSuccessListener(new OnSuccessListener<DriveContents>() {
                                @Override
                                public void onSuccess(DriveContents driveContents) {
                                    InputStream inputStream = driveContents.getInputStream();
                                    int i;
                                    try {
                                        while((i = inputStream.read())!=-1) {
                                            char c = (char)i;
                                            Log.e("Char",c+"");
                                        }
                                    }catch (Exception e)
                                    {

                                    }

                                }
                            });
                            Log.e("Files are ",metadata.getOriginalFilename()+"."+metadata.getFileExtension());
                        }
                    }
                });

            }
        });

        /*Tasks.whenAll(appFolderTask)
                .continueWithTask(task -> {
                    DriveFolder parent = appFolderTask.getResult();
                    Log.e("DriveFolder",parent.getDriveId().asDriveFolder().toString());
                    Task<MetadataBuffer> files = driveResourceClient.listChildren(parent);
                    files.addOnSuccessListener(new OnSuccessListener<MetadataBuffer>() {
                        @Override
                        public void onSuccess(MetadataBuffer metadatas) {
                            for(Metadata metadata:metadatas)
                            {
                                Log.e("Files are ",metadata.getOriginalFilename()+"."+metadata.getFileExtension());
                            }
                        }
                    });
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
                });*/
    }
}
