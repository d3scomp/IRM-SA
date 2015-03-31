package cz.cuni.mff.d3s.irmsa.strategies.commons.variations;

import java.util.Set;

import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

/**
 * Simple interface to listen to result of the adaptation.
 */
public interface AdaptationResultListener {

	/**
	 * Informs the listener about results of the adaptation.
	 * @param improvement overall system fitness improvement
	 * @param infos system state after adaptation
	 */
	default void adaptationImprovement(final double improvement,
			final Set<InvariantInfo<?>> infos) {
		//nothing
	}
}
