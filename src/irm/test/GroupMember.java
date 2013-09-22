package irm.test;

import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeUtil;

public class GroupMember extends ComponentDefinition {
	public Integer sensorData;
	public Integer position;
	public Integer temperature;
	public Integer acceleration;
	public Long noMovementSince;
	public Boolean nearbyGMInDanger;
	public Boolean breathingAparatusUsed;
	
	public GroupMember(String id) {
		this.id = id;
		this.sensorData = 0;
		this.position = 0;
		this.temperature = 0;
		this.acceleration = 0;
		this.nearbyGMInDanger = false;
		this.breathingAparatusUsed = false;
		this.noMovementSince = RuntimeUtil.getRuntime().getCurrentTime();
	}
	
	@Process
	public static void positionFromGPS() {
		
	}
	
	@Process
	public static void positionFromSenseors() {
		
	}
	
	@Process
	public static void scarceTemperatureMonitoring() {
		
	}
	
	@Process
	public static void closeTemperatureMonitoring() {
		
	}
	
	@Process
	public static void accelerationMonitoring() {
		
	}
	
	@Process
	public static void buzzerAndLights() {
		
	}
}
