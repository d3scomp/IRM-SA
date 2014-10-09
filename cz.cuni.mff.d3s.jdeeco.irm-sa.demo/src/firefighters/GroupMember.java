package firefighters;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

@Component
@IRMComponent("GroupMember")
public class GroupMember {

	public String id;
	public Long temperature;
	public Integer acceleration;
	public Position position;
	public Long oxygenLevel;
	public String leaderId;
	public Boolean nearbyGMInDanger;
	public Boolean alarmOn;
 
	public GroupMember(String id, String leaderId) {
		this.id = id;
		this.leaderId = leaderId;
		this.nearbyGMInDanger = false;
	}
 
	@Process
	@Invariant("pi5")
	@PeriodicScheduling(period=2000) 
	public static void monitorTemperatureScarcely(
			@In("id") String id, // very ugly hard-coding, just to identify the member's id
		@Out("temperature") ParamHolder<Long> temperature 
	) {
		System.out.println("monitorTemperatureScarcely");
		long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		// hard-coded scenario: M1 starts to have has high temperature at time 5999 (4th invocation of this process)
		if ((id.equals("M1")) && (simulatedTime >= 5999)) {
			// the timestamp of the first time this happens has to be logged to a file!
			System.out.println("M1 surpassed temperature threshold (50) at : " + simulatedTime);
			temperature.value = 51l;
		} else {
			temperature.value = 25l;
		}
	}
 
	@Process
	@Invariant("pi2")
	@PeriodicScheduling(period=1000) 
	public static void monitorAcceleration(
		@Out("acceleration") ParamHolder<Integer> acceleration 
	) {
		acceleration.value = 3;
	}
 
	@Process
	@Invariant("pi3")
	@PeriodicScheduling(period=1000) 
	public static void monitorPositionITS(
		@Out("position") ParamHolder<Position> position
	) {
		position.value = new Position(4,4);
	}
 
	@Process
	@Invariant("pi4")
	@PeriodicScheduling(period=1000) 
	public static void monitorPositionGPS(
		@Out("position") ParamHolder<Position> position 
	) {
		position.value = new Position(5,5);
	}
 
	@Process
	@Invariant("pi6")
	@PeriodicScheduling(period=1000) 
	public static void monitorTemperatureClosely(
		@Out("temperature") ParamHolder<Long> temperature 
	) {
		System.out.println("monitorTemperatureClosely");
		temperature.value = 50l;
	}
 
	@Process
	@Invariant("pi7")
	@PeriodicScheduling(period=2000) 
	public static void monitorOxygenLevel(
		@Out("oxygenLevel") ParamHolder<Long> oxygenLevel 
	) {
		oxygenLevel.value = 70l;
	}
	
	@Process
	@Invariant("pi8")
	@PeriodicScheduling(period=1000)
	public static void searchAndRescue(
			@In("id") String id, // very ugly hard-coding, just to identify the member's id
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger 
	) {
		long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		if (nearbyGMInDanger && (id.equals("M2"))) {
			// the timestamp of the first time this happens has to be logged to a file!
			System.out.println("M2 search and rescue operation started at time : " + simulatedTime);	
		}
	}
	
	@InvariantMonitor("pi6") 
	public static boolean monitorTemperatureCloselyMonitor(
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger
	) {
		return !nearbyGMInDanger;
	}
	
	@InvariantMonitor("pi5") 
	public static boolean monitorOxygenLevelMonitor(
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger
	) {
		return !nearbyGMInDanger;
	}
	
	@InvariantMonitor("pi8") 
	public static boolean searchAndRescueMonitor(
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger
	) {
		return nearbyGMInDanger;
	}
	
}
