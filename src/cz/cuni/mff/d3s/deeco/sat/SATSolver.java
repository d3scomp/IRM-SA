package cz.cuni.mff.d3s.deeco.sat;

import java.util.List;

import cz.cuni.mff.d3s.deeco.irm.IRMPrimitive;
import cz.cuni.mff.d3s.deeco.irm.Invariant;

public abstract class SATSolver {
	
	
	/**
	 * Returns a set of Invariants that 
	 * @return
	 */
	public abstract List<IRMPrimitive> solve();
	
	public abstract void addTopInvariant(Invariant top);
}
