package combined;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.KnowledgeMetadataHolder;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

@Component
@SystemComponent
public class EvaluationComponent {

	@Local
	private static final String DELIMITER = ";";
	@Local
	private static final String POSITION_LABEL = "position";
	@Local
	private static final String TEMPERATURE_LABEL = "temperature";
	
	@Local
	private static PrintWriter writer;
	
	@Local
	private static KnowledgeManager componentsKnowledge;

	/** Mandatory id field. */
	public String id;
	
	public EvaluationComponent(String id){
		this.id = id;
	}
	
	public static void init(ComponentInstance component){
		EvaluationComponent.componentsKnowledge = component.getKnowledgeManager();
		
		try {
			writer = new PrintWriter("EvaluationData.csv", "UTF-8");

			StringBuilder builder = new StringBuilder();
			builder.append("time").append(DELIMITER);
			builder.append("actual_temperature").append(DELIMITER);
			builder.append("belief_temperature").append(DELIMITER);
			builder.append("temperature_distance").append(DELIMITER);
			builder.append("actual_position").append(DELIMITER);
			builder.append("belief_position").append(DELIMITER);
			builder.append("position_distance").append("\n");
			writer.write(builder.toString());

		} catch (IOException e) {
			writer = null;
			Log.e("Can't open fitnessData.txt file", e);
		}
	}
	
	public static void finit(){
		if(writer != null){
			writer.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Process
	@PeriodicScheduling(period=50)
	public static void gatherEvaluationData(){
		if(EvaluationComponent.componentsKnowledge != null
				&& writer != null){
		try {
			// Extract components ID, Temperature and Position
			final List<KnowledgePath> knowledgePaths = new LinkedList<>();
			KnowledgePath componentIdPath;
				componentIdPath = KnowledgePathHelper.createKnowledgePath("id", PathOrigin.COMPONENT);
				knowledgePaths.add(componentIdPath);
				final KnowledgePath componentTempPath = 
						KnowledgePathHelper.createKnowledgePath(TEMPERATURE_LABEL, PathOrigin.COMPONENT);
				knowledgePaths.add(componentTempPath);
				final KnowledgePath componentPosPath =
						KnowledgePathHelper.createKnowledgePath(POSITION_LABEL, PathOrigin.COMPONENT);
				knowledgePaths.add(componentPosPath);
				
				final ValueSet knowledgeValues = componentsKnowledge.get(knowledgePaths);
	
				final String componentId = (String) knowledgeValues.getValue(componentIdPath);
				final MetadataWrapper<Double> componentTemperature = 
						(MetadataWrapper<Double>) knowledgeValues.getValue(componentTempPath);
				final MetadataWrapper<Location> componentPosition = 
						(MetadataWrapper<Location>) knowledgeValues.getValue(componentPosPath);
	
				// time
				final long time = ProcessContext.getTimeProvider().getCurrentMilliseconds();
				// actual temperature
				final double actualTemperature = Environment.getRealTemperature(componentId);
				// belief temperature
				final double beliefTemperature = componentTemperature.getValue();
				// actual position
				final Location actualPosition = Environment.getRealPosition(componentId).toPositionComponent();
				// belief position
				final Location beliefPosition = componentPosition.getValue();
				
				// temperature distance
				final double temperatureDistance = KnowledgeMetadataHolder.distance(
						TEMPERATURE_LABEL, actualTemperature, beliefTemperature);
				// position distance
				final double positionDistance = KnowledgeMetadataHolder.distance(
						POSITION_LABEL, actualPosition, beliefPosition);
				
				StringBuilder builder = new StringBuilder();
				// time
				builder.append(time).append(DELIMITER);
				// actual temperature
				builder.append(actualTemperature).append(DELIMITER);
				// belief temperature
				builder.append(beliefTemperature).append(DELIMITER);
				// temperature distance
				builder.append(temperatureDistance).append(DELIMITER);
				// actual position
				builder.append(actualPosition).append(DELIMITER);
				// belief position
				builder.append(beliefPosition).append(DELIMITER);
				// position distance
				builder.append(positionDistance).append("\n");
	
				writer.write(builder.toString());

			} catch (ParseException | AnnotationProcessorException | KnowledgeNotFoundException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
