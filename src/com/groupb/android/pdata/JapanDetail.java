package com.groupb.android.pdata;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JapanDetail extends Activity {
	private EditText editText1;
	private EditText editText2;
	private EditText editText3;
	private EditText editText4;
	private EditText editText5;
	private EditText editText6;
	private EditText editText7;
	private EditText editText8;
	private EditText editText9;

	private int rowid = 0;

	JapanHelper dbHelper;
	JapanDao japanDao;
	JapanSetGet newJapanSetGet;
	SQLiteDatabase db;
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		context = this;

		//EditTextの実体を作成

		editText1 = (EditText) findViewById(R.id.editText1);
		editText2 = (EditText) findViewById(R.id.editText2);
		editText3 = (EditText) findViewById(R.id.editText3);
		editText4 = (EditText) findViewById(R.id.editText4);
		editText5 = (EditText) findViewById(R.id.editText5);
		editText6 = (EditText) findViewById(R.id.editText6);
		editText7 = (EditText) findViewById(R.id.editText7);
		editText8 = (EditText) findViewById(R.id.editText8);
		editText9 = (EditText) findViewById(R.id.editText9);

		//データベースを操作するにあたっての準備

		dbHelper = new JapanHelper(this);
		db = dbHelper.getWritableDatabase();
		japanDao = new JapanDao(db);
		newJapanSetGet = new JapanSetGet();

		// インテントで送られてきた付加情報(≒rowid)を得る

		Bundle extras = getIntent().getExtras();

		// 付加情報があればそれをrowidに設定する

		rowid = extras.getInt("ROWID");

		// rowidで指定された列の一覧を取得する

		newJapanSetGet = japanDao.findById(rowid);

		// 取得した一覧から各々の値をEditTextに入れる

		String text1 = newJapanSetGet.getTodofuken();
		editText1.setText(text1);
		String text2 = newJapanSetGet.getKentyo();
		editText2.setText(text2);
		String text3 = Integer.toString(newJapanSetGet.getJinko());
		editText3.setText(text3);
		String text4 = Double.toString(newJapanSetGet.getMenseki());
		editText4.setText(text4);
		String text5 = Double.toString(newJapanSetGet.getMitudo());
		editText5.setText(text5);
		String text6 = Integer.toString(newJapanSetGet.getSi());
		editText6.setText(text6);
		String text7 = Integer.toString(newJapanSetGet.getKu());
		editText7.setText(text7);
		String text8 = Integer.toString(newJapanSetGet.getTyo());
		editText8.setText(text8);
		String text9 = Integer.toString(newJapanSetGet.getSon());
		editText9.setText(text9);

		// //更新ボタンが押されたときの処理

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				boolean flag = false;

				//JapanSetGet newJapanSetGet2 = new JapanSetGet();
				newJapanSetGet.setRowid(rowid);

				// EditTextのデータを読み取っていく

				String text1 = editText1.getText().toString();
				newJapanSetGet.setTodofuken(text1);
				String text2 = editText2.getText().toString();
				newJapanSetGet.setKentyo(text2);

				// if文は空白が入ったときのエラー回避処理
				// 空白の場合0を入れる(空白をintやdouble型に変換できないため)

				String text3 = editText3.getText().toString();
				if (text3.length() != 0) {
					newJapanSetGet.setJinko(Integer.parseInt(text3));
				} else {
					newJapanSetGet.setJinko(0);
				}

				try {
					String text4 = editText4.getText().toString();
					if (text4.length() != 0) {
						newJapanSetGet.setMenseki(Double.parseDouble(text4));
					} else {
						newJapanSetGet.setMenseki(0);
					}
					String text5 = editText5.getText().toString();
					if (text5.length() != 0) {
						newJapanSetGet.setMitudo(Double.parseDouble(text5));
					} else {
						newJapanSetGet.setMenseki(0);
					}
				} catch (NumberFormatException n) {
					flag = true;
				}

				String text6 = editText6.getText().toString();
				if (text6.length() != 0) {
					newJapanSetGet.setSi(Integer.parseInt(text6));
				} else {
					newJapanSetGet.setSi(0);
				}
				String text7 = editText7.getText().toString();
				if (text7.length() != 0) {
					newJapanSetGet.setKu(Integer.parseInt(text7));
				} else {
					newJapanSetGet.setKu(0);
				}
				String text8 = editText8.getText().toString();
				if (text8.length() != 0) {
					newJapanSetGet.setTyo(Integer.parseInt(text8));
				} else {
					newJapanSetGet.setTyo(0);
				}
				String text9 = editText9.getText().toString();
				if (text9.length() != 0) {
					newJapanSetGet.setSon(Integer.parseInt(text9));
				} else {
					newJapanSetGet.setSon(0);
				}

				if(flag == true){
					Toast.makeText(context, "※入力欄に数値でない値が入っています", Toast.LENGTH_SHORT).show();
				}else{

				// update()メソッドでデータベースのデータを更新する
				japanDao.update(newJapanSetGet);

				// 更新するのでRESULT_OKを返しアクティビティを終了する
				setResult(RESULT_OK);
				finish();
				}
			}
		});

		// //キャンセルボタンが押された場合の処理
		// RESULT_CANCELを返しアクティビティを終了する

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}
