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

package dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Computes shortest path between two vertices in a graph.
 */
public class Dijkstra {

	/**
	 * Computes paths inside the graph from source.
	 * Reset vertices between calls!
	 * @param source start node
	 */
	public static <T, U> void computePaths(final Vertex<T> source) {
		source.minDistance = 0;
		final PriorityQueue<Vertex<T>> vertexQueue = new PriorityQueue<>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex<T> u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge<T> e : u.adjacencies) {
				final Vertex<T> v = e.target;
				final double weight = e.weight;
				final double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);

					v.minDistance = distanceThroughU;
					v.previous = u;
					v.previousEdge = e;
					vertexQueue.add(v);
				}
			}
		}
	}

	/**
	 * Creates new path to target. Call computePaths first!
	 * @param target target vertex
	 * @return list of vertices to go through
	 */
	public static <T> List<Vertex<T>> getVerticesOnShortestPathTo(final Vertex<T> target) {
		final List<Vertex<T>> path = new ArrayList<>();
		for (Vertex<T> vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);

		Collections.reverse(path);
		return path;
	}

	/**
	 * Creates new path to target. Call computePaths first!
	 * @param target target vertex
	 * @return list of vertices to go through
	 */
	public static <T> List<Edge<T>> getEdgesOnShortestPathTo(final Vertex<T> target) {
		final List<Edge<T>> path = new ArrayList<>();
		for (Vertex<T> vertex = target; vertex != null && vertex.previousEdge != null; vertex = vertex.previous)
			path.add(vertex.previousEdge);

		Collections.reverse(path);
		return path;
	}
}
