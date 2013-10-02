package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.Invocable;
import cz.cuni.mff.d3s.deeco.model.LockingMode;
import cz.cuni.mff.d3s.deeco.model.Parameter;

public class Active extends Invocable {

	private final String id;
	
	public Active(String id, List<Parameter> parameters, Method method,
			LockingMode lockingMode) {
		super(parameters, method, lockingMode);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
