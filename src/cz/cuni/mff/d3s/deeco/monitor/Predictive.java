package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.model.LockingMode;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;

public class Predictive extends Monitor {

	private final long period;
	
	public Predictive(long period, String id, List<Parameter> parameters, Method method,
			LockingMode lockingMode) {
		super(id, parameters, method, lockingMode);
		this.period = period;
	}
	
	public long getPeriod() {
		return period;
	}

}
