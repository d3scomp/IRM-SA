package combined;

import cz.cuni.mff.d3s.irmsa.strategies.correlation.metric.Metric;

public class CoordinateMetric implements Metric {

	static public double distance(final PositionComponent pos1, final PositionComponent pos2) {
		return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
	}

	@Override
	public double distance(Object value1, Object value2) {
		if(!(value1 instanceof PositionComponent) || !(value2 instanceof PositionComponent))
			throw new IllegalArgumentException("Can't compute a distance of anything else than Positions.");

		final PositionComponent pos1 = (PositionComponent) value1;
		final PositionComponent pos2 = (PositionComponent) value2;
		return distance(pos1, pos2);
	}
}
