package deeco.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

/**
 * This class is just an experiment. Kept here for reference. 
 * 
 * @author Ilias
 *
 */
@Ensemble
@PeriodicScheduling(period = 51)
public class GroupMemberDataAggregationOld {

	@Membership
	public static boolean membership(
			@In("member.id") String memberId,
			@In("member.battery") Integer battery, // just to rule out GroupLeaders
			@In("coord.knowledgeHistoryOfAllComponents") Map<Integer, Map<String, List<Object>>> knowledgeHistoryOfAllComponents) {
		return true;
	}

	@KnowledgeExchange
	public static void map(
			@In("member.id") String memberId,
			@In("member.battery") Integer battery, // just to rule out GroupLeaders
			@InOut("coord.knowledgeHistoryOfAllComponents") ParamHolder<Map<Integer, Map<String, List<Object>>>> knowledgeHistoryOfAllComponents) throws KnowledgeNotFoundException {

		System.out.println("KnowledgeExchange for component " + memberId); 
		ComponentInstance i = (ComponentInstance) ProcessContext.getCurrentProcess().eContainer();
		KnowledgeManager km = i.getKnowledgeManager();
		
		if (!km.getId().equals(memberId)) {
			return;
		}
		
		Collection<KnowledgePath> paths = getKnowledgePaths();
		ValueSet valueset = null;
		valueset = km.get(paths);
		
		// FIXME hard-coded conversion of id to Integer
		Integer memberIdInt = Integer.parseInt(memberId);
		Map<String, List<Object>> memberKnowledgeHistory = knowledgeHistoryOfAllComponents.value.get(memberIdInt);
		if (memberKnowledgeHistory == null) {
			memberKnowledgeHistory = new HashMap<>();
		}
		
		for (KnowledgePath path : paths) {
			// FIXME hard-coded casting to Integer because we know all the data are Integers
			Integer value = ((Integer) valueset.getValue(path));
			// FIXME hard-coded: getting the first element and casting to PathNodeField			
			String field = ((PathNodeField) path.getNodes().get(0)).getName();
			List<Object> fieldHistory = memberKnowledgeHistory.get(field);
			if (fieldHistory == null) {
				fieldHistory = new ArrayList<>();
			}
			fieldHistory.add(value);
			
			memberKnowledgeHistory.put(field, fieldHistory);
		}
		
		knowledgeHistoryOfAllComponents.value.put(memberIdInt, memberKnowledgeHistory);
		
	}

	/*
	 * FIXME Paths are hard-coded for now.
	 * We should implement a utility method getAll() in the KnowledgeManger.
	 */
	private static Collection<KnowledgePath> getKnowledgePaths() {
		Collection<KnowledgePath> paths = new HashSet<KnowledgePath>();
		
		paths.add(getField("position"));
		paths.add(getField("temperature"));
		paths.add(getField("battery"));

		return paths;
	}

	static KnowledgePath getField(String fieldName) {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		KnowledgePath path = factory.createKnowledgePath();
		PathNodeField field = factory.createPathNodeField();
		field.setName(fieldName);
		path.getNodes().add(field);
		return path;
	}

}