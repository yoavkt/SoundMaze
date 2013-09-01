package com.example.soundmaze;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

public class TopScoresActivity extends Activity {
	UserAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_top_scores);

		final ListView listTasks = 
				(ListView)findViewById(R.id.listView1);

		TableHelper th=new TableHelper(this);
		List<User> tasks = th.all();

		adapter = 

				new UserAdapter(this, tasks);
		listTasks.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.top_scores, menu);
		return true;
	}

}
