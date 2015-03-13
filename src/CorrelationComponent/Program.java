package CorrelationComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Create some components and simulate the data exchange between them and a CorrelationComponent.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Program {

	public static void main(String[] args) {

		// The number of values iteratively measured by each component
		// (also the number of simulation cycles)
		final int dataLength = 10000;
		// The variance (top limit) for randomly generated numbers
		final int smallVariance = 10;
		final int largeVariance = 100;
		
		// Labels for the knowledge fields
		final String positionLabel = "position";
		final String temperatureLabel = "temperature";
		final String batteryLabel = "battery";
		
		// Ids of components in the simulation
		final int component1Id = 1;
		final int component2Id = 2;
		final int component3Id = 3;
		
		final String componentType = "firefighter";
		
		// Components in the simulation
		Component[] components = new Component[]{
				new Component(component1Id, componentType),
				new Component(component2Id, componentType),
				new Component(component3Id, componentType)};
		
		CorrelationManager correlationComponent = new CorrelationManager();
		
		// Values for the components for the simulation
		// Integer: ComponentId
		// String: knowledge field label
		// List<Object>: The values for the simulation
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
		
		
		/* ***** Simulate knowledge exchange ***** */
		// For the length of the prepared data
		for(int i = 0; i < dataLength; i++){
			// For each component
			for(Component component : components){
				// Get the prepared values for the component
				Map<String, List<Object>> componentValues = values.get(component.getId());
				// For each knowledge field of the component
				for(String label : componentValues.keySet()){
					// Set the generated value for the current simulation cycle
					component.setKnowledge(label, componentValues.get(label).get(i));
				}
				// Exchange the new knowledge values with the Correlation component
				correlationComponent.receiveData(component);
			}
		}
		
		// Print the computed correlations of data in the system
		correlationComponent.printCorrelations();

		System.out.println("done");
	}
	
	/**
	 * Generate a list of random numbers.
	 * @param dataLength The number of generated values.
	 * @param limit The bound for the magnitude of each generated value
	 * @return The list of generated numbers.
	 */
	private static List<Object> randomData(int dataLength, int limit){
		Random rand = new Random();
		List<Object> result = new ArrayList<Object>();
		for(int i = 0; i < dataLength; i++){
			result.add(rand.nextInt(limit));
		}
		
		return result;
	}

	/**
	 * Generate a list of random numbers that are close to the given numbers.
	 * @param existingData The list of numbers to compare the generate dnumbers to.
	 * @param label specifies the knowledge field for which the values are generated.
	 * It specifies the distance bound and metric.
	 * @return The list of generated numbers.
	 */
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
