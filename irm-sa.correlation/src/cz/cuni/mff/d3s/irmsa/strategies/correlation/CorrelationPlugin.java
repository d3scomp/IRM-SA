package cz.cuni.mff.d3s.irmsa.strategies.correlation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.MetaAdaptationPlugin;

public class CorrelationPlugin implements DEECoPlugin {

	/** Plugin dependencies. */
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Collections.unmodifiableList(Arrays.asList(MetaAdaptationPlugin.class));

	private final List<DEECoNode> deecoNodes;

	/** MetaAdaptationPlugin managing this plugin. */
	private final MetaAdaptationPlugin metaAdaptationPlugin;

	public CorrelationPlugin(final MetaAdaptationPlugin metaAdaptationPlugin,
			List<DEECoNode> nodesInRealm){
		deecoNodes = nodesInRealm;
		this.metaAdaptationPlugin = metaAdaptationPlugin;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	@Override
	public void init(DEECoContainer container) {
		try {
			final CorrelationManager manager = new CorrelationManager(deecoNodes);
			container.deployComponent(manager);
			//register correlation manager to meta manager
			for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
				if (c.getName().equals(manager.getClass().getName())) {
					manager.setDeecoComponent(c);
					metaAdaptationPlugin.registerManager(manager);
				}
			}
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
