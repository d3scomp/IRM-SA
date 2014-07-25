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
package cz.cuni.mff.d3s.irmsa.satsolver;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;

public class SatSolverTest {

	static final String MODELS_BASE_PATH = "test.cz.cuni.mff.d3s.deeco.demo.vehicles.designModels.".replaceAll("[.]", File.separator);
	
	IRM design;
	IRMInstance instance;
	IRMRuntimeFactory factory = IRMRuntimeFactory.eINSTANCE;
	
	@Before
	/**
	 * Create an IRM runtime instance that complies to the model of vehicles_simple model.
	 */
	public void setUp() {
		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE;
		
		design = (IRM) EMFHelper.loadModelFromXMI(MODELS_BASE_PATH + "vehicles_simple.irmdesign");
		instance = factory.createIRMInstance();
		instance.setDesignModel(design);
	}

	@Test
	/**
	 * Graph with 1 invariant AND-decomposed into 2 process invariants.
	 * No shadows.
	 */
	public void testSimpleANDdecomposition() {
		 InvariantInstance i1 = createPresentInvariantInstance("i1", true);
		 instance.getInvariantInstances().add(i1);
		 InvariantInstance pi1 = createPresentInvariantInstance("pi1", true);
		 instance.getInvariantInstances().add(pi1);
		 InvariantInstance pi2 = createPresentInvariantInstance("pi2", true);
		 instance.getInvariantInstances().add(pi2);
		 
		 createANDdecomposition(i1, pi1, pi2);
		 
		 SATSolver solver = new SATSolver(instance);
		 if (solver.solve()) {		 
			 assertEquals(getSelectedInstances(instance).size(), 3);
		 }
	}

	@Test
	/**
	 * Graph with 1 invariant OR-decomposed into 2 process invariants.
	 * Right invariant chosen.
	 * No shadows.
	 */
	public void testSimpleORdecomposition1() {
		 InvariantInstance i1 = createPresentInvariantInstance("i1", true);
		 instance.getInvariantInstances().add(i1);
		 InvariantInstance pi1 = createPresentInvariantInstance("pi1", true);
		 instance.getInvariantInstances().add(pi1);
		 InvariantInstance pi2 = createPresentInvariantInstance("pi2", true);
		 instance.getInvariantInstances().add(pi2);
		 
		 createORdecomposition(i1, pi1, pi2);
		 
		 SATSolver solver = new SATSolver(instance);
		 if (solver.solve()) {		 
			 List<InvariantInstance> selected = getSelectedInstances(instance);
			 assertEquals(selected.size(), 2);
			 assertEquals(((PresentInvariantInstance) selected.get(0)).getInvariant().getRefID(),"i1");
			 assertEquals(((PresentInvariantInstance) selected.get(1)).getInvariant().getRefID(),"pi1");
		 }
	}
	
	@Test
	/**
	 * Graph with 1 invariant OR-decomposed into 2 process invariants.
	 * Left invariant chosen.
	 * No shadows.
	 */
	public void testSimpleORdecomposition2() {
		 InvariantInstance i1 = createPresentInvariantInstance("i1", true);
		 instance.getInvariantInstances().add(i1);
		 InvariantInstance pi1 = createPresentInvariantInstance("pi1", true);
		 instance.getInvariantInstances().add(pi1);
		 InvariantInstance pi2 = createPresentInvariantInstance("pi2", false);
		 instance.getInvariantInstances().add(pi2);
		 
		 createORdecomposition(i1, pi1, pi2);
		 
		 SATSolver solver = new SATSolver(instance);
		 if (solver.solve()) {		 
			 List<InvariantInstance> selected = getSelectedInstances(instance);
			 assertEquals(selected.size(), 2);
			 assertEquals(((PresentInvariantInstance) selected.get(0)).getInvariant().getRefID(),"i1");
			 assertEquals(((PresentInvariantInstance) selected.get(1)).getInvariant().getRefID(),"pi1");
		 }
	}
	
	@Test
	/**
	 * Graph comprised of 1 invariant OR-decomposed into one invariant and another one which is AND-decomposed into 2 more.
	 * No shadows.
	 */
	public void testANDandORdecomposition() {
		 InvariantInstance i1 = createPresentInvariantInstance("i1", true);
		 instance.getInvariantInstances().add(i1);
		 InvariantInstance pi1 = createPresentInvariantInstance("pi1", false);
		 instance.getInvariantInstances().add(pi1);
		 InvariantInstance i2 = createPresentInvariantInstance("i2", true);
		 instance.getInvariantInstances().add(i2);
		 InvariantInstance ei1 = createPresentInvariantInstance("ei1", true);
		 instance.getInvariantInstances().add(ei1);
		 InvariantInstance pi2 = createPresentInvariantInstance("pi2", true);
		 instance.getInvariantInstances().add(pi2);
		 
		 createORdecomposition(i1, pi1, i2);
		 createANDdecomposition(i2, ei1, pi2);
		 
		 SATSolver solver = new SATSolver(instance);
		 if (solver.solve()) {		 
			 List<InvariantInstance> selected = getSelectedInstances(instance);
			 assertEquals(selected.size(), 4);
			 assertEquals(((PresentInvariantInstance) selected.get(0)).getInvariant().getRefID(),"i1");
			 assertEquals(((PresentInvariantInstance) selected.get(1)).getInvariant().getRefID(),"i2");
			 assertEquals(((PresentInvariantInstance) selected.get(2)).getInvariant().getRefID(),"ei1");
			 assertEquals(((PresentInvariantInstance) selected.get(3)).getInvariant().getRefID(),"pi2");
		 }
	}
	
	@Test
	/**
	 * Graph comprised of 1 invariant OR-decomposed into one invariant and an AND-decomposition consisting of 3 more.
	 * Large sub-tree selected.
	 * One shadow invariant.
	 */
	public void testANDandORdecompositionWithShadows1() {
		 InvariantInstance i1 = createPresentInvariantInstance("i1", true);
		 instance.getInvariantInstances().add(i1);
		 InvariantInstance pi1 = createPresentInvariantInstance("pi1", false);
		 instance.getInvariantInstances().add(pi1);
		 InvariantInstance shadow = createShadowInvariantInstance();
		 instance.getInvariantInstances().add(shadow);
		 InvariantInstance ei1 = createPresentInvariantInstance("ei1", true);
		 instance.getInvariantInstances().add(ei1);
		 InvariantInstance pi2 = createPresentInvariantInstance("pi2", true);
		 instance.getInvariantInstances().add(pi2);
		 InvariantInstance pi3 = createPresentInvariantInstance("pi3", true);
		 instance.getInvariantInstances().add(pi3);
		 
		 createORdecomposition(i1, pi1, shadow);
		 createANDdecomposition(shadow, ei1, pi2, pi3);
		 
		 SATSolver solver = new SATSolver(instance);
		 if (solver.solve()) {		 
			 List<InvariantInstance> selected = getSelectedInstances(instance);
			 assertEquals(selected.size(), 5);
			 assertEquals(((PresentInvariantInstance) selected.get(0)).getInvariant().getRefID(),"i1");
			 assert (selected.get(1) instanceof ShadowInvariantInstance);
			 assertEquals(((PresentInvariantInstance) selected.get(2)).getInvariant().getRefID(),"ei1");
			 assertEquals(((PresentInvariantInstance) selected.get(3)).getInvariant().getRefID(),"pi2");
			 assertEquals(((PresentInvariantInstance) selected.get(4)).getInvariant().getRefID(),"pi3");
		 }
	}

	@Test
	/**
	 * Graph comprised of 1 invariant OR-decomposed into one invariant and an AND-decomposition consisting of 3 more.
	 * Small sub-tree selected.
	 * One shadow invariant.
	 */
	public void testANDandORdecompositionWithShadows2() {
		 InvariantInstance i1 = createPresentInvariantInstance("i1", true);
		 instance.getInvariantInstances().add(i1);
		 InvariantInstance pi1 = createPresentInvariantInstance("pi1", true);
		 instance.getInvariantInstances().add(pi1);
		 InvariantInstance shadow = createShadowInvariantInstance();
		 instance.getInvariantInstances().add(shadow);
		 InvariantInstance ei1 = createPresentInvariantInstance("ei1", true);
		 instance.getInvariantInstances().add(ei1);
		 InvariantInstance pi2 = createPresentInvariantInstance("pi2", true);
		 instance.getInvariantInstances().add(pi2);
		 InvariantInstance pi3 = createPresentInvariantInstance("pi3", false);
		 instance.getInvariantInstances().add(pi3);
		 
		 createORdecomposition(i1, pi1, shadow);
		 createANDdecomposition(shadow, ei1, pi2, pi3);
		 
		 SATSolver solver = new SATSolver(instance);
		 if (solver.solve()) {
			 List<InvariantInstance> selected = getSelectedInstances(instance);
			 assertEquals(selected.size(), 2);
			 assertEquals(((PresentInvariantInstance) selected.get(0)).getInvariant().getRefID(),"i1");
			 assertEquals(((PresentInvariantInstance) selected.get(1)).getInvariant().getRefID(),"pi1");
		 }
	}

	private List<InvariantInstance> getSelectedInstances(IRMInstance i) {
		List<InvariantInstance> ret = new ArrayList<>();
		for (InvariantInstance ii : i.getInvariantInstances()) {
			if (ii.isSelected()) {
				ret.add(ii);
			}
		}
		return ret;
	}

	private void createANDdecomposition(InvariantInstance parent, InvariantInstance... children) {
		Alternative a = factory.createAlternative();
		parent.getAlternatives().add(a);
		for (InvariantInstance child : children) {
			a.getChildren().add(child);	
		}
	}

	private void createORdecomposition(InvariantInstance parent, InvariantInstance... children) {
		for (InvariantInstance child : children) {
			Alternative a = factory.createAlternative();
			parent.getAlternatives().add(a);
			a.getChildren().add(child);	
		}
	}
	
	private PresentInvariantInstance createPresentInvariantInstance(String id, boolean satisfied) {
		PresentInvariantInstance pi = factory.createPresentInvariantInstance();
		pi.setDiagramInstance(instance);
		pi.setInvariant(findByIDInDesignModel(id));
		pi.setSatisfied(satisfied);
		return pi;
	}

	private ShadowInvariantInstance createShadowInvariantInstance() {
		ShadowInvariantInstance si = factory.createShadowInvariantInstance();
		si.setDiagramInstance(instance);
		si.setSatisfied(true);
		return si;
	}
	
	private Invariant findByIDInDesignModel(String id) {
		for (Invariant i : design.getInvariants()) {
			if (i.getRefID().equals(id)) {
				return i;
			}
		}
		return null;
	}

}
