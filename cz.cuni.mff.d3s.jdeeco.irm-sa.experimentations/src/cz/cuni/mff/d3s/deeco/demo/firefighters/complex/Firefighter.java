package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

@Component
@IRMComponent("Firefighter")
public class Firefighter {

	// just to pass the annotation processor checks - use FF_ID instead
	public String id;

	public String FF_ID;
	public Long temperature;
	public Integer acceleration;
	public Position position;
	public Long oxygenLevel;
	public String leaderId;
	public Boolean nearbyGMInDanger;
	public Boolean alarmOn;

	@Local
	public CurrentTimeProvider clock;

	public Firefighter(String FF_ID, String GL_ID, CurrentTimeProvider clock) {
		this.FF_ID = FF_ID;
		this.leaderId = GL_ID;
		this.temperature = 25L;
		this.acceleration = 30;
		this.position = new Position(34, 23);
		this.oxygenLevel = 90L;
		this.nearbyGMInDanger = false;
		this.alarmOn = false;
		this.clock = clock;
		
		FFSHelper.getInstance().registerFF(FF_ID);
	}

	@Process
	@Invariant("17")
	@PeriodicScheduling(period = 5000)
	public static void monitorTemperatureScarcely(
			@In("FF_ID") String FF_ID,
			@In("clock") CurrentTimeProvider clock,
			@InOut("temperature") ParamHolder<Long> temperature) {
		if (FFSHelper.getInstance().executeDangerSituation(FF_ID, clock.getCurrentMilliseconds())) {
			temperature.value = 75L;
			System.out.println(FF_ID + " in Danger at: " + clock.getCurrentMilliseconds());
		}
	}

	@Process
	@Invariant("11")
	@PeriodicScheduling(period = 1000)
	public static void monitorAcceleration(
			@Out("acceleration") ParamHolder<Integer> acceleration) {
		acceleration.value = 3;
	}

	@Process
	@Invariant("25")
	@PeriodicScheduling(period = 1000)
	public static void monitorPositionITS(
			@Out("position") ParamHolder<Position> position) {
		position.value = new Position(1, 1);
	}

	@Process
	@Invariant("27")
	@PeriodicScheduling(period = 1000)
	public static void monitorPositionGPS(
			@Out("position") ParamHolder<Position> position) {
		position.value = new Position(5, 5);
	}

	@Process
	@Invariant("15")
	@PeriodicScheduling(period = 1000)
	public static void monitorTemperatureClosely(
			@Out("temperature") ParamHolder<Long> temperature) {
		System.out.println("monitorTemperatureClosely");
		temperature.value = 40l;
	}

	@Process
	@Invariant("30")
	@PeriodicScheduling(period = 2000)
	public static void monitorOxygenLevel(
			@Out("oxygenLevel") ParamHolder<Long> oxygenLevel) {
		oxygenLevel.value = 70l;
	}

	@Process
	@Invariant("SOS")
	@PeriodicScheduling(period = 1000)
	public static void searchAndRescue(
			@In("FF_ID") String FF_ID,
			@In("clock") CurrentTimeProvider clock) {
		
	}

	@InvariantMonitor("16")
	public static boolean noLifeThreatMonitor(
			@In("FF_ID") String FF_ID,
			@In("clock") CurrentTimeProvider clock,
			@In("nearbyGMInDanger") Boolean nearbyGMInDanger) {
		if (nearbyGMInDanger && FFSHelper.getInstance().initiateSearchAndRescue(FF_ID, clock.getCurrentMilliseconds())) {
			System.out.println(FF_ID + " initiates search and rescue mission at " + clock.getCurrentMilliseconds());
			return false;
		}
		return true;
	}
}
