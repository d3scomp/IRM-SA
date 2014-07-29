package cz.cuni.mff.d3s.deeco.demo.vehicles.simple;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("Vehicle")
public class Vehicle {

	public Integer position;
	public POI POI;
	public Plan plan;
	public Map<Integer,Availability> availabilityList;
	public Boolean planFeasibility;
 
	public Vehicle() {
		position = 5;
	}
 
	@Process
	@Invariant("pi5")
	@PeriodicScheduling(600) 
	public static void planWhenFarFromPOI(
		@In("id") String id, 
		@In("position.x") Integer position,  
		@In("POI") POI POI,  
		@In("availabilityList") Map<Integer,Availability> availabilityList,  
		@Out("plan") ParamHolder<Plan> plan 
	) {
		System.out.println("Inside " + id + "#planWhenFarFromPOI");
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
	
	@InvariantMonitor("pi5") 
	public static boolean checkPlanWhenFarFromPOI(
		@In("id") String id, 
		@In("position") Integer position,  
		@In("POI") POI POI,  
		@In("availabilityList") Map<Integer,Availability> availabilityList  
	) {
		System.out.println("Invoked " + id + "#checkPlanWhenFarFromPOI");
		System.out.println("position: " + position);
		return true;
	}
 
	@Process
	@Invariant("pi4")
	@PeriodicScheduling(300) 
	public static void planWhenCloseToPOI(
		@In("id") String id, 
		@In("position") Integer position,  
		@In("POI") POI POI,  
		@In("availabilityList") Map<Integer,Availability> availabilityList,  
		@Out("plan") ParamHolder<Plan> plan 
	) {
		System.out.println("Inside " + id + "#planWhenCloseToPOI");
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
	
	@InvariantMonitor("pi4") 
	public static boolean checkPlanWhenCloseToPOI(
		@In("id") String id, 
		@In("position") Integer position,  
		@In("POI") POI POI,  
		@In("availabilityList") Map<Integer,Availability> availabilityList
	) {
		System.out.println("Invoked " + id + "#checkPlanWhenCloseToPOI");
		System.out.println("position: " + position);
		return false;
	}
 
	@Process
	@Invariant("pi2")
	@PeriodicScheduling(300) 
	public static void checkFeasibility(
		@In("id") String id, 
		@In("position") Integer position,  
		@In("POI") POI POI,  
		@Out("planFeasibility") ParamHolder<Boolean> planFeasibility 
	) {
		System.out.println("Inside " + id + "#checkFeasibility");
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
 
	@Process
	@Invariant("pi1")
	@PeriodicScheduling(100) 
	public static void monitorPosition(
		@In("id") String id, 
		@Out("position") ParamHolder<Integer> position 
	) {
		System.out.println("Inside " + id + "#monitorPosition");
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
	}
}
