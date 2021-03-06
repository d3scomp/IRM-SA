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

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irmsa.EMFHelper;
import cz.cuni.mff.d3s.irmsa.IRMPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.MetaAdaptationPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.period.AdapteeSelectorFitness;
import cz.cuni.mff.d3s.irmsa.strategies.period.DeltaComputorFixed;
import cz.cuni.mff.d3s.irmsa.strategies.period.DirectionSelectorImpl;
import cz.cuni.mff.d3s.irmsa.strategies.period.InvariantFitnessCombinerAverage;
import cz.cuni.mff.d3s.irmsa.strategies.period.PeriodAdaptationPlugin;

/**
 * This class contains main for centralized run.
 */
public class CentralizedRun {

	/** Directory containing models. */
	static private final String MODELS_BASE_PATH = "model/";

	/** Path to design model of the simulation to run. */
	static private final String DESIGN_MODEL_PATH =
			MODELS_BASE_PATH + "firefighters.period.irmdesign";

	/** End of the simulation in milliseconds. */
	static private final long SIMULATION_END = 200000;

	/**
	 * Runs centralized simulation.
	 * @param args command line arguments, ignored
	 * @throws DEECoException
	 * @throws AnnotationProcessorException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(final String args[])
			throws DEECoException, AnnotationProcessorException, InstantiationException, IllegalAccessException {
		Log.i("Preparing simulation");

		/* create IRM plugin */
		@SuppressWarnings("unused")
		final IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
		final IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);

		final IRMPlugin irmPlugin = new IRMPlugin(design).withLog(false);
		final MetaAdaptationPlugin metaAdaptationPlugin = new MetaAdaptationPlugin(irmPlugin);

		// create PeriodAdaptationPlugin
		final RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		final PeriodAdaptationPlugin periodAdaptionPlugin =
				new PeriodAdaptationPlugin(metaAdaptationPlugin, model, design, irmPlugin.getTrace())
						.withInvariantFitnessCombiner(new InvariantFitnessCombinerAverage())
						.withAdapteeSelector(new AdapteeSelectorFitness())
						.withDirectionSelector(new DirectionSelectorImpl())
						.withDeltaComputor(new DeltaComputorFixed(250));

		final SimulationTimer simulationTimer = new DiscreteEventTimer();
		/* create main application container */
		final DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
		simulation.addPlugin(irmPlugin);
		simulation.addPlugin(metaAdaptationPlugin);
		simulation.addPlugin(periodAdaptionPlugin);
		/* deploy components and ensembles */
		final DEECoNode deecoNode = simulation.createNode(1);

		deecoNode.deployComponent(new FireFighter());
		deecoNode.deployComponent(new Environment());

		Log.i("Simulation Starts");
		simulation.start(SIMULATION_END);
		Log.i("Simulation Finished");
	}
}
