package cz.cuni.mff.d3s.deeco.irm;

import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.monitor.MonitoringProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;

public class ExchangeInvariant extends Invariant {

	private Ensemble ensemble;
	private MonitoringProvider monitoringProvider;

	public ExchangeInvariant(String id, String description,
			String coordinator, String member) {
		super(id, description, null, Arrays.asList(new String[] {
				coordinator, member }));
	}
	
	public void setMonitorInstanceProvider(MonitoringProvider monitoringProvider) {
		this.monitoringProvider = monitoringProvider;
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
}
