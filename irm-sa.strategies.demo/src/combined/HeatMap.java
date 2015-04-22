package combined;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment heat map holder.
 */
public class HeatMap {

	/** Environment heat map. */
	static final int[][] HEAT_MAP = new int[][] {
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },

		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },

		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },

		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
		{  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 }
	};

	/**
	 * Segments/corridors. Minimal length at least 2!
	 * Do not use adjacent crossings! Ie. do not create the following:
	 *   #
	 *   #
	 * ######
	 *    #
	 *    #
	 */
	static final Segment[] SEGMENTS = new Segment[] {
		/* 0*/ null, //zero segment => indexing starts from 1 :-( //TODO reindex
		/* 1*/ new Segment(new int[0], new int[] {2}, 0, 0, 3, 0),
		/* 2*/ new Segment(new int[] {1}, new int[] {3}, 4, 0, 4, 4),
		/* 3*/ new Segment(new int[] {2, 6}, new int[] {4}, 4, 5, 1, 5),
		/* 4*/ new Segment(new int[] {3}, new int[] {5}, 1, 6, 1, 10),
		/* 5*/ new Segment(new int[] {4, 8}, new int[] {7}, 1, 11, 7, 11),
		/* 6*/ new Segment(new int[] {3}, new int[] {7, 19, 20}, 5, 5, 8, 5),
		/* 7*/ new Segment(new int[] {6}, new int[] {5}, 8, 6, 8, 11),
		/* 8*/ new Segment(new int[] {5}, new int[] {9}, 1, 12, 1, 17),
		/* 9*/ new Segment(new int[] {8}, new int[] {10}, 2, 17, 4, 17),
		/*10*/ new Segment(new int[] {9}, new int[] {11}, 5, 17, 5, 14),
		/*11*/ new Segment(new int[] {10}, new int[] {12}, 6, 14, 7, 14),
		/*12*/ new Segment(new int[] {11}, new int[] {13}, 8, 14, 8, 17),
		/*13*/ new Segment(new int[] {12}, new int[] {14}, 9, 17, 12, 17),
		/*14*/ new Segment(new int[] {13, 15}, new int[0], 13, 17, 13, 19),
		/*15*/ new Segment(new int[] {14}, new int[] {16}, 14, 17, 16, 17),
		/*16*/ new Segment(new int[] {15}, new int[] {17}, 17, 17, 17, 12),
		/*17*/ new Segment(new int[] {16}, new int[] {18}, 16, 12, 10, 12),
		/*18*/ new Segment(new int[] {17}, new int[] {19}, 10, 11, 10, 6),
		/*19*/ new Segment(new int[] {6}, new int[] {18, 21}, 9, 5, 10, 5),
		/*20*/ new Segment(new int[] {6}, new int[0], 8, 4, 8, 1),
		/*21*/ new Segment(new int[] {19}, new int[] {22, 23, 24}, 11, 5, 12, 5),
		/*22*/ new Segment(new int[] {21}, new int[0], 12, 4, 12, 3),
		/*23*/ new Segment(new int[] {21}, new int[0], 12, 6, 12, 7),
		/*24*/ new Segment(new int[] {21}, new int[] {25, 26, 27}, 13, 5, 14, 5),
		/*25*/ new Segment(new int[] {24}, new int[0], 14, 4, 14, 3),
		/*26*/ new Segment(new int[] {24}, new int[0], 14, 6, 14, 7),
		/*27*/ new Segment(new int[] {24}, new int[] {28, 29, 30}, 15, 5, 16, 5),
		/*28*/ new Segment(new int[] {27}, new int[0], 16, 4, 16, 3),
		/*29*/ new Segment(new int[] {27}, new int[0], 16, 6, 16, 7),
		/*30*/ new Segment(new int[] {27}, new int[] {31}, 17, 5, 18, 5),
		/*31*/ new Segment(new int[] {30}, new int[] {32}, 18, 6, 18, 8),
		/*32*/ new Segment(new int[] {31}, new int[0], 18, 9, 12, 9)
	};

	static final Map<Position, Vertex<Position, Integer>> GRAPH;

	static {
		//TODO convert segments to graph
		GRAPH = new HashMap<>();
		for (int i = 1; i < SEGMENTS.length; ++i) { //TODO fix when segments are reindexed
			final Segment s = SEGMENTS[i];

			//create start crossing if needed
			if (s.startCrossing == null) {
				if (s.starts.length == 0) {
					s.startCrossing = new Position(i, 0);
				} else if (s.starts.length == 1) {
					final Segment other = SEGMENTS[s.starts[0]];
					//decide who contains the crossing
					if (Utils.contains(other.starts, i)) {
						if (other.startCrossing != null) {
							s.startCrossing = other.startCrossing;
						} else {
							if (s.x1 == other.x2 || s.y1 == other.y2) { //our start
								s.startCrossing = new Position(i, 0);
							} else if (other.x1 == s.x2 || other.y1 == s.y2) { //their start
								s.startCrossing = new Position(s.starts[0], 0);
							} else {
								throw new RuntimeException("This should never happen! Probably badly defined segments!");
							}
							other.startCrossing = s.startCrossing;
						}
					} else if (Utils.contains(other.ends, i)) {
						if (other.endCrossing != null) {
							s.startCrossing = other.endCrossing;
						} else {
							//decide who contains the crossing
							if (s.x1 == other.x1 || s.y1 == other.y1) { //our start
								s.startCrossing = new Position(i, 0);
							} else if (other.x2 == s.x2 || other.y2 == s.y2) { //their end
								s.startCrossing = new Position(s.starts[0], other.temps.length - 1);
							} else {
								throw new RuntimeException("This should never happen! Probably badly defined segments!");
							}
							other.endCrossing = s.startCrossing;
						}
					} else { // not bidirectional?
						throw new RuntimeException("This should never happen! Probably badly defined segments!");
					}
				} else /*if (s.starts.length > 1)*/ {
					//do nothing, one of the other ends will take care of it
				}
			}

			//create end crossing if needed
			if (s.endCrossing == null) {
				if (s.ends.length == 0) {
					s.endCrossing = new Position(i, s.temps.length - 1);
				} else if (s.ends.length == 1) {
					final Segment other = SEGMENTS[s.ends[0]];
					//decide who contains the crossing
					if (Utils.contains(other.starts, i)) {
						if (other.startCrossing != null) {
							s.endCrossing = other.startCrossing;
						} else {
							if (s.x2 == other.x2 || s.y2 == other.y2) { //our end
								s.endCrossing = new Position(i, s.temps.length - 1);
							} else if (other.x1 == s.x1 || other.y1 == s.y1) { //their start
								s.endCrossing = new Position(s.ends[0], 0);
							} else {
								throw new RuntimeException("This should never happen! Probably badly defined segments!");
							}
							other.startCrossing = s.endCrossing;
						}
					} else if (Utils.contains(other.ends, i)) {
						if (other.endCrossing != null) {
							s.endCrossing = other.endCrossing;
						} else {
							//decide who contains the crossing
							if (s.x2 == other.x1 || s.y2 == other.y1) { //our end
								s.endCrossing = new Position(i, s.temps.length - 1);
							} else if (other.x2 == s.x1 || other.y2 == s.y1) { //their end
								s.endCrossing = new Position(s.ends[0], other.temps.length - 1);
							} else {
								throw new RuntimeException("This should never happen! Probably badly defined segments!");
							}
							other.endCrossing = s.endCrossing;
						}
					} else { // not bidirectional?
						throw new RuntimeException("This should never happen! Probably badly defined segments!");
					}
				} else /*if (s.endss.length > 1)*/ {
					//do nothing, one of the other ends will take care of it
				}
			}
		}

		for (int i = 1; i < SEGMENTS.length; ++i) { //TODO fix when segments are reindexed
			final Segment s = SEGMENTS[i];
			if (s.startCrossing == null) {
				throw new RuntimeException("This should never happen! Check segment definitions.");
			}
			if (s.endCrossing == null) {
				throw new RuntimeException("This should never happen! Check segment definitions.");
			}
			final double w = PositionMetric.distance(s.startCrossing, s.endCrossing);
			Vertex<Position, Integer> start = GRAPH.get(s.startCrossing);
			if (start == null) {
				start = new Vertex<>(s.startCrossing);
				GRAPH.put(start.value, start);
			}
			Vertex<Position, Integer> end = GRAPH.get(s.endCrossing);
			if (end == null) {
				end = new Vertex<>(s.endCrossing);
				GRAPH.put(end.value, end);
			}
			final Edge<Position, Integer> edge1 = new Edge<>(end, w, i);
			start.adjacencies.add(edge1);
			final Edge<Position, Integer> edge2 = new Edge<>(start, w, i);
			end.adjacencies.add(edge2);
		}

		//debug
//		List<Vertex<Position, Integer>>  list = new ArrayList<>(GRAPH.values());
//		Dijkstra.computePaths(GRAPH.get(new Position(1, 0)));
//		Collections.sort(list);
//		for (Vertex<Position, Integer> v : list) {
//			System.out.println("Crossing " + v.value);
//			for (Edge<Position, Integer> e : v.adjacencies) {
//				System.out.println("Corridor " + e.value + " to " + e.target.value + " of length " + e.weight);
//			}
//		}
	}

	/**
	 * Class representing corridor in which FF move.
	 */
	public static class Segment {

		/** Corridors at start. */
		public final int[] starts;

		/** Corridors at end. */
		public final int[] ends;

		/** Temperatures on corridors squares. */
		public final int[] temps;

		/** Start X-coord. */
		public final int x1;

		/** Start Y-coord. */
		public final int y1;

		/** End X-coord. */
		public final int x2;

		/** End Y-coord. */
		public final int y2;

		/** Segment orientation. */
		public final Orientation orientation;

		/** Crossing at the start. */
		public Position startCrossing;

		/** Crossing at the start. */
		public Position endCrossing;

		/** Weight for dijkstra. */
		public double weigth;

		/**
		 * Only constructor
		 * @param starts segments at start
		 * @param ends segments at end
		 * @param x1 Start X-coord
		 * @param y1 Start Y-coord
		 * @param x2 End X-coord
		 * @param y2 End Y-coord
		 */
		public Segment(final int[] starts, final int[] ends,
				final int x1, final int y1, final int x2, final int y2) {
			assert x1 == x2 || y1 == y2;
			this.starts = starts;
			this.ends = ends;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			if (x1 == x2) {
				if (y1 < y2) {
					temps = new int[y2 - y1 + 1];
					for (int i = y1; i <= y2; ++i) {
						temps[i - y1] = HEAT_MAP[i][x1];
					}
					orientation = Orientation.S;
				} else {
					temps = new int[y1 - y2 + 1];
					for (int i = y1; i >= y2; --i) {
						temps[y1 - i] = HEAT_MAP[i][x1];
					}
					orientation = Orientation.N;
				}
			} else if (y1 == y2) {
				if (x1 < x2) {
					temps = new int[x2 - x1 + 1];
					for (int i = x1; i <= x2; ++i) {
						temps[i - x1] = HEAT_MAP[y1][i];
					}
					orientation = Orientation.E;
				} else {
					temps = new int[x1 - x2 + 1];
					for (int i = x1; i >= x2; --i) {
						temps[x1 - i] = HEAT_MAP[y1][i];
					}
					orientation = Orientation.W;
				}
			} else {
				throw new IllegalArgumentException("Bad start/end pos!");
			}
		}

		public Point toPoint(final int index) {
			switch (orientation) {
				case N:
					return new Point(x1, y1 - index);
				case E:
					return new Point(x1 + index, y1);
				case S:
					return new Point(x1, y1 + index);
				case W:
					return new Point(x1 - index, y1);
				default:
					return null;
			}
		}
	}

	public static class Point {
		public final int x;
		public final int y;

		public Point(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}

	public enum Orientation {
		N, E, S, W
	}

	/**
	 * Utility classes need no constructor.
	 */
	private HeatMap() {
		//nothing
	}
}
