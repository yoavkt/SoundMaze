package com.example.soundmaze;

import il.ac.huji.todolist.FlickrImage;

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

	static boolean created = false;
	static MazeMaster instance;
	private static Context _con;
	static private HashMap<String, Maze> _mazeMap;
	
	
	private MazeMaster(Context con) throws JSONException, IOException {
		_mazeMap=new HashMap<String, Maze>();
		_con=con;
		
		
		JSONObject json= new JSONObject(loadJSONString());
		JSONObject photosOBJ = json.getJSONObject("Mazes");
		JSONArray arr=photosOBJ.getJSONArray("Maze");
		for (int i = 0; i < arr.length(); i++) {
			_mazeMap.put(arr.getJSONObject(i).getString("Name"), new Maze(arr.getJSONObject(i)));
		}
	}

	public static MazeMaster getMazeMaster(Context con) throws JSONException, IOException {
		if (!created)
			instance = new MazeMaster(con);
		return instance;
	}
	public static Maze getMaze(String name){
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
