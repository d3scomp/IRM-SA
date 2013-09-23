package cz.cuni.mff.d3s.deeco.irm;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitor.AssumptionMonitor;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;

public class Assumption extends Invariant implements Evaluable {

	private MonitorProvider provider;

	public Assumption(String id, String description, Operation operation,
			List<String> roles, MonitorProvider provider) {
		super(id, description, operation, roles, provider);
	}
	
	@Override
	public boolean evaluate(Map<String, String> assignedRoles) {
		AssumptionMonitor am = (AssumptionMonitor) provider.getMonitor(id);
		am = am.createForRoleAssignment(assignedRoles);
		return am.getEvaluation();
	}
}
