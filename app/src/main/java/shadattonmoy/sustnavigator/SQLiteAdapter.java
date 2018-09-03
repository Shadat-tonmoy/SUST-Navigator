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
import java.util.List;

import shadattonmoy.sustnavigator.utils.Values;

/**
 * Created by Shadat Tonmoy on 7/9/2017.
 */

public class SQLiteAdapter {


    SQLiteHelper sqLiteHelper = null;
    Context context;
    private  static  SQLiteAdapter instance;

    public static synchronized SQLiteAdapter getInstance(Context context)
    {
        if(instance==null)
            instance = new SQLiteAdapter(context);
        return instance;
    }

    private SQLiteAdapter(Context context)
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

    public long addCourse(Course course,String semesterCode)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COURSE_CODE,course.getCourse_code());
        contentValues.put(SQLiteHelper.COURSE_TITLE,course.getCourse_title());
        contentValues.put(SQLiteHelper.COURSE_CREDIT,course.getCourse_credit());
        contentValues.put(SQLiteHelper.COURSE_SEMESTER,semesterCode);
        long id = db.insert(SQLiteHelper.COURSE,null,contentValues);
        return id;

    };

    public long addSemester(String semesterCode)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.SEMESTER_CODE,semesterCode);
        long id = db.insert(SQLiteHelper.SEMESTER_TABLE,null,contentValues);
        return id;

    };

    public List<Course> getCourses(String semesterCode)
    {
        List<Course> courses = new ArrayList<>();
        String [] columns = {SQLiteHelper.COURSE_ID,SQLiteHelper.COURSE_SEMESTER,SQLiteHelper.COURSE_CODE,SQLiteHelper.COURSE_TITLE,SQLiteHelper.COURSE_CREDIT,SQLiteHelper.COURSE_DETAIL};
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String selection = SQLiteHelper.COURSE_SEMESTER+"=?";
        String[] selectionArgs = {semesterCode};
        Cursor cursor = db.query(SQLiteHelper.COURSE,columns,selection,selectionArgs,null,null,null);

        while (cursor.moveToNext()) {

            int indexOfCourseID = cursor.getColumnIndex(sqLiteHelper.COURSE_ID);
            int indexOfCourseCode= cursor.getColumnIndex(sqLiteHelper.COURSE_CODE);
            int indexOfCourseTitle = cursor.getColumnIndex(sqLiteHelper.COURSE_TITLE);
            int indexOfCourseCredit = cursor.getColumnIndex(sqLiteHelper.COURSE_CREDIT);
            int indexOfCourseSemester = cursor.getColumnIndex(sqLiteHelper.COURSE_SEMESTER);
            int indexOfCourseDetails = cursor.getColumnIndex(sqLiteHelper.COURSE_DETAIL);

            long courseID = cursor.getInt(indexOfCourseID);
            String courseCode = cursor.getString(indexOfCourseCode);
            String courseTitle = cursor.getString(indexOfCourseTitle);
            String courseCredit = cursor.getString(indexOfCourseCredit);
            String courseSemester = cursor.getString(indexOfCourseSemester);
            String courseDetails = cursor.getString(indexOfCourseDetails);
            Course course = new Course(courseCode,courseTitle,courseCredit);
            course.setCourse_id(String.valueOf(courseID));
            course.setCourseDetail(courseDetails);
            courses.add(course);
        }
        return courses;
    }

    public List<String> getSemesters()
    {
        List<String> semesters = new ArrayList<>();
        String [] columns = {SQLiteHelper.SEMESTER_CODE};
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String orderBy = sqLiteHelper.SEMESTER_CODE+" ASC ";
        Cursor cursor = db.query(SQLiteHelper.SEMESTER,columns,null,null,null,null,orderBy);

        while (cursor.moveToNext()) {

            int indexOfSemesterCode = cursor.getColumnIndex(sqLiteHelper.SEMESTER_CODE);

            String semesterCode = cursor.getString(indexOfSemesterCode);
            semesters.add(semesterCode);
        }
        return semesters;

    }

    public Cursor getGPARecord(String[] semesters)
    {
        String [] columns = {SQLiteHelper.ID,SQLiteHelper.SEMESTER,SQLiteHelper.COURSE_CODE,SQLiteHelper.COURSE_TITLE,SQLiteHelper.COURSE_CREDIT,SQLiteHelper.GRADE,SQLiteHelper.IS_ADDED};
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String selection = SQLiteHelper.SEMESTER+"=?";
        String[] selectionArgs = semesters;
        if(semesters.length>1)
        {
            for(int i=1;i<=semesters.length-1;i++)
            {
                selection+=" OR "+sqLiteHelper.SEMESTER+"=?";
            }
        }
        for(String selectionarg:selectionArgs)
            Log.e("Selections",selectionarg);
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

    public int updateCourse(Course course)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COURSE_CODE,course.getCourse_code());
        contentValues.put(SQLiteHelper.COURSE_TITLE,course.getCourse_title());
        contentValues.put(SQLiteHelper.COURSE_CREDIT,course.getCourse_credit());
        String whereClause = sqLiteHelper.COURSE_ID+"=?";
        String[] whereArgs = {course.getCourse_id()};
        int result = db.update(sqLiteHelper.COURSE,contentValues,whereClause,whereArgs);
        Log.e("UpdatedResult",course.toString());
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

    public int deleteSemester(String semester)
    {
//        deleteCourse(semester);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String whereClause = sqLiteHelper.SEMESTER_CODE+"=?";
        String semesterCode = Values.getSemesterCode(semester);
        String[] whereArgs = {semesterCode};
        int result = db.delete(sqLiteHelper.SEMESTER_TABLE,whereClause,whereArgs);
        Log.e("ResultOfDelete",result+" "+semesterCode+" "+semester);
        return result;
    }

    public int deleteCourse(String semester)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String whereClause = sqLiteHelper.SEMESTER_CODE+"=?";
        String[] whereArgs = {semester};
        int result = -1;
        try {
            result = db.delete(sqLiteHelper.COURSE,whereClause,whereArgs);
        }catch (Exception e)
        {
            e.printStackTrace();
            return result;
        }

        return result;
    }

    public int deleteSingleCourse(String courseId)
    {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String whereClause = sqLiteHelper.COURSE_ID+"=?";
        String[] whereArgs = {courseId};
        int result = -1;
        try {
            result = db.delete(sqLiteHelper.COURSE,whereClause,whereArgs);
        }catch (Exception e)
        {
            e.printStackTrace();
            return result;
        }
        Log.e("CourseDelete",courseId+" result "+result);
        return result;
    }
    public class SQLiteHelper extends SQLiteOpenHelper{

        static final String DB_NAME = "database";
        static final int DB_VERSION = 21;
        static final String TABLE_NAME = "cgpa";
        static final String COURSE = "course";
        static final String ID = "_id";
        static final String COURSE_ID = "course_id";
        static final String COURSE_DETAIL = "course_detail";
        static final String COURSE_SEMESTER= "course_semester";
        static final String SEMESTER = "semseter";
        static final String SEMESTER_TABLE = "semseter";
        static final String SEMESTER_ID= "semester_id";
        static final String SEMESTER_CODE = "semester_code";
        static final String COURSE_CODE = "course_code";
        static final String COURSE_TITLE = "course_title";
        static final String COURSE_CREDIT = "course_credit";
        static final String GRADE = "grade";
        static final String SEMESTER_INT = "semester_int";
        static final String IS_ADDED = "is_added";

        static final String CREATE_TABLE = "create table "+TABLE_NAME +"("+ID +" INTEGER primary key autoincrement, "+SEMESTER+" varchar(50),"+ COURSE_CODE+" varchar(500), "+ COURSE_TITLE+" varchar(500),"+ COURSE_CREDIT +" varchar(50),"+ GRADE +" varchar(100),"+IS_ADDED+" INTEGER DEFAULT 0)";

        static final String CREATE_SEMESTER_TABLE = "create table "+SEMESTER_TABLE+"("+SEMESTER_ID +" INTEGER primary key autoincrement, "+SEMESTER_CODE+" varchar(500)"+")";

        private static final String CREATE_COURSE_TABLE = "create table " + COURSE + "(" + COURSE_ID+ " INTEGER primary key autoincrement," + COURSE_TITLE + " varchar(255), " + COURSE_CODE+ " varchar(255)," + COURSE_CREDIT + " REAL," + COURSE_DETAIL+ " varchar(1000)," + COURSE_SEMESTER + " varchar(255));";

        private static final String DROP_TABLE = "drop table if exists "+TABLE_NAME+" ";
        private static final String DROP_COURSE_TABLE = "drop table if exists "+COURSE+" ";
        private static final String DROP_SEMESTER_TABLE = "drop table if exists "+SEMESTER+" ";



        public SQLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context,"OnCreate",Toast.LENGTH_SHORT).show();
            try {
                db.execSQL(CREATE_TABLE);
                db.execSQL(CREATE_COURSE_TABLE);
                db.execSQL(CREATE_SEMESTER_TABLE);

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
                db.execSQL(DROP_COURSE_TABLE);
                db.execSQL(DROP_SEMESTER_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(context,"Exception on upgrade : "+e.toString(),Toast.LENGTH_SHORT).show();
            }


        }
    }

}
