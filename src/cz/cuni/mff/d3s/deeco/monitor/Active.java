package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.model.LockingMode;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;

public class Active extends Monitor {

	public Active(String id, List<Parameter> parameters, Method method,
			LockingMode lockingMode) {
		super(id, parameters, method, lockingMode);
		// TODO Auto-generated constructor stub
	}
}
