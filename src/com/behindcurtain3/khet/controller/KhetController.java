package com.behindcurtain3.khet.controller;

import java.util.ArrayList;

import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.engine.Referee;
import com.behindcurtain3.khet.engine.RuleBook;
import com.behindcurtain3.khet.player.Player;
import com.behindcurtain3.khet.util.BoardHelper;

public class KhetController implements Controller {
	private Referee _ref;
	private Player _silver;
	private Player _red;
	private Player _currentPlayer;	// Holds whichever Player turn it is
	private Player _opponent;		// Holds opposite Player as _currentPlayer
	
	private Boolean _gameOver = false;
	private int _badMovesSubmitted = 0;
	private int _numTurns = 0;
	
	public KhetController(){
		
	}

	@Override
	public void startNewGame(Player one, Player two, ArrayList<Piece> config) {
		_silver = one;
		_red = two;
		
		// Tell the players their color
		_silver.setColor(Piece.Silver);
		_red.setColor(Piece.Red);
		
		// Set current player
		_currentPlayer = _silver;
		_opponent = _red;
		
		// Setup ref & helper
		_ref = Referee.getInstance();
		_ref.startnewGame(config);
		
		_silver.setBoard(_ref.getBoard());
		_red.setBoard(_ref.getBoard());
		
		// Mark the game as not over
		_gameOver = false;
		
		// Start the game
		play();
	}
	
	public void play(){
		do{
			_badMovesSubmitted = 0;
			turn();
			_numTurns++;
			BoardHelper.getInstance().printBoard(_ref.getBoard());
		}while(!_gameOver);
	}
	
	/*
	 * Completes one turn per call
	 * 1. Gets the current players move
	 * 	a. if draw or resign then handle accordingly
	 *  b. if illegal move then update player and request new move
	 * 2. Updates board and sends updates to players
	 * 	a. do the move
	 *  b. fire laser
	 *  c. send updates to players
	 *  d. including win/lose conditions
	 * 3. Switches currentplayer & opponent
	 */
	private void turn(){
		Move requestedMove;
		Boolean turnCompleted = false;
		do{
			requestedMove = _currentPlayer.getMove(_ref.getBoard());
			if(requestedMove.requestDraw){
				// If player requests a draw query their opponent for acceptance
				Boolean drawAccepted = _opponent.opponentRequestDraw();
				_currentPlayer.drawRequestStatus(drawAccepted); // report draw status to player
				if(drawAccepted){
					_currentPlayer.gameDrawn();
					_opponent.gameDrawn();
					onDraw(); // game is drawn
				} else {
					turnCompleted = false;
				}
			} else if(requestedMove.resign){
				_opponent.gameResigned();
				onResign();
			} else {
				if(_ref.attemptMove(requestedMove)){
					_currentPlayer.validMove(); // let the player know their move was good
					_currentPlayer.laserFired(_ref.getBoard().getLaserPath());
					
					_opponent.opponentMove(requestedMove); // let the opponent know of the move
					_opponent.laserFired(_ref.getBoard().getLaserPath());
					turnCompleted = true;
					
					// Check for game over conditions
					if(RuleBook.isRedDead(_ref.getBoard())){
						_silver.gameWon();
						_red.gameLost();
						onGameOver();
					} else if(RuleBook.isSilverDead(_ref.getBoard())){
						_silver.gameLost();
						_red.gameWon();
						onGameOver();
					}					
				} else {
					_currentPlayer.illegalMove("Move submitted was not approved by the referee.");
					_badMovesSubmitted++;
					if(_badMovesSubmitted >= 10){
						_currentPlayer.gameLost();
						_opponent.gameWon();
						onGameOver();
						turnCompleted = true;
					}
				}
			}
		}while(!turnCompleted);
		
		// Switch the players
		Player dummy = _currentPlayer;
		_currentPlayer = _opponent;
		_opponent = dummy;
		
	}	
	
	private void onDraw(){
		_gameOver = true;
	}
	
	private void onResign(){
		_gameOver = true;
	}
	
	private void onGameOver(){
		_gameOver = true;
		System.out.println("Total Turns: " + _numTurns);
	}
	
	
}
