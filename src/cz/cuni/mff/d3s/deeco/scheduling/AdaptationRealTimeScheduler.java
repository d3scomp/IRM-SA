package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.am.AdaptationManager;
import cz.cuni.mff.d3s.deeco.executor.TaskExecutor;
import cz.cuni.mff.d3s.deeco.executor.ThreadPoolTaskExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

public class AdaptationRealTimeScheduler extends RealTimeScheduler {

	private final AdaptationManager am;

	public AdaptationRealTimeScheduler(TaskExecutor executor,
			KnowledgeManager km, AdaptationManager am) {
		super(executor, km);
		this.am = am;
	}

	public AdaptationRealTimeScheduler(KnowledgeManager km, AdaptationManager am) {
		this(new ThreadPoolTaskExecutor(km), km, am);
	}
	
	public AdaptationRealTimeScheduler(AdaptationManager am) {
		this(null, am);
	}

	@Override
	protected void execute(Task task) {
		if (am.isToBeScheduled(task))
			super.execute(task);
	}

}
