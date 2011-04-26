package com.behindcurtain3.khet.player;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.engine.ExNihilo;

public class ComputerPlayer implements Player {	
	private boolean Debug = true;
	
	private ExNihilo engine;
	
	private int _color;
	private Board _board; // Hold our copy of the board	
	private Move _moveSubmitted; // Holds the move we submit to the controller

	public ComputerPlayer(){
		engine = new ExNihilo();
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
		System.out.println("Game over, you lose!");
		if (Debug) printGameSummary();
	}

	@Override
	public void gameResigned() {
		System.out.println("The opponent has resigned.");
		if (Debug) printGameSummary();
	}

	@Override
	public void gameWon() {
		System.out.println("Game over, you win!");
		if (Debug) printGameSummary();
	}

	@Override
	public Move getMove(Board board) {
		_board = board.copy();		
		_moveSubmitted = engine.generateMove(_board, _color);	
		
		if(Debug) printMoveSummary();
        
		return _moveSubmitted;
	}

	@Override
	public void illegalMove(String arg0) {
		System.out.println("*** ILLEGAL MOVE ***");
		System.out.println("Move: " + _moveSubmitted.from + "->" + _moveSubmitted.to);
		System.out.println("Direction: " + _moveSubmitted.compass.getDirection());
	}

	@Override
	public void laserFired(ArrayList<Integer> list) {

	}

	@Override
	public void opponentMove(Move m) {
		_board.move(m);
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
			_moveSubmitted = null;
		}			
	}
	
	private void printGameSummary(){
		System.out.println("-------------------------- GAME SUMMARY -------------------------------");
		System.out.println("Color: " + _color);
    	System.out.println("Moves: " + engine.getNumberOfMovesGenerated());    	
    	System.out.println("Total Time: " + engine.getTotalGenerationTime());
    	System.out.println("Avg Time: " + engine.getAvgGenerationTime());
    	System.out.println("-------------------------- END SUMMARY ---------------------------------");
	}
	
	private void printMoveSummary(){
		System.out.println("----------------------------- MOVE SUMMARY --------------------------------");
		System.out.println("MOVE SCORE: " + _moveSubmitted.score);
    	System.out.println("NODES CHECKED: " + engine.getNodesChecked());
    	System.out.println("NODES PRUNED: " + engine.getNodesPruned());
    	System.out.println("TIME: " + engine.getPreviousGenerationTime() + " AVG: " + engine.getAvgGenerationTime());
    	System.out.println("Total: " + engine.getTotalGenerationTime() + " Turns: " + engine.getNumberOfMovesGenerated());
    	System.out.println("--------------------------------- END -------------------------------------");
	}
}
