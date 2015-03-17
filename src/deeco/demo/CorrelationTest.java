package deeco.demo;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.BroadcastLoopback;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DummyKnowledgePublisher;
/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class CorrelationTest {
	
	public static void main(String[] args) throws DEECoException, AnnotationProcessorException, InstantiationException, IllegalAccessException {

		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DummyKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		DEECoNode deeco1 = realm.createNode();
		deeco1.deployComponent(new GroupMember("1"));
		
		DEECoNode deeco2 = realm.createNode();
		deeco2.deployComponent(new GroupMember("2"));
		
		DEECoNode deeco3 = realm.createNode();
		deeco3.deployComponent(new GroupLeader("3"));
		
		/* Create node that holds the correlation component */
		realm.createNode(new CorrelationPlugin());
		
		/* WHEN simulation is performed */
		realm.start(10000);
		
	}	
	
	public class Variances {
		static final int SMALL_VARIANCE = 10;
		static final int LARGE_VARIANCE = 100;
	}

}
