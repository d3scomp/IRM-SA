package period.model;

import java.util.Optional;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.MonitorContext;

/**
 * For now the content of methods is hacked!!!
 */
@Component
@IRMComponent("FireFighter")
public class FireFighter {

	/** Position fitness should reset its state after this number of determinePosition. */
	static private final int POSION_STATE_CACHE = 20;

	/** Maximal allowed inaccuracy. */
	static private final double MAX_INACCURACY = 20.0;

	/** Invariants with fitness less than this value are considered unsatisfied. */
	static private final double SATISFACTION_BOUND = 0.8;

	/** The firefighters should be powered at least this time in ms. */
	static private final long TARGET_DURABILITY = 400000;

	/**
	 * Key for storing and retrieving value of last battery level from internal data.
	 * Last battery level is needed for estimation of battery drainage for
	 * determining the fitness of the battery level invariant.
	 */
	static private final String LAST_BATTERY_LEVEL = "lastBatteryLevel";

	/**
	 * Key for storing and retrieving time of last battery check from internal data.
	 * Time of last battery check is needed for estimation of battery drainage
	 * for determining the fitness of the battery level invariant.
	 */
	static private final String LAST_BATTERY_CHECK = "lastBatteryCheck";

	/**
	 * Key for storing and retrieving last period of determineBatteryLevel from internal data.
	 * Time of last battery check is needed for estimation of battery drainage
	 * for determining the fitness of the battery level invariant.
	 */
	static private final String LAST_BATTERY_PERIOD = "lastBatteryPeriod";

	/**
	 * Key for storing and retrieving value of inaccuracy accumulator from internal data.
	 * Inaccuracy accumulator is intended for accumulating position inaccuracy
	 * at the time of determining the position. It is needed for determining
	 * the  fitness of position inaccuracy invariant.
	 */
	static private final String INACCURACY_ACCUMULATOR = "inaccuracyAccumulator";

	/**
	 * Key for storing and retrieving value of OK counter from internal data.
	 * OK counter is intended for counting calls of determinePosition when the
	 * inaccuracy is within the given bounds.
	 */
	static private final String POSITION_OK_COUNTER = "positionOKCounter";

	/**
	 * Key for storing and retrieving value of BAD counter from internal data.
	 * BAD counter is intended for counting calls of determinePosition when the
	 * inaccuracy is not within the given bounds and the method should have been
	 * called sooner.
	 */
	static private final String POSITION_BAD_COUNTER = "positionBadCounter";

	/**
	 * Key for storing and retrieving last period of determinePosition from internal data.
	 * Time of last battery check is needed for estimation of battery drainage
	 * for determining the fitness of the battery level invariant.
	 */
	static private final String LAST_POSITION_PERIOD = "lastPositionPeriod";

	/**
	 * Returns component instance of the current process.
	 * @return component instance of the current process
	 */
	static private ComponentInstance getComponentInstance() {
		final ComponentInstance ci = MonitorContext.getMonitoredComponent();
		if (ci != null) {
			return ci;
		} else {
			return ProcessContext.getCurrentProcess().getComponentInstance();
		}
	}

	/**
	 * Not type-safe method for retrieving objects from component's internal data.
	 * @param key key to search value for
	 * @return typed object from component's internal data
	 * @throws RuntimeException when no, null or wrongly typed data are provided
	 */
	static private <T> T retrieveFromInternalData(final String key) {
		final ComponentInstance instance = getComponentInstance();
		final Object value = instance.getInternalData().get(key);
		@SuppressWarnings("unchecked")
		final T result = (T) value;
		return result;
	}

	static protected void storeInInternalData(final String key, final Object value) {
		System.out.println("SET INTERNAL DATA: " + key + " " + value);
		final ComponentInstance instance = getComponentInstance();
		instance.getInternalData().put(key, value);
	}

	/**
	 * Returns current period of process with given name on given component instance.
	 * @param name process name
	 * @return
	 */
	static protected long getCurrentProcessPeriod(final String name) {
		final Optional<ComponentProcess> process = getComponentInstance().getComponentProcesses().stream()
				.filter(cp -> cp.getName().equals(name))
				.findFirst();
		if (process.isPresent()) {
			for (Trigger trigger : process.get().getTriggers()) {
				if (trigger instanceof TimeTrigger) {
					return ((TimeTrigger) trigger).getPeriod();
				}
			}
		}
		return 0L;
	}

	static protected double getLastBatteryLevel() {
		Double val = retrieveFromInternalData(LAST_BATTERY_LEVEL);
		if (val == null) {
			return 100.0;
		} else {
			return val;
		}
	}

	static protected void setLastBatteryLevel(final double value) {
		storeInInternalData(LAST_BATTERY_LEVEL, value);
	}

	static protected long getLastBatteryCheck() {
		Long val = retrieveFromInternalData(LAST_BATTERY_CHECK);
		if (val == null) {
			return 0L;
		} else {
			return val;
		}
	}

	static protected void setLastBatteryCheck(final long value) {
		storeInInternalData(LAST_BATTERY_CHECK, value);
	}

	static protected void setLastBatteryPeriod(final long value) {
		storeInInternalData(LAST_BATTERY_PERIOD, value);
	}

	static protected long getLastBatteryPeriod() {
		Long val = retrieveFromInternalData(LAST_BATTERY_PERIOD);
		if (val == null) {
			return -1L;
		} else {
			return val;
		}
	}

	static protected double getInaccuracyAccumulator() {
		Double val = retrieveFromInternalData(INACCURACY_ACCUMULATOR);
		if (val == null) {
			return 0.0;
		} else {
			return val;
		}
	}

	static protected void setInaccuracyAccumulator(final double value) {
		storeInInternalData(INACCURACY_ACCUMULATOR, value);
	}

	static protected int getPositionOkCounter() {
		Integer val = retrieveFromInternalData(POSITION_OK_COUNTER);
		if (val == null) {
			return 0;
		} else {
			return val;
		}
	}

	static protected void setPositionOkCounter(final int value) {
		storeInInternalData(POSITION_OK_COUNTER, value);
	}

	static protected int getPositionBadCounter() {
		Integer val = retrieveFromInternalData(POSITION_BAD_COUNTER);
		if (val == null) {
			return 0;
		} else {
			return val;
		}
	}

	static protected void setPositionBadCounter(final int value) {
		storeInInternalData(POSITION_BAD_COUNTER, value);
	}

	static protected void setLastPositionPeriod(final long value) {
		storeInInternalData(LAST_POSITION_PERIOD, value);
	}

	static protected long getLastPositionPeriod() {
		Long val = retrieveFromInternalData(LAST_POSITION_PERIOD);
		if (val == null) {
			return -1L;
		} else {
			return val;
		}
	}

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
		final double diff = getLastBatteryLevel() - batteryLevel;
		final long period = time - getLastBatteryCheck();
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
		final long currentPeriod = getCurrentProcessPeriod("determineBatteryLevel");
		final long previousPeriod = getLastBatteryPeriod();
		if (currentPeriod != previousPeriod) {
			setLastBatteryLevel(batteryLevel);
			setLastBatteryCheck(time);
			setLastBatteryPeriod(currentPeriod);
		}
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
			setPositionOkCounter(getPositionOkCounter() + 1);
		} else {
			setPositionBadCounter(getPositionBadCounter() + 1);
		}
		setInaccuracyAccumulator(getInaccuracyAccumulator() + inacc);
		position.value = Environment.getPosition();
		System.out.println("Determining Pos at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		System.out.println("Pos: " + position.value);
	}

	@InvariantMonitor("P02")
	public static double determinePositionFitness() {
		System.out.println("Determining PosFit at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		final int posOk = getPositionOkCounter();
		final int posBad = getPositionBadCounter();
		double result;
		if (posBad > 0) {
			final double ratio = (1.0 * posOk) / (posOk + posBad);
			result = SATISFACTION_BOUND * ratio;
		} else if (posOk + posBad == 0) {
			return 1.0;
		} else {
			final double ratio = getInaccuracyAccumulator() / ((posOk + posBad) * MAX_INACCURACY);
			result = (1.0 - SATISFACTION_BOUND) * ratio + SATISFACTION_BOUND;
		}
		final long currentPeriod = getCurrentProcessPeriod("determinePosition");
		final long previousPeriod = getLastPositionPeriod();
		if (currentPeriod != previousPeriod || posOk + posBad > POSION_STATE_CACHE) {
			//reset state if too old or adaptation happened
			setPositionBadCounter(0);
			setPositionOkCounter(0);
			setInaccuracyAccumulator(0.0);
			setLastPositionPeriod(currentPeriod);
		}
		return result;
	}
}
