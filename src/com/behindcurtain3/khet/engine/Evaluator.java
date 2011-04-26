package com.behindcurtain3.khet.engine;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.util.Bitboard;
import com.behindcurtain3.khet.util.BoardHelper;
import com.behindcurtain3.khet.util.Color;
import com.behindcurtain3.khet.util.Math;
import com.behindcurtain3.khet.util.Pieces;

public class Evaluator {
	private static Evaluator instance = null;
	
	private final int _scorePyramid = 5;
	private final int _scoreDoubleObelisk = 2;
	private final int _scoreSingleObelisk = 1;
	
	protected Evaluator(){
	}
	
	public static Evaluator getInstance(){
		if(instance == null){
			instance = new Evaluator();
		}
		return instance;
	}
	
	/*
	 * Takes a board as input and returns a score
	 */
	public int score(Board b){		
		if (RuleBook.isSilverDead(b))
            return Math.INFINITY;
        if (RuleBook.isRedDead(b))
            return -Math.INFINITY;

        int score = 0;
        // --- SCORING -------------------
        // 1. Material
        // 2. Pharaoh Safety
        // 3. Reflector Positioning (How well your reflector row is positioned)
        score += scoreMaterial(b);
        score += scorePositioning(b);
        //score += ScoreDjedsPosition(b);
        //score += ScorePrimaryReflectors(b);        
        
        return score;
	}
	
	private int scoreMaterial(Board b){
		int score = 0;
		// Positions
		for (int i = 0; i < BoardHelper.TILES; i++){
			Piece p = b.getPieceAtIndex(i);
			if(p.type() == Pieces.None)
				continue;
			
			switch(p.color()){
			case Color.Silver:
				switch(p.type()){
					case Pieces.Pyramid:
						score -= _scorePyramid;
						break;
					case Pieces.DoubleObelisk:
						score -= _scoreDoubleObelisk;
						break;
					case Pieces.SingleObelisk:
						score -= _scoreSingleObelisk;
						break;
				}
				break;
			case Color.Red:
				switch(p.type()){
				case Pieces.Pyramid:
					score += _scorePyramid;
					break;
				case Pieces.DoubleObelisk:
					score += _scoreDoubleObelisk;
					break;
				case Pieces.SingleObelisk:
					score += _scoreSingleObelisk;
					break;
			}
				break;
			}
		}
		return score;
    }
	
	/*
	  * Scores the positioning of the pyramids
	  * 1. Having a pyramid on your home row +1
	  * 2. Pyramid reflecting out from home row +1
	  * 3. -1 FOR Having a mirror reflecting towards your pharaoh
	  * 4. -1 FOR Pharaoh on same row as opposition home mirror 
	  */
	private int scorePositioning(Board b){
		int score = 0;
		
		Bitboard reflectors = Bitboard.or(b.getBitboardByType(Pieces.Pyramid), b.getBitboardByType(Pieces.Djed));
		
		//Bitboard redReflectors = Bitboard.and(reflectors, b.getBitboardByColor(Color.Red));
		//Bitboard silverReflectors = Bitboard.and(reflectors, b.getBitboardByColor(Color.Silver));
		
		// Check reflectors on home rows
		Bitboard redOnHome = Bitboard.and(reflectors, BoardHelper.getInstance().getRedHome());
		Bitboard silverOnHome = Bitboard.and(reflectors, BoardHelper.getInstance().getSilverHome());
		//Bitboard redNotOnHome = Bitboard.xor(reflectors, redOnHome);
		//Bitboard silverNotOnHome = Bitboard.xor(reflectors, silverOnHome);
		 
		for(int i = 0; i < BoardHelper.TILES; i++){
			if(redOnHome.get(i)){
				score++;
				 
				// Check rotation
				if(b.getPieceAtIndex(i).NE)
					score++;
			} else if(silverOnHome.get(i)){
				score--;

				// Check rotation
				if(b.getPieceAtIndex(i).SW)
					score--;
			}
			/*
			// +1/-1 for mirrors reflecting towards the opposite pharaoh
			if(redNotOnHome.get(i)){
				if(b.getPieceAtIndex(i).SW)
					score++;
			} else if(silverNotOnHome.get(i)){
				if(b.getPieceAtIndex(i).NE)
					score--;
			}
			*/
		}
		 
		return score;
	}
}
