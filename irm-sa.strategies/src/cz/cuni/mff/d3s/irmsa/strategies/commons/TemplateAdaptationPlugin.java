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
import cz.cuni.mff.d3s.irmsa.IRMPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.AdapteeSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DeltaComputor;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DirectionSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.InvariantFitnessCombiner;

/**
 * Template plugin for evolution adaptation strategies.
 */
public abstract class TemplateAdaptationPlugin <T extends TemplateAdaptationPlugin<T, U>, U extends Backup> implements DEECoPlugin {

	/** Plugin dependencies. */
	static private List<Class<? extends DEECoPlugin>> dependencies =
			Collections.unmodifiableList(Arrays.asList(IRMPlugin.class));

	/** Runtime model. */
	protected final RuntimeMetadata model;

	/** Design model. */
	protected final IRM design;

	/** Trace model. */
	protected final TraceModel trace;

	/** TemplateAdaptationManager delegates operations to this object. */
	protected final AdaptationManagerDelegate<U> delegate;

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

	/**
	 * Only constructor.
	 * @param model model
	 * @param design design
	 * @param trace trace
	 */
	public TemplateAdaptationPlugin(final AdaptationManagerDelegate<U> delegate,
			final RuntimeMetadata model, final IRM design, final TraceModel trace) {
		this.delegate = delegate;
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
	protected abstract TemplateAdaptationManager createAdaptationManager();

	/**
	 * Here can descendant provide additional data to adaptation manager.
	 * @param data adaptation manager's internal data
	 */
	protected void provideDataToManager(final EMap<String, Object> data) {
		//nothing
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return dependencies;
	}

	@Override
	public void init(final DEECoContainer container) {
		//relying on annotation processor from IRMPlugin
		try {
			final TemplateAdaptationManager manager = createAdaptationManager();
			container.deployComponent(manager);
			// pass necessary data to the PeriodAdaptationManager
			for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
				if (c.getName().equals(manager.getClass().getName())) {
					final EMap<String, Object> data = c.getInternalData();
					data.put(TemplateAdaptationManager.DESIGN_MODEL, design);
					data.put(TemplateAdaptationManager.TRACE_MODEL, trace);
					data.put(TemplateAdaptationManager.ADAPTATION_DELEGATE, delegate);
					data.put(TemplateAdaptationManager.INVARIANT_FITNESS_COMBINER, invariantFitnessCombiner);
					data.put(TemplateAdaptationManager.ADAPTEE_SELECTOR, adapteeSelector);
					data.put(TemplateAdaptationManager.DIRECTION_SELECTOR, directionSelector);
					data.put(TemplateAdaptationManager.DELTA_COMPUTOR, deltaComputor);
					data.put(TemplateAdaptationManager.ADAPTATION_BOUND, adaptationBound);
					provideDataToManager(data);
				}
			}
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}
}
