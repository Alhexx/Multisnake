package multisnake;

import java.io.Serializable;

public class Pos implements Serializable {

	public final int x;
	public final int y;

	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Pos of(int x, int y) {
		return new Pos(x, y);
	}

	public Pos add(Pos point) {
		return new Pos(this.x + point.x, this.y + point.y);
	}

	public Pos sub(Pos point) {
		return new Pos(this.x - point.x, this.y - point.y);
	}

	@Override
	public String toString() {
		return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
    		result = prime * result + (x ^ (x >>> 32));
    		result = prime * result + (y ^ (x >>> 32));
    		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null || getClass() != o.getClass()) {
			return false;
		} else {
			Pos point = (Pos) o;
			return this.x == point.x && this.y == point.y;
		}
	}

}
