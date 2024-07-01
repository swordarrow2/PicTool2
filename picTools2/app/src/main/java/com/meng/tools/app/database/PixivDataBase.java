package com.meng.tools.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PixivDataBase extends DataBaseHelper {

    /*
     *@author 清梦
     *@date 2024-07-01 15:54:50
     */
    public static final String TAG = "PixivDataBase";
    private final String DATABASE_NAME = "pixiv_record";
    private final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "op_log";


    @Override
    public SQLiteOpenHelper initSQLiteOpenHelper(Context context) {
        if (getSQLiteOpenHelper() != null) {
            return getSQLiteOpenHelper();
        }
        return new SQLiteOpenHelper(context, Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME + ".db", null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table " + TABLE_NAME + "( _id integer primary key autoincrement , _op varchar(200),_time long)");
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


    public long addFailed(String picture) {
        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_op", picture);
        values.put("_time", Calendar.getInstance(Locale.CHINA).getTimeInMillis());
        return db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<String> getAllFailed() {
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = getSQLiteOpenHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        int index = cursor.getColumnIndex("_op");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(cursor.getString(index));

        }
        cursor.close();
        db.close();
        return result;
    }

    public void deleteFailed(String pixivId) {
        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{pixivId});
    }

}
