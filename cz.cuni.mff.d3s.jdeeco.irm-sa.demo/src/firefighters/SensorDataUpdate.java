package firefighters;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

@Ensemble
@Invariant("ei2")
@PeriodicScheduling(period=Settings.ENSEMBLE_PERIOD)
public class SensorDataUpdate {

	@Membership
	public static boolean membership(
		@In("member.leaderId") String leaderId,
		@In("coord.id") String id
	) {
		return leaderId.equals(id);
	}

	@KnowledgeExchange
	public static void map(
		@In("member.id") String id,
		@In("member.acceleration") Integer acceleration,  
		@In("member.oxygenLevel") Long oxygenLevel,  
		@In("member.position") Position position,  
		@In("member.temperature") Long temperature,  
		@InOut("coord.sensorReadings") ParamHolder<Map<String,SensorReadings>> sensorReadings
	) {
		System.out.println("Ensemble at "+id+" fired at " + ProcessContext.getTimeProvider().getCurrentMilliseconds() + " temperature " + temperature);
		sensorReadings.value.put(id, new SensorReadings(temperature, position, acceleration, oxygenLevel));
	}	
}