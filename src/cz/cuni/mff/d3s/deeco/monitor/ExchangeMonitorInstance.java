package cz.cuni.mff.d3s.deeco.monitor;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.monitoring.ExchangeMonitor;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleJob;

public class ExchangeMonitorInstance extends MonitorInstance implements Runnable, ExchangeMonitor {

	protected EnsembleJob job;

	public ExchangeMonitorInstance(String id, KnowledgeManager km) {
		super(id, km);
	}
	
	private ExchangeMonitorInstance(String id, EnsembleJob job, KnowledgeManager km) {
		super(id, km);
		this.job = job;
	}
	
	public ExchangeMonitorInstance createForJob(EnsembleJob job) {
		ExchangeMonitorInstance result = new ExchangeMonitorInstance(id, job, km);
		return result;
	}

	public boolean getEvaluation() {
		return evaluation;
	}

	private void monitorExecutionFinished(boolean result) {
		this.evaluation = result;
	}

	@Override
	public void run() {
		// Here should be the code for checking the obsoleteness of data
		// produced by this ensmeble.
		monitorExecutionFinished(true);
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return job.getEvaluatedKnowledgePath(parameter, session);
	}

}
