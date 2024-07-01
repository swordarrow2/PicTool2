package com.meng.tools.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SJF on 2024/6/30.
 */

public abstract class DataBaseHelper {

    private static HashMap<Class<? extends DataBaseHelper>, DataBaseHelper> databaseMap = new HashMap<>();

    public static <T extends DataBaseHelper> T getInstance(Class<T> clazz) {
        DataBaseHelper helper = databaseMap.get(clazz);
        if (helper == null) {
            try {
                helper = clazz.newInstance();
                databaseMap.put(clazz, helper);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (T) helper;
    }


    //表名
//    private final String TABLE_NAME = "record";//"failedPic";
//    //表的主键
//    private final String KEY_ID = "_id";
//    private final String ID = "pixivId";
    //创建一个表的sql语句
//    private final String sql = "create table "
//            + TABLE_NAME + "( " + KEY_ID
//            + " integer primary key autoincrement,"
//            + ID + " text)";
    private SQLiteOpenHelper sqLiteOpenHelper;

    public abstract SQLiteOpenHelper initSQLiteOpenHelper(Context context);

    public void init(Context context) {
        if (sqLiteOpenHelper != null) {
            throw new IllegalStateException("dbHelper has already init.");
        }
        sqLiteOpenHelper = initSQLiteOpenHelper(context);

    }

    public final SQLiteOpenHelper getSQLiteOpenHelper() {
        return sqLiteOpenHelper;
    }

//    public ArrayList<String> searchFailedPic() {
//        ArrayList<String> arrayList = new ArrayList<>();
//        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + tableName, null);
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            arrayList.add(cursor.getString(1));
//        }
//        cursor.close();
//        db.close();
//        return arrayList;
//    }

    //插入一条数据
    public long insertData(String tableName, ContentValues values) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.insert(tableName, null, values);
    }

    //根据主键删除某条记录
    public void deleteData(String tableName, String colName, String value) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.delete(tableName, colName + "=?", new String[]{value});
    }

    //查询数据，返回一个Cursor
    public Cursor query(String tableName) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        return db.rawQuery("select * from " + tableName, null);
    }

    public ArrayList<ContentValues> getAllData(String tableName) {
        ArrayList<ContentValues> result = new ArrayList<>();
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ContentValues cv = new ContentValues();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                cv.put(cursor.getColumnName(i), cursor.getString(i));
            }
            result.add(cv);
        }
        cursor.close();
        db.close();
        return result;
    }


    public long updateData(String tableName, String colName, String value, ContentValues values) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.update(tableName, values, colName + "=?", new String[]{value});
    }

    public enum DatabaseName {

        NAME_PIXIV("pixiv_record"),
        NAME_MEDICINE("medicine_record"),
        NAME_VOICE("voice_history");

        private String name;

        DatabaseName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

