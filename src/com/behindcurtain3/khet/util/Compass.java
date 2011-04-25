package com.behindcurtain3.khet.util;

public class Compass {
	public static final int None = 0;
	public static final int North = 1;
	public static final int South = 2;
	public static final int East = 3;
	public static final int West = 4;
	public static final int ClockWise = 5;
	public static final int CounterClockWise = 6;
	
	private int _direction = Compass.None;
	
	public Compass(){
	}
	
	public Compass(int c){
		_direction = c;
	}
	
	public int getDirection(){
		return _direction;
	}
	
	public void setDirection(int d){
		_direction = d;
	}
}
