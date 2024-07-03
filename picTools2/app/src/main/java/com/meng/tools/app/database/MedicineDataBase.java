package com.meng.tools.app.database;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.os.*;

import com.meng.app.*;

import java.io.*;
import java.util.*;

public class MedicineDataBase extends DataBaseHelper {

    /*`
     *@author 清梦
     *@date 2024-07-01 15:54:50
     */
    public static final String TAG = "BoxDataBase";
    private final String DATABASE_NAME = "box_record";
    private final int DATABASE_VERSION = 1;
    private final String TABLE_DATA_MAIN = "data_main";// id , name , describe , slot id , picture file

    @Override
    public SQLiteOpenHelper initSQLiteOpenHelper(Context context) {
        return new SQLiteOpenHelper(context, Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME + ".db", null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists " + TABLE_DATA_MAIN +
                        "( _id integer primary key autoincrement , _name varchar(200), _describe varchar(2048) , _slot_id integer , _picture_file mediumblob);");
                db.execSQL("create table if not exists bind( _id integer primary key , _sn varchar(200))");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
    }

    public void bindMachine(String sn) {
        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
        db.delete("bind", "_id = 1 ", null);
        ContentValues cv = new ContentValues();
        cv.put("_id", 1);
        cv.put("_sn", sn);
        insertData("bind", cv);
    }

    public String getBindingMachine() {
        SQLiteDatabase db = getSQLiteOpenHelper().getReadableDatabase();
        try (Cursor cursor = db.rawQuery("select _sn from bind where _id = 1", null)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getString(0);
            }
        }
        return null;
    }

    public void addMedicine(String name, String describe, int slotId, byte[] pictureFile) {
        ContentValues cv = new ContentValues();
        cv.put("_name", name);
        cv.put("_describe", describe);
        cv.put("_slot_id", slotId);
        if (pictureFile != null && pictureFile.length > 1_048_576) {
            MainActivity.instance.showToast("picture too large " + pictureFile.length + "B,compress.");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeByteArray(pictureFile, 0, pictureFile.length);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            cv.put("_picture_file", baos.toByteArray());
        } else {
            cv.put("_picture_file", pictureFile);
        }
        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
        db.insert(TABLE_DATA_MAIN, null, cv);

    }

    public ArrayList<Medicine> getAllMedicine() {
        ArrayList<Medicine> result = new ArrayList<>();
        SQLiteDatabase db = getSQLiteOpenHelper().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_DATA_MAIN, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(new Medicine(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getBlob(4)));
        }
        cursor.close();
        db.close();
        return result;
    }

    public long updateMedicine(int id, String name, String describe, int slotId, byte[] pictureFile) {
        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_name", name);
        cv.put("_describe", describe);
        cv.put("_slot_id", slotId);
        cv.put("_picture_file", pictureFile);
        return db.update(TABLE_DATA_MAIN, cv, " _id= " + id, null);
    }

    public void deleteMedicine(int id) {
        SQLiteDatabase db = getSQLiteOpenHelper().getWritableDatabase();
        db.delete(TABLE_DATA_MAIN, "_id= " + id, null);
    }


    public static class Medicine {
        public int id;
        public String name;
        public String describe;
        public int slotId;
        public byte[] picture;

        public Medicine(int id, String name, String describe, int slotId, byte[] picture) {
            this.id = id;
            this.name = name;
            this.describe = describe;
            this.slotId = slotId;
            this.picture = picture;
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != Medicine.class) {
                return false;
            }
            return id == ((Medicine) o).id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

}
