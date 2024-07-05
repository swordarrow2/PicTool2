package com.meng.app.database;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;

import com.meng.app.*;

public class BoxDataBase extends DataBaseHelper {

    /*`
     *@author 清梦
     *@date 2024-07-01 15:54:50
     */
    public static final String TAG = "BoxDataBase";
    private final String DATABASE_NAME = "box_record";
    private final int DATABASE_VERSION = 1;
    private final String TABLE_OPERATE_LOG = "_op_log";//opid , time , action , thing id , count
    private final String TABLE_DATA_MAIN = "_data_main";//thing id , name , describe , slot id , picture file , count


    @Override
    public SQLiteOpenHelper initSQLiteOpenHelper(Context context) {
        MainActivity.instance.showToast("init");
        return new SQLiteOpenHelper(context, Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME + ".db", null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists " + TABLE_OPERATE_LOG +
                        "( _opid integer primary key autoincrement , _time long , _action varchar(200) , _thing_id integer , _count integer);");
                db.execSQL("create table if not exists " + TABLE_DATA_MAIN +
                        "( _thing_id integer primary key autoincrement , _name varchar(200), _describe varchar(1024) , _slot_id integer , _picture_file mediumblob);");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
    }

    public void addOpLog(int medicineId, String action, int count) {
        ContentValues cv = new ContentValues();
        cv.put("_thing_id", medicineId);
        cv.put("_time", System.currentTimeMillis());
        cv.put("_action", action);
        cv.put("_count", count);
        insertData(TABLE_OPERATE_LOG, cv);
    }

    public int getResidueMedicine(int id) {
        SQLiteDatabase db = getSQLiteOpenHelper().getReadableDatabase();
        try (Cursor cursor = db.rawQuery("select count from " + TABLE_DATA_MAIN + " where _thing_id = " + id, null)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        }
        return -1;
    }

    public int getResidueMedicine(String name) {
        SQLiteDatabase db = getSQLiteOpenHelper().getReadableDatabase();
        try (Cursor cursor = db.rawQuery("select count from " + TABLE_DATA_MAIN + " where _name = " + name, null)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(1);
            }
        }
        return -1;
    }
}
