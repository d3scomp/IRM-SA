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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.sat4j.core.VecInt;
import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.pb.tools.XplainPB;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.GateTranslator;

/**
 * Extends the default DependencyHelper provided by the Sat4j library by
 * providing a convenience method to add "xor" constraints.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 * @param <T>
 *            The class of the objects to map into boolean variables.
 * @param <C>
 *            The class of the object to map to each constraint.
 */
public class CustomSat4jDependencyHelper<T, C> extends DependencyHelper<T, C> {

	private final GateTranslator gator;
	final Map<IConstr, C> descs = new HashMap<IConstr, C>();
	private final boolean explanationEnabled;
	private XplainPB xplain;
	final IPBSolver solver;

	public CustomSat4jDependencyHelper(IPBSolver solver) {
		this(solver, true);
	}

	public CustomSat4jDependencyHelper(IPBSolver solver, boolean explanationEnabled) {
		super(solver, explanationEnabled);
		if (explanationEnabled) {
			this.xplain = new XplainPB(solver);
			this.solver = this.xplain;
		} else {
			this.xplain = null;
			this.solver = solver;
		}
		this.gator = new GateTranslator(getSolver());
		this.explanationEnabled = explanationEnabled;
	}

	/**
	 * Create a constraint of the form thing <=> (thing1 xor thing 2 ... xor
	 * thing)
	 * 
	 * @see org.sat4j.pb.tools.DependencyHelper#or(Object, Object, Object...)
	 * 
	 * @param name
	 * @param thing
	 * @param otherThings
	 * @throws ContradictionException
	 */
	public void xor(C name, T thing, @SuppressWarnings("unchecked") T... otherThings)
			throws ContradictionException {
		IVecInt literals = new VecInt(otherThings.length);
		for (T t : otherThings) {
			literals.push(getIntValue(t));
		}
		IConstr[] constrs = this.gator.xor(getIntValue(thing), literals);
		for (IConstr constr : constrs) {
			if (constr != null) {
				this.descs.put(constr, name);
			}
		}
	}

	@Override
	public int getNumberOfConstraints() {
		return this.descs.size() + super.getNumberOfConstraints();
	}

	@Override
	public void reset() {
		this.descs.clear();
		super.reset();
	}

	@Override
	public Set<C> why() throws TimeoutException {
		if (!this.explanationEnabled) {
			throw new UnsupportedOperationException("Explanation not enabled!");
		}
		Collection<IConstr> explanation = this.xplain.explain();
		Set<C> ezexplain = new TreeSet<C>();
		for (IConstr constr : explanation) {
			C desc = this.descs.get(constr);
			if (desc != null) {
				ezexplain.add(desc);
			}
		}
		Set<C> ezexplainFromParentClass = super.why();
		ezexplain.addAll(ezexplainFromParentClass);
		return ezexplain;
	}

}
