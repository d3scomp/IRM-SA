package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.scheduling.AdaptationRealTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJob;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleJob;
import cz.cuni.mff.d3s.deeco.scheduling.Job;
import cz.cuni.mff.d3s.deeco.scheduling.ParametrizedInstance;

public class ProcessEnsembleMonitoring implements JobExecutionListener {
	protected final Active active;
	protected final Predictive predictive;
	protected final AdaptationRealTimeScheduler scheduler;
	protected final Job job;
	protected final KnowledgeManager km;

	private Boolean evaluation;

	public ProcessEnsembleMonitoring(Active active, Predictive predictive, Job job,
			AdaptationRealTimeScheduler scheduler, KnowledgeManager km) {
		this.km = km;
		this.active = active;
		this.predictive = predictive;
		this.scheduler = scheduler;
		this.job = job;
		this.evaluation = true;
	}

	@Override
	public void jobExecutionFinished(Job job) {
		if (job instanceof ComponentProcessJob) {
			if (evaluation) {
				if (active != null)
					scheduler.scheduleRunnable(new MonitorTask(active, km), 0);
			}
		} else if (job instanceof EnsembleJob) {
			// do nothing as
		}
		// Depending if the job is ensemble or component process job:
		// Verify by calling active (if possible - ensembles have only
		// predictive monitoring)
		// if evaluation is false then call periodically predictive until the
		// evaluation results in true.
	}

	@Override
	public void jobExecutionStarted(Job job) {
		// Do nothing
	}

	@Override
	public void jobExecutionException(Job job, Throwable t) {
		synchronized (evaluation) {
			if (evaluation) {
				if (predictive != null) {
					scheduler.scheduleRunnable(new MonitorTask(predictive, km),
							predictive.getPeriod());
					evaluation = false;
				}
			}
		}
	}

	public boolean getEvaluation() {
		return evaluation;
	}

	private void monitorExecutionFinished(boolean result, Monitor monitor) {
		synchronized (evaluation) {
			evaluation = result;
			if (!evaluation) {
				if (predictive != null)
					scheduler.scheduleRunnable(new MonitorTask(predictive, km),
							predictive.getPeriod());
			}
		}
	}

	private class MonitorTask extends ParametrizedInstance implements Runnable {

		private Monitor monitor;

		public MonitorTask(Monitor monitor, KnowledgeManager km) {
			super(km);
			this.monitor = monitor;
		}

		@Override
		public String getEvaluatedKnowledgePath(Parameter parameter,
				ISession session) {
			return job.getEvaluatedKnowledgePath(parameter, session);
		}

		@Override
		public void run() {
			boolean result = true;
			try {
				Object[] processParameters = getParameterMethodValues(monitor,
						null);
				Method m = monitor.getMethod();
				result = (boolean) m.invoke(null, processParameters);
			} catch (Exception e) {
				result = false;
			}
			monitorExecutionFinished(result, monitor);
		}

	}

}
