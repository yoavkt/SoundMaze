package com.example.soundmaze;


public class User {
	private String _name;
	private int _score;
	public User(String name,int score) {
		_name=name;
		_score=score;
		
	}

	public String getName() {
		return _name;
	}

	public int getScore() {
		return _score;
	}
}
