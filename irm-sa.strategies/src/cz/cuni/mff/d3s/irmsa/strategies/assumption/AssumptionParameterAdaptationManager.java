package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.irmsa.strategies.commons.EvolutionaryAdaptationManager;

@Component
@SystemComponent
public class AssumptionParameterAdaptationManager extends EvolutionaryAdaptationManager {

	/** Observe time stored in internal data under this key. */
	static final String OBSERVE_TIME = "observeTime";

	/**
	 * Only constructor.
	 * @param maximumTries maximal number of tries for adaptation, -1 for unbounded
	 */
	protected AssumptionParameterAdaptationManager(final int maximumTries) {
		super(new AssumptionParameterStateHolder(), maximumTries);
	}
}
