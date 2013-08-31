package com.example.soundmaze;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Point;

public class Maze implements Serializable {

	
	private static final long serialVersionUID = 1L;
	String _mazeName;
	boolean[][] _verticalWall;
	boolean[][] _horizontalWall;
	Point _currentPoint;
	Point _pointEnd;
	String _mazeImageName;
	int _mazeColNum;
	int _mazeRowNum;
	public int get_mazeColNum() {
		return _mazeColNum;
	}
	public int get_mazeRowNum() {
		return _mazeRowNum;
	}
	public boolean[][] get_horizontalWall() {
		return _horizontalWall;
	}
	public boolean[][] get_verticalWall() {
		return _verticalWall;
	}
	public Point get_currentPoint() {
		return _currentPoint;
	}
	public Point get_pointEnd() {
		return _pointEnd;
	}
	public Maze(JSONObject jsonObject) throws JSONException {
		
		_verticalWall=new boolean[_mazeRowNum=jsonObject.getInt("rowNum")][_mazeColNum=jsonObject.getInt("colNum")];
		_horizontalWall=new boolean[_mazeRowNum][_mazeColNum];
		_currentPoint=new Point(jsonObject.getInt("startRow"),jsonObject.getInt("startCol"));
		_pointEnd=new Point(jsonObject.getInt("endRow"),jsonObject.getInt("endCol"));
		JSONArray rowsArr=jsonObject.getJSONArray("Vertical");
		for (int i = 0; i < rowsArr.length(); i++) {
				for (int j = 0; j < jsonObject.getInt("colNum"); j++)
					_verticalWall[i][j]=rowsArr.getJSONArray(i).getBoolean(j);
		}
		JSONArray colsArr=jsonObject.getJSONArray("Horizontal");
		for (int i = 0; i < colsArr.length(); i++) {
				for (int j = 0; j < jsonObject.getInt("rowNum"); j++)
					_horizontalWall[i][j]=colsArr.getJSONArray(i).getBoolean(j);
		}
		_mazeName=jsonObject.getString("Name");
	}
	public String get_mazeName() {
		return _mazeName;
	}
	public boolean legalMove(Point from, Point to) {
		//Left move
		try{
		if (from.x == to.x && from.y == to.y - 1)
			return _verticalWall[from.x][from.y];
		//Right Move
		if (from.x == to.x && from.y - 1 == to.y)
			return _verticalWall[from.x+1][from.y];
		// Down move
		if (from.x == to.x - 1 && from.y == to.y)
			return _horizontalWall[from.x][from.y+1];
		//Up move
		if (from.x - 1 == to.x && from.y == to.y)
			return _horizontalWall[from.x][from.y];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		return false;
	}

	public boolean winMaze(Point loc) {
		return loc.equals(_pointEnd);
	}

}
