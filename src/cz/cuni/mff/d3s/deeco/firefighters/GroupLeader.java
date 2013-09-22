package cz.cuni.mff.d3s.deeco.firefighters;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.ActiveMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

public class GroupLeader extends ComponentDefinition {
	public List<SensorData> sensorDataList;
	public Boolean GMInDanger;
	
	public GroupLeader(String id) {
		this.id = id;
		this.sensorDataList = new LinkedList<>();
		this.GMInDanger = false;
	}
	
	@Process
	public static void determinGMInDanger(
			@In("sensorDataList") List<SensorData> sensorData,
			@Out("GMInDanger") OutWrapper<Boolean> inDanger) {
		
	}
}
