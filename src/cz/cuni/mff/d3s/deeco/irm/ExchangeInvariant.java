package cz.cuni.mff.d3s.deeco.irm;

import java.util.Arrays;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;

public class ExchangeInvariant extends Invariant implements Evaluable {

	private Ensemble ensemble;

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

	public Ensemble getEnsemble() {
		return ensemble;
	}

	public void setEnsemble(Ensemble ensemble) {
		this.ensemble = ensemble;
	}

	@Override
	public boolean evaluate(Map<String, String> assignedRoles) {
		return provider
				.getMonitor(
						assignedRoles.get(getCoordinatorRole())
								+ assignedRoles.get(getMemberRole())
								+ ensemble.getId()).getEvaluation();
	}
}
