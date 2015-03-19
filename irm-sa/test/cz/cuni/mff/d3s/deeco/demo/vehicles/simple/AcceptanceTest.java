package cz.cuni.mff.d3s.deeco.demo.vehicles.simple;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;
import cz.cuni.mff.d3s.irmsa.IRMPlugin;

public class AcceptanceTest {
	
	static public final String MODELS_BASE_PATH = "test.cz.cuni.mff.d3s.deeco.demo.vehicles.designModels.".replaceAll("[.]", "/");//File.separator);
	static public final String XMIFILE_PREFIX = "vehicles_simple_";
	static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "vehicles_simple.irmdesign";	

	@Test
	public void sampleRun() throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
		
		/* create IRM plugin */
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE; 
		IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
		
		SimulationTimer simulationTimer = new DiscreteEventTimer(); 
		/* create main application container */
		DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
		simulation.addPlugin(new IRMPlugin(trace, design));
		/* deploy components and ensembles */
		DEECoNode deecoNode = simulation.createNode(1);
		
		deecoNode.deployComponent(new Vehicle());
		deecoNode.deployComponent(new Vehicle());
		deecoNode.deployComponent(new Parking());
		deecoNode.deployEnsemble(UpdateParkingAvailabilityWhenCloseToPOI.class);
		deecoNode.deployEnsemble(UpdateParkingAvailabilityWhenFarFromPOI.class);
		
		simulation.start(2000);
		
	}

}
