package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("Officer")
public class Officer {

	// just to pass the annotation processor checks - use GL_ID instead
	public String id;

	public String GL_ID;
	public Integer noOfGMsInDanger;
	public Map<String,Long> temperatureMap;
	public Map<String,Position> positionMap;
	public Map<String,Integer> accelerationMap;
	public Map<String,Long> oxygenLevelMap;
 
	public Officer() {
		/* 
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("4")
	@PeriodicScheduling(period=4000) 
	public static void findGMsInDanger(
		@In("temperatureMap") Map<String,Long> temperatureMap,  
		@In("oxygenLevelMap") Map<String,Long> oxygenLevelMap,  
		@In("accelerationMap") Map<String,Integer> accelerationMap,  
		@In("positionMap") Map<String,Position> positionMap,  
		@Out("noOfGMsInDanger") ParamHolder<Integer> noOfGMsInDanger 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
}
