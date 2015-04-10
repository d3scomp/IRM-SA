package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("7")
@PeriodicScheduling(period=3000)
public class GMsInDangerUpdate {

	@Membership
	public static boolean membership(
		@In("member.leaderId") String leaderId,  
		@In("coord.GL_ID") String GL_ID 
	) {
			/*
				replace next line with actual condition
			*/
			return true;
	}

	@KnowledgeExchange
	public static void map(
		@Out("member.nearbyGMInDanger") ParamHolder<Boolean> nearbyGMInDanger,  
		@In("coord.noOfGMsInDanger") Integer noOfGMsInDanger 
	) {
		/*
		    Add knowledge exchange function here. 
		 */
	}	
}