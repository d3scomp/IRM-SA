package deeco.demo;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import deeco.demo.CorrelationTest.Variances;

@Component
public class GroupLeader {

	public String id;
	public Integer leaderId;
	
	public MetadataWrapper<Integer> position;
	public MetadataWrapper<Integer> temperature;
	
	public GroupLeader(String id) {
		this.id = id;
		position = new MetadataWrapper<>(0);
		temperature = new MetadataWrapper<>(0);
	}
	
	@Process
	@PeriodicScheduling(period=50)
	public static void changePosition(
			@In("id") String id,
			@InOut("position") ParamHolder<MetadataWrapper<Integer>> position) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		position.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());
		
		System.out.println("GL#" + id + ",\tposition : " + position.value.getValue() + " (" + position.value.getTimestamp() + ")");
	}

	@Process
	@PeriodicScheduling(period=50)
	public static void changeTemperature(
			@In("id") String id,
			@InOut("temperature") ParamHolder<MetadataWrapper<Integer>> temperature) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		temperature.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());
		
		System.out.println("GL#" + id + ",\ttemperature : " + temperature.value.getValue() + " (" + temperature.value.getTimestamp() + ")");
	}
	
}
