package com.example.soundmaze;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Point;

public class Maze {

	String _mazeName;
	mazeCell[][] _mazeMap;
	Point _pointStart;
	Point _pointEnd;
	String _mazeImageName;
	

	public Maze(String mazeName, mazeCell[][] mazeMap, Point pointStart,
			Point pointEnd,String mazeImageName) {
		_mazeName = mazeName;
		_mazeMap = mazeMap;
		_pointStart = pointStart;
		_pointEnd = pointEnd;
		_mazeImageName=mazeImageName;
	}

	public Maze(JSONObject jsonObject) throws JSONException {
		
		_mazeMap=new mazeCell[jsonObject.getInt("rowNum")][jsonObject.getInt("colNum")];
		_pointStart=new Point(jsonObject.getInt("startRow"),jsonObject.getInt("startCol"));
		_pointEnd=new Point(jsonObject.getInt("endRow"),jsonObject.getInt("endCol"));
		JSONArray rowsArr=jsonObject.getJSONArray("Rows");
		for (int i = 0; i < rowsArr.length(); i++) {
				for (int j = 0; j < jsonObject.getInt("colNum"); j++)
					_mazeMap[i][j]=new mazeCell( rowsArr.getJSONArray(i).getInt(j));
		}
		_mazeName=jsonObject.getString("Name");
	}
	public String get_mazeName() {
		return _mazeName;
	}
	public boolean legalMove(Point from, Point to) {
		mazeCell m = _mazeMap[from.x][from.y];
		if (from.x == to.x && from.y == to.y - 1)
			return m.canMoveRight();
		if (from.x == to.x && from.y - 1 == to.y)
			return m.canMoveLeft();
		if (from.x == to.x - 1 && from.y == to.y)
			return m.canMoveDown();
		if (from.x - 1 == to.x && from.y == to.y)
			return m.canMoveUp();
		return false;
	}

	public boolean winMaze(Point loc) {
		return loc.equals(_pointEnd);
	}

}
