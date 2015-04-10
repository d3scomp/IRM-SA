package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("SiteLeader")
public class SiteLeader {

	// just to pass the annotation processor checks
	public String id;

	public Map<Integer,Integer> noOfFFInDanger;
	public Map<String,Photo> photos;
	public Map<String,ThermalPhoto>  thermalPhotos;
 
	public SiteLeader() {
		/* 
			Just a skeleton,
			business logic to be provided here.
		*/
	}
}
