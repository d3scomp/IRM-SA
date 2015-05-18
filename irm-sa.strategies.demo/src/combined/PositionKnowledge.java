package combined;

/**
 * Extension of Position containing also information about sensor inaccuracy.
 */
public class PositionKnowledge extends Position {

	/** Generated UID. */
	private static final long serialVersionUID = -8628242433411285211L;

	/** Sensor inaccuracy. */
	public final Double inaccuracy;

	/**
	 * Create from coordinates.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param inaccuracy sensor inaccuracy
	 */
	public PositionKnowledge(final double x, final double y, final double inaccuracy) {
		super(x, y);
		this.inaccuracy = inaccuracy;
	}

	/**
	 * Create from Position.
	 * @param position Position blueprint
	 * @param inaccuracy sensor inaccuracy
	 */
	public PositionKnowledge(final Position position, final double inaccuracy) {
		super(position.x, position.y);
		this.inaccuracy = inaccuracy;
	}

	@Override
	public PositionKnowledge clone() {
		return (PositionKnowledge) super.clone();
	}
}
