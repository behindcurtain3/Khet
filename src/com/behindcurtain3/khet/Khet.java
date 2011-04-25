package com.behindcurtain3.khet;

import com.behindcurtain3.khet.controller.KhetController;
import com.behindcurtain3.khet.player.ComputerPlayer;
import com.behindcurtain3.khet.player.Player;
import com.behindcurtain3.khet.util.BoardHelper;

public class Khet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Player p1 = new ComputerPlayer();
		Player p2 = new ComputerPlayer();
		
		KhetController game = new KhetController();
		game.startNewGame(p1, p2, BoardHelper.getInstance().getStandardConfig());
	}

}
