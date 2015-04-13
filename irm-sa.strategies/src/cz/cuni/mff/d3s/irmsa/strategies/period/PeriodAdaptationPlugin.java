package cz.cuni.mff.d3s.irmsa.strategies.period;

import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.strategies.MetaAdaptationPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.commons.EvolutionaryAdaptationManager;
import cz.cuni.mff.d3s.irmsa.strategies.commons.EvolutionaryAdaptationPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.AdapteeSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DeltaComputor;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DirectionSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.InvariantFitnessCombiner;

/**
 * Plugin for period adaptation strategy.
 */
public class PeriodAdaptationPlugin extends EvolutionaryAdaptationPlugin<PeriodAdaptationPlugin, PeriodBackup> {

	/**
	 * Only constructor.
	 * @param metaAdaptationPlugin plugin managing this plugin
	 * @param model model
	 * @param design design
	 * @param trace trace
	 */
	public PeriodAdaptationPlugin(final MetaAdaptationPlugin metaAdaptationPlugin,
			final RuntimeMetadata model, final IRM design, final TraceModel trace) {
		super(new PeriodAdaptationManagerDelegate(),
				metaAdaptationPlugin, model, design, trace);
	}

	@Override
	protected PeriodAdaptationPlugin self() {
		return this;
	}

	@Override
	protected InvariantFitnessCombiner createDefaultInvariantFitnessCombiner() {
		return new InvariantFitnessCombinerAverage();
	}

	@Override
	protected AdapteeSelector createDefaultAdapteeSelector() {
		return new AdapteeSelectorFitness();
	}

	@Override
	protected DirectionSelector createDefaultDirectionSelector() {
		return new DirectionSelectorImpl();
	}

	@Override
	protected DeltaComputor createDefaultDeltaComputor() {
		return new DeltaComputorFixed();
	}

	@Override
	protected EvolutionaryAdaptationManager createAdaptationManager() {
		return new PeriodAdaptationManager();
	}

}
