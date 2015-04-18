package combined;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;
import static combined.HeatMap.heatMap;

/**
 * This whole component is a kind of hack.
 * It serves as sensors to FightFighter components.
 */
@Component
@SystemComponent
public class Environment {

	/** Firefighters' initial position. */
	static public final Integer INITIAL_POSITION = 100;

	/** Firefighters' initial inaccuracy. */
	static public final Integer INITIAL_INACCURACY = 0;

	/** Firefighters' initial battery level. */
	static public final Integer INITIAL_BATTERY_LEVEL = 1600;

	/** Firefighters' initial temperature. */
	static public final Integer INITIAL_TEMPERATURE = heatMap[INITIAL_POSITION];

	/** Checking position drains this much energy from battery. */
	static private final int GPS_ENERGY_COST = 4;

	/** FF1's GPS loses precision at this time. */
	static private final int GPS_BREAK_TIME = 50_000;

	/** FF1's thermometer dies at this time. */
	static private final int THERMO_DEAD_TIME = 150_000;

	/** Maximal distance between group members. */
	static final int MAX_GROUP_DISTANCE = 8;

//	/** Firefighters' states stored in internal data under this key. */
//	static private final String FIREFIGHTERS = "Firefighters";

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
	static private Map<String, FireFighterState> $$firefighters = new HashMap<>();

	/**
	 * Returns Firefighter's states.
	 * Creates new map if needed.
	 * @return map with firefighters map
	 */
	static protected Map<String, FireFighterState> getFirefighters() {
//		final ComponentProcess process = ProcessContext.getCurrentProcess();
//		final ComponentInstance instance = process.getComponentInstance();
//		final EMap<String, Object> data = instance.getInternalData();
//		final Object obj = data.get(FIREFIGHTERS);
//		if (obj == null || !(obj instanceof Map)) {
//			final Map<String, FireFighterState> ffs = new HashMap<>();
//			data.put(FIREFIGHTERS, ffs);
//			return ffs;
//		} else {
//			@SuppressWarnings("unchecked")
//			final Map<String, FireFighterState> ffs = (Map<String, FireFighterState>) obj;
//			return ffs;
//		}
		return $$firefighters;
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
	static public int getPosition(final String ffId, final MetadataWrapper<Integer> position) {
		final FireFighterState ff = getFirefighter(ffId);
		ff.batteryLevel -= GPS_ENERGY_COST;
		if (ff.batteryLevel <= 0.0) {
			ff.batteryLevel = 0;
			return Integer.MIN_VALUE;
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
		return heatMap[ff.position];
	}

	/** Environment component id. Not used, but mandatory. */
	public String id;

	@Process
	@PeriodicScheduling(period=500)
	static public void simulation(
			@In("id") String id) {
		final Map<String, FireFighterState> firefighters = getFirefighters();
		for (final String ffId : firefighters.keySet()) {
			final FireFighterState ff = getFirefighter(ffId);

			final int d = RANDOM.nextInt(FF_MOVEMENT + 1);
			ff.position += ff.direction * d;
			if (ff.position < 0) {
				ff.position = -ff.position;
				ff.direction = 1;
			} else if (ff.position >= heatMap.length) {
				ff.position = heatMap.length - (ff.position - heatMap.length) - 1;
				ff.direction = -1;
			}

			if (ffId.equals(FF_FOLLOWER_ID)) {
				final FireFighterState leader = firefighters.get(FF_LEADER_ID);
				if (leader != null && Math.abs(leader.position - ff.position) >= MAX_GROUP_DISTANCE) {
					if (leader.position < ff.position) {
						ff.position = leader.position + MAX_GROUP_DISTANCE;
					} else {
						ff.position = leader.position - MAX_GROUP_DISTANCE;
					}
				}
			}

			System.out.println("TIME: " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
			System.out.println(ffId + " batteryLevel = " + ff.batteryLevel);
			System.out.println(ffId + " position = " + ff.position);
			System.out.println(ffId + " temperature = " + heatMap[ff.position]);

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
		protected int position = INITIAL_POSITION;

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
