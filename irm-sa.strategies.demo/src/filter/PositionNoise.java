package filter;

import combined.PositionComponent;

public class PositionNoise extends Filter<PositionComponent> {

	/**
	 * Generator of random numbers with the normal distribution.
	 */
	private RandomNoise noise;
	
	/**
	 * Create new instance of {@link PositionNoise} with the normal
	 * distribution with specified mean and standard deviation.
	 * 
	 * @param mean The mean of the normal distribution.
	 * @param deviation The standard deviation of the normal distribution.
	 */
	public PositionNoise(double mean, double deviation) {
		noise = new RandomNoise(mean, deviation);
	}
	
	/**
	 * Create new instance of {@link PositionNoise} with the normal
	 * distribution with specified mean and standard deviation.
	 * The innerFilter specifies the chain of filters inside this instance.
	 * 
	 * @param mean The mean of the normal distribution.
	 * @param deviation The standard deviation of the normal distribution.
	 * @param innerFilter The chain of filters inside this instance.
	 */
	public PositionNoise(double mean, double deviation, Filter<PositionComponent> innerFilter) {
		super(innerFilter);
		noise = new RandomNoise(mean, deviation);
	}

	/**
	 * Apply noise defined by this instance to the given data.
	 * 
	 * @param data The data to apply the filter at.
	 * @return The given data with applied noise.
	 */
	@Override
	protected PositionComponent applyNoise(final PositionComponent data) {
		// horizontal noise
		double h = noise.getNoise();
		// vertical noise
		double v = noise.getNoise();
		
		return new PositionComponent(data.x + h, data.y + v);
	}
}
