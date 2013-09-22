package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.deeco.am.AdaptationManager;
import cz.cuni.mff.d3s.deeco.monitor.ProcessEnsembleMonitoring;
import cz.cuni.mff.d3s.deeco.monitor.ProcessEnsembleMonitoringProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.PeriodicSchedule;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public class AdaptationRealTimeScheduler extends RealTimeScheduler implements ProcessEnsembleMonitoringProvider {

	private AdaptationManager am;
	//Temporal solution for separation reasons.
	//Should be changed - possibly each monitor in relevant job
	private final Map<String, ProcessEnsembleMonitoring> processEnsembleMonitoring;
	
	public AdaptationRealTimeScheduler(AdaptationManager am) {
		this.am = am;
		this.am.setScheduler(this);
		this.processEnsembleMonitoring = new HashMap<String, ProcessEnsembleMonitoring>();
	}
	
	@Override
	public void jobExecutionFinished(Job job) {
		processEnsembleMonitoring.get(job.getInstanceId()).jobExecutionFinished(job);
		Schedule schedule = job.getSchedule();
		if (schedule instanceof PeriodicSchedule) {
			executor.schedule(job, ((PeriodicSchedule) schedule).getPeriod(),
					TimeUnit.MILLISECONDS);
		}
	}
	
	@Override
	public void jobExecutionException(Job job, Throwable t) {
		processEnsembleMonitoring.get(job.getInstanceId()).jobExecutionException(job, t);
	}
	
	@Override
	public void jobExecutionStarted(Job job) {
		processEnsembleMonitoring.get(job.getInstanceId()).jobExecutionStarted(job);
	}
	
	
	@Override
	public void schedule(Job job) {
		if (executor != null) {
			if (!processEnsembleMonitoring.containsKey(job.getInstanceId()))
				processEnsembleMonitoring.put(job.getInstanceId(), am.createMonitoringFor(job));
			executor.schedule(job, 0, TimeUnit.MILLISECONDS);
		}
	}
	
	public void scheduleRunnable(Runnable runnable, long delay) {
		executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public ProcessEnsembleMonitoring getMonitoring(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
