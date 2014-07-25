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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitectureFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.impl.ExchangeInvariantInstanceImpl;
import cz.cuni.mff.d3s.irm.model.runtime.impl.PresentInvariantInstanceImpl;
import cz.cuni.mff.d3s.irm.model.runtime.impl.ProcessInvariantInstanceImpl;
import cz.cuni.mff.d3s.irm.model.runtime.impl.ShadowInvariantInstanceImpl;
import cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.satsolver.SATSolverPreProcessor;

public class IRMInstanceGeneratorTest {

	static final String MODELS_BASE_PATH = "test.cz.cuni.mff.d3s.deeco.demo.vehicles.designModels.".replaceAll("[.]", File.separator);
	
	RuntimeMetadataFactory rFactory; 
	ArchitectureFactory aFactory;
	TraceFactory tFactory;

	@Before
	public void setUp() {
		rFactory = RuntimeMetadataFactory.eINSTANCE;
		aFactory = ArchitectureFactory.eINSTANCE;
		tFactory = TraceFactory.eINSTANCE;
		
		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
	}
	
	@Test
	public void testVehiclesSimple() {
		IRM design = (IRM) EMFHelper.loadModelFromXMI(MODELS_BASE_PATH + "vehicles_simple.irmdesign");
		Architecture architecture = aFactory.createArchitecture();
		TraceModel trace = tFactory.createTraceModel();
		
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 1);
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 2);
		createComponentInstanceByName(design, architecture, trace, "Parking", 1);
		
		IRMInstanceGenerator generator = new IRMInstanceGenerator(architecture, design, trace);
		List<IRMInstance> IRMInstances = generator.generateIRMInstances();
		
		// THEN there are exactly 2 instances of the IRM runtime meta-model
		assertEquals(IRMInstances.size(),2);
		
		// THEN the first instance..
		IRMInstance i0 = IRMInstances.get(0);
		List<IRMComponentInstance> components0 = i0.getIRMcomponentInstances();
		// ..has exactly two component instances 
		assertEquals(components0.size(),2);

		// THEN the second instance..
		IRMInstance i1 = IRMInstances.get(1);
		List<IRMComponentInstance> components1 = i1.getIRMcomponentInstances();
		// ..has exactly two component instances 
		assertEquals(components1.size(),2);
		
		// THEN the two Vehicle components in the different IRM instances wrap DIFFERENT runtime components
		assertNotEquals(findArchitectureComponentInstance(components0, "Vehicle"), findArchitectureComponentInstance(components1, "Vehicle"));
		// THEN the two Parking components in the different IRM instances wrap THE SAME runtime components
		assertEquals(findArchitectureComponentInstance(components0, "Parking"), findArchitectureComponentInstance(components1, "Parking"));
		
		// THEN there are exactly 15 invariant instances in both cases
		List<InvariantInstance> invariants0 = i0.getInvariantInstances();
		List<InvariantInstance> invariants1 = i1.getInvariantInstances();
		assertEquals(invariants0.size(),15);
		assertEquals(invariants1.size(),15);
			
		// THEN the number of PresentInvariantInstances is 15 in both cases
		assertEquals(getInvariantInstancesWithSuperClasses(invariants0, PresentInvariantInstanceImpl.class).size(), 15);
		assertEquals(getInvariantInstancesWithSuperClasses(invariants1, PresentInvariantInstanceImpl.class).size(), 15);
		// out of which 5 are ProcessInvariantInstances and 2 are ExchangeInvariantInstances in both cases
		assertEquals(getInvariantInstances(invariants0, ProcessInvariantInstanceImpl.class).size(), 5);
		assertEquals(getInvariantInstances(invariants1, ProcessInvariantInstanceImpl.class).size(), 5);
		assertEquals(getInvariantInstances(invariants0, ExchangeInvariantInstanceImpl.class).size(), 2);
		assertEquals(getInvariantInstances(invariants1, ExchangeInvariantInstanceImpl.class).size(), 2);
		
		// THEN the number of ShadowInvariantInstances is 0 in both cases
		assertEquals(getInvariantInstances(invariants0, ShadowInvariantInstanceImpl.class).size(), 0);
		assertEquals(getInvariantInstances(invariants1, ShadowInvariantInstanceImpl.class).size(), 0);
		
		// THEN the number of Alternatives is 7 in both cases (5 non-shared AND, 1 non-shared OR decomposition)
		assertEquals(getAlternatives(invariants0).size(), 7);
		assertEquals(getAlternatives(invariants1).size(), 7);
	}

	@Test
	public void testVehiclesShadow() {
		IRM design = (IRM) EMFHelper.loadModelFromXMI(MODELS_BASE_PATH + "vehicles_shadow.irmdesign");
		Architecture architecture = aFactory.createArchitecture();
		TraceModel trace = tFactory.createTraceModel();
		
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 1);
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 2);
		createComponentInstanceByName(design, architecture, trace, "Parking", 1);
		
		IRMInstanceGenerator generator = new IRMInstanceGenerator(architecture, design, trace);
		List<IRMInstance> IRMInstances = generator.generateIRMInstances();
		
		// THEN there are exactly 2 instances of the IRM runtime meta-model
		assertEquals(IRMInstances.size(),2);
		
		// THEN on the first instance..
		IRMInstance i = IRMInstances.get(0);
		
		// ..there are exactly 14 invariant instances in both cases
		List<InvariantInstance> invariants = i.getInvariantInstances();
		assertEquals(invariants.size(),14);
		
		// ..the number of PresentInvariantInstances is 12
		assertEquals(getInvariantInstancesWithSuperClasses(invariants, PresentInvariantInstanceImpl.class).size(), 12);
		// ..out of which 4 are ProcessInvariantInstances and 2 are ExchangeInvariantInstances
		assertEquals(getInvariantInstances(invariants, ProcessInvariantInstanceImpl.class).size(), 4);
		assertEquals(getInvariantInstances(invariants, ExchangeInvariantInstanceImpl.class).size(), 2);

		// ..the number of ShadowInvariantInstances is 2
		assertEquals(getInvariantInstances(invariants, ShadowInvariantInstanceImpl.class).size(), 2);
		
		// ..the number of Alternatives is 7 (5 non-shared AND, 1 non-shared OR decomposition)
		assertEquals(getAlternatives(invariants).size(), 7);
	}
	
	@Test
	public void testVehiclesShared() {
		IRM design = (IRM) EMFHelper.loadModelFromXMI(MODELS_BASE_PATH + "vehicles_shared.irmdesign");
		Architecture architecture = aFactory.createArchitecture();
		TraceModel trace = tFactory.createTraceModel();
		
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 1);
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 2);
		createComponentInstanceByName(design, architecture, trace, "Parking", 1);
		
		IRMInstanceGenerator generator = new IRMInstanceGenerator(architecture, design, trace);
		List<IRMInstance> IRMInstances = generator.generateIRMInstances();
		
		for (IRMInstance i : IRMInstances) {
			SATSolverPreProcessor preProcessor = new SATSolverPreProcessor(i);
			preProcessor.convertDAGToForest();
		}

		// THEN there are exactly 2 instances of the IRM runtime meta-model
		assertEquals(IRMInstances.size(),2);
		
		// THEN on the first instance..
		IRMInstance i = IRMInstances.get(0);
		
		// ..there are exactly 19 invariant instances
		List<InvariantInstance> invariants = i.getInvariantInstances();
		assertEquals(invariants.size(),19);
		
		// ..the number of PresentInvariantInstances is 18
		assertEquals(getInvariantInstancesWithSuperClasses(invariants, PresentInvariantInstanceImpl.class).size(), 18);
		// ..out of which 6 are ProcessInvariantInstances and 3 are ExchangeInvariantInstances
		assertEquals(getInvariantInstances(invariants, ProcessInvariantInstanceImpl.class).size(), 6);
		assertEquals(getInvariantInstances(invariants, ExchangeInvariantInstanceImpl.class).size(), 3);

		// ..the number of ShadowInvariantInstances is 1
		assertEquals(getInvariantInstances(invariants, ShadowInvariantInstanceImpl.class).size(), 1);
		
		// ..the number of Alternatives is 8 (4 non-shared AND, 1 shared AND, 1 non-shared OR decomposition)
		assertEquals(getAlternatives(invariants).size(), 8);
	}
	
	@Test
	public void testVehiclesSharedInterleaved() {
		IRM design = (IRM) EMFHelper.loadModelFromXMI(MODELS_BASE_PATH + "vehicles_shared_interleaved.irmdesign");
		Architecture architecture = aFactory.createArchitecture();
		TraceModel trace = tFactory.createTraceModel();
		
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 1);
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 2);
		createComponentInstanceByName(design, architecture, trace, "Parking", 1);
		
		IRMInstanceGenerator generator = new IRMInstanceGenerator(architecture, design, trace);
		List<IRMInstance> IRMInstances = generator.generateIRMInstances();
		
		for (IRMInstance i : IRMInstances) {
			SATSolverPreProcessor preProcessor = new SATSolverPreProcessor(i);
			preProcessor.convertDAGToForest();
		}
		
		// THEN there are exactly 2 instances of the IRM runtime meta-model
		assertEquals(IRMInstances.size(),2);
		
		// THEN on the first instance..
		IRMInstance i = IRMInstances.get(0);
		
		// ..there are exactly 19 invariant instances
		List<InvariantInstance> invariants = i.getInvariantInstances();
		assertEquals(invariants.size(),24);
		
		// ..the number of PresentInvariantInstances is 22
		assertEquals(getInvariantInstancesWithSuperClasses(invariants, PresentInvariantInstanceImpl.class).size(), 22);
		// ..out of which 7 are ProcessInvariantInstances and 4 are ExchangeInvariantInstances
		assertEquals(getInvariantInstances(invariants, ProcessInvariantInstanceImpl.class).size(), 7);
		assertEquals(getInvariantInstances(invariants, ExchangeInvariantInstanceImpl.class).size(), 4);
		
		// ..the number of ShadowInvariantInstances is 1
		assertEquals(getInvariantInstances(invariants, ShadowInvariantInstanceImpl.class).size(), 2);
		
		// ..the number of Alternatives is 11 (3 non-shared AND, 2 shared AND, 1 shared OR decomposition)
		assertEquals(getAlternatives(invariants).size(), 11);
	}

	@Test
	public void testVehiclesForest() {
		IRM design = (IRM) EMFHelper.loadModelFromXMI(MODELS_BASE_PATH + "vehicles_forest.irmdesign");
		Architecture architecture = aFactory.createArchitecture();
		TraceModel trace = tFactory.createTraceModel();
		
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 1);
		createComponentInstanceByName(design, architecture, trace, "Vehicle", 2);
		createComponentInstanceByName(design, architecture, trace, "Parking", 1);
		
		IRMInstanceGenerator generator = new IRMInstanceGenerator(architecture, design, trace);
		List<IRMInstance> IRMInstances = generator.generateIRMInstances();
		
		for (IRMInstance i : IRMInstances) {
			SATSolverPreProcessor preProcessor = new SATSolverPreProcessor(i);
			preProcessor.convertDAGToForest();
		}
		
		// THEN there are exactly 2 instances of the IRM runtime meta-model
		assertEquals(IRMInstances.size(),2);
		
		// THEN on the first instance..
		IRMInstance i = IRMInstances.get(0);
		
		// ..there are exactly 18 invariant instances
		List<InvariantInstance> invariants = i.getInvariantInstances();
		assertEquals(invariants.size(),18);
		
		// ..the number of PresentInvariantInstances is 17
		assertEquals(getInvariantInstancesWithSuperClasses(invariants, PresentInvariantInstanceImpl.class).size(), 17);
		// ..out of which 5 are ProcessInvariantInstances and 3 are ExchangeInvariantInstances
		assertEquals(getInvariantInstances(invariants, ProcessInvariantInstanceImpl.class).size(), 5);
		assertEquals(getInvariantInstances(invariants, ExchangeInvariantInstanceImpl.class).size(), 3);
		
		// ..the number of ShadowInvariantInstances is 1
		assertEquals(getInvariantInstances(invariants, ShadowInvariantInstanceImpl.class).size(), 1);
		
		// ..the number of Alternatives is 8 (4 non-shared AND, 1 shared AND, 1 non-shared OR decomposition)
		assertEquals(getAlternatives(invariants).size(), 8);
	}
	
	private void createComponentInstanceByName(IRM design, Architecture architecture, TraceModel trace, String name, int id) {
		LocalComponentInstance aci = aFactory.createLocalComponentInstance();
		ComponentInstance rci = rFactory.createComponentInstance();
		rci.setName(name+id);
		aci.setRuntimeInstance(rci);
		aci.setId(name+id);
		aci.setKnowledgeManager(new CloningKnowledgeManager(name+id));
		architecture.getComponentInstances().add(aci);
		
		ComponentTrace cTrace = tFactory.createComponentTrace();
		cTrace.setFrom(rci);
		cTrace.setTo(getDesignComponentByName(design, name));
		trace.getComponentTraces().add(cTrace);
	}

	private Component getDesignComponentByName(IRM design, String name) {
		for (Component c : design.getComponents()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}
	
	private cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance findArchitectureComponentInstance(List<IRMComponentInstance> components, String componentName) {
		for (IRMComponentInstance i : components) {
			if (i.getArchitectureInstance().getId().contains(componentName)) {
				return i.getArchitectureInstance();
			}
		}
		return null;
	}
	
	private List<InvariantInstance> getInvariantInstances(List<InvariantInstance> invariants, Class<?> clazz) {
		List<InvariantInstance> ret = new ArrayList<>();
		for (InvariantInstance i : invariants) {
			if (i.getClass().equals(clazz)) {
				ret.add(i);
			}
		}
		return ret;
	}
	
	private List<InvariantInstance> getInvariantInstancesWithSuperClasses(List<InvariantInstance> invariants, Class<?> clazz) {
		List<InvariantInstance> ret = getInvariantInstances(invariants, clazz);
		for (InvariantInstance i : invariants) {
			if (i.getClass().getSuperclass().equals(clazz)) {
				ret.add(i);
			}
		}
		return ret;
	}

	private List<Alternative> getAlternatives(List<InvariantInstance> invariants) {
		ArrayList<Alternative> alternatives = new ArrayList<>();
		for (InvariantInstance i : invariants) {
			alternatives.addAll(i.getAlternatives());
		}
 		return alternatives;
	}

}
