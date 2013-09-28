package com.example.soundmaze;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddUserActivity extends Activity{
	TableHelper th;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		 th=new TableHelper(this);
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText userName = (EditText)findViewById(R.id.edtNewName);
				String user = userName.getText().toString();
				if (user == null || "".equals(user)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					int score=getIntent().getIntExtra("score",0);
					th.insert(new User(user,score));
					setResult(RESULT_OK);
					finish();
				}
			}
		});
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}



}
