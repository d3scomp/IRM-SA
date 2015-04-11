package cz.cuni.mff.d3s.irmsa.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.irmsa.AdaptationListener;
import cz.cuni.mff.d3s.irmsa.IRMPlugin;

public class MetaAdaptationPlugin implements DEECoPlugin {

	/** Plugin dependencies. */
	static private List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Collections.unmodifiableList(Arrays.asList(IRMPlugin.class));

	/** IRMPlugin to listen to. */
	protected final IRMPlugin irmPlugin;

	/** Managed managers. */
	protected final List<AdaptationManager> managers = new ArrayList<>();

	/**
	 * Only constructor.
	 * @param irmPlugin IRMPlugin to listen to
	 */
	public MetaAdaptationPlugin(final IRMPlugin irmPlugin) {
		this.irmPlugin = irmPlugin;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	@Override
	public void init(final DEECoContainer container) {
		try {
			final MetaAdaptationManager manager = new MetaAdaptationManager();
			container.deployComponent(manager);
			// pass necessary data to the MetaAdaptationManager
			for (ComponentInstance c : container.getRuntimeMetadata().getComponentInstances()) {
				if (c.getName().equals(manager.getClass().getName())) {
					final EMap<String, Object> data = c.getInternalData();
					data.put(MetaAdaptationManager.MANAGED_MANAGERS, managers);
					//
					irmPlugin.registerListener(new AdaptationListener() {
						@Override
						public boolean canIRMRun() {
							final Boolean canRun = (Boolean) data.get(MetaAdaptationManager.IRM_CAN_RUN);
							return canRun == null || canRun;
						}
						@Override
						public void adaptationResult(int solutions, int total) {
							data.put(MetaAdaptationManager.RUN_FLAG, solutions == 0);
							if (solutions == 0) {
								data.put(MetaAdaptationManager.IRM_CAN_RUN, false);
							}
						}
					});
				}
			}
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy MetaAdaptationManager", e);
		}
	}

	/**
	 * Registers AdaptationManager to manage.
	 * @param manager manager to register
	 */
	public void registerManager(final AdaptationManager manager) {
		managers.add(manager);
		manager.stop(); //make them not run
	}

	/**
	 * Unregisters AdaptationManager to manage.
	 * @param manager manager to unregister
	 */
	public void unregisterManager(final AdaptationManager manager) {
		managers.remove(manager);
	}
}
