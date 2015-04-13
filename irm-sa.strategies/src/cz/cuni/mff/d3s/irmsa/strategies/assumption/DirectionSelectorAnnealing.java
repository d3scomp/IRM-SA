package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import java.util.Set;
import java.util.function.BiFunction;

import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

/**
 * Sometimes picks not optimal direction.
 */
public class DirectionSelectorAnnealing extends DirectionSelectorImpl {

	/** Default acceptance function. */
	static public BiFunction<Double, Double, Double> DEFAULT_ACCEPTANCE =
			(newFit, oldFit) -> {
				return newFit > oldFit ? 1.0 : 0.25 - newFit / oldFit;
			};

	/** Returns acceptance probability for given fitnesses. */
	protected BiFunction<Double, Double, Double> acceptance = DEFAULT_ACCEPTANCE;

	/**
	 * Sets acceptance probability function.
	 * @param acceptance new acceptance probability function
	 * @return this
	 */
	public DirectionSelectorAnnealing withAcceptance(
			final BiFunction<Double, Double, Double> acceptance) {
		this.acceptance = acceptance;
		return this;
	}

	@Override
	public void adaptationImprovement(final double improvement,
			final Set<InvariantInfo<?>> infos) {
		for (InvariantInfo<?> info : infos) {
			if (AssumptionInstance.class.isAssignableFrom(info.clazz)) {
				final AssumptionInfo ai = (AssumptionInfo) info;
				final String id = ai.getParameterId();
				final AssumptionInfo last = adaptees.get(id);
				if (last == null) {
					continue;
				}
				if (Math.random() < acceptance.apply(info.fitness, last.fitness)) {
					//improvement
					directions.put(id, last.direction);
				} else {
					//worsening
					directions.put(id, last.direction.opposite());
				}
			}
		}
		//prepare next run
		reset();
	}
}
