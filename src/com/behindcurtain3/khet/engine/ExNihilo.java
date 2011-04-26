package com.behindcurtain3.khet.engine;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.util.Math;

/*
 * ExNihilo represent the game "engine"
 * It will take a Board object as input, evaluate it and return a move based on its configuration.
 * Currently uses alphabeta pruning
 */
public class ExNihilo {
	int _maxDepth;
	int _nodesChecked;
	int _nodesPruned;
	boolean debug = false;
	
	public ExNihilo(){
		_maxDepth = 2;
		
		_nodesChecked = 0;
		_nodesPruned = 0;
	}
	
	public int getNodesPruned(){
		return _nodesPruned;
	}
	public int getNodesChecked(){
		return _nodesChecked;
	}
	
	public void setSearchDepth(int d){
		_maxDepth = d;
	}
	public int getSearchDepth(){
		return _maxDepth;
	}
	
	/*
	 * @params
	 * Board b, the board object to work off of
	 * int c, the color side to generate a move for
	 */
	public Move generateMove(Board b, int c){
		int score;
        int threshold;
        if (c == Piece.Silver)
            threshold = Math.INFINITY+1;
        else
            threshold = -Math.INFINITY-1;

        Move m = new Move();
        _nodesChecked = 0;
        _nodesPruned = 0;

        ArrayList<Move> moves = RuleBook.getValidMoves(b);
        
        if (debug) System.out.println("POSSIBLE MOVES: " + moves.size());
        
        for (int i = 0; i < moves.size(); i++) {
            Board b2 = b.copy();
            b2.move(moves.get(i));
            score = alphaBeta(b2, -Math.INFINITY, Math.INFINITY, 0);

            // Max
            if (c == Piece.Red) {
                if (score > threshold) {
                    m = moves.get(i);
                    m.score = score;
                    threshold = score;
                }
            }
            // Min
            else {
                if (score < threshold) {
                    m = moves.get(i);
                    m.score = score;
                    threshold = score;
                }
            }
        }
        return m;
	}
	
	private int alphaBeta(Object object, int alpha, int beta, int depth) {
		Board b = (Board)object;
        _nodesChecked++;
        if (depth >= _maxDepth)
            return Evaluator.getInstance().score(b);
        if (RuleBook.isRedDead(b))
            return -Math.INFINITY;
        if (RuleBook.isSilverDead(b))
            return Math.INFINITY;            
        
        ArrayList<Move> moves = RuleBook.getValidMoves(b);
        
        // Max
        if (!b.silverToMove()) {
            for (int i = 0; i < moves.size(); i++) {
                Board b2 = b.copy();
                b2.move(moves.get(i));

                int score = alphaBeta(b2, alpha, beta, depth + 1);

                if (score > alpha)
                    alpha = score;
                if (alpha >= beta) {
                    _nodesPruned += moves.size() - 1 - i;
                    return alpha;
                }
            }
            return alpha;
        // Min
        } else {
            for (int i = 0; i < moves.size(); i++) {
                Board b2 = b.copy();
                b2.move(moves.get(i));
                
                int score = alphaBeta(b2, alpha, beta, depth + 1);

                if (score < beta)
                    beta = score;
                if (alpha >= beta) {
                    _nodesPruned += moves.size() - 1 - i;
                    return beta;
                }
            }
            return beta;
        }
    }
}
