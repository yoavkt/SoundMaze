package com.example.soundmaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddUserActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText userName = (EditText)findViewById(R.id.edtNewName);
				String user = userName.getText().toString();
				if (user == null || "".equals(user)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					Intent resultIntent = new Intent();
					resultIntent.putExtra("name", user);

					setResult(RESULT_OK, resultIntent);
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
