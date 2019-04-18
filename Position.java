
public class Position {

	private int xCor;
	private int yCor;
	private Platser plats = null;

	Position(int x, int y) {
		xCor = x;
		yCor = y;

	}

	public int getYCor() {
		return yCor;
	}

	public int getXCor() {
		return xCor;
	}

	public void setPlats(Platser p) {
		plats = p;
	}

	public Platser getPlats() {
		return plats;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Position) {
			Position p = (Position)other;	
			if (xCor == p.xCor && yCor == p.yCor) {
			return true;
		}
		}
			return false;
		
	}

	public int hashCode() {
		return xCor * 10000 + yCor;
	}
}
