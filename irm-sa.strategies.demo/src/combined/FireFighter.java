package combined;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter;
import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter.Direction;
import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter.Scope;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
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
	 * Key for storing and retrieving value of inaccuracy history from internal data.
	 * Inaccuracy history is intended for keeping position inaccuracy
	 * at the time of determining the position. It is needed for determining
	 * the  fitness of position inaccuracy invariant.
	 */
	static private final String INACCURACY_HISTORY = "inaccuracyHistory";

	/**
	 * Key for storing and retrieving last period of determinePosition from internal data.
	 * Time of last battery check is needed for estimation of battery drainage
	 * for determining the fitness of the battery level invariant.
	 */
	static private final String LAST_POSITION_PERIOD = "lastPositionPeriod";

	/**
	 * Key for storing and retrieving last inaccuracy bound from internal data.
	 * Time of last inaccuracy bound is needed for detecting whether the assumption
	 * parameter adaptation has happened so state of position fitness should reset.
	 */
	static private final String LAST_INACCURACY_BOUND = "lastInaccuracyBound";

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
		return val == null ? Environment.INITIAL_BATTERY_LEVEL : val;
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

	static protected Deque<Double> getInaccuracyHistory() {
		Deque<Double> val = retrieveFromInternalData(INACCURACY_HISTORY);
		if (val == null) {
			val = new ArrayDeque<>(POSION_STATE_HISTORY);
			storeInInternalData(INACCURACY_HISTORY, val);
		}
		return val;
	}

	static protected void setLastPositionPeriod(final long value) {
		storeInInternalData(LAST_POSITION_PERIOD, value);
	}

	static protected long getLastPositionPeriod() {
		final Long val = retrieveFromInternalData(LAST_POSITION_PERIOD);
		return val == null ? -1L : val;
	}

	static protected void setLastInaccuracyBound(final int value) {
		storeInInternalData(LAST_INACCURACY_BOUND, value);
	}

	static protected int getLastInaccuracyBound() {
		final Integer val = retrieveFromInternalData(LAST_INACCURACY_BOUND);
		return val == null ? 0 : val;
	}

	/** Mandatory id field. */
	public String id;

	/** Battery level. */
	public MetadataWrapper<Integer> batteryLevel;

	/** Position in sector positioning system. */
	public MetadataWrapper<Position> position;

	/** Environment temperature. */
	public MetadataWrapper<Double> temperature;

	/**
	 * Only constructor.
	 * @param id component id
	 */
	public FireFighter(final String id) {
		this.id = id;
		batteryLevel = new MetadataWrapper<Integer>(Environment.INITIAL_BATTERY_LEVEL);
		position = new MetadataWrapper<Position>(Environment.INITIAL_POSITION);
		temperature = new MetadataWrapper<Double>(Environment.INITIAL_TEMPERATURE);
	}

	/**
	 * Returns current simulation time.
	 * @return current simulation time
	 */
	public static long currentTime() {
		return ProcessContext.getTimeProvider().getCurrentMilliseconds();
	}

	private static void resetBatteryStateIfNeeded(int batteryLevel) {
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
		resetBatteryStateIfNeeded(batteryLevel.value.getValue());
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
		System.err.println("BATTERY FITNESS = " + fitness);
		return fitness;
	}

	@Process
	@Invariant("P02")
	@PeriodicScheduling(period=1250)
	public static void determinePosition(
		@In("id") String id,
		@InOut("position") ParamHolder<MetadataWrapper<Position>> position
	) {
		final double inacc = Environment.getInaccuracy(id);
		final Deque<Double> history = getInaccuracyHistory();
		if (history.size() >= POSION_STATE_HISTORY) {
			history.removeFirst();
		}
		history.add(inacc);
		if (position.value.isOperational()) {
			position.value.setValue(Environment.getPosition(id), currentTime());
		}
	}

	@InvariantMonitor("P02")
	public static boolean determinePositionSatisfaction(
			@In("position") MetadataWrapper<Position> position) {
		return currentTime() - position.getTimestamp() < TOO_OLD;
	}

	@InvariantMonitor("P02")
	public static double determinePositionFitness(
			@In("position") MetadataWrapper<Position> position) {
		final long time = currentTime();
		final boolean recent = time - position.getTimestamp() < TOO_OLD;
		return recent ? 1.0 : 0.0;
	}

	@InvariantMonitor("A02")
	public static boolean positionAccuracySatisfaction(
			@AssumptionParameter(name = "bound", defaultValue = 20,
			maxValue = 30, minValue = 15, scope = Scope.COMPONENT,
			initialDirection = Direction.UP)
			int bound) {
		int bad = 0;
		for (Double i : getInaccuracyHistory()) {
			if (i > bound) {
				++bad;
			}
		}
		return bad == 0;
	}

	@InvariantMonitor("A02")
	public static double positionAccuracyFitness(
			@AssumptionParameter(name = "bound", defaultValue = 20,
			maxValue = 30, minValue = 15, scope = Scope.COMPONENT,
			initialDirection = Direction.UP)
			int bound) {
		int posBad = 0;
		int posOk = 0;
		int inacc = 0;
		for (Double i : getInaccuracyHistory()) {
			inacc += i;
			if (i > bound) {
				++posBad;
			} else {
				++posOk;
			}
		}
		double result;
		if (posBad > 0) {
			final double ratio = (1.0 * posOk) / (posOk + posBad);
			result = SATISFACTION_BOUND * ratio;
		} else if (posOk + posBad == 0) {
			result = 1.0;
		} else {
			final double ratio = 1.0 * inacc / ((posOk + posBad) * bound);
			result = (1.0 - SATISFACTION_BOUND) * ratio + SATISFACTION_BOUND;
		}
		return result;
	}

	@Process
	@Invariant("P03")
	@PeriodicScheduling(period=1000, order = 2)
	public static void determineTemperature(
		@In("id") String id,
		@InOut("temperature") ParamHolder<MetadataWrapper<Double>> temperature
	) {
		if (temperature.value.isOperational()) {
			temperature.value.setValue(Environment.getTemperature(id, temperature.value), currentTime());
		} else {
			System.out.println("Temperature sensor not operational!");
		}
	}

	@InvariantMonitor("P03")
	public static boolean determineTemperatureSatisfaction(
			@In("temperature") MetadataWrapper<Double> temperature) {
		return currentTime() - temperature.getTimestamp() < TOO_OLD;
	}

	@InvariantMonitor("P03")
	public static double determineTemperatureFitness(
			@In("id") String id,
			@In("temperature") MetadataWrapper<Double> temperature) {
		long heldTemperatureTime = temperature.getTimestamp();
		long currentTime = currentTime();
		long delta = currentTime - heldTemperatureTime;
		double oldnessTreshold = 15000.0;

		double fitness = 1 - Math.min(1, (delta / oldnessTreshold));
		return fitness;
	}
}
