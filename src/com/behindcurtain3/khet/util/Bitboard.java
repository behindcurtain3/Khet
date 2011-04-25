package com.behindcurtain3.khet.util;

import java.util.ArrayList;

import com.behindcurtain3.khet.Board;

public class Bitboard extends ArrayList<Boolean> {
	public Boolean valid = false;
	
	
	public Bitboard(){
		reset();
	}	
	
	public void reset(){
		this.clear();
		for(int i = 0; i < Board.tiles; i++)
			this.add(false);
	}
	
	public static Bitboard and(Bitboard a, Bitboard b){
		Bitboard result = new Bitboard();
        for(int i = 0; i < a.size(); i++)
        {
            if (result.size() == i)
                result.add(false);

            if(a.get(i) && b.get(i))
                result.set(i, true);
        }
        return result;
	}
	
	public static Bitboard or(Bitboard a, Bitboard b)
    {
        Bitboard result = new Bitboard();
        for (int i = 0; i < a.size(); i++)
        {
            if (result.size() == i)
                result.add(false);

            if (a.get(i) || b.get(i))
                result.set(i, true);
        }
        return result;
    }
	
	public Boolean equalTo(Bitboard b){
		
		for(int i = 0; i < this.size(); i++){
			if(this.get(i) != b.get(i))
				return false;
		}		
		
		return true;
	}
	
	public Bitboard copy(){
		Bitboard result = new Bitboard();
		
		for(int i = 0; i < size(); i++){
			result.add(this.get(i));
		}
		
		return result;
	}
	
	public static Bitboard getEmptyBitboard(){
		return new Bitboard();
	}
}
