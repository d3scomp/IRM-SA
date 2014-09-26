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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;

/**
 * <p>
 * Solves a single IRM runtime instance. By "solving" we mean (i) translating
 * the instance to a predicate logic formula, (ii) invoking the SAT solver with
 * the formula as input and, (iii) setting the <code>satisfied</code> flag of
 * each invariant instance by translating back the result of the SAT solver.
 * </p>
 * <p>
 * Using the "default" solver, which is is resolution-based. The other available
 * option is cutting-planes-based (more powerful reasoning, but less optimized).
 * </p>
 * @see <a href="http://www.sat4j.org/maven23/org.sat4j.pb/apidocs/index.html">Sat4j pseudo 2.3.1 API</a>
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class SATSolver {

	/**
	 * Helper API for adding AND and XOR constraints to the solver.
	 */
	private CustomSat4jDependencyHelper<SATVariable, String> helper = new CustomSat4jDependencyHelper<>(SolverFactory.newDefault());
	
	/**
	 * Holds the instance to be solved. 
	 */
	private IRMInstance forestInstance;

	public SATSolver(IRMInstance forestInstance) {
		this.forestInstance = forestInstance;
	}
	
	/**
	 * Returns the wrapped instance. To be called after <code>solve()</code> has
	 * returned <code>true</code> in order to retrieved the "solved" IRM instance.
	 */
	public IRMInstance getInstance() {
		return forestInstance;
	}

	public void setInstance(IRMInstance i) {
		forestInstance = i;
	}
	
	/**
	 * <p>
	 * Encodes the wrapped IRm instances into SAT constraints and invokes the
	 * solver. If a solution exists, translates the solution back to setting the
	 * <code>satisfied</code> flag on each invariant instance of the IRM
	 * instance.
	 * </p>
	 * <p>
	 * In case there is a solution, it should be retrieved via a subsequent call
	 * to <code>getInstance()</code>.
	 * </p>
	 * 
	 * @return <code>true</code> if there is a solution (in a sensible time
	 *         frame), <code>false</code> otherwise
	 */
	public boolean solve() {
		Log.i("Encoding...");
		try {
			List<InvariantInstance> roots =  forestInstance.getRoots();
			for (InvariantInstance root : roots) {
				encodeInvariantInstance(new SelectionVariable(root));
			}
		} catch (ContradictionException e) {
			Log.e("Error while encoding the IRMInstance " + forestInstance.toString() + " into SAT.", e);
		}
		
		Set<InvariantInstance> selectedInstances  = new HashSet<>();
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
				setSelectedInInvariantInstances(selectedInstances);
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

	/**
	 * <p>
	 * Recursively encodes the IRM graph into a SSAT formula by visiting each
	 * node using DFS.
	 * </p>
	 * <p>
	 * The encoding process is the following:
	 * <ul>
	 * <li>For each invariant, create one selection variable and one monitoring variable.</li>
	 * <li>For each invariant, add constraint that the corresponding selection variable implies the corresponding monitoring variable.</li>
	 * <li>For each selection variable that corresponds to an IRM forest root, assign it to <code>true</code>.</li>
	 * <li>For each monitoring variable, assign it to <code>true</code>/<code>false</code> according to the value of the associated monitor.</li>
	 * <li>For each AND-decomposition, add constraint that the selection variable of the parent is equivalent to the conjunction of the selection variables of its children.</li>
	 * <li>For each OR-decomposition, add constraint that the selection variable of the parent is equivalent to the exclusive disjunction of selection variables of its children.</li>
	 * </ul>
	 * </p>
	 * @param s
	 *            variable corresponding to the currently-encoded invariant
	 * @throws ContradictionException
	 */
	void encodeInvariantInstance(SelectionVariable s) throws ContradictionException {
		InvariantInstance i = s.getInstance();
		// if the wrapped is a root, add "root = true" constraint
		if (forestInstance.getRoots().contains(i)) {
			helper.setTrue(s, "Root " + i.toString() + " is selected.");
		}
		// create a monitoring variable for each selection variable
		SATVariable m = new MonitoringVariable(i);
		// add "s => m" constraint
		helper.halfOr("Implication from selection to monitoring variable of InvariantInstance " + i.toString(), m, s);
		// add "m = true" or "m = false" constraint
		if (i.isSatisfied()) {
			helper.setTrue(m, "Invariant " + i.toString() + " is satisfied.");
		} else {
			helper.setFalse(m, "Invariant " + i.toString() + " is NOT satisfied.");				
		}
		
		List<Alternative> alternatives = i.getAlternatives();
		List<SATVariable> selectionVariablesBelow = new ArrayList<>();
		// visit the invariant's children
		for (Alternative a: alternatives) {
			for (InvariantInstance child : a.getChildren()) {
				SelectionVariable selectionVariable = new SelectionVariable(child);
				encodeInvariantInstance(selectionVariable);
				selectionVariablesBelow.add(selectionVariable);
			}	
		}
		// AND-decomposition: add "p <=> c1 AND c2 AND ... AND cx" constraint
		if (alternatives.size() == 1) {
			helper.and("Invariant's " + i.toString() + " AND-decomposition.", s, selectionVariablesBelow.toArray(new SATVariable[selectionVariablesBelow.size()]));
		} 
		// OR-decomposition: add "p <=> c1 XOR c2 XOR ... XOR cx" constraint
		if (alternatives.size() > 1) {
			helper.xor("Invariant's " + i.toString() + " OR-decomposition.", s, selectionVariablesBelow.toArray(new SATVariable[selectionVariablesBelow.size()]));
		}
	}
	
	private void setSelectedInInvariantInstances(Set<InvariantInstance> selectedInstances) {
		for (InvariantInstance i : forestInstance.getInvariantInstances()) {
			if (selectedInstances.contains(i)) {
				i.setSelected(true);
			} else {
				i.setSelected(false);
			}
		}
	}
	
}
