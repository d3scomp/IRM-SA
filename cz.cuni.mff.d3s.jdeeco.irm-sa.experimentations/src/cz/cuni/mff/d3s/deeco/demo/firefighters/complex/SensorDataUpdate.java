package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("10")
@PeriodicScheduling(period=2000)
public class SensorDataUpdate {

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
		@In("member.acceleration") Integer acceleration,  
		@In("member.oxygenLevel") Long oxygenLevel,  
		@In("member.position") Position position,  
		@In("member.temperature") Long temperature,  
		@In("member.FF_ID") String FF_ID,  
		@InOut("coord.accelerationMap") ParamHolder<Map<String,Integer>> accelerationMap,  
		@InOut("coord.oxygenLevelMap") ParamHolder<Map<String,Long>> oxygenLevelMap,  
		@InOut("coord.positionMap") ParamHolder<Map<String,Position>> positionMap,  
		@InOut("coord.temperatureMap") ParamHolder<Map<String,Long>> temperatureMap 
	) {
		System.out.println("SensorDataUpdate mapping!");
	}	
}