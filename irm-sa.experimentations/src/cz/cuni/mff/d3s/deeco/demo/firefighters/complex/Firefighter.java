package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("Firefighter")
public class Firefighter {

	// just to pass the annotation processor checks - use FF_ID instead
	public String id;

	public Integer nodeId;
	public Integer team;
	public String FF_ID;
	public Long temperature;
	public Integer acceleration;
	public Position position;
	public Long oxygenLevel;
	public String leaderId;
	public Boolean nearbyGMInDanger;
	public Boolean alarmOn;

	public Firefighter(Integer nodeId, String FF_ID, String GL_ID) {
		this.id = FF_ID;
		this.nodeId = nodeId;
		this.FF_ID = FF_ID;
		this.leaderId = GL_ID;
		this.temperature = 25L;
		this.acceleration = 30;
		this.position = new Position(34, 23);
		this.oxygenLevel = 90L;
		this.nearbyGMInDanger = false;
		this.alarmOn = false;
		
		FFSHelper.getInstance().registerFF(nodeId, GL_ID);
	}

	@Process
	@Invariant("17")
	@PeriodicScheduling(period = 5000, order=7)
	public static void monitorTemperatureScarcely(
			@In("FF_ID") String FF_ID,
			@In("leaderId") String leaderId,
			@In("nodeId") Integer nodeId,
			@InOut("temperature") ParamHolder<Long> temperature) {
		if (FFSHelper.getInstance().executeDangerSituation(FF_ID, nodeId, leaderId, ClockProvider.getClock().getCurrentMilliseconds())) {
			temperature.value = 75L;
			System.out.println(FF_ID + " in Danger at: " + ClockProvider.getClock().getCurrentMilliseconds());
		}
	}

	@Process
	@Invariant("11")
	@PeriodicScheduling(period = 1000, order=6)
	public static void monitorAcceleration(
			@Out("acceleration") ParamHolder<Integer> acceleration) {
		acceleration.value = 3;
	}

	@Process
	@Invariant("25")
	@PeriodicScheduling(period = 1000, order=5)
	public static void monitorPositionITS(
			@Out("position") ParamHolder<Position> position) {
		position.value = new Position(1, 1);
	}

	@Process
	@Invariant("27")
	@PeriodicScheduling(period = 1000, order=4)
	public static void monitorPositionGPS(
			@Out("position") ParamHolder<Position> position) {
		position.value = new Position(5, 5);
	}

	@Process
	@Invariant("15")
	@PeriodicScheduling(period = 1000, order=3)
	public static void monitorTemperatureClosely(
			@InOut("temperature") ParamHolder<Long> temperature) {
		//System.out.println("monitorTemperatureClosely");
	}

	@Process
	@Invariant("30")
	@PeriodicScheduling(period = 2000, order=2)
	public static void monitorOxygenLevel(
			@Out("oxygenLevel") ParamHolder<Long> oxygenLevel) {
		oxygenLevel.value = 70l;
	}

	@Process
	@Invariant("SOS")
	@PeriodicScheduling(period = 1000, order=1)
	public static void searchAndRescue(
			@In("FF_ID") String FF_ID) {
		
	}

	@InvariantMonitor("16")
	public static boolean noLifeThreatMonitor(
			@In("nodeId") Integer nodeId,
			@In("FF_ID") String FF_ID,
			@In("leaderId") String leaderId,
			@In("nearbyGMInDanger") Boolean nearbyGMInDanger) {
		if (FFSHelper.getInstance().initiateSearchAndRescue(nearbyGMInDanger, FF_ID, leaderId, nodeId, ClockProvider.getClock().getCurrentMilliseconds())) {
			System.out.println(FF_ID + " initiates search and rescue mission at " + ClockProvider.getClock().getCurrentMilliseconds());
			return false;
		}
		return true;
	}
}
