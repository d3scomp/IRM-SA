package dijkstra;

/**
 * Class representing edge in graph.
 */
public class Edge<T> {

	/** Destination.*/
	public final Vertex<T> target;

	/** Corridor length. */
	public final double weight;

	/**
	 * Only constructor
	 * @param target destination
	 * @param weight corridor length
	 * @param value encapsulated value
	 */
	public Edge(final Vertex<T> target, final double weight) {
		this.target = target;
		this.weight = weight;
	}
}
