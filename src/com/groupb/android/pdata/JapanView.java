package com.groupb.android.pdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class JapanView extends ListActivity implements OnClickListener,
		AnimationListener, DialogInterface.OnClickListener {

	Context context = null;
	JapanHelper helper;
	SQLiteDatabase db;
	JapanDao dao;
	JapanSetGet jsg;

	Resources res;
	InputStream is;
	BufferedReader br;

	Button button_import;
	Button button_export;
	ListView list;
	AlertDialog dialog;

	int topPosition;
	int topPositionY;
	int flag;
	int passFlag;

	CharSequence[] items;
	String str;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		splashAnimation();
		context = this;

		// //テーブルの有無を確認する処理
		helper = new JapanHelper(JapanView.this);
		db = helper.getWritableDatabase();
		dao = new JapanDao(db);
		jsg = new JapanSetGet();
		jsg = dao.findById(1);

		// すでにテーブルがある場合はそれを表示する
		if (jsg != null) {
			setListView();
			Toast.makeText(context, "前回終了時のデータを読み込みました", Toast.LENGTH_SHORT)
					.show();
			passFlag = 1;
		}

		// インポートボタンの作成
		button_import = (Button) findViewById(R.id.button_import);
		button_import.setOnClickListener(this);

		// エクスポートボタンの作成
		button_export = (Button) findViewById(R.id.button_export);
		button_export.setOnClickListener(this);

//		起動と同時にパスワード入力画面になる
//		if(passFlag == 0){
//		onClick(button_import);
//		}
	}

	/**
	 * インポート or エクスポートボタンを押したときの処理
	 * (1)インポートが押された場合(パスワード認証の有無)
	 *      (a)パスワード認証が必要か不要か
	 * (b)アラートダイアログで呼び出すデータを表示させる
	 *      (2)エクスポートボタンが押された場合
	 *      (a)アラートダイアログで保存先を表示させる
	 */
	public void onClick(View v) {

		// インポートボタンが押された場合
		if (v == button_import) {

			// passFlag(パスワード認証の有無)が0の時はパスワード入力画面に飛ぶ
			if (passFlag == 0) {
				Intent intent = new Intent(JapanView.this, JapanPassword.class);
				startActivityForResult(intent, 0);
			} else {

				// flag(押された項目がインポートかエクスポートか判断するフラグ)
				flag = 1;
				items = getResources().getStringArray(R.array.items);
				dialog = new AlertDialog.Builder(this).setTitle("ファイルを選んでください")
						.setItems(items, this).setCancelable(true).show();
			}
		}

		if (v == button_export) {
			flag = 2;
			items = getResources().getStringArray(R.array.items2);
			dialog = new AlertDialog.Builder(this).setTitle("保存先を選んでください")
					.setItems(items, this).setCancelable(true).show();
		}
	}

	/**
	 * インポートボタンおよびエクスポートのダイアログ項目が押されたときに呼び出されるメソッド
	 * (1)インポートの項目が押された場合
	 *      (a)「日本」が押された場合
	 *      (b)「米国」が押された場合
	 * (2)エクスポートの項目が押された場合
	 *      (a)「本体」が押された場合
	 *      (b)「SDカード」が押された場合
	 */
	public void onClick(DialogInterface dialog, int which) {
		if (this.dialog == dialog) {
			if (flag == 1) {
				switch (which) {
				case 0:
					CSVReader("日本");
					break;
				case 1:
					CSVReader("米国");
					break;
				case 2:
				case 3:
					Toast.makeText(context, "データがありません", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
			if (flag == 2) {
				switch (which) {
				case 0:
					CSVWriter("本体");
					break;
				case 1:
					CSVWriter("SDカード");
					break;
				}
			}
		}
	}

	/**
	 * CSVファイルを読み込みデータベースにデータを入れるメソッド
	 * (1)「日本」の場合
	 * (2)「米国」の場合
	 * @param country  リストダイアログで選ばれた国名
	 */
	public void CSVReader(String country) {
		// CSVファイルを読み出す
		helper.onUpgrade(db);
		dao = new JapanDao(db);
		jsg = new JapanSetGet();

		// ファイルから値を読み込む
		res = JapanView.this.getResources();

		//米国の場合
		if (country == "米国") {
			is = res.openRawResource(R.raw.usa2);
		}

		//日本の場合
		if (country == "日本") {
			is = res.openRawResource(R.raw.state);
		}
		br = new BufferedReader(new InputStreamReader(is));
		try {
			while ((str = br.readLine()) != null) {
				String[] strList = str.split(",");

				jsg.setTodofuken(strList[0]);
				jsg.setKentyo(strList[1]);
				jsg.setJinko(Integer.valueOf(strList[2]));
				jsg.setMenseki(Double.valueOf(strList[3]));
				jsg.setMitudo(Double.valueOf(strList[4]));
				jsg.setSi(Integer.valueOf(strList[5]));

				// 区に値が入っているかどうかの判定
				if (strList[6].length() != 0) {
					jsg.setKu(Integer.valueOf(strList[6]));
				} else {
					jsg.setKu(0);
				}
				jsg.setTyo(Integer.valueOf(strList[7]));

				// 村に値が入っているかどうかの判定
				if (strList.length == 9) {
					jsg.setSon(Integer.valueOf(strList[8]));
				} else {
					jsg.setSon(0);
				}
				dao.insert(jsg);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// リストビューを作成
		setListView();
		Toast.makeText(context, "データの読み込みに成功しました", Toast.LENGTH_SHORT).show();
	}

	/**
	 * データベースをCSVファイルに書き出すメソッド
	 * (1)本体保存の場合
	 * (2)SDカード保存の場合
	 * @param place 保存する場所
	 */
	public void CSVWriter(String place) {

		FileOutputStream fos = null;
		BufferedWriter out = null;

		if (place == "SDカード") {
			String filePath = Environment.getExternalStorageDirectory()
					+ "/csv/newStateSD.csv";
			File file = new File(filePath);
			file.getParentFile().mkdir();
			try {
				fos = new FileOutputStream(file, false);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				out = new BufferedWriter(osw);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (place == "本体") {
			try {
				fos = context.openFileOutput("newState.csv",
						MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			out = new BufferedWriter(new OutputStreamWriter(fos));
		}

		try {
			List<JapanSetGet> japanlist = dao.findAll();

			for (JapanSetGet japan : japanlist) {
				String str = japan.getTodofuken() + "," + japan.getKentyo()
						+ "," + Integer.toString(japan.getJinko()) + ","
						+ Double.toString(japan.getMenseki()) + ","
						+ Double.toString(japan.getMitudo()) + ","
						+ Integer.toString(japan.getSi()) + ","
						+ Integer.toString(japan.getKu()) + ","
						+ Integer.toString(japan.getTyo()) + ","
						+ Integer.toString(japan.getSon());
				out.write(str);
				out.write(System.getProperty("line.separator"));
			}
			out.flush();
			Toast.makeText(context, "ファイルの保存に成功しました", Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			Toast.makeText(context, "ファイルの保存に失敗しました", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
	}

	/**
	 * リストビューを作成しデータを表示するメソッド
	 * (1)データを格納するためのArrayListを作成
	 * (2)シンプルアダプターでリストビューを作成
	 */
	public void setListView() {

		// 表示形式をフォーマットするための処理(3桁おきにコンマ)
		DecimalFormat df = new DecimalFormat("###,###");

		List<JapanSetGet> japanlist = dao.findAll();
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (JapanSetGet japan : japanlist) {

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("todofuken", japan.getTodofuken());
			map.put("kentyo", japan.getKentyo() + "\t"
					+ df.format(japan.getJinko()) + "人");
			data.add(map);
		}

		// 値を取り込んだリストビューを作成
		SimpleAdapter adapter = new SimpleAdapter(JapanView.this, data,
				android.R.layout.simple_list_item_2, new String[] {
						"todofuken", "kentyo" }, new int[] {
						android.R.id.text1, android.R.id.text2 });

		setListAdapter(adapter);
	}

	/**
	 * リストの項目をクリックしたときの処理
	 * (1)スクロール位置を記憶する
	 * (2)クリックされた位置+1(＝rowid)を付加情報にしてインテントを送る
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		list = (ListView) findViewById(android.R.id.list);
		topPosition = list.getFirstVisiblePosition();
		topPositionY = list.getChildAt(0).getTop();

		Intent intent = new Intent(JapanView.this, JapanDetail.class);
		intent.putExtra("ROWID", position + 1);
		startActivityForResult(intent, 0);
	}

	/**
	 * サブアクティビティから呼び出されるメソッド
	 * (1)パスワード認証が成功した場合
	 *      (a)フラグを1にしてonClickメソッドに戻る
	 * (2)パスワード認証が失敗した場合
	 *      (b)アプリケーションを強制終了
	 * (3)更新ボタンが押された場合
	 *      (a)リストを更新する
	 *      (b)記憶したスクロール位置に戻る
	 * (4)キャンセルボタンが押された場合
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// パスワード認証が成功した場合
		if (resultCode == 777) {
			passFlag = 1;
			Toast.makeText(context, "パスワード認証成功", Toast.LENGTH_SHORT).show();
			onClick(button_import);
		}

		// パスワード認証が失敗した場合
		if (resultCode == 666) {
			Toast.makeText(context, "パスワード認証失敗\nアプリケーションを終了します",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		// 更新ボタンが押された場合の処理
		if (resultCode == RESULT_OK) {
			// リストを更新する
			setListView();
			// 記憶したスクロール位置に戻る
			list.setSelectionFromTop(topPosition, topPositionY);
			Toast.makeText(context, "データを更新しました", Toast.LENGTH_SHORT).show();
		}

		// キャンセルボタンが押された場合の処理
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(context, "キャンセルしました", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * スプラッシュアニメーションを表示させるメソッド
	 */
	private void splashAnimation() {
		AlphaAnimation alphaanime = new AlphaAnimation(1, 0);
		alphaanime.setStartOffset(2000);
		alphaanime.setDuration(1000);
		alphaanime.setFillAfter(true);
		alphaanime.setAnimationListener(this);
		LinearLayout linear = (LinearLayout) findViewById(R.id.linear1);
		linear.startAnimation(alphaanime);
	}

	public void onAnimationEnd(Animation animation) {
		LinearLayout linear = (LinearLayout) findViewById(R.id.linear1);
		linear.setVisibility(View.GONE);
	}

	public void onAnimationRepeat(Animation animation) {
	}

	public void onAnimationStart(Animation animation) {
	}
}
