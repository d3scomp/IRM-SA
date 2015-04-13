package cz.cuni.mff.d3s.irmsa.strategies.period;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.irmsa.strategies.commons.EvolutionaryAdaptationManager;

@Component
@SystemComponent
public class PeriodAdaptationManager extends EvolutionaryAdaptationManager {

	/**
	 * Only constructor
	 */
	protected PeriodAdaptationManager() {
		super(new PeriodStateHolder());
	}
}
