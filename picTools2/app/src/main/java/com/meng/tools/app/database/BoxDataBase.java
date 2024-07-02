package com.meng.tools.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.meng.app.MainActivity;

public class BoxDataBase extends DataBaseHelper {

    /*`
     *@author 清梦
     *@date 2024-07-01 15:54:50
     */
    public static final String TAG = "BoxDataBase";
    private final String DATABASE_NAME = "box_record";
    private final int DATABASE_VERSION = 1;
    private final String TABLE_OPERATE_LOG = "_op_log";//opid , time , action , thing id , count
    private final String TABLE_DATA_MAIN = "_data_main";//thing id , name , describe , slot id , picture link


    @Override
    public SQLiteOpenHelper initSQLiteOpenHelper(Context context) {
        MainActivity.instance.showToast("init");
        return new SQLiteOpenHelper(context, Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME + ".db", null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table " + TABLE_OPERATE_LOG +
                        "( _opid integer primary key autoincrement , _time long , _action varchar(200) , _thing_id integer , _count integer);");
               // db.execSQL("create table " + TABLE_DATA_MAIN +
               //         "( _thing_id integer primary key autoincrement , _name varchar(200),_describe varchar(1024) , _slot_id integer , _picture_link varchar(512));");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                switch (newVersion) {
                    case 2:
                        //
                    case 3:
                        //
                    default:
                        break;
                }
                //   db.execSQL("drop table record");
                //onCreate(db);
            }
        };
    }

//
//    public long addFailed(String picture) {
//        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("_op", picture);
//        values.put("_time", Calendar.getInstance(Locale.CHINA).getTimeInMillis());
//        return db.insert(TABLE_NAME, null, values);
//    }
//
//    public ArrayList<String> getAllFailed() {
//        ArrayList<String> result = new ArrayList<>();
//        SQLiteDatabase db = getSQLiteOpenHelper().getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
//        int index = cursor.getColumnIndex("_op");
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            result.add(cursor.getString(index));
//
//        }
//        cursor.close();
//        db.close();
//        return result;
//    }
//
//    public void deleteFailed(String pixivId) {
//        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
//        db.delete(TABLE_NAME, "_id=?", new String[]{pixivId});
//    }

}
