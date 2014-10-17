package firefighters;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

@Component
@IRMComponent("GroupLeader")
public class GroupLeader {

	public String id;
	public Integer noOfGMsInDanger;
	public Map<String,SensorReadings> sensorReadings;
 
	public GroupLeader(String id) {
		this.id = id;
		this.sensorReadings = new HashMap<>();
		this.noOfGMsInDanger = 0;
	}
 
	@Process
	@Invariant("pi1")
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD) 
	public static void findGMsInDanger(
		@In("sensorReadings") Map<String,SensorReadings> membersSensorReadings,   
		@InOut("noOfGMsInDanger") ParamHolder<Integer> noOfGMsInDanger
	) {
//		System.out.println("temperatureMap: " + temperatureMap);
//		System.out.println("oxygenLevelMap: " + oxygenLevelMap);
//		System.out.println("accelerationMap: " + accelerationMap);
//		System.out.println("positionMap: " + positionMap);
//		System.out.println("noOfGMsInDanger: " + noOfGMsInDanger.value);
		int FFinDanger = 0;
		SensorReadings readings;
		for (String mId : membersSensorReadings.keySet()) {
			readings = membersSensorReadings.get(mId);
			if ((readings.temperature!=null) && (readings.temperature > 50)) {
				FFinDanger++;
			}
		}
		noOfGMsInDanger.value = FFinDanger;
		long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		if (noOfGMsInDanger.value > 0) {
			System.out.println("GMsInDanger determined on the GroupLeader's side at time : " + currentTime);
			
		}
		if (InDangerTimeHelper.getInstance().isSetInDangerTime()) {
			NearbyGMInDangerInaccuaracy.getInstance().setCreationTime(currentTime);
		}
	}
}
