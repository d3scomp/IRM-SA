package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("Officer")
public class Officer {

	public static final int TEMPERATURE_THREASHOLD = 50;
	
	// just to pass the annotation processor checks - use GL_ID instead
	public String id;

	public Integer nodeId;
	public String GL_ID;
	public Integer noOfGMsInDanger;
	public Map<String,Long> temperatureMap;
	public Map<String,Position> positionMap;
	public Map<String,Integer> accelerationMap;
	public Map<String,Long> oxygenLevelMap;
 
	public Officer(Integer nodeId, String GL_ID) {
		this.id = GL_ID;
		this.nodeId = nodeId;
		this.GL_ID = GL_ID;
		this.noOfGMsInDanger = 0;
		this.temperatureMap = new HashMap<>();
		this.positionMap = new HashMap<>();
		this.accelerationMap = new HashMap<>();
		this.oxygenLevelMap = new HashMap<>();
		
		FFSHelper.getInstance().registerOfficer(nodeId, GL_ID);
	}
 
	@Process
	@Invariant("4")
	@PeriodicScheduling(period=4000, order=8) 
	public static void findGMsInDanger(
		@In("GL_ID") String GL_ID, 
		@In("temperatureMap") Map<String,Long> temperatureMap,  
		@In("oxygenLevelMap") Map<String,Long> oxygenLevelMap,  
		@In("accelerationMap") Map<String,Integer> accelerationMap,  
		@In("positionMap") Map<String,Position> positionMap,  
		@Out("noOfGMsInDanger") ParamHolder<Integer> noOfGMsInDanger 
	) {
		//Simplification that takes into account the temperature growth.
		noOfGMsInDanger.value = 0;
		for (Long temperature: temperatureMap.values()) {
			if (temperature != null && temperature > TEMPERATURE_THREASHOLD) {
				noOfGMsInDanger.value++;
			}
		}
		if (noOfGMsInDanger.value > 0) {
			FFSHelper.getInstance().setOfficerTime(GL_ID, ClockProvider.getClock().getCurrentMilliseconds());
		}
	}
	
	@InvariantMonitor("6")
	public static boolean gmInDanger(
			@In("noOfGMsInDanger") Integer noOfGMsInDanger ) {
		return noOfGMsInDanger > 0;
	}
	
	@InvariantMonitor("8")
	public static boolean gmNotInDanger(
			@In("noOfGMsInDanger") Integer noOfGMsInDanger ) {
		return noOfGMsInDanger == 0;
	}
}
