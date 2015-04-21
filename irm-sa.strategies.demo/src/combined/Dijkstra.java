/* The authors of this work have released all rights to it and placed it
in the public domain under the Creative Commons CC0 1.0 waiver
(http://creativecommons.org/publicdomain/zero/1.0/).

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Retrieved from: http://en.literateprograms.org/Dijkstra's_algorithm_(Java)?oldid=15444
 */

package combined;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class representing corridor end or leader position.
 */
class Vertex<T, U> implements Comparable<Vertex<T, U>> {

	/** Encapsulated value. */
	public final T value;

	public final List<Edge<T, U>> adjacencies = new ArrayList<>();

	/** Seems it needs to be reset after each run! */
	public double minDistance = Double.POSITIVE_INFINITY;

	/** Seems it needs to be reset after each run! */
	public Vertex<T, U> previous;

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
	public int compareTo(final Vertex<T, U> other) {
		return Double.compare(minDistance, other.minDistance);
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Vertex)) {
			return false;
		}
		final Vertex<?, ?> vertex = (Vertex<?, ?>) other;
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
	}
}

/**
 * Class representing corridors.
 */
class Edge<T, U> {

	/** Destination.*/
	public final Vertex<T, U> target;

	/** Corridor length. */
	public final double weight;

	/** Encapsulated value. */
	public final U value;

	/**
	 * Only constructor
	 * @param target destination
	 * @param weight corridor length
	 * @param value encapsulated value
	 */
	public Edge(final Vertex<T, U> target, final double weight, final U value) {
		this.target = target;
		this.weight = weight;
		this.value = value;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Edge)) {
			return false;
		}
		final Edge<?, ?> edge = (Edge<?, ?>) other;
		if (weight != edge.weight) {
			return false;
		}
		if (target == null) {
			return edge.target == null;
		}
		return target.equals(edge.target);
	}
}

/**
 * Computes shortest path between two positions in heatmap.
 */
public class Dijkstra {

	/**
	 * Computes paths inside the graph from source.
	 * @param source start node
	 */
	public static <T, U> void computePaths(Vertex<T, U> source) {
		source.minDistance = 0;
		PriorityQueue<Vertex<T, U>> vertexQueue = new PriorityQueue<>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex<T, U> u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge<T, U> e : u.adjacencies) {
				Vertex<T, U> v = e.target;
				double weight = e.weight;
				double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);

					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	public static <T, U> List<Vertex<T, U>> getShortestPathTo(Vertex<T, U> target) {
		List<Vertex<T, U>> path = new ArrayList<>();
		for (Vertex<T, U> vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);

		Collections.reverse(path);
		return path;
	}
}
