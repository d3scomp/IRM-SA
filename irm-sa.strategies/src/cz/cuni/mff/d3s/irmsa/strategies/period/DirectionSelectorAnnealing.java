package cz.cuni.mff.d3s.irmsa.strategies.period;

import java.util.Set;
import java.util.function.BiFunction;

import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
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
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
				final String id = PeriodAdaptationManagerDelegate.getProcessInvariantInstanceId(pii);
				final InvariantInfo<ProcessInvariantInstance> last = processAdaptees.get(id);
				if (last == null) {
					continue;
				}
				if (Math.random() < acceptance.apply(info.fitness, last.fitness)) {
					//improvement
					processDirections.put(id, last.direction);
				} else {
					//worsening
					processDirections.put(id, last.direction.opposite());
				}
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii = info.getInvariant();
				final String id = PeriodAdaptationManagerDelegate.getExchangeInvariantInstanceId(xii);
				final InvariantInfo<ExchangeInvariantInstance> last = exchangeAdaptees.get(id);
				if (last == null) {
					continue;
				}
				if (Math.random() < acceptance.apply(info.fitness, last.fitness)) {
					//improvement
					exchangeDirections.put(id, last.direction);
				} else {
					//worsening
					exchangeDirections.put(id, last.direction.opposite());
				}
			}
		}
		//prepare next run
		reset();
	}
}
