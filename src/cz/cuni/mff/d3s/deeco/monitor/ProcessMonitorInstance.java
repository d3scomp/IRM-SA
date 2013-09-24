package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.monitoring.ProcessMonitor;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJob;
import cz.cuni.mff.d3s.deeco.scheduling.Job;

public class ProcessMonitorInstance extends MonitorInstance implements Runnable, ProcessMonitor, JobExecutionListener {
	protected final BooleanCondition activeMonitor;
	protected ComponentProcessJob job;

	private Boolean predictiveEvaluation;

	public ProcessMonitorInstance(BooleanCondition active,
			KnowledgeManager km) {
		super((active == null) ? "" : active.getId(), km);
		this.activeMonitor = active;
		this.predictiveEvaluation = true;
	}
	
	private ProcessMonitorInstance(BooleanCondition active,
			KnowledgeManager km, ComponentProcessJob job) {
		super((active == null) ? "" : active.getId(), km);
		this.activeMonitor = active;
		this.predictiveEvaluation = true;
	}
	
	public ProcessMonitorInstance createForJob(ComponentProcessJob job) {
		ProcessMonitorInstance result = new ProcessMonitorInstance(activeMonitor, km, job);
		return result;
	}

	@Override
	public void jobExecutionFinished(Job job) {
		// perhaps put it in another thread
		if (activeMonitor != null) {
			try {
				Object[] processParameters = getParameterMethodValues(activeMonitor,
						null);
				Method m = activeMonitor.getMethod();
				this.evaluation = (boolean) m.invoke(null, processParameters);
			} catch (Exception e) {
				this.evaluation = false;
			}
		}
	}

	@Override
	public void jobExecutionStarted(Job job) {
		// Do nothing
	}

	@Override
	public void jobExecutionException(Job job, Throwable t) {
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
		// Should run the process in the mock, check the results by activeMonitor and
		// put the results to the predictiveEvaluation.
		predictiveExecutionFinished(true);
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return job.getEvaluatedKnowledgePath(parameter, session);
	}

}
