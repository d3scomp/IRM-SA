package combined;

import combined.HeatMap.Point;

import cz.cuni.mff.d3s.irmsa.strategies.correlation.metric.Metric;

public class PositionMetric implements Metric {

	static public double distance(final Position pos1, final Position pos2) {
		final Point point1 = HeatMap.SEGMENTS[pos1.segment].toPoint(pos1.index);
		final Point point2 = HeatMap.SEGMENTS[pos2.segment].toPoint(pos2.index);
		return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
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
