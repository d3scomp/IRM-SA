package combined;

import java.util.ArrayList;
import java.util.List;

import dijkstra.Edge;
import dijkstra.Vertex;

/**
 * Environment heat map holder.
 */
public class HeatMap {

	/** Environment heat map. */
	static final int[][] HEAT_MAP = new int[][] {
		{    7,  10,  14,  15,  17,  18,  19,  19,  19,  19,  20,  21,  22,  23,  22,  21,  18,  15,  11,   7 },
		{   10,  15,  20,  22,  24,  25,  26,  27,  28,  30,  33,  37,  41,  43,  43,  40,  35,  29,  21,  13 },
		{   14,  20,  26,  29,  31,  33,  35,  37,  41,  45,  51,  57,  62,  64,  64,  61,  55,  47,  34,  23 },
		{   15,  22,  29,  32,  35,  37,  40,  44,  51,  58,  66,  74,  79,  81,  80,  77,  71,  63,  47,  32 },
		{   17,  24,  31,  35,  38,  41,  45,  52,  61,  71,  82,  91,  96,  98,  97,  93,  87,  78,  59,  41 },
		{   18,  25,  33,  37,  41,  44,  51,  61,  72,  85,  97, 105, 109, 109, 108, 105,  99,  91,  70,  50 },
		{   19,  26,  35,  39,  42,  47,  57,  69,  83,  96, 107, 112, 113, 112, 110, 107, 103,  98,  76,  56 },
		{   19,  27,  36,  40,  44,  49,  62,  76,  89, 102, 112, 115, 114, 113, 111, 108, 105, 101,  79,  58 },
		{   19,  28,  36,  41,  44,  51,  65,  80,  93, 106, 115, 115, 115, 114, 112, 109, 105, 101,  79,  58 },
		{   19,  28,  37,  41,  44,  52,  66,  81,  94, 107, 116, 116, 115, 114, 112, 109, 105, 101,  79,  58 },
		{   19,  28,  37,  41,  44,  51,  65,  80,  93, 106, 115, 116, 115, 114, 112, 109, 105, 101,  79,  58 },
		{   19,  28,  36,  41,  44,  50,  62,  76,  90, 103, 113, 115, 115, 114, 112, 109, 105, 101,  79,  58 },
		{   19,  27,  36,  40,  44,  48,  58,  70,  84,  97, 108, 113, 114, 113, 111, 108, 105,  99,  77,  56 },
		{   19,  26,  35,  39,  42,  46,  53,  63,  74,  87,  99, 107, 111, 111, 110, 107, 101,  93,  71,  51 },
		{   18,  25,  33,  37,  41,  43,  48,  55,  64,  74,  85,  94,  99, 100,  99,  96,  89,  80,  61,  42 },
		{   17,  24,  31,  35,  38,  41,  43,  48,  54,  61,  70,  78,  83,  85,  84,  80,  74,  65,  49,  33 },
		{   15,  22,  29,  32,  35,  37,  39,  41,  45,  50,  55,  61,  66,  68,  68,  64,  58,  50,  36,  24 },
		{   14,  20,  26,  29,  31,  33,  35,  36,  37,  39,  42,  46,  49,  51,  51,  48,  42,  35,  25,  17 },
		{   10,  15,  20,  22,  24,  25,  26,  27,  28,  28,  28,  29,  30,  30,  30,  28,  25,  21,  15,  10 },
		{    7,  10,  14,  15,  17,  18,  19,  19,  19,  19,  19,  19,  19,  19,  18,  17,  15,  14,  10,   7 }
	};

	/** Junctions in the map. */
	static final Junction[] JUNCTIONS = new Junction[] {
		/*  0 */ new Junction(0, 0),
		/*  1 */ new Junction(4, 0),
		/*  2 */ new Junction(4, 5),
		/*  3 */ new Junction(1, 5),
		/*  4 */ new Junction(1, 11),
		/*  5 */ new Junction(8, 11),
		/*  6 */ new Junction(8, 5),
		/*  7 */ new Junction(1, 17),
		/*  8 */ new Junction(5, 17),
		/*  9 */ new Junction(5, 14),
		/* 10 */ new Junction(8, 14),
		/* 11 */ new Junction(8, 17),
		/* 12 */ new Junction(13, 17),
		/* 13 */ new Junction(13, 19),
		/* 14 */ new Junction(17, 17),
		/* 15 */ new Junction(17, 12),
		/* 16 */ new Junction(10, 12),
		/* 17 */ new Junction(10, 5),
		/* 18 */ new Junction(12, 5),
		/* 19 */ new Junction(12, 3),
		/* 20 */ new Junction(12, 7),
		/* 21 */ new Junction(14, 5),
		/* 22 */ new Junction(14, 3),
		/* 23 */ new Junction(14, 7),
		/* 24 */ new Junction(16, 5),
		/* 25 */ new Junction(16, 3),
		/* 26 */ new Junction(16, 7),
		/* 27 */ new Junction(18, 5),
		/* 28 */ new Junction(18, 9),
		/* 29 */ new Junction(12, 9),
		/* 30 */ new Junction(8, 1)
	};

	/** List of corridors in the map. */
	static final List<Corridor> CORRIDORS = new ArrayList<>();

	/**
	 * Auxiliary field for determining original segment index.
	 * For debug purposes only.
	 */
	static private int corrIndex = 1;

	static {
		/*  1 */ joinJunctions(0, 1);
		/*  2 */ joinJunctions(1, 2);
		/*  3 */ joinJunctions(2, 3);
		/*  4 */ joinJunctions(3, 4);
		/*  5 */ joinJunctions(4, 5);
		/*  6 */ joinJunctions(2, 6);
		/*  7 */ joinJunctions(5, 6);
		/*  8 */ joinJunctions(4, 7);
		/*  9 */ joinJunctions(7, 8);
		/* 10 */ joinJunctions(8, 9);
		/* 11 */ joinJunctions(9, 10);
		/* 12 */ joinJunctions(10, 11);
		/* 13 */ joinJunctions(11, 12);
		/* 14 */ joinJunctions(12, 13);
		/* 15 */ joinJunctions(12, 14);
		/* 16 */ joinJunctions(14, 15);
		/* 17 */ joinJunctions(15, 16);
		/* 18 */ joinJunctions(16, 17);
		/* 19 */ joinJunctions(6, 17);
		/* 20 */ joinJunctions(6, 30);
		/* 21 */ joinJunctions(17, 18);
		/* 22 */ joinJunctions(18, 19);
		/* 23 */ joinJunctions(18, 20);
		/* 24 */ joinJunctions(18, 21);
		/* 25 */ joinJunctions(21, 22);
		/* 26 */ joinJunctions(21, 23);
		/* 27 */ joinJunctions(21, 24);
		/* 28 */ joinJunctions(24, 25);
		/* 29 */ joinJunctions(24, 26);
		/* 30 */ joinJunctions(24, 27);
		/* 31 */ joinJunctions(27, 28);
		/* 32 */ joinJunctions(28, 29);
	}

	/**
	 * Joins junctions with given indices with new Corridors.
	 * Also adds new Corridors to CORRIDORS.
	 * @param index1 junction 1 index
	 * @param index2 junction 2 index
	 */
	static private void joinJunctions(final int index1, final int index2) {
		final Junction junction1 = JUNCTIONS[index1];
		final Junction junction2 = JUNCTIONS[index2];
		final double weight = PositionMetric.distance(junction1.value, junction2.value);
		final int size = CORRIDORS.size();
		final Corridor corridor1 = new Corridor(junction1, junction2, weight, size, size + 1);
		corridor1.origIndex = corrIndex;
		CORRIDORS.add(corridor1);
		final Corridor corridor2 = new Corridor(junction2, junction1, weight, size + 1, size);
		CORRIDORS.add(corridor2);
		corridor2.origIndex = corrIndex;
		++corrIndex;
	}

	/**
	 * Returns temperature on given location.
	 * @param location location to compute temperature on
	 * @return temperature on given location
	 */
	public static double temperature(final Location location) {
		final int index1 = (int) location.index;
		double f = location.index - index1;
		final int index2 = (int) Math.ceil(location.index);
		final int temperature1 = CORRIDORS.get(location.corridor).temps[index1];
		final int temperature2 =  CORRIDORS.get(location.corridor).temps[index2];
		if (temperature1 < temperature2) {
			return temperature1 + f * (temperature2 - temperature1);
		} else {
			return temperature2 + (1 - f) * (temperature1 - temperature2);
		}
	}

	/**
	 * Class representing junction of corridors or end of corridor.
	 */
	static class Junction extends Vertex<Position> {

		/**
		 * Only constructor.
		 * @param x x-coordinate
		 * @param y y-coordinate
		 */
		public Junction(final double x, final double y) {
			super(new Position(x, y));
		}
	}

	/**
	 * Class representing oriented corridor between junctions.
	 */
	static class Corridor extends Edge<Position> {

		/** Origin. */
		final Vertex<Position> source;

		/** Corridor index in CORRIDORS. */
		final int index;

		/** Opposite corridor. */
		final int opposite;

		/** Temperatures on corridors squares. */
		public final int[] temps;

		/** Corridor orientation. */
		public final Orientation orientation;

		/** Original segment index. For debug purposes only. */
		public int origIndex;

		/**
		 * Only constructor.
		 * @param source corridor origin
		 * @param target corridor target
		 * @param weight corridor length
		 * @param index corridor index in CORRIDORS
		 * @param opposite opposite corridor index
		 */
		public Corridor(final Vertex<Position> source, final Vertex<Position> target,
				final double weight, final int index, final int opposite) {
			super(target, weight);
			this.source = source;
			this.index = index;
			this.opposite = opposite;
			//
			final int x1 = (int) Math.round(source.value.x);
			final int y1 = (int) Math.round(source.value.y);
			final int x2 = (int) Math.round(target.value.x);
			final int y2 = (int) Math.round(target.value.y);
			assert x1 == x2 || y1 == y2;
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
			//
			source.adjacencies.add(this);
		}

		/**
		 * Converts index in corridor to Position.
		 * @param index index in corridor
		 * @return new Position
		 */
		public Position toPosition(final double index) {
			double x = Environment.CORRIDOR_SIZE * source.value.x;
			double y = Environment.CORRIDOR_SIZE * source.value.y;
			double shift = Environment.CORRIDOR_SIZE * index;
			switch (orientation) {
				case N:
					return new Position(x, y - shift);
				case E:
					return new Position(x + shift, y);
				case S:
					return new Position(x, y + shift);
				case W:
					return new Position(x - shift, y);
				default:
					return null;
			}
		}
	}

	/**
	 * Corridors can have these orientations.
	 */
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
