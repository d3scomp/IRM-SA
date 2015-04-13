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

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;
import cz.cuni.mff.d3s.irmsa.IRMPlugin;

/**
 * Experiment run for the IRM-JSS paper evaluation.
 * TODO make it work with jDEECo 3.0 
 * 
 * @author Ilias
 *
 */
public class CentralizedRun {

	private static final String MODELS_BASE_PATH = "designModels/";
	private static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters.irmdesign";
	private static final long SIMULATION_DURATION = 2000; // in milliseconds
	
	
	static public final String XMIFILE_PREFIX = "vehicles_simple_";//!!!!!!!!!
	
	private static IRM design;
	private static DEECoSimulation simulation;
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, AnnotationProcessorException, DEECoException {
		System.out.println("Preparing simulation...");
		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE; 
		design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		DiscreteEventTimer simulationTimer = new DiscreteEventTimer();
		/* create main application container */
		simulation = new DEECoSimulation(simulationTimer);
		simulation.addPlugin(new IRMPlugin(trace, design)
				.withLog(true)
				.withLogDir(MODELS_BASE_PATH)
				.withLogPrefix(XMIFILE_PREFIX));
		/* deploy components and ensembles */
		createAndDeployComponents();

		simulation.start(SIMULATION_DURATION);

		System.out.println("Simulation finished...");
	}
	
	private static void createAndDeployComponents() throws AnnotationProcessorException, InstantiationException, IllegalAccessException, DEECoException {
		DEECoNode deecoNode = simulation.createNode(1);

		deecoNode.deployComponent(new GroupLeader("L1"));
		deecoNode.deployComponent(new GroupMember("M1", "L1"));
		deecoNode.deployComponent(new GroupMember("M2", "L1"));
		deecoNode.deployEnsemble(SensorDataUpdate.class);
		deecoNode.deployEnsemble(GMsInDangerUpdate.class);
	}
}
