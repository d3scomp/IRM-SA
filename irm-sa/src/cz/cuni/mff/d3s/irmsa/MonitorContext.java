package cz.cuni.mff.d3s.irmsa;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

/**
 * A class providing reflective capabilities to monitors.
 * TODO add monitored ensemble too
 */
public class MonitorContext {

	/** Thread singleton. */
	private static ThreadLocal<MonitorContext> context = new ThreadLocal<>();

	/**
	 * @return the monitoredComponent
	 */
	static public ComponentInstance getMonitoredComponent() {
		final MonitorContext c = context.get();
		if (c != null) {
			return c.monitoredComponent;
		} else {
			return null;
		}
	}

	static void setMonitoredComponent(final ComponentInstance monitoredComponent) {
		MonitorContext c = context.get();
		if (c == null) {
			c = new MonitorContext();
			context.set(c);
		}
		c.monitoredComponent = monitoredComponent;
	}

	/** Monitored component. */
	private ComponentInstance monitoredComponent;
}
