package com.example.soundmaze;

import java.io.IOException;
import java.util.Date;

import org.json.JSONException;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseAnalytics;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	MazeMaster myMazeMaster;
	//tamar
	TableHelper th;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		//tamar
		th=new TableHelper(this);
		Intent intent = new Intent(this, AddUserActivity.class);
		startActivityForResult(intent, 1337);
		//
		
		
		try {
			 myMazeMaster=new MazeMaster(this.getApplicationContext());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Button btnMaze1 = (Button) findViewById(R.id.btnMainMaze1);
		btnMaze1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						Game.class);
				//myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 1")); // Optional
																	// parameters
				MainActivity.this.startActivity(myIntent);
			}
		});

		final Button btnMaze2 = (Button) findViewById(R.id.btnMainMaze2);
		btnMaze2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", R.string.maze_2_name); // Optional
																	// parameters
				MainActivity.this.startActivity(myIntent);
				
			}
		});

		final Button btnMainTopScores = (Button) findViewById(R.id.btnMainTopScores);
		btnMainTopScores.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						TopScoresActivity.class);
//				myIntent.getIntExtra("table", th);
				MainActivity.this.startActivity(myIntent);
			}
		});
		final Button btnMainInst = (Button) findViewById(R.id.btnMainInst);
		btnMainInst.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						InstructActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});

	}
	//tamar
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1337 && resultCode == RESULT_OK) {
			String userName = data.getStringExtra("name");
			th.insert(new User(userName,222));
			
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
