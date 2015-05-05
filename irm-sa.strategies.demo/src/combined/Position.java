package combined;

public class Position implements Cloneable {

	/** Segment id. */
	public int segment;

	/** Position inside segment. */
	public double index;

	/**
	 * Only constructor.
	 */
	public Position(final int segment, final double index) {
		this.segment = segment;
		this.index = index;
	}
	
	public Location toPositionComponent() {
		return HeatMap.SEGMENTS[segment].toPosition(index);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Double.hashCode(index);
		result = prime * result + segment;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		if (index != other.index) {
			return false;
		}
		if (segment != other.segment) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("{ segment = %d, index = %.4f }", segment, index);
	}

	@Override
	public Position clone() {
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			return null; //should never happen
		}
	}
}
