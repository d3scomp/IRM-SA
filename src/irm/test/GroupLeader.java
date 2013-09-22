package irm.test;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;

public class GroupLeader extends ComponentDefinition {
	public List<Integer> sensorDataList;
	public Boolean gmInDanger;
	
	public GroupLeader(String id) {
		this.id = id;
		this.gmInDanger = false;
		this.sensorDataList = new LinkedList<>();
	}
	
	@Process
	public static void monitorGMs() {
		
	}
}
