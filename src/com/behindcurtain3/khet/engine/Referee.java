package com.behindcurtain3.khet.engine;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.util.Bitboard;
import com.behindcurtain3.khet.util.Compass;

public class Referee {
	private ArrayList<Move> _moveLog;
	private Board _masterBoard = new Board();
	private static Bitboard _silverTrespassing;
	private static Bitboard _redTrespassing;
	
	
	public Referee(){
		_silverTrespassing = new Bitboard();
		_silverTrespassing.set(0, true); _silverTrespassing.set(8, true);
		_silverTrespassing.set(10, true);
		_silverTrespassing.set(20, true);
		_silverTrespassing.set(30, true);
		_silverTrespassing.set(40, true);
		_silverTrespassing.set(50, true);
		_silverTrespassing.set(60, true);
		_silverTrespassing.set(70, true); _silverTrespassing.set(78, true);
		
		_redTrespassing = new Bitboard();
		_redTrespassing.set(1, true); _redTrespassing.set(9, true);
		_redTrespassing.set(19, true);
		_redTrespassing.set(29, true);
		_redTrespassing.set(39, true);
		_redTrespassing.set(49, true);
		_redTrespassing.set(59, true);
		_redTrespassing.set(69, true);
		_redTrespassing.set(71, true); _redTrespassing.set(79, true);	
		
	}
	
	public void startnewGame(ArrayList<Piece> config){
		_moveLog = new ArrayList<Move>();
		_masterBoard = new Board();
		_masterBoard.configure(config);
	}
	
	public Board getBoard(){
		return _masterBoard;
	}
	
	public Boolean attemptMove(Move move){
		if(isMoveValid(_masterBoard, move)){
			_masterBoard.move(move);
			_moveLog.add(move);
			return true;
		}
		return false;
	}
	
	public static Boolean isMoveValid(Board board, Move move){
		// This is pretty simple, call getValidMoves and compare w/ the move submitted.
		ArrayList<Move> validMoves = getValidMoves(board);
		
		for(Move m: validMoves){
			if(m.equals(move))
				return true;
		}		
		
		return false;
	}
	
	public static ArrayList<Move> getValidMoves(Board board){
		ArrayList<Move> moves = new ArrayList<Move>();
		
		Bitboard pieces = new Bitboard();
		Bitboard occupied = Bitboard.or(board.getBitboardByColor(Piece.Red), board.getBitboardByColor(Piece.Silver));
		
		if(board.silverToMove())
			pieces = board.getBitboardByColor(Piece.Silver);
		else
			pieces = board.getBitboardByColor(Piece.Red);
		
		// First generate movements, each piece can move to the 8 tiles adjacent to itself
		for(int i = 0; i < pieces.size(); i++){
			if(pieces.get(i)){
				int x = i % 10;
                int y = i / 10;
                int index = -1;
                int type = board.getPieceTypeAtIndex(i);
                
                Boolean upValid = true;
            	Boolean leftValid = true;
            	Boolean rightValid = true;
            	Boolean downValid = true;
            	
            	if(y - 1 < 0)
            		upValid = false;
            	if(y + 1 > 7)
            		downValid = false;
            	if(x - 1 < 0)
            		leftValid = false;
            	if(x + 1 > 9)
            		rightValid = false;

                for (int j = 0; j < 8; j++) {                	
                    switch (j) {
                        case 0:
                            // Up & left
                        	if(upValid && leftValid)
                        		index = ((y - 1) * 10) + (x - 1);
                        	else
                        		index = -1;
                            break;
                        case 1:
                            // Up
                        	if(upValid)
                        		index = ((y - 1) * 10) + x;
                        	else
                        		index = -1;
                            break;
                        case 2:
                            // Up & Right
                        	if(upValid && rightValid)
                        		index = ((y - 1) * 10) + (x + 1);
                        	else
                        		index = -1;
                            break;
                        case 3:
                            // Left
                        	if(leftValid)
                        		index = (y * 10) + (x - 1);
                        	else
                        		index = -1;
                            break;
                        case 4:
                            // Right
                        	if(rightValid)
                        		index = (y * 10) + (x + 1);
                        	else
                        		index = -1;
                            break;
                        case 5:
                            // Down & left
                        	if(downValid && leftValid)
                        		index = ((y + 1) * 10) + (x - 1);
                        	else
                        		index = -1;
                            break;
                        case 6:
                            // Down
                        	if(downValid)
                        		index = ((y + 1) * 10) + x;
                        	else
                        		index = -1;
                            break;
                        case 7:
                            // Down & Right
                        	if(downValid && rightValid)
                        		index = ((y + 1) * 10) + (x + 1);
                        	else
                        		index = -1;
                            break;
                    }
                    if(isIndexValid(occupied, index)){ // check for valid index
                    	if((board.silverToMove() && !isSilverTrespassing(index)) || (!board.silverToMove() && !isRedTrespassing(index))){
                    		int target = board.getPieceTypeAtIndex(index);
    	                    
    	                    if (type != Piece.Djed || target == Piece.None){
    	                    	if(isIndexUnOccupied(occupied, index)){ // target index is empty
    	                    		moves.add(new Move(i, index, board.getPieceAtIndex(i).copy()));
    	                    	}
    	                    } else { // if its a djed check to see if it can swap w/ a non-empty target
    	                        if ((target == Piece.DoubleObelisk || target == Piece.SingleObelisk || target == Piece.Pyramid)){
    	                        	moves.add(new Move(i, index, board.getPieceAtIndex(i).copy()));
    	                        }                            
    	                    }
    	                    
    	                    if(type == Piece.DoubleObelisk && target == Piece.None){ // The doubleobelisk can split, generate those moves here
    		                    moves.add(new Move(i, index, board.getPieceAtIndex(i), new Compass(), 0, true, false));
    	                    }
    	                    
    	                    if(type == Piece.SingleObelisk && target == Piece.SingleObelisk){ // singleobelisk can join together, stacking
    	                    	if(pieces.get(index)){ // make sure the target is the same color
    	                    		moves.add(new Move(i, index, board.getPieceAtIndex(i), new Compass(), 0, false, true));
    	                    	}
    	                    }    	                    
                    	}
	                    
                    }
                } // End of loop for movements
                
                if(type == Piece.Djed || type == Piece.Pyramid){ // Look for rotations
                	moves.add(new Move(i, i, board.getPieceAtIndex(i).copy(), new Compass(Compass.ClockWise)));
                	moves.add(new Move(i, i, board.getPieceAtIndex(i).copy(), new Compass(Compass.CounterClockWise)));
                }
                
			} // End of is piece present check
		}
		
		return moves;
	}
	
	public static Boolean isIndexValid(Bitboard bit, int index){
		if(index >= 0 && index < bit.size())
			return true;
		
		return false;
	}
	
	public static Boolean isIndexUnOccupied(Bitboard bit, int index){
		return  !bit.get(index);
	}
	
	public static Boolean isSilverDead(Board board){
		Bitboard silverPharaoh = board.getBitboardByTypeAndColor(Piece.Pharaoh, Piece.Silver);
		
		if(silverPharaoh.equalTo(Bitboard.getEmptyBitboard()))
			return true;
		
		return false;
	}
	
	public static Boolean isRedDead(Board board){
		Bitboard redPharaoh = board.getBitboardByTypeAndColor(Piece.Pharaoh, Piece.Red);
		
		if(redPharaoh.equalTo(Bitboard.getEmptyBitboard()))
			return true;
		
		return false;
	}
	
	public static Boolean isSilverTrespassing(int index){
		return _silverTrespassing.get(index);
	}
	
	public static Boolean isRedTrespassing(int index){
		return _redTrespassing.get(index);
	}

}