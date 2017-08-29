package shadattonmoy.navigationdrawer;

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
    public long insertTeacher(SQLiteDatabase db,String dept,String name,String designation, String room, String phone, String email,String fb)
    {
        //db = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.DEPT,dept);
        contentValues.put(SQLiteHelper.TEACHER_NAME,name);
        contentValues.put(SQLiteHelper.TEACHER_DESIGNATION,designation);
        contentValues.put(SQLiteHelper.TEACHER_ROOM,room);
        contentValues.put(SQLiteHelper.TEACHER_PHONE,phone);
        contentValues.put(SQLiteHelper.TEACHER_EMAIL,email);
        contentValues.put(SQLiteHelper.TEACHER_FB,fb);
        long id = db.insert(SQLiteHelper.TABLE_NAME,null,contentValues);
        return id;

    };

    public Cursor getTeacherRecord(String dept)
    {
        String [] columns = {SQLiteHelper.ID,SQLiteHelper.DEPT,SQLiteHelper.TEACHER_NAME,SQLiteHelper.TEACHER_DESIGNATION,SQLiteHelper.TEACHER_ROOM,SQLiteHelper.TEACHER_PHONE,SQLiteHelper.TEACHER_EMAIL,SQLiteHelper.TEACHER_FB};
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String selection = SQLiteHelper.DEPT+"=?";
        String[] selectionArgs = {dept};
        Cursor cursor = db.query(SQLiteHelper.TABLE_NAME,columns,selection,selectionArgs,null,null,null);

        return cursor;
    }
    public class SQLiteHelper extends SQLiteOpenHelper{

        static final String DB_NAME = "database";
        static final int DB_VERSION = 17;
        static final String TABLE_NAME = "teachers";
        static final String ID = "_id";
        static final String DEPT = "dept";
        static final String TEACHER_NAME = "name";
        static final String TEACHER_DESIGNATION = "designation";
        static final String TEACHER_ROOM = "room";
        static final String TEACHER_PHONE = "phone";
        static final String TEACHER_EMAIL = "email";
        static final String TEACHER_FB = "fb";
        static final String CREATE_TABLE = "create table "+TABLE_NAME +"("+ID +" INTEGER primary key autoincrement, "+DEPT+" varchar(50),"+ TEACHER_NAME +" varchar(500), "+ TEACHER_DESIGNATION +" varchar(500),"+ TEACHER_ROOM +" varchar(50),"+ TEACHER_PHONE +" varchar(100),"+ TEACHER_EMAIL +" varchar(100),"+ TEACHER_FB +" varchar(100))";
        private static final String DROP_TABLE = "drop table if exists "+TABLE_NAME+" ";



        public SQLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context,"OnCreate",Toast.LENGTH_SHORT).show();
            try {
                db.execSQL(CREATE_TABLE);
                insertTeacherData(db);

            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Exception ",e.toString());
            }

        }

        public void insertTeacherData(SQLiteDatabase db)
        {
            long id = insertTeacher(db,"CSE","Dr. Muhammad Zafar Iqbal","Professor","Room 324,IICT","N/A","mzi@sust.edu","N/A");



            id = insertTeacher(db,"CSE","Dr Mohammad Shahidur Rahman","Professor","Room 321,IICT","+8801914930807","rahmanms@sust.edu","N/A");


            id = insertTeacher(db,"CSE","Dr Mohammad Reza Selim","Professor","Room 320,IICT","+8801972357830","selim@sust.edu","N/A");

            id = insertTeacher(db,"CSE","M. Jahirul Islam,PhD.,PEng.","Professor","Room 321,IICT","N/A","jahir-cse@sust.edu","N/A");


            id = insertTeacher(db,"CSE","Md Masum","Associate Professor","N/A","+8801919736248","masum-cse@sust.edu","N/A");


            id = insertTeacher(db,"CSE","Mahruba Sharmin Chowdhury","Assistant Professor","N/A","+8801917566699","mahruba-cse@sust.edu","https://web.facebook.com/mahruba.chowdhury");


            id = insertTeacher(db,"CSE","Md Eamin Rahman","Assistant Professor","Room 212,IICT","+8801677014633","eamin-cse@sust.edu","N/A");


            id = insertTeacher(db,"CSE","Sabir Ismail","Assistant Professor","Room 211,IICT","+8801753350226","sabir-cse@sust.edu","https://web.facebook.com/sabir.ismail01");

            id = insertTeacher(db,"CSE","Md Saiful Islam","Assistant Professor","Room 214,IICT","+8801717960979","saiful-cse@sust.edu","https://web.facebook.com/saifulcse");



            id = insertTeacher(db,"CSE","Marium-E-Jannat","Assistant Professor","Room 210,IICT","+8801727354101","jannat-cse@sust.edu","https://web.facebook.com/jannatmarium.mukta");



            id = insertTeacher(db,"CSE","Sheikh Nabil Mohammad","Assistant Professor","Room 213,IICT","+8801678137313","nabil-cse@sust.edu","https://web.facebook.com/sknabil");



            id = insertTeacher(db,"CSE","Biswapriyo Chakrabarty","Lecturer","Room 207,IICT","+8801612300990","biswa-cse@sust.edu","https://web.facebook.com/bchakrabarty");



            id = insertTeacher(db,"CSE","Md. Mahfuzur Rahaman","Lecturer","Room 317,IICT","+8801717831156","mahfuz-cse@sust.edu","N/A");

            id = insertTeacher(db,"EEE","Biswajit Paul","Assistant Professor","Room 418,IICT","+8801712554955","biswajit-eee@sust.edu","N/A");

            id = insertTeacher(db,"EEE","Tuhin Dev","Assistant Professor","Room 412,IICT","+8801758464744","deytuhin-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Md Rasedujjaman","Assistant Professor","Room 412,IICT","+8801714557885","mrased-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Ifte Khairul Amin","Assistant Professor","Room 4o*,IICT","+8801911034624","iftekhar-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Mehedhi Hasan","Lecturer","Room 40*,IICT","+8801912237361","mhasan-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Jawata Afnan","Lecturer","Room 40*,IICT","+8801717259325","jawatafnan-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Mohammad Kamruzzaman Khan Prince","Lecturer","Room 411,IICT","+8801737327023","kzaman-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Jibesh Kanti Saha","Lecturer","Room 419,IICT","+8801680003287","jibesh-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Md. Mohsinur Rahman Adnan","Lecturer","Room 417,IICT","+8801916422526","mmradnan-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Arif Ahammad","Lecturer","Room 418,IICT","+8801720122789","arif-eee@sust.edu","N/A");
            id = insertTeacher(db,"EEE","Riashad Siddque","Lecturer","Room 418,IICT","+8801684627024","riashad-eee@sust.edu","N/A");

            id = insertTeacher(db,"CEP","Dr. Md. Akhtarul Islam","Professor","N/A","+8801821714479","mislam-eee@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Dr. Md. Mastabur Rahman","Professor","N/A","+8801821713850","mrahmanbsb-eee@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Dr. Engr. Salma Akhter","Professor","N/A","+880152438452","salma-eee@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Dr. Abu Yousuf","Professor","N/A","+8801711904480","ayousuf-eee@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Dr. Md. Abdul Mumin","Associate Professor","N/A","N/A","N/A","N/A");
            id = insertTeacher(db,"CEP","Dr. Salatul Islam Mozumder","Associate Professor","N/A","+8801914201907","salatuislam-eee@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Dr. Kazi Farida Akhter","Associate Professor","N/A","+8801758464744","N/A","N/A");
            id = insertTeacher(db,"CEP","Dr. Muhammad Nurunnabi Siddiquee","Associate Professor","N/A","N/A","nsiddiquee-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Md. Mohibul Alam","Associate Professor","N/A","+8801821714479","mmalam-cep@yahoo.com","N/A");
            id = insertTeacher(db,"CEP","Md. Zakir Hossain","Associate Professor","N/A","+8801821714479","zakip-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Muhammad Zobayer Bin Mukhlish","Assistant Professor","N/A","N/A","zobayer-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Md. Fakar Uddin","Assistant Professor","N/A","+8801557474719","mfuddin-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Pradip Saha","Assistant Professor","N/A","+8801718348524","pradip-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Mohammad Shaiful Alam Amin","Assistant Professor","N/A","+8801717075267","masaamin-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Sreejon Das","Assistant Professor","N/A","+8801821714479","sreejon-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Muhammad Abdus Salam","Assistant Professor","N/A","+8801816040562","salam-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Mohammad Rakib Uddin","Assistant Professor","N/A","+8801821714479","rakib-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Niloy Chandra Sarker","Assistant Professor","N/A","+8801821714479","niloy-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Dr. Md. Mostafizur Rahman","Assistant Professor","N/A","+8801821714479","mostafiz-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Mitun Chandra Bhoumick","Assistant Professor","N/A","+8801821714479","mcbhoumick-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Humayun Ahmad","Assistant Professor","N/A","+8801722762615","humayun-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Rahatun Akter","Assistant Professor","N/A","+8801724274932","rahatun-cep@sust.edu","N/A");
            id = insertTeacher(db,"CEP","Md. Shahadat Hossain","Lecturer","N/A","+8801924844809","shahadat_cep43@yahoo.com","N/A");
            id = insertTeacher(db,"CEP","Ms. Maleeha Matin","Lecturer","N/A","+8801674676266","maleehamatin@yahoo.com","N/A");

            id = insertTeacher(db,"IPE","Dr. Egnr. Mohammad Iqbal","Professor","N/A","+8801758464744","muhammad.hasan-ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Dr. Abul Mukid Mohammad Mukaddes","Professor","N/A","+8801758464744",",mithu-ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Dr. Md. Ariful Islam","Professor","N/A","+8801758464744","abmmalek@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Dr. Mohammad Muhshin Aziz Khan","Professor","N/A","+8801758464744","ahmedsayem02@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Dr. Muhammad Mahamood Hasan","Professor","N/A","+8801758464744","misbah-ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Dr. Md. Abu Hayat Mithu,PhD","Associate Professor","N/A","+8801758464744","rashed-ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Engr A B M Abdul Malek","Associate Professor","N/A","+8801758464744","basitchy_23@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Ahmed Sayem","Associate Professor","N/A","+8801758464744","anis134ipe@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Syed Misbah Uddin","Associate","N/A","+8801758464744","nasima_ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Dr. Choudhury Abul Anam Rashed","Assistant Professor","N/A","+8801758464744","samad_ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Chowdhury Md. Luthfur Rahman","Assistant Professor","N/A","+8801758464744","shumon330@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Md. Anisul Islam","Assistant Professor","N/A","+8801758464744","ssdsust@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Nasima Bagum","Assistant Professor","N/A","+8801758464744","karim-ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Muhammad Abdus Shumon","Assistant Professor","N/A","+8801758464744","salman-ipe@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Md. Rezaul Hasan Shumon","Assistant Professor","N/A","+8801758464744","deytuhin-eee@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Shuchisnigdha Deb","Assistant Professor","N/A","+8801758464744","deytuhin-eee@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Engr Mohammed Abdul Karim","Assistant Professor","N/A","+8801758464744","deytuhin-eee@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Ahmed Salman Imtiaz","Assistant Professor","N/A","+8801758464744","deytuhin-eee@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Shanta Saha","Assistant Professor","N/A","+8801758464744","santa-ipe@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Syeda Kumrun Nahar","Assistant Professor","N/A","+8801758464744","aurjoma@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Jahid Hasan","Assistant Professor","N/a","+8801758464744","j.hasan.ipe@gmail.com","N/A");

            id = insertTeacher(db,"IPE","Md. Mostafizur Rahman","Associate Professor","N/A","1711324590","mustafiz_su@yahoo.com\t","N/A");
            id = insertTeacher(db,"IPE","Iftekhar Rahman","Associate Professor","N/A"," 01712127544","mustafiz_su@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","K Taufiq Elahi","Associate Professor","N/A","1711120218","ifti48buet@gmail.com, ifti48buet-arc@sust.edu","N/A");
            id = insertTeacher(db,"IPE","S M Zahed Sarwar","Assistant Professor","N/A","1786128656","ktaufiqelahi@yahoo.com, elahi-arc@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Rezwan Sobhan","Assistant Professor","N/A","1711275588","dreesharchitects@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Shubhajit Chowdhury","Assistant Professor","N/A","1715224262","arc.sobhan@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Mohammad Shamsul Arefin","Assistant Professor","N/A","1751731166","shubha_arch@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Afrina Haque","Assistant Professor","N/A","1719482862","msanarch@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Kawshik Saha","Assistant Professor","N/A","1926950016","ar.afrina@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Subrata Das","Assistant Professor","N/A","1712852564","kawshik.saha@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Gourpada Dey","Assistant Professor","N/A","1719334997","ar.subrata@yahoo.com","N/A");
            id = insertTeacher(db,"IPE","Mohammad Tanvir Hasan","Assistant Professor","N/A","1676710551","tan.arc14@.gmail.com","N/A");
            id = insertTeacher(db,"IPE","Hossain Mohammad Nahyan","Assistant Professor","N/A","1752857857","ar.nahyan@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Shahidul Islam","Assistant Professor","N/A","1748553090","ar.shahidulislam@gmail.com, shahidul-arc@sust.edu","N/A");
            id = insertTeacher(db,"IPE","Gourpada Dey","Assistant Professor","N/A","88 01717362528","ar.robindey@gmail.com, gourpadadey-arc@sust.edu;","N/A");
            id = insertTeacher(db,"IPE","Rupak Dash","Lecturer","N/A","88 01768-000999","ar.rupak.sust@gmail.com","N/A");
            id = insertTeacher(db,"IPE","Shahla Safwat Ravhee","Lecturer","N/A","88 01675868645","rvi.arc@gmail.com","N/A");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Toast.makeText(context,"OnUpgrade",Toast.LENGTH_SHORT).show();
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

}
