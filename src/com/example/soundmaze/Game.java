package com.example.soundmaze;

import java.io.IOException;

import org.json.JSONException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Game extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MazeMaster m;
		try {
			m = new MazeMaster(this.getApplicationContext());
			MazeView view = new MazeView(this,m.getMaze("Maze 1"));
			setContentView(view);
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
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
