package cz.cuni.mff.d3s.irmsa.strategies.correlation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.ComponentPair;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.CorrelationLevel;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.CorrelationLevel.DistanceClass;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.DistancePair;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.KnowledgeMetadataHolder;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.KnowledgeQuadruple;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.LabelPair;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

@Component
@SystemComponent
public class CorrelationManager {

	private static final boolean dumpValues = false;
	
	/** Run flag stored in internal data under this key. */
	static final String RUN_FLAG = "runFlag";

	/** Done flag stored in internal data under this key. */
	static final String DONE_FLAG = "doneFlag";

	public String id = "CorrelationManager";

	/**
	 * Holds the history of knowledge of all the other components in the system.
	 *
	 * String - ID of a component
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
	
	public Map<LabelPair, Double> distanceBounds;

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
		distanceBounds = new HashMap<>();

		this.otherNodes = otherNodes;
	}

	/*
	 * For quick debugging.
	 */
//	@Process
//	@PeriodicScheduling(period=1000)
	public static void printHistory(
			@In("knowledgeHistoryOfAllComponents") Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			@In("correlationLevels") List<CorrelationLevel> levels){

		StringBuilder b = new StringBuilder(1024);
		b.append("Printing global history...\n");

		for (String id: history.keySet()) {

			b.append("Component " + id + "\n");

			Map<String, List<MetadataWrapper<? extends Object>>> componentHistory = history.get(id);
			for (String field : componentHistory.keySet()) {
				b.append("\t" + field + ":\n");

				b.append("\ttime: ");
				List<MetadataWrapper<? extends Object>> values = componentHistory.get(field);
				for (MetadataWrapper<? extends Object> value : values) {
					b.append(value.getTimestamp() + ", ");
				}
				b.delete(b.length()-2, b.length());
				b.append("\n\tvalues: ");
				for (MetadataWrapper<? extends Object> value : values) {
					b.append(value.getValue() + ", ");
				}
				b.delete(b.length()-2, b.length());
				b.append("\n\n");
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
			@In("knowledgeHistoryOfAllComponents") Map<String, Map<String, List<MetadataWrapper<?>>>> history,
			@InOut("lastProcessedTimestamps") ParamHolder<Map<ComponentPair, Map<LabelPair, Long>>> processedTimestamps,
			@InOut("correlationLevels") ParamHolder<List<CorrelationLevel>> levels,
			@InOut("distanceBounds") ParamHolder<Map<LabelPair, Double>> bounds){

		System.out.println("Correlation process started...");
		
		for(LabelPair labels : getAllLabelPairs(history)){
			System.out.println(String.format("%s -> %s", labels.getFirstLabel(), labels.getSecondLabel()));
			List<DistancePair> distances = computeDistances(history, labels);
			double boundary = getDistanceBoundary(distances, labels);
			System.out.println(String.format("Boundary: %f", boundary));
			bounds.value.put(labels, boundary);
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
			@InOut("distanceBounds") ParamHolder<Map<LabelPair, Double>> bounds,
			@In("otherNodes") List<DEECoNode> deecoNodes) throws Exception {
		
		final boolean run = ComponentHelper.retrieveFromInternalData(RUN_FLAG, true);
		System.out.println("Correlation ensembles management process started...");

		for(LabelPair labels : bounds.value.keySet()){
			String correlationFilter = labels.getFirstLabel();
			String correlationSubject = labels.getSecondLabel();
			double distance = bounds.value.get(labels);
			if (Double.isNaN(distance) || !run) {
				String ensembleName = CorrelationEnsembleFactory
						.composeClassName(correlationFilter, correlationSubject);
				System.out.println(String.format("Undeploying ensemble %s",	ensembleName));
				// Undeploy the ensemble if the meta-adaptation is stopped or the correlation between the data is not reliable
				for (DEECoNode node : deecoNodes) {
					node.undeployEnsemble(ensembleName);
				}
			} else {
				CorrelationEnsembleFactory.setEnsembleMembershipBoundary(correlationFilter, correlationSubject, distance);
				Class<?> ensemble = CorrelationEnsembleFactory.getEnsembleDefinition(correlationFilter, correlationSubject);
				System.out.println(String.format("Deploying ensemble %s", ensemble.getName()));
				// Deploy the ensemble if the correlation is reliable enough and the meta-adaptation is running
				for(DEECoNode node : deecoNodes){
					node.undeployEnsemble(ensemble.getName());
					node.deployEnsemble(ensemble);
				}
			}
		}
			
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
	
	private static Set<LabelPair> getAllLabelPairs(
			Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history){
		Set<LabelPair> labelPairs = new HashSet<LabelPair>();
		
		for(ComponentPair components : getComponentPairs(history.keySet()))
		{
			labelPairs.addAll(getLabelPairs(history, components));
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
	
	// Get all the components containing the given pair of knowledge fields
	private static Set<String> getComponents(
			Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			LabelPair labels){
		
		Set<String> components = new HashSet<>(history.keySet());
		
		for(String component : history.keySet()){
			if(!history.get(component).keySet().contains(labels.getFirstLabel())
					|| !history.get(component).keySet().contains(labels.getSecondLabel())){
				// If the component doesn't contain both the specified knowledge fields remove it
				components.remove(component);
			}
		}
		
		return components;
	}
	
	private static List<KnowledgeQuadruple> extractKnowledgeHistory(
			Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			ComponentPair components,
			LabelPair labels){
		
		List<KnowledgeQuadruple> knowledgeVectors = new ArrayList<>();
		List<MetadataWrapper<? extends Object>> c1Values1 = new ArrayList<>(
				history.get(components.component1Id).get(labels.getFirstLabel()));
		List<MetadataWrapper<? extends Object>> c1Values2 = new ArrayList<>(
				history.get(components.component1Id).get(labels.getSecondLabel()));
		List<MetadataWrapper<? extends Object>> c2Values1 = new ArrayList<>(
				history.get(components.component2Id).get(labels.getFirstLabel()));
		List<MetadataWrapper<? extends Object>> c2Values2 = new ArrayList<>(
				history.get(components.component2Id).get(labels.getSecondLabel()));

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
			knowledgeVectors.add(values);

			Log.d(String.format("Correlation for [%s:%s]{%s -> %s}(%d)",
					components.component1Id, components.component2Id,
					labels.getFirstLabel(), labels.getSecondLabel(), timeSlot));

			removeEarlierValuesForTimeSlot(c1Values1, timeSlot);
			removeEarlierValuesForTimeSlot(c1Values2, timeSlot);
			removeEarlierValuesForTimeSlot(c2Values1, timeSlot);
			removeEarlierValuesForTimeSlot(c2Values2, timeSlot);
			values = getMinCommonTimeSlotValues(c1Values1, c1Values2, c2Values1, c2Values2);
		}
		
		return knowledgeVectors;
	}
	
	// Return matrix of distances and distance classes for given knowledge fields anoung all the components
	private static List<DistancePair> computeDistances(
			Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> history,
			LabelPair labels){
		
		List<KnowledgeQuadruple> knowledgeVectors = new ArrayList<>();
		
		Set<String> componentIds = getComponents(history, labels);
		List<ComponentPair> componentPairs = getComponentPairs(componentIds);
		for(ComponentPair components : componentPairs){
			knowledgeVectors.addAll(extractKnowledgeHistory(history, components, labels));
		}

		List<DistancePair> distancePairs = new ArrayList<>();
		
		for(KnowledgeQuadruple knowledge : knowledgeVectors){
			// Consider only operational fields
			if(knowledge.c1Value1.isOperational() && knowledge.c2Value1.isOperational()
					&& knowledge.c1Value2.isOperational() && knowledge.c2Value2.isOperational()){
				double distance = KnowledgeMetadataHolder.distance(
						labels.getFirstLabel(),
						knowledge.c1Value1.getValue(),
						knowledge.c2Value1.getValue());
				DistanceClass distanceClass = KnowledgeMetadataHolder.classifyDistance(
						labels.getSecondLabel(),
						knowledge.c1Value2.getValue(),
						knowledge.c2Value2.getValue());
				distancePairs.add(new DistancePair(distance, distanceClass, knowledge.c1Value1.getTimestamp()));
			}
		}

		if (dumpValues) {
			StringBuilder b = new StringBuilder();
			b.append("Computed distances\n");
			fillDistances(distancePairs, b);
			System.out.print(b.toString());
		}
		
		return distancePairs;
	}
	
	private static double getDistanceBoundary(List<DistancePair> distancePairs, LabelPair labels){
		// Sort the data by the distance of first knowledge field
		Collections.sort(distancePairs);
		if(dumpValues) {
			StringBuilder b = new StringBuilder();
			b.append("Sorted distances\n");
			fillDistances(distancePairs, b);
			System.out.print(b.toString());
		}
		// Count the correlation for all the distances based on all smaller distances than the computed one
		List<Double> correlations = new ArrayList<>(Collections.nCopies(distancePairs.size(), Double.NaN));
		int closeCnt = 0;
		for(int i = 0; i < distancePairs.size(); i++){
			if(distancePairs.get(i).distanceClass == DistanceClass.Close){
				closeCnt++;
			}
			double corr = ((double) closeCnt) / ((double) i);
			correlations.set(i, corr);
		}
		// Find the greatest distance that satisfies the correlation level
		double confidenceLevel = KnowledgeMetadataHolder.getConfidenceLevel(labels.getSecondLabel());
		for(int i = distancePairs.size() - 1; i >= 0; i--){
			if(correlations.get(i) >= confidenceLevel){
				return distancePairs.get(i).distance;
			}
		}
		
		return Double.NaN;
	}
	
	private static void fillDistances(List<DistancePair> distancePairs, StringBuilder b){
		b.append("time: ");
		for(DistancePair dp : distancePairs)
		{
			b.append(dp.timestamp + ", ");
		}
		b.delete(b.length()-2, b.length());
		b.append("\n");
		b.append("distance: ");
		for(DistancePair dp : distancePairs)
		{
			b.append(String.format(Locale.ENGLISH, "%.1f, ", dp.distance));
		}
		b.delete(b.length()-2, b.length());
		b.append("\n");
		b.append("class: ");
		for(DistancePair dp : distancePairs)
		{
			b.append(dp.distanceClass + ", ");
		}
		b.delete(b.length()-2, b.length());
		b.append("\n");
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
