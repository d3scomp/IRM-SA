package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

public class ClockProvider {
	private static ClockProvider instance;
	
	public static void init(CurrentTimeProvider timeProvider) {
		instance = new ClockProvider(timeProvider);
	}
	
	public static CurrentTimeProvider getClock() {
		return instance.timeProvider;
	}
	
	private final CurrentTimeProvider timeProvider;
	
	private ClockProvider(CurrentTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}
}
