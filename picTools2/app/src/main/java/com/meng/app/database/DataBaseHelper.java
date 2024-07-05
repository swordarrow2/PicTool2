package com.meng.app.database;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

import java.util.*;

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

    private SQLiteOpenHelper sqLiteOpenHelper;

    abstract SQLiteOpenHelper initSQLiteOpenHelper(Context context);

    public final void init(Context context) {
        if (sqLiteOpenHelper != null) {
            throw new IllegalStateException("dbHelper has already init.");
        }
        sqLiteOpenHelper = initSQLiteOpenHelper(context);
    }

    final SQLiteOpenHelper getSQLiteOpenHelper() {
        return sqLiteOpenHelper;
    }

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


    long updateData(String tableName, String colName, String value, ContentValues values) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.update(tableName, values, colName + "=?", new String[]{value});
    }

}

