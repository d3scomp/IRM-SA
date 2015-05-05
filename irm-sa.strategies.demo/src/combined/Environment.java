package combined;

import static combined.HeatMap.SEGMENTS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import combined.HeatMap.Segment;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;
import filter.DoubleNoise;
import filter.PositionNoise;

/**
 * This whole component is a kind of hack.
 * It serves as sensors to FightFighter components.
 */
@Component
@SystemComponent
public class Environment {

	/** Firefighters' initial position. */
	static private final Location INITIAL_POSITION = new Location(19, 0);
	
	static public final Position INITIAL_FF_POSITION = INITIAL_POSITION.toPositionComponent();

	/** Firefighters' initial inaccuracy. */
	static public final Integer INITIAL_INACCURACY = 0;

	/** Firefighters' initial battery level. */
	static public final Double INITIAL_BATTERY_LEVEL = 1600.0;

	/** Firefighters' initial temperature. */
	static public final Double INITIAL_TEMPERATURE = HeatMap.temperature(INITIAL_POSITION);

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

	static Location getRealPosition(final String ffId) {
		return getFirefighter(ffId).position;
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
				return brokenGPSInaccuracy.apply(ff.position.toPositionComponent());
			} else {
				return positionNoise.apply(ff.position.toPositionComponent());
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
		return HeatMap.temperature(ff.position);
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
	 * Returns index of segment to go on shortest path.
	 * @param from start position, must be start/end of corridor
	 * @param to target position
	 * @return index of segment to go on shortest path
	 */
	static private int findShortestPath(final Location from, final Location to) {
		//TODO do not compute this on every crossing, but store the result?
		final Segment s = HeatMap.SEGMENTS[from.segment];
		final Segment e = HeatMap.SEGMENTS[to.segment];

		final Vertex<Location, Integer> start = new Vertex<>(from);

		final double w1 = PositionMetric.distance(from, s.startCrossing);
		final Vertex<Location, Integer> n1 = HeatMap.GRAPH.get(s.startCrossing);
		final Edge<Location, Integer> e1 = new Edge<>(n1, w1, -1);
		start.adjacencies.add(e1);

		final double w2 = PositionMetric.distance(from, s.endCrossing);
		final Vertex<Location, Integer> n2 = HeatMap.GRAPH.get(s.endCrossing);
		final Edge<Location, Integer> e2 = new Edge<>(n2, w2, -1);
		start.adjacencies.add(e2);

		final Vertex<Location, Integer> end = new Vertex<>(to);

		final double w3 = PositionMetric.distance(to, e.startCrossing);
		final Vertex<Location, Integer> n3 = HeatMap.GRAPH.get(e.startCrossing);
		final Edge<Location, Integer> e3 = new Edge<>(end, w3, to.segment);
		n3.adjacencies.add(e3);

		final double w4 = PositionMetric.distance(to, e.endCrossing);
		final Vertex<Location, Integer> n4 = HeatMap.GRAPH.get(e.endCrossing);
		final Edge<Location, Integer> e4 = new Edge<>(end, w4, to.segment);
		n4.adjacencies.add(e4);

		try {
			Dijkstra.computePaths(start);
			final List<Vertex<Location, Integer>> path = Dijkstra.getShortestPathTo(end);
			if (path.size() < 3) {
				//ups
				System.err.println("NO PATH FOUND?! " + from + "->" + to);
				return -1;
			}
			int result = -1;
			final Vertex<Location, Integer> first = path.get(1); //zero is start
			if (from.segment != first.value.segment) {
				result = first.value.segment;
			} else {
				final Vertex<Location, Integer> second = path.get(2);
				for (Edge<Location, Integer> edge : first.adjacencies) {
					if (edge.target.equals(second)) {
						result = edge.value;
						break;
					}
				}
			}
			System.out.println("+++Dijkstra says: " + result);
			return result;
		} finally {
			n3.adjacencies.remove(e3);
			n4.adjacencies.remove(e4);
			for (Vertex<Location, Integer> v : HeatMap.GRAPH.values()) {
				v.reset();
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

			final double bonus = ffId.equals(FF_FOLLOWER_ID) ? FF_BONUS : 0;
			double steps = RANDOM.nextDouble() * (FF_MOVEMENT + bonus) * SIMULATION_PERIOD;
			System.out.println("+++" + ffId + " STEPS: " + steps);
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
					if (ffId.equals(FF_FOLLOWER_ID)) {
						final FireFighterState leader = getFirefighter(FF_LEADER_ID);
						if (leader.position.segment == ff.position.segment) {
							//we stay
						} else {
							nextSegIndex = findShortestPath(ff.position, leader.position);
							if (!Utils.contains(candidates, nextSegIndex)) { //should we turn around?
								nextSegIndex = -1;
								ff.direction = (byte) -ff.direction;
							}
						}
					} else {
						final int r = RANDOM.nextInt(candidates.length);
						if (candidates[r] == prevSeg) {
							//we stay
						} else {
							nextSegIndex = candidates[r];
						}
					}
					decide = false; // we have decided
				} else {
					if (ff.direction > 0) { //going up
						ff.position.index += steps;
						steps = 0;
						if (ff.position.index > currSeg.temps.length - 1) { //end hit?
							//correct our position and steps left
							steps = ff.position.index - currSeg.temps.length + 1;
							ff.position.index = currSeg.temps.length - 1;

							//decide where to go next
							if (currSeg.ends.length == 0) {
								ff.direction = -1; //dead end, return
							} else {
								if (ffId.equals(FF_FOLLOWER_ID)) {
									final FireFighterState leader = getFirefighter(FF_LEADER_ID);
									if (leader.position.segment == ff.position.segment) {
										//we stay
										steps = 0;
									} else {
										nextSegIndex = findShortestPath(ff.position, leader.position);
										if (!Utils.contains(currSeg.ends, nextSegIndex)) { //should we turn around?
											nextSegIndex = -1;
											ff.direction = (byte) -ff.direction;
										}
									}
								} else {
									final int r = RANDOM.nextInt(currSeg.ends.length);
									nextSegIndex = currSeg.ends[r];
								}
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
								if (ffId.equals(FF_FOLLOWER_ID)) {
									final FireFighterState leader = getFirefighter(FF_LEADER_ID);
									if (leader.position.segment == ff.position.segment) {
										//we stay
										steps = 0;
									} else {
										nextSegIndex = findShortestPath(ff.position, leader.position);
										if (!Utils.contains(currSeg.starts, nextSegIndex)) { //should we turn around?
											nextSegIndex = -1;
											ff.direction = (byte) -ff.direction;
										}
									}
								} else {
									final int r = RANDOM.nextInt(currSeg.starts.length);
									nextSegIndex = currSeg.starts[r];
								}
							}
						}
					}
				}
				if (nextSegIndex > 0) { //TODO fix when segments are reindexed
					final Segment nextSeg = SEGMENTS[nextSegIndex];
					if (Utils.contains(nextSeg.ends, ff.position.segment)) {
						prevSeg = ff.position.segment;
						decide = nextSeg.ends.length > 1;
						ff.position.segment = nextSegIndex;
						ff.position.index = nextSeg.temps.length - 1;
						ff.direction = -1;
					} else if (Utils.contains(nextSeg.starts, ff.position.segment)) {
						prevSeg = ff.position.segment;
						decide = nextSeg.starts.length > 1;
						ff.position.segment = nextSegIndex;
						ff.position.index = 0;
						ff.direction = 1;
					} else {
						throw new IllegalArgumentException("Segments must be bidirectionally connected! (" + ff.position.segment + ", " + nextSegIndex + ")");
					}
					--steps; //we made a step
				}
			}

			System.out.println("TIME: " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
			System.out.println(ffId + " batteryLevel = " + ff.batteryLevel);
			System.out.println(ffId + " position = " + ff.position);
			System.out.println(ffId + " temperature = " + HeatMap.temperature(ff.position));

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
			System.out.println("LEADER POS: " + leader.position + "(" + leader.position.segment + ", " + leader.position.index + ")");
			System.out.println("FOLLOW POS: " + follower.position  + "(" + follower.position.segment + ", " + follower.position.index + ")");
			System.out.println("DISTANCE  : " + PositionMetric.distance(leader.position, follower.position));
		}
	}

	/**
	 * Simple holder of data related to firefighter's state.
	 */
	protected static class FireFighterState {

		/** Firefighter's position. */
		protected Location position = INITIAL_POSITION.clone();

		/** Firefighter's position inaccuracy. */
		protected double inaccuracy = INITIAL_INACCURACY;

		/** Firefighter's battery level. */
		protected double batteryLevel = INITIAL_BATTERY_LEVEL;

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
