package deeco.metadata;

import java.util.HashMap;
import java.util.Map;

import deeco.metadata.CorrelationLevel.DistanceClass;
import deeco.metric.DifferenceMetric;
import deeco.metric.Metric;

/**
 * A class that maps additional metadata to individual knowledge fields.
 * The metadata are hard coded to predefined knowledge fields identified by
 * labels. The metadata are a distance bound and metric.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class KnowledgeMetadataHolder {

	/**
	 * Encapsulates the metadata for a single knowledge field.
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	private static class KnowledgeMetadata{
		/**
		 * The distance bound delimiting close and far distance.
		 */
		private int bound;
		/**
		 * The metric to compute the distance of values of the knowledge field.
		 */
		private Metric metric;
		
		/**
		 * Create a new instance of KnowledgeMetadata.
		 * @param bound The distance bound delimiting close and far distance.
		 * @param metric The metric to compute the distance of values of the knowledge field.
		 */
		public KnowledgeMetadata(int bound, Metric metric){
			this.bound = bound;
			this.metric = metric;
		}
		
		/**
		 * The distance bound delimiting close and far distance.
		 * @return The distance bound delimiting close and far distance.
		 */
		public int getBound(){
			return bound;
		}
		
		/**
		 * The metric to compute the distance of values of the knowledge field.
		 * @return The metric to compute the distance of values of the knowledge field.
		 */
		public Metric getMetric(){
			return metric;
		}
	}
	
	/**
	 * Mapping of the metadata to knowledge fields identified by theirs labels.
	 */
	private static Map<String, KnowledgeMetadata> knowledgeMetadata;
	
	/**
	 * Ititialize the static instance of the KnowledgeMetadataHolder and fill in
	 * default metadata values for predefined knowledge fields.
	 */
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
	
	/**
	 * Set the given metadata for the knowledge field identified by the given label. 
	 * @param label Identifies the metadata field.
	 * @param bound The distance bound delimiting close and far distance.
	 * @param metric The metric to compute the distance of values of the knowledge field.
	 */
	public static void setBoundAndMetric(String label, int bound, Metric metric){
		knowledgeMetadata.put(label, new KnowledgeMetadata(bound, metric));
	}
	
	/**
	 * The distance bound delimiting close and far distance for the knowledge field
	 * identified by the given label.
	 * @param label Identifies the knowledge field.
	 * @return The distance bound delimiting close and far distance for the knowledge
	 * field identified by the given label.
	 */
	public static int getBound(String label){
		if(knowledgeMetadata.containsKey(label)){
			return knowledgeMetadata.get(label).getBound();
		}
		else {
			return -1;
		}
	}
	
	/**
	 * The metric to compute the distance of values of the knowledge field
	 * identified by the given label.
	 * @param label Identifies the knowledge field.
	 * @return The metric to compute the distance of values of the knowledge
	 * field identified by the given label.
	 */
	public static Metric getMetric(String label){
		if(knowledgeMetadata.containsKey(label)){
			return knowledgeMetadata.get(label).getMetric();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Indicates whether the KnowledgeMetadataHolder contains the specified label.
	 * @param label Identifies the knowledge field.
	 * @return True if the MetadataKnowledgeHolder contains metadata for the
	 * knowledge field identified by the given label.
	 */
	public static boolean containsLabel(String label){
		return knowledgeMetadata.containsKey(label);
	}
	
	/**
	 * Classifies the distance between the given values based on the distance bound
	 * using the metric specific to the knowledge field identified by the given label.
	 * @param label Identifies the knowledge field.
	 * @param value1 The value to classify the distance from.
	 * @param value2 The value to classify the distance to.
	 * @return The class of the measured distance for the specified knowledge field.
	 */
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
