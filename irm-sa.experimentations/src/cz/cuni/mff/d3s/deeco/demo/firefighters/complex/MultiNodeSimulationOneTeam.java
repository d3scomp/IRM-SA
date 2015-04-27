package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irmsa.EMFHelper;
import cz.cuni.mff.d3s.irmsa.IRMPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

	public class MultiNodeSimulationOneTeam {

		public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {
			SimulationTimer simulationTimer = new DiscreteEventTimer();
			/* create main application container */
			DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
			/* add network plugins */
			simulation.addPlugin(new CustomBroadcastLoopback());
			simulation.addPlugin(Network.class);
			simulation.addPlugin(DefaultKnowledgePublisher.class);
			simulation.addPlugin(KnowledgeInsertingStrategy.class);
			simulation.addPlugin(CustomL2Strategy.class);
			/* create and add irm plugin */
			@SuppressWarnings("unused")
			IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
			IRM design = (IRM) EMFHelper.loadModelFromXMI(Settings.DESIGN_MODEL_PATH);
			simulation.addPlugin(new IRMPlugin(design).withPeriod(Settings.ADAPTATION_PERIOD)
					.withLog(false)
					.withLogDir(Settings.MODELS_BASE_PATH)
					.withLogPrefix(Settings.XMIFILE_PREFIX));
			
			ClockProvider.init(simulationTimer);
			
			/* create nodes and deploy components and ensembles */
			DEECoNode deecoNode1 = simulation.createNode(1);
			deecoNode1.deployComponent(new Firefighter(1, "FF1", "OF1"));
			deecoNode1.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode1.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode1.deployEnsemble(PhotosUpdate.class);
			deecoNode1.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode2 = simulation.createNode(2);
			deecoNode2.deployComponent(new Firefighter(2, "FF2", "OF1"));
			deecoNode2.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode2.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode2.deployEnsemble(PhotosUpdate.class);
			deecoNode2.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode3 = simulation.createNode(3);
			deecoNode3.deployComponent(new Firefighter(3, "FF3", "OF1"));
			deecoNode3.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode3.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode3.deployEnsemble(PhotosUpdate.class);
			deecoNode3.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode10 = simulation.createNode(10);
			deecoNode10.deployComponent(new Officer(10, "OF1"));
			deecoNode10.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode10.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode10.deployEnsemble(PhotosUpdate.class);
			deecoNode10.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode13 = simulation.createNode(13);
			deecoNode13.deployComponent(new SiteLeader());
			deecoNode13.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode13.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode13.deployEnsemble(PhotosUpdate.class);
			deecoNode13.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode14 = simulation.createNode(14);
			deecoNode14.deployComponent(new UnmannedAerialVehicle("UAV1"));
			deecoNode14.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode14.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode14.deployEnsemble(PhotosUpdate.class);
			deecoNode14.deployEnsemble(SensorDataUpdate.class);

			simulation.start(Settings.SIMULATION_DURATION);

		}

	}
