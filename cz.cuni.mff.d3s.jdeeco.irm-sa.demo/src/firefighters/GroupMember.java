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
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD) 
	public static void monitorTemperatureScarcely(
			@In("id") String id, // very ugly hard-coding, just to identify the member's id
			@Out("temperature") ParamHolder<Long> temperature 
	) {
		System.out.println("monitorTemperatureScarcely");
		long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		// hard-coded scenario: M1 starts to have has high temperature at time 5999 (4th invocation of this process)
		if ((id.equals("M1")) && (simulatedTime >= 6000)) {
			if (!InDangerTimeHelper.getInstance().isSetInDangerTime()) {
				InDangerTimeHelper.getInstance().setInDangerTime(simulatedTime);
				System.out.println("GroupMember reported high temperature at " + simulatedTime);
			}
			temperature.value = 51l;
		} else {
			temperature.value = 25l;
		}
	}
 
	@Process
	@Invariant("pi2")
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD) 
	public static void monitorAcceleration(
		@Out("acceleration") ParamHolder<Integer> acceleration 
	) {
		acceleration.value = 3;
	}
 
	@Process
	@Invariant("pi3")
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD) 
	public static void monitorPositionITS(
		@Out("position") ParamHolder<Position> position
	) {
		position.value = new Position(4,4);
	}
 
	@Process
	@Invariant("pi4")
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD) 
	public static void monitorPositionGPS(
		@Out("position") ParamHolder<Position> position 
	) {
		position.value = new Position(5,5);
	}
 
	@Process
	@Invariant("pi6")
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD) 
	public static void monitorTemperatureClosely(
		@Out("temperature") ParamHolder<Long> temperature 
	) {
		System.out.println("monitorTemperatureClosely");
		temperature.value = 40l;
	}
 
	@Process
	@Invariant("pi7")
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD) 
	public static void monitorOxygenLevel(
		@Out("oxygenLevel") ParamHolder<Long> oxygenLevel 
	) {
		oxygenLevel.value = 70l;
	}
	
	@Process
	@Invariant("pi8")
	@PeriodicScheduling(period=Settings.PROCESS_PERIOD, order=5)
	public static void searchAndRescue(
			@In("id") String id, // very ugly hard-coding, just to identify the member's id
			@In("nearbyGMInDanger") Boolean nearbyGMInDanger
	) {
		long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		if (id.equals("M2") && currentTime > 5000) {
			if (nearbyGMInDanger) {
				long inDangerTime = InDangerTimeHelper.getInstance().getInDangerTime();
				long afterTime = currentTime - inDangerTime;
				// the timestamp of the first time this happens has to be logged to a file!
				System.out.println("M2 search and rescue operation started after: " + afterTime);
			} else {
				System.out.println("M2 search and rescue operation started as data is inaccurate (MAX_INACCURACY = "+NearbyGMInDangerInaccuaracy.getMaxInaccuracy()+")");
			}
			Results.getInstance().setReactionTime(currentTime);
		}
	}
	
	@InvariantMonitor("pi6") 
	public static boolean monitorTemperatureCloselyMonitor(
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger
	) {
		if (NearbyGMInDangerInaccuaracy.getInstance().isInaccurate() && InDangerTimeHelper.getInstance().isSetInDangerTime()) {
			return false;
		}
		return !nearbyGMInDanger;
	}
	
	@InvariantMonitor("pi5") 
	public static boolean monitorOxygenLevelMonitor(
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger
	) {
		if (NearbyGMInDangerInaccuaracy.getInstance().isInaccurate() && InDangerTimeHelper.getInstance().isSetInDangerTime()) {
			return false;
		}
		return !nearbyGMInDanger;
	}
	
	@InvariantMonitor("pi8") 
	public static boolean searchAndRescueMonitor(
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger,
		@In("id") String id
	) {
		if (nearbyGMInDanger) {
			return true;
		}
		if (NearbyGMInDangerInaccuaracy.getInstance().isInaccurate() && InDangerTimeHelper.getInstance().isSetInDangerTime()) {
			System.out.println("The value of the nearbyGMInDanger is inaccurate. Switching to safe mode.");
			return true;
		}
		return false;
	}
	
}
