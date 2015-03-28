package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import java.lang.reflect.Parameter;

import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

/**
 * Wraps Assumption and one of its monitor's parameter.
 */
public class AssumptionInfo extends InvariantInfo<AssumptionInstance> {

	/**
	 * Factory method to wrap given AssumptionInstance.
	 * @param processInvariant AssumptionInstance to wrap
	 * @param monitor invariant monitor of the assumption
	 * @param parameter adapted parameter
	 * @return newly created InvariantInfo wrapping given AssumptionInstance
	 */
	static public  AssumptionInfo create(final AssumptionInstance assumption,
			final InvariantMonitor monitor, final Parameter parameter) {
		return new AssumptionInfo(assumption, AssumptionInstance.class,
				monitor, parameter);
	}

	/** Monitor for the assumption. */
	public final InvariantMonitor monitor;

	/** Adapted parameter. */
	public final Parameter parameter;

	/**
	 * Only constructor.
	 * @param invariant assumption to wrap
	 * @param clazz assumption instance class
	 * @param monitor assumption monitor
	 * @param parameter wrapped parameter
	 */
	protected AssumptionInfo(final AssumptionInstance invariant,
			final Class<AssumptionInstance> clazz, final InvariantMonitor monitor,
			final Parameter parameter) {
		super(invariant, clazz);
		this.monitor = monitor;
		this.parameter = parameter;
	}
}
