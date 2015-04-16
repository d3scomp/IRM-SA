package combined;

import java.util.Optional;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.MonitorContext;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

@Component
@IRMComponent("FireFighter")
public class FireFighter {

	/** Position fitness should reset its state after this number of determinePosition. */
	static private final int POSION_STATE_HISTORY = 20;

	/** Maximal allowed inaccuracy. */
	static private final double MAX_INACCURACY = 20.0;

	/** Invariants with fitness less than this value are considered unsatisfied. */
	static private final double SATISFACTION_BOUND = 0.8;

	/** The firefighters should be powered at least this time in ms. */
	static private final long TARGET_DURABILITY = 400000;

	/** Data older than this (in ms) are considered old. */
	static private  final long TOO_OLD = 2000;

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

	static protected int getLastBatteryLevel() {
		final Integer val = retrieveFromInternalData(LAST_BATTERY_LEVEL);
		return val == null ? 1000 : val;
	}

	static protected void setLastBatteryLevel(final int value) {
		storeInInternalData(LAST_BATTERY_LEVEL, value);
	}

	static protected long getLastBatteryCheck() {
		final Long val = retrieveFromInternalData(LAST_BATTERY_CHECK);
		return val == null ? 0L : val;
	}

	static protected void setLastBatteryCheck(final long value) {
		storeInInternalData(LAST_BATTERY_CHECK, value);
	}

	static protected void setLastBatteryPeriod(final long value) {
		storeInInternalData(LAST_BATTERY_PERIOD, value);
	}

	static protected long getLastBatteryPeriod() {
		final Long val = retrieveFromInternalData(LAST_BATTERY_PERIOD);
		return val == null ? -1L : val;
	}

	static protected int getInaccuracyAccumulator() {
		final Integer val = retrieveFromInternalData(INACCURACY_ACCUMULATOR);
		return val == null ? 0 : val;
	}

	static protected void setInaccuracyAccumulator(final int value) {
		storeInInternalData(INACCURACY_ACCUMULATOR, value);
	}

	static protected int getPositionOkCounter() {
		final Integer val = retrieveFromInternalData(POSITION_OK_COUNTER);
		return val == null ? 0 : val;
	}

	static protected void setPositionOkCounter(final int value) {
		storeInInternalData(POSITION_OK_COUNTER, value);
	}

	static protected int getPositionBadCounter() {
		final Integer val = retrieveFromInternalData(POSITION_BAD_COUNTER);
		return val == null ? 0 : val;
	}

	static protected void setPositionBadCounter(final int value) {
		storeInInternalData(POSITION_BAD_COUNTER, value);
	}

	static protected void setLastPositionPeriod(final long value) {
		storeInInternalData(LAST_POSITION_PERIOD, value);
	}

	static protected long getLastPositionPeriod() {
		final Long val = retrieveFromInternalData(LAST_POSITION_PERIOD);
		return val == null ? -1L : val;
	}

	/** Mandatory id field. */
	public String id;

	/** Battery level. */
	public MetadataWrapper<Integer> batteryLevel;

	/** For now the position is 1D only. */
	public MetadataWrapper<Integer> position;

	/** Environment temperature. */
	public MetadataWrapper<Integer> temperature;

	/**
	 * Only constructor.
	 * @param id component id
	 */
	public FireFighter(final String id) {
		this.id = id;
		batteryLevel = new MetadataWrapper<Integer>(Environment.INITIAL_BATTERY_LEVEL);
		position = new MetadataWrapper<Integer>(Environment.INITIAL_POSITION);
		temperature = new MetadataWrapper<Integer>(Environment.INITIAL_TEMPERATURE);
	}

	/**
	 * Returns current simulation time.
	 * @return current simulation time
	 */
	public static long currentTime() {
		return ProcessContext.getTimeProvider().getCurrentMilliseconds();
	}

	@Process
	@Invariant("P01")
	@PeriodicScheduling(period=1000)
	public static void determineBatteryLevel(
		@In("id") String id,
		@InOut("batteryLevel") ParamHolder<MetadataWrapper<Integer>> batteryLevel
	) {
		if (batteryLevel.value.isOperational()) {
			batteryLevel.value.setValue(Environment.getBatteryLevel(id), currentTime());
		}
	}

	@InvariantMonitor("P01")
	public static boolean determineBatteryLevelSatisfaction(
			@In("batteryLevel") MetadataWrapper<Integer> batteryLevel) {
		return currentTime() - batteryLevel.getTimestamp() < TOO_OLD;
	}

	@InvariantMonitor("P01")
	public static double determineBatteryLevelFitness(
			@In("batteryLevel") MetadataWrapper<Integer> batteryLevel) {
		final boolean satisfied = determineBatteryLevelSatisfaction(batteryLevel);
		return satisfied ? 1.0 : 0.0;
	}

	private static double batteryDrainageSatisfactionInternal(int batteryLevel) {
		final long time = currentTime();
		if (time > TARGET_DURABILITY) {
			return 1.0; //objective achieved
		}
		if (batteryLevel <= 0) {
			return 0.0; //objective failed
		}
		final long timeLeft = TARGET_DURABILITY - time;
		final int diff = getLastBatteryLevel() - batteryLevel;
		final long period = time - getLastBatteryCheck();
		final double energyLeft = 1.0 * batteryLevel / diff * period;
		if (energyLeft < timeLeft) {
			final double ratio = energyLeft / timeLeft;
			return SATISFACTION_BOUND * ratio;
		} else {
			return 1.0;
		}
	}

	@InvariantMonitor("A01")
	public static boolean batteryDrainageSatisfaction(
			@In("batteryLevel") MetadataWrapper<Integer> batteryLevel) {
		final int bl = batteryLevel.getValue();
		final double fitness = batteryDrainageSatisfactionInternal(bl);
		return fitness > SATISFACTION_BOUND;
	}

	@InvariantMonitor("A01")
	public static double batteryDrainageFitness(
			@In("batteryLevel") MetadataWrapper<Integer> batteryLevel) {
		final int bl = batteryLevel.getValue();
		final double fitness = batteryDrainageSatisfactionInternal(bl);
		//reset state if period adaptation took place
		final long currentPeriod = getCurrentProcessPeriod("determineBatteryLevel");
		final long previousPeriod = getLastBatteryPeriod();
		final long time = currentTime();
		if (currentPeriod != previousPeriod) {
			setLastBatteryLevel(bl);
			setLastBatteryCheck(time);
			setLastBatteryPeriod(currentPeriod);
		}
		return fitness;
	}

	@Process
	@Invariant("P02")
	@PeriodicScheduling(period=1250)
	public static void determinePosition(
		@In("id") String id,
		@InOut("position") ParamHolder<MetadataWrapper<Integer>> position
	) {
		if (position.value.isOperational()) {
			final int inacc = Environment.getInaccuracy(id);
			if (inacc <= MAX_INACCURACY) {
				setPositionOkCounter(getPositionOkCounter() + 1);
			} else {
				setPositionBadCounter(getPositionBadCounter() + 1);
			}
			setInaccuracyAccumulator(getInaccuracyAccumulator() + inacc);
			position.value.setValue(Environment.getPosition(id), currentTime());
		} else {
			setPositionBadCounter(getPositionBadCounter() + 1);
		}
	}

	@InvariantMonitor("P02")
	public static boolean determinePositionSatisfaction(
			@In("position") MetadataWrapper<Integer> position) {
		return currentTime() - position.getTimestamp() < TOO_OLD;
	}

	@InvariantMonitor("P02")
	public static double determinePositionFitness(
			@In("position") MetadataWrapper<Integer> position) {
		final long time = currentTime();
		final boolean recent = time - position.getTimestamp() < TOO_OLD;
		return recent ? 1.0 : 0.0;
	}

	@InvariantMonitor("A02")
	public static boolean positionAccuracySatisfaction() {
		final int posBad = getPositionBadCounter();
		return posBad == 0;
	}

	@InvariantMonitor("A02")
	public static double positionAccuracyFitness() {
		final int posOk = getPositionOkCounter();
		final int posBad = getPositionBadCounter();
		double result;
		if (posBad > 0) {
			final double ratio = (1.0 * posOk) / (posOk + posBad);
			result = SATISFACTION_BOUND * ratio;
		} else if (posOk + posBad == 0) {
			result = 1.0;
		} else {
			final double ratio = getInaccuracyAccumulator() / ((posOk + posBad) * MAX_INACCURACY);
			result = (1.0 - SATISFACTION_BOUND) * ratio + SATISFACTION_BOUND;
		}
		final long currentPeriod = getCurrentProcessPeriod("determinePosition");
		final long previousPeriod = getLastPositionPeriod();
		//TODO better history
		if (currentPeriod != previousPeriod || posOk + posBad > POSION_STATE_HISTORY) {
			//reset state if too old or period adaptation happened
			setPositionBadCounter(0);
			setPositionOkCounter(0);
			setInaccuracyAccumulator(0);
			setLastPositionPeriod(currentPeriod);
		}
		return result;
	}

	@Process
	@Invariant("P03")
	@PeriodicScheduling(period=1000)
	public static void determineTemperature(
		@In("id") String id,
		@InOut("temperature") ParamHolder<MetadataWrapper<Integer>> temperature
	) {
		if (temperature.value.isOperational()) {
			temperature.value.setValue(Environment.getTemperature(id), currentTime());
		}
	}
}
