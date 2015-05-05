package combined;

import cz.cuni.mff.d3s.irmsa.strategies.correlation.metric.Metric;

public class PositionMetric implements Metric {

	static public double distance(final Position pos1, final Position pos2) {
		return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
	}

	@Override
	public double distance(Object value1, Object value2) {
		if(!(value1 instanceof Position) || !(value2 instanceof Position))
			throw new IllegalArgumentException("Can't compute a distance of anything else than Positions.");

		final Position pos1 = (Position) value1;
		final Position pos2 = (Position) value2;
		return distance(pos1, pos2);
	}
}
