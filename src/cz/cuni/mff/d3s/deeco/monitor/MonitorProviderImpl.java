package cz.cuni.mff.d3s.deeco.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.monitoring.AssumptionMonitor;
import cz.cuni.mff.d3s.deeco.monitoring.ExchangeMonitor;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.monitoring.ProcessMonitor;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJob;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleJob;

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
	public ProcessMonitor getProcessMonitor(ComponentProcessJob job) {
		String key = getProcessMonitorInstanceKey(job.getComponentId(),
				job.getModelId());
		if (processMonitorInstances.containsKey(key))
			return processMonitorInstances.get(key);
		else {
			ProcessMonitorInstance pmi = (ProcessMonitorInstance) processMonitors
					.get(job.getModelId());
			if (pmi == null) {
				pmi = new ProcessMonitorInstance(null, km);
				processMonitors.put(job.getModelId(), pmi);
			}
			pmi = pmi.createForJob(job);
			processMonitorInstances.put(key, pmi);
			return pmi;
		}
	}

	@Override
	public ExchangeMonitor getExchangeMonitor(EnsembleJob job) {
		String key = getExchangeMonitorInstanceKey(job.getCoordinator(),
				job.getMember(), job.getEnsemble().getExchangeId());
		if (exchangeMonitorInstances.containsKey(key))
			return exchangeMonitorInstances.get(key);
		else {
			ExchangeMonitorInstance emi = (ExchangeMonitorInstance) exchangeMonitors
					.get(job.getEnsemble().getExchangeId());
			if (emi == null) {
				emi = new ExchangeMonitorInstance(job.getEnsemble()
						.getExchangeId(), km);
				exchangeMonitors.put(job.getEnsemble().getExchangeId(), emi);
			}
			emi = emi.createForJob(job);
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
