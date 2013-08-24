package com.example.soundmaze;

import org.json.JSONObject;

import android.graphics.Point;

public class Maze {

	String _mazeName;
	mazeCell[][] _mazeMap;
	Point _pointStart;
	Point _pointEnd;

	public Maze(String mazeName, mazeCell[][] mazeMap, Point pointStart,
			Point pointEnd) {
		_mazeName = mazeName;
		_mazeMap = mazeMap;
		_pointStart = pointStart;
		_pointEnd = pointEnd;
	}

	public Maze(JSONObject jsonObject) {
		
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
