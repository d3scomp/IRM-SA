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
package cz.cuni.mff.d3s.irmsa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.demo.vehicles.simple.Vehicle;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory;
import cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;

/**
 * Tests the activation & deactivation of DEECo processes based on the IRM runtime model instance(s).
 *
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class ArchitectureReconfiguratorTest {

	static final String MODELS_BASE_PATH = "test.cz.cuni.mff.d3s.deeco.demo.vehicles.designModels.".replaceAll("[.]", "/");

	static public final String XMIFILE_PREFIX = "vehicles_simple_";

	IRM design;
	TraceModel trace;

	DEECoNode deecoNode;

	@Before
	public void createRuntime() throws AnnotationProcessorException, DEECoException {

		trace = TraceFactory.eINSTANCE.createTraceModel();
		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
		design = (IRM) EMFHelper.loadModelFromXMI(MODELS_BASE_PATH + "vehicles_simple.irmdesign");

		IRMPlugin irmPlugin = new IRMPlugin(trace, design)
				.withLog(true)
				.withLogDir(MODELS_BASE_PATH)
				.withLogPrefix(XMIFILE_PREFIX);

		SimulationTimer simulationTimer = new DiscreteEventTimer();
		deecoNode = new DEECoNode(1, simulationTimer, irmPlugin);
		deecoNode.deployComponent(new Vehicle());

	}

	@Test
	public void testProcessDeactivation() {
		IRMInstance instance = IRMRuntimeFactory.eINSTANCE.createIRMInstance();
		instance.setDesignModel(design);
		ComponentProcess process = getProcess("pi1");
		ProcessInvariantInstance pi1 = IRMHelper.createProcessInvariantInstance("pi1", process, instance, design);
		pi1.setSelected(false);
		instance.getInvariantInstances().add(pi1);

		// WHEN the process is active
		assertTrue(process.isActive());
		ArchitectureReconfigurator reconfigurator = new ArchitectureReconfigurator(deecoNode.getRuntimeMetadata());
		reconfigurator.addInstance(instance);
		// WHEN the reconfigurator is invoked
		reconfigurator.toggleProcessesAndEnsembles();
		// THEN the process gets deactivated
		assertFalse(process.isActive());
	}

	@Test
	public void testProcessActivation() {
		IRMInstance instance = IRMRuntimeFactory.eINSTANCE.createIRMInstance();
		instance.setDesignModel(design);
		ComponentProcess process = getProcess("pi1");
		ProcessInvariantInstance pi1 = IRMHelper.createProcessInvariantInstance("pi1", process, instance, design);
		pi1.setSelected(true);
		instance.getInvariantInstances().add(pi1);

		// WHEN the process is non active
		process.setActive(false);
		ArchitectureReconfigurator reconfigurator = new ArchitectureReconfigurator(deecoNode.getRuntimeMetadata());
		reconfigurator.addInstance(instance);
		// WHEN the reconfigurator is invoked
		reconfigurator.toggleProcessesAndEnsembles();
		// THEN the process gets activated
		assertTrue(process.isActive());
	}

	private ComponentProcess getProcess(String invariantrefID) {
		for (ComponentInstance c: deecoNode.getRuntimeMetadata().getComponentInstances()) {
			if (!c.isSystemComponent()) {
				for (ComponentProcess p : c.getComponentProcesses()) {
					if (getInvariantForProcess(p).getRefID().equals(invariantrefID)) {
						return p;
					}
				}
			}
		}
		return null;
	}

	private Invariant getInvariantForProcess(ComponentProcess p) {
		for (ProcessTrace pt : trace.getProcessTraces()) {
			if (pt.getFrom().equals(p)) {
				return pt.getTo();
			}
		}
		return null;
	}


}
