package com.behindcurtain3.khet.engine;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;
import com.behindcurtain3.khet.Move;
import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.util.Bitboard;
import com.behindcurtain3.khet.util.BoardHelper;
import com.behindcurtain3.khet.util.Compass;
import com.behindcurtain3.khet.util.RefereeHelper;

public class Referee {
	private static Referee instance = null;
	
	private ArrayList<Move> _moveLog;
	private Board _masterBoard = new Board();
	private Bitboard _silverTrespassing;
	private Bitboard _redTrespassing;
	
	protected Referee(){
		init();
	}
	
	public static Referee getInstance(){
		if(instance == null){
			instance = new Referee();
		}
		return instance;
	}
	
	private void init(){
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
		if(RefereeHelper.isMoveValid(_masterBoard, move)){
			_masterBoard.move(move);
			_moveLog.add(move);
			return true;
		}
		return false;
	}
	
	public Boolean isSilverTrespassing(int index){
		return _silverTrespassing.get(index);
	}
	
	public Boolean isRedTrespassing(int index){
		return _redTrespassing.get(index);
	}

}
