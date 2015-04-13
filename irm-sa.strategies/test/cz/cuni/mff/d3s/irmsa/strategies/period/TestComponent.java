package cz.cuni.mff.d3s.irmsa.strategies.period;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

@Component
@IRMComponent("TestComponent")
public class TestComponent {

	/** Mandatory knowledge. */
	public String id;

	/** Method testProcessInvarint was last called at this time. */
	public Long time = 0L;

	/**
	 * Only constructor.
	 */
	public TestComponent() {
		//nothing
	}

	@Process
	@Invariant("I01")
	@PeriodicScheduling(period = 5000, order = 99)
	public static void testProcessInvarint(
		@In("id") String id,
		@Out("time") ParamHolder<Long> time
	) {
		time.value = ProcessContext.getTimeProvider().getCurrentMilliseconds();
	}

	@InvariantMonitor("I01")
	public static boolean invariantSatisfactionMonitor(
			@In("time") Long time) {
		return time + 5000L > ProcessContext.getTimeProvider().getCurrentMilliseconds();
	}

	@InvariantMonitor("I01")
	public static double invariantFitnessMonitor(
			@In("time") Long time) {
		return invariantSatisfactionMonitor(time) ? 1.0 : 0.0;
	}
}
