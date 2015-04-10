package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.io.File;

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
import cz.cuni.mff.d3s.jdeeco.network.device.BroadcastLoopback;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

	public class MultiNodeSimulation {

		static public final String MODELS_BASE_PATH = "designModels" + File.separator;
		static public final String XMIFILE_PREFIX = "firefighters_complex";
		static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters_complex.irmdesign";

		public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {

			SimulationTimer simulationTimer = new DiscreteEventTimer();
			/* create main application container */
			DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
			/* add network plugins */
			simulation.addPlugin(new BroadcastLoopback());
			simulation.addPlugin(Network.class);
			simulation.addPlugin(DefaultKnowledgePublisher.class);
			simulation.addPlugin(KnowledgeInsertingStrategy.class);
			/* create and add irm plugin */
			@SuppressWarnings("unused")
			IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
			IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
			simulation.addPlugin(new IRMPlugin(design)
					.withLog(true)
					.withLogDir(MODELS_BASE_PATH)
					.withLogPrefix(XMIFILE_PREFIX));
			
			/* create nodes and deploy components and ensembles */
			DEECoNode deecoNode1 = simulation.createNode(1);
			deecoNode1.deployComponent(new Officer("OF1"));
			deecoNode1.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode1.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode1.deployEnsemble(PhotosUpdate.class);
			deecoNode1.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode2 = simulation.createNode(2);
			deecoNode2.deployComponent(new Firefighter("FF1", "OF1"));
			deecoNode2.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode2.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode2.deployEnsemble(PhotosUpdate.class);
			deecoNode2.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode3 = simulation.createNode(3);
			deecoNode3.deployComponent(new SiteLeader());
			deecoNode3.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode3.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode3.deployEnsemble(PhotosUpdate.class);
			deecoNode3.deployEnsemble(SensorDataUpdate.class);
			
			DEECoNode deecoNode4 = simulation.createNode(4);
			deecoNode4.deployComponent(new UnmannedAerialVehicle("UAV1"));
			deecoNode4.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode4.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode4.deployEnsemble(PhotosUpdate.class);
			deecoNode4.deployEnsemble(SensorDataUpdate.class);

			simulation.start(14000);

		}

	}
