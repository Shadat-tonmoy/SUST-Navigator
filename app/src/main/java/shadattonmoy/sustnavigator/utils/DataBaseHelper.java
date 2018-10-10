package shadattonmoy.sustnavigator.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private Context context;
    private InputStream inputStream;
    private String databasePath;

    //private String DB_PATH = context.getApplicationContext().getPackageName()+"/databases/";
    private static String DB_NAME = Values.DATABASE_NAME + ".db";//the extension may be .sqlite or .db
    public SQLiteDatabase myDataBase;
    /*private String DB_PATH = "/data/data/"
                        + context.getApplicationContext().getPackageName()
                        + "/databases/";*/


    public DataBaseHelper(Context context)
    {
        super(context,DB_NAME,null,1);
        this.databasePath = context.getDatabasePath(DB_NAME).getPath();
        Log.e("DatabasePathFromConst",databasePath);


    }

    public DataBaseHelper(Context context, InputStream inputStream) throws IOException {
        super(context, DB_NAME, null, 1);
        this.context = context;
        this.inputStream = inputStream;
        this.databasePath = context.getDatabasePath(DB_NAME).getPath();
        boolean dbexist = checkdatabase();
        /*if (dbexist) {
            //System.out.println("Database exists");
            opendatabase();
        } else {
            System.out.println("Database doesn't exist");
            createdatabase();
        }*/
    }

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if (dbexist) {
            //System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkdatabase() {
        //SQLiteDatabase checkdb = null;
        boolean checkdb = false;
        try {
            File dbfile = new File(databasePath);
            //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        // Path to the just created empty db
        String outfilename = databasePath + DB_NAME;
        //Open the empty db as the output stream
        OutputStream newDatabase = new FileOutputStream(databasePath);
        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            newDatabase.write(buffer, 0, length);
            Log.e("Writing",buffer.toString());
        }
        //Close the streams
        newDatabase.flush();
        newDatabase.close();
        inputStream.close();

    }

    public void opendatabase() throws SQLException {
        //Open the database
        myDataBase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}