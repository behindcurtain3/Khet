package com.behindcurtain3.khet;

import java.util.ArrayList;

import com.behindcurtain3.khet.util.Bitboard;
import com.behindcurtain3.khet.util.BoardHelper;
import com.behindcurtain3.khet.util.Compass;
import com.behindcurtain3.khet.util.PieceHelper;

public class Board {
	private ArrayList<Piece> _board;
	private ArrayList<Integer> _trail;
	private Boolean _silverToMove = true;

	public Board(){
		_board = new ArrayList<Piece>();
		_board.addAll(BoardHelper.getInstance().getEmptyConfig());
	}
	
	private void setSilverToMove(Boolean value){
		_silverToMove = value;
	}
	
	public Boolean silverToMove(){
		return _silverToMove;
	}
	
	public ArrayList<Integer> getLaserPath(){
		return _trail;
	}
		
	/*
	 * Resets the board to only blank tiles
	 */
	public void clear(){
		for(int i = 0; i < _board.size(); i++)
			_board.set(i, new Piece(PieceHelper.None));
	}
	
	/*
	 * Pass an ArrayList<Piece> to configure the board
	 */
	public void configure(ArrayList<Piece> configuration){
		if(configuration.size() != _board.size())
			return;
		
		clear(); // clear the board
		
		for(int i = 0; i < configuration.size(); i++)
			_board.set(i, configuration.get(i));
	}
	
	/*
	 * Carries out a move on the board, the board assumes any move given to it is valid!
	 * You can check the validity of a move with the referee.
	 */
	public void move(Move m){
		// Check for a rotation
		if(m.compass.getDirection() == Compass.ClockWise){
			Piece p = new Piece();
			
			/*
			 * Check each direction, if true turn it off and turn the corresponding one on
			 */
			if(_board.get(m.from).NE){
				p.NE = false;
				p.SE = true;
			}
			if(_board.get(m.from).SE){
				p.SE = false;
				p.SW = true;
			}
			if(_board.get(m.from).SW){
				p.SW = false;
				p.NW = true;
			}
			if(_board.get(m.from).NW){
				p.NW = false;
				p.NE = true;
			}
			
			_board.get(m.from).NE = p.NE;
			_board.get(m.from).SE = p.SE;
			_board.get(m.from).SW = p.SW;
			_board.get(m.from).NW = p.NW;
			
		} else if(m.compass.getDirection() == Compass.CounterClockWise){
			Piece p = new Piece();
			
			/*
			 * Check each direction, if true turn it off and turn the corresponding one on
			 */
			if(_board.get(m.from).NE){
				p.NE = false;
				p.NW = true;
			}
			if(_board.get(m.from).SE){
				p.SE = false;
				p.NE = true;
			}
			if(_board.get(m.from).SW){
				p.SW = false;
				p.SE = true;
			}
			if(_board.get(m.from).NW){
				p.NW = false;
				p.SW = true;
			}
			
			_board.get(m.from).NE = p.NE;
			_board.get(m.from).SE = p.SE;
			_board.get(m.from).SW = p.SW;
			_board.get(m.from).NW = p.NW;
		} else { // Its a move
			if(m.split){ // double obelisk is splitting
				_board.get(m.from).setType(PieceHelper.SingleObelisk); // change the current piece to a single obelisk
				_board.set(m.to, _board.get(m.from).copy()); // copy the piece to the new square
			} else if(m.join) { // single obelisk is stacking
				_board.get(m.from).setType(PieceHelper.DoubleObelisk); // change the current piece to a double obelisk
				_board.set(m.to, _board.get(m.from).copy()); // copy the piece to the new square
				_board.set(m.from, new Piece()); // delete the piece that occupied the old square
			} else if(_board.get(m.from).type() == PieceHelper.Djed){ // Djed always swaps
				Piece to = _board.get(m.to).copy();
				_board.set(m.to, _board.get(m.from).copy());
				_board.set(m.from, to);
			} else {
				_board.set(m.to, _board.get(m.from).copy());
				_board.set(m.from, new Piece());
			}
		}
		
		// After the move has been done we always fire a laser!
		fireLaser();
	}
	
	/*
	 * Does the pathfinding for the laser.
	 * Removes any piece struck on a non-reflective surface.
	 */
	private void fireLaser(){
		int index;
		_trail = new ArrayList<Integer>(); 
		Compass finder = new Compass();
		Bitboard pieces = Bitboard.or(this.getBitboardByColor(PieceHelper.Silver), this.getBitboardByColor(PieceHelper.Red));
		
		// Figure out which spot to fire from based whose move it is
		if(silverToMove()){ // Silver fires from 79
			index = 79;
			finder.setDirection(Compass.North);
		} else { // Red from 0
			index = 0;
			finder.setDirection(Compass.South);
		}
		_trail.add(index);
		
		Boolean endReached = false;
		
		while(!endReached){
			int x = index % 10; // Horz position
            int y = index / 10; // Vert position
            
        	// Increment the index based on direction
        	switch(finder.getDirection()){
            	case Compass.North:
            		y -= 1;
            		break;
            	case Compass.South:
            		y += 1;
            		break;
            	case Compass.East:
            		x += 1;
            		break;
            	case Compass.West:
            		x -= 1;
            		break;
        	}
        	if(y < 0 || y >= 8)
            	endReached = true;
            if(x < 0 || x >= 10)
            	endReached = true;
            	
            if(!endReached){
            	index = (y * 10) + x; // Get the new index
            	_trail.add(index);
            	
            	// There is a piece present
            	if(pieces.get(index)){
            		Piece p = this.getPieceAtIndex(index);
            		Boolean collision = false;
            		// Check for collision with a mirror
            		switch(finder.getDirection()){
            		case Compass.North: // North reflects off of SE or SW
            			if(p.SE)
            				finder.setDirection(Compass.East);
            			else if(p.SW)
            				finder.setDirection(Compass.West);
            			else
            				collision = true;
                		break;
                	case Compass.South:
                		if(p.NE)
            				finder.setDirection(Compass.East);
            			else if(p.NW)
            				finder.setDirection(Compass.West);
            			else
            				collision = true;
                		break;
                	case Compass.East:
                		if(p.NW)
            				finder.setDirection(Compass.North);
            			else if(p.SW)
            				finder.setDirection(Compass.South);
            			else
            				collision = true;
                		break;
                	case Compass.West:
                		if(p.NE)
            				finder.setDirection(Compass.North);
            			else if(p.SE)
            				finder.setDirection(Compass.South);
            			else
            				collision = true;
                		break;
            		}
            		if(collision){ // remove the piece
            			if(getPieceTypeAtIndex(index) == PieceHelper.DoubleObelisk){ // Double ob's become single when hit
            				_board.get(index).setType(PieceHelper.SingleObelisk);
            			} else {
            				_board.set(index, new Piece());
            			}
            			endReached = true;
            		}
            	}
            }
		}
		swapTurns();
	}
	
	/*
	 * Returns a copy of itself
	 */
	public Board copy(){
		ArrayList<Piece> b = new ArrayList<Piece>(BoardHelper.TILES);
		for(int i = 0; i < _board.size(); i++)
			b.add(_board.get(i).copy());
		
		Board newBoard = new Board();
		newBoard.configure(b);
		newBoard.setSilverToMove(_silverToMove);		
		
		return newBoard;
	}
	
	/*
	public Bitboard getSilverPharaoh(){
		if(silverPharaoh.valid)
			return silverPharaoh;
		
		silverPharaoh = getBitboardByTypeAndColor(Piece.Pharaoh, Piece.Silver);	
				
		return silverPharaoh;
	}
	
	public Bitboard getSilverDjed(){
		if(silverDjed.valid)
			return silverDjed;
		
		silverDjed = getBitboardByTypeAndColor(Piece.Djed, Piece.Silver);	
				
		return silverDjed;
	}
	
	public Bitboard getSilverPyramid(){
		if(silverPyramid.valid)
			return silverPyramid;
		
		silverPyramid = getBitboardByTypeAndColor(Piece.Pyramid, Piece.Silver);	
				
		return silverPyramid;
	}
	
	public Bitboard getSilverSingleObelisk(){
		if(silverSingleObelisk.valid)
			return silverSingleObelisk;
		
		silverSingleObelisk = getBitboardByTypeAndColor(Piece.SingleObelisk, Piece.Silver);	
				
		return silverSingleObelisk;
	}
	
	public Bitboard getSilverDoubleObelisk(){
		if(silverDoubleObelisk.valid)
			return silverDoubleObelisk;
		
		silverDoubleObelisk = getBitboardByTypeAndColor(Piece.DoubleObelisk, Piece.Silver);	
				
		return silverDoubleObelisk;
	}
	
	public Bitboard getRedPharaoh(){
		if(redPharaoh.valid)
			return redPharaoh;
		
		redPharaoh = getBitboardByTypeAndColor(Piece.Pharaoh, Piece.Red);	
				
		return redPharaoh;
	}
	
	public Bitboard getRedDjed(){
		if(redDjed.valid)
			return redDjed;
		
		redDjed = getBitboardByTypeAndColor(Piece.Djed, Piece.Red);	
				
		return redDjed;
	}
	
	public Bitboard getRedPyramid(){
		if(redPyramid.valid)
			return redPyramid;
		
		redPyramid = getBitboardByTypeAndColor(Piece.Pyramid, Piece.Red);	
				
		return redPyramid;
	}
	
	public Bitboard getRedSingleObelisk(){
		if(redSingleObelisk.valid)
			return redSingleObelisk;
		
		redSingleObelisk = getBitboardByTypeAndColor(Piece.SingleObelisk, Piece.Red);
				
		return redSingleObelisk;
	}
	
	public Bitboard getRedDoubleObelisk(){
		if(redDoubleObelisk.valid)
			return redDoubleObelisk;
		
		redDoubleObelisk = getBitboardByTypeAndColor(Piece.DoubleObelisk, Piece.Red);				
		return redDoubleObelisk;
	}

	*/
	
	
	public Bitboard getBitboardByType(int type){
		Bitboard b = new Bitboard();
		
		for(int i = 0; i < _board.size(); i++){
			if(_board.get(i).type() == type)
				b.set(i, true);
		}
		
		return b;
	}
	
	public Bitboard getBitboardByTypeAndColor(int type, int color){
		Bitboard b = new Bitboard();
		
		for(int i = 0; i < _board.size(); i++){
			if(_board.get(i).type() == type && _board.get(i).color() == color)
				b.set(i, true);
		}
		
		return b;
	}
	
	public Bitboard getBitboardByColor(int color){
		Bitboard b = new Bitboard();
		
		for(int i = 0; i < _board.size(); i++){
			if(_board.get(i).color() == color)
				b.set(i, true);
		}
		
		return b;
	}
	
	public int getPieceTypeAtIndex(int i){
		if(i < 0 || i > BoardHelper.TILES)
			return -1;
		
		return _board.get(i).type();
	}
	public Piece getPieceAtIndex(int i){
		if(i < 0 || i > BoardHelper.TILES)
			return null;
		
		return _board.get(i);
	}
	
	public void swapTurns(){
		_silverToMove = !_silverToMove;
	}
}
