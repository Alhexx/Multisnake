package multisnake;

public class Food {

	private Pos position;
	
	public Food(Pos position) {
		this.position = position;
	}

	public Pos pos() {
		return position;
	}

	public int power() {
		return 1;
	}

}
