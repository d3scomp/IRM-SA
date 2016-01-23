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
package combined;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
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
import cz.cuni.mff.d3s.irmsa.strategies.assumption.AssumptionParameterAdaptationManagerDelegate;
import cz.cuni.mff.d3s.irmsa.strategies.assumption.AssumptionParameterAdaptationPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.CorrelationPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.KnowledgeMetadataHolder;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metric.DifferenceMetric;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metric.Metric;
import cz.cuni.mff.d3s.irmsa.strategies.period.PeriodAdaptationManagerDelegate;
import cz.cuni.mff.d3s.irmsa.strategies.period.PeriodAdaptationPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * This class contains main for centralized run.
 */
public class Run {

	/** Directory containing models. */
	static private final String MODELS_BASE_PATH = "model/";

	/** Path to design model of the simulation to run. */
	static private final String DESIGN_MODEL_PATH =
			MODELS_BASE_PATH + "firefighters.combined.irmdesign";

	/** End of the simulation in milliseconds. */
	static private final long SIMULATION_END = 300_000;

	static final boolean enableMetaAdaptation = true;

	/**
	 * Runs centralized simulation.
	 * @param args command line arguments, ignored
	 * @throws DEECoException
	 * @throws AnnotationProcessorException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(final String args[])
			throws DEECoException, AnnotationProcessorException, InstantiationException, IllegalAccessException, IOException {
		Log.i("Preparing simulation");

		final String suffix = enableMetaAdaptation
				? "Adapt"
				: "NoAdapt";
		
		filter.Filter.filterWriter = new PrintWriter(String.format("filters%s.txt", suffix));
		
		final List<DEECoNode> nodesInSimulation = new ArrayList<DEECoNode>();
		final SimulationTimer simulationTimer = new DiscreteEventTimer();

		/* create main application container */
		final DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
		simulation.addPlugin(new SimpleBroadcastDevice(100, 50, SimpleBroadcastDevice.DEFAULT_RANGE, 128));
		simulation.addPlugin(Network.class);
		simulation.addPlugin(DefaultKnowledgePublisher.class);
		simulation.addPlugin(KnowledgeInsertingStrategy.class);
		simulation.addPlugin(new PositionPlugin(0, 0));

		//create nodes without adaptation
		DEECoNode deeco1 = simulation.createNode(1);
		nodesInSimulation.add(deeco1);
		deeco1.deployComponent(new FireFighter(Environment.FF_FOLLOWER_ID));

		DEECoNode deeco2 = simulation.createNode(2);
		nodesInSimulation.add(deeco2);
		deeco2.deployComponent(new FireFighter(Environment.LONELY_FF_ID));

		//create adaptation plugins
		@SuppressWarnings("unused")
		final IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
		final IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);

		final IRMPlugin irmPlugin = new IRMPlugin(design).withLog(false);
		final MetaAdaptationPlugin metaAdaptationPlugin = new MetaAdaptationPlugin(irmPlugin);

		// create PeriodAdaptationPlugin
		final RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		final PeriodAdaptationPlugin periodAdaptionPlugin =
				new PeriodAdaptationPlugin(metaAdaptationPlugin, model, design, irmPlugin.getTrace())
						.withInvariantFitnessCombiner(new cz.cuni.mff.d3s.irmsa.strategies.period.InvariantFitnessCombinerAverage())
						.withAdapteeSelector(new cz.cuni.mff.d3s.irmsa.strategies.period.AdapteeSelectorFitness())
						.withDirectionSelector(new cz.cuni.mff.d3s.irmsa.strategies.period.DirectionSelectorImpl())
						.withDeltaComputor(new cz.cuni.mff.d3s.irmsa.strategies.period.DeltaComputorFixed(Environment.POSITION_ADAPTATION_STEP))
						.withConsiderAssumptions(true)
						.withAdaptationBound(0.8)
						.withMaximumTries(3);

		// create AssumptionParameterAdaptationPlugin
		final AssumptionParameterAdaptationPlugin assumptionParameterAdaptionPlugin =
				new AssumptionParameterAdaptationPlugin(metaAdaptationPlugin, model, design, irmPlugin.getTrace())
						.withInvariantFitnessCombiner(new cz.cuni.mff.d3s.irmsa.strategies.assumption.InvariantFitnessCombinerAverage())
						.withAdapteeSelector(new cz.cuni.mff.d3s.irmsa.strategies.assumption.AdapteeSelectorFitness())
						.withDirectionSelector(new cz.cuni.mff.d3s.irmsa.strategies.assumption.DirectionSelectorImpl())
						.withDeltaComputor(new cz.cuni.mff.d3s.irmsa.strategies.assumption.DeltaComputorFixed(5))
						.withAdaptationBound(0.4)
						.withMaximumTries(3);

		// create correlation plugin
		registerMetadataForFields();

//		MonitorPlugin monitorPlugin = new MonitorPlugin(model, design, irmPlugin.getTrace());
		final DEECoNode deeco3;
		if(enableMetaAdaptation){
			final CorrelationPlugin correlationPlugin =
					new CorrelationPlugin(metaAdaptationPlugin, nodesInSimulation);
			// Meta-adaptation enabled
			deeco3 = simulation.createNode(3,
					irmPlugin, metaAdaptationPlugin,
					periodAdaptionPlugin,
					assumptionParameterAdaptionPlugin,
					correlationPlugin/*,
					monitorPlugin*/);
			nodesInSimulation.add(deeco3);
		} else {
			// Meta-adaptation disabled
				deeco3 = simulation.createNode(3,
						irmPlugin,
						metaAdaptationPlugin/*,
						monitorPlugin*/);
				nodesInSimulation.add(deeco3);
		}
		//deploy components
		ComponentInstance ff1ComponentInstance = deeco3.deployComponent(new FireFighter(Environment.FF_LEADER_ID));
		deeco3.deployComponent(new Environment());
		deeco3.deployEnsemble(FireFighterDataAggregation.class);
		deeco3.deployComponent(new EvaluationComponent("eval"));

		// Assign the FF1 to the evaluation component
		EvaluationComponent.init(ff1ComponentInstance);

		Environment.positionWriter = new PrintWriter(String.format("PositionSampled%s.csv", suffix), "UTF-8");
		AssumptionParameterAdaptationManagerDelegate.assumptionWriter = new PrintWriter(String.format("Assumption%s.csv", suffix), "UTF-8");
		AssumptionParameterAdaptationManagerDelegate.assumptionWriter.println("time;old;new");
		PeriodAdaptationManagerDelegate.periodWriter = new PrintWriter(String.format("Period%s.csv", suffix), "UTF-8");
		PeriodAdaptationManagerDelegate.periodWriter.println("time;old;new");

		// deeco3.getRuntimeMetadata().getComponentInstances().add(ci);
		//KnowledgeManager km = ff1ComponentInstance.getKnowledgeManager();
		// km.get(arg0) // TODO: examples in unit tests of knowledge manager

		Log.i("Simulation Starts");
		simulation.start(SIMULATION_END);
		// Close the file writer
		EvaluationComponent.finit();
		Environment.positionWriter.close();
		AssumptionParameterAdaptationManagerDelegate.assumptionWriter.close();
		PeriodAdaptationManagerDelegate.periodWriter.close();
		filter.Filter.filterWriter.close();
		Log.i("Simulation Finished");
	}

	/**
	 * Prepare and register metadata for fields.
	 */
	private static void registerMetadataForFields() {
		final String positionLabel = "position";
		final String temperatureLabel = "temperature";
		final String batteryLabel = "batteryLevel";

		final int positionBoundary = Environment.MAX_GROUP_DISTANCE;
		final int temperatureBoundary = 20;
		final int batteryBoundary = 20;

		final Metric simpleMetric = new DifferenceMetric();
		final Metric positionMetric = new PositionMetric();

		final double positionConfidence = 0.9;
		final double temperatureConfidence = 0.8;
		final double batteryConfidence = 0.9;

		KnowledgeMetadataHolder.setBoundAndMetric(positionLabel, positionBoundary, positionMetric, positionConfidence);
		KnowledgeMetadataHolder.setBoundAndMetric(temperatureLabel, temperatureBoundary, simpleMetric, temperatureConfidence);
		KnowledgeMetadataHolder.setBoundAndMetric(batteryLabel, batteryBoundary, simpleMetric, batteryConfidence);
	}
}
