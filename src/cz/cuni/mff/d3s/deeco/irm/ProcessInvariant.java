package cz.cuni.mff.d3s.deeco.irm;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.monitor.MonitoringProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;

public class ProcessInvariant extends Invariant implements Evaluable {

	private ComponentProcess process;
	private MonitoringProvider monitoringProvider;

	public ProcessInvariant(String id, String description, String owner) {
		super(id, description, null, Arrays.asList(new String[] { owner }));
	}
	
	public void setMonitorInstanceHolder(MonitoringProvider monitoringProvider) {
		this.monitoringProvider = monitoringProvider;
	}
	
	public String getOwner() {
		return roles.get(0);
	}

	public ComponentProcess getProcess() {
		return process;
	}

	public void setProcess(ComponentProcess process) {
		this.process = process;
	}

	@Override
	public boolean evaluate(List<String> assignedRoles) {
		return false;
	}
}
