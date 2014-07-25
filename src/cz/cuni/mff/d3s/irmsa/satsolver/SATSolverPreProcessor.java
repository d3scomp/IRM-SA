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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;

import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory;

/**
 * <p>
 * Preprocesses an IRM runtime model instance before it can be used by the
 * <code>SATSolver</code>. By "preprocessing" we mean translating the directed
 * acyclic graph (DAG) representing the instance into a forest (tree with
 * multiple roots).
 * </p>
 * <p>
 * If the DAG is already a forest (i.e. if there are no shared invariant
 * instances), then preprocessing has no effect whatsoever.
 * </p>
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public class SATSolverPreProcessor {

	private IRMInstance instance;
	private Set<InvariantInstance> visited;
	
	/**
	 * The constructor is used as a setter in this case
	 * 
	 * @param instance
	 *            instance to be preprocessed
	 */
	public SATSolverPreProcessor(IRMInstance instance) {
		this.instance = instance;
	}
	
	/**
	 * Converts the wrapped IRM runtime model instance instance which is a DAG
	 * (in the general case) to a forest by traversing the DAG from the roots to
	 * the leaves and creating new invariant instances where necessary.
	 * 
	 * @return the wrapped IRM runtime model instance which is the result of preprocessing
	 */
	public IRMInstance convertDAGToForest() {
		visited = new HashSet<>();
		for (InvariantInstance root : instance.getRoots()) {
			visitChildren(root);
		}
		return instance;
	}

	/**
	 * 
	 * @param ii
	 */
	void visitChildren(InvariantInstance ii) {
		for (Alternative a : ii.getAlternatives()) {
			// holds the elements to be deleted (because we cannot delete them in place)
			List<InvariantInstance> toDuplicate = new ArrayList<>();
			
			for (InvariantInstance child : a.getChildren()) {
				if (visited.contains(child)) {
					toDuplicate.add(child);
				} else {
					visited.add(child);
				}
			}
			
			for (InvariantInstance oldI : toDuplicate) {
				// create an exact copy of the old invariant
				// by using the EcoreUtils copy functionality we don't need to care whether 
				// the copied invariant is present or shadow
				InvariantInstance newI = EcoreUtil.copy(oldI);
				for (Alternative oldA : oldI.getAlternatives()) {
					Alternative newA = IRMRuntimeFactory.eINSTANCE.createAlternative();
					newA.getChildren().addAll(oldA.getChildren());
					newI.getAlternatives().add(newA);
				}
				// remove the old invariant from the old alternative's list and add the new invariant to the list
				a.getChildren().remove(oldI);
				a.getChildren().add(newI);
				// add the new invariant to the global list
				instance.getInvariantInstances().add(newI);
			}

			for (InvariantInstance child : a.getChildren()) {
				visitChildren(child);
			}
		}
	}
	
}
