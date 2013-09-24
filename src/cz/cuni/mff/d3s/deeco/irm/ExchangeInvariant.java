package cz.cuni.mff.d3s.deeco.irm;

import java.util.Arrays;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;

public class ExchangeInvariant extends Invariant implements Evaluable {

	public ExchangeInvariant(String id, String description, String coordinator,
			String member, MonitorProvider provider) {
		super(id, description, null, Arrays.asList(new String[] { coordinator,
				member }), provider);
	}

	public void setMonitorProvider(MonitorProvider provider) {
		this.provider = provider;
	}

	public String getCoordinatorRole() {
		return roles.get(0);
	}

	public String getMemberRole() {
		return roles.get(1);
	}

	@Override
	public boolean evaluate(Map<String, String> assignedRoles) {
		return provider.getExchangeMonitorEvaluation(id,
				assignedRoles.get(getCoordinatorRole()),
				assignedRoles.get(getMemberRole()));
	}
}
