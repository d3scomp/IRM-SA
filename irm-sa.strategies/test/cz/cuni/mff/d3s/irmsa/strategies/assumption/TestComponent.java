package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter;
import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter.Scope;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;

@Component
@IRMComponent("TestComponent")
public class TestComponent {

	/** Mandatory Knowledge. */
	public String id;

	/** Knowledge to be assumed. */
	public Integer testKnowledge = 10;


	/**
	 * Only constructor.
	 */
	public TestComponent() {
		//nothing
	}

	@InvariantMonitor("A01")
	public static boolean testSatisfactionMonitor(
			@In("testKnowledge") Integer testKnowledge,
			@AssumptionParameter(name = "param", defaultValue = 20, minValue = 0, maxValue = 100, scope = Scope.COMPONENT)
			int param
	) {
		return param < testKnowledge;
	}

	@InvariantMonitor("A01")
	public static double testFitnessMonitor(
			@In("testKnowledge") Integer testKnowledge,
			@AssumptionParameter(name = "param", defaultValue = 20, minValue = 0, maxValue = 100, scope = Scope.COMPONENT)
			int param
	) {
		return param < testKnowledge ? 1.0 : 0.0;
	}
}
