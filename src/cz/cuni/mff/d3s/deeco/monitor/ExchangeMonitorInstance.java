package cz.cuni.mff.d3s.deeco.monitor;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.Parameter;
import cz.cuni.mff.d3s.deeco.monitoring.ExchangeMonitor;
import cz.cuni.mff.d3s.deeco.task.EnsembleTask;
import cz.cuni.mff.d3s.deeco.task.Task;

public class ExchangeMonitorInstance extends MonitorInstance implements
		Runnable, ExchangeMonitor {

	protected EnsembleTask task;

	public ExchangeMonitorInstance(String id, KnowledgeManager km) {
		super(id, km);
	}

	private ExchangeMonitorInstance(String id, EnsembleTask task,
			KnowledgeManager km) {
		super(id, km);
		this.task = task;
	}

	public ExchangeMonitorInstance createForJob(EnsembleTask task) {
		ExchangeMonitorInstance result = new ExchangeMonitorInstance(id, task,
				km);
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
		return parameter.getKnowledgePath().getEvaluatedPath(km,
				task.getCoordinator(), task.getMember(), null, session);
	}

	@Override
	public void executionFinished(Task task) {
		// Do nothing
	}

	@Override
	public void executionStarted(Task task) {
		// Do nothing

	}

	@Override
	public void executionException(Task task, Throwable t) {
		// Do nothing
	}

}
