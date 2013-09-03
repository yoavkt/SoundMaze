package com.example.soundmaze;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;

public class MazeMaster {

	 boolean created = false;
	 MazeMaster instance;
	private  Context _con;
	 private HashMap<String, Maze> _mazeMap;
	
	
	public MazeMaster(Context con) throws JSONException, IOException {
		_mazeMap=new HashMap<String, Maze>();
		_con=con;
		JSONObject json= new JSONObject(loadJSONString());
		JSONObject photosOBJ = json.getJSONObject("Mazes");
		JSONArray arr=photosOBJ.getJSONArray("Maze");
		for (int i = 0; i < arr.length(); i++) {
			Maze m=new Maze(arr.getJSONObject(i));
			_mazeMap.put(m.get_mazeName(), m);
		}
	}

	public  Maze getMaze(String name){
		return _mazeMap.get(name);
	}
	
	private String loadJSONString() throws IOException{
		AssetManager am = _con.getAssets();
		InputStream is = am.open("mazes.txt");
		 BufferedReader in= new BufferedReader(new InputStreamReader(is));
		String line;
        final StringBuilder buffer = new StringBuilder();
        while ((line = in.readLine()) != null)
        {
            buffer.append(line).append(System.getProperty("line.separator"));
        }
        return buffer.toString();
	}
}
