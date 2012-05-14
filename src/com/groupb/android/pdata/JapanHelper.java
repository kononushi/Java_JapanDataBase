package com.groupb.android.pdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JapanHelper extends SQLiteOpenHelper{

private static final String TABLE_SQL = "create table japan "
	+"(rowid integer primary key autoincrement, "
	+"todofuken text not null,"
	+"kentyo text not null,"
	+"jinko integer not null,"
	+"menseki real not null,"
	+"mitudo real not null,"
	+"si integer not null,"
	+"ku integer not null,"
	+"tyo integer not null,"
	+"son integer not null)";

	public JapanHelper(Context context) {
		super(context, "japan", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_SQL);
	}

	public void onUpgrade(SQLiteDatabase db) {
		db.execSQL("drop table if exists japan");
		onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
