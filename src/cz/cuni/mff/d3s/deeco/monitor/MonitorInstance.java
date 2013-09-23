package cz.cuni.mff.d3s.deeco.monitor;


import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.monitoring.Monitor;
import cz.cuni.mff.d3s.deeco.scheduling.ParametrizedInstance;

public abstract class MonitorInstance extends ParametrizedInstance implements Monitor {
	protected final String id;
	protected Boolean evaluation;
	
	public MonitorInstance(String id, KnowledgeManager km) {
		super(km);
		this.id = id;
		this.evaluation = true;
	}
	
	public String getId() {
		return id;
	}
}
