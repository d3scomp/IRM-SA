package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("7")
@PeriodicScheduling(period=Settings.ENSEMBLE_PERIOD)
public class GMsInDangerUpdate {

	@Membership
	public static boolean membership(
		@In("member.leaderId") String leaderId,  
		@In("coord.GL_ID") String GL_ID 
	) {
			return leaderId.equals(GL_ID);
	}

	@KnowledgeExchange
	public static void map(
		@Out("member.nearbyGMInDanger") ParamHolder<Boolean> nearbyGMInDanger,  
		@In("coord.noOfGMsInDanger") Integer noOfGMsInDanger 
	) {
		System.out.println("GMInDangerUpdate!");
		nearbyGMInDanger.value = noOfGMsInDanger > 0;
	}	
}