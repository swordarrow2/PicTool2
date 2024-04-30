package com.meng.eleTool.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DBNAME = "elements.db";
    
    public DBOpenHelper(Context context) {
        super(context, context.getExternalFilesDir("../../../../") + File.separator + DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table tb_record (_id integer primary key auto_increment,model_id integer,io varchar(1),nums integer,_date date NOT NULL DEFAULT '0000-00-00')");
        db.execSQL("create table tb_model (_id integer primary key auto_increment,model_name varchar(200),packaging varchar(200),model_type varchar(200),print varchar(200),_from varchar(20) NOT NULL DEFAULT '',note varchar(200))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //DO NOT add break
        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE tb_model ADD COLUMN _from varchar(20) NOT NULL DEFAULT ''");
            case 2:
     //           db.execSQL( "date NOT NULL DEFAULT '0000-00-00'");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
    
}
