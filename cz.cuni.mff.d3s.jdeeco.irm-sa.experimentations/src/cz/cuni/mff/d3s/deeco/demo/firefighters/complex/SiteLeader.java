package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;

@Component
@IRMComponent("SiteLeader")
public class SiteLeader {

	// just to pass the annotation processor checks
	public String id;

	public Map<Integer,Integer> noOfFFInDanger;
	public Map<String,Photo> photos;
	public Map<String,ThermalPhoto>  thermalPhotos;
 
	public SiteLeader() {
		this.noOfFFInDanger = new HashMap<>();
		this.photos = new HashMap<>();
		this.thermalPhotos = new HashMap<>();
	}
}
