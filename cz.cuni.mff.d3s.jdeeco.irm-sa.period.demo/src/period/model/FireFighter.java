package period.model;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

/**
 * For now the content of methods is hacked!!!
 */
@Component
@IRMComponent("FireFighter")
public class FireFighter {

	static private final double MAX_INACCURACY = 20.0;

	/** Hack. */
	static private double lastBatteryLevel = Environment.getBatteryLevel();

	/** Hack */
	static private long lastBatteryCheck = 0L;

	static private double inaccuracyAcc = 0.0;

	public Double batteryLevel;
	public Double position;

	public FireFighter() {
		batteryLevel = Environment.getBatteryLevel();
		position = Environment.getPosition();
	}

	@Process
	@Invariant("P01")
	@PeriodicScheduling(period=1000)
	public static void determineBatteryLevel(
		@Out("batteryLevel") ParamHolder<Double> batteryLevel
	) {
		batteryLevel.value = Environment.getBatteryLevel();
		System.out.println("Determining BL at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Battery level: " + batteryLevel.value);
	}

	@InvariantMonitor("P01")
	public static double determineBatteryLevelFitness(
			@In("batteryLevel") Double batteryLevel) {
		System.out.println("Determining BLF at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Battery level: " + batteryLevel);
		final double diff = (lastBatteryLevel - batteryLevel);
		final long time = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		final long period = time - lastBatteryCheck;
		System.out.println("Battery level diff: " + diff);
		System.out.println("Battery level period: " + period);
		final double fitness = 0.00025 / (diff / period);
		if (Double.isInfinite(fitness)) {
			return 1.0;
		}
		lastBatteryLevel = batteryLevel;
		lastBatteryCheck = time;
		System.out.println("Battery level Fitness: " + fitness);
		return fitness;
	}


	@Process
	@Invariant("P02")
	@PeriodicScheduling(period=2000)
	public static void determinePosition(
		@Out("position") ParamHolder<Double> position
	) {
		inaccuracyAcc += Environment.getInaccuracy();
		position.value = Environment.getPosition();
		System.out.println("Determining Pos at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Pos: " + position.value);
	}

	@InvariantMonitor("P02")
	public static double determinePositionFitness() {
		System.out.println("Determining PosFit at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		final double inaccuracy = Environment.getInaccuracy();
		if (inaccuracy > MAX_INACCURACY) {
			System.out.println("Position Fitness: 0");
			return 0;
		}
//		System.out.println("Position Fitness: " + 4 * (inaccuracy / MAX_INACCURACY));
//		return 4 * (inaccuracy / MAX_INACCURACY);
		System.out.println("Inaccuracy accumulator: " + inaccuracyAcc);
		final double res = 1 / inaccuracyAcc * 1000;
		inaccuracyAcc = 0;
		return res;
	}
}
