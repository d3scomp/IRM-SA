package cz.cuni.mff.d3s.deeco.irm;

import java.util.Arrays;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitor.ProcessEnsembleMonitoringProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;

public class ProcessInvariant extends Invariant implements Evaluable {

	private ProcessEnsembleMonitoringProvider provider;
	private ComponentProcess process;

	public ProcessInvariant(String id, String description, String owner) {
		super(id, description, null, Arrays.asList(new String[] { owner }));
	}

	public void setMonitorProvider(ProcessEnsembleMonitoringProvider provider) {
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
		return provider.getMonitoring(
				assignedRoles.get(getOwner()) + process.getId())
				.getEvaluation();
	}
}
