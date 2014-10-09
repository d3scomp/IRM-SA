package firefighters;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

@Ensemble
@Invariant("ei1")
@PeriodicScheduling(period=3000)
public class GMsInDangerUpdate {

	@Membership
	public static boolean membership(
		@In("member.leaderId") String leaderId,  
		@In("coord.id") String id 
	) {
		return leaderId.equals(id);
	}

	@KnowledgeExchange
	public static void map(
		@Out("member.nearbyGMInDanger") ParamHolder<Boolean> nearbyGMInDanger,  
		@In("coord.noOfGMsInDanger") Integer noOfGMsInDanger 
	) {
		nearbyGMInDanger.value = noOfGMsInDanger > 0;
		if (nearbyGMInDanger.value) {
			long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
			System.out.println("nearbyGMInDanger signal propagated to GroupMember at time : " + simulatedTime);	
		}
		
	}	
}