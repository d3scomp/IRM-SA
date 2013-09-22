package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.runtime.model.Invocable;
import cz.cuni.mff.d3s.deeco.runtime.model.LockingMode;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;

public abstract class Monitor extends Invocable {

	private final String id;
	
	public Monitor(String id, List<Parameter> parameters, Method method,
			LockingMode lockingMode) {
		super(parameters, method, lockingMode);
		this.id = id;
	}

}
