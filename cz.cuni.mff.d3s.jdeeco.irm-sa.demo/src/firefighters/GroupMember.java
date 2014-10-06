package firefighters;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

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
	@PeriodicScheduling(period=5000) 
	public static void monitorTemperatureScarcely(
		@Out("temperature") ParamHolder<Long> temperature 
	) {
		System.out.println("monitorTemperatureScarcely");
		Random r = new Random();
//		temperature.value = r.nextLong();
		temperature.value = 25l;
	}
 
	@Process
	@Invariant("pi2")
	@PeriodicScheduling(period=1000) 
	public static void monitorAcceleration(
		@Out("acceleration") ParamHolder<Integer> acceleration 
	) {
		System.out.println("monitorAcceleration");
		Random r = new Random();
//		acceleration.value = r.nextInt();
		acceleration.value = 3;
	}
 
	@Process
	@Invariant("pi3")
	@PeriodicScheduling(period=1000) 
	public static void monitorPositionITS(
		@Out("position") ParamHolder<Position> position 
	) {
		System.out.println("monitorPositionITS");
		position.value = new Position(4,4);
	}
 
	@Process
	@Invariant("pi4")
	@PeriodicScheduling(period=1000) 
	public static void monitorPositionGPS(
		@Out("position") ParamHolder<Position> position 
	) {
		System.out.println("monitorPositionGPS");		
		position.value = new Position(5,5);
	}
 
	@Process
	@Invariant("pi6")
	@PeriodicScheduling(period=1000) 
	public static void monitorTemperatureClosely(
		@Out("temperature") ParamHolder<Long> temperature 
	) {
		System.out.println("monitorTemperatureClosely");
		Random r = new Random();
//		temperature.value = r.nextLong();
		temperature.value = 50l;
	}
 
	@Process
	@Invariant("pi7")
	@PeriodicScheduling(period=2000) 
	public static void monitorOxygenLevel(
		@Out("oxygenLevel") ParamHolder<Long> oxygenLevel 
	) {
		System.out.println("monitorOxygenLevel");
		Random r = new Random();
//		oxygenLevel.value = r.nextLong();
		oxygenLevel.value = 70l;
	}
	
}
