/*******************************************************************************
 * Copyright 2014 Charles University in Prague
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.irmsa.strategies.period;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irmsa.strategies.commons.Direction;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DirectionSelector;

/**
 * Temporary direction selector that does nothing.
 */
public class DirectionSelectorImpl implements DirectionSelector {

	/** Processes adapted in the last run. */
	final protected Map<String, InvariantInfo<ProcessInvariantInstance>> processAdaptees = new HashMap<>();

	/** Exchanges adapted in the last run. */
	final protected Map<String, InvariantInfo<ExchangeInvariantInstance>> exchangeAdaptees = new HashMap<>();

	/** Suitable directions for individual ProcessInvariantInstances. */
	final protected Map<String, Direction> processDirections = new HashMap<>();

	/** Suitable directions for individual ExchangeInvariantInstances. */
	final protected Map<String, Direction> exchangeDirections = new HashMap<>();

	@Override
	public void selectDirection(InvariantInfo<?> info) {
		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ProcessInvariantInstance pii = info.getInvariant();
			final String id = PeriodAdaptationManagerDelegate.getProcessInvariantInstanceId(pii);
			Direction direction = processDirections.get(id);
			if (direction == null) {
				direction = Direction.DOWN;
			}
			info.direction = direction;
			@SuppressWarnings("unchecked")
			final InvariantInfo<ProcessInvariantInstance> pi =
					(InvariantInfo<ProcessInvariantInstance>) info;
			processAdaptees.put(id, pi);
		} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ExchangeInvariantInstance xii = info.getInvariant();
			final String id = PeriodAdaptationManagerDelegate.getExchangeInvariantInstanceId(xii);
			Direction direction = exchangeDirections.get(id);
			if (direction == null) {
				direction = Direction.DOWN;
			}
			info.direction = direction;
			@SuppressWarnings("unchecked")
			final InvariantInfo<ExchangeInvariantInstance> xi =
					(InvariantInfo<ExchangeInvariantInstance>) info;
			exchangeAdaptees.put(id, xi);
		}
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
				if (info.fitness > last.fitness) {
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
				if (info.fitness > last.fitness) {
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

	/**
	 * Resets state for next run.
	 */
	protected void reset() {
		processAdaptees.clear();
		exchangeAdaptees.clear();
	}
}
