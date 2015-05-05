package combined;

import cz.cuni.mff.d3s.irmsa.strategies.correlation.metric.Metric;

public class LocationMetric implements Metric {

	static public double distance(final Location pos1, final Location pos2) {
		//TODO maybe return corridor distance not euclidean?
		return PositionMetric.distance(pos1.toPosition(), pos2.toPosition());
	}

	@Override
	public double distance(Object value1, Object value2) {
		if(!(value1 instanceof Location) || !(value2 instanceof Location))
			throw new IllegalArgumentException("Can't compute a distance of anything else than Locations.");

		final Location pos1 = (Location) value1;
		final Location pos2 = (Location) value2;
		return distance(pos1, pos2);
	}
}
