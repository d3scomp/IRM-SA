package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;

public class AssumptionMonitor extends MonitorInstance {

	private final BooleanCondition condition;
	private Map<String, String> roleAssignment;

	public AssumptionMonitor(BooleanCondition condition, KnowledgeManager km) {
		super(condition.getId(), km);
		this.condition = condition;
	}
	
	private AssumptionMonitor(BooleanCondition condition, KnowledgeManager km, Map<String, String> roleAssignment) {
		super(condition.getId(), km);
		this.condition = condition;
		this.roleAssignment = roleAssignment;
	}

	public AssumptionMonitor createForRoleAssignment(Map<String, String> roleAssignment) {
		AssumptionMonitor result = new AssumptionMonitor(condition, km, roleAssignment);
		return result;
	}

	@Override
	public boolean getEvaluation() {
		try {
			Object[] processParameters = getParameterMethodValues(condition,
					null);
			Method m = condition.getMethod();
			return (boolean) m.invoke(null, processParameters);
		} catch (Exception e) {
			Log.e("AssumptionMonitor", e);
		}
		return false;
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return parameter.getKnowledgePath().getEvaluatedPath(km,
				roleAssignment, session);
	}
}
