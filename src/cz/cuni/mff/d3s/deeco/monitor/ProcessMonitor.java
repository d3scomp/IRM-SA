package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJob;
import cz.cuni.mff.d3s.deeco.scheduling.Job;

public class ProcessMonitor extends MonitorInstance implements Runnable, JobExecutionListener {
	protected final Active active;
	protected ComponentProcessJob job;

	private Boolean predictiveEvaluation;

	public ProcessMonitor(Active active,
			KnowledgeManager km) {
		super(active.getId(), km);
		this.active = active;
		this.predictiveEvaluation = true;
	}
	
	private ProcessMonitor(Active active,
			KnowledgeManager km, ComponentProcessJob job) {
		super(active.getId(), km);
		this.active = active;
		this.predictiveEvaluation = true;
	}
	
	public ProcessMonitor createForJob(ComponentProcessJob job) {
		ProcessMonitor result = new ProcessMonitor(active, km, job);
		return result;
	}

	@Override
	public void jobExecutionFinished(Job job) {
		// perhaps put it in another thread
		if (active != null) {
			try {
				Object[] processParameters = getParameterMethodValues(active,
						null);
				Method m = active.getMethod();
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
		// Should run the process in the mock, check the results by active and
		// put the results to the predictiveEvaluation.
		predictiveExecutionFinished(true);
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return job.getEvaluatedKnowledgePath(parameter, session);
	}

}
