package com.example.soundmaze;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class Maze implements Parcelable  {

	
	public static final int UP = 0, DOWN = 1, RIGHT = 2, LEFT = 3;

	
	String _mazeName;
	boolean[][] _verticalWall;
	boolean[][] _horizontalWall;
	Point _currentPoint;
	Point _pointEnd;
	int _mazeColNum;
	int _mazeRowNum;
	

	public void set_currentPoint(Point _currentPoint) {
		this._currentPoint = _currentPoint;
	}
	public void set_horizontalWall(boolean[][] _horizontalWall) {
		this._horizontalWall = _horizontalWall;
	}public void set_mazeColNum(int _mazeColNum) {
		this._mazeColNum = _mazeColNum;
	}

	public void set_mazeName(String _mazeName) {
		this._mazeName = _mazeName;
	}
	public void set_mazeRowNum(int _mazeRowNum) {
		this._mazeRowNum = _mazeRowNum;
	}
	public void set_pointEnd(Point _pointEnd) {
		this._pointEnd = _pointEnd;
	}
	public void set_verticalWall(boolean[][] _verticalWall) {
		this._verticalWall = _verticalWall;
	}
	
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
	
	public Maze(Parcel in){
		_currentPoint=new Point(in.readInt(),in.readInt());
		_pointEnd=new Point(in.readInt(), in.readInt());
		_mazeName=in.readString();
		_mazeColNum=in.readInt();
		_mazeRowNum=in.readInt();
		_verticalWall=new boolean [_mazeRowNum][_mazeColNum];
		for (int r = 0; r < _mazeRowNum; r++) 
				in.readBooleanArray(_verticalWall[r]);
		_horizontalWall=new boolean [_mazeRowNum][_mazeColNum];
		for (int r = 0; r < _mazeRowNum; r++) 
			in.readBooleanArray(_horizontalWall[r]);
	}
	public Maze(JSONObject jsonObject) throws JSONException {
		
		_verticalWall=new boolean[_mazeRowNum=jsonObject.getInt("rowNum")][_mazeColNum=jsonObject.getInt("colNum")];
		_horizontalWall=new boolean[_mazeRowNum][_mazeColNum];
		_currentPoint=new Point(jsonObject.getInt("startCol"),jsonObject.getInt("startRow"));
		_pointEnd=new Point(jsonObject.getInt("endCol"),jsonObject.getInt("endRow"));
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
	public boolean checkAndMove(Point from, Point to){
		if (legalMove(from,to)){
			_currentPoint=to;
			return true;
		}
		return false;
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
	public boolean move(int direction) {
		boolean moved = false;
		if(direction == UP) {
			if(_currentPoint.y != 0 && !_horizontalWall[_currentPoint.y-1][_currentPoint.x]) {
				_currentPoint.y--;
				moved = true;
			}
		}
		if(direction == DOWN) {
			if(_currentPoint.y != _mazeRowNum-1 && !_horizontalWall[_currentPoint.y][_currentPoint.x]) {
				_currentPoint.y++;
				moved = true;
			}
		}
		if(direction == RIGHT) {
			if(_currentPoint.x != _mazeRowNum-1 && !_verticalWall[_currentPoint.y][_currentPoint.x]) {
				_currentPoint.x++;
				moved = true;
			}
		}
		if(direction == LEFT) {
			if(_currentPoint.x != 0 && !_verticalWall[_currentPoint.y][_currentPoint.x-1]) {
				_currentPoint.x--;
				moved = true;
			}
		}
	//	if(moved) 
		//	return this.winMaze(_currentPoint);
		return moved;
	}
	public boolean winMaze() {
		return _currentPoint.equals(_pointEnd);
	}
	public boolean winMaze(Point loc) {
		return loc.equals(_pointEnd);
	}
	@Override
	public int describeContents() {
		
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flag) {
		
		out.writeInt(_currentPoint.x);
		out.writeInt(_currentPoint.y);
		out.writeInt(_pointEnd.x);
		out.writeInt(_pointEnd.y);
		out.writeString(_mazeName);
		out.writeInt(_mazeColNum);
		out.writeInt(_mazeRowNum);
		for (int i = 0; i < _verticalWall.length; i++) 
				out.writeBooleanArray(_verticalWall[i]);		
		for (int i = 0; i < _horizontalWall.length; i++) 
			out.writeBooleanArray(_horizontalWall[i]);		
	}
	public static final Parcelable.Creator<Maze> CREATOR = new Parcelable.Creator<Maze>() {
	     public Maze createFromParcel(Parcel in) {
	         return new Maze(in);
	     }

	     public Maze[] newArray(int size) {
	         return new Maze[size];
	     }
	 };


}
