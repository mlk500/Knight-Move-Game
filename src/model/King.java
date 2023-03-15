package model;

import java.util.ArrayList;
import java.util.Arrays;

public class King {
	public static final ArrayList<Integer> DROWKING = new ArrayList<Integer>(Arrays.asList(-1, 0, 1, 0, 1, 1, -1, -1));
	public static final ArrayList<Integer> DCOLKING = new ArrayList<Integer>(Arrays.asList(0, 1, 0, -1, 1, -1, 1, -1));
	private Tile CurrTile;
	Utilities utilities = Utilities.getInstance();

	public King(Tile currTile) {
		super();
		CurrTile = currTile;
	}

	public Tile getCurrTile() {
		return CurrTile;
	}

	public void setCurrTile(Tile currTile) {
		CurrTile = currTile;
	}

	@Override
	public String toString() {
		return "King2" + "[ CurrTile=" + CurrTile + "]";
	}

}
