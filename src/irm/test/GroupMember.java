package irm.test;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeUtil;

public class GroupMember extends ComponentDefinition {
	public Integer position;
	public Integer temperature;
	public Integer acceleration;
	public Long noMovementSince;
	public Boolean nearbyGMInDanger;
	public String groupLeaderId;

	public GroupMember(String id, String groupLeaderId, long currentTime) {
		this.id = id;
		this.position = 0;
		this.temperature = 0;
		this.acceleration = 0;
		this.nearbyGMInDanger = false;
		this.groupLeaderId = groupLeaderId;
		this.noMovementSince = currentTime;
	}

	@Process("30")
	public static void positionFromGPS(
			@Out("position") OutWrapper<Integer> position,
			@Out("noMovementSince") OutWrapper<Long> noMovementSince) {
		System.out.println("GM: positionFromGPS");
		position.value = new Random().nextInt(20);
		noMovementSince.value = RuntimeUtil.getRuntime().getCurrentTime();
	}

	@Process("29")
	public static void positionFromSenseors(
			@Out("position") OutWrapper<Integer> position,
			@Out("noMovementSince") OutWrapper<Long> noMovementSince) {
		System.out.println("GM: positionFromSenseors");
		position.value = new Random().nextInt(20);
		noMovementSince.value = RuntimeUtil.getRuntime().getCurrentTime();
	}

	@Process("22")
	public static void scarceTemperatureMonitoring(
			@Out("temperature") OutWrapper<Integer> temperature) {
		System.out.println("GM: positionFromGPS");
		temperature.value = new Random().nextInt(20);
	}

	@Process("20")
	public static void closeTemperatureMonitoring(
			@Out("temperature") OutWrapper<Integer> temperature) {
		System.out.println("GM: scarceTemperatureMonitoring");
		temperature.value = new Random().nextInt(20);
	}

	@Process("21")
	public static void accelerationMonitoring(
			@Out("acceleration") OutWrapper<Integer> acceleration) {
		System.out.println("GM: scarceTemperatureMonitoring");
		acceleration.value = new Random().nextInt(20);
	}
}
