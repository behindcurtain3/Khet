package com.behindcurtain3.khet;

public class Piece {
	/*
	 * Reflections - NE, SE, SW, NW
	 * The four directions a mirror can be facing.
	 * if = 0 no reflection from light coming from that direction
	 * if = 1 reflect!
	 */	
	public Boolean NE;
	public Boolean SE;
	public Boolean SW;
	public Boolean NW;
	
	private int _type;
	private int _color;
	
	public Piece(){	
		NE = false;
		SE = false;
		SW = false;
		NW = false;
		
		_type = 0;
		_color = -1;
	}
	
	public Piece(int t){
		NE = false;
		SE = false;
		SW = false;
		NW = false;
		
		_type = t;
		_color = -1;
	}
	
	public Piece(int t, int c, boolean ne, boolean se, boolean sw, boolean nw){
		_type = t;
		_color = c;
		NE = ne;
		SE = se;
		SW = sw;
		NW = nw;
	}
	
	public int type(){
		return _type;
	}
	
	public void setType(int t){
		_type = t;
	}
	
	public int color(){
		return _color;
	}
	
	public void setColor(int c){
		_color = c;
	}

	public Piece copy(){
		return new Piece(this._type, this._color, this.NE, this.SE, this.SW, this.NW);
	}
	
}
