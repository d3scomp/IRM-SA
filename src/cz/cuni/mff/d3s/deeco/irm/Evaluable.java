package cz.cuni.mff.d3s.deeco.irm;

import java.util.List;

public interface Evaluable {
	/**
	 * Evaluates this node against assigned roles. The assigned roles order
	 * corresponds to the order of the roles.
	 * 
	 * @param assignedRoles
	 * @return
	 */
	boolean evaluate(List<String> assignedRoles);
}
