package irm.test;

import cz.cuni.mff.d3s.deeco.annotations.Condition;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.definitions.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

public class GMInDanger extends EnsembleDefinition {
	@Condition
	public static boolean membership(
			@In("coord.groupLeaderId") String gmGLId,
			@In("member.id") String leaderId) {
		return gmGLId.equals(leaderId);
	}

	@KnowledgeExchange("8")
	public static void map(
			@Out("coord.nearbyGMInDanger") OutWrapper<Boolean> gmGMInDanger,
			@In("member.gmInDanger") Boolean glGMInDanger) {
		System.out.println("GMInDanger");
		gmGMInDanger.value = glGMInDanger;
	}
}
