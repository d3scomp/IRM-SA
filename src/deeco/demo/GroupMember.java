package deeco.demo;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import deeco.demo.CorrelationTest.Variances;

@Component
public class GroupMember {

	public String id;
	
	public Integer position;
	public Integer temperature;
	public Integer battery;
	
	public GroupMember(String id) {
		this.id = id;
		position = 0;
		temperature = 0;
		battery = 0;
	}
	
	@Process
	@PeriodicScheduling(period=50)
	public static void changePosition(
			@In("id") String id,
			@InOut("position") ParamHolder<Integer> position) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		position.value += seed;
		
		System.out.println("GM#" + id + ",\tposition : " + position.value);
	}

	@Process
	@PeriodicScheduling(period=50)
	public static void changeTemperature(
			@In("id") String id,
			@InOut("temperature") ParamHolder<Integer> temperature) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		temperature.value += seed;
		
		System.out.println("GM#" + id + ",\ttemperature : " + temperature.value);
	}
	
	@Process
	@PeriodicScheduling(period=50)
	public static void changeBattery(
			@In("id") String id,
			@InOut("battery") ParamHolder<Integer> battery) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.LARGE_VARIANCE);
		battery.value += seed;
		
		System.out.println("GM#" + id + ",\tbattery : " + battery.value);
	}
	
}
