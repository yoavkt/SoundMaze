package com.example.soundmaze;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

public class MazeView extends View {
	
	private Maze _maze;
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

	public MazeView(Context context, Maze maze) {
		super(context);
		_gameContext = (Activity)context;
		_maze = maze;
		line = new Paint();
		line.setColor(getResources().getColor(R.color.wall));
		red = new Paint();
		red.setColor(getResources().getColor(R.color.ball));
		background = new Paint();
		background.setColor(getResources().getColor(R.color.board));
		setFocusable(true);
		this.setFocusableInTouchMode(true);
	}
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = (w < h)?w:h;
		height=width;
		//for now square mazes
		lineWidth = 1;          //for now 1 pixel wide walls
		cellWidth = (width - ((float)_maze.get_mazeColNum()*lineWidth)) / _maze.get_mazeColNum();
		totalCellWidth = cellWidth+lineWidth;
		cellHeight = (height - ((float)_maze.get_mazeRowNum()*lineWidth)) / (float)_maze.get_mazeRowNum();
		totalCellHeight = cellHeight+lineWidth;
		red.setTextSize(cellHeight*0.75f);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(0, 0, width, height, background);
		drawWalls(canvas);
		drawPlayer(canvas);
		drawEndPoint(canvas);
	}
	public void drawPlayer(Canvas canvas)
	{
		canvas.drawCircle((_maze.get_currentPoint().x * totalCellWidth)+(cellWidth/2),   //x of center
				  (_maze.get_currentPoint().y * totalCellHeight)+(cellWidth/2),  //y of center
				  (cellWidth*0.45f),                           //radius
				  red);
	}
	public void drawEndPoint(Canvas canvas)
	{
	    canvas.drawText("F",
                (_maze.get_pointEnd().x * totalCellWidth)+(cellWidth*0.25f),
                (_maze.get_pointEnd().y * totalCellHeight)+(cellHeight*0.75f),
                red);
	}
	public void drawWalls(Canvas canvas)
	{
		boolean tmp[][]=_maze.get_horizontalWall();
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
		boolean moved = false;
		switch(keyCode) {
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
				return super.onKeyDown(keyCode,evt);
		}
		if(moved) {
			//the ball was moved so we'll redraw the view
			invalidate();
			if(_maze.winMaze()) {
				/*AlertDialog.Builder builder = new AlertDialog.Builder(_gameContext);
				builder.setTitle(_gameContext.getText(R.string.finished_title));
				LayoutInflater inflater = _gameContext.getLayoutInflater();
				View view = inflater.inflate(R.layout.finish, null);
				builder.setView(view);
				View closeButton =view.findViewById(R.id.closeGame);
				closeButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View clicked) {
						if(clicked.getId() == R.id.closeGame) {
							context.finish();
						}
					}
				});
				AlertDialog finishDialog = builder.create();
				finishDialog.show();*/
			}
		}
		return true;
	}
	
}
