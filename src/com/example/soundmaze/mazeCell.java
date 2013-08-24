package com.example.soundmaze;

public class mazeCell {
	private int _cellCode;

	public mazeCell(int cellCode) {
		_cellCode = cellCode;
	}

	public boolean canMoveRight() {
		return (_cellCode == 1) || (_cellCode == 2) || (_cellCode == 3)
				|| (_cellCode == 7) || (_cellCode == 8) || (_cellCode == 11)
				|| (_cellCode == 15) || (_cellCode == 0);
	}

	public boolean canMoveLeft() {
		return (_cellCode == 1) || (_cellCode == 3) || (_cellCode == 4)
				|| (_cellCode == 5) || (_cellCode == 6) || (_cellCode == 9)
				|| (_cellCode == 15) || (_cellCode == 0);
	}

	public boolean canMoveUp() {
		return (_cellCode == 2) || (_cellCode == 3) || (_cellCode == 4)
				|| (_cellCode == 6) || (_cellCode == 7) || (_cellCode == 10)
				|| (_cellCode == 14) || (_cellCode == 0);
	}

	public boolean canMoveDown() {
		return (_cellCode == 1) || (_cellCode == 2) || (_cellCode == 4)
				|| (_cellCode == 5) || (_cellCode == 8) || (_cellCode == 12)
				|| (_cellCode == 14) || (_cellCode == 0);
	}

}
