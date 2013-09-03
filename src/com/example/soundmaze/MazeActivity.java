package com.example.soundmaze;


import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MazeActivity extends Activity {

	Maze stageMaze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze);
	//	Intent intent = getIntent();
	//	Bundle extras = intent.getExtras();
	//	Maze maze = (Maze)extras.getSerializable("maze");
	//	MazeView view = new MazeView(this,maze);
	//	setContentView(view);
		MazeView myMazeView = (MazeView) this.findViewById(R.id.mazeView);
		MazeMaster m;
		try {
			m = new MazeMaster(this.getApplicationContext());
		//	MazeView view = new MazeView(this,m.getMaze("Maze 1"));
			
			myMazeView.setMaze(m.getMaze("Maze 1"));
			
		//	setContentView(view);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maze, menu);
		return true;
	}

}
