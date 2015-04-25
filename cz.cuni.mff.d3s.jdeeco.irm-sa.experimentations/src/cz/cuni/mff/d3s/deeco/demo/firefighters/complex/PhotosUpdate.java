package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("ex5")
@PeriodicScheduling(period=10000, order=12)
public class PhotosUpdate {

	@Membership
	public static boolean membership(
		@In("member.UAV_ID") String UAV_ID 
	) {
			/*
				replace next line with actual condition
			*/
			return true;
	}

	@KnowledgeExchange
	public static void map(
		@In("member.lastPhoto") Photo lastPhoto,  
		@In("member.lastThermalPhoto") ThermalPhoto lastThermalPhoto,  
		@In("member.UAV_ID") String UAV_ID,  
		@Out("coord.photos") ParamHolder<Map<String,Photo>> photos,  
		@Out("coord.thermalPhotos") ParamHolder<Map<String,ThermalPhoto> > thermalPhotos 
	) {
		//System.out.println("PhotosUpdate!");
	}	
}