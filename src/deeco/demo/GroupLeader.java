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
public class GroupLeader {

	public String id;
	
	public Integer leaderId;
	
	public Integer position;
	public Integer temperature;
	
	public GroupLeader(String id) {
		this.id = id;
		position = 0;
		temperature = 0;
	}
	
	@Process
	@PeriodicScheduling(period=50)
	public static void changePosition(
			@In("id") String id,
			@InOut("position") ParamHolder<Integer> position) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		position.value += seed;
		
		System.out.println("GL#" + id + ",\tposition : " + position.value);
	}

	@Process
	@PeriodicScheduling(period=50)
	public static void changeTemperature(
			@In("id") String id,
			@InOut("temperature") ParamHolder<Integer> temperature) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		temperature.value += seed;
		
		System.out.println("GL#" + id + ",\ttemperature : " + temperature.value);
	}
	
}
