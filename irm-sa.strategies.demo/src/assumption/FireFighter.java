package assumption;

import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

/**
 * For now the content of methods is hacked!!!
 */
@Component
@IRMComponent("FireFighter")
public class FireFighter {

	/** Mandatory field. */
	public String id;

	/** Battery level. */
	public Double batteryLevel;

	/** For now the position is 1D only. */
	public Double position;

	/**
	 * Only constructor.
	 */
	public FireFighter() {
		batteryLevel = Environment.INITIAL_BATTERY_LEVEL;
		position = Environment.INITIAL_POSITION;
	}

	@Process
	@Invariant("P01")
	@PeriodicScheduling(period=1000)
	public static void determineBatteryLevel(
		@In("id") String id,
		@Out("batteryLevel") ParamHolder<Double> batteryLevel
	) {
		batteryLevel.value = Environment.getBatteryLevel(id);
		System.out.println("Determining BL at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Battery level: " + batteryLevel.value);
	}

	@Process
	@Invariant("P02")
	@PeriodicScheduling(period=1250)
	public static void determinePosition(
		@In("id") String id,
		@Out("position") ParamHolder<Double> position
	) {
		position.value = Environment.getPosition(id);
		System.out.println("Determining Pos at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Pos: " + position.value);
	}

	@InvariantMonitor("A02")
	public static boolean positionInaccuracyMonitor(
			@In("id") String id,
			@In("position") Double position,
			@AssumptionParameter(name = "param", defaultValue = 20, minValue = 0, maxValue = 100)
			int param
	) {
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		return Environment.getInaccuracy(id) < param;
	}
}
