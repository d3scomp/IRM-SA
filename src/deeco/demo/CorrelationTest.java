package deeco.demo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.BroadcastLoopback;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;
import deeco.metadata.CorrelationLevel.DistanceClass;
import deeco.metadata.KnowledgeMetadataHolder;
import deeco.metadata.MetadataWrapper;
/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class CorrelationTest {
	
	public static void main(String[] args) throws DEECoException, AnnotationProcessorException, InstantiationException, IllegalAccessException {

		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new BroadcastLoopback());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		
		DEECoNode deeco1 = realm.createNode(1);
		deeco1.deployComponent(new GroupMember("1"));
		
		DEECoNode deeco2 = realm.createNode(2);
		deeco2.deployComponent(new GroupMember("2"));
		
		DEECoNode deeco3 = realm.createNode(3);
		deeco3.deployComponent(new GroupLeader("3"));
		
		/* Create node that holds the correlation component */
		realm.createNode(4, new CorrelationPlugin());
		
		/* WHEN simulation is performed */
		realm.start(10000);
		
/*		try{
			CorrelationEnsembleFactory cef = new CorrelationEnsembleFactory();
			Class testClass = cef.getEnsembleDefinition("position", "temperature");
			System.out.println(String.format("Class: %s", testClass.toGenericString()));
			for(Annotation a : testClass.getAnnotations()){
				System.out.println(String.format("Annotation: %s", a.toString()));
			}
			Method[] methods = testClass.getMethods();
			for(Method m : methods){
				System.out.println(String.format("\nMethod: %s", m.toString()));

				for(Annotation a : m.getAnnotations()){
					System.out.println(String.format("Annotation: %s", a.toString()));
				}
				
				for(Parameter p : m.getParameters()){
					System.out.println(String.format("Parameter: %s", p.getName()));

					for(Annotation a : p.getAnnotations()){
						System.out.println(String.format("Annotation: %s", a.toString()));
					}
				}
			}

			MetadataWrapper<Integer> memberPosition = new MetadataWrapper<Integer>(0);
			MetadataWrapper<Integer> memberTemperature = new MetadataWrapper<Integer>(0);
			MetadataWrapper<Integer> coordPosition = new MetadataWrapper<Integer>(0);
			MetadataWrapper<Integer> coordTemperature = new MetadataWrapper<Integer>(0);
			memberTemperature.malfunction();
			Method m = testClass.getMethod("membership", new Class[]{
					MetadataWrapper.class,
					MetadataWrapper.class,
					MetadataWrapper.class,
					MetadataWrapper.class});
			boolean ret = (boolean) m.invoke(testClass.newInstance(), new Object[]{
				memberPosition,
				memberTemperature,
				coordPosition,
				coordTemperature});
			System.out.println(ret);
			System.out.println(!memberTemperature.isOperational()
					&& coordTemperature.isOperational()
					&& KnowledgeMetadataHolder.classifyDistance("position", memberPosition.getValue(), coordPosition.getValue()) == DistanceClass.Close);
		} catch(Exception e){
			e.printStackTrace();
		}*/
		
	}	
	
	public class Variances {
		static final int SMALL_VARIANCE = 10;
		static final int LARGE_VARIANCE = 100;
	}

}
