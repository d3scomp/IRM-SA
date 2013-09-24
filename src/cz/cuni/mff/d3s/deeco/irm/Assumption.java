package cz.cuni.mff.d3s.deeco.irm;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;

public class Assumption extends Invariant implements Evaluable {

	public Assumption(String id, String description, Operation operation,
			List<String> roles, MonitorProvider provider) {
		super(id, description, operation, roles, provider);
	}
	
	@Override
	public boolean evaluate(Map<String, String> assignedRoles) {
		return provider.getAssumptionMonitorEvaluation(id, assignedRoles);
	}
}
