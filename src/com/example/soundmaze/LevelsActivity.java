package com.example.soundmaze;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LevelsActivity extends Activity {

	MazeMaster myMazeMaster;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_levels);
		try {
			myMazeMaster=new MazeMaster(this.getApplicationContext());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Button btnMaze1 = (Button) findViewById(R.id.btnMainMaze01);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"Chunkfive.otf");
		btnMaze1.setTypeface(face);
		btnMaze1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 3"));
				LevelsActivity.this.startActivity(myIntent);
			}
		});

		final Button btnMaze2 = (Button) findViewById(R.id.btnMainMaze02);
		btnMaze2.setTypeface(face);
		btnMaze2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 8"));
				LevelsActivity.this.startActivity(myIntent);


			}
		});

		final Button btnMaze3 = (Button) findViewById(R.id.btnMainMaze03);
		btnMaze3.setTypeface(face);
		btnMaze3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 5"));
				LevelsActivity.this.startActivity(myIntent);


			}
		});

		final Button btnMaze4 = (Button) findViewById(R.id.btnMainMaze04);
		btnMaze4.setTypeface(face);
		btnMaze4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 6"));
				LevelsActivity.this.startActivity(myIntent);


			}
		});

		final Button btnMaze5 = (Button) findViewById(R.id.btnMainMaze05);
		btnMaze5.setTypeface(face);
		btnMaze5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 4"));
				LevelsActivity.this.startActivity(myIntent);


			}
		});

		final Button btnMaze6 = (Button) findViewById(R.id.btnMainMaze06);
		btnMaze6.setTypeface(face);
		btnMaze6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 7"));
				LevelsActivity.this.startActivity(myIntent);


			}
		});

		final Button btnMaze7 = (Button) findViewById(R.id.btnMainMaze07);
		btnMaze7.setTypeface(face);
		btnMaze7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 2"));
				LevelsActivity.this.startActivity(myIntent);


			}
		});

		final Button btnMaze8 = (Button) findViewById(R.id.btnMainMaze08);
		btnMaze8.setTypeface(face);
		btnMaze8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(LevelsActivity.this,
						MazeActivity.class);
				myIntent.putExtra("maze", myMazeMaster.getMaze("Maze 1"));
				LevelsActivity.this.startActivity(myIntent);


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
