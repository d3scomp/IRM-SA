package combined;

import java.io.Serializable;

import combined.HeatMap.Point;

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;

public class Position extends KnowledgeImpl implements Serializable {

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
	public String toString() {
		final Point p = HeatMap.SEGMENTS[segment].toPoint(index);
		return "[" + p.x + ", " + p.y + "]";
	}
}
