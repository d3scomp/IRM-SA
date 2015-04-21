package combined;

import static combined.HeatMap.SEGMENTS;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import combined.HeatMap.Segment;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

/**
 * This whole component is a kind of hack.
 * It serves as sensors to FightFighter components.
 */
@Component
@SystemComponent
public class Environment {

	/** Firefighters' initial position. */
	static public final Position INITIAL_POSITION = new Position(19, 0);

	/** Firefighters' initial inaccuracy. */
	static public final Integer INITIAL_INACCURACY = 0;

	/** Firefighters' initial battery level. */
	static public final Integer INITIAL_BATTERY_LEVEL = 1600;

	/** Firefighters' initial temperature. */
	static public final Integer INITIAL_TEMPERATURE = SEGMENTS[INITIAL_POSITION.segment].temps[INITIAL_POSITION.index];

	/** Checking position drains this much energy from battery. */
	static private final int GPS_ENERGY_COST = 4;

	/** FF1's GPS loses precision at this time. */
	static private final int GPS_BREAK_TIME = 50_000;

	/** FF1's thermometer dies at this time. */
	static private final int THERMO_DEAD_TIME = 150_000;

	/** Maximal distance between group members. */
	static final int MAX_GROUP_DISTANCE = 8;

	/** Returned when battery is too low to provide GPS readings. */
	static final Position BAD_POSITION = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);

	/** Firefighter leading the group. */
	static final String FF_LEADER_ID = "FF1";

	/** Firefighter following in the group. */
	static final String FF_FOLLOWER_ID = "FF2";

	/** Firefighter with this id goes left at start. */
	static final String LONELY_FF_ID = "FF3";

	/**
	 * Maximal movement of a firefighter in a tick.
	 * Also inaccuracy caused by regular firefighter movement.
	 */
	static private final int FF_MOVEMENT = 4;

	/** Inaccuracy in case of GPS malfunction. */
	static private final int BROKEN_GSP_INACURRACY = 9;

	/** RNG. */
	static private final Random RANDOM = new Random(24);

	/////////////////////
	//ENVIRONMENT STATE//
	/////////////////////

	/** Firefighters state. */
	static private Map<String, FireFighterState> firefighters = new HashMap<>();

	/**
	 * Returns Firefighter's states.
	 * Creates new map if needed.
	 * @return map with firefighters map
	 */
	static protected Map<String, FireFighterState> getFirefighters() {
		return firefighters;
	}

	/**
	 * Returns FireFighterState for given firefighter.
	 * Creates new state if needed.
	 * @param ffId firefighter id
	 * @return FireFighterState for given firefighter
	 */
	static protected FireFighterState getFirefighter(final String ffId) {
		final Map<String, FireFighterState> firefighters = getFirefighters();
		FireFighterState ff = firefighters.get(ffId);
		if (ff == null) {
			ff = new FireFighterState(ffId);
			firefighters.put(ffId, ff);
		}
		return ff;
	}

	/**
	 * Returns position of given firefighter or NaN with insufficient energy.
	 * Drains energy from the battery! If not enough energy left,
	 * Integer.MIN_VALUE returned.
	 * @param ffId firefighter id
	 * @return position of given firefighter or NaN with insufficient energy
	 */
	static public Position getPosition(final String ffId) {
		final FireFighterState ff = getFirefighter(ffId);
		ff.batteryLevel -= GPS_ENERGY_COST;
		if (ff.batteryLevel <= 0.0) {
			ff.batteryLevel = 0;
			return BAD_POSITION;
		} else {
			ff.inaccuracy = 0;
			return ff.position;
		}
	}

	/**
	 * Returns inaccuracy of given firefighter.
	 * @param ffId firefighter id
	 * @return inaccuracy of given firefighter
	 */
	static public int getInaccuracy(final String ffId) {
		return getFirefighter(ffId).inaccuracy;
	}

	/**
	 * Returns battery level of given firefighter.
	 * @param ffId firefighter id
	 * @return battery level of given firefighter
	 */
	static public int getBatteryLevel(final String ffId) {
		return getFirefighter(ffId).batteryLevel;
	}

	/**
	 * Returns temperature of given firefighter.
	 * @param ffId firefighter id
	 * @param temperature needed for making it non-operational
	 * @return battery level of given firefighter
	 */
	static public int getTemperature(final String ffId,
			final MetadataWrapper<Integer> temperature) {
		if (ffId.equals(FF_LEADER_ID)
				&& ProcessContext.getTimeProvider().getCurrentMilliseconds() >= THERMO_DEAD_TIME) {
			temperature.malfunction();
		}
		final FireFighterState ff =  getFirefighter(ffId);
		return SEGMENTS[ff.position.segment].temps[ff.position.index];
	}

	/** Environment component id. Not used, but mandatory. */
	public String id;

	/**
	 * Returns true if key is contained in the array
	 * @param array array to search in
	 * @param key key to search for
	 * @return true if key is contained in the array, false otherwise
	 */
	static private boolean contains(int[] array, int key) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == key) {
				return true;
			}
		}
		return false;
	}

	@Process
	@PeriodicScheduling(period=500)
	static public void simulation(
			@In("id") String id) {
		final Map<String, FireFighterState> firefighters = getFirefighters();
		for (final String ffId : firefighters.keySet()) {
			final FireFighterState ff = getFirefighter(ffId);

			int steps = RANDOM.nextInt(FF_MOVEMENT + 1);
			boolean decide = false; //in previous iteration we entered new segment, but we may leave it immediately into another segment
			int prevSeg = -1; //previously occupied segment
			while (steps > 0) {
				final Segment currSeg = SEGMENTS[ff.position.segment];
				int nextSegIndex = -1;
				if (decide) { //we need to decide whether stay in this segment, or go immediately to other
					int[] candidates;
					if (ff.position.index == 0) {
						candidates = currSeg.starts; //we are at the start
					} else /*if (ff.position.index == currSeg.temps.length - 1)*/ {
						candidates = currSeg.ends; //we are at the end
					}
					final int r = RANDOM.nextInt(candidates.length);
					if (candidates[r] == prevSeg) {
						//we stay
					} else {
						nextSegIndex = candidates[r];
					}
				} else {
					if (ff.direction > 0) { //going up
						ff.position.index += steps;
						steps = 0;
						if (ff.position.index >= currSeg.temps.length) { //end hit?
							//correct our position and steps left
							steps = ff.position.index - currSeg.temps.length + 1;
							ff.position.index = currSeg.temps.length - 1;

							//decide where to go next
							if (currSeg.ends.length == 0) {
								ff.direction = -1; //dead end, return
							} else {
								final int r = RANDOM.nextInt(currSeg.ends.length);
								nextSegIndex = currSeg.ends[r];
							}
						}
					} else /*if (ff.direction < 0)*/ { //going down
						ff.position.index -= steps;
						steps = 0;
						if (ff.position.index < 0) { //start hit?
							//correct our position and steps left
							steps = -ff.position.index;
							ff.position.index = 0;

							//decide where to go next
							if (currSeg.starts.length == 0) {
								ff.direction = 1; //dead end, return
							} else {
								final int r = RANDOM.nextInt(currSeg.starts.length);
								nextSegIndex = currSeg.starts[r];
							}
						}
					}
				}
				if (nextSegIndex != -1) {
					final Segment nextSeg = SEGMENTS[nextSegIndex];
					if (contains(nextSeg.ends, ff.position.segment)) {
						prevSeg = ff.position.segment;
						decide = nextSeg.ends.length > 1;
						ff.position.segment = nextSegIndex;
						ff.position.index = nextSeg.temps.length - 1;
					} else if (contains(nextSeg.starts, ff.position.segment)) {
						prevSeg = ff.position.segment;
						decide = nextSeg.starts.length > 1;
						ff.position.segment = nextSegIndex;
						ff.position.index = 0;
					} else {
						throw new IllegalArgumentException("Segments must be bidirectionally connected! (" + ff.position.segment + ", " + nextSegIndex + ")");
					}
					--steps; //we made a step
				}
			}

			System.out.println("TIME: " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
			System.out.println(ffId + " batteryLevel = " + ff.batteryLevel);
			System.out.println(ffId + " position = " + ff.position);
			System.out.println(ffId + " temperature = " + SEGMENTS[ff.position.segment].temps[ff.position.index]);

			if (ffId.equals(FF_LEADER_ID)) {
				final long time = ProcessContext.getTimeProvider().getCurrentMilliseconds();
				if (time >= GPS_BREAK_TIME) {
					ff.inaccuracy += BROKEN_GSP_INACURRACY;
				} else {
					ff.inaccuracy += FF_MOVEMENT;
				}
			} else {
				ff.inaccuracy += FF_MOVEMENT;
			}
		}
	}

	/**
	 * Simple holder of data related to firefighter's state.
	 */
	protected static class FireFighterState {

		/** Firefighter's position. */
		protected Position position = INITIAL_POSITION;

		/** Firefighter's position inaccuracy. */
		protected int inaccuracy = INITIAL_INACCURACY;

		/** Firefighter's battery level. */
		protected int batteryLevel = INITIAL_BATTERY_LEVEL;

		/** Movement direction. */
		protected byte direction;

		/**
		 * Only constructor.
		 * @param ffId firefighter id
		 */
		public FireFighterState(final String ffId) {
			direction = (byte) (LONELY_FF_ID.equals(ffId) ? -1 : 1);
		}
	}
}
