package com.example.soundmaze;

import com.parse.Parse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class AddUserActivity extends Activity{
	TableHelper th;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		
		TextView  res = (TextView) findViewById(R.id.textViewInstruct);
		final int score=getIntent().getIntExtra("score",0);
		res.setText("Enter your name you scored"+String.valueOf(score)+"Points!");
		th=new TableHelper(this);
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText userName = (EditText)findViewById(R.id.edtNewName);
				String user = userName.getText().toString();
				if (user == null || "".equals(user)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
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
