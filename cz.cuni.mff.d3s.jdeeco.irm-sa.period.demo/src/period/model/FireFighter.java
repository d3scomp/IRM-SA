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

	static private final double SATISFACTION_BOUND = 0.8;

	static private final long TARGET_DURABILITY = 400000;

	/** Hack. */
	static private double lastBatteryLevel = Environment.getBatteryLevel();

	/** Hack */
	static private long lastBatteryCheck = 0L;

	static private double inaccuracyAcc = 0.0;

	static private int posOk = 0;

	static private int posBad = 0;

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
		final long time = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		if (time > TARGET_DURABILITY /*|| batteryLevel <= 0.00001*/) {
			return 1.0;
		}
		if (batteryLevel <= 0.00001) {
			return 0.0;
		}
		final long timeLeft = TARGET_DURABILITY - time;
		final double diff = (lastBatteryLevel - batteryLevel);
		final long period = time - lastBatteryCheck;
		System.out.println("Battery level diff: " + diff);
		System.out.println("Battery level period: " + period);
		final double energyLeft = batteryLevel / diff * period;
		double result;
		if (energyLeft < timeLeft) {
			final double ratio = energyLeft / timeLeft;
			result = SATISFACTION_BOUND * ratio;
		} else {
			result = 1.0;
		}
		lastBatteryLevel = batteryLevel;
		lastBatteryCheck = time;
		System.out.println("Battery level Fitness: " + result);
		return result;
	}


	@Process
	@Invariant("P02")
	@PeriodicScheduling(period=1250)
	public static void determinePosition(
		@Out("position") ParamHolder<Double> position
	) {
		final double inacc = Environment.getInaccuracy();
		if (inacc <= MAX_INACCURACY) {
			++posOk;
		} else {
			++posBad;
		}
		inaccuracyAcc += inacc;
		position.value = Environment.getPosition();
		System.out.println("Determining Pos at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Pos: " + position.value);
	}

	@InvariantMonitor("P02")
	public static double determinePositionFitness() {
		System.out.println("Determining PosFit at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		double result;
		if (posBad > 0) {
			final double ratio = (1.0 * posOk) / (posOk + posBad);
			result = SATISFACTION_BOUND * ratio;
		} else if (posOk + posBad == 0){
			return 1.0;
		} else {
			final double ratio = inaccuracyAcc / ((posOk + posBad) * MAX_INACCURACY);
			result = (1.0 - SATISFACTION_BOUND) * ratio + SATISFACTION_BOUND;
		}
		posBad = 0;
		posOk = 0;
		inaccuracyAcc = 0;
		return result;
	}
}
