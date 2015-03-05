package cz.cuni.mff.d3s.irmsa;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.demo.vehicles.simple.AdaptationManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;

public class IRMPlugin implements DEECoPlugin {

	List<Class<? extends DEECoPlugin>> dependencies = new ArrayList<>();
	
	TraceModel trace;
	IRM design;
	
	public IRMPlugin(TraceModel trace, IRM design) {
		this.trace = trace;
		this.design = design;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// This plugin has no dependencies other than jDEECo core
		return dependencies;
	}
	
	@Override
	public void init(DEECoContainer container) {
		AnnotationProcessorExtensionPoint irmAwareAnnotationProcessorExtension = new IrmAwareAnnotationProcessorExtension(design,trace);
		container.getProcessor().addExtension(irmAwareAnnotationProcessorExtension);
		
		try {
			container.deployComponent(new AdaptationManager(design, trace));
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}

		// pass design and trace models to the AdaptationManager
		for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
			if (c.getName().equals(AdaptationManager.class.getName())) {
				c.getInternalData().put(AdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(AdaptationManager.TRACE_MODEL, trace);
			}
		}

		
	}

}