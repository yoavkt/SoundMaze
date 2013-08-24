package com.example.soundmaze;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button btnMaze1 = (Button) findViewById(R.id.btnMainMaze1);
		btnMaze1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(MainActivity.this, MazeActivity.class);
            	myIntent.putExtra("maze", R.string.maze_1_name); //Optional parameters
            	MainActivity.this.startActivity(myIntent);
            }
        });
		
		final Button btnMaze2 = (Button) findViewById(R.id.btnMainMaze2);
		btnMaze2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(MainActivity.this, MazeActivity.class);
            	myIntent.putExtra("maze", R.string.maze_2_name); //Optional parameters
            	MainActivity.this.startActivity(myIntent);
            }
        });
		
		final Button btnMainTopScores = (Button) findViewById(R.id.btnMainTopScores);
		btnMainTopScores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(MainActivity.this, TopScoresActivity.class);
            	MainActivity.this.startActivity(myIntent);
            }
        });
		final Button btnMainInst = (Button) findViewById(R.id.btnMainInst);
		btnMainInst.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(MainActivity.this, InstructActivity.class);
            	MainActivity.this.startActivity(myIntent);
            }
        });

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
