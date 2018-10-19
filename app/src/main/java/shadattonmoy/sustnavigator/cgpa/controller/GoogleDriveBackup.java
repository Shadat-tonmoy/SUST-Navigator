package shadattonmoy.sustnavigator.cgpa.controller;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import shadattonmoy.sustnavigator.SQLiteAdapter;
import shadattonmoy.sustnavigator.utils.Values;

public class GoogleDriveBackup {
    private Context context;
    private GoogleSignInAccount signInAccount;
    private DriveResourceClient driveResourceClient;
    private DriveClient driveClient;
    private TaskCompletionSource mOpenItemTaskSource;
    private FragmentActivity activity;
    protected static final int REQUEST_CODE_OPEN_ITEM = 1;


    public GoogleDriveBackup(Context context,GoogleSignInAccount signInAccount,FragmentActivity activity) {
        this.context = context;
        this.signInAccount = signInAccount;
        this.activity = activity;
    }

    public void saveDBToDrive()
    {
        deleteExistingFiles();
        String dbname = Values.DATABASE_NAME;
        File database = context.getDatabasePath(dbname);
        String packageName = context.getPackageName();
        driveResourceClient = Drive.getDriveResourceClient(context, signInAccount);
        driveClient = Drive.getDriveClient(context, signInAccount);
        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        final Task<DriveContents> createContentsTask = driveResourceClient.createContents();
        Tasks.whenAll(appFolderTask, createContentsTask)
                .continueWithTask(task -> {
                    DriveFolder parent = appFolderTask.getResult();
//                    Log.e("ParentFolder",parent.getDriveId()+"");

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

    public void deleteExistingFiles()
    {
        String packageName = context.getPackageName();
        driveResourceClient = Drive.getDriveResourceClient(context, signInAccount);
        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        Tasks.whenAll(appFolderTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DriveFolder parent = appFolderTask.getResult();
//                Log.e("DriveFolder",parent.getDriveId().asDriveFolder().toString());
                Task<MetadataBuffer> files = driveResourceClient.listChildren(parent);
                files.addOnSuccessListener(new OnSuccessListener<MetadataBuffer>() {
                    @Override
                    public void onSuccess(MetadataBuffer metadatas) {
//                        Log.e("ReadingData","Success "+metadatas.toString());
                        for(Metadata metadata:metadatas)
                        {
                            DriveFile driveFile = metadata.getDriveId().asDriveFile();
                            driveResourceClient.delete(driveFile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    Log.e("Deleting","AllFiles");
                                }
                            });
//                            Log.e("Files are ",metadata.getOriginalFilename()+"."+metadata.getFileExtension());
                        }
                    }
                });

                files.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.e("DeletingFalied",e.getMessage());
                    }
                });

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
//                Log.e("DriveFolder",parent.getDriveId().asDriveFolder().toString());
                Task<MetadataBuffer> files = driveResourceClient.listChildren(parent);
                files.addOnSuccessListener(new OnSuccessListener<MetadataBuffer>() {
                    @Override
                    public void onSuccess(MetadataBuffer metadatas) {
//                        Log.e("ReadingData","Success "+metadatas.toString());
                        for(Metadata metadata:metadatas)
                        {
                            DriveFile driveFile = metadata.getDriveId().asDriveFile();
                            driveResourceClient.openFile(driveFile,DriveFile.MODE_READ_ONLY).addOnSuccessListener(new OnSuccessListener<DriveContents>() {
                                @Override
                                public void onSuccess(DriveContents driveContents) {
                                    InputStream inputStream = driveContents.getInputStream();
                                    try {
                                        buildDatabaseFileFromInputStream(inputStream);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
//                            Log.e("Files are ",metadata.getOriginalFilename()+"."+metadata.getFileExtension());
                        }
                    }
                });

                files.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BackupReadingFailed",e.getMessage());
                    }
                });

            }
        });
    }

    public void buildDatabaseFileFromInputStream(InputStream inputStream) throws IOException {
        String backupDBName= Values.DATABASE_NAME + "_tmp.db";
        String localDBWithPath = context.getDatabasePath(backupDBName).getPath();

        // Opened assets database structure
        OutputStream myOutput = null;
        try {
            File localDBFile = new File(localDBWithPath);
            myOutput = new FileOutputStream(localDBFile);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[4096000];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            inputStream.close();
            File file = context.getDatabasePath(backupDBName);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
            }

            SQLiteDatabase dbFromCloud = SQLiteDatabase.openOrCreateDatabase(file, null);
            copyData(dbFromCloud);
//            Log.e("TMPDB",context.getDatabasePath(Values.DATABASE_NAME + "_tmp.db")+"");

        } catch (Exception e) {
//            e.printStackTrace();
            Log.e("Error",e.getMessage());
        }

    }

    private void copyData(SQLiteDatabase cloudDB) {
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.getInstance(context);
        sqLiteAdapter.recreateCGPATable();
        String cloudDBName= Values.DATABASE_NAME + "_tmp.db";
        Cursor cursor = cloudDB.query(true, SQLiteAdapter.SQLiteHelper.CGPA_TABLE, null, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] colums = cursor.getColumnNames();
            String id = cursor.getString(0);
            String semester = cursor.getString(1);
            String code = cursor.getString(2);
            String title = cursor.getString(3);
            String credit = cursor.getString(4);
            String grade = cursor.getString(5);
            int isAdded = cursor.getInt(6);
            sqLiteAdapter.insertCourseCGPA(semester,code,title,credit,grade,isAdded);
            cursor.moveToNext();
        }
        cursor.close();
        cloudDB.close();
//        context.deleteDatabase(cloudDBName);
        boolean deleteDB = context.deleteDatabase(Values.DATABASE_NAME + "_tmp");
        Log.e("DBDelete",deleteDB+" ");
    }

    public void startBackupTask()
    {
        DriveBackupTask driveBackupTask = new DriveBackupTask();
        driveBackupTask.execute();
    }

    public void startRestoreTask()
    {
        DriveRestoreTask driveRestoreTask = new DriveRestoreTask();
        driveRestoreTask.execute();
    }

    private class DriveBackupTask extends AsyncTask<Void,Void,Void>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Backup Data is in progress. This may take a while");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            saveDBToDrive();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Values.showToast(context,"Data Backup Successfully");
        }
    }

    private class DriveRestoreTask extends AsyncTask<Void,Void,Void>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Restoring Data is in progress. This may take a while");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readDBFromDrive();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Values.showToast(context,"Data Backup Successfully");
        }
    }




}

