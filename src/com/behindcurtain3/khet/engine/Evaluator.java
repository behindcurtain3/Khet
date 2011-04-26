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
        score += scorePyramids(b);
        //score += ScorePharaohSafety(b);
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
	  */
	private int scorePyramids(Board b){
		int score = 0;
		 
		Bitboard redPyramidsOnHome = Bitboard.and(b.getBitboardByColor(Color.Red), BoardHelper.getInstance().getRedHome());
		Bitboard silverPyramidsOnHome = Bitboard.and(b.getBitboardByColor(Color.Silver), BoardHelper.getInstance().getSilverHome());
		 
		for(int i = 0; i < BoardHelper.TILES; i++){
			if(redPyramidsOnHome.get(i)){
				score++;
				 
				// Check rotation
				if(b.getPieceAtIndex(i).NE)
					score++;
			}
			 
			if(silverPyramidsOnHome.get(i)){
				score--;

				// Check rotation
				if(b.getPieceAtIndex(i).SW)
					score--;
			}
		}
		 
		return score;
	}
}
