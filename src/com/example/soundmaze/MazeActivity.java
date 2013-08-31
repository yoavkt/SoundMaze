package com.example.soundmaze;


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
		Intent intent = getIntent();
		Maze playMaze=MazeMaster.getMaze(intent.getStringExtra("maze"));
		ImageView img =(ImageView) findViewById(R.id.imageView1);

		//img.setImageResource(R.drawable.mz2);
		
		final Button btnMaze1 = (Button) findViewById(R.id.btnMove);
		
		btnMaze1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ImageView image = (ImageView) findViewById(R.id.pawn) ;
				FrameLayout.LayoutParams lp= (android.widget.FrameLayout.LayoutParams) image.getLayoutParams();
				int t=lp.leftMargin;
				lp.setMargins(t+10, 0, 0, 0);
				image.setLayoutParams(lp);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maze, menu);
		return true;
	}

}
