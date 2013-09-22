package cz.cuni.mff.d3s.deeco.sat;

import java.util.List;

import cz.cuni.mff.d3s.deeco.irm.IRMPrimitive;

public abstract class SATSolver {
	
	
	/**
	 * Returns a set of Invariants that 
	 * @return
	 */
	public abstract List<IRMPrimitive> solve();
}
