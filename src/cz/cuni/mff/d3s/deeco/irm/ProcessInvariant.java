package cz.cuni.mff.d3s.deeco.irm;

import java.util.Arrays;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;

public class ProcessInvariant extends Invariant implements Evaluable {
	private ComponentProcess process;

	public ProcessInvariant(String id, String description, String owner,
			MonitorProvider provider) {
		super(id, description, null, Arrays.asList(new String[] { owner }),
				provider);
	}

	public void setMonitorProvider(MonitorProvider provider) {
		this.provider = provider;
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
	public boolean evaluate(Map<String, String> assignedRoles) {
		return provider.getProcessMonitorEvaluation(id,
				assignedRoles.get(getOwner()));
	}
}
