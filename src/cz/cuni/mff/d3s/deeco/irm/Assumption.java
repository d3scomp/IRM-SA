package cz.cuni.mff.d3s.deeco.irm;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.assumptions.AssumptionMonitoring;

public class Assumption extends Invariant implements Evaluable {

	private AssumptionMonitoring monitoring;

	public Assumption(String id, String description, Operation operation,
			List<String> roles, AssumptionMonitoring monitoring) {
		super(id, description, operation, roles);
		this.monitoring = monitoring;
	}
	
	@Override
	public boolean evaluate(Map<String, String> assignedRoles) {
		return monitoring.evaluate(assignedRoles);
	}
}
