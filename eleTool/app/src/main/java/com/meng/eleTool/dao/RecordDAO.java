package com.meng.eleTool.dao;

import android.R.bool;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import com.meng.eleTool.model.Model;
import com.meng.eleTool.model.Record;

/**
 * Created by SJF on 2023/2/7.
 */

public class RecordDAO {
	private DBOpenHelper helper;
	private SQLiteDatabase db;

	public RecordDAO(DBOpenHelper helper) {
		this.helper = helper;
	}

	public void save(Record record) {
		if (isIdExist(record._id)) {
			update(record);
		} else {
			add(record);
		}
	}

	private void add(Record record) {
		db = helper.getWritableDatabase();
		db.execSQL("insert into tb_record (_id,model_id,io,nums,_date) values (null,?,?,?,?)",
				new Object[] { record.model_id, record.io, record.nums, record.date });
	}

	private void update(Record record) {
		db = helper.getWritableDatabase();
		db.execSQL("update tb_record set model_id = ? , io = ? , nums = ? , _date = ? where _id = ?",
				new Object[] { record.model_id, record.io, record.nums, record.date, record._id });
	}

	public Record get(int id) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from tb_record where _id = ?", new String[] { String.valueOf(id) });
		if (cursor.moveToNext()) {
			return new Record(id, cursor.getInt(cursor.getColumnIndex("model_id")),
					cursor.getString(cursor.getColumnIndex("io")), cursor.getInt(cursor.getColumnIndex("nums")),
					cursor.getString(cursor.getColumnIndex("_date")));
		}
		return null;
	}

	public boolean isIdExist(int id) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from tb_record where _id = ?", new String[] { String.valueOf(id) });
		return cursor.moveToNext();
	}

	public List<Record> gets(int start, int count) {
		List<Record> elementList = new ArrayList<Record>();
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from tb_record limit ?,?",
				new String[] { String.valueOf(start), String.valueOf(count) });
		while (cursor.moveToNext()) {
			elementList.add(new Record(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("model_id")), cursor.getString(cursor.getColumnIndex("io")),
					cursor.getInt(cursor.getColumnIndex("nums")), cursor.getString(cursor.getColumnIndex("_date"))));
		}
		return elementList;
	}

	public List<Record> getAll() {
		return gets(0, (int) getCount());
	}

	public long getCount() {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(_id) from tb_record", null);
		if (cursor.moveToNext()) {
			return cursor.getLong(0);
		}
		return 0;
	}

	public int getMaxId() {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select max(_id) from tb_record", null);
		while (cursor.moveToLast()) {
			return cursor.getInt(0);
		}
		return 0;
	}
}
