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
import android.widget.TextView;

public class MainActivity extends Activity {
	
	MazeMaster myMazeMaster;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		TextView header = (TextView) findViewById(R.id.mainHeader);
		Typeface face1 = Typeface.createFromAsset(getAssets(),
				"FontleroyBrown.ttf");
		header.setTypeface(face1);
		try {
			 myMazeMaster=new MazeMaster(this.getApplicationContext());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Typeface face = Typeface.createFromAsset(getAssets(),
				"Chunkfive.otf");
		
		final Button btnMaze1 = (Button) findViewById(R.id.btnMainMaze1);
		btnMaze1.setTypeface(face);
		btnMaze1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						LevelsActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});



		final Button btnMainTopScores = (Button) findViewById(R.id.btnMainTopScores);
		btnMainTopScores.setTypeface(face);
		btnMainTopScores.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						TopScoresActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
		final Button btnMainInst = (Button) findViewById(R.id.btnMainInst);
		btnMainInst.setTypeface(face);
		btnMainInst.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						InstructActivity.class);
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
