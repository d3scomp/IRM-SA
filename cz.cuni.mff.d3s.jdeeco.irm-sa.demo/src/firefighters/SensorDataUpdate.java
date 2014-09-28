package firefighters;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("ei2")
@PeriodicScheduling(period=2000)
public class SensorDataUpdate {

	@Membership
	public static boolean membership(
		@In("member.leaderId") String leaderId,
		@In("coord.id") String id
	) {
		if (leaderId == id) {
			System.out.println("SensorDataUpdate ensemble formed...");
			return true;
		} else {
			return false;
		}
	}

	@KnowledgeExchange
	public static void map(
		@In("member.id") String id,
		@In("member.acceleration") Integer acceleration,  
		@In("member.oxygenLevel") Long oxygenLevel,  
		@In("member.position") Position position,  
		@In("member.temperature") Long temperature,  
		@InOut("coord.accelerationMap") ParamHolder<Map<String,Integer>> accelerationMap,  
		@InOut("coord.oxygenLevelMap") ParamHolder<Map<String,Long>> oxygenLevelMap,  
		@InOut("coord.positionMap") ParamHolder<Map<String,Position>> positionMap,  
		@InOut("coord.temperatureMap") ParamHolder<Map<String,Long>> temperatureMap 
	) {
		accelerationMap.value.put(id, acceleration);
		oxygenLevelMap.value.put(id, oxygenLevel);
		positionMap.value.put(id, position);
		temperatureMap.value.put(id, temperature);
	}	
}