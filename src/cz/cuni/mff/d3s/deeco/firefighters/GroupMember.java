package cz.cuni.mff.d3s.deeco.firefighters;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.ActiveMonitor;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.annotations.Process;

public class GroupMember extends ComponentDefinition {
	public String groupLeaderId;
	public Integer position;
	public Integer temperature;
	public Integer acceleration;
	public Integer airLevel;
	public Boolean nearbyGMInDanger;

	public GroupMember(String id) {
		this.id = id;
		this.position = 0;
		this.temperature = 36;
		this.acceleration = 0;
		this.airLevel = 100;
		this.nearbyGMInDanger = false;
	}

	public static void acceleration(
			@Out("acceleration") OutWrapper<Integer> acceleration) {

	}
	
	@Process
	@PeriodicScheduling(3000)
	public static void scarceTemperature(
			@Out("temperature") OutWrapper<Integer> temperature) {

	}
	
	@Process
	public static void closeTemperature(
			@Out("temperature") OutWrapper<Integer> temperature) {

	}
	
	@Process
	public static void airLevel(
			@Out("airLevel") OutWrapper<Integer> airLevel) {

	}
	
	@Process
	public static void gpsPosition(
			@Out("position") OutWrapper<Integer> position) {

	}
	
	@Process
	public static void indoorsPosition(
			@Out("position") OutWrapper<Integer> position) {

	}
	
	@Process
	public static void alert() {

	}
	
	@ActiveMonitor("gpsPosition")
	public static boolean gpsPositionMonitor(@In("position") Integer position) {
		return position >= 0;
	}
}
