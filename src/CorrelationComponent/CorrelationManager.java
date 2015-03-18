package CorrelationComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * An emulation of a DEECo component that specifies the functionality
 * for computation of the correlation between knowledge data
 * of other components.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class CorrelationManager {
	
	/**
	 * Enumerates the classes of distances.
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	public enum DistanceClass {
		/**
		 * The distance is far.
		 */
		Far,
		/**
		 * The distance is close.
		 */
		Close,
		/**
		 * The distance is undefined.
		 */
		Undefined;
	}
	
	/**
	 * A simple holder of a pair of knowledge labels.
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	private class LabelPair{
		/**
		 * The first label.
		 */
		private String firstLabel;
		/**
		 * The second label.
		 */
		private String secondLabel;
		
		/**
		 * Creates a new pair of knowledge labels.
		 * @param label1 The first label in the pair.
		 * @param label2 The second label in the pair.
		 */
		public LabelPair(String label1, String label2){
			firstLabel = label1;
			secondLabel = label2;
		}
		
		/**
		 * Returns the first label in the pair.
		 * @return The first label in the pair.
		 */
		public String getFirstLabel(){
			return firstLabel;
		}
		
		/**
		 * Returns the second label in the pair.
		 * @return The second label in the pair.
		 */
		public String getSecondLabel(){
			return secondLabel;
		}
		
		/**
		 * Returns true if the argument is LabelPair and
		 * both the labels from the other LabelPair equals the labels
		 * from this LabelPair. The ordering of the labels matter.
		 * @param other the LabelPair to compare with.
		 * @return True if the other is an LabelPair and both its
		 * labels equals to the labels in this instance. The ordering
		 * of the labels matter in the comparision. False otherwise.
		 */
		@Override
		public boolean equals(Object other){
			if(!(other instanceof LabelPair)){
				return false;
			}
			
			LabelPair otherLabelPair = (LabelPair) other;
			
			return firstLabel.equals(otherLabelPair.firstLabel)
					&& secondLabel.equals(otherLabelPair.secondLabel);
		}
	}

	/**
	 * This class encapsulates the correlation of knowledge identified by two labels.
	 * The correlation captures the the dependency of the knowledge identified by the
	 * secong label on the knowledge identified by the first label. The dependency is
	 * following:
	 * 	knowledge(firstLabel) -> knowledge(secondLabel)
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	private class CorrelationLevel{
		/**
		 * The pair of labels for which the correlation is held in this instance.
		 * The ordering of the labels matter and gives the direction of dependency.
		 * knowledge(firstLabel) -> knowledge(secondLabel)
		 */
		private LabelPair labels;
		/**
		 * The count of close knowledge values (comparing only knowledge values taken in the same time frame)
		 * identified by the first label.
		 */
		private int data1CloseCount;
		/**
		 * The count of close knowledge values (comparing only knowledge values taken in the same time frame)
		 * identified by the second label. The comparison of these knowledge values is made only when the
		 * knowledge values identified by the first label are close. The distance classes of the knowledge
		 * values identified by the first label serves as a filter of the knowledge values identified by
		 * the second label that will be compared.
		 */
		private int data2CloseCount;
		
		/**
		 * Create new instance of CorrelationLevel for given pair of labels.
		 * @param labels the pair of label for which the correlation will be held.
		 */
		public CorrelationLevel(LabelPair labels){
			this.labels = labels;
			data1CloseCount = 0;
			data2CloseCount = 0;
		}
		
		/**
		 * The passed values will contribute to the computed correlation.
		 * @param component1Value1 The value from one component identified by the first label. 
		 * @param component2Value1 The value from the other component identified by the first label.
		 * @param component1Value2 The value from one component identified by the second label.
		 * @param component2Value2 The value from the other component identified by the second label.
		 */
		public void addValues(Object component1Value1, Object component2Value1,
				Object component1Value2, Object component2Value2){
			if(DistanceClass.Close == KnowledgeMetadataHolder.classifyDistance(
					labels.firstLabel, component1Value1, component2Value1)){
				data1CloseCount++;
				if(DistanceClass.Close == KnowledgeMetadataHolder.classifyDistance(
						labels.secondLabel, component1Value2, component2Value2)){
					data2CloseCount++;
				}
			}
		}
		
		/**
		 * Returns the pair of labels for which the correlation is held in this instance.
		 * @return The pair of labels for which the correlation is held in this instance.
		 */
		public LabelPair getLabelPair(){
			return labels;
		}
		
		/**
		 * Returns the correlation level for the knowledge identified by the second label dependent
		 * on the knowledge identified by the first label.
		 * @return The correlation level for the knowledge identified by the second label dependent
		 * on the knowledge identified by the first label.
		 */
		public double getCorrelationLevel(){
			return (double) data2CloseCount / (double) data1CloseCount;
		}
		
	}
	
	/**
	 * The history of knowledge of all the components.
	 * Integer: componentId
	 * String: knowledge field
	 * List<Object> trace of knowledge field values
	 */
	Map<Integer, Map<String, List<Object>>> knowledgeHistoryOfAllComponents;
	
	
	/**
	 * The correlation of knowledge for a pair of knowledge fields.
	 */
	List<CorrelationLevel> correlationLevels;
	
	/**
	 * Create a new instance of CorrelationManager.
	 */
	public CorrelationManager(){
		knowledgeHistoryOfAllComponents = new HashMap<Integer, Map<String,List<Object>>>();
		correlationLevels = new ArrayList<CorrelationManager.CorrelationLevel>();
	}
	
	/**
	 * Receives data from the specified component. Simulates the knowledge exchange.
	 * @param component The component to receive data from.
	 */
	public void receiveData(Component component){ // TODO: split into knowledge exchange and component process
		if(!knowledgeHistoryOfAllComponents.containsKey(component.getId())){
			knowledgeHistoryOfAllComponents.put(component.getId(), new HashMap<String, List<Object>>());
		}
		Map<String, List<Object>> historyData = knowledgeHistoryOfAllComponents.get(component.getId());
				
		for(String label : component.getKnowledgeLabels()){
			if(!historyData.containsKey(label)){
				historyData.put(label, new ArrayList<Object>());
			}
			historyData.get(label).add(component.getKnowledge(label));
		}
	}
	
	/**
	 * Simulate the components process. Compute the correlations.
	 */
	public void process(){
		// Compute the closeness between pairs of knowledge fields
		// Consider the last knowledge entry only
		for(int component1Id : knowledgeHistoryOfAllComponents.keySet()){
			for(int component2Id : knowledgeHistoryOfAllComponents.keySet()){
				if(component1Id == component2Id){
					continue;
				}
				// For pair labels
				for(LabelPair labels : getLabelPairs(component1Id, component2Id)){
					CorrelationLevel correlationLevel = getCorrelationLevel(labels);
					List<Object> c1Values1 = knowledgeHistoryOfAllComponents.get(component1Id).get(labels.firstLabel);
					List<Object> c1Values2 = knowledgeHistoryOfAllComponents.get(component1Id).get(labels.secondLabel);
					Object c1Value1 = c1Values1.get(c1Values1.size()-1);
					Object c1Value2 = c1Values2.get(c1Values2.size()-1);
					List<Object> c2Values1 = knowledgeHistoryOfAllComponents.get(component2Id).get(labels.firstLabel);
					List<Object> c2Values2 = knowledgeHistoryOfAllComponents.get(component2Id).get(labels.secondLabel);
					Object c2Value1 = c2Values1.get(c2Values1.size()-1);
					Object c2Value2 = c2Values2.get(c2Values2.size()-1);
					
					correlationLevel.addValues(c1Value1, c2Value1, c1Value2, c2Value2);
				}
			}
		}
	}
	
	/**
	 * Print all the computed correlations for all the pairs of knowledge fields.
	 */
	public void printCorrelations(){
		
		for(CorrelationLevel correlationLevel : correlationLevels)
		System.out.println(String.format("%s -> %s : %.2f",
				correlationLevel.getLabelPair().getFirstLabel(),
				correlationLevel.getLabelPair().getSecondLabel(),
				correlationLevel.getCorrelationLevel()));
	}
	
	/**
	 * Returns the correlation for specified pair of knowledge fields.
	 * @param labels the pair of labels that identifies the knowledge fields.
	 * The correlation dependency is following: knowledge(firstLabel) -> knowledge(secondLabel)
	 * @return The correlation for specified pair of knowledge fields.
	 */
	private CorrelationLevel getCorrelationLevel(LabelPair labels){
		for(CorrelationLevel correlationLevel : correlationLevels){
			if(correlationLevel.getLabelPair().equals(labels)){
				return correlationLevel;
			}
		}
		
		CorrelationLevel correlationLevel = new CorrelationLevel(labels);
		correlationLevels.add(correlationLevel);
		return correlationLevel;
	}
	
	/**
	 * Returns the list of all the pairs of labels that are common to both the specified components.
	 * All the pairs are inserted in both the possible ways [a,b] and [b,a].
	 * @param component1Id The ID of the first component.
	 * @param component2Id The ID of the second component.
	 * @return The list of all the pairs of labels that are common to both the specified components.
	 * All the pairs are inserted in both the possible ways [a,b] and [b,a].
	 */
	private List<LabelPair> getLabelPairs(int component1Id, int component2Id){
		List<LabelPair> labelPairs = new ArrayList<LabelPair>();
		
		Set<String> c1Labels = knowledgeHistoryOfAllComponents.get(component1Id).keySet();
		Set<String> c2Labels = knowledgeHistoryOfAllComponents.get(component2Id).keySet();
		
		// For all the label pairs
		for(String label1 : c1Labels){
			for(String label2 : c1Labels){
				if(label1.equals(label2)){
					// The pair mustn't contain one label twice
					continue;
				}
				if(c2Labels.contains(label1)
						&& c2Labels.contains(label2)){
					// Both the components has to contain both the labels
					labelPairs.add(new LabelPair(label1, label2));
				}
			}
		}
		
		return labelPairs;
	}
	
	/**
	 * Indicates whether the traces of knowledge history for the two specified components have the same length.
	 * @param component1Id The ID of the first component.
	 * @param component2Id The ID of the second component.
	 * @param labels The pair of labels for which the knowledge history length is checked.
	 * @return True if the knowledge history length for the specified pair of labels and the specified components
	 * equals. False otherwise.
	 */
	private boolean isEqualSizeHistory(int component1Id, int component2Id, LabelPair labels){
		int c1L1Size = knowledgeHistoryOfAllComponents.get(component1Id).get(labels.firstLabel).size();
		int c1L2Size = knowledgeHistoryOfAllComponents.get(component1Id).get(labels.secondLabel).size();
		int c2L1Size = knowledgeHistoryOfAllComponents.get(component2Id).get(labels.firstLabel).size();
		int c2L2Size = knowledgeHistoryOfAllComponents.get(component2Id).get(labels.secondLabel).size();
		
		return c1L1Size == c1L2Size
				&& c1L1Size == c2L1Size
				&& c1L1Size == c2L2Size;
	}
}


//@PeiodicScheduling(1000) // 1 sec polling
//@Ensemble
//public class SensingEnsemble {
//
///**
// * 
// * @param component
// */
//public boolean membership(
//		@In("member.id") Integer memberId,
//		@InOut("coord.knowledgeHistoryOfAllComponents") Integer coordId, // CorrelationManger
//		) {
//	return true;
//}
//
///**
// * 
// */
//public void exchange(
//		@In("member.id") Integer memberId,
//		@InOut("coord.knowledgeHistoryOfAllComponents") Map<Integer, Map<String, List<Object>>> knowledgeHistoryOfAllComponents, // CorrelationManger
//		{
//	// here we need to "hack in" access to the knowledgeManager in order to write:
//	ReadOnlyKnowledgeManger manager = context.getComponentProcess.eInstance().getKnowledgeManager();
//	List<Object> componentKnowledge = manager.getAll();
//	for (Object o : componentKnowledge) {
//		
//	}
//	
//}
//
//}
