package com.behindcurtain3.khet.player;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.engine.Evaluator;
import com.behindcurtain3.khet.engine.Referee;
import com.behindcurtain3.khet.util.Math;

public class ComputerPlayer implements Player {
	// Debugging
	private final Boolean Debug = true;
	private int _nodesChecked;
	private int _nodesPruned;
	
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
		System.out.print("Game over, you lose!");
	}

	@Override
	public void gameResigned() {
		System.out.print("The opponent has resigned.");

	}

	@Override
	public void gameWon() {
		System.out.print("Game over, you win!");
	}

	@Override
	public Move getMove(Board board) {
		_board = board.copy();
		if (Debug) System.out.print("-------------------------- GENERATING MOVES -------------------------------");
		long start = System.currentTimeMillis();
        //Move moveToMake = MaxMove(Board, IsSilver, 0);
        Move moveToMake = generateMove();
        if (Debug) System.out.print("MOVE SCORE: " + moveToMake.score);
        if (Debug) System.out.print("NODES CHECKED: " + _nodesChecked);
        if (Debug) System.out.print("NODES PRUNED: " + _nodesPruned);
        if (Debug) System.out.print("TIME: " + (System.currentTimeMillis() - start));
        if (Debug) System.out.print("--------------------------------- END -------------------------------------");
		
		//ArrayList<Move> moves = Referee.getValidMoves(board);
		//System.out.print(TAG, "Moves available: " + moves.size());
		
        _moveSubmitted = moveToMake;
        
		return moveToMake;
	}

	@Override
	public void illegalMove(String arg0) {
		System.out.print("*** ILLEGAL MOVE ***");
		System.out.print("Move: " + _moveSubmitted.from + "->" + _moveSubmitted.to);
		System.out.print("Direction: " + _moveSubmitted.compass.getDirection());
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
	}
	
	@Override
	public void setBoard(Board b) {
		_board = b.copy();
	}

	@Override
	public void validMove() {
		if(_moveSubmitted != null){
			System.out.print("--- Move Complete --- " + _color);
			//_board.move(_moveSubmitted);
			//_board.setSilverToMove(!_board.silverToMove());
			_moveSubmitted = null;
		}			
	}
	
	private Move generateMove(){
		int score;
        int threshold;
        if (_color == Piece.Silver)
            threshold = Math.INFINITY+1;
        else
            threshold = -Math.INFINITY-1;

        Move m = new Move();
        _nodesChecked = 0;
        _nodesPruned = 0;

        ArrayList<Move> moves = Referee.getValidMoves(_board);
        if (Debug) System.out.print("POSSIBLE MOVES: " + moves.size());
        for (int i = 0; i < moves.size(); i++)
        {
            Board b2 = _board.copy();
            b2.move(moves.get(i));
            //b2.setSilverToMove(!b2.silverToMove());
            score = alphaBeta(b2, -Math.INFINITY, Math.INFINITY, 0);
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
            return Evaluator.getInstance().score(b);
        if (Referee.isRedDead(b))
            return -Math.INFINITY;
        if (Referee.isSilverDead(b))
            return Math.INFINITY;            
        
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

}
