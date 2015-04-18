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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.AdapteeSelector;

public class AdapteeSelectorFitness implements AdapteeSelector {

	/** Numbers differing in only this value are considered equal. */
	static private final double TOLERANCE = 1e-9;

	/**
	 * Returns whether given values are equal considering TOLERANCE.
	 * @param val1 value 1
	 * @param val2 value 2
	 * @return true if given values are equal considering TOLERANCE
	 */
	static private boolean toleranceEqual(final double val1, final double val2) {
		return Math.abs(val1 - val2) <= TOLERANCE;
	}

	@Override
	public Set<InvariantInfo<?>> selectAdaptees(
			final Collection<InvariantInfo<?>> infos) {
		final Set<InvariantInfo<?>> result = new HashSet<>();
		double fitness = Double.MAX_VALUE; //fitness of invariants in result
		for (InvariantInfo<?> invariant: infos) {
			if (invariant.fitness < fitness) {
				result.clear();
				fitness = invariant.fitness;
				if (AssumptionInstance.class.isAssignableFrom(invariant.clazz)) {
					//use process or  exchange siblings instead of assumptions
					final InvariantInstance parent = invariant.getInvariant().getParent();
					if (parent == null) {
						continue;
					}
					for (InvariantInfo<?> ii : infos) {
						if (parent.equals(ii.getInvariant().getParent())
								&& !AssumptionInstance.class.isAssignableFrom(ii.clazz)) {
							result.add(ii);
						}
					}
				} else {
					result.add(invariant);
				}
			} else if (toleranceEqual(invariant.fitness, fitness)) {
				result.add(invariant);
			}
		}
		return result;
	}
}
