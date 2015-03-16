package period.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

/**
 * This whole component is a kind of hack.
 * It serves as sensors to FightFighter components.
 */
@Component
@SystemComponent
public class Environment {

	/** Firefighters' initial position. */
	static public final Double INITIAL_POSITION = 0.0;

	/** Firefighters' initial inaccuracy. */
	static public final Double INITIAL_INACCURACY = 0.0;

	/** Firefighters' initial battery level. */
	static public final Double INITIAL_BATTERY_LEVEL = 100.0;

	/** Checking position drains this much energy from battery. */
	static private final double GPS_ENERGY_COST = 0.4;

	/** GPS breaks at this time. */
	static private final int GPS_BREAK_TIME = 50000;

	/**
	 * Maximal movement of a firefighter in a tick.
	 * Also inaccuracy caused by regular firefighter movement.
	 */
	static private final double FF_MOVEMENT = 4.0;

	/** Inaccuracy in case of GPS malfunction. */
	static private final double BROKEN_GSP_INACURRACY = 9.0;

	/** RNG. */
	static private final Random RANDOM = new Random();

	/////////////////////
	//ENVIRONMENT STATE//
	/////////////////////

	/** Firefighters state. */
	static private Map<String, FireFighterState> firefighters = new HashMap<>();

	/**
	 * Returns FireFighterState for given firefighter.
	 * Creates new state if needed.
	 * @param ffId firefighter id
	 * @return FireFighterState for given firefighter
	 */
	static protected FireFighterState getFirefighter(final String ffId) {
		FireFighterState ff = firefighters.get(ffId);
		if (ff == null) {
			ff = new FireFighterState();
			firefighters.put(ffId, ff);
		}
		return ff;
	}

	/**
	 * Returns position of given firefighter or NaN with insufficient energy.
	 * Drains energy from the battery! If not enough energy left, NaN returned
	 * @param ffId firefighter id
	 * @return position of given firefighter or NaN with insufficient energy
	 */
	static public double getPosition(final String ffId) {
		final FireFighterState ff = getFirefighter(ffId);
		ff.batteryLevel -= GPS_ENERGY_COST;
		if (ff.batteryLevel <= 0.0) {
			ff.batteryLevel = 0.0;
			return Double.NaN;
		} else {
			ff.inaccuracy = 0.0;
			return ff.position;
		}
	}

	/**
	 * Returns inaccuracy of given firefighter.
	 * @param ffId firefighter id
	 * @return inaccuracy of given firefighter
	 */
	static public double getInaccuracy(final String ffId) {
		return getFirefighter(ffId).inaccuracy;
	}

	/**
	 * Returns battery level of given firefighter.
	 * @param ffId firefighter id
	 * @return battery level of given firefighter
	 */
	static public double getBatteryLevel(final String ffId) {
		return getFirefighter(ffId).batteryLevel;
	}

	/** Environment component id. Not used. */
	public String id;

	@Process
	@PeriodicScheduling(period=500)
	static public void simulation(
			@In("id") String id) {
		System.out.println("Simulating at " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		for (final String ffId : firefighters.keySet()) {
			System.out.println("Firefighter: " + ffId);
			final FireFighterState ff = getFirefighter(ffId);
			final double d = RANDOM.nextDouble() * FF_MOVEMENT;
			ff.position += RANDOM.nextBoolean() ? d : -d;
			if (ProcessContext.getTimeProvider().getCurrentMilliseconds() < GPS_BREAK_TIME) {
				ff.inaccuracy += FF_MOVEMENT;
			} else {
				ff.inaccuracy += BROKEN_GSP_INACURRACY;
			}
			System.out.println("Position: " + ff.position);
			System.out.println("Inaccuracy: " + ff.inaccuracy);
		}
	}

	/**
	 * Simple holder of data related to firefighter's state.
	 */
	protected static class FireFighterState {

		/** Firefighter's position. */
		protected double position = INITIAL_POSITION;

		/** Firefighter's position inaccuracy. */
		protected double inaccuracy = INITIAL_INACCURACY;

		/** Firefighter's battery level. */
		protected double batteryLevel = INITIAL_BATTERY_LEVEL;
	}
}
