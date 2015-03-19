package deeco.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import deeco.metadata.CorrelationLevel;
import deeco.metadata.LabelPair;
import deeco.metadata.MetadataWrapper;

@Component
public class DEECoCorrelationManager {


	public String id;
	
	public Map<Integer, Map<String, List<MetadataWrapper<? extends Object>>>> knowledgeHistoryOfAllComponents;

	/**
	 * The correlation of knowledge for a pair of knowledge fields.
	 */
	public List<CorrelationLevel> correlationLevels;
	
	public long lastProcessTime;
	
	

	public DEECoCorrelationManager() {
		knowledgeHistoryOfAllComponents = new HashMap<>();
		correlationLevels = new ArrayList<>();
		lastProcessTime = -1;
	}
	
	/*
	 * For quick debugging.
	 */
	@Process
	@PeriodicScheduling(period=1000)
	public static void printHistory(
			@In("knowledgeHistoryOfAllComponents") Map<Integer, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			@In("correlationLevels") List<CorrelationLevel> levels){
		
		StringBuilder b = new StringBuilder(1024);
		b.append("Printing global history...\n");
		
		for (Integer id: history.keySet()) {

			b.append("\tComponent " + id + "\n");
			
			Map<String, List<MetadataWrapper<? extends Object>>> componentHistory = history.get(id);
			for (String field : componentHistory.keySet()) {
				b.append("\t\t" + field + ":\t[");
				
				List<MetadataWrapper<? extends Object>> values = componentHistory.get(field); 
				for (MetadataWrapper<? extends Object> value : values) {
					b.append(value.getValue() + " (" + value.getTimestamp() + "), ");
				}
				
				b.delete(b.length()-2, b.length());
				b.append("]\n");
			}
			
		}
		
		b.append("Printing correlations...\n");

		for(CorrelationLevel correlationLevel : levels){
			b.append(String.format("%s -> %s : %.2f\n",
				correlationLevel.getLabelPair().getFirstLabel(),
				correlationLevel.getLabelPair().getSecondLabel(),
				correlationLevel.getCorrelationLevel()));
		}
		
		System.out.println(b.toString());
	}
	
	/**
	 * Method that performs the correlation between the data in the system 
	 * 
	 * @param history the time series of all knowledge of all components
	 */
	@Process
	@PeriodicScheduling(period=1000)
	public static void calculateCorrelation(
			@In("knowledgeHistoryOfAllComponents") Map<Integer, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			@In("lastProcessTime") Long processedDataTimestamp,
			@InOut("correlationLevels") ParamHolder<List<CorrelationLevel>> levels){
		
		System.out.println("Correlation process started...");
		
		// Compute the closeness between pairs of knowledge fields
		// Consider the last knowledge entry only
		for(int component1Id : history.keySet()){
			for(int component2Id : history.keySet()){
				if(component1Id == component2Id){
					continue;
				}
				// For pair labels
				for(LabelPair labels : getLabelPairs(history, component1Id, component2Id)){
					CorrelationLevel correlationLevel = getCorrelationLevel(levels.value, labels);
					List<MetadataWrapper<? extends Object>> c1Values1 = history.get(component1Id).get(labels.getFirstLabel());
					List<MetadataWrapper<? extends Object>> c1Values2 = history.get(component1Id).get(labels.getSecondLabel());
					Object c1Value1 = c1Values1.get(c1Values1.size()-1).getValue();
					Object c1Value2 = c1Values2.get(c1Values2.size()-1).getValue();
					List<MetadataWrapper<? extends Object>> c2Values1 = history.get(component2Id).get(labels.getFirstLabel());
					List<MetadataWrapper<? extends Object>> c2Values2 = history.get(component2Id).get(labels.getSecondLabel());
					Object c2Value1 = c2Values1.get(c2Values1.size()-1).getValue();
					Object c2Value2 = c2Values2.get(c2Values2.size()-1).getValue();
					
					correlationLevel.addValues(c1Value1, c2Value1, c1Value2, c2Value2);
				}
			}
		}
	}

	/**
	 * Returns the correlation for specified pair of knowledge fields.
	 * @param labels the pair of labels that identifies the knowledge fields.
	 * The correlation dependency is following: knowledge(firstLabel) -> knowledge(secondLabel)
	 * @return The correlation for specified pair of knowledge fields.
	 */
	private static CorrelationLevel getCorrelationLevel(List<CorrelationLevel> levels, LabelPair labels){
		for(CorrelationLevel correlationLevel : levels){
			if(correlationLevel.getLabelPair().equals(labels)){
				return correlationLevel;
			}
		}
		
		CorrelationLevel correlationLevel = new CorrelationLevel(labels);
		levels.add(correlationLevel);
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
	private static List<LabelPair> getLabelPairs(Map<Integer, Map<String, List<MetadataWrapper<? extends Object>>>> history, int component1Id, int component2Id){
		List<LabelPair> labelPairs = new ArrayList<LabelPair>();
		
		Set<String> c1Labels = history.get(component1Id).keySet();
		Set<String> c2Labels = history.get(component2Id).keySet();
		
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
	
}
