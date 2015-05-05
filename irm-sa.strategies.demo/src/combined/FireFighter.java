package combined;

import java.util.Deque;

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
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

import static combined.FireFighterHelper.*;

@Component
@IRMComponent("FireFighter")
public class FireFighter {
	
	///////////////////////////////////////////////////////////////////////////
	//     KNOWLEDGE                                                         //
	///////////////////////////////////////////////////////////////////////////
	
	/** Mandatory id field. */
	public String id;

	/** Battery level. */
	public MetadataWrapper<Double> batteryLevel;

	/** Position in sector positioning system. */
	public MetadataWrapper<Position> position;

	/** Environment temperature. */
	public MetadataWrapper<Double> temperature;

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Only constructor.
	 * @param id component id
	 */
	public FireFighter(final String id) {
		this.id = id;
		batteryLevel = new MetadataWrapper<Double>(Environment.INITIAL_BATTERY_LEVEL);
		position = new MetadataWrapper<Position>(Environment.INITIAL_FF_POSITION);
		temperature = new MetadataWrapper<Double>(Environment.INITIAL_TEMPERATURE);
	}
	
	@Process
	@Invariant("P01")
	@PeriodicScheduling(period=1000)
	public static void determineBatteryLevel(
			@In("id") String id,
			@InOut("batteryLevel") ParamHolder<MetadataWrapper<Double>> batteryLevel
	) {
		
		if (batteryLevel.value.isOperational()) {
			batteryLevel.value.setValue(Environment.getBatteryLevel(id), currentTime());
		}
		resetBatteryStateIfNeeded(batteryLevel.value.getValue());
	}

	@InvariantMonitor("P01")
	public static boolean determineBatteryLevelSatisfaction(
			@In("batteryLevel") MetadataWrapper<Double> batteryLevel) {
		return currentTime() - batteryLevel.getTimestamp() < TOO_OLD;
	}

	@InvariantMonitor("P01")
	public static double determineBatteryLevelFitness(
			@In("batteryLevel") MetadataWrapper<Double> batteryLevel) {
		final boolean satisfied = determineBatteryLevelSatisfaction(batteryLevel);
		return satisfied ? 1.0 : 0.0;
	}

	@InvariantMonitor("A01")
	public static boolean batteryDrainageSatisfaction(
			@In("batteryLevel") MetadataWrapper<Double> batteryLevel) {
		final double bl = batteryLevel.getValue();
		final double fitness = batteryDrainageSatisfactionInternal(bl);
		return fitness > SATISFACTION_BOUND;
	}

	@InvariantMonitor("A01")
	public static double batteryDrainageFitness(
			@In("batteryLevel") MetadataWrapper<Double> batteryLevel) {
		final double bl = batteryLevel.getValue();
		final double fitness = batteryDrainageSatisfactionInternal(bl);
		System.err.println("BATTERY FITNESS = " + fitness);
		return fitness;
	}

	@Process
	@Invariant("P02")
	@PeriodicScheduling(period = Environment.INITIAL_POSITION_PERIOD)
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
			@AssumptionParameter(name = "bound", defaultValue = Environment.FF_POS_INAC_BOUND,
			maxValue = Environment.FF_POS_INAC_BOUND_MAX, minValue = Environment.FF_POS_INAC_BOUND_MIN, scope = Scope.COMPONENT,
			initialDirection = Direction.UP)
			double bound) {
		resetPositionStateIfNeeded(bound);
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
			@AssumptionParameter(name = "bound", defaultValue = Environment.FF_POS_INAC_BOUND,
			maxValue = Environment.FF_POS_INAC_BOUND_MAX, minValue = Environment.FF_POS_INAC_BOUND_MIN, scope = Scope.COMPONENT,
			initialDirection = Direction.UP)
			double bound) {
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
	@PeriodicScheduling(period=200, order = 2)
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
