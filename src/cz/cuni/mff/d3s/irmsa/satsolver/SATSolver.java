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
import java.util.Collection;
import java.util.List;

import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;

/**
 * Solves a single IRM runtime instance. By "solving" we mean (i) translating
 * the instance to a predicate logic formula, (ii) invoking the SAT solver with
 * the formula as input and, (iii) setting the <code>satisfied</code> flag of
 * each invariant instance by translating back the result of the SAT solver.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 *         TODO(IG) method-level comments
 */
public class SATSolver {

	/**
	 *  The "default" solver is resolution-based, the other available option is cutting-planes-based (more powerful reasoning, but less optimized).
	 *  @see <a href="http://www.sat4j.org/maven23/org.sat4j.pb/apidocs/index.html">Sat4j pseudo 2.3.1 API</a>
	 */
	private CustomSat4jDependencyHelper<SATVariable, String> helper = new CustomSat4jDependencyHelper<>(SolverFactory.newDefault());
	private IRMInstance forestInstance;
	private List<InvariantInstance> selectedInstances;

	public SATSolver(IRMInstance i) {
		forestInstance = i;
	}
	
	public IRMInstance getInstance() {
		return forestInstance;
	}

	public void setInstance(IRMInstance i) {
		forestInstance = i;
	}
	
	public boolean solve() {
		selectedInstances  = new ArrayList<>();
		Log.i("Encoding...");
		encodeForest();
		try {
			Log.i("Number of variables: " + helper.getNumberOfVariables());
			Log.i("Number of constraints: " + helper.getNumberOfConstraints());
			
			if (helper.hasASolution()) {
				Log.i("Solution found...");
				Collection<SATVariable> solution = helper.getASolution();
				Log.i("Solution size: " + solution.size());
				Log.i("Model: ");
				for (SATVariable v : solution) {
					if (v instanceof SelectionVariable) {
						InvariantInstance selected = v.getInstance();
						selectedInstances.add(selected);	
					}
				}
				toggleSelectedInvariantInstances();
				return true;
			} else {
				Log.i("No solution found...");
				return false;
			}
		} catch (TimeoutException e) {
			Log.e("Timeout threshold reached while trying to find a solution", e);
			return false;
		}
	}

	private void toggleSelectedInvariantInstances() {
		for (InvariantInstance i : forestInstance.getInvariantInstances()) {
			if (selectedInstances.contains(i)) {
				i.setSelected(true);
			} else {
				i.setSelected(false);
			}
		}
	}

	void encodeForest() {
		try {
			List<InvariantInstance> roots =  forestInstance.getRoots();
			for (InvariantInstance root : roots) {
				encodeInvariantInstance(new SelectionVariable(root));
			}
		} catch (ContradictionException e) {
			Log.e("Error while encoding the IRMInstance " + forestInstance.toString() + " into SAT.", e);
		}
	}

	void encodeInvariantInstance(SelectionVariable s) throws ContradictionException {
		
		InvariantInstance i = s.getInstance();
		
		if (forestInstance.getRoots().contains(i)) {
			helper.setTrue(s, "Root " + i.toString() + " is selected.");
		}
		
		SATVariable m = new MonitoringVariable(i);
		helper.halfOr("Implication from selection to monitoring variable of InvariantInstance " + i.toString(), m, s);
		
		if (i.isSatisfied()) {
			helper.setTrue(m, "Invariant " + i.toString() + " is satisfied.");
		} else {
			helper.setFalse(m, "Invariant " + i.toString() + " is NOT satisfied.");				
		}
		
		List<Alternative> alternatives = i.getAlternatives();
		List<SATVariable> selectionVariablesBelow = new ArrayList<>();

		for (Alternative a: alternatives) {
			for (InvariantInstance child : a.getChildren()) {
				SelectionVariable selectionVariable = new SelectionVariable(child);
				encodeInvariantInstance(selectionVariable);
				selectionVariablesBelow.add(selectionVariable);
			}	
		}
		// AND-decomposition
		if (alternatives.size() == 1) {
			helper.and("Invariant's " + i.toString() + " AND-decomposition.", s, selectionVariablesBelow.toArray(new SATVariable[selectionVariablesBelow.size()]));
		} 
		// OR-decomposition
		if (alternatives.size() > 1) {
			helper.xor("Invariant's " + i.toString() + " OR-decomposition.", s, selectionVariablesBelow.toArray(new SATVariable[selectionVariablesBelow.size()]));
		}
	}
	
}
