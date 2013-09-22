package cz.cuni.mff.d3s.deeco.irm;

import java.util.Arrays;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitor.ProcessEnsembleMonitoringProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;

public class ExchangeInvariant extends Invariant implements Evaluable {

	private Ensemble ensemble;
	private ProcessEnsembleMonitoringProvider provider;

	public ExchangeInvariant(String id, String description, String coordinator,
			String member) {
		super(id, description, null, Arrays.asList(new String[] { coordinator,
				member }));
	}

	public void setMonitorProvider(ProcessEnsembleMonitoringProvider provider) {
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
				.getMonitoring(
						assignedRoles.get(getCoordinatorRole())
								+ assignedRoles.get(getMemberRole())
								+ ensemble.getId()).getEvaluation();
	}
}
