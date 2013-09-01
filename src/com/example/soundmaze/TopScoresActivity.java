package com.example.soundmaze;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class TopScoresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_top_scores);
		SqlHalper helper = new SqlHalper(this);
		TableHelper th=new TableHelper(this);
		th.insert(new User("tamar",222));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.top_scores, menu);
		return true;
	}

}
