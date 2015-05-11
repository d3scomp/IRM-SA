package combined;

/**
 * Class representing coordinates in Corridor coordinate system.
 */
public class Location implements Cloneable {

	/** Corridor id. */
	public int corridor;

	/** Position inside corridor. */
	public double index;

	/**
	 * Only constructor.
	 * @param corridor corridor id
	 * @param index position inside corridor
	 */
	public Location(final int corridor, final double index) {
		this.corridor = corridor;
		this.index = index;
	}

	/**
	 * Converts Location into Position.
	 * @return position corresponding to this Location
	 */
	public Position toPosition() {
		return HeatMap.CORRIDORS.get(corridor).toPosition(index);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Double.hashCode(index);
		result = prime * result + corridor;
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
		if (!(obj instanceof Location)) {
			return false;
		}
		Location other = (Location) obj;
		if (index != other.index) {
			return false;
		}
		if (corridor != other.corridor) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("{ corridor = %d, index = %.4f }", corridor, index);
	}

	@Override
	public Location clone() {
		try {
			return (Location) super.clone();
		} catch (CloneNotSupportedException e) {
			return null; //should never happen
		}
	}
}
