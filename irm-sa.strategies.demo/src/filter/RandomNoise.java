package filter;

import java.util.Random;

public class RandomNoise {

	private static long seed = 75698;
	
	private double mean;
	private double deviation;
	private Random rand;
	
	public RandomNoise() {
		init(0.0, 1.0);
	}
	
	public RandomNoise(double mean, double deviation) {
		init(mean, deviation);
	}
	
	private void init(double mean, double deviation) {
		setDistribution(mean, deviation);
		
		rand = new Random(seed);
		seed += 498349;
	}
	
	public void setDistribution(double mean, double deviation) {
		this.mean = mean;
		this.deviation = deviation;
	}
	
	protected double getNoise() {
		return mean + rand.nextGaussian()*deviation;
	}

}
