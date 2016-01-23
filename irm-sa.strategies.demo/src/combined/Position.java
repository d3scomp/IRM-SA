package combined;

import java.io.Serializable;
import java.util.Locale;

/**
 * Class representing coordinates in cartesian coordinate system.
 */
public class Position implements Serializable, Cloneable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -5980471557237426848L;

	/** X-coordinate. */
	public final Double x;

	/** Y-coordinate. */
	public final Double y;

	/**
	 * Only constructor.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}


	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "[%.4f,%.4f]", x, y);
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