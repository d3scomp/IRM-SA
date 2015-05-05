package dijkstra;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing vertex in a graph.
 */
public class Vertex<T> implements Comparable<Vertex<T>> {

	/** Encapsulated value. */
	public final T value;

	/** Edges from this vertex. */
	public final List<Edge<T>> adjacencies = new ArrayList<>();

	/** Seems it needs to be reset after each run! */
	public double minDistance = Double.POSITIVE_INFINITY;

	/** Seems it needs to be reset after each run! */
	public Vertex<T> previous;

	/** Seems it needs to be reset after each run! */
	public Edge<T> previousEdge;

	/**
	 * Only constructor.
	 * @param value encapsulated value
	 */
	public Vertex(final T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}

	@Override
	public int compareTo(final Vertex<T> other) {
		return Double.compare(minDistance, other.minDistance);
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Vertex)) {
			return false;
		}
		final Vertex<?> vertex = (Vertex<?>) other;
		if (value == null) {
			return vertex.value == null;
		}
		return value.equals(vertex.value);
	}

	/**
	 * Resets to original state.
	 */
	public void reset() {
		minDistance = Double.POSITIVE_INFINITY;
		previous = null;
		previousEdge = null;
	}
}
