package cz.cuni.mff.d3s.deeco.monitor;

import static cz.cuni.mff.d3s.deeco.processor.ConditionParser.parseBooleanConditions;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;

public class AssumptionParser {
	public static List<AssumptionMonitor> extractEnsembleProcess(Class<?> c,
			KnowledgeManager km) {
		List<AssumptionMonitor> result = new LinkedList<>();
		List<BooleanCondition> conditions = parseBooleanConditions(c);
		for (BooleanCondition condition : conditions) {
			result.add(new AssumptionMonitor(condition, km));
		}
		return result;
	}
}
