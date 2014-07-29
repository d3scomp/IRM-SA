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

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory;

/**
 * Helper methods to create IRM instances for the unit tests.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public class IRMHelper {

	public static void createANDdecomposition(InvariantInstance parent, InvariantInstance... children) {
		Alternative a = IRMRuntimeFactory.eINSTANCE.createAlternative();
		parent.getAlternatives().add(a);
		for (InvariantInstance child : children) {
			a.getChildren().add(child);	
		}
	}

	public static void createORdecomposition(InvariantInstance parent, InvariantInstance... children) {
		for (InvariantInstance child : children) {
			Alternative a = IRMRuntimeFactory.eINSTANCE.createAlternative();
			parent.getAlternatives().add(a);
			a.getChildren().add(child);	
		}
	}
	
	public static PresentInvariantInstance createPresentInvariantInstance(String id, IRMInstance instance, IRM design) {
		PresentInvariantInstance pi = IRMRuntimeFactory.eINSTANCE.createPresentInvariantInstance();
		pi.setDiagramInstance(instance);
		pi.setInvariant(findByIDInDesignModel(id, design));
		return pi;
	}
	
	public static ProcessInvariantInstance createProcessInvariantInstance(String id, ComponentProcess process, IRMInstance instance, IRM design) {
		ProcessInvariantInstance pi = IRMRuntimeFactory.eINSTANCE.createProcessInvariantInstance();
		pi.setDiagramInstance(instance);
		pi.setComponentProcess(process);
		pi.setInvariant(findByIDInDesignModel(id, design));
		return pi;
	}

	public static ShadowInvariantInstance createShadowInvariantInstance(IRMInstance instance) {
		ShadowInvariantInstance si = IRMRuntimeFactory.eINSTANCE.createShadowInvariantInstance();
		si.setDiagramInstance(instance);
		si.setSatisfied(true);
		return si;
	}
	
	private static Invariant findByIDInDesignModel(String id, IRM design) {
		for (Invariant i : design.getInvariants()) {
			if (i.getRefID().equals(id)) {
				return i;
			}
		}
		return null;
	}

}
