package deeco.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

@Component
public class DEECoCorrelationManager {

	public String id;
	
	public Map<Integer, Map<String, List<Object>>> knowledgeHistoryOfAllComponents;

	public DEECoCorrelationManager() {
		knowledgeHistoryOfAllComponents = new HashMap<>(); 
	}
	
	/*
	 * For quick debugging.
	 */
	@Process
	@PeriodicScheduling(period=100)
	public static void printHistory(
			@In("knowledgeHistoryOfAllComponents") Map<Integer, Map<String, List<Object>>> history){
		
		StringBuilder b = new StringBuilder(1024);
		b.append("Printing global history...\n");
		
		for (Integer id: history.keySet()) {

			b.append("\tComponent " + id + "\n");
			
			Map<String, List<Object>> componentHistory = history.get(id);
			for (String field : componentHistory.keySet()) {
				b.append("\t\t" + field + ":\t[");
				
				List<Object> values = componentHistory.get(field); 
				for (Object value : values) {
					b.append(value + ", ");
				}
				
				b.delete(b.length()-2, b.length());
				b.append("]\n");
			}
			
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
			@In("knowledgeHistoryOfAllComponents") Map<Integer, Map<String, List<Object>>> history){
		
		System.out.println("Correlation process started...");
		
	}
	
}
