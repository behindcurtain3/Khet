package com.behindcurtain3.khet.player;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;

/*
 * Typical turn goes like this:
 * Controller: player->getMove();
 * Controller: player->validMove();
 * 				or player->illegalMove();
 * Controller: otherplayer->updateBoard(Move); send the move the other player just did
 */
public interface Player {
	// Setup
	public void setColor(int color);
	public void setBoard(Board b);
	
	// During game
	// Players turn
	public Move getMove(Board b);
	public void validMove(); // Move was legal
	public void illegalMove(String reason); // Move submitted was illegal
	public void drawRequestStatus(Boolean status); // True if accepted, false if denied
	
	// Opponents turn
	public void opponentMove(Move move);
	
	// Both
	public void laserFired(ArrayList<Integer> laserPath);
	
	// Requests
	public Boolean opponentRequestDraw();
	
	// Game statuses
	public void gameDrawn();
	public void gameLost();
	public void gameWon();
	public void gameResigned();
}
