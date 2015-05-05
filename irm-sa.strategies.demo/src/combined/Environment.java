package combined;

import static combined.HeatMap.CORRIDORS;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import combined.HeatMap.Corridor;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;
import dijkstra.Dijkstra;
import dijkstra.Edge;
import dijkstra.Vertex;
import filter.DoubleNoise;
import filter.PositionNoise;

/**
 * This whole component is a kind of hack.
 * It serves as sensors to FightFighter components.
 */
@Component
@SystemComponent
public class Environment {

	/** Firefighters' initial location. */
	static private final Location INITIAL_LOCATION = new Location(19, 0);

	/** Firefighters' initial position. */
	static public final Position INITIAL_POSITION = INITIAL_LOCATION.toPosition();

	/** Firefighters' initial inaccuracy. */
	static public final Integer INITIAL_INACCURACY = 0;

	/** Firefighters' initial battery level. */
	static public final Double INITIAL_BATTERY_LEVEL = 1600.0;

	/** Firefighters' initial temperature. */
	static public final Double INITIAL_TEMPERATURE = HeatMap.temperature(INITIAL_LOCATION);

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
	 * Average movement of a firefighter in a ms.
	 * Also inaccuracy caused by regular firefighter movement.
	 */
	static public final double FF_MOVEMENT = 0.004;

	/** Bonus speed for follower firefighter to keep the group together. */
	static private final double FF_BONUS = 0.25 * FF_MOVEMENT;

	/** Inaccuracy in case of GPS malfunction. */
	static public final double BROKEN_GSP_INACURRACY = FF_MOVEMENT * 2.25;

	/** Simulation tick in ms. */
	static public final long SIMULATION_PERIOD = 50;

	/** HeatMap square size in meters. */
	static public final double CORRIDOR_SIZE = 2.0;

	/** Initial period for determine position. */
	static public final long INITIAL_POSITION_PERIOD = 1250;

	/** Initial value of inaccuracy assumption parameter. */
	static public final double FF_POS_INAC_BOUND =
			INITIAL_POSITION_PERIOD / SIMULATION_PERIOD * BROKEN_GSP_INACURRACY * SIMULATION_PERIOD * 0.95;

	/** Maximal value of inaccuracy assumption parameter. */
	static public final double FF_POS_INAC_BOUND_MAX = FF_POS_INAC_BOUND * 1.25;

	/** Minimal value of inaccuracy assumption parameter. */
	static public final double FF_POS_INAC_BOUND_MIN = FF_POS_INAC_BOUND * 0.75;

	/** RNG. */
	static private final Random RANDOM = new Random(24);

	/** Filter for position. */
	static private PositionNoise positionNoise = new PositionNoise(0.0, 0.3);

	/** Filter for position if GPS is broken. */
	static private PositionNoise brokenGPSInaccuracy = new PositionNoise(0.0, 1.5, positionNoise);

	/** Filter for battery level. */
	static private DoubleNoise batteryNoise = new DoubleNoise(0.0, 1.0);

	/** Filter for temperature. */
	static private DoubleNoise temperatureNoise = new DoubleNoise(0.0, 2.0);

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
	 * Returns location of the firefighter with the given id.
	 * @param ffId firefighter id
	 * @return firefighter's location
	 */
	static Location getLocation(final String ffId) {
		return getFirefighter(ffId).location;
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
			if (ffId.equals(FF_LEADER_ID)
					&& ProcessContext.getTimeProvider().getCurrentMilliseconds() >= GPS_BREAK_TIME) {
				return brokenGPSInaccuracy.apply(ff.location.toPosition());
			} else {
				return positionNoise.apply(ff.location.toPosition());
			}
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
	static public double getRealBatteryLevel(final String ffId) {
		return getFirefighter(ffId).batteryLevel;
	}

	/**
	 * Returns battery level of given firefighter.
	 * @param ffId firefighter id
	 * @return battery level of given firefighter
	 */
	static public double getBatteryLevel(final String ffId) {
		return batteryNoise.apply(getFirefighter(ffId).batteryLevel);
	}

	/**
	 * Just retrieves the temperature
	 * @param ffId firefighter id
	 * @return firefighter environment temperature
	 */
	static public double getRealTemperature(final String ffId) {
		final FireFighterState ff =  getFirefighter(ffId);
		return HeatMap.temperature(ff.location);
	}

	/**
	 * Returns temperature of given firefighter.
	 * @param ffId firefighter id
	 * @param temperature needed for making it non-operational
	 * @return battery level of given firefighter
	 */
	static public double getTemperature(final String ffId,
			final MetadataWrapper<Double> temperature) {
		if (ffId.equals(FF_LEADER_ID)
				&& ProcessContext.getTimeProvider().getCurrentMilliseconds() >= THERMO_DEAD_TIME) {
			temperature.malfunction();
		}
		return temperatureNoise.apply(getRealTemperature(ffId));
	}

	/** Environment component id. Not used, but mandatory. */
	public String id;

	/**
	 * Prepares firefighter's plan how to get to its target.
	 * @param ff firefighter which needs new plan
	 */
	static private void preparePlan(final FireFighterState ff) {
		ff.plan.clear(); //discard previous plan
		final Corridor startCorr = CORRIDORS.get(ff.location.corridor);
		final Corridor targetCorr = CORRIDORS.get(ff.target.corridor);
		if (ff.location.corridor == ff.target.corridor) { //correct corridor?
			if (ff.location.index > ff.target.index) { //facing wrong way?
				//turn back
				ff.location.corridor = startCorr.opposite;
				ff.location.index = startCorr.weight - ff.location.index;
				//move target to correct corridor
				ff.target.corridor = startCorr.opposite;
				ff.target.index = startCorr.weight - ff.target.index;
			}
			return; //no other plan needed
		}
		if (startCorr.opposite == ff.target.corridor) {
			final double targetIndexInStartCorr = startCorr.weight - ff.target.index;
			if (ff.location.index > targetIndexInStartCorr) { //facing wrong way?
				//turn back
				ff.location.corridor = startCorr.opposite;
				ff.location.index = startCorr.weight - ff.location.index;
			} else { //facing right way
				//move target to correct corridor
				ff.target.corridor = startCorr.index;
				ff.target.index = targetIndexInStartCorr;
			}
			return; //no other plan needed
		}

		//find corridors to go through to get to target
		final Vertex<Position> start = new Vertex<>(ff.location.toPosition());
		final Vertex<Position> target = new Vertex<>(ff.target.toPosition());

		//add new vertices and edges for start and target locations
		//from start to current corridor end
		final double w1 = startCorr.weight - ff.location.index;
		final Vertex<Position> v1 = startCorr.target;
		final AuxiliaryEdge e1 = new AuxiliaryEdge(v1, w1, startCorr.index);
		start.adjacencies.add(e1);

		//from start to current corridor start
		final double w2 = ff.location.index;
		final Vertex<Position> v2 = startCorr.source;
		final AuxiliaryEdge e2 = new AuxiliaryEdge(v2, w2, startCorr.opposite);
		start.adjacencies.add(e2);

		//from target corridor start to target location
		final double w3 = ff.target.index;
		final Vertex<Position> v3 = target;
		final AuxiliaryEdge e3 = new AuxiliaryEdge(v3, w3, targetCorr.index);
		targetCorr.source.adjacencies.add(e3);

		//from target corridor end to target location
		final double w4 = ff.target.index;
		final Vertex<Position> v4 = target;
		final AuxiliaryEdge e4 = new AuxiliaryEdge(v4, w4, targetCorr.opposite);
		targetCorr.target.adjacencies.add(e4);

		try {
			Dijkstra.computePaths(start);
			final List<Edge<Position>> path = Dijkstra.getEdgesOnShortestPathTo(target);
			if (path.size() < 2) {
				throw new RuntimeException("NO PATH FOUND?! " + ff.location + "->" + ff.target); //something wrong!
			}
			//process first edge
			final Edge<Position> first = path.get(0);
			if (!(first instanceof AuxiliaryEdge)) {
				throw new RuntimeException("Unexpected path!"); //something wrong!
			}
			if (startCorr.opposite == ((AuxiliaryEdge) first).index) { //facing wrong way?
				//turn back
				ff.location.corridor = startCorr.opposite;
				ff.location.index = startCorr.weight - ff.location.index;
			}
			//process edges from second to the one before last
			for (int i = 1; i < path.size() - 1; ++i) {
				final Edge<Position> edge = path.get(i);
				if (edge instanceof Corridor) {
					final int corridor = ((Corridor) edge).index;
					ff.plan.add(corridor); //filling the plan
				} else {
					throw new RuntimeException("Unexpected edges on path!"); //something wrong!
				}
			}
			//process last edge
			final Edge<Position> last = path.get(path.size() - 1);
			if (!(last instanceof AuxiliaryEdge)) {
				throw new RuntimeException("Unexpected path!"); //something wrong!
			}
			if (targetCorr.opposite == ((AuxiliaryEdge) last).index) { //is the target on opposite corridor?
				//move target to correct corridor
				ff.target.corridor = targetCorr.opposite;
				ff.target.index = targetCorr.weight - ff.target.index;
			}
			ff.plan.add(ff.target.corridor); //last corridor to plan
		} finally {
			//clean up, restore initial state
			v3.adjacencies.remove(e3);
			v4.adjacencies.remove(e4);
			for (Vertex<Position> v : HeatMap.JUNCTIONS) {
				v.reset();
			}
		}
	}

	/**
	 * Computes movement for given firefighter in one simulation tick.
	 * @param ffId firefighter id
	 * @param ff firefighter state
	 * @return movement for given firefighter
	 */
	static private double computeMovement(final String ffId, final FireFighterState ff) {
		//TODO fixed speed with variation
		final double bonus = ffId.equals(FF_FOLLOWER_ID) ? FF_BONUS : 0;
		return RANDOM.nextDouble() * (FF_MOVEMENT + bonus) * SIMULATION_PERIOD;
	}

	/**
	 * Generates random location as firefighter target.
	 * @return random location
	 */
	static Location randomTarget() {
		final int corridor = RANDOM.nextInt(CORRIDORS.size());
		final int index = RANDOM.nextInt(CORRIDORS.get(corridor).temps.length);
		return new Location(corridor, index);
	}

	/**
	 * Moves given firefighter.
	 * @param ff firefighter to move
	 */
	static private void moveFireFighter(final String ffId, final FireFighterState ff) {
		if (ffId.equals(FF_FOLLOWER_ID)) {
			//follower's  target moves, the plan must be recalculated
			final FireFighterState leader = getFirefighter(FF_LEADER_ID);
			ff.target = leader.location.clone();
			preparePlan(ff);
		}
		double movement = computeMovement(ffId, ff);
		System.out.println("+++ " + ffId + " MOVEMENT: " + movement);
		while (movement > 0) { //move while we can
			final Corridor currCorr = CORRIDORS.get(ff.location.corridor);
			if (ff.target.corridor == currCorr.index) { //are we in target corridor?
				if (ff.location.index + movement >= ff.target.index) {
					//we are here, do not move more than needed
					movement -= ff.target.index - ff.location.index;
					ff.target = randomTarget(); //new target
					preparePlan(ff); //prepare plan
				} else {
					//we cannot reach the target in this simulation tick
					ff.location.index += movement;
					break; //no movement left
				}
			} else { //we are not in target corridor, lets move on
				if (ff.location.index + movement >= currCorr.weight) {
					//we hit the end of corridor
					movement -= currCorr.weight - ff.location.index;
					try {
					ff.location.corridor = ff.plan.remove(); //follow the plan
					} catch (NoSuchElementException e) {
						e.printStackTrace();
					}
					ff.location.index = 0.0; //new position in corridor
				} else {
					//we cannot reach the corridor end in this simulation tick
					ff.location.index += movement;
					break; //no movement left
				}
			}
		}
	}

	@Process
	@PeriodicScheduling(period=SIMULATION_PERIOD, order = 1)
	static public void simulation(
			@In("id") String id) {
		final Map<String, FireFighterState> firefighters = getFirefighters();
		for (final String ffId : firefighters.keySet()) {
			final FireFighterState ff = getFirefighter(ffId);

			moveFireFighter(ffId, ff);

			System.out.println("TIME: " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
			System.out.println(ffId + " batteryLevel = " + ff.batteryLevel);
			System.out.println(ffId + " position = " + ff.location);
			System.out.println(ffId + " temperature = " + HeatMap.temperature(ff.location));

			if (ffId.equals(FF_LEADER_ID)) {
				final long time = ProcessContext.getTimeProvider().getCurrentMilliseconds();
				if (time >= GPS_BREAK_TIME) {
					ff.inaccuracy += BROKEN_GSP_INACURRACY * SIMULATION_PERIOD;
				} else {
					ff.inaccuracy += FF_MOVEMENT * SIMULATION_PERIOD;
				}
			} else {
				ff.inaccuracy += FF_MOVEMENT;
			}
		}
		final FireFighterState leader = getFirefighter(FF_LEADER_ID);
		final FireFighterState follower = getFirefighter(FF_FOLLOWER_ID);
		if (leader != null && follower != null) {
			System.out.println("#########################################");
			System.out.println("LEADER POS: " + leader.location + "(" + leader.location.corridor + ", " + leader.location.index + ")");
			System.out.println("FOLLOW POS: " + follower.location  + "(" + follower.location.corridor + ", " + follower.location.index + ")");
			System.out.println("DISTANCE  : " + LocationMetric.distance(leader.location, follower.location));
		}
	}

	/**
	 * Simple holder of data related to firefighter's state.
	 */
	protected static class FireFighterState {

		/** Firefighter's location. */
		protected Location location = INITIAL_LOCATION.clone();

		/** Firefighter's position inaccuracy. */
		protected double inaccuracy = INITIAL_INACCURACY;

		/** Firefighter's battery level. */
		protected double batteryLevel = INITIAL_BATTERY_LEVEL;

		/** Target where the firefighter moves. */
		protected Location target = randomTarget();

		/** Plan how to reach the target. It contains list of corridor indices. */
		protected Deque<Integer> plan = new ArrayDeque<>();

		/**
		 * Only constructor.
		 * @param ffId firefighter id
		 */
		public FireFighterState(final String ffId) {
			preparePlan(this);
		}
	}

	/**
	 * Auxiliary class for computing plans.
	 */
	static class AuxiliaryEdge extends Edge<Position> {

		/** Corridor index. */
		public final int index;

		/**
		 * Only constructor.
		 * @param target target vertex
		 * @param weight corridor length
		 * @param index corridor index
		 */
		public AuxiliaryEdge(final Vertex<Position> target, final double weight, final int index) {
			super(target, weight);
			this.index = index;
		}
	}
}
