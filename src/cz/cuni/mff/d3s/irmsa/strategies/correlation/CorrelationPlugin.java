package cz.cuni.mff.d3s.irmsa.strategies.correlation;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;

public class CorrelationPlugin implements DEECoPlugin {

	List<Class<? extends DEECoPlugin>> dependencies = new ArrayList<>();

	private final List<DEECoNode> deecoNodes;

	public CorrelationPlugin(List<DEECoNode> nodesInRealm){
		deecoNodes = nodesInRealm;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// This plugin has no dependencies other than jDEECo core
		return dependencies;
	}

	@Override
	public void init(DEECoContainer container) {
		try {
			container.deployComponent(new CorrelationManager(deecoNodes));
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
