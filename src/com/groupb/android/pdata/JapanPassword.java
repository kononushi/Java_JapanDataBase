package com.groupb.android.pdata;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JapanPassword extends Activity {

	int limit;
	int limit2 = 3;
	String pass1;
	int pass2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass);

		final Context context = this;
		final EditText editText = (EditText) findViewById(R.id.pass);
		Button ok = (Button) findViewById(R.id.ok);
		Button cancel = (Button) findViewById(R.id.cancel);

		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pass1 = editText.getText().toString();
				if (pass1.length() == 0) {
					pass2 = 0;
				} else {
					pass2 = Integer.valueOf(pass1);
				}

				// パスワードを3回間違えたとき
				if (limit == 2) {
					setResult(666);
					finish();
				} else {
					// パスワード認証処理
					if (pass2 == 1945) {
						setResult(777);
						finish();
					} else {
						limit++;
						limit2--;
						Toast.makeText(context,
								"パスワードが違います" + "\n" + "残り入力可能回数 : " + limit2,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		// キャンセルボタンが押されたときの処理
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}