package cz.cuni.mff.d3s.deeco.assumptions;

import java.lang.reflect.Method;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.irm.Evaluable;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.scheduling.ParametrizedInstance;

public class AssumptionMonitoring extends ParametrizedInstance implements
		Evaluable {

	private final BooleanCondition condition;
	private Map<String, String> assignedRoles;

	public AssumptionMonitoring(BooleanCondition condition, KnowledgeManager km) {
		super(km);
		this.condition = condition;
	}

	@Override
	public boolean evaluate(Map<String, String> assignedRoles) {
		this.assignedRoles = assignedRoles;
		try {
		Object[] processParameters = getParameterMethodValues(condition,
				null);
		Method m = condition.getMethod();
		return (boolean) m.invoke(null, processParameters);
		} catch (Exception e) {
			Log.e("AssumptionMonitoring", e);
		}
		return false;
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return parameter.getKnowledgePath().getEvaluatedPath(km, assignedRoles,
				session);
	}

}
