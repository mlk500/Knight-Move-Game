package model;

import java.util.ArrayList;
import java.util.Objects;

public class Queen {

	private Tile CurrTile;

	// Constructor
	public Queen(Tile currTile) {
		super();
		CurrTile = currTile;
	}

	// Setter and Getter
	public Tile getCurrTile() {
		return CurrTile;
	}

	public void setCurrTile(Tile currTile) {
		CurrTile = currTile;
	}

	// hash code
	@Override
	public int hashCode() {
		return Objects.hash(CurrTile);
	}

	// equals
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Queen other = (Queen) obj;
		return Objects.equals(CurrTile, other.CurrTile);
	}
	// -------------------------------------------------------------------------------------------------------------------------

	public ArrayList<Integer> getQueenPos(String rowOrCol) {
		ArrayList<Integer> queenRow = new ArrayList<>();
		ArrayList<Integer> queencol = new ArrayList<>();
		int currI = getCurrTile().getPos().getX();
		int currJ = getCurrTile().getPos().getY();
		for (int i = 1; i < 8; i++) {

			if (currI + i < 8) {
				queenRow.add(i);
				queencol.add(0);
				if (currJ + i < 8) {
					queenRow.add(i);
					queencol.add(i);
				}
				if (currJ - i > 0) {
					queenRow.add(i);
					queencol.add(-1 * i);
				}
			}

			if (currI - i > 0) {
				queenRow.add(-1 * i);
				queencol.add(0);
				if (currJ - i > 0) {
					queenRow.add(-1 * i);
					queencol.add(-1 * i);
				}
				if (currJ + i < 8) {
					queenRow.add(-1 * i);
					queencol.add(i);
				}

			}
			if (currJ - i > 0) {
				queenRow.add(0);
				queencol.add(-1 * i);
			}
			if (currJ + i < 8) {
				queenRow.add(0);
				queencol.add(i);
			}

		}

		if (rowOrCol == "row") {
			return queenRow;
		} else {
			return queencol;
		}

	}
}
