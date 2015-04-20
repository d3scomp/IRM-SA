package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

public class Simulation {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, AnnotationProcessorException, InterruptedException, DEECoException {
		StringBuilder result = new StringBuilder();
		
//		// Baseline - all components on a single node
//		SingleNodeSimulation.main(null);
//		result.append("-------------BSELINE-------------\n");
//		result.append(FFSHelper.getInstance().print() + "\n");
			result.append("-------------NO INACCURACY THREASHOLD-------------\n");
			FFSHelper.getInstance().reset();
			Settings.NETWORK_DELAY = 500;
			MultiNodeSimulation.main(null);
			result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
//		FFSHelper.getInstance().reset();
//		Settings.NETWORK_DELAY = 1000;
//		MultiNodeSimulation.main(null);
//		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
//		FFSHelper.getInstance().reset();
//		Settings.NETWORK_DELAY = 1500;
//		MultiNodeSimulation.main(null);
//		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
//		FFSHelper.getInstance().reset();
//		Settings.NETWORK_DELAY = 2000;
//		MultiNodeSimulation.main(null);
//		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
//		FFSHelper.getInstance().reset();
//		Settings.NETWORK_DELAY = 2500;
//		MultiNodeSimulation.main(null);
//		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
//		
		System.out.println(result.toString());
	}

}
