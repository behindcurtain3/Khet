package com.behindcurtain3.khet.controller;

import java.util.ArrayList;

import com.behindcurtain3.khet.Piece;
import com.behindcurtain3.khet.player.Player;

public interface Controller {
	public void startNewGame(Player one, Player two, ArrayList<Piece> config);
	public void play();
}
