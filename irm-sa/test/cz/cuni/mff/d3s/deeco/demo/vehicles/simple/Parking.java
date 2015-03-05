package cz.cuni.mff.d3s.deeco.demo.vehicles.simple;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("Parking")
public class Parking {

	public String id;
	public Integer position;
	public Availability availability;
 
	public Parking() {
		/* 
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("pi3")
	@PeriodicScheduling(period=400) 
	public static void monitorAvailability(
		@Out("availability") ParamHolder<Availability> availability 
	) {
		System.out.println("Inside Parking#monitorAvailability");
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
}
