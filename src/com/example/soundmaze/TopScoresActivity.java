package com.example.soundmaze;


import java.util.List;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class TopScoresActivity extends Activity {
	UserAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_top_scores);

		final ListView listTasks = 
				(ListView)findViewById(R.id.listView1);
		TextView topScoreText=(TextView)findViewById(R.id.topScoreText);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"Chunkfive.otf");
		topScoreText.setTypeface(face);
		
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
