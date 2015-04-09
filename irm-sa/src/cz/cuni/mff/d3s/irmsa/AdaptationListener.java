package cz.cuni.mff.d3s.irmsa;

/**
 * Interface for listening to adaptation result.
 */
@FunctionalInterface
public interface AdaptationListener {

	/**
	 * This adaptation cycle ended with this number of solutions from total instances.
	 * @param solutions number of found solutions
	 * @param total total number of instances to solve
	 */
	void adaptationResult(int solutions, int total);
}
