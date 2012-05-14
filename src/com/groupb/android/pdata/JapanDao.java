package com.groupb.android.pdata;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class JapanDao {

	SQLiteDatabase db;
	final String[] COLUMNS = { "rowid", "todofuken", "kentyo", "jinko",
			"menseki", "mitudo", "si", "ku", "tyo", "son" };

	// コンストラクタ
	JapanDao(SQLiteDatabase db) {
		this.db = db;
	}

	// データの挿入
	public long insert(JapanSetGet jsg) {

		//コンテンツバリューで値を保持する
		ContentValues cv = new ContentValues();

		cv.put("todofuken", jsg.getTodofuken());
		cv.put("kentyo", jsg.getKentyo());
		cv.put("jinko", jsg.getJinko());
		cv.put("menseki", jsg.getMenseki());
		cv.put("mitudo", jsg.getMitudo());
		cv.put("si", jsg.getSi());
		cv.put("ku", jsg.getKu());
		cv.put("tyo", jsg.getTyo());
		cv.put("son", jsg.getSon());

		//レコードを挿入する
		return db.insert("japan", null, cv);
	}

	// レコードの更新
	public int update(JapanSetGet jsg) {

		ContentValues cv = new ContentValues();

		cv.put("todofuken", jsg.getTodofuken());
		cv.put("kentyo", jsg.getKentyo());
		cv.put("jinko", jsg.getJinko());
		cv.put("menseki", jsg.getMenseki());
		cv.put("mitudo", jsg.getMitudo());
		cv.put("si", jsg.getSi());
		cv.put("ku", jsg.getKu());
		cv.put("tyo", jsg.getTyo());
		cv.put("son", jsg.getSon());

		// 更新する条件の作成
		String whereClause = "rowid = " + jsg.getRowid();

		// テーブルに対して更新する
		return db.update("japan", cv, whereClause, null);
	}

	// テーブルのレコード全てを取得する
	public List<JapanSetGet> findAll() {
		// リターンするリスト
		List<JapanSetGet> jsgList = new ArrayList<JapanSetGet>();
		// テーブルにあるレコードを全て取得する(rowid順)
		Cursor cursor = db.query("japan", COLUMNS, null, null, null, null,
				"rowid");
		// レコードが存在する間繰り返す
		while (cursor.moveToNext()) {
			// リストへ追加する
			JapanSetGet jsg = new JapanSetGet();

			jsg.setRowid(cursor.getInt(0));
			jsg.setTodofuken(cursor.getString(1));
			jsg.setKentyo(cursor.getString(2));
			jsg.setJinko(cursor.getInt(3));
			jsg.setMenseki(cursor.getDouble(4));
			jsg.setMitudo(cursor.getDouble(5));
			jsg.setSi(cursor.getInt(6));
			jsg.setKu(cursor.getInt(7));
			jsg.setTyo(cursor.getInt(8));
			jsg.setSon(cursor.getInt(9));

			jsgList.add(jsg);
		}
		return jsgList;
	}

	// rowidからテーブルの1列分のレコードを取得する
	public JapanSetGet findById(int rowid) {

		Cursor cursor = db.query("japan", COLUMNS, "rowid =" + rowid, null,
				null, null, null);

		while (cursor.moveToNext()) {
			JapanSetGet jsg = new JapanSetGet();
			jsg.setRowid(cursor.getInt(0));
			jsg.setTodofuken(cursor.getString(1));
			jsg.setKentyo(cursor.getString(2));
			jsg.setJinko(cursor.getInt(3));
			jsg.setMenseki(cursor.getDouble(4));
			jsg.setMitudo(cursor.getDouble(5));
			jsg.setSi(cursor.getInt(6));
			jsg.setKu(cursor.getInt(7));
			jsg.setTyo(cursor.getInt(8));
			jsg.setSon(cursor.getInt(9));

			return jsg;
		}
		return null;
	}

}
