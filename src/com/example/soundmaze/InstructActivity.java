package com.example.soundmaze;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class InstructActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instruct);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.instruct, menu);
		return true;
	}

}
