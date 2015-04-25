package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

public class MultiThreadedSimulation {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, AnnotationProcessorException, InterruptedException, DEECoException {
		
		if (args.length != 1) {
			System.out.println("Wrong number of parameters!");
			return;
		}
		
		Settings.BROADCAST_PERIOD = Integer.parseInt(args[0]);
		System.out.println("Broadcast period: " + Settings.BROADCAST_PERIOD);
		
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
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		Settings.INACCURACY = 2000;
		result.append("-------------INACCURACY "+Settings.INACCURACY+"-------------\n");
		
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 1000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		Settings.INACCURACY = 5000;
		result.append("-------------INACCURACY "+Settings.INACCURACY+"-------------\n");
		
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 1000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		Settings.INACCURACY = 8000;
		result.append("-------------INACCURACY "+Settings.INACCURACY+"-------------\n");
		
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 1000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 3000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 5000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 8000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		FFSHelper.getInstance().reset();
		Settings.NETWORK_DELAY = 12000;
		MultiNodeSimulationOneTeam.main(null);
		result.append("Network Delay: " + Settings.NETWORK_DELAY + " - " + FFSHelper.getInstance().print() + "\n");
		
		System.out.println(result.toString());
	}

}
