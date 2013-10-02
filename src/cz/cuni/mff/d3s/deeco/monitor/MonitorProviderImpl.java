package cz.cuni.mff.d3s.deeco.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.monitoring.AssumptionMonitor;
import cz.cuni.mff.d3s.deeco.monitoring.ExchangeMonitor;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.monitoring.ProcessMonitor;
import cz.cuni.mff.d3s.deeco.task.ComponentProcessTask;
import cz.cuni.mff.d3s.deeco.task.EnsembleTask;

public class MonitorProviderImpl implements MonitorProvider {

	private final static String SEPARATOR = ".";

	private final KnowledgeManager km;
	private final Map<String, AssumptionMonitor> assumptionMonitors;
	private final Map<String, ProcessMonitor> processMonitors;
	private final Map<String, ExchangeMonitor> exchangeMonitors;
	private final Map<String, ProcessMonitor> processMonitorInstances;
	private final Map<String, ExchangeMonitor> exchangeMonitorInstances;

	public MonitorProviderImpl(KnowledgeManager km) {
		this.km = km;
		this.assumptionMonitors = new HashMap<>();
		this.processMonitors = new HashMap<>();
		this.exchangeMonitors = new HashMap<>();
		this.processMonitorInstances = new HashMap<>();
		this.exchangeMonitorInstances = new HashMap<>();
	}

	@Override
	public boolean getExchangeMonitorEvaluation(String eInvariantId,
			String coordinatorId, String memberId) {
		String key = getExchangeMonitorInstanceKey(coordinatorId, memberId,
				eInvariantId);
		if (!exchangeMonitorInstances.containsKey(key))
			return true;
		return exchangeMonitorInstances.get(key).getEvaluation();
	}

	@Override
	public boolean getProcessMonitorEvaluation(String pInvariantId,
			String ownerId) {
		String key = getProcessMonitorInstanceKey(ownerId, pInvariantId);
		if (!processMonitorInstances.containsKey(key))
			return true;
		return processMonitorInstances.get(key).getEvaluation();
	}

	@Override
	public boolean getAssumptionMonitorEvaluation(String assumptionId,
			Map<String, String> roleAssignment) {
		if (!assumptionMonitors.containsKey(assumptionId))
			return true;
		AssumptionMonitorInstance ami = (AssumptionMonitorInstance) assumptionMonitors
				.get(assumptionId);
		return ami.createForRoleAssignment(roleAssignment).getEvaluation();
	}

	@Override
	public void monitorAssumption(BooleanCondition assumption) {
		if (!assumptionMonitors.containsKey(assumption.getId())) {
			assumptionMonitors.put(assumption.getId(),
					new AssumptionMonitorInstance(assumption, km));
		}

	}

	@Override
	public void monitorAssumptions(List<BooleanCondition> assumptions) {
		for (BooleanCondition a : assumptions)
			monitorAssumption(a);
	}

	@Override
	public void monitorProcessInvariant(BooleanCondition activeMonitor) {
		if (!processMonitors.containsKey(activeMonitor.getId())) {
			processMonitors.put(activeMonitor.getId(),
					new ProcessMonitorInstance(activeMonitor, km));
		}

	}

	@Override
	public void monitorProcessInvariants(List<BooleanCondition> activeMonitors) {
		for (BooleanCondition am : activeMonitors)
			monitorProcessInvariant(am);

	}

	@Override
	public void monitorExchangeInvariant(String eInvariantId) {
		if (!exchangeMonitors.containsKey(eInvariantId)) {
			exchangeMonitors.put(eInvariantId, new ExchangeMonitorInstance(
					eInvariantId, km));
		}

	}

	@Override
	public ProcessMonitor getProcessMonitor(ComponentProcessTask task) {
		String processId = task.getComponentProcess().getId();
		String key = getProcessMonitorInstanceKey(task.getComponentId(),
				processId);
		if (processMonitorInstances.containsKey(key))
			return processMonitorInstances.get(key);
		else {
			ProcessMonitorInstance pmi = (ProcessMonitorInstance) processMonitors
					.get(processId);
			if (pmi == null) {
				pmi = new ProcessMonitorInstance(null, km);
				processMonitors.put(processId, pmi);
			}
			pmi = pmi.createForJob(task);
			processMonitorInstances.put(key, pmi);
			return pmi;
		}
	}

	@Override
	public ExchangeMonitor getExchangeMonitor(EnsembleTask task) {
		String key = getExchangeMonitorInstanceKey(task.getCoordinator(),
				task.getMember(), task.getEnsemble().getExchangeId());
		if (exchangeMonitorInstances.containsKey(key))
			return exchangeMonitorInstances.get(key);
		else {
			ExchangeMonitorInstance emi = (ExchangeMonitorInstance) exchangeMonitors
					.get(task.getEnsemble().getExchangeId());
			if (emi == null) {
				emi = new ExchangeMonitorInstance(task.getEnsemble()
						.getExchangeId(), km);
				exchangeMonitors.put(task.getEnsemble().getExchangeId(), emi);
			}
			emi = emi.createForJob(task);
			exchangeMonitorInstances.put(key, emi);
			return emi;
		}
	}

	private String getProcessMonitorInstanceKey(String ownerId,
			String pInvariantId) {
		return pInvariantId + SEPARATOR + ownerId;
	}

	private String getExchangeMonitorInstanceKey(String coordinatorId,
			String memberId, String eInvariantId) {
		return eInvariantId + SEPARATOR + coordinatorId + SEPARATOR + memberId;
	}

}
