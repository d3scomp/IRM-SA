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

	public class SingleNodeSimulation {

		static public final String MODELS_BASE_PATH = "designModels" + File.separator;
		static public final String XMIFILE_PREFIX = "firefighters_complex";
		static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters_complex.irmdesign";

		public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {

			/* create IRM plugin */
			@SuppressWarnings("unused")
			IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
			IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);

			SimulationTimer simulationTimer = new DiscreteEventTimer();
			/* create main application container */
			DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
			simulation.addPlugin(new IRMPlugin(design)
					.withLog(true)
					.withLogDir(MODELS_BASE_PATH)
					.withLogPrefix(XMIFILE_PREFIX));
			/* deploy components and ensembles */
			DEECoNode deecoNode = simulation.createNode(1);

			deecoNode.deployComponent(new Officer("OF1"));
			deecoNode.deployComponent(new Firefighter("FF1", "OF1"));
			deecoNode.deployComponent(new SiteLeader());
			deecoNode.deployComponent(new UnmannedAerialVehicle("UAV1"));
			deecoNode.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode.deployEnsemble(PhotosUpdate.class);
			deecoNode.deployEnsemble(SensorDataUpdate.class);

			simulation.start(2000);

		}

	}
