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
package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter;
import cz.cuni.mff.d3s.irmsa.strategies.commons.Direction;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DirectionSelector;

/**
 * Temporary direction selector that does nothing.
 */
public class DirectionSelectorImpl implements DirectionSelector {

	/** Assumptions adapted in the last run. */
	final protected Map<String, AssumptionInfo> adaptees = new HashMap<>();

	/** Suitable directions for individual ProcessInvariantInstances. */
	final protected Map<String, Direction> directions = new HashMap<>();

	@Override
	public void selectDirection(InvariantInfo<?> ii) {
		if (ii instanceof AssumptionInfo) {
			final AssumptionInfo info = (AssumptionInfo) ii;
			final String id = info.getParameterId();
			Direction direction = directions.get(id);
			if (direction == null) {
				AssumptionParameter param = info.parameter.getAnnotation(AssumptionParameter.class);
				if (param.initialDirection() == AssumptionParameter.Direction.DOWN) {
					direction = Direction.DOWN;
				} else {
					direction = Direction.UP;
				}
			}
			info.direction = direction;
			adaptees.put(id, info);
		}
	}

	@Override
	public void adaptationImprovement(final double improvement,
			final Set<InvariantInfo<?>> infos) {
		for (InvariantInfo<?> ii : infos) {
			if (ii instanceof AssumptionInfo) {
				final AssumptionInfo info = (AssumptionInfo) ii;
				final String id = info.getParameterId();
				final AssumptionInfo last = adaptees.get(id);
				if (last == null) {
					continue;
				}
				if (info.fitness > last.fitness) {
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

	/**
	 * Resets state for next run.
	 */
	protected void reset() {
		adaptees.clear();
	}
}
