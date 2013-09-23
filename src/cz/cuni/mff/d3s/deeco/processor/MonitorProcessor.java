package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotatedMethods;
import static cz.cuni.mff.d3s.deeco.processor.ParserHelper.getParameterList;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.ActiveMonitor;
import cz.cuni.mff.d3s.deeco.exceptions.ParametersParseException;
import cz.cuni.mff.d3s.deeco.monitor.Active;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.runtime.model.LockingMode;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;

public class MonitorProcessor {
	// public static List<Monitor> extractMonitors(Class<?> c) throws
	// ParseException {
	//
	// assert (c != null);
	//
	// List<Method> methods = getAnnotatedMethods(c, ActiveMonitor.class);
	// methods.addAll(getAnnotatedMethods(c, PredictiveMonitor.class));
	//
	// final List<Monitor> result = new LinkedList<>();
	//
	// if (methods == null || methods.size() == 0) {
	// return result;
	// }
	//
	// LockingMode lockingMode;
	// List<Parameter> parameters;
	// ActiveMonitor am;
	// PredictiveMonitor pm;
	// for (Method m : methods) {
	// try {
	// parameters = getParameterList(m);
	// } catch (ParametersParseException cepe) {
	// throw new ParseException(c.getName()
	// + ": Parameters for the method " + m.getName()
	// + " cannot be parsed.");
	// }
	//
	// lockingMode = LockingMode.WEAK;
	// am = m.getAnnotation(ActiveMonitor.class);
	// if (am == null) {
	// pm = m.getAnnotation(PredictiveMonitor.class);
	// if (pm == null)
	// continue;
	// else
	// result.add(new Predictive(pm.period(), pm.value(), parameters, m,
	// lockingMode));
	// } else {
	// result.add(new Active(am.value(), parameters, m,
	// lockingMode));
	// }
	// }
	// return result;
	// }

	public static List<Active> extractMonitors(Class<?> c)
			throws ParseException {

		assert (c != null);

		List<Method> methods = getAnnotatedMethods(c, ActiveMonitor.class);

		final List<Active> result = new LinkedList<>();

		if (methods == null || methods.size() == 0) {
			return result;
		}

		LockingMode lockingMode;
		List<Parameter> parameters;
		ActiveMonitor am;
		for (Method m : methods) {
			try {
				parameters = getParameterList(m);
			} catch (ParametersParseException cepe) {
				throw new ParseException(c.getName()
						+ ": Parameters for the method " + m.getName()
						+ " cannot be parsed.");
			}

			lockingMode = LockingMode.WEAK;
			am = m.getAnnotation(ActiveMonitor.class);
			result.add(new Active(am.value(), parameters, m, lockingMode));
		}
		return result;
	}
}
