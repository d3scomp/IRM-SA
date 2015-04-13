package simulation;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;

public class SimpleSimulator implements DEECoPlugin, SimulationTimer {

	private long currentMilliseconds;
	private final long simulationStartTime;
	private final long simulationEndTime;
	private final TreeSet<Callback> callbacks;
	
	public SimpleSimulator(long simulationStartTime, long simulationEndTime) {
		this.simulationStartTime = simulationStartTime;
		this.simulationEndTime = simulationEndTime;
		this.currentMilliseconds = -1;
		this.callbacks = new TreeSet<>();
	}
	
	@Override
	public void init(DEECoContainer container) {
		//Do nothing
	}

	@Override
	public void notifyAt(long time, TimerEventListener listener,
			DEECoContainer node) {
		callbacks.add(new Callback(listener, time));
	}

	@Override
	public void start(long duration) {
		currentMilliseconds = simulationStartTime;
		Callback callback;
		// Iterate through all the callbacks until the MATSim callback.
		while (!callbacks.isEmpty()) {
			callback = callbacks.pollFirst();
			currentMilliseconds = callback.callbackTime;
			if (currentMilliseconds > simulationEndTime) {
				break;
			}
			//System.out.println("At: " + currentMilliseconds);
			callback.listener.at(currentMilliseconds);
		}
		
	}
	
	@Override
	public long getCurrentMilliseconds() {
		return currentMilliseconds;
	}
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	
	private class Callback implements Comparable<Callback> {

		public final long callbackTime;
		public final TimerEventListener listener;

		public Callback(TimerEventListener listener, long milliseconds) {
			this.listener = listener;
			this.callbackTime = milliseconds;
		}

		@Override
		public int compareTo(Callback c) {
			if (c.callbackTime < callbackTime) {
				return 1;
			} else if (c.callbackTime > callbackTime) {
				return -1;
			} else if (this == c) {
				return 0;
			} else {
				return this.hashCode() < c.hashCode() ? 1 : -1;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((listener == null) ? 0 : listener.hashCode());
			result = prime * result
					+ (int) (callbackTime ^ (callbackTime >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Callback other = (Callback) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (listener == null) {
				if (other.listener != null)
					return false;
			} else if (!listener.equals(other.listener))
				return false;
			if (callbackTime != other.callbackTime)
				return false;
			return true;
		}

		private SimpleSimulator getOuterType() {
			return SimpleSimulator.this;
		}
	}

}
