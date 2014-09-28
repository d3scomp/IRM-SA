package firefighters;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("ei1")
@PeriodicScheduling(period=2000)
public class GMsInDangerUpdate {

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
		@Out("member.nearbyGMInDanger") ParamHolder<Boolean> nearbyGMInDanger,  
		@In("coord.noOfGMsInDanger") Integer noOfGMsInDanger 
	) {
		nearbyGMInDanger.value = noOfGMsInDanger > 0;
	}	
}