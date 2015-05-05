package combined;

import cz.cuni.mff.d3s.irmsa.strategies.correlation.metric.Metric;

public class PositionMetric implements Metric {

	static public double distance(final PositionEnvironment pos1, final PositionEnvironment pos2) {
		return CoordinateMetric.distance(pos1.toPositionComponent(), pos2.toPositionComponent());
	}

	@Override
	public double distance(Object value1, Object value2) {
		if(!(value1 instanceof PositionEnvironment) || !(value2 instanceof PositionEnvironment))
			throw new IllegalArgumentException("Can't compute a distance of anything else than Positions.");

		final PositionEnvironment pos1 = (PositionEnvironment) value1;
		final PositionEnvironment pos2 = (PositionEnvironment) value2;
		return distance(pos1, pos2);
	}
}
