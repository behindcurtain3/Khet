package com.behindcurtain3.khet.engine;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.util.Bitboard;
import com.behindcurtain3.khet.util.BoardHelper;
import com.behindcurtain3.khet.util.Compass;
import com.behindcurtain3.khet.util.PieceHelper;

public class RuleBook {
	
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
		Bitboard occupied = Bitboard.or(board.getBitboardByColor(PieceHelper.Red), board.getBitboardByColor(PieceHelper.Silver));
		
		if(board.silverToMove())
			pieces = board.getBitboardByColor(PieceHelper.Silver);
		else
			pieces = board.getBitboardByColor(PieceHelper.Red);
		
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
                    if(BoardHelper.isIndexValid(occupied, index)){ // check for valid index
                    	if((board.silverToMove() && !Referee.getInstance().isSilverTrespassing(index)) || (!board.silverToMove() && !Referee.getInstance().isRedTrespassing(index))){
                    		int target = board.getPieceTypeAtIndex(index);
    	                    
    	                    if (type != PieceHelper.Djed || target == PieceHelper.None){
    	                    	if(BoardHelper.isIndexUnOccupied(occupied, index)){ // target index is empty
    	                    		moves.add(new Move(i, index, board.getPieceAtIndex(i).copy()));
    	                    	}
    	                    } else { // if its a djed check to see if it can swap w/ a non-empty target
    	                        if ((target == PieceHelper.DoubleObelisk || target == PieceHelper.SingleObelisk || target == PieceHelper.Pyramid)){
    	                        	moves.add(new Move(i, index, board.getPieceAtIndex(i).copy()));
    	                        }                            
    	                    }
    	                    
    	                    if(type == PieceHelper.DoubleObelisk && target == PieceHelper.None){ // The doubleobelisk can split, generate those moves here
    		                    moves.add(new Move(i, index, board.getPieceAtIndex(i), new Compass(), 0, true, false));
    	                    }
    	                    
    	                    if(type == PieceHelper.SingleObelisk && target == PieceHelper.SingleObelisk){ // singleobelisk can join together, stacking
    	                    	if(pieces.get(index)){ // make sure the target is the same color
    	                    		moves.add(new Move(i, index, board.getPieceAtIndex(i), new Compass(), 0, false, true));
    	                    	}
    	                    }    	                    
                    	}
	                    
                    }
                } // End of loop for movements
                
                if(type == PieceHelper.Djed || type == PieceHelper.Pyramid){ // Look for rotations
                	moves.add(new Move(i, i, board.getPieceAtIndex(i).copy(), new Compass(Compass.ClockWise)));
                	moves.add(new Move(i, i, board.getPieceAtIndex(i).copy(), new Compass(Compass.CounterClockWise)));
                }
                
			} // End of is piece present check
		}
		
		return moves;
	}
	
	public static Boolean isSilverDead(Board board){
		Bitboard silverPharaoh = board.getBitboardByTypeAndColor(PieceHelper.Pharaoh, PieceHelper.Silver);
		
		if(silverPharaoh.equalTo(Bitboard.getEmptyBitboard()))
			return true;
		
		return false;
	}
	
	public static Boolean isRedDead(Board board){
		Bitboard redPharaoh = board.getBitboardByTypeAndColor(PieceHelper.Pharaoh, PieceHelper.Red);
		
		if(redPharaoh.equalTo(Bitboard.getEmptyBitboard()))
			return true;
		
		return false;
	}
}
