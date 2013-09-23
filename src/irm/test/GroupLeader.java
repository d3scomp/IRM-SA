package irm.test;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

public class GroupLeader extends ComponentDefinition {
	public List<Integer> sensorDataList;
	public Boolean gmInDanger;

	public GroupLeader(String id) {
		this.id = id;
		this.gmInDanger = false;
		this.sensorDataList = new LinkedList<>();
	}

	@Process("10")
	public static void monitorGMs(
			@In("sensorDataList") List<Integer> sensorDataList,
			@Out("gmInDanger") OutWrapper<Boolean> gmInDanger) {
		System.out.println("GL: monitorGMs" + sensorDataList);
		for (Integer t : sensorDataList) {
			if (t > 10) {
				gmInDanger.value = true;
				return;
			}
		}
		gmInDanger.value = false;
	}
}
