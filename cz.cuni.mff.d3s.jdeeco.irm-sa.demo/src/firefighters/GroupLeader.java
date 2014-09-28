package firefighters;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("GroupLeader")
public class GroupLeader {

	public String id;
	public Integer noOfGMsInDanger;
	public Map<String,Long> temperatureMap;
	public Map<String,Position> positionMap;
	public Map<String,Integer> accelerationMap;
	public Map<String,Long> oxygenLevelMap;
 
	public GroupLeader(String id) {
		this.id = id;
		this.temperatureMap = new HashMap<>();
		this.positionMap = new HashMap<>();
		this.accelerationMap = new HashMap<>();
		this.oxygenLevelMap = new HashMap<>();
		this.noOfGMsInDanger = 0;
	}
 
	@Process
	@Invariant("pi1")
	@PeriodicScheduling(period=1000) 
	public static void findGMsInDanger(
		@In("temperatureMap") Map<String,Long> temperatureMap,  
		@In("oxygenLevelMap") Map<String,Long> oxygenLevelMap,  
		@In("accelerationMap") Map<String,Integer> accelerationMap,  
		@In("positionMap") Map<String,Position> positionMap,  
		@Out("noOfGMsInDanger") ParamHolder<Integer> noOfGMsInDanger 
	) {
		System.out.println("temperatureMap: " + temperatureMap);
		System.out.println("oxygenLevelMap: " + oxygenLevelMap);
		System.out.println("accelerationMap: " + accelerationMap);
		System.out.println("positionMap: " + positionMap);
		noOfGMsInDanger.value = 0;
	}
}
