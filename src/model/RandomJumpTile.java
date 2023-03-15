package model;

import java.util.Random;

public class RandomJumpTile extends Tile{
	
	public RandomJumpTile(int idTile) {
		super(idTile);
		// TODO Auto-generated constructor stub
	}
	
	// function take the place of queen or king and choose the tile Different from its location is the place of the king and queen
	public int nextTile(int place_king_or_queen) {	
		int newTile;
		do {
			// choose random number
			newTile=getRandomNumberUsingNextInt(1,65);
		}while((newTile == super.getId()) || (newTile == place_king_or_queen)); // Choose again if the place is problematic
		return(newTile);
	}
	
	// function take Number range and return random number
	public int getRandomNumberUsingNextInt(int min, int max) {
   	    Random random = new Random();
   	    return random.nextInt(max - min) + min;
   	}
}
