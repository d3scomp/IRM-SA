package combined;

import combined.HeatMap.Point;

public class PositionEnvironment implements Cloneable {

	/** Segment id. */
	public int segment;

	/** Position inside segment. */
	public double index;

	/**
	 * Only constructor.
	 */
	public PositionEnvironment(final int segment, final double index) {
		this.segment = segment;
		this.index = index;
	}
	
	public PositionComponent toPositionComponent() {
		final Point p = HeatMap.SEGMENTS[segment].toPoint(index);
		return new PositionComponent(p.x,  p.y);
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
		if (!(obj instanceof PositionEnvironment)) {
			return false;
		}
		PositionEnvironment other = (PositionEnvironment) obj;
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
	public PositionEnvironment clone() {
		try {
			return (PositionEnvironment) super.clone();
		} catch (CloneNotSupportedException e) {
			return null; //should never happen
		}
	}
}
