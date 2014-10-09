package firefighters;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.demo.vehicles.simple.Availability;
import cz.cuni.mff.d3s.deeco.demo.vehicles.simple.POI;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

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
		@InOut("noOfGMsInDanger") ParamHolder<Integer> noOfGMsInDanger 
	) {
//		System.out.println("temperatureMap: " + temperatureMap);
//		System.out.println("oxygenLevelMap: " + oxygenLevelMap);
//		System.out.println("accelerationMap: " + accelerationMap);
//		System.out.println("positionMap: " + positionMap);
//		System.out.println("noOfGMsInDanger: " + noOfGMsInDanger.value);
		
		int FFinDanger = 0;
		Collection<Long> temperatures = temperatureMap.values();	
		for (Long temperature : temperatures) {
			if ((temperature!=null) && (temperature > 50)) {
				FFinDanger++;
			}
		}
		noOfGMsInDanger.value = FFinDanger;
		if (noOfGMsInDanger.value > 0) {
			System.out.println("GMsInDanger determined on the GroupLeader's side at time : " + ProcessContext.getTimeProvider().getCurrentMilliseconds());
		}
	}
}
