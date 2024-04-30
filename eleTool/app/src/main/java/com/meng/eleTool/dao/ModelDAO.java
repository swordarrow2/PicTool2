package com.meng.eleTool.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.meng.eleTool.model.Model;

/**
 * Created by SJF on 2023/2/7.
 */

public class ModelDAO {
	private DBOpenHelper helper;
	private SQLiteDatabase db;

	public ModelDAO(DBOpenHelper helper) {
		this.helper = helper;
	}

	public void save(Model model) {
		if (isNameExist(model.name)) {
			update(model);
		} else {
			add(model);
		}
	}

	private void add(Model element) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"insert into tb_model (_id,model_name,packaging,model_type,print,_from,note) values (null,?,?,?,?,?,?)",
				new Object[] { element.name, element.pack, element.type, element.print, element.from, element.note });
	}

	private void update(Model element) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update tb_model set model_name = ?,packaging = ?,model_type = ?,print = ?,_from = ?,note = ? where _id = ?",
				new Object[] { element.name, element.pack, element.type, element.print, element.from, element.note,
						element.id });
	}

	public boolean isNameExist(String modelName) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from tb_model where model_name = ?", new String[] { modelName });
		return cursor.moveToNext();
	}

	public Model get(String name) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from tb_model where model_name = ?", new String[] { name });
		if (cursor.moveToNext()) {
			return new Model(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("model_name")),
					cursor.getString(cursor.getColumnIndex("packaging")),
					cursor.getString(cursor.getColumnIndex("model_type")),
					cursor.getString(cursor.getColumnIndex("print")), cursor.getString(cursor.getColumnIndex("_from")),
					cursor.getString(cursor.getColumnIndex("note")));
		}
		return null;
	}

	public Model get(int id) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from tb_model where _id = ?", new String[] { String.valueOf(id) });
		if (cursor.moveToNext()) {
			return new Model(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("model_name")),
					cursor.getString(cursor.getColumnIndex("packaging")),
					cursor.getString(cursor.getColumnIndex("model_type")),
					cursor.getString(cursor.getColumnIndex("print")), cursor.getString(cursor.getColumnIndex("_from")),
					cursor.getString(cursor.getColumnIndex("note")));
		}
		return null;
	}

	public List<Model> gets(int start, int count) {
		List<Model> elementList = new ArrayList<Model>();
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from tb_model limit ?,?",
				new String[] { String.valueOf(start), String.valueOf(count) });
		while (cursor.moveToNext()) {
			elementList.add(new Model(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("model_name")),
					cursor.getString(cursor.getColumnIndex("packaging")),
					cursor.getString(cursor.getColumnIndex("model_type")),
					cursor.getString(cursor.getColumnIndex("print")), cursor.getString(cursor.getColumnIndex("_from")),
					cursor.getString(cursor.getColumnIndex("note"))));
		}
		return elementList;
	}

	public List<Model> getAll() {
		return gets(0, (int) getCount());
	}

	public long getCount() {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(_id) from tb_model ", null);
		if (cursor.moveToNext()) {
			return cursor.getLong(0);
		}
		return 0;
	}

	public int getMaxId() {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select max(_id) from tb_model ", null);
		while (cursor.moveToLast()) {
			return cursor.getInt(0);
		}
		return 0;
	}
}
