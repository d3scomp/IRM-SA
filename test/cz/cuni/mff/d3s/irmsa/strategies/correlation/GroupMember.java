package cz.cuni.mff.d3s.irmsa.strategies.correlation;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.CorrelationTest.Variances;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

@Component
@IRMComponent("GroupMember")
public class GroupMember {

	public String id;
	public String role = "GroupMember";

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
	@PeriodicScheduling(period=500)
	public static void changePosition(
			@In("id") String id,
			@InOut("position") ParamHolder<MetadataWrapper<Integer>> position) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		position.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());

		System.out.println("GM#" + id + ",\tposition : " + position.value.getValue() + " (" + position.value.getTimestamp() + ")");
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void changeTemperature(
			@In("id") String id,
			@InOut("temperature") ParamHolder<MetadataWrapper<Integer>> temperature) {

		if(id.equals("1") && ProcessContext.getTimeProvider().getCurrentMilliseconds() > 10000){
			temperature.value.malfunction();
		} else {
//			Random rand = new Random();
//			int seed = rand.nextInt(Variances.SMALL_VARIANCE);
			// Setting fixed value of temperature to ensure the correlation (should be random,
			// the variance should reflect the boundary for temperature defined in KnowledgeMetadataHolder)
			temperature.value.setValue(20, ProcessContext.getTimeProvider().getCurrentMilliseconds());
		}

		System.out.println("GM#" + id + ",\ttemperature : " + temperature.value.getValue() + " (" + temperature.value.getTimestamp() + ")");
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void changeBattery(
			@In("id") String id,
			@InOut("battery") ParamHolder<MetadataWrapper<Integer>> battery) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.LARGE_VARIANCE);
		battery.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());

		System.out.println("GM#" + id + ",\tbattery : " + battery.value.getValue() + " (" + battery.value.getTimestamp() + ")");
	}

}
