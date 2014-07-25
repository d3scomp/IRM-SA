package cz.cuni.mff.d3s.deeco.demo.vehicles.simple;

import java.io.File;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Distribution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Execution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Scheduling;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkBuilder;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;

public class AcceptanceTest {
	
	static final String MODELS_BASE_PATH = "test.cz.cuni.mff.d3s.deeco.demo.vehicles.designModels.".replaceAll("[.]", File.separator);
	static final String XMIFILE_PREFIX = "vehicles_simple_";
	static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "vehicles_simple.irmdesign";	

	@Test
	public void sampleRun() throws AnnotationProcessorException, InterruptedException {
		RuntimeMetadata runtime = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE; 
		IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
		
		AnnotationProcessorExtensionPoint extension = new IrmAwareAnnotationProcessorExtension(design,trace);
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, runtime, extension);
		
		processor.process(
			new Vehicle(), new Vehicle(), new Parking(), new AdaptationManager(), // Application Components + Adaptation Manager
			UpdateParkingAvailabilityWhenCloseToPOI.class, UpdateParkingAvailabilityWhenFarFromPOI.class // Ensembles
		);

		// pass design and trace models to the AdaptationManager
		for (ComponentInstance c : runtime.getComponentInstances()) {
			if (c.getName().equals(AdaptationManager.class.getName())) {
				c.getInternalData().put(AdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(AdaptationManager.TRACE_MODEL, trace);
			}
		}
		
		RuntimeFrameworkBuilder builder = new RuntimeFrameworkBuilder(
				new RuntimeConfiguration(Scheduling.WALL_TIME,Distribution.LOCAL, Execution.SINGLE_THREADED));
		RuntimeFramework runtimeFramework = builder.build(runtime);
		
		runtimeFramework.start();
		
		// WHEN the AdaptationManager runs for the first time
		Thread.sleep(1000);
		
		// terminate execution
		runtimeFramework.stop();
	}

}
