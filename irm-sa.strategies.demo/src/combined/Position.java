package combined;

import java.io.Serializable;

import combined.HeatMap.Point;
import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;

public class Position extends KnowledgeImpl implements Serializable, Cloneable {

	private static final long serialVersionUID = 3725325029322368894L;

	/** Segment id. */
	public int segment;

	/** Position inside segment. */
	public int index;

	/**
	 * Only constructor.
	 */
	public Position(final int segment, final int index) {
		this.name = "PositionKnowledge";
		this.type = "PositionKnowledgeType";
		this.segment = segment;
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		try {
			final Point p = HeatMap.SEGMENTS[segment].toPoint(index);
			return "[" + p.x + ", " + p.y + "]";
		} catch (IndexOutOfBoundsException e) {
			return "{ segment = " + segment + ", index = " + index + "}";
		}
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
