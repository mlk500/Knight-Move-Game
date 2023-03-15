package model;

public class Tile {

	private static int idCounter=1 ;
	private int id;
	private Boolean visited=false;
	private Tile parent;
	private int score;
	Position pos;

	// Constructors
	public Tile() {
		super();
		this.id = idCounter++;
	}
	public Tile(int id) {
		super();
		this.id = id;
	}
	
	public Tile( int x, int y, Tile parent,Boolean visited) {
		super();
		pos=new Position(x, y);
		this.setParent(parent);
		this.visited = visited;
	}
	public Tile( int x, int y, Tile parent) {
		super();
		pos=new Position(x, y);
		this.setParent(parent);
	}
	public Tile( int x, int y) {
		super();
		pos=new Position(x, y);
	}
	
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
	// Getter and Setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Boolean getVisited() {
		return visited;
	}
	public void setVisited(Boolean visited) {
		this.visited = visited;
	}
	
	public static int getIdCounter() {
		return idCounter;
	}
	public static void setIdCounter(int idCounter) {
		Tile.idCounter = idCounter;
	}
	
	public Tile getParent() {
		return parent;
	}
	public void setParent(Tile parent) {
		this.parent = parent;
	}
	@Override
	public String toString() {
		return "Tile [id=" + id + ", Visited=" + visited + ", parent=" + parent + ", pos=" + pos + "]";
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
