package deeco.demo;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;

public class CorrelationPlugin implements DEECoPlugin {

	List<Class<? extends DEECoPlugin>> dependencies = new ArrayList<>();
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// This plugin has no dependencies other than jDEECo core
		return dependencies;
	}

	@Override
	public void init(DEECoContainer container) {
		try {
			container.deployComponent(new DEECoCorrelationManager());
			// FIXME these two ensembles could be unified if we implement the "member.*" in knowledge exchange parameters 
			container.deployEnsemble(GroupMemberDataAggregation.class);
			container.deployEnsemble(GroupLeaderDataAggregation.class);
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		} 
		catch (DuplicateEnsembleDefinitionException e) {
			e.printStackTrace();
		}		
	}

}
