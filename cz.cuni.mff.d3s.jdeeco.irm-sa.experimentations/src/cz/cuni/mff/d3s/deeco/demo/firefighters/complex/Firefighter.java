package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

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
 
	public Firefighter() {
		/* 
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("17")
	@PeriodicScheduling(period=5000) 
	public static void monitorTemperatureScarcely(
		@Out("temperature") ParamHolder<Long> temperature 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("11")
	@PeriodicScheduling(period=1000) 
	public static void monitorAcceleration(
		@Out("acceleration") ParamHolder<Integer> acceleration 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("25")
	@PeriodicScheduling(period=1000) 
	public static void monitorPositionITS(
		@Out("position") ParamHolder<Position> position 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("27")
	@PeriodicScheduling(period=1000) 
	public static void monitorPositionGPS(
		@Out("position") ParamHolder<Position> position 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("15")
	@PeriodicScheduling(period=1000) 
	public static void monitorTemperatureClosely(
		@Out("temperature") ParamHolder<Long> temperature 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("pi7")
	@PeriodicScheduling(period=2000) 
	public static void monitorOxygenLevel(
		@Out("oxygenLevel") ParamHolder<Long> oxygenLevel 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("pi8")
	@PeriodicScheduling(period=1000) 
	public static void searchAndRescue(
		@In("nearbyGMInDanger") Boolean nearbyGMInDanger 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
}
