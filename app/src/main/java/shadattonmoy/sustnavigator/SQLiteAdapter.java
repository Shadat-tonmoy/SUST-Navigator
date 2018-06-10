package shadattonmoy.sustnavigator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Shadat Tonmoy on 7/9/2017.
 */

public class SQLiteAdapter {


    SQLiteHelper sqLiteHelper = null;
    Context context;

    public SQLiteAdapter(Context context)
    {
        sqLiteHelper = new SQLiteHelper(context);
        this.context=context;

    }

    /*
    * method to insert teacher data in sqlite
    */
    public long insertCourse(String semester,String code,String title,String credit, String grade,int isAdded)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.SEMESTER,semester);
        contentValues.put(SQLiteHelper.COURSE_CODE,code);
        contentValues.put(SQLiteHelper.COURSE_TITLE,title);
        contentValues.put(SQLiteHelper.COURSE_CREDIT,credit);
        contentValues.put(SQLiteHelper.GRADE,grade);
        contentValues.put(SQLiteHelper.IS_ADDED,isAdded);
        long id = db.insert(SQLiteHelper.TABLE_NAME,null,contentValues);
        return id;

    };

    public Cursor getGPARecord(String[] semesters)
    {
        String [] columns = {SQLiteHelper.ID,SQLiteHelper.SEMESTER,SQLiteHelper.COURSE_CODE,SQLiteHelper.COURSE_TITLE,SQLiteHelper.COURSE_CREDIT,SQLiteHelper.GRADE,SQLiteHelper.IS_ADDED};
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String selection = SQLiteHelper.SEMESTER+"=?";
        String[] selectionArgs = semesters;
        Cursor cursor = db.query(SQLiteHelper.TABLE_NAME,columns,selection,selectionArgs,null,null,null);

        return cursor;
    }

    public int updateRecord(String semester,String code,String title,String credit, String grade)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COURSE_CODE,code);
        contentValues.put(SQLiteHelper.COURSE_TITLE,title);
        contentValues.put(SQLiteHelper.COURSE_CREDIT,credit);
        contentValues.put(SQLiteHelper.GRADE,grade);
        String whereClause = sqLiteHelper.SEMESTER+"=?";
        String[] whereArgs = {semester};
        int result = db.update(sqLiteHelper.TABLE_NAME,contentValues,whereClause,whereArgs);
        return result;
    }

    public int delete(String semester)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String whereClause = sqLiteHelper.SEMESTER+"=?";
        String[] whereArgs = {semester};
        int result = db.delete(sqLiteHelper.TABLE_NAME,whereClause,whereArgs);
        return result;

    }
    public class SQLiteHelper extends SQLiteOpenHelper{

        static final String DB_NAME = "database";
        static final int DB_VERSION = 20;
        static final String TABLE_NAME = "cgpa";
        static final String ID = "_id";
        static final String SEMESTER = "semseter";
        static final String COURSE_CODE = "course_code";
        static final String COURSE_TITLE = "course_title";
        static final String COURSE_CREDIT = "course_credit";
        static final String GRADE = "grade";
        static final String SEMESTER_INT = "semester_int";
        static final String IS_ADDED = "is_added";

        static final String CREATE_TABLE = "create table "+TABLE_NAME +"("+ID +" INTEGER primary key autoincrement, "+SEMESTER+" varchar(50),"+ COURSE_CODE+" varchar(500), "+ COURSE_TITLE+" varchar(500),"+ COURSE_CREDIT +" varchar(50),"+ GRADE +" varchar(100),"+IS_ADDED+" INTEGER DEFAULT 0)";
        private static final String DROP_TABLE = "drop table if exists "+TABLE_NAME+" ";



        public SQLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context,"OnCreate",Toast.LENGTH_SHORT).show();
            try {
                db.execSQL(CREATE_TABLE);

            } catch (SQLException e) {
                e.printStackTrace();
                //Log.e("Exception ",e.toString());
                Toast.makeText(context,"Exception on create : "+e.toString(),Toast.LENGTH_SHORT).show();
            }

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Toast.makeText(context,"OnUpgrade",Toast.LENGTH_SHORT).show();
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(context,"Exception on upgrade : "+e.toString(),Toast.LENGTH_SHORT).show();
            }


        }
    }

}
