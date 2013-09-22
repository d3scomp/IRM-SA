package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.deeco.am.AdaptationManager;
import cz.cuni.mff.d3s.deeco.monitor.Monitoring;
import cz.cuni.mff.d3s.deeco.monitor.MonitoringProvider;
import cz.cuni.mff.d3s.deeco.runtime.model.PeriodicSchedule;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public class AdaptationRealTimeScheduler extends RealTimeScheduler implements MonitoringProvider {

	private AdaptationManager am;
	//Temporal solution for separation reasons.
	//Should be changed - possibly each monitor in relevant job
	private final Map<String, Monitoring> monitoring;
	
	public AdaptationRealTimeScheduler(AdaptationManager am) {
		this.am = am;
		this.am.setScheduler(this);
		this.monitoring = new HashMap<String, Monitoring>();
	}
	
	@Override
	public void jobExecutionFinished(Job job) {
		monitoring.get(job.getInstanceId()).jobExecutionFinished(job);
		Schedule schedule = job.getSchedule();
		if (schedule instanceof PeriodicSchedule) {
			executor.schedule(job, ((PeriodicSchedule) schedule).getPeriod(),
					TimeUnit.MILLISECONDS);
		}
	}
	
	@Override
	public void jobExecutionException(Job job, Throwable t) {
		monitoring.get(job.getInstanceId()).jobExecutionException(job, t);
	}
	
	@Override
	public void jobExecutionStarted(Job job) {
		monitoring.get(job.getInstanceId()).jobExecutionStarted(job);
	}
	
	
	@Override
	public void schedule(Job job) {
		if (executor != null) {
			if (!monitoring.containsKey(job.getInstanceId()))
				monitoring.put(job.getInstanceId(), am.createMonitoringFor(job));
			executor.schedule(job, 0, TimeUnit.MILLISECONDS);
		}
	}
	
	public void scheduleRunnable(Runnable runnable, long delay) {
		executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public Monitoring getMonitoring(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
