package com.example.soundmaze;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MazeActivity extends Activity {

	Maze stageMaze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze);
		Intent in= this.getIntent();
		Maze m1 = (Maze)in.getParcelableExtra("maze");
		MazeView myMazeView = (MazeView) this.findViewById(R.id.mazeView);
		myMazeView.setMaze(m1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maze, menu);
		return true;
	}

}
