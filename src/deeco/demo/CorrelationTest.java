package deeco.demo;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class CorrelationTest {
	
	public static void main(String[] args) throws DEECoException, AnnotationProcessorException {

		CorrelationPlugin correlationPlugin = new CorrelationPlugin();
		
		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		
		DEECoNode deeco = realm.createNode(correlationPlugin);
				
		/* deploy components and ensembles */
		deeco.deployComponent(new GroupMember("1"));
		deeco.deployComponent(new GroupMember("2"));
		deeco.deployComponent(new GroupLeader("3"));
		
		/* decentralized case */
//		DEECoNode deeco = realm.createNode(); 
//		deeco.deployComponent(new GroupMember("1"));
//		deeco.deployComponent(new GroupMember("2"));
//		deeco.deployComponent(new GroupLeader("3"));
//		DEECoNode correlationNode = realm.createNode(correlationPlugin);
		
		/* WHEN simulation is performed */
		realm.start(1000);
		
	}	
	
	public class Variances {
		static final int SMALL_VARIANCE = 10;
		static final int LARGE_VARIANCE = 100;
	}

}
