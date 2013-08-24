package com.example.soundmaze;

public class Maze {

	
	int[][][] maze;
	int size;
	final int DIRECTON=3;
	public Maze(int res, String index) {
		if (res==0)
			size=14;
		else
			size=19;
		maze=new int[size][size][DIRECTON];
		this.setMaze(index);
	}
	public void setMaze(String index){
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				for (int j2 = 0; j2 < maze[i][j].length; j2++) {
					maze[i][j][j2]=0;
				}
			}
		}
		if (index.equals(R.string.maze_1_name)){
		for (int i = 0; i < maze.length-1; i++) {
			//column X rows X dirs
			maze[i][1][2]=1;
		}
		for (int i = 0; i < maze.length-1; i++) {
			maze[14][i][3]=1;
		}
		}
		else{
			for (int i = 0; i < maze.length-1; i++) {
				//column X rows X dirs
				maze[1][i][3]=1;
			}
			for (int i = 0; i < maze.length-1; i++) {
				maze[i][14][2]=1;
			}
			}
		}
			
		
	}


