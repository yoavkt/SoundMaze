package com.example.soundmaze;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHalper extends SQLiteOpenHelper {

	public SqlHalper(Context context) {
		super(context, "users_db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table users ( _id integer primary key autoincrement,"
				+  " name text, score integer );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Nothing to do.
	}

}