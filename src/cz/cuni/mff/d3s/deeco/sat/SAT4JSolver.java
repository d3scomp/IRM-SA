package cz.cuni.mff.d3s.deeco.sat;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sat4j.pb.SolverFactory;
import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVec;

import cz.cuni.mff.d3s.deeco.irm.Evaluable;
import cz.cuni.mff.d3s.deeco.irm.IRMPrimitive;
import cz.cuni.mff.d3s.deeco.irm.Invariant;
import cz.cuni.mff.d3s.deeco.irm.Operation;
import cz.cuni.mff.d3s.deeco.irm.OperationType;
import cz.cuni.mff.d3s.deeco.logging.Log;

public class SAT4JSolver extends SATSolver {

	private final List<Invariant> tops;
	private final Map<String, String> roleAssignment;
	private final DependencyHelper<IRMPrimitive, String> dh;

	public SAT4JSolver(Map<String, String> roleAssignment) {
		this.tops = new LinkedList<>();
		this.roleAssignment = roleAssignment;
		this.dh = new DependencyHelper<>(SolverFactory.newCuttingPlanes());
	}

	@Override
	public void addTopInvariant(Invariant top) {
		if (!tops.contains(top))
			this.tops.add(top);
	}

	@Override
	public List<IRMPrimitive> solve() {
		List<IRMPrimitive> result = new LinkedList<>();
		try {
			for (Invariant top : tops) {
				dh.clause("clause"+top.toString(),
						top.getIRMPrimitives().toArray(new IRMPrimitive[] {}));
				encode(top, roleAssignment);
			}
			if (dh.hasASolution()) {
				result = new LinkedList<>();
				IVec<IRMPrimitive> sol = dh.getSolution();
				IRMPrimitive item;
				for (Iterator<IRMPrimitive> it = sol.iterator(); it.hasNext();) {
					item = it.next();
					result.add(item);
				}
			}
		} catch (Exception e) {
			Log.e("Solver exception", e);
		}
		return result;
	}

	private void encode(IRMPrimitive primitive, Map<String, String> roleAssignment)
			throws ContradictionException {
		if (primitive instanceof Invariant) {
			Invariant i = (Invariant) primitive;
			if (i.isLeaf()) {
				Evaluable e = (Evaluable) i;
				if (e.evaluate(roleAssignment))
					dh.setTrue(i, "True" + i.toString());
				else
					dh.setFalse(i, "False" + i.toString());
			} else {
				dh.iff("iff" + i.toString(), i, i.getOperation());
				encode(i.getOperation(), roleAssignment);
			}
		} else {
			Operation o = (Operation) primitive;
			if (o.getType().equals(OperationType.OR)) {
				dh.or("or" + o.toString(), o,
						o.getChildren().toArray(new IRMPrimitive[] {}));
			} else if (o.getType().equals(OperationType.AND)) {
				dh.and("and" + o.toString(), o,
						o.getChildren().toArray(new IRMPrimitive[] {}));
			}
			for (IRMPrimitive irmp : o.getChildren())
				encode(irmp, roleAssignment);
		}
	}
}
