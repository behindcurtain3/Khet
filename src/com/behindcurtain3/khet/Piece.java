package com.behindcurtain3.khet;

public class Piece {
	// Types
	public final static int None = 0;
	public final static int Pharaoh = 1;
	public final static int Djed = 2;
	public final static int Pyramid = 3;
	public final static int SingleObelisk = 4;
	public final static int DoubleObelisk = 5;
	
	// Color
	public final static int Red = 10;
	public final static int Silver = 11;
	
	/*
	 * Reflections - NE, SE, SW, NW
	 * The four directions a mirror can be facing.
	 * if = 0 no reflection from light coming from that direction
	 * if = 1 reflect!
	 */	
	public Boolean NE = false;
	public Boolean SE = false;
	public Boolean SW = false;
	public Boolean NW = false;
	
	private int _type = 0;
	private int _color = -1;
	
	public Piece(){
		
	}
	
	public Piece(int t){
		setType(t);
	}
	
	
	public int type(){
		return _type;
	}
	
	public void setType(int t){
		if(t < Piece.None || t > Piece.DoubleObelisk)
			_type = 0;
		else
			_type = t;
	}
	
	public int color(){
		return _color;
	}
	
	public void setColor(int c){
		if(c < Piece.Red || c > Piece.Silver)
			c = 10;
		else
			_color = c;
	}

	public Piece copy(){
		Piece p = new Piece();
		p.NE = this.NE;
		p.SE = this.SE;
		p.SW = this.SW;
		p.NW = this.NW;
		
		p.setType(this.type());
		p.setColor(this.color());
		
		return p;
	}
	
}
