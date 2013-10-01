package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.ExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.monitoring.ProcessMonitor;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessTask;
import cz.cuni.mff.d3s.deeco.scheduling.Task;

public class ProcessMonitorInstance extends MonitorInstance implements
		Runnable, ProcessMonitor, ExecutionListener {
	protected final BooleanCondition activeMonitor;
	protected ComponentProcessTask task;

	private Boolean predictiveEvaluation;

	public ProcessMonitorInstance(BooleanCondition active, KnowledgeManager km) {
		super((active == null) ? "" : active.getId(), km);
		this.activeMonitor = active;
		this.predictiveEvaluation = true;
	}

	private ProcessMonitorInstance(BooleanCondition active,
			KnowledgeManager km, ComponentProcessTask task) {
		super((active == null) ? "" : active.getId(), km);
		this.activeMonitor = active;
		this.predictiveEvaluation = true;
		this.task = task;
	}

	public ProcessMonitorInstance createForJob(ComponentProcessTask task) {
		ProcessMonitorInstance result = new ProcessMonitorInstance(
				activeMonitor, km, task);
		return result;
	}

	@Override
	public void executionFinished(Task task) {
		// perhaps put it in another thread
		if (activeMonitor != null) {
			try {
				Object[] processParameters = getParameterMethodValues(
						activeMonitor.getParameters(), null);
				Method m = activeMonitor.getMethod();
				this.evaluation = (boolean) m.invoke(null, processParameters);
			} catch (Exception e) {
				this.evaluation = false;
			}
		}
	}

	@Override
	public void executionStarted(Task task) {
		// Do nothing
	}

	@Override
	public void executionException(Task task, Throwable t) {
		this.evaluation = false;
	}

	public boolean getEvaluation() {
		if (evaluation)
			return evaluation;
		else
			return predictiveEvaluation;
	}

	private void predictiveExecutionFinished(boolean result) {
		this.predictiveEvaluation = result;
	}

	@Override
	public void run() {
		// Should run the process in the mock, check the results by
		// activeMonitor and
		// put the results to the predictiveEvaluation.
		predictiveExecutionFinished(true);
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return parameter.getKnowledgePath().getEvaluatedPath(km, null, null,
				task.getComponentId(), session);
	}

}
