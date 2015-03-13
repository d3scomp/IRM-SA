package CorrelationComponent;

import java.util.HashMap;
import java.util.Map;

import CorrelationComponent.CorrelationManager.DistanceClass;

public class KnowledgeMetadataHolder {

	private static class KnowledgeMetadata{
		private int bound;
		private Metric metric;
		
		public KnowledgeMetadata(int bound, Metric metric){
			this.bound = bound;
			this.metric = metric;
		}
		
		public int getBound(){
			return bound;
		}
		
		public Metric getMetric(){
			return metric;
		}
	}
	
	private static Map<String, KnowledgeMetadata> knowledgeMetadata;
	
	static {
		knowledgeMetadata = new HashMap<String, KnowledgeMetadata>();
		
		/* default data */

		final String positionLabel = "position";
		final String temperatureLabel = "temperature";
		final String batteryLabel = "battery";
		
		final int positionBoundary = 8;
		final int temperatureBoundary = 3;
		final int batteryBoundary = 2;

		final Metric simpleMetric = new DifferenceMetric();

		knowledgeMetadata.put(positionLabel, new KnowledgeMetadata(positionBoundary, simpleMetric));
		knowledgeMetadata.put(temperatureLabel, new KnowledgeMetadata(temperatureBoundary, simpleMetric));
		knowledgeMetadata.put(batteryLabel, new KnowledgeMetadata(batteryBoundary, simpleMetric));
	}
	
	public static void setBoundAndMetric(String label, int bound, Metric metric){
		knowledgeMetadata.put(label, new KnowledgeMetadata(bound, metric));
	}
	
	public static int getBound(String label){
		if(knowledgeMetadata.containsKey(label)){
			return knowledgeMetadata.get(label).getBound();
		}
		else {
			return -1;
		}
	}
	
	public static Metric getMetric(String label){
		if(knowledgeMetadata.containsKey(label)){
			return knowledgeMetadata.get(label).getMetric();
		}
		else {
			return null;
		}
	}
	
	public static boolean containsLabel(String label){
		return knowledgeMetadata.containsKey(label);
	}
	
	public static DistanceClass classifyDistance(String label, Object value1, Object value2){
		if(containsLabel(label)){
			Metric metric = getMetric(label);
			int bound = getBound(label);
			
			if(metric.distance(value1, value2) <= bound){
				return DistanceClass.Close;
			}
			else{
				return DistanceClass.Far;
			}	
		}
		
		return DistanceClass.Undefined;
	}
}
