package com.behindcurtain3.khet;

import com.behindcurtain3.khet.util.Compass;

public class Move {
	public int from; // board index moving from
	public int to; // board index moving to
	public Piece piece = new Piece(); // piece involved
	public Piece captured = null;	// null if no capture, otherwise contains piece captured. Useful for taking back moves
	public Compass compass = new Compass(); 
	public int score = 0;
	public Boolean split = false; // used for unstacking obelisks
	public Boolean join = false; // used for stacking obelisks
	public Boolean requestDraw = false;
	public Boolean resign = false;
	
	public Move(){
	}
	
	public Move(int f, int t, Piece p){
		from = f;
		to = t;
		piece = p;
		compass = new Compass(Compass.None);
	}
	
	public Move(int f, int t, Piece p, Compass c){
		from = f;
		to = t;
		piece = p;
		compass = c;
	}	
	
	public Move(int f, int t, Piece p, Compass c, int s, Boolean sp, Boolean j){
		from = f;
		to = t;
		piece = p;
		compass = c;
		score = s;
		split = sp;
		join = j;
	}
	
	public boolean equals(Object m){
		Move move = (Move)m;
		
		if(move.from != this.from)
			return false;
		
		if(move.to != this.to)
			return false;
		
		if(move.piece.type() != this.piece.type())
			return false;
		
		if(move.piece.color() != this.piece.color())
			return false;
		
		if(move.captured == null && this.captured != null)
			return false;
		if(this.captured == null && move.captured != null)
			return false;
		if(move.captured != null && this.captured != null){
			if(move.captured.type() != this.captured.type())
				return false;
			if(move.captured.color() != this.captured.color())
				return false;
		}
		
		if(move.compass.getDirection() != this.compass.getDirection())
			return false;
		
		if(move.split != this.split)
			return false;
		
		if(move.join != this.join)
			return false;
		
		return true;
	}
}
