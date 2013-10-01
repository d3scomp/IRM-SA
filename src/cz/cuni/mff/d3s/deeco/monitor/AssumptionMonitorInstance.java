package cz.cuni.mff.d3s.deeco.monitor;

import java.lang.reflect.Method;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.monitoring.AssumptionMonitor;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;

public class AssumptionMonitorInstance extends MonitorInstance implements AssumptionMonitor {

	private final BooleanCondition condition;
	private Map<String, String> roleAssignment;

	public AssumptionMonitorInstance(BooleanCondition condition, KnowledgeManager km) {
		super(condition.getId(), km);
		this.condition = condition;
	}
	
	private AssumptionMonitorInstance(BooleanCondition condition, KnowledgeManager km, Map<String, String> roleAssignment) {
		super(condition.getId(), km);
		this.condition = condition;
		this.roleAssignment = roleAssignment;
	}

	public AssumptionMonitorInstance createForRoleAssignment(Map<String, String> roleAssignment) {
		AssumptionMonitorInstance result = new AssumptionMonitorInstance(condition, km, roleAssignment);
		return result;
	}

	@Override
	public boolean getEvaluation() {
		try {
			Object[] processParameters = getParameterMethodValues(condition.getParameters(),
					null);
			Method m = condition.getMethod();
			return (boolean) m.invoke(null, processParameters);
		} catch (KMNotExistentException kmnee) {
			//Log.e("AssumptionMonitorInstance", kmnee);
		} catch (Exception e) {
			Log.e("AssumptionMonitorInstance", e);
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
