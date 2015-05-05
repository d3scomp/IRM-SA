package filter;

public class TemperatureNoise extends Filter<Double> {

	private RandomNoise noise;
	
	public TemperatureNoise(double mean, double deviation) {
		noise = new RandomNoise(mean, deviation);
	}
	
	public TemperatureNoise(double mean, double deviation, Filter<Double> innerFilter) {
		super(innerFilter);
		noise = new RandomNoise(mean, deviation);
	}

	@Override
	protected Double applyNoise(Double data) {
		return (data + noise.getNoise());
	}
}
