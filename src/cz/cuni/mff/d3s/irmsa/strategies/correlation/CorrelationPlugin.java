package cz.cuni.mff.d3s.irmsa.strategies.correlation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.irmsa.strategies.AdaptationManager;
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
					final EMap<String, Object> data = c.getInternalData();
					metaAdaptationPlugin.registerManager(new AdaptationManager() {
						@Override
						public void stop() {
							data.put(CorrelationManager.RUN_FLAG, false);
						}
						@Override
						public void run() {
							data.put(CorrelationManager.RUN_FLAG, true);
							data.put(CorrelationManager.DONE_FLAG, false);
						}
						@Override
						public boolean isDone() {
							final Boolean result = (Boolean) data.get(CorrelationManager.DONE_FLAG);
							return result == null || result;
						}
					});
				}
			}
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
