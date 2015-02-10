/*******************************************************************************
 * Copyright 2014 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package firefighters;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.network.DefaultKnowledgeDataManager;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.simulation.DelayedKnowledgeDataHandler;
import cz.cuni.mff.d3s.deeco.simulation.DirectSimulationHost;
import cz.cuni.mff.d3s.deeco.simulation.JDEECoSimulation;
import cz.cuni.mff.d3s.deeco.simulation.SimulationRuntimeBuilder;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;

public class CentralizedRun {

	private static final String MODELS_BASE_PATH = "designModels/";
	private static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters.irmdesign";
	private static final long SIMULATION_START = 0; // in milliseconds
	private static final long SIMULATION_END = 15000; // in milliseconds
	private static final long NETWORK_DELAY = 100; // in milliseconds (this value is actually not used in the centralized case)
	
	private static IRM design;
	private static JDEECoSimulation simulation;
	private static SimulationRuntimeBuilder builder;

	public static void main(String args[]) throws AnnotationProcessorException, InterruptedException {
		Log.i("Preparing simulation");

		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE; 
		design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);

		DelayedKnowledgeDataHandler networkKnowledgeDataHandler = new DelayedKnowledgeDataHandler(NETWORK_DELAY);
		simulation = new JDEECoSimulation(SIMULATION_START, SIMULATION_END, networkKnowledgeDataHandler);
		
		builder = new SimulationRuntimeBuilder();
		
		List<TimerTaskListener> listeners = new LinkedList<>();
		listeners.add(networkKnowledgeDataHandler);

		createAndDeployComponents(listeners);
		
		Log.i("Simulation Starts");
		simulation.run();
		Log.i("Simulation Finished");
		
		System.out.println("The reaction time is: " + (Results.getInstance().getReactionTime() - InDangerTimeHelper.getInstance().getInDangerTime()));
	}
	
	private static void createAndDeployComponents(Collection<? extends TimerTaskListener> simulationListeners) throws AnnotationProcessorException {
		RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		AnnotationProcessorExtensionPoint extension = new IrmAwareAnnotationProcessorExtension(design,trace);
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, new CloningKnowledgeManagerFactory(), extension);
		
		GroupLeader leader = new GroupLeader("L1");
		GroupMember member1 = new GroupMember("M1", "L1");
		GroupMember member2 = new GroupMember("M2", "L1");
		
		processor.process(
				leader,
				member1,
				member2,
				new AdaptationManager(),
				SensorDataUpdate.class,
				GMsInDangerUpdate.class 
			);
		
		// pass design and trace models to the AdaptationManager
		for (ComponentInstance c : model.getComponentInstances()) {
			if (c.getName().equals(AdaptationManager.class.getName())) {
				c.getInternalData().put(AdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(AdaptationManager.TRACE_MODEL, trace);
			}
		}
		
		DirectSimulationHost host = simulation.getHost("host");
		RuntimeFramework runtime = builder.build(host, simulation, simulationListeners, model, new DefaultKnowledgeDataManager(model.getEnsembleDefinitions(), null), new CloningKnowledgeManagerFactory(),null,
				/* FIXME the following argument throws an exception in task invocation */ null);
		
		runtime.start();	
	}
}
