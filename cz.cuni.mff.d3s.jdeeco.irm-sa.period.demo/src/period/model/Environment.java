package period.model;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

/**
 * This whole component is a hack!!!
 */
@Component
@SystemComponent
public class Environment {

	static private final double GPS_ENERGY_COST = 0.4;

	static private final Random RANDOM = new Random();

	static private final int GPS_BREAK_TIME = 50000;

	static private final double FF_MOVEMENT = 4.0;

	static private final double BROKEN_GSP_INACURRACY = 9.0;

	/////////////////////
	//ENVIRONMENT STATE//
	/////////////////////

	static private double position = 0.0;

	static private double inaccuracy = 0.0;

	static private double batteryLevel = 100.0;

	static public double getPosition() {
		batteryLevel -= GPS_ENERGY_COST;
		if (batteryLevel < 0d) {
			batteryLevel = 0;
		}
		inaccuracy = 0.0;
		return position;
	}

	static public double getInaccuracy() {
		return inaccuracy;
	}

	static public double getBatteryLevel() {
		return batteryLevel;
	}

	public String id;

	@Process
	@PeriodicScheduling(period=500)
	static public void simulation(
			@In("id") String id) {
		final double d = RANDOM.nextDouble() * FF_MOVEMENT;
		position += RANDOM.nextBoolean() ? d : -d;
		if (ProcessContext.getTimeProvider().getCurrentMilliseconds() < GPS_BREAK_TIME) {
			inaccuracy += FF_MOVEMENT;
		} else {
			inaccuracy += BROKEN_GSP_INACURRACY;
		}
		System.out.println("Simulating at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Position: " + position);
		System.out.println("Inaccuracy: " + inaccuracy);
	}
}
