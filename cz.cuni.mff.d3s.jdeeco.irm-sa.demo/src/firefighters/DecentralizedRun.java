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

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irmsa.EMFHelper;
import cz.cuni.mff.d3s.irmsa.IRMPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.BroadcastLoopback;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * Experiment run for the IRM-JSS paper evaluation. TODO make it work with
 * jDEECo 3.0
 * 
 * @author Ilias
 *
 */
public class DecentralizedRun {

	private static final String MODELS_BASE_PATH = "designModels/";
	private static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH
			+ "firefighters.irmdesign";
	private static final long SIMULATION_DURATION = 120000; // in milliseconds

	static public final String XMIFILE_PREFIX = "firefighters0";// !!!!!!!!!

	private static IRM design;
	private static DEECoSimulation simulation;
	private static int hostId = 1;

	@SuppressWarnings("unused")
	public static void main(String args[]) throws AnnotationProcessorException,
			InterruptedException, InstantiationException, IllegalAccessException, DEECoException {
		System.setProperty(DeecoProperties.PUBLISHING_PERIOD, new Integer(
				Settings.BROADCAST_PERIOD).toString());
		int networkDelay;
		int numberOfIterations = 5;
		List<String> afterTimes = new LinkedList<>();
		boolean inaccuarcyEnabled;
		afterTimes.add("----------Inaccuracy disabled-------------");
		afterTimes.add("\n");
		for (int i = 0; i <= numberOfIterations; i++) {


			InDangerTimeHelper.getInstance().reset();
			Results.getInstance().reset();
			System.out.println("Preparing simulation");
			networkDelay = i * Settings.NETWORK_DELAY;
			IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
			design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
			DiscreteEventTimer simulationTimer = new DiscreteEventTimer();
			simulation = new DEECoSimulation(simulationTimer);
			simulation.addPlugin(new IRMPlugin(design).withLog(true)
							.withLogDir(MODELS_BASE_PATH)
							.withLogPrefix(XMIFILE_PREFIX));
			simulation.addPlugin(new BroadcastLoopback());
			simulation.addPlugin(Network.class);
			simulation.addPlugin(DefaultKnowledgePublisher.class);
			simulation.addPlugin(KnowledgeInsertingStrategy.class);

			createAndDeployGroupLeader("L1");
			createAndDeployGroupMember("M1", "L1");
			createAndDeployGroupMember("M2", "L1");

			// Here we enable the inaccuracy checking - i.e. the second case.
			// The parameter sets the threashold (maximum) inaccuracy (in
			// milliseconds). Please comment the following line if you would
			// like to disable the
			// inaccuracy checking and have just the first (simple) case.
			// NearbyGMInDangerInaccuaracy.enableInaccuarcyChecking(400);

			Log.i("Simulation Starts");
			simulation.start(SIMULATION_DURATION);
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
			inaccuracy = Settings.BASE_INACCURACY + j
					* Settings.INACCURACY_INTERVAL;
			afterTimes.add("----------Inaccuracy " + inaccuracy
					+ "-------------");
			for (int i = 0; i <= numberOfIterations; i++) {
				InDangerTimeHelper.getInstance().reset();
				Results.getInstance().reset();
				Log.i("Preparing simulation");
				networkDelay = i * Settings.NETWORK_DELAY;
				IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
				design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
				DiscreteEventTimer simulationTimer = new DiscreteEventTimer();
				simulation = new DEECoSimulation(simulationTimer);
				simulation.addPlugin(new IRMPlugin(design).withLog(true)
						.withLogDir(MODELS_BASE_PATH)
						.withLogPrefix(XMIFILE_PREFIX));

				createAndDeployGroupLeader("L1");
				createAndDeployGroupMember("M1", "L1");
				createAndDeployGroupMember("M2", "L1");

				// Here we enable the inaccuracy checking - i.e. the second
				// case.
				// The parameter sets the threashold (maximum) inaccuracy (in
				// milliseconds). Please comment the following line if you would
				// like to disable the
				// inaccuracy checking and have just the first (simple) case.
				NearbyGMInDangerInaccuaracy
						.enableInaccuarcyChecking(inaccuracy);

				Log.i("Simulation Starts");
				simulation.start(SIMULATION_DURATION);
				Log.i("Simulation Finished");
				StringBuilder resultString = new StringBuilder("inaccuracy: "
						+ inaccuracy);
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

		System.out.println(NearbyGMInDangerInaccuaracy.getInstance()
				.getInaccuracies());
	}	

	private static void createAndDeployGroupMember(String idx,
			String leaderIdx)
			throws AnnotationProcessorException,
			InstantiationException, IllegalAccessException, DEECoException {
		GroupMember component = new GroupMember(idx, leaderIdx);
		createAndDeployComponent(component);
	}

	private static void createAndDeployGroupLeader(String idx)
			throws AnnotationProcessorException,
			InstantiationException, IllegalAccessException, DEECoException {
		GroupLeader component = new GroupLeader(idx);
		createAndDeployComponent(component);
	}

	private static void createAndDeployComponent(Object component)
			throws AnnotationProcessorException,
			InstantiationException, IllegalAccessException, DEECoException {
		DEECoNode deecoNode = simulation.createNode(hostId++);
		deecoNode.deployComponent(component);
		deecoNode.deployEnsemble(SensorDataUpdate.class);
		deecoNode.deployEnsemble(GMsInDangerUpdate.class);
	}
}
