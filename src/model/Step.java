package model;

import java.util.Objects;

public class Step {

	private int point;
	private Tile  tile;
	
	// Constructors
	public Step(int point, Tile tile) {
		super();
		this.point = point;
		this.tile = tile;
	}
	public Step() {
		super();
	}

	// Getter and Setter
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public Tile getTile() {
		return tile;
	}
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	// hash code
	@Override
	public int hashCode() {
		return Objects.hash(point, tile);
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
		Step other = (Step) obj;
		return point == other.point && Objects.equals(tile, other.tile);
	}
	
	
	
	
}
