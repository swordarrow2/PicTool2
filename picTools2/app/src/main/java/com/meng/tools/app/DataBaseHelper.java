package com.meng.tools.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SJF on 2024/6/30.
 */

public class DataBaseHelper {

    private static HashMap<DatabaseName, DataBaseHelper> databaseMap = new HashMap<>();

    public static DataBaseHelper getInstance(DatabaseName name) {
        DataBaseHelper helper = databaseMap.get(name);
        if (helper == null) {
            helper = new DataBaseHelper();
            databaseMap.put(name, helper);
        }
        return helper;
    }

    private String tableName;
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

    public void init(Context context, final DatabaseName name, final String tableName, final String primaryKey, final LinkedHashMap<String, String> ColAndType) {
        if (sqLiteOpenHelper != null) {
            throw new IllegalStateException("dbHelper has already init.");
        }
        this.tableName = tableName;
        sqLiteOpenHelper = new SQLiteOpenHelper(context, Environment.getExternalStorageDirectory() + "/" + name.getName() + ".db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, String> entry : ColAndType.entrySet()) {
                    builder.append(", ").append(entry.getKey()).append(" ").append(entry.getValue()).append(" ");
                }
                builder.append(")");
                db.execSQL("create table " + tableName + "( " +
                        primaryKey + " integer primary key autoincrement"
                        + builder.toString());
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                //   db.execSQL("drop table record");
                //onCreate(db);
            }
        };
    }

    public ArrayList<String> searchFailedPic() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    //插入一条数据
    public long insertData(ContentValues values) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.insert(tableName, null, values);
    }

    //根据主键删除某条记录
    public void deleteData(String colName, String value) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.delete(tableName, colName + "=?", new String[]{value});
    }

    //查询数据，返回一个Cursor
    public Cursor query() {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        return db.rawQuery("select * from " + tableName, null);
    }

    public ArrayList<ContentValues> getAllData() {
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


    public long updateData(String colName, String value, ContentValues values) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.update("record", values, colName + "=?", new String[]{value});
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

