package period;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.IRMPlugin;

public class IRMPeriodAdaptationPlugin implements DEECoPlugin {

	/** Plugin dependencies. */
	static private List<Class<? extends DEECoPlugin>> dependencies =
//			Collections.unmodifiableList(Arrays.asList(IRMPlugin.class));
			Collections.emptyList();

	final RuntimeMetadata model;

	final IRM design;

	final TraceModel trace;

	InvariantFitnessCombiner invariantFitnessCombiner =
			new InvariantFitnessCombinerAverage();

	AdapteeSelector adapteeSelector = new AdapteeSelectorTree();

	DirectionSelector directionSelector = new DirectionSelectorImpl();

	DeltaComputor deltaComputor = new DeltaComputorFixed();

	/**
	 * Only constructor.
	 * @param model model
	 * @param design design
	 * @param trace trace
	 */
	public IRMPeriodAdaptationPlugin(RuntimeMetadata model, IRM design, TraceModel trace) {
		this.model = model;
		this.design = design;
		this.trace = trace;
	}

	/**
	 * Sets invariant fitness combiner.
	 * @param invariantFitnessCombiner new fitness combiner
	 * @return this
	 */
	public IRMPeriodAdaptationPlugin withInvariantFitnessCombiner(
			final InvariantFitnessCombiner invariantFitnessCombiner) {
		this.invariantFitnessCombiner = invariantFitnessCombiner;
		return this;
	}

	/**
	 * Sets adaptee selector.
	 * @param adapteeSelector new adaptee selector
	 * @return this
	 */
	public IRMPeriodAdaptationPlugin withAdapteeSelector(
			final AdapteeSelector adapteeSelector) {
		this.adapteeSelector = adapteeSelector;
		return this;
	}

	/**
	 * Sets direction selector.
	 * @param directionSelector new direction selector
	 * @return this
	 */
	public IRMPeriodAdaptationPlugin withDirectionSelector(
			final DirectionSelector directionSelector) {
		this.directionSelector = directionSelector;
		return this;
	}

	/**
	 * Sets delta computor.
	 * @param deltaComputor new delta computor
	 * @return this
	 */
	public IRMPeriodAdaptationPlugin withDeltaComputor(
			final DeltaComputor deltaComputor) {
		this.deltaComputor = deltaComputor;
		return this;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return dependencies;
	}

	@Override
	public void init(DEECoContainer container) {
		final AnnotationProcessorExtensionPoint irmAwareAnnotationProcessorExtension =
				new IrmAwareAnnotationProcessorExtension(design, trace);
		container.getProcessor().addExtension(irmAwareAnnotationProcessorExtension);

		try {
			container.deployComponent(new PeriodAdaptationManager());
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy PeriodAdaptationManager", e);
		}

		// pass necessary data to the AdaptationManager
		for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
			if (c.getName().equals(PeriodAdaptationManager.class.getName())) {
				final EMap<String, Object> data = c.getInternalData();
				data.put(PeriodAdaptationManager.DESIGN_MODEL, design);
				data.put(PeriodAdaptationManager.TRACE_MODEL, trace);
				data.put(PeriodAdaptationManager.INVARIANT_FITNESS_COMBINER, invariantFitnessCombiner);
				data.put(PeriodAdaptationManager.ADAPTEE_SELECTOR, adapteeSelector);
				data.put(PeriodAdaptationManager.DIRECTION_SELECTOR, directionSelector);
				data.put(PeriodAdaptationManager.DELTA_COMPUTOR, deltaComputor);
			}
		}
	}
}
