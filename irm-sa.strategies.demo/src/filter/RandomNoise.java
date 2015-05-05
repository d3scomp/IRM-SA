package filter;

import java.util.Random;

/**
 * A generator of random numbers with normal distribution.
 * Mean and standard deviation can be specified. The seed
 * of the random is fixed and depends on the order in which
 * the {@link RandomNoise} objects are instantiated.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class RandomNoise {

	/**
	 * The initial seed for the {@link RandomNoise} object.
	 * The seed is changed after new instance of the {@link RandomNoise}
	 * is created.
	 */
	private static long seed = 75698;
	
	/**
	 * The mean of the normal distribution.
	 */
	private double mean;
	/**
	 * The deviation of the normal distribution.
	 */
	private double deviation;
	/**
	 * {@link Random} number generator.
	 */
	private Random rand;
	
	/**
	 * Create new instance of {@link RandomNoise} with the mean 0.0
	 * and the standard deviation 1.0.
	 */
	public RandomNoise() {
		init(0.0, 1.0);
	}
	
	/**
	 * Create new instance of {@link RandomNoise} with the given mean
	 * and the given standard deviation.
	 * 
	 * @param mean The mean of the normal distribution.
	 * @param deviation The standard deviation of the normal distribution.
	 */
	public RandomNoise(double mean, double deviation) {
		init(mean, deviation);
	}
	
	/**
	 * Assign the mean, deviation and {@link Random} number generator.
	 * Change the seed for the next instance.
	 * 
	 * @param mean The mean of the normal distribution.
	 * @param deviation The standard deviation of the normal distribution.
	 */
	private void init(double mean, double deviation) {
		setDistribution(mean, deviation);
		
		rand = new Random(seed);
		seed += 498349;
	}

	/**
	 * Assign the mean and standard deviation.
	 * 
	 * @param mean The mean of the normal distribution.
	 * @param deviation The standard deviation of the normal distribution.
	 */
	public void setDistribution(double mean, double deviation) {
		this.mean = mean;
		this.deviation = deviation;
	}
	
	/**
	 * Get next random number with normal distribution with predefined
	 * mean and standard deviation.
	 * @return Next random number with normal distribution with predefined
	 * mean and standard deviation.
	 */
	protected double getNoise() {
		return mean + rand.nextGaussian()*deviation;
	}

}
