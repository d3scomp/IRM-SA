package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.demo.firefighters.complex.Settings;
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

		public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException, InstantiationException, IllegalAccessException {

			/* create IRM plugin */
			@SuppressWarnings("unused")
			IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
			IRM design = (IRM) EMFHelper.loadModelFromXMI(Settings.DESIGN_MODEL_PATH);

			SimulationTimer simulationTimer = new DiscreteEventTimer();
			/* create main application container */
			DEECoSimulation simulation = new DEECoSimulation(simulationTimer);
			simulation.addPlugin(new IRMPlugin(design).withPeriod(Settings.ADAPTATION_PERIOD)
					.withLog(true)
					.withLogDir(Settings.MODELS_BASE_PATH)
					.withLogPrefix(Settings.XMIFILE_PREFIX));
			/* deploy components and ensembles */
			DEECoNode deecoNode = simulation.createNode(1);

			deecoNode.deployComponent(new Officer("OF1", simulationTimer));
			deecoNode.deployComponent(new Firefighter("FF1", "OF1", simulationTimer));
			deecoNode.deployComponent(new Firefighter("FF2", "OF1", simulationTimer));
			deecoNode.deployComponent(new Firefighter("FF3", "OF1", simulationTimer));
			deecoNode.deployComponent(new SiteLeader());
			deecoNode.deployComponent(new UnmannedAerialVehicle("UAV1"));
			deecoNode.deployEnsemble(GMsInDangerUpdate.class);
			deecoNode.deployEnsemble(FFsInDangerUpdate.class);
			deecoNode.deployEnsemble(PhotosUpdate.class);
			deecoNode.deployEnsemble(SensorDataUpdate.class);

			simulation.start(Settings.SIMULATION_DURATION);

		}

	}
