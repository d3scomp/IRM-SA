package cz.cuni.mff.d3s.deeco.irm;

import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;

public class Assumption extends Invariant implements Evaluable {

	private BooleanCondition condition;

	public Assumption(String id, String description, Operation operation,
			List<String> roles, BooleanCondition condition) {
		super(id, description, operation, roles);
		this.condition = condition;
	}

	public BooleanCondition getCondition() {
		return condition;
	}
	
	@Override
	public boolean evaluate(List<String> assignedRoles) {
		return false;
	}

	
}
