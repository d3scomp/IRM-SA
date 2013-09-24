package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.deeco.am.AdaptationManager;
import cz.cuni.mff.d3s.deeco.runtime.model.PeriodicSchedule;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public class AdaptationRealTimeScheduler extends RealTimeScheduler {

	private AdaptationManager am;

	public AdaptationRealTimeScheduler(AdaptationManager am) {
		this.am = am;
		this.am.setScheduler(this);
	}

	@Override
	public void jobExecutionFinished(Job job) {
		Schedule schedule = job.getSchedule();
		if (schedule instanceof PeriodicSchedule) {
			job.setCancelExecution(!am.isToBeScheduled(job));
				executor.schedule(job,
						((PeriodicSchedule) schedule).getPeriod(),
						TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void jobExecutionException(Job job, Throwable t) {
		// do nothing
	}

	@Override
	public void jobExecutionStarted(Job job) {
		// do nothing
	}

	@Override
	public void schedule(Job job) {
		if (executor != null) {
			job.setCancelExecution(!am.isToBeScheduled(job));
			executor.schedule(job, 0, TimeUnit.MILLISECONDS);
		}
	}

}
