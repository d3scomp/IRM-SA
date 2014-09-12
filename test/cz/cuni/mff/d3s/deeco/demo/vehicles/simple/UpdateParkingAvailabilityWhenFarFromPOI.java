package cz.cuni.mff.d3s.deeco.demo.vehicles.simple;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("ei2")
@PeriodicScheduling(period=600)
public class UpdateParkingAvailabilityWhenFarFromPOI {

	@Membership
	public static boolean membership(
		@In("member.POI") POI POI,  
		@In("coord.position") Integer position,  
		@In("coord.availability") Availability availability 
	) {
			/*
				replace next line with actual condition
			*/
			return true;
	}

	@KnowledgeExchange
	public static void map(
		@InOut("member.availabilityList") ParamHolder<Map<Integer,Availability>> availabilityList,  
		@In("coord.availability") Availability availability 
	) {
		System.out.println("Inside UpdateParkingAvailabilityWhenFarFromPOI mapping");
		/*
		    Add knowledge exchange function here. 
		 */
	}	
	
	@InvariantMonitor("ei2")
	public static boolean checkUpdateParkingAvailabilityWhenFarFromPOI(
		@In("member.availabilityList") Map<Integer,Availability> availabilityList,  
		@In("coord.availability") Availability availability 
	) {
		System.out.println("Invoked checkUpdateParkingAvailabilityWhenFarFromPOI monitor");
		return true;
	}
}