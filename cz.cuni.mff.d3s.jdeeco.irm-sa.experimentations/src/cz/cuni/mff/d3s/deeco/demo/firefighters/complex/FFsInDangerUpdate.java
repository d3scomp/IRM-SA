package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("ex6")
@PeriodicScheduling(period=8000)
public class FFsInDangerUpdate {

	@Membership
	public static boolean membership(
		@In("member.GL_ID") String GL_ID 
	) {
			/*
				replace next line with actual condition
			*/
			return true;
	}

	@KnowledgeExchange
	public static void map(
		@In("member.noOfGMsInDanger") Integer noOfGMsInDanger,  
		@In("member.GL_ID") String GL_ID,  
		@Out("coord.noOfFFInDanger") ParamHolder<Map<Integer,Integer>> noOfFFInDanger 
	) {
		/*
		    Add knowledge exchange function here. 
		 */
	}	
}