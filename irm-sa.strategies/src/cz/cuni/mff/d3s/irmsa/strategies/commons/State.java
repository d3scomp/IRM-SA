package cz.cuni.mff.d3s.irmsa.strategies.commons;

/**
 * Simple enum with possible states of the EvolutionaryAdaptationManager's adapt method.
 */
public enum State {

	/** No adaptation in progress, start from beginning. */
	STARTED,

	/** Adaptation in progress, observe time has ended. */
	OBSERVED
}
