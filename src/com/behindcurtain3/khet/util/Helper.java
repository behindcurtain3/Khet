package com.behindcurtain3.khet.util;

import java.util.ArrayList;

import com.behindcurtain3.khet.Piece;

public class Helper {
	
	private static Bitboard _silverHome;
	private static Bitboard _redHome;
	
	public static void init(){
		_silverHome = new Bitboard();
		_silverHome.set(9, true);
		_silverHome.set(19, true);
		_silverHome.set(29, true);
		_silverHome.set(39, true);
		_silverHome.set(49, true);
		_silverHome.set(59, true);
		_silverHome.set(69, true);
		_silverHome.set(79, true);
		
		_redHome = new Bitboard();
		_redHome.set(0, true);
		_redHome.set(10, true);
		_redHome.set(20, true);
		_redHome.set(30, true);
		_redHome.set(40, true);
		_redHome.set(50, true);
		_redHome.set(60, true);
		_redHome.set(70, true);
	}
	
	public static Bitboard getSilverHome(){
		return _silverHome;
	}
	public static Bitboard getRedHome(){
		return _redHome;
	}
	
	public static ArrayList<Piece> getStandardConfig(){
		ArrayList<Piece> config = new ArrayList<Piece>();
		for(int i = 0; i < 80; i++){
        	config.add(new Piece());
        }
		
		Piece p = new Piece();
		
		// RED
		// Pharoah
		p.setColor(Piece.Red);
		p.setType(Piece.Pharaoh);
		config.set(5, p);
		
		// Double Obelisks
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.DoubleObelisk);
		config.set(4, p);
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.DoubleObelisk);
		config.set(6, p);
		
		// Djeds
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Djed);
		p.NE = true;
		p.SW = true;
		config.set(34, p);
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Djed);
		p.SE = true;
		p.NW = true;
		config.set(35, p);
		
		// Pyramids
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Pyramid);
		p.SE = true;
		config.set(7, p);
		
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Pyramid);
		p.SW = true;
		config.set(12, p);
		
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Pyramid);
		p.NE = true;
		config.set(30, p);
		
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Pyramid);
		p.SE = true;
		config.set(40, p);
		
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Pyramid);
		p.SE = true;
		config.set(37, p);
		
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Pyramid);
		p.NE = true;
		config.set(47, p);
		
		p = new Piece();
		p.setColor(Piece.Red);
		p.setType(Piece.Pyramid);
		p.SE = true;
		config.set(56, p);
		
		// SILVER
		// Pharaoh
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pharaoh);
		config.set(74, p);
		
		// Double o's
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.DoubleObelisk);
		config.set(73, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.DoubleObelisk);
		config.set(75, p);

		// Djeds
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Djed);
		p.NW = true;
		p.SE = true;
		config.set(44, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Djed);
		p.NE = true;
		p.SW = true;
		config.set(45, p);
		
		// Pyramids
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pyramid);
		p.NW = true;
		config.set(23, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pyramid);
		p.SW = true;
		config.set(32, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pyramid);
		p.NW = true;
		config.set(42, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pyramid);
		p.NW = true;
		config.set(39, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pyramid);
		p.SW = true;
		config.set(49, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pyramid);
		p.NE = true;
		config.set(67, p);
		
		p = new Piece();
		p.setColor(Piece.Silver);
		p.setType(Piece.Pyramid);
		p.NW = true;
		config.set(72, p);
		
		return config;
	}
}
