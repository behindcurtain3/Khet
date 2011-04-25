package com.behindcurtain3.khet.player;

import java.util.ArrayList;

import android.util.Log;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.controller.Referee;
import com.behindcurtain3.khet.util.Bitboard;
import com.behindcurtain3.khet.util.Helper;

public class ComputerPlayer implements Player {
	// Debugging
	private final Boolean Debug = true;
	private String TAG;
	private int _nodesChecked;
	private int _nodesPruned;
	
	// Scoring values
	private final int INFINITY = 99999999;
	private final int _scorePyramid = 5;
	private final int _scoreDoubleObelisk = 2;
	private final int _scoreSingleObelisk = 1;
	
	private int _color;
	private Board _board; // Hold our copy of the board	
	private Move _moveSubmitted; // Holds the move we submit to the controller
	
	// Move generation
	private int _maxDepth;

	public ComputerPlayer(){
		_maxDepth = 1;
	}
	
	@Override
	public void drawRequestStatus(Boolean arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void gameDrawn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameLost() {
		Log.d(TAG, "Game over, you lose!");
	}

	@Override
	public void gameResigned() {
		Log.d(TAG, "The opponent has resigned.");

	}

	@Override
	public void gameWon() {
		Log.d(TAG, "Game over, you win!");
	}

	@Override
	public Move getMove(Board board) {
		_board = board.copy();
		if (Debug) Log.d(TAG, "-------------------------- GENERATING MOVES -------------------------------");
		long start = System.currentTimeMillis();
        //Move moveToMake = MaxMove(Board, IsSilver, 0);
        Move moveToMake = generateMove();
        if (Debug) Log.d(TAG, "MOVE SCORE: " + moveToMake.score);
        if (Debug) Log.d(TAG, "NODES CHECKED: " + _nodesChecked);
        if (Debug) Log.d(TAG, "NODES PRUNED: " + _nodesPruned);
        if (Debug) Log.d(TAG, "TIME: " + (System.currentTimeMillis() - start));
        if (Debug) Log.d(TAG, "--------------------------------- END -------------------------------------");
		
		//ArrayList<Move> moves = Referee.getValidMoves(board);
		//Log.d(TAG, "Moves available: " + moves.size());
		
        _moveSubmitted = moveToMake;
        
		return moveToMake;
	}

	@Override
	public void illegalMove(String arg0) {
		Log.d(TAG, "*** ILLEGAL MOVE ***");
		Log.d(TAG, "Move: " + _moveSubmitted.from + "->" + _moveSubmitted.to);
		Log.d(TAG, "Direction: " + _moveSubmitted.compass.getDirection());
	}

	@Override
	public void laserFired(ArrayList<Integer> list) {
		/*
		for(int i: list){
			System.out.print(i + ", ");
		}
		System.out.println();
		*/
	}

	@Override
	public void opponentMove(Move m) {
		_board.move(m);
		//_board.setSilverToMove(!_board.silverToMove());
	}

	@Override
	public Boolean opponentRequestDraw() {
		return false;
	}

	@Override
	public void setColor(int c) {
		_color = c;
		TAG = "Khet-Computer";
	}
	
	@Override
	public void setBoard(Board b) {
		_board = b.copy();
	}

	@Override
	public void validMove() {
		if(_moveSubmitted != null){
			Log.d(TAG, "--- Move Complete --- " + _color);
			//_board.move(_moveSubmitted);
			//_board.setSilverToMove(!_board.silverToMove());
			_moveSubmitted = null;
		}			
	}
	
	private Move generateMove(){
		int score;
        int threshold;
        if (_color == Piece.Silver)
            threshold = INFINITY+1;
        else
            threshold = -INFINITY-1;

        Move m = new Move();
        _nodesChecked = 0;
        _nodesPruned = 0;

        ArrayList<Move> moves = Referee.getValidMoves(_board);
        if (Debug) Log.d(TAG, "POSSIBLE MOVES: " + moves.size());
        for (int i = 0; i < moves.size(); i++)
        {
            Board b2 = _board.copy();
            b2.move(moves.get(i));
            //b2.setSilverToMove(!b2.silverToMove());
            score = alphaBeta(b2, -INFINITY, INFINITY, 0);
            //score = AlphaBeta(!IsSilver, b2, -INFINITY, INFINITY, 0);


            // Max
            if (_color == Piece.Red)
            {
                if (score > threshold)
                {
                    m = moves.get(i);
                    m.score = score;
                    threshold = score;
                }
            }
            // Min
            else
            {
                if (score < threshold)
                {
                    m = moves.get(i);
                    m.score = score;
                    threshold = score;
                }
            }
        }
        return m;
	}
	
	private int alphaBeta(Object object, int alpha, int beta, int depth)
    {
		Board b = (Board)object;
        _nodesChecked++;
        if (depth >= _maxDepth)
            return evaluateBoard(b);
        if (Referee.isRedDead(b))
            return -INFINITY;
        if (Referee.isSilverDead(b))
            return INFINITY;            
        
        ArrayList<Move> moves = Referee.getValidMoves(b);
        // Max
        if (!b.silverToMove())
        {
            for (int i = 0; i < moves.size(); i++)
            {
                Board b2 = b.copy();
                b2.move(moves.get(i));
                //b2.setSilverToMove(!b2.silverToMove());

                int score = alphaBeta(b2, alpha, beta, depth + 1);

                if (score > alpha)
                    alpha = score;
                if (alpha >= beta)
                {
                    _nodesPruned += moves.size() - 1 - i;
                    return alpha;
                }
            }
            return alpha;
        }
        // Min
        else
        {
            for (int i = 0; i < moves.size(); i++)
            {
                Board b2 = b.copy();
                b2.move(moves.get(i));
                //b2.setSilverToMove(!b2.silverToMove());
                
                int score = alphaBeta(b2, alpha, beta, depth + 1);

                if (score < beta)
                    beta = score;
                if (alpha >= beta)
                {
                    _nodesPruned += moves.size() - 1 - i;
                    return beta;
                }
            }
            return beta;
        }
    }
	
	private int evaluateBoard(Board b){
		if (Referee.isSilverDead(b))
            return INFINITY;
        if (Referee.isRedDead(b))
            return -INFINITY;

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
	
	 private int scoreMaterial(Board b)
     {
         int score = 0;
         // Positions
         for (int i = 0; i < Board.tiles; i++)
         {
        	 Piece p = b.getPieceAtIndex(i);
        	 if(p.type() == Piece.Pyramid && p.color() == Piece.Silver){
        		 score -= _scorePyramid;
        	 } else if(p.type() == Piece.Pyramid && p.color() == Piece.Red){
        		 score += _scorePyramid;
        	 } else if(p.type() == Piece.DoubleObelisk && p.color() == Piece.Silver){
        		 score -= _scoreDoubleObelisk;
        	 } else if(p.type() == Piece.DoubleObelisk && p.color() == Piece.Red){
        		 score += _scoreDoubleObelisk;
        	 } else if(p.type() == Piece.SingleObelisk && p.color() == Piece.Silver){
        		 score -= _scoreSingleObelisk;
        	 } else if(p.type() == Piece.SingleObelisk && p.color() == Piece.Red){
        		 score += _scoreSingleObelisk;
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
		 
		 Bitboard redPyramidsOnHome = Bitboard.and(b.getBitboardByColor(Piece.Red), Helper.getRedHome());
		 Bitboard silverPyramidsOnHome = Bitboard.and(b.getBitboardByColor(Piece.Silver), Helper.getSilverHome());
		 
		 for(int i = 0; i < Board.tiles; i++){
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
