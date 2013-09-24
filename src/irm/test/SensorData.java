package irm.test;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Condition;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.definitions.EnsembleDefinition;

public class SensorData extends EnsembleDefinition {
	@Condition
	public static boolean membership(
			@In("member.groupLeaderId") String gmGLId,
			@In("coord.id") String leaderId) {
		return gmGLId.equals(leaderId);
	}

	@KnowledgeExchange("12")
	public static void map(
			@In("member.temperature") Integer mTemperature,
			@InOut("coord.sensorDataList") List<Integer> sensorData) {
		System.out.println("SensorData");
		sensorData.clear();
		sensorData.add(mTemperature);
	}
}
