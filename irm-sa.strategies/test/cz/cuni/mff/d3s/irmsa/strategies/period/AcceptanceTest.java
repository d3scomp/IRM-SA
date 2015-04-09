package cz.cuni.mff.d3s.irmsa.strategies.period;

import java.net.URL;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
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

	@Test
	public void sampleRun() throws InstantiationException, IllegalAccessException, DEECoException, AnnotationProcessorException {
		/* create IRM plugin */
		final TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		@SuppressWarnings("unused")
		final IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
		final URL modelURL = getClass().getResource("period_simple.irmdesign");
		final IRM design = (IRM) EMFHelper.loadModelFromXMI(modelURL.toString());

		final SimulationTimer simulationTimer = new DiscreteEventTimer();
		/* create main application container */
		final DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
		simulation.addPlugin(new IRMPlugin(trace, design).withLog(false));

		final RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		simulation.addPlugin(new PeriodAdaptationPlugin(model, design, trace));

		/* deploy components and ensembles */
		final DEECoNode deecoNode = simulation.createNode(1);
		deecoNode.deployComponent(new TestComponent());

		simulation.start(50_000);
	}
}
