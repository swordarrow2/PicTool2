package com.meng.tools.app.database;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;
import android.util.*;

import java.util.*;

public class DebugDataBase extends DataBaseHelper {

    /*`
     *@author 清梦
     *@date 2024-07-01 15:54:50
     */
    public static final String TAG = "DebugDataBase";
    private final String DATABASE_NAME = "debug_log";
    private final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "op_log";


    @Override
    public SQLiteOpenHelper initSQLiteOpenHelper(Context context) {
        return new SQLiteOpenHelper(context, Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME + ".db", null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists " + TABLE_NAME + "( _id integer primary key autoincrement , _time long, _op varchar(512))");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
    }


    public long addOperate(String _op) {
        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_op", _op);
        values.put("_time", Calendar.getInstance(Locale.CHINA).getTimeInMillis());
        return db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<Pair<String, String>> getAllLog() {
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        SQLiteDatabase db = getSQLiteOpenHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(new Pair<>(String.valueOf(cursor.getLong(1)), cursor.getString(2)));
        }
        cursor.close();
        db.close();
        return result;
    }

}
