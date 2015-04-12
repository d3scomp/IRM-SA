package cz.cuni.mff.d3s.irmsa.strategies.correlation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.ComponentPair;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.CorrelationLevel;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.KnowledgeMetadataHolder;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.KnowledgeQuadruple;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.LabelPair;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

@Component
public class CorrelationManager {

	public String id;
	
	/**
	 * Holds the history of knowledge of all the other components in the system.
	 * 
	 * Integer - ID of a component
	 * String - Label of a knowledge field of the component
	 * MetadataWrapper - knowledge field value together with its meta data
	 */
	public Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> knowledgeHistoryOfAllComponents;

	/**
	 * The correlation of knowledge for a pair of knowledge fields.
	 * e.g. position -> temperature
	 */
	public List<CorrelationLevel> correlationLevels;
	
	/**
	 * The timestamps of the last processed knowledge related to component pair and theirs knowledge pairs.
	 */
	public Map<ComponentPair, Map<LabelPair, Long>> lastProcessedTimestamps;

	/**
	 * Time slot duration in milliseconds. Correlation of values is computed
	 * within these time slots.
	 */
	@Local
	private static final long TIME_SLOT_DURATION = 1000;
	
	/**
	 * The list of the other DEECo nodes that exists in the system.
	 * Except the node on which the CorrelaitonManager component is deployed.
	 */
	@Local
	public final List<DEECoNode> otherNodes;
	
	/**
	 * Create an instance of the {@link CorrelationManager} that will hold
	 * a reference to the given {@link DEECoNode}s.
	 * @param otherNodes The other {@link DEECoNode}s in the system.
	 */
	public CorrelationManager(List<DEECoNode> otherNodes) {
		knowledgeHistoryOfAllComponents = new HashMap<>();
		correlationLevels = new ArrayList<>();
		lastProcessedTimestamps = new HashMap<>();
		
		this.otherNodes = otherNodes;
	}
	
	/*
	 * For quick debugging.
	 */
	@Process
	@PeriodicScheduling(period=1000)
	public static void printHistory(
			@In("knowledgeHistoryOfAllComponents") Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			@In("correlationLevels") List<CorrelationLevel> levels){
		
		StringBuilder b = new StringBuilder(1024);
		b.append("Printing global history...\n");
		
		for (String id: history.keySet()) {

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
	 * Method that measures the correlation between the data in the system 
	 * 
	 * @param history The time series of all knowledge of all components.
	 * @param processedTimestamps The timestamps of lastly processed knowledge.
	 * @param levels The structure that holds the calculated correlations.
	 */
	@Process
	@PeriodicScheduling(period=1000)
	public static void calculateCorrelation(
			@In("knowledgeHistoryOfAllComponents") Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			@InOut("lastProcessedTimestamps") ParamHolder<Map<ComponentPair, Map<LabelPair, Long>>> processedTimestamps,
			@InOut("correlationLevels") ParamHolder<List<CorrelationLevel>> levels){
		
		System.out.println("Correlation process started...");
		Map<ComponentPair, Map<LabelPair, Long>> processedTimeSlots = processedTimestamps.value;
		
		// Compute the closeness between pairs of knowledge fields
		// Consider the unprocessed knowledge entries only
		for(ComponentPair components : getComponentPairs(history.keySet())){
			
			if(!processedTimeSlots.containsKey(components)){
				processedTimeSlots.put(components, new HashMap<>());
			}
			
			// For pair labels
			for(LabelPair labels : getLabelPairs(history, components)){
				if(!processedTimeSlots.get(components).containsKey(labels)){
					processedTimeSlots.get(components).put(labels, -1L);
				}
				correlationsForTimeSlots(history, processedTimeSlots,
						levels.value, components, labels);
			}
		}
	}
	
	/**
	 * Deploys, activates and deactivates correlation ensembles based on the current
	 * correlation of the data in the system.
	 * @param levels The structure that holds the calculated correlations. 
	 * @param deecoNodes The {@link DEECoNode}s in the system, where the ensembles are managed on.
	 * @throws Exception If there is a problem creating the ensemble definition class, or deploying it.
	 */
	@Process
	@PeriodicScheduling(period=1000)
	public static void manageCorrelationEnsembles(
			@InOut("correlationLevels") ParamHolder<List<CorrelationLevel>> levels,
			@In("otherNodes") List<DEECoNode> deecoNodes) throws Exception {
		System.out.println("Correlation ensembles management process started...");
		
		for(CorrelationLevel level : levels.value){
			String correlationFilter = level.getLabelPair().getFirstLabel();
			String correlationSubject = level.getLabelPair().getSecondLabel();
			@SuppressWarnings("rawtypes")
			Class ensemble = CorrelationEnsembleFactory.getEnsembleDefinition(correlationFilter, correlationSubject);
			if(level.getCorrelationLevel() > KnowledgeMetadataHolder.getConfidenceLevel(correlationSubject)
					&& !isEnsembleActive(deecoNodes, ensemble.getName())){
				// Activate if confidence level is satisfied and the ensemble is not deployed or inactive
				System.out.println(String.format("Deploying ensemble %s", ensemble.getName()));
				if(!setEnsembleActive(deecoNodes, ensemble.getName(), true)){
					// If the ensemble is not deployed, deploy it
					for(DEECoNode node : deecoNodes){
						node.deployEnsemble(ensemble);
					}	
				}
			} else if(level.getCorrelationLevel() < KnowledgeMetadataHolder.getConfidenceLevel(level.getLabelPair().getSecondLabel())
					&& isEnsembleActive(deecoNodes, ensemble.getName())) {
				// deactivate if deployed and confidence level is not satisfied
				System.out.println(String.format("Undeploying ensemble %s", ensemble.getName()));
				setEnsembleActive(deecoNodes, ensemble.getName(), false);
			}				
		}
	}
	
	/**
	 * Find all the ensemble instances on the given {@link DEECoNode}s and set theirs active status.
	 * @param deecoNodes The nodes on which the ensemble will be searched.
	 * @param ensembleName The name of the ensemble to be found.
	 * @param active The active status to be set.
	 * @return True if any ensemble of the given name is found. False otherwise. 
	 */
	private static boolean setEnsembleActive(List<DEECoNode> deecoNodes, String ensembleName, boolean active){
		boolean ensemblesFound = false;
		for(DEECoNode node : deecoNodes){
			for(ComponentInstance componentInstance : node.getRuntimeMetadata().getComponentInstances()){
				for(EnsembleController ensemble : componentInstance.getEnsembleControllers()){
					if(ensemble.getEnsembleDefinition().getName().equals(ensembleName)){
						ensemble.setActive(active);
						ensemblesFound = true;
					}
				}
			}
		}
		
		return ensemblesFound;
	}
	
	/**
	 * Finds the ensemble instances of the given name and checks whether it is active.
	 * @param deecoNodes The nodes on which the ensemble will be searched. 
	 * @param ensembleName The name of the ensemble to be found.
	 * @return True if at least one instance of the ensemble that is active is found.
	 * 		False otherwise.
	 */
	private static boolean isEnsembleActive(List<DEECoNode> deecoNodes, String ensembleName){
		// Assume the ensemble is not deployed or inactive
		boolean active = false;
		
		for(DEECoNode node : deecoNodes){
			for(ComponentInstance componentInstance : node.getRuntimeMetadata().getComponentInstances()){
				for(EnsembleController ensemble : componentInstance.getEnsembleControllers()){
					if(ensemble.getEnsembleDefinition().getName().equals(ensembleName)
							&& ensemble.isActive()){
						// If the ensemble is deployed and active store this information
						active = true;
					}
				}
			}
		}
		
		return active;
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
	 * Returns a list of all the pairs of labels that are common to both the specified components.
	 * All the pairs are inserted in both the possible ways [a,b] and [b,a].
	 * @param component1Id The ID of the first component.
	 * @param component2Id The ID of the second component.
	 * @return The list of all the pairs of labels that are common to both the specified components.
	 * All the pairs are inserted in both the possible ways [a,b] and [b,a].
	 */
	private static List<LabelPair> getLabelPairs(
			Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			ComponentPair components){
		List<LabelPair> labelPairs = new ArrayList<LabelPair>();
		
		Set<String> c1Labels = history.get(components.component1Id).keySet();
		Set<String> c2Labels = history.get(components.component2Id).keySet();
		
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
	 * Returns a list of all the pairs of components IDs from the given set of
	 * components IDs. The ordering of the components in the pair doesn't matter,
	 * therefore no two pairs with inverse ordering of the same two components
	 * are returned. As well as no pair made of a single component is returned. 
	 * @param componentIds The set of components IDs.
	 * @return The list of pairs of components IDs.
	 */
	private static List<ComponentPair> getComponentPairs(Set<String> componentIds){
		List<ComponentPair> componentPairs = new ArrayList<>();
		
		String[] componentArr = componentIds.toArray(new String[0]);
		for(int i = 0 ; i < componentArr.length; i++){
			for(int j = i+1; j < componentArr.length; j++){
				componentPairs.add(new ComponentPair(componentArr[i], componentArr[j]));
			}
		}
		
		return componentPairs;
	}
	
	/**
	 * Computes data correlation for the given components between the specified
	 * knowledge fields. The correlation is computed for the unprocessed interval
	 * respecting the time slots.
	 * @param history The knowledge history of all the components in the system.
	 * @param processedTimeSlots The last time slots of the last processed data
	 * 		  in the knowledge history.
	 * @param levels The correlation computed so far.
	 * @param components The pair of component for which the correlation will
	 * 		  be computed.
	 * @param labels The pair of labels for which the correlation will be computed.
	 */
	private static void correlationsForTimeSlots(
			Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			Map<ComponentPair, Map<LabelPair, Long>> processedTimeSlots,
			List<CorrelationLevel> levels,
			ComponentPair components,
			LabelPair labels){
		long lastTimeSlot = processedTimeSlots.get(components).get(labels);
		
		CorrelationLevel correlationLevel = getCorrelationLevel(levels, labels);
		List<MetadataWrapper<? extends Object>> c1Values1 = getUnprocessedTimeSlotsValues(
				history.get(components.component1Id).get(labels.getFirstLabel()), lastTimeSlot);
		List<MetadataWrapper<? extends Object>> c1Values2 = getUnprocessedTimeSlotsValues(
				history.get(components.component1Id).get(labels.getSecondLabel()), lastTimeSlot);
		List<MetadataWrapper<? extends Object>> c2Values1 = getUnprocessedTimeSlotsValues(
				history.get(components.component2Id).get(labels.getFirstLabel()), lastTimeSlot);
		List<MetadataWrapper<? extends Object>> c2Values2 = getUnprocessedTimeSlotsValues(
				history.get(components.component2Id).get(labels.getSecondLabel()), lastTimeSlot);
		
		KnowledgeQuadruple values = getMinCommonTimeSlotValues(
				c1Values1, c1Values2, c2Values1, c2Values2);
		if(values == null){
			Log.d(String.format("Correlation for [%s:%s]{%s -> %s} Skipped",
					components.component1Id, components.component2Id,
					labels.getFirstLabel(), labels.getSecondLabel()));
		}
		long timeSlot = -1;
		while(values != null){
			timeSlot = values.timeSlot;
			correlationLevel.addValues(values.c1Value1.getValue(), values.c2Value1.getValue(),
									   values.c1Value2.getValue(), values.c2Value2.getValue());
			
			Log.d(String.format("Correlation for [%s:%s]{%s -> %s}(%d)",
					components.component1Id, components.component2Id,
					labels.getFirstLabel(), labels.getSecondLabel(), timeSlot));
			
			removeEarlierValuesForTimeSlot(c1Values1, timeSlot);
			removeEarlierValuesForTimeSlot(c1Values2, timeSlot);
			removeEarlierValuesForTimeSlot(c2Values1, timeSlot);
			removeEarlierValuesForTimeSlot(c2Values2, timeSlot);
			values = getMinCommonTimeSlotValues(c1Values1, c1Values2, c2Values1, c2Values2);
		}
		if(timeSlot > -1){
			processedTimeSlots.get(components).put(labels, timeSlot);
		}
	}
	
	/**
	 * Extracts a list of values that are newer than the given time slot.
	 * @param values The list of values on which the extraction will be performed.
	 * @param lastTimeSlot The time slot delimiting the values being extracted.
	 * 		  Only newer values are extracted.
	 * @return A list of values that are newer than the given time slot.
	 */
	private static List<MetadataWrapper<? extends Object>> getUnprocessedTimeSlotsValues(
			List<MetadataWrapper<? extends Object>> values, long lastTimeSlot){
		List<MetadataWrapper<? extends Object>> unprocessed = new ArrayList<>();
		for(MetadataWrapper<? extends Object> value : values){
			long valueTimeSlot = value.getTimestamp() / TIME_SLOT_DURATION;
			if(valueTimeSlot > lastTimeSlot){
				unprocessed.add(value);
			}
		}
		
		return unprocessed;
	}
	
	/**
	 * Provides a quadruple of values with the smallest common time slot.
	 * @param c1Values1 List of values of component 1 for label 1.
	 * @param c1Values2 List of values of component 1 for label 2.
	 * @param c2Values1 List of values of component 2 for label 1.
	 * @param c2Values2 List of values of component 2 for label 2.
	 * @return A quadruple of values with the smallest common time slot.
	 */
	private static KnowledgeQuadruple getMinCommonTimeSlotValues(
			List<MetadataWrapper<? extends Object>> c1Values1,
			List<MetadataWrapper<? extends Object>> c1Values2,
			List<MetadataWrapper<? extends Object>> c2Values1,
			List<MetadataWrapper<? extends Object>> c2Values2){
		// Supposing that c1Values1 are sorted by timestamps
		for(MetadataWrapper<? extends Object> c1Value1 : c1Values1){
			long timeSlot = c1Value1.getTimestamp() / TIME_SLOT_DURATION;
			MetadataWrapper<? extends Object> c1Value2 =
					getFirstValueForTimeSlot(c1Values2, timeSlot);
			MetadataWrapper<? extends Object> c2Value1 =
					getFirstValueForTimeSlot(c2Values1, timeSlot);
			MetadataWrapper<? extends Object> c2Value2 =
					getFirstValueForTimeSlot(c2Values2, timeSlot);
			if(c1Value2 != null && c2Value1 != null && c2Value2 != null){
				return new KnowledgeQuadruple(c1Value1, c1Value2,
											  c2Value1, c2Value2, timeSlot);
			}	
		}
		return null;
	}
	
	/**
	 * Returns the first value within the given time slot.
	 * @param values The list of values from which the required value is extracted.
	 * @param timeSlot The required time slot for the extracted value.
	 * @return The first value within the given time slot.
	 */
	private static MetadataWrapper<? extends Object> getFirstValueForTimeSlot(
			List<MetadataWrapper<? extends Object>> values, long timeSlot){
		MetadataWrapper<? extends Object> earliestValue = null;
		for(MetadataWrapper<? extends Object> value : values){
			long valueTimeSlot = value.getTimestamp() / TIME_SLOT_DURATION;
			if(valueTimeSlot == timeSlot){
				if(earliestValue == null
						|| earliestValue.getTimestamp() > value.getTimestamp()){
					earliestValue = value;
				}
			}
		}
		
		return earliestValue;
	}
	
	/**
	 * Removes all the values that have belong to the specified time slot or any preceding,
	 * from the given list of values.
	 * @param values The list of values from which the specified values will be removed.
	 * @param timeSlot The time slot for which (and for all preceding) the values will
	 * be removed.
	 */
	private static void removeEarlierValuesForTimeSlot(
			List<MetadataWrapper<? extends Object>> values, long timeSlot){
		List<MetadataWrapper<? extends Object>> toRemove = new ArrayList<>();
		for(MetadataWrapper<? extends Object> value : values){
			long valueTimeSlot = value.getTimestamp() / TIME_SLOT_DURATION;
			if(valueTimeSlot <= timeSlot){
				toRemove.add(value);
			}
		}
		values.removeAll(toRemove);
	}
}
