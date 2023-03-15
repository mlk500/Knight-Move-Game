package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class Horse {
	// Horse possible moves
	public static final ArrayList<Integer> DROWHORSELV234 = new ArrayList<Integer>(Arrays.asList(-3, -3, -1, -1, 1, 1,
			3, 3, -1, 1, -1, -1, -3, -2, -2, -1, -3, -2, -2, 3, 1, 2, 2, 3, 1, 2, 2));
	public static final ArrayList<Integer> DCOLHORSELV234 = new ArrayList<Integer>(Arrays.asList(1, -1, -1, 1, 1, -1,
			-1, 1, -3, -3, 3, -2, -2, -3, -1, 2, 2, 1, 3, -2, -2, -3, -1, 2, 2, 1, 3));
	public static final ArrayList<Integer> DROWHORSELV1 = new ArrayList<Integer>(
			Arrays.asList(-2, -2, -1, 1, 2, 2, -1, 1));
	public static final ArrayList<Integer> DCOLHORSELV1 = new ArrayList<Integer>(
			Arrays.asList(-1, 1, 2, 2, 1, -1, -2, -2));

	private static int idCounter = 1;
	private int id;
	private String Nickname;
	private Tile CurrTile;
	private HashSet<Position> visitedPositions;
	private Boolean victoryCup = false;

	// Constructors
	public Horse(String nickname, Tile currTile) {
		super();
		visitedPositions = new HashSet<Position>();
		this.id = idCounter++;
		Nickname = nickname;
		CurrTile = currTile;
	}

	public Horse(Boolean victoryCup) {
		super();
		this.setVictoryCup(victoryCup);
	}

	public static int getIdCounter() {
		return idCounter;
	}

	public static void setIdCounter(int idCounter) {
		Horse.idCounter = idCounter;
	}

	public HashSet<Position> getVisitedPositions() {
		return visitedPositions;
	}

	public void setVisitedPositions(HashSet<Position> visitedPositions) {
		this.visitedPositions = visitedPositions;
	}

	// Getter and Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public Tile getCurrTile() {
		return CurrTile;
	}

	public void setCurrTile(Tile currTile) {
		CurrTile = currTile;
	}

	// hash code
	@Override
	public int hashCode() {
		return Objects.hash(CurrTile, Nickname, id);
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
		Horse other = (Horse) obj;
		return Objects.equals(CurrTile, other.CurrTile) && Objects.equals(Nickname, other.Nickname) && id == other.id;
	}

	@Override
	public String toString() {
		return " visitedPositions=" + visitedPositions + "]";
	}

	// check if the tile is Forget Tile
	public boolean isForgetfulTile(int[] listForgetfulTile) {
		if (findIndex(listForgetfulTile, this.CurrTile.getId()) == -1) {
			return (false);
		} else {
			return (true);
		}
	}

	// check if the tile is Forget Tile
	public boolean IsRandomJumpTile(int[] listRandomJumpTile) {
		if (findIndex(listRandomJumpTile, this.CurrTile.getId()) == -1) {
			return (false);
		} else {
			return (true);
		}
	}

	// function take number and list , return the place of number in the list
	public static int findIndex(int arr[], int t) {
		// if array is Null
		if (arr == null) {
			return -1;
		}
		// find length of array
		int len = arr.length;
		int i = 0;
		// traverse in the array
		while (i < len) {
			// if the i-th element is t
			// then return the index
			if (arr[i] == t) {
				return i;
			} else {
				i = i + 1;
			}
		}
		return -1;
	}

	public Boolean getVictoryCup() {
		return victoryCup;
	}

	public void setVictoryCup(Boolean victoryCup) {
		this.victoryCup = victoryCup;
	}

}
