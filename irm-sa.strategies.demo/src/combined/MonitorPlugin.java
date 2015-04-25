package combined;

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
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.strategies.MetaAdaptationPlugin;

/**
 * Plugin deploying Monitor component.
 */
public class MonitorPlugin implements DEECoPlugin {

	/** Plugin dependencies. */
	static private List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Collections.unmodifiableList(Arrays.asList(MetaAdaptationPlugin.class));

	/** Runtime model. */
	protected final RuntimeMetadata model;

	/** Design model. */
	protected final IRM design;

	/** Trace model. */
	protected final TraceModel trace;

	Monitor monitor;

	/**
	 * Only constructor.
	 * @param model model
	 * @param design design
	 * @param trace trace
	 */
	public MonitorPlugin(final RuntimeMetadata model, final IRM design, final TraceModel trace) {
		this.model = model;
		this.design = design;
		this.trace = trace;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	@Override
	public void init(final DEECoContainer container) {
		try {
			monitor = new Monitor();
			container.deployComponent(monitor);
			container.deployEnsemble(MonitorEnsemble.class);
			// pass necessary data to the EvolutionaryAdaptationManager
			for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
				if (c.getName().equals(monitor.getClass().getName())) {
					final EMap<String, Object> data = c.getInternalData();
					data.put(Monitor.DESIGN_MODEL, design);
					data.put(Monitor.TRACE_MODEL, trace);
				}
			}
		} catch (AnnotationProcessorException | DuplicateEnsembleDefinitionException e) {
			Log.e("Error while trying to deploy Monitor", e);
		}
	}

	public void finit(){
		monitor.writer.close();
	}
}
