package CorrelationComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Program {

	public static void main(String[] args) {

		final int dataLength = 10000;
		final int smallVariance = 10;
		final int largeVariance = 100;
		
		final String positionLabel = "position";
		final String temperatureLabel = "temperature";
		final String batteryLabel = "battery";
		
		final int component1Id = 1;
		final int component2Id = 2;
		final int component3Id = 3;
		
		final String componentType = "firefighter";
		
		Component[] components = new Component[]{
				new Component(component1Id, componentType),
				new Component(component2Id, componentType),
				new Component(component3Id, componentType)};
		
		CorrelationManager correlationComponent = new CorrelationManager();
		
		Map<Integer, Map<String, List<Object>>> values = new HashMap<>();

		// Component 1 values
		Map<String, List<Object>> v1 = new HashMap<>();
		v1.put(positionLabel, randomData(dataLength, smallVariance));
		v1.put(temperatureLabel, randomData(dataLength, smallVariance));
		v1.put(batteryLabel, randomData(dataLength, largeVariance));
		values.put(component1Id, v1);

		// Component 2 values
		Map<String, List<Object>> v2 = new HashMap<>();
		v2.put(positionLabel, randomData(dataLength, smallVariance));
		v2.put(temperatureLabel, randomData(dataLength, smallVariance));
		v2.put(batteryLabel, randomData(dataLength, largeVariance));
		values.put(component2Id, v2);

		// Component 3 values
		Map<String, List<Object>> v3 = new HashMap<>();
		v3.put(positionLabel, randomData(dataLength, smallVariance));
		v3.put(temperatureLabel, randomData(dataLength, smallVariance));
		values.put(component3Id, v3);
		
		
		// Simulate knowledge exchange
		for(int i = 0; i < dataLength; i++){
			for(Component component : components){
				Map<String, List<Object>> componentValues = values.get(component.getId()); 
				for(String label : componentValues.keySet()){
					component.setKnowledge(label, componentValues.get(label).get(i));
				}
				correlationComponent.receiveData(component);
			}
		}
		
		correlationComponent.printCorrelations();

		System.out.println("done");
	}
	
	private static List<Object> randomData(int dataLength, int limit){
		Random rand = new Random();
		List<Object> result = new ArrayList<Object>();
		for(int i = 0; i < dataLength; i++){
			result.add(rand.nextInt(limit));
		}
		
		return result;
	}
	
	private static List<Object> closeData(List<Object> existingData, String label){
		
		int maxDistance = KnowledgeMetadataHolder.getBound(label);
		Random rand = new Random();
		List<Object> result = new ArrayList<Object>();
		
		for(Object existingValue : existingData){
			Integer value = (Integer) existingValue;
			Integer randDiff = maxDistance - rand.nextInt(2*maxDistance);
			result.add(value + randDiff);
		}
		
		return result;
	}

}
