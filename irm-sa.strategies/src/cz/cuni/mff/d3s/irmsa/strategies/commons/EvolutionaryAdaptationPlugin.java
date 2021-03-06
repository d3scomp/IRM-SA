package cz.cuni.mff.d3s.irmsa.strategies.commons;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.strategies.AdaptationManager;
import cz.cuni.mff.d3s.irmsa.strategies.MetaAdaptationPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.AdapteeSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DeltaComputor;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DirectionSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.InvariantFitnessCombiner;

/**
 * Template plugin for evolution adaptation strategies.
 */
public abstract class EvolutionaryAdaptationPlugin <T extends EvolutionaryAdaptationPlugin<T, U>, U extends Backup> implements DEECoPlugin {

	/** Plugin dependencies. */
	static private List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Collections.unmodifiableList(Arrays.asList(MetaAdaptationPlugin.class));

	/** Runtime model. */
	protected final RuntimeMetadata model;

	/** Design model. */
	protected final IRM design;

	/** Trace model. */
	protected final TraceModel trace;

	/** EvolutionaryAdaptationManager delegates operations to this object. */
	protected final EvolutionaryAdaptationManagerDelegate<U> delegate;

	/** MetaAdaptationPlugin managing this plugin. */
	protected final MetaAdaptationPlugin metaAdaptationPlugin;

	/** Combines independent fitnesses into overall system fitness. */
	protected InvariantFitnessCombiner invariantFitnessCombiner =
			createDefaultInvariantFitnessCombiner();

	/** Selects processes to adapt. */
	protected AdapteeSelector adapteeSelector =
			createDefaultAdapteeSelector();

	/** Selects direction of period adaptation. */
	protected DirectionSelector directionSelector =
			createDefaultDirectionSelector();

	/** Calculates period delta (difference between old and new period) for adaptees. */
	protected DeltaComputor deltaComputor =
			createDefaultDeltaComputor();

	/** No adaptation takes place if overall fitness is at least this bound. */
	protected double adaptationBound = 0.5;

	/** Maximal number of tries to adapt. -1 for unbounded. */
	protected int maximumTries = EvolutionaryAdaptationManager.TRIES_UNBOUDED;

	/**
	 * Only constructor.
	 * @param delegate adaptation manager delegate
	 * @param metaAdaptationPlugin plugin managing this plugin
	 * @param model model
	 * @param design design
	 * @param trace trace
	 */
	public EvolutionaryAdaptationPlugin(final EvolutionaryAdaptationManagerDelegate<U> delegate,
			final MetaAdaptationPlugin metaAdaptationPlugin,
			final RuntimeMetadata model, final IRM design, final TraceModel trace) {
		this.delegate = delegate;
		this.metaAdaptationPlugin = metaAdaptationPlugin;
		this.model = model;
		this.design = design;
		this.trace = trace;
	}

	protected abstract T self();

	/**
	 * Sets invariant fitness combiner.
	 * @param invariantFitnessCombiner new fitness combiner
	 * @return this
	 */
	public T withInvariantFitnessCombiner(
			final InvariantFitnessCombiner invariantFitnessCombiner) {
		this.invariantFitnessCombiner = invariantFitnessCombiner;
		return self();
	}

	/**
	 * Sets adaptee selector.
	 * @param adapteeSelector new adaptee selector
	 * @return this
	 */
	public T withAdapteeSelector(final AdapteeSelector adapteeSelector) {
		this.adapteeSelector = adapteeSelector;
		return self();
	}

	/**
	 * Sets direction selector.
	 * @param directionSelector new direction selector
	 * @return this
	 */
	public T withDirectionSelector(final DirectionSelector directionSelector) {
		this.directionSelector = directionSelector;
		return self();
	}

	/**
	 * Sets delta computor.
	 * @param deltaComputor new delta computor
	 * @return this
	 */
	public T withDeltaComputor(final DeltaComputor deltaComputor) {
		this.deltaComputor = deltaComputor;
		return self();
	}

	/**
	 * Sets adaptation bound.
	 * @param adaptationBound new adaptationBound
	 * @return this
	 */
	public T withAdaptationBound(final double adaptationBound) {
		this.adaptationBound = adaptationBound;
		return self();
	}

	/**
	 * Sets maximum tries. -1 for unbounded.
	 * @param maximumTries new maximum tries
	 * @return this
	 */
	public T withMaximumTries(final int maximumTries) {
		this.maximumTries = maximumTries;
		return self();
	}

	/**
	 * Creates default InvariantFitnessCombiner. Called in constructor!
	 * @return default InvariantFitnessCombiner
	 */
	protected abstract InvariantFitnessCombiner createDefaultInvariantFitnessCombiner();

	/**
	 * Creates default AdapteeSelector. Called in constructor!
	 * @return default AdapteeSelector
	 */
	protected abstract AdapteeSelector createDefaultAdapteeSelector();

	/**
	 * Creates default DirectionSelector. Called in constructor!
	 * @return default DirectionSelector
	 */
	protected abstract DirectionSelector createDefaultDirectionSelector();

	/**
	 * Creates default DeltaComputor. Called in constructor!
	 * @return default DeltaComputor
	 */
	protected abstract DeltaComputor createDefaultDeltaComputor();

	/**
	 * Creates new AdaptationManager. Unique class for each Plugin!
	 * @return new AdaptationManager
	 */
	protected abstract EvolutionaryAdaptationManager createAdaptationManager();

	/**
	 * Here can descendant provide additional data to adaptation manager.
	 * @param data adaptation manager's internal data
	 */
	protected void provideDataToManager(final EMap<String, Object> data) {
		//nothing
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	@Override
	public void init(final DEECoContainer container) {
		//relying on annotation processor from IRMPlugin
		try {
			final EvolutionaryAdaptationManager manager = createAdaptationManager();
			container.deployComponent(manager);
			// pass necessary data to the EvolutionaryAdaptationManager
			for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
				if (c.getName().equals(manager.getClass().getName())) {
					final EMap<String, Object> data = c.getInternalData();
					data.put(EvolutionaryAdaptationManager.DESIGN_MODEL, design);
					data.put(EvolutionaryAdaptationManager.TRACE_MODEL, trace);
					data.put(EvolutionaryAdaptationManager.ADAPTATION_DELEGATE, delegate);
					data.put(EvolutionaryAdaptationManager.INVARIANT_FITNESS_COMBINER, invariantFitnessCombiner);
					data.put(EvolutionaryAdaptationManager.ADAPTEE_SELECTOR, adapteeSelector);
					data.put(EvolutionaryAdaptationManager.DIRECTION_SELECTOR, directionSelector);
					data.put(EvolutionaryAdaptationManager.DELTA_COMPUTOR, deltaComputor);
					data.put(EvolutionaryAdaptationManager.ADAPTATION_BOUND, adaptationBound);
					provideDataToManager(data);
					//
					metaAdaptationPlugin.registerManager(new AdaptationManager() {

						@Override
						public void stop() {
							data.put(EvolutionaryAdaptationManager.RUN_FLAG, false);
						}

						@Override
						public void run() {
							data.put(EvolutionaryAdaptationManager.RUN_FLAG, true);
							data.put(EvolutionaryAdaptationManager.DONE_FLAG, false);
						}

						@Override
						public boolean isDone() {
							final Boolean result = (Boolean) data.get(EvolutionaryAdaptationManager.DONE_FLAG);
							return result == null || result;
						}
					});
				}
			}
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy EvolutionaryAdaptationManager", e);
		}
	}
}
