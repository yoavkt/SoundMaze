package com.example.soundmaze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class MazeView extends View {
	
	public Maze _maze;
	private Activity _gameContext;
	//width and height of the whole maze and width of lines which
	//make the walls
	private int width, height, lineWidth;
	//width and height of cells in the maze
	float cellWidth, cellHeight;
	//the following store result of cellWidth+lineWidth 
	//and cellHeight+lineWidth respectively 
	float totalCellWidth, totalCellHeight;
	//the finishing point of the maze
	private Paint line, red, background;
	private int _score=0;
	private int _scoreFactor=1;
	private int _life=3;
	Bitmap playerBitMap;
	Bitmap goalBitMap;
//	TextView textScore;

	public MazeView(Context context, AttributeSet attrs) {
		super(context,attrs);
		_gameContext = (Activity)context;
		setFocusable(true);
		this.setFocusableInTouchMode(true);
		setDisplayItmes();
		 playerBitMap=BitmapFactory.decodeResource(getResources(), R.drawable.dogpawn_small);
		 goalBitMap=BitmapFactory.decodeResource(getResources(), R.drawable.bone);
//		textScore=(TextView)findViewById(R.id.textScore);
		
		
	}
	
	public MazeView(Context context, Maze maze) {
		super(context);
		_gameContext = (Activity)context;
		_maze = maze;
		_scoreFactor=(int) (_scoreFactor*maze.get_difficulty());
		setDisplayItmes();
		
	}
	public void restartMaze(){
		_maze.resetLocation();
		_life=3;
		_score=0;
		unHeroMode();
	}
	public int getLife() {
		return _life;
	}
	public boolean getWin(){
		return _maze.winMaze();
	}
	public int get_score() {
		return _score;
	}
	public void startMazeHeroMode()
	{
		_scoreFactor=(int) (3*_maze.get_difficulty());
		background = new Paint();
		background.setColor(getResources().getColor(R.color.wall));
		 invalidate();
	}
	public void unHeroMode()
	{
		_scoreFactor=(int) (1*_maze.get_difficulty());
		background = new Paint();
		background.setColor(getResources().getColor(R.color.board));
		 invalidate();
	}
	private void setDisplayItmes()
	{
		line = new Paint();
		line.setColor(getResources().getColor(R.color.wall));
		red = new Paint();
		red.setColor(getResources().getColor(R.color.ball));
		background = new Paint();
		background.setColor(getResources().getColor(R.color.blue));
//		setBackgroundResource(R.drawable.trophygolden2);
		}
	public void setMaze(Maze maze)
	{
		_maze = maze;
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		width = (w < h)?w:h;
		height=width;   
		lineWidth = 1;          //for now 1 pixel wide walls
		cellWidth = (width - ((float)_maze.get_mazeColNum()*lineWidth)) / _maze.get_mazeColNum();
		totalCellWidth = cellWidth+lineWidth;
		cellHeight = (height - ((float)_maze.get_mazeRowNum()*lineWidth)) / (float)_maze.get_mazeRowNum();
		totalCellHeight = cellHeight+lineWidth;
		red.setTextSize(cellHeight*0.75f);
		this.drawableStateChanged();
		super.onSizeChanged(w, h, oldw, oldh);
		
	}
	protected void onDraw(Canvas canvas) {
		if (_maze!=null){
		canvas.drawRect(0, 0, width, height, background);
		drawWalls(canvas);
		drawPlayer(canvas);
		drawEndPoint(canvas);
		}
	}
	public void drawPlayer(Canvas canvas)
	{
	//	canvas.drawCircle((_maze.get_currentPoint().x * totalCellWidth)+(cellWidth/2),   //x of center
		//		  (_maze.get_currentPoint().y * totalCellHeight)+(cellWidth/2),  //y of center
			//	  (cellWidth*0.45f),                           //radius
				//   line);
		//canvas.drawRect(0, 0, 50, 50, red);
		
		Rect rc=new Rect((int)(_maze.get_currentPoint().x * totalCellWidth),
					(int)(_maze.get_currentPoint().y * totalCellHeight),
					(int)((_maze.get_currentPoint().x * totalCellWidth)+(totalCellWidth-5)),
						(int)((_maze.get_currentPoint().y * totalCellHeight)+totalCellHeight-5));
		
		//canvas.drawRect(rc, red);
		//canvas.drawBitmap(bm, 0, 0, null);
		canvas.drawBitmap(playerBitMap, null, rc, null);
	}
	public void drawEndPoint(Canvas canvas)
	{
		Rect rc=new Rect((int)(_maze.get_pointEnd().x * totalCellWidth),
				(int)(_maze.get_pointEnd().y * totalCellHeight),
				(int)((_maze.get_pointEnd().x * totalCellWidth)+(totalCellWidth-5)),
					(int)((_maze.get_pointEnd().y * totalCellHeight)+totalCellHeight-5));
		canvas.drawBitmap(goalBitMap, null, rc, null);
	//    canvas.drawText("F",
       //         (_maze.get_pointEnd().x * totalCellWidth)+(cellWidth*0.25f),
        //        (_maze.get_pointEnd().y * totalCellHeight)+(cellHeight*0.75f),
        //        red);
	}
	public void drawWalls(Canvas canvas)
	{
		for(int i = 0; i < _maze.get_mazeRowNum(); i++) {
			for(int j = 0; j < _maze.get_mazeColNum(); j++){
				float x = j * totalCellWidth;
				float y = i * totalCellHeight;
				if(j < _maze.get_mazeRowNum() - 1 && _maze.get_verticalWall()[i][j]) {
					//we'll draw a vertical line
					canvas.drawLine(x + cellWidth,
									y,               //start Y
									x + cellWidth,   //stop X
									y + cellHeight,  //stop Y
									line);
				}
				if(i < _maze.get_mazeColNum() - 1 && _maze.get_horizontalWall()[i][j]) {
					//we'll draw a horizontal line
					canvas.drawLine(x,               //startX 
									y + cellHeight,  //startY 
								    x + cellWidth,   //stopX 
								    y + cellHeight,  //stopY 
									line);
				}
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent evt) {
		return movementUpdater(keyCode);
	}
	
	public boolean movementUpdater(int moveCode)
	{
		boolean moved = false;
		switch(moveCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				moved = _maze.move(Maze.UP);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				moved = _maze.move(Maze.DOWN);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				moved = _maze.move(Maze.RIGHT);
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				moved = _maze.move(Maze.LEFT);
				break;
			default:
				moved= false;
		}
		if(moved) {
			_score=(_score+5)*_scoreFactor;
			invalidate();
			if(_maze.winMaze()) {
				_score=_score+100*_scoreFactor;
			}
		}else{
			_score=_score-5;
			_life=_life-1;
		}
		return moved;
	}
	
}
