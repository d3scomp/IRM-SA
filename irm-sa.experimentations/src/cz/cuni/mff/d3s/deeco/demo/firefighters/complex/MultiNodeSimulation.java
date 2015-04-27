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

	public class MultiNodeSimulation {

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
			
			DEECoNode deecoNode4 = simulation.createNode(4);
			deecoNode4.deployComponent(new Firefighter(4, "FF4", "OF2"));
			deecoNode4.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode4.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode4.deployEnsemble(PhotosUpdate.class);
			deecoNode4.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode5 = simulation.createNode(5);
			deecoNode5.deployComponent(new Firefighter(5, "FF5", "OF2"));
			deecoNode5.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode5.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode5.deployEnsemble(PhotosUpdate.class);
			deecoNode5.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode6 = simulation.createNode(6);
			deecoNode6.deployComponent(new Firefighter(6, "FF6", "OF2"));
			deecoNode6.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode6.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode6.deployEnsemble(PhotosUpdate.class);
			deecoNode6.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode7 = simulation.createNode(7);
			deecoNode7.deployComponent(new Firefighter(7, "FF7", "OF3"));
			deecoNode7.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode7.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode7.deployEnsemble(PhotosUpdate.class);
			deecoNode7.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode8 = simulation.createNode(8);
			deecoNode8.deployComponent(new Firefighter(8, "FF8", "OF3"));
			deecoNode8.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode8.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode8.deployEnsemble(PhotosUpdate.class);
			deecoNode8.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode9 = simulation.createNode(9);
			deecoNode9.deployComponent(new Firefighter(9, "FF9", "OF3"));
			deecoNode9.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode9.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode9.deployEnsemble(PhotosUpdate.class);
			deecoNode9.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode10 = simulation.createNode(10);
			deecoNode10.deployComponent(new Officer(10, "OF1"));
			deecoNode10.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode10.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode10.deployEnsemble(PhotosUpdate.class);
			deecoNode10.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode11 = simulation.createNode(11);
			deecoNode11.deployComponent(new Officer(11, "OF2"));
			deecoNode11.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode11.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode11.deployEnsemble(PhotosUpdate.class);
			deecoNode11.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode12 = simulation.createNode(12);
			deecoNode12.deployComponent(new Officer(12, "OF3"));
			deecoNode12.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode12.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode12.deployEnsemble(PhotosUpdate.class);
			deecoNode12.deployEnsemble(SensorDataUpdate.class);
			
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
