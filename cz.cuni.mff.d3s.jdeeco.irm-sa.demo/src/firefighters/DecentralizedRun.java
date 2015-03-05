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

import cz.cuni.mff.d3s.deeco.DeecoProperties;
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
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.simulation.DelayedKnowledgeDataHandler;
import cz.cuni.mff.d3s.deeco.simulation.DirectSimulationHost;
import cz.cuni.mff.d3s.deeco.simulation.JDEECoSimulation;
import cz.cuni.mff.d3s.deeco.simulation.SimulationRuntimeBuilder;
import cz.cuni.mff.d3s.deeco.simulation.TimerTaskListener;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;

/**
 * Experiment run for the IRM-JSS paper evaluation.
 * TODO make it work with jDEECo 3.0 
 * 
 * @author Ilias
 *
 */
public class DecentralizedRun {

	private static final String MODELS_BASE_PATH = "designModels/";
	private static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH
			+ "firefighters.irmdesign";
	private static final long SIMULATION_START = 0; // in milliseconds
	private static final long SIMULATION_END = 15000; // in milliseconds

	private static IRM design;
	private static JDEECoSimulation simulation;
	private static SimulationRuntimeBuilder builder;

	@SuppressWarnings("unused")
	public static void main(String args[]) throws AnnotationProcessorException,
			InterruptedException, DuplicateEnsembleDefinitionException {
		System.setProperty(DeecoProperties.PUBLISHING_PERIOD, new Integer(Settings.BROADCAST_PERIOD).toString());
		int networkDelay;
		int numberOfIterations = 5;
		List<String> afterTimes = new LinkedList<>();
		boolean inaccuarcyEnabled;
		afterTimes.add("----------Inaccuracy disabled-------------");
		afterTimes.add("\n");
		for (int i = 0; i <= numberOfIterations; i++) {
			InDangerTimeHelper.getInstance().reset();
			Results.getInstance().reset();
			Log.i("Preparing simulation");
			networkDelay = i * Settings.NETWORK_DELAY;
			IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
			design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
			DelayedKnowledgeDataHandler networkKnowledgeDataHandler = new CustomDelayedNetworkKnowledgeDataHandler(networkDelay);

			simulation = new JDEECoSimulation(SIMULATION_START, SIMULATION_END,
					networkKnowledgeDataHandler);

			builder = new SimulationRuntimeBuilder();

			List<TimerTaskListener> listeners = new LinkedList<>();
			listeners.add(networkKnowledgeDataHandler);

			createAndDeployGroupLeader("L1", listeners);
			createAndDeployGroupMember("M1", "L1", listeners);
			createAndDeployGroupMember("M2", "L1", listeners);

			// Here we enable the inaccuracy checking - i.e. the second case.
			// The parameter sets the threashold (maximum) inaccuracy (in
			// milliseconds). Please comment the following line if you would
			// like to disable the
			// inaccuracy checking and have just the first (simple) case.
			// NearbyGMInDangerInaccuaracy.enableInaccuarcyChecking(400);

			Log.i("Simulation Starts");
			simulation.run();
			Log.i("Simulation Finished");
			long srTime = Results.getInstance().getReactionTime();
			long idTime = InDangerTimeHelper.getInstance().getInDangerTime();
			StringBuilder resultString = new StringBuilder(
					"no inaccuracy, network delay: " + networkDelay);
			resultString.append(" - " + (srTime - idTime));
			afterTimes.add(resultString.toString());
		}
		afterTimes.add("\n");
		afterTimes.add("----------Inaccuracy enabled-------------");
		afterTimes.add("\n");
		long inaccuracy;
		for (int j = 0; j < 3; j++) {
			inaccuracy = Settings.BASE_INACCURACY + j*Settings.INACCURACY_INTERVAL;
			afterTimes.add("----------Inaccuracy "+inaccuracy+"-------------");
			for (int i = 0; i <= numberOfIterations; i++) {
				InDangerTimeHelper.getInstance().reset();
				Results.getInstance().reset();
				Log.i("Preparing simulation");
				networkDelay = i * Settings.NETWORK_DELAY;
				IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
				design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);

				// Here we set the arbitrary value for the network delay.
				DelayedKnowledgeDataHandler networkKnowledgeDataHandler = new CustomDelayedNetworkKnowledgeDataHandler(
						networkDelay);

				simulation = new JDEECoSimulation(SIMULATION_START,
						SIMULATION_END, networkKnowledgeDataHandler);

				builder = new SimulationRuntimeBuilder();

				List<TimerTaskListener> listeners = new LinkedList<>();
				listeners.add(networkKnowledgeDataHandler);

				createAndDeployGroupLeader("L1", listeners);
				createAndDeployGroupMember("M1", "L1", listeners);
				createAndDeployGroupMember("M2", "L1", listeners);

				// Here we enable the inaccuracy checking - i.e. the second
				// case.
				// The parameter sets the threashold (maximum) inaccuracy (in
				// milliseconds). Please comment the following line if you would
				// like to disable the
				// inaccuracy checking and have just the first (simple) case.
				NearbyGMInDangerInaccuaracy.enableInaccuarcyChecking(inaccuracy);

				Log.i("Simulation Starts");
				simulation.run();
				Log.i("Simulation Finished");
				StringBuilder resultString = new StringBuilder(
						"inaccuracy: " + inaccuracy);
				resultString.append(", network delay: " + networkDelay);
				resultString
						.append(" - "
								+ (Results.getInstance().getReactionTime() - InDangerTimeHelper
										.getInstance().getInDangerTime()));
				afterTimes.add(resultString.toString());
			}
			afterTimes.add("\n");
		}

		for (String result : afterTimes) {
			System.out.println(result);
		}
		
		System.out.println(NearbyGMInDangerInaccuaracy.getInstance().getInaccuracies());
	}

	private static void createAndDeployGroupMember(String idx,
			String leaderIdx,
			Collection<? extends TimerTaskListener> simulationListeners)
			throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		GroupMember component = new GroupMember(idx, leaderIdx);
		createAndDeployComponent(component, idx, simulationListeners);
	}

	private static void createAndDeployGroupLeader(String idx,
			Collection<? extends TimerTaskListener> simulationListeners)
			throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		GroupLeader component = new GroupLeader(idx);
		createAndDeployComponent(component, idx, simulationListeners);
	}

	private static void createAndDeployComponent(Object component, String hostId, Collection<? extends TimerTaskListener> simulationListeners)
			throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE
				.createRuntimeMetadata();
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		AnnotationProcessorExtensionPoint extension = new IrmAwareAnnotationProcessorExtension(
				design, trace);
		AnnotationProcessor processor = new AnnotationProcessor(
				RuntimeMetadataFactoryExt.eINSTANCE, model, new CloningKnowledgeManagerFactory(), extension);
		processor.processComponent(component);
		// new AdaptationManager(), ???
		processor.processEnsemble(SensorDataUpdate.class);
		processor.processEnsemble(GMsInDangerUpdate.class);

		// pass design and trace models to the AdaptationManager
		for (ComponentInstance c : model.getComponentInstances()) {
			if (c.getName().equals(AdaptationManager.class.getName())) {
				c.getInternalData().put(AdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(AdaptationManager.TRACE_MODEL, trace);
			}
		}

		DirectSimulationHost host = simulation.getHost(hostId);
		RuntimeFramework runtime = builder.build(host, simulation,
				simulationListeners, model,
				new DefaultKnowledgeDataManager(model.getEnsembleDefinitions(), null),
				new CloningKnowledgeManagerFactory(), null,
					/* FIXME the following argument throws an exception in task invocation */ null);

		runtime.start();
	}
}
