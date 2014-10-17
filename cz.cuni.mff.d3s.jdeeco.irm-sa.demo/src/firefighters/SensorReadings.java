package firefighters;

public class SensorReadings {
	public final Long temperature;
	public final Position position;
	public final Integer acceleration;
	public final Long oxygen;
	
	
	public SensorReadings(Long temperature, Position position,
			Integer acceleration, Long oxygen) {
		super();
		this.temperature = temperature;
		this.position = position;
		this.acceleration = acceleration;
		this.oxygen = oxygen;
	}
	
	public String toString() {
		return "("+temperature+", "+position+", "+acceleration+", "+oxygen+")";
	}
}
