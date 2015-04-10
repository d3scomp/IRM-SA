package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("UnmannedAerialVehicle")
public class UnmannedAerialVehicle {

	// just to pass the annotation processor checks - use UAV_ID instead
	public String id;

	public String UAV_ID;
	public Photo lastPhoto;
	public ThermalPhoto lastThermalPhoto;
 
	public UnmannedAerialVehicle() {
		/* 
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("ex9")
	@PeriodicScheduling(period=10000) 
	public static void takeThermalPhoto(
		@Out("lastThermalPhoto") ParamHolder<ThermalPhoto> lastThermalPhoto 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("ex4")
	@PeriodicScheduling(period=10000) 
	public static void takePhoto(
		@Out("lastPhoto") ParamHolder<Photo> lastPhoto 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
}
