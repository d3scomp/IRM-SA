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
package period;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import period.model.Component1;
import period.model.Component2;
import period.model.Ensemble1;
import period.model.Environment;
import period.model.FireFighter;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
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

/**
 * This class contains main for centralized run.
 * Most of it is adapted from CentralizedRun in fire fighters demo.
 */
public class CentralizedRun {

	/** Directory containing models. */
	static private final String MODELS_BASE_PATH = "model/";

	/** Path to design model of the simulation to run. */
	static private final String DESIGN_MODEL_PATH =
			MODELS_BASE_PATH + "firefighters.irmdesign";

	/** Start of the simulation in milliseconds. */
	static private final long SIMULATION_START = 0;

	/** End of the simulation in milliseconds. */
	static private final long SIMULATION_END = 400000;

	/** Network delay in milliseconds. Not used in centralized case. */
	static private final long NETWORK_DELAY = 100;

	/**
	 * Runs centralized simulation.
	 * @param args command line arguments, ignored
	 * @throws AnnotationProcessorException
	 */
	public static void main(String args[]) throws AnnotationProcessorException {
		Log.i("Preparing simulation");

		@SuppressWarnings("unused")
		final IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
		final IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);

		final DelayedKnowledgeDataHandler networkKnowledgeDataHandler = new DelayedKnowledgeDataHandler(NETWORK_DELAY);
		final JDEECoSimulation simulation = new JDEECoSimulation(SIMULATION_START, SIMULATION_END, networkKnowledgeDataHandler);

		final SimulationRuntimeBuilder builder = new SimulationRuntimeBuilder();

		List<TimerTaskListener> listeners = new LinkedList<>();
		listeners.add(networkKnowledgeDataHandler);

		createAndDeployComponents(design, simulation, builder, listeners);

		Log.i("Simulation Starts");
		simulation.run();
		Log.i("Simulation Finished");
	}

	/**
	 * Creates and deploys components.
	 * @param design IRM to use
	 * @param simulation JDEECoSimulation to use
	 * @param builder SimulationRuntimeBuilder to use
	 * @param simulationListeners listeners to use
	 * @throws AnnotationProcessorException
	 */
	private static void createAndDeployComponents(IRM design,
			JDEECoSimulation simulation,
			SimulationRuntimeBuilder builder,
			Collection<? extends TimerTaskListener> simulationListeners)
					throws AnnotationProcessorException {
		final RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		final TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		final AnnotationProcessorExtensionPoint extension = new IrmAwareAnnotationProcessorExtension(design, trace);
		final AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, new CloningKnowledgeManagerFactory(), extension);

		processor.process(
				new FireFighter(),
				new Environment(),
				PeriodAdaptationManager.create()
						.withInvariantFitnessCombiner(new InvariantFitnessCombinerAverage())
						.withAdapteeSelector(new AdapteeSelectorFitness())
						.withDirectionSelector(new DirectionSelectorImpl())
						.withDeltaComputor(new DeltaComputorFixed(250))
						.build());

		PeriodAdaptationManager.prepare(model, design, trace);

		final DirectSimulationHost host = simulation.getHost("host");
		final List<EnsembleDefinition> ensembles = model.getEnsembleDefinitions();
		final RuntimeFramework runtime = builder.build(host, simulation, simulationListeners, model, new DefaultKnowledgeDataManager(ensembles, null), new CloningKnowledgeManagerFactory(),null, 
				/* FIXME the following argument throws an exception in task invocation */ null);

		runtime.start();
	}
}
