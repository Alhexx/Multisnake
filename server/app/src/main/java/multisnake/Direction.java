package multisnake;

public enum Direction {

	N, S, E, W;

	public Pos normalVector() {
		return
			this == N ? Pos.of(0 , 1) :
			this == S ? Pos.of(0 ,-1) :
			this == E ? Pos.of(1, 0) :
			this == W ? Pos.of(-1 , 0) :
			null;
	}

	public Direction opposite() {
		return
			this == N ? S :
			this == S ? N :
			this == E ? W :
			this == W ? E :
			null;
	}

}
