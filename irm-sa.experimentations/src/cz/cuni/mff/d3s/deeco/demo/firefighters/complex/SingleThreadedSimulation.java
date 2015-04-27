package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

public class SingleThreadedSimulation {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, AnnotationProcessorException, InterruptedException, DEECoException {
		
		System.setProperty(DeecoProperties.PUBLISHING_PERIOD, Integer.toString(Settings.BROADCAST_PERIOD));
		StringBuilder result = new StringBuilder();
		
		// Baseline - all components on a single node
		SingleNodeSimulation.main(null);
		result.append("-------------BSELINE-------------\n");
		result.append("Centralized: " + FFSHelper.getInstance().print() + "\n");
		result.append("-------------NO INACCURACY THREASHOLD-------------\n");
		Settings.INACCURACY = Long.MAX_VALUE;
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 1000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		Settings.INACCURACY = 1000;
		result.append("-------------INACCURACY "+Settings.INACCURACY+"-------------\n");
		
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 1000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		Settings.INACCURACY = 2000;
		result.append("-------------INACCURACY "+Settings.INACCURACY+"-------------\n");
		
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 1000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		Settings.INACCURACY = 3000;
		result.append("-------------INACCURACY "+Settings.INACCURACY+"-------------\n");
		
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 1000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulation.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		System.out.println(result.toString());
	}

}
