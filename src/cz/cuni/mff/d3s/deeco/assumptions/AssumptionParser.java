package cz.cuni.mff.d3s.deeco.assumptions;

import static cz.cuni.mff.d3s.deeco.processor.ConditionParser.parseBooleanConditions;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;

public class AssumptionParser {
	public static List<AssumptionMonitoring> extractEnsembleProcess(Class<?> c, KnowledgeManager km)
			throws ParseException {
		List<AssumptionMonitoring> result = new LinkedList<>();
		List<BooleanCondition> conditions = parseBooleanConditions(c);
		for (BooleanCondition condition : conditions) {
			result.add(new AssumptionMonitoring(condition, km));
		}
		return result;
	}
}
