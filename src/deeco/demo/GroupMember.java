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
public class GroupMember {

	public String id;
	
	public MetadataWrapper<Integer> position;
	public MetadataWrapper<Integer> temperature;
	public MetadataWrapper<Integer> battery;
	
	public GroupMember(String id) {
		this.id = id;
		position = new MetadataWrapper<>(0);
		temperature = new MetadataWrapper<>(0);
		battery = new MetadataWrapper<>(0);
	}
	
	@Process
	@PeriodicScheduling(period=50)
	public static void changePosition(
			@In("id") String id,
			@InOut("position") ParamHolder<MetadataWrapper<Integer>> position) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		position.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());
		
		System.out.println("GM#" + id + ",\tposition : " + position.value.getValue() + " (" + position.value.getTimestamp() + ")");
	}

	@Process
	@PeriodicScheduling(period=50)
	public static void changeTemperature(
			@In("id") String id,
			@InOut("temperature") ParamHolder<MetadataWrapper<Integer>> temperature) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		temperature.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());
		
		System.out.println("GM#" + id + ",\ttemperature : " + temperature.value.getValue() + " (" + temperature.value.getTimestamp() + ")");
	}
	
	@Process
	@PeriodicScheduling(period=50)
	public static void changeBattery(
			@In("id") String id,
			@InOut("battery") ParamHolder<MetadataWrapper<Integer>> battery) {
		
		Random rand = new Random();
		int seed = rand.nextInt(Variances.LARGE_VARIANCE);
		battery.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());
		
		System.out.println("GM#" + id + ",\tbattery : " + battery.value.getValue() + " (" + battery.value.getTimestamp() + ")");
	}
	
}
