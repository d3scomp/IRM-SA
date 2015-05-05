package combined;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.MonitorContext;

/**
 * Holds constants and methods used by {@link FireFighter} that are not
 * considered to be a part of the {@link FireFighter} as a DEECo component.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class FireFighterHelper {

	/** Position fitness should reset its state after this number of determinePosition. */
	public static final int POSION_STATE_HISTORY = 20;

	/** Invariants with fitness less than this value are considered unsatisfied. */
	public static final double SATISFACTION_BOUND = 0.8;

	/** The firefighters should be powered at least this time in ms. */
	public static final long TARGET_DURABILITY = 400000;

	/** Data older than this (in ms) are considered old. */
	public static final long TOO_OLD = 2000;

	/**
	 * Key for storing and retrieving value of last battery level from internal data.
	 * Last battery level is needed for estimation of battery drainage for
	 * determining the fitness of the battery level invariant.
	 */
	public static final String LAST_BATTERY_LEVEL = "lastBatteryLevel";

	/**
	 * Key for storing and retrieving time of last battery check from internal data.
	 * Time of last battery check is needed for estimation of battery drainage
	 * for determining the fitness of the battery level invariant.
	 */
	public static final String LAST_BATTERY_CHECK = "lastBatteryCheck";

	/**
	 * Key for storing and retrieving value of inaccuracy history from internal data.
	 * Inaccuracy history is intended for keeping position inaccuracy
	 * at the time of determining the position. It is needed for determining
	 * the  fitness of position inaccuracy invariant.
	 */
	public static final String INACCURACY_HISTORY = "inaccuracyHistory";

	/**
	 * Key for storing and retrieving last period of determinePosition from internal data.
	 * Time of last battery check is needed for estimation of battery drainage
	 * for determining the fitness of the battery level invariant.
	 */
	public static final String LAST_POSITION_PERIOD = "lastPositionPeriod";

	/**
	 * Key for storing and retrieving last inaccuracy bound from internal data.
	 * Time of last inaccuracy bound is needed for detecting whether the assumption
	 * parameter adaptation has happened so state of position fitness should reset.
	 */
	public static final String LAST_INACCURACY_BOUND = "lastInaccuracyBound";

	/**
	 * Returns component instance of the current process.
	 * @return component instance of the current process
	 */
	private static ComponentInstance getComponentInstance() {
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
	private static <T> T retrieveFromInternalData(final String key) {
		final ComponentInstance instance = getComponentInstance();
		final Object value = instance.getInternalData().get(key);
		@SuppressWarnings("unchecked")
		final T result = (T) value;
		return result;
	}

	private static void storeInInternalData(final String key, final Object value) {
		System.out.println("SET INTERNAL DATA: " + key + " " + value);
		final ComponentInstance instance = getComponentInstance();
		instance.getInternalData().put(key, value);
	}

	/**
	 * Returns current period of process with given name on given component instance.
	 * @param name process name
	 * @return
	 */
	private static long getCurrentProcessPeriod(final String name) {
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

	private static double getLastBatteryLevel() {
		final Integer val = retrieveFromInternalData(LAST_BATTERY_LEVEL);
		return val == null ? Environment.INITIAL_BATTERY_LEVEL : val;
	}

	private static void setLastBatteryLevel(final double value) {
		storeInInternalData(LAST_BATTERY_LEVEL, value);
	}

	private static long getLastBatteryCheck() {
		final Long val = retrieveFromInternalData(LAST_BATTERY_CHECK);
		return val == null ? 0L : val;
	}

	private static void setLastBatteryCheck(final long value) {
		storeInInternalData(LAST_BATTERY_CHECK, value);
	}

	public static Deque<Double> getInaccuracyHistory() {
		Deque<Double> val = retrieveFromInternalData(INACCURACY_HISTORY);
		if (val == null) {
			val = new ArrayDeque<>(POSION_STATE_HISTORY);
			storeInInternalData(INACCURACY_HISTORY, val);
		}
		return val;
	}

	private static void setLastPositionPeriod(final long value) {
		storeInInternalData(LAST_POSITION_PERIOD, value);
	}

	private static long getLastPositionPeriod() {
		final Long val = retrieveFromInternalData(LAST_POSITION_PERIOD);
		return val == null ? -1L : val;
	}

	private static void setLastInaccuracyBound(final double value) {
		storeInInternalData(LAST_INACCURACY_BOUND, value);
	}

	private static double getLastInaccuracyBound() {
		final Double val = retrieveFromInternalData(LAST_INACCURACY_BOUND);
		return val == null ? 0.0 : val;
	}

	/**
	 * Returns current simulation time.
	 * @return current simulation time
	 */
	public static long currentTime() {
		return ProcessContext.getTimeProvider().getCurrentMilliseconds();
	}

	public static void resetBatteryStateIfNeeded(double batteryLevel) {
		//only determine position drains power
		final long currentPeriod = getCurrentProcessPeriod("determinePosition");
		final long previousPeriod = getLastPositionPeriod();
		final long time = currentTime();
		if (currentPeriod != previousPeriod) {
			setLastBatteryLevel(batteryLevel);
			setLastBatteryCheck(time);
			setLastPositionPeriod(currentPeriod);
		}
	}

	public static double batteryDrainageSatisfactionInternal(double batteryLevel) {
		final long time = currentTime();
		if (time > TARGET_DURABILITY) {
			return 1.0; //objective achieved
		}
		if (batteryLevel <= 0) {
			return 0.0; //objective failed
		}
		final long timeLeft = TARGET_DURABILITY - time;
		final double diff = getLastBatteryLevel() - batteryLevel;
		final long period = time - getLastBatteryCheck();
		final double energyLeft = 1.0 * batteryLevel / diff * period;
		if (energyLeft < timeLeft) {
			final double ratio = energyLeft / timeLeft;
			System.err.println("LAST BATTERY = " + getLastBatteryLevel());
			System.err.println("CURR BATTERY = " + batteryLevel);
			System.err.println("PERIOD = " + period);
			System.err.println("DIFF = " + diff);
			System.err.println("RATIO = " + ratio);
			return SATISFACTION_BOUND * ratio;
		} else {
			return 1.0;
		}
	}

	public static void resetPositionStateIfNeeded(final double inaccBound) {
		final long currentPeriod = getCurrentProcessPeriod("determinePosition");
		final long previousPeriod = getLastPositionPeriod();
		final double previousBound = getLastInaccuracyBound();
		if (currentPeriod != previousPeriod || inaccBound != previousBound) {
			//reset state if adaptation happened
			getInaccuracyHistory().clear();
			setLastPositionPeriod(currentPeriod);
			setLastInaccuracyBound(inaccBound);
		}
	}

}
