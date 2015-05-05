package filter;

/**
 * Represents noise filter for the temperature reading sensors.
 * The level of noise is adjusted by the mean and deviation of the
 * normal distribution.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class DoubleNoise extends Filter<Double> {

	/**
	 * Generator of random numbers with the normal distribution.
	 */
	private NormalDistribution noise;
	
	/**
	 * Create new instance of {@link DoubleNoise} with the normal
	 * distribution with specified mean and standard deviation.
	 * 
	 * @param mean The mean of the normal distribution.
	 * @param deviation The standard deviation of the normal distribution.
	 */
	public DoubleNoise(double mean, double deviation) {
		noise = new NormalDistribution(mean, deviation);
	}
	
	/**
	 * Create new instance of {@link DoubleNoise} with the normal
	 * distribution with specified mean and standard deviation.
	 * The innerFilter specifies the chain of filters inside this instance.
	 * 
	 * @param mean The mean of the normal distribution.
	 * @param deviation The standard deviation of the normal distribution.
	 * @param innerFilter The chain of filters inside this instance.
	 */
	public DoubleNoise(double mean, double deviation, Filter<Double> innerFilter) {
		super(innerFilter);
		noise = new NormalDistribution(mean, deviation);
	}

	/**
	 * Apply noise defined by this instance to the given data.
	 * 
	 * @param data The data to apply the filter at.
	 * @return The given data with applied noise.
	 */
	@Override
	protected Double applyNoise(final Double data) {
		return (data + noise.getNext());
	}
}
