package cz.cuni.mff.d3s.deeco.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.monitoring.Monitor;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;

public class MonitorProviderImpl implements MonitorProvider {

	private final Map<String, Monitor> monitors;

	public MonitorProviderImpl() {
		this.monitors = new HashMap<String, Monitor>();
	}

	@Override
	public Monitor getMonitor(String monitorId) {
		return monitors.get(monitorId);
	}

	@Override
	public void addMonitor(Monitor monitor) {
		if (!monitors.containsKey(monitor.getId()))
			monitors.put(monitor.getId(), monitor);
	}

	@Override
	public void addAllMonitors(List<? extends Monitor> monitors) {
		for (Monitor m : monitors)
			addMonitor(m);
	}

}
