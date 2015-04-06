package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor;
import cz.cuni.mff.d3s.irmsa.strategies.commons.EvolutionaryAdaptationManagerDelegate;
import cz.cuni.mff.d3s.irmsa.strategies.commons.Direction;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

public class AssumptionParameterAdaptationManagerDelegate implements EvolutionaryAdaptationManagerDelegate<AssumptionParameterBackup> {

	@Override
	public Set<InvariantInfo<?>> extractInvariants(
			final List<IRMInstance> irmInstances) {
		final Set<InvariantInfo<?>> infos = new HashSet<>();
		for (IRMInstance irm: irmInstances) {
			for (InvariantInstance invariant: irm.getInvariantInstances()) {
				if (invariant instanceof AssumptionInstance) {
					final AssumptionInstance ai =(AssumptionInstance) invariant;
					List<InvariantMonitor> monitors = ai.getDiagramInstance().getTraceModel().getInvariantSatisfactionMonitors();
					for (InvariantMonitor im : monitors) {
						if (im.getInvariant().equals(ai)) {
							for (Parameter p : im.getMethod().getParameters()) {
								if (p.isAnnotationPresent(AssumptionParameter.class) && ai.getComponentInstance() != null) {
									infos.add(AssumptionInfo.create(ai, im, p));
								}
							}
						}
					}
				}
			}
		}
		return infos;
	}

	@Override
	public AssumptionParameterBackup applyChanges(
			final Set<InvariantInfo<?>> adaptees, final AssumptionParameterBackup backup) {
		for (InvariantInfo<?> invariantInfo : adaptees) {
			final AssumptionInfo info = (AssumptionInfo) invariantInfo;
			final AssumptionInstance assumption = info.getInvariant();
			final AssumptionParameter assumptionParameter = info.parameter.getAnnotation(AssumptionParameter.class);
			final ReadOnlyKnowledgeManager knowledgeManager = assumption.getComponentInstance().getKnowledgeManager();
			final EMap<String, Object> data = knowledgeManager.getComponent().getInternalData();
			final String paramId = info.getParameterId();
			Object value = data.get(paramId);
			if (value == null) {
				value = assumptionParameter.defaultValue();
			}
			value = computeNewValue(value, info.parameter.getType(), info.direction, info.delta);
			data.put(paramId, value);
			final AssumptionParameterBackup.Change change = new AssumptionParameterBackup.Change(paramId, info.delta, info.direction.opposite());
			backup.parameters.put(knowledgeManager.getId(), change);
		}
		return backup;
	}

	@Override
	public long computeObserveTime(final Set<InvariantInfo<?>> adaptees,
			final Set<InvariantInfo<?>> infos) {
		final EMap<String, Object> data = ProcessContext.getCurrentProcess().getComponentInstance().getInternalData();
		final Long l = (Long) data.get(AssumptionParameterAdaptationManager.OBSERVE_TIME);
		return l;
	}

	/**
	 * Computes new parameter value.
	 * @param value current value
	 * @param resultType class of resulting class
	 * @param direction direction of change
	 * @param delta value delta
	 * @return computed value
	 */
	static private Number computeNewValue(final Object value,
			final Class<?> resultType, final Direction direction, final Number delta) {
		if (value instanceof Number) {
			Number number = (Number) value;
			if (resultType.isAssignableFrom(double.class)) {
				Double d = number.doubleValue() + direction.getCoef() * delta.doubleValue();
				return d;
			} else if (resultType.isAssignableFrom(float.class)) {
				Float f = number.floatValue() + direction.getCoef() * delta.floatValue();
				return f;
			} else if (resultType.isAssignableFrom(long.class)) {
				Long l= number.longValue() + direction.getCoef() * delta.longValue();
				return l;
			} else if (resultType.isAssignableFrom(int.class)) {
				Integer i= number.intValue() + direction.getCoef() * delta.intValue();
				return i;
			} else if (resultType.isAssignableFrom(short.class)) {
				Integer i= number.shortValue() + direction.getCoef() * delta.shortValue();
				return i.shortValue();
			} else if (resultType.isAssignableFrom(byte.class)) {
				Integer i= number.shortValue() + direction.getCoef() * delta.shortValue();
				return i.byteValue();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public void restoreBackup(final Set<InvariantInfo<?>> infos, final AssumptionParameterBackup backup) {
		for (InvariantInfo<?> ii: infos) {
			final AssumptionInfo info = (AssumptionInfo) ii;
			final AssumptionInstance assumption = info.getInvariant();
			final ReadOnlyKnowledgeManager knowledgeManager = assumption.getComponentInstance().getKnowledgeManager();
			final EMap<String, Object> data = knowledgeManager.getComponent().getInternalData();
			final String paramId = info.getParameterId();
			final AssumptionParameterBackup.Change change = backup.parameters.get(paramId);
			if (change == null) {
				continue;
			}
			Object value = data.get(paramId);
			value = computeNewValue(value, info.parameter.getType(), change.direction, change.delta);
			data.put(paramId, value);
		}
	}
}
