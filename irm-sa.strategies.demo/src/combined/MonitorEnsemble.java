package combined;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

/**
 * Auxiliary ensemble collecting data from FF1 to Monitor component.
 */
@Ensemble
@PeriodicScheduling(period = 100, order = 19)
public class MonitorEnsemble {

	@Membership
	public static boolean membership(
		@In("member.id") String id,
		@In("member.temperature") MetadataWrapper<Integer> memberTemperature, //only firefighters
		@In("coord.thoughtTemperature") MetadataWrapper<Integer> thoughtTemperature) { //only monitor
		return id.equals(Environment.FF_LEADER_ID);
	}

	@KnowledgeExchange
	public static void map(
			@In("member.id") String id,
			@Out("coord.thoughtTemperature") ParamHolder<MetadataWrapper<Integer>> thoughtTemperature,
			@In("member.temperature") MetadataWrapper<Integer> temperature) throws KnowledgeNotFoundException {
		thoughtTemperature.value = temperature;
		System.out.println(String.format("temperature exchange: %d : %d", temperature.getTimestamp(), temperature.getValue()));
	}
}
